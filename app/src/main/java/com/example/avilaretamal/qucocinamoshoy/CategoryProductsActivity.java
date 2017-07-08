package com.example.avilaretamal.qucocinamoshoy;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class CategoryProductsActivity extends AppCompatActivity{
    String CategoryId = null;
    Boolean ok = false;
    String[] RecipesNames = null;
    Integer[] RecipesIds = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);

        Bundle aux = getIntent().getExtras();
        if (aux != null) {
            CategoryId = String.valueOf(aux.getInt("category_id"));
        }
        GridView vista = (GridView) findViewById(R.id.recetas);

        if (CategoryId != null) {
            try {
                Boolean aux2 = new CategoryProductsActivity.ExternalLoginTask(getApplicationContext()).execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
                e.printStackTrace();
            }

            TextView anuncio = (TextView) findViewById(R.id.test);
            if (RecipesIds != null) {
                anuncio.setVisibility(View.GONE);
                vista.setAdapter(new RecipeAdapter(this, RecipesNames, RecipesIds));
                vista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getApplicationContext(),((TextView) view.findViewById(R.id.category_name)).getText(), Toast.LENGTH_SHORT).show();
                        startActivity(((new Intent(getApplicationContext(), RecipeActivity.class)).putExtra("recipe_id", RecipesIds[position])).putExtra("category_id", Integer.parseInt(CategoryId)));
                        finish();
                    }
                });
            } else {
                anuncio.setText("Uups, aun no hay recetas en esta categoría.");
            }
        }
        /*TextView test = (TextView) findViewById(R.id.test);
        test.setText("Hola: " + CategoryId);*/
        //Log.v("Valoooor: ", "categoryId");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Inicio.class));
        super.onBackPressed();
        finish();
    }

    public class ExternalLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final Context MainContext;

        ExternalLoginTask(Context context) {
            MainContext = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpURLConnection connection = null;

            URL url = null;
            try {
                url = new URL(URIdata.BASE_URL + URIdata.CATEGORY_RECIPES + CategoryId + ".json");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                connection = (HttpURLConnection) url.openConnection();
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
            connection.setUseCaches(false);
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
                while ((line = rd.readLine()) != null) {
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
                RecipesNames = RecipeData.GetCategoriesNames(response.toString());
                RecipesIds = RecipeData.GetCategoriesIds(response.toString());
                ok = true;
            }
            try {
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }
            System.out.println("Http request response '{}': " + response.toString());
            Log.v("RESPONSE", response.toString());


            return true;
        }
    }
}
