package com.example.avilaretamal.qucocinamoshoy;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;

import java.util.HashMap;
import java.util.Map;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.RequestQueue;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.BufferedWriter;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    String email;
    String password;
    Boolean log = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (UserData.GetToken(getApplicationContext()) != null) {
            startActivity(new Intent(getApplicationContext(), Inicio.class));
            finish();
        }

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.d("ALERTA", "ESTA ONCLICK\n");
                        email = ((EditText) findViewById(R.id.email)).getText().toString();
                        password = ((EditText) findViewById(R.id.password)).getText().toString();

                        if (email != "" && password != "") {
                            try {
                                Boolean aux = new MainActivity.ExternalLoginTask(getApplicationContext()).execute().get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                                e.printStackTrace();
                            }
                        }
                        if (!log) {
                            Toast.makeText(getApplicationContext(), "Credenciales incorrectas.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public class ExternalLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final Context MainContext;

        ExternalLoginTask(Context context){
            MainContext = context;
        }

        @Override
        protected void onPreExecute(){
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpURLConnection connection = null;
            String urlParameters="email="+email+"&password="+password;

            URL url = null;
            try {
                url = new URL(URIdata.BASE_URL + URIdata.LOGIN);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                connection = (HttpURLConnection)url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                connection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            try {
                connection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);


            // Send request
            DataOutputStream wr = null;
            try {
                wr = new DataOutputStream(connection.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (wr != null) {
                    wr.writeBytes(urlParameters);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                wr.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int response_code = 0;
            try {
                response_code = connection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response_code == 200) {
                // Get Response
                InputStream is = null;
                try {
                    is = connection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                try {
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.length() != 0 && response_code == 200) {
                    UserData.RegisterToken(MainContext, response.toString());
                    log = true;
                }
                try {
                    rd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                connection.disconnect();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (log) {
                startActivity(new Intent(MainContext, Inicio.class));
                finish();
            }
        }
    }
}