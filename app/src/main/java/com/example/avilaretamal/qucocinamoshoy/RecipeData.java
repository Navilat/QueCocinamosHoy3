package com.example.avilaretamal.qucocinamoshoy;


import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RecipeData {
    private static final String DATA = "data";
    private static final String COUNT = "count";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String USERNAME = "user";
    private static final String RECIPE = "recipe";
    private static final String INGREDIENTS = "ingredients";
    private static final String PROCEDURE = "procedure";


    public static String[] GetCategoriesNames(String response) {
        JSONObject JsonResponse = null;
        String[] CategoriesNames = null;
        JSONArray auxiliar = null;
        int count = 0;

        try {
            JsonResponse = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            auxiliar = JsonResponse.getJSONArray(DATA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            count = JsonResponse.getInt(COUNT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (count > 0) {
            CategoriesNames = new String[count];
            for (int i = 0; i < count ; i++) {
                JSONObject object = null;
                try {
                    object = auxiliar.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    CategoriesNames[i] = object.getString(NAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //Log.v("Contadooor", CategoriesNames[7]);
        return CategoriesNames;
    }

    public static Integer[] GetCategoriesIds(String response) {
        JSONObject JsonResponse = null;
        Integer[] CategoriesIds = null;
        JSONArray auxiliar = null;
        int count = 0;

        try {
            JsonResponse = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            auxiliar = JsonResponse.getJSONArray(DATA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            count = JsonResponse.getInt(COUNT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (count > 0) {
            CategoriesIds = new Integer[count];
            for (int i = 0; i < count ; i++) {
                JSONObject object = null;
                try {
                    object = auxiliar.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    CategoriesIds[i] = object.getInt(ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //Log.v("Contadooor", String.valueOf(CategoriesIds[1]));
        return CategoriesIds;
    }

    public static String ShowRecipeName(String response) {
        String RecipeName = null;
        JSONObject JsonResponse = null;
        JSONObject auxiliar = null;

        try {
            JsonResponse = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            auxiliar = JsonResponse.getJSONObject(RECIPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            RecipeName = auxiliar.getString(NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RecipeName;
    }

    public static String ShowRecipeIngredients(String response) {
        String RecipeIngredients = null;
        JSONObject JsonResponse = null;
        JSONObject auxiliar = null;

        try {
            JsonResponse = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            auxiliar = JsonResponse.getJSONObject(RECIPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            RecipeIngredients = auxiliar.getString(INGREDIENTS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RecipeIngredients.replace("\\n", System.getProperty("line.separator"));
    }

    public static String ShowRecipeProcedure(String response) {
        String RecipeProcedure = null;
        JSONObject JsonResponse = null;
        JSONObject auxiliar = null;

        try {
            JsonResponse = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            auxiliar = JsonResponse.getJSONObject(RECIPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            RecipeProcedure = auxiliar.getString(PROCEDURE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RecipeProcedure.replace("\\n", System.getProperty("line.separator"));
    }

    public static String ShowUserName(String response) {
        String UserName = null;
        JSONObject JsonResponse = null;

        try {
            JsonResponse = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            UserName = JsonResponse.getString(USERNAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return UserName;
    }
}
