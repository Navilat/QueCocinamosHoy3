package com.example.avilaretamal.qucocinamoshoy;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class RecipeActivity extends AppCompatActivity {
    String RecipeId = null;
    Integer CategoryId = null;
    String RecipeName = null;
    String RecipeIngredients = null;
    String RecipeProcedure = null;
    String UserName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Bundle aux = getIntent().getExtras();
        if (aux != null) {
            RecipeId = String.valueOf(aux.getInt("recipe_id"));
            CategoryId = aux.getInt("category_id");

            if (RecipeId != null) {
                ImageView image = (ImageView) findViewById(R.id.recipe_image);
                String PACKAGE_NAME = getApplicationContext().getPackageName();
                int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/r" + RecipeId , null, null);
                image.setImageBitmap(BitmapFactory.decodeResource(getResources(), imgId));

                try {
                    Boolean aux2 = new ExternalLoginTask(getApplicationContext()).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }

                TextView recipe_name = (TextView) findViewById(R.id.recipe_name);
                recipe_name.setText(RecipeName);
                TextView user_name = (TextView) findViewById(R.id.user_name);
                user_name.setText(UserName);

                TextView ingredients = (TextView) findViewById(R.id.ingredients);
                ingredients.setText(RecipeIngredients);
                TextView procedure = (TextView) findViewById(R.id.procedure);
                procedure.setText(RecipeProcedure);

            }
        }

    }

    @Override
    public void onBackPressed() {
        startActivity((new Intent(getApplicationContext(), CategoryProductsActivity.class)).putExtra("category_id", CategoryId));
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
                url = new URL(URIdata.BASE_URL + URIdata.RECIPE + RecipeId + ".json");
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
                RecipeName = RecipeData.ShowRecipeName(response.toString());
                RecipeIngredients = RecipeData.ShowRecipeIngredients(response.toString());
                RecipeProcedure = RecipeData.ShowRecipeProcedure(response.toString());
                UserName = RecipeData.ShowUserName(response.toString());
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
