package com.example.avilaretamal.qucocinamoshoy;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class Inicio extends AppCompatActivity{
    Boolean ok = false;
    String[] CategoriesNames = null;
    Integer[] CategoriesIds = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        GridView vista = (GridView) findViewById(R.id.categorias);

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserData.RemoveToken(getApplicationContext());
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
        );

        try {
            Boolean aux = new ExternalLoginTask(getApplicationContext()).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.v("BBBBBB", "CategoriesNames es null");
        if (CategoriesNames != null) {
            Log.v("AAAAAAAAAA", "no es nullllll");
            vista.setAdapter(new CategoryAdapter(this, CategoriesNames));
            vista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getApplicationContext(),((TextView) view.findViewById(R.id.category_name)).getText(), Toast.LENGTH_SHORT).show();
                    startActivity((new Intent(getApplicationContext(), CategoryProductsActivity.class)).putExtra("category_id", CategoriesIds[position]));
                    finish();
                }
            });
        }
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

            URL url = null;
            try {
                url = new URL(URIdata.BASE_URL + URIdata.CATEGORIES);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                connection = (HttpURLConnection)url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                connection.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", "Token token=" + UserData.GetToken(MainContext));

            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            try {
                connection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);

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
                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            int response_code = 0;
            try {
                response_code = connection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.length() != 0 && response_code == 200) {
                CategoriesNames = RecipeData.GetCategoriesNames(response.toString());
                CategoriesIds = RecipeData.GetCategoriesIds(response.toString());
                ok = true;
            }
            try {
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if(connection != null) {
                    connection.disconnect();
                }
            }
            System.out.println("Http request response '{}': "+response.toString());
            Log.v("RESPONSE", response.toString());



            return true;
        }

        /*@Override
        protected void onPostExecute(Boolean aBoolean) {
            if (ok) { //cambiar
                startActivity(new Intent(MainContext, Inicio.class));
                finish();
            }
        }*/
    }
}
