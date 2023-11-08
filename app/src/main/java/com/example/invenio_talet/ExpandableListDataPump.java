package com.example.invenio_talet;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExpandableListDataPump  {
    private static Context ctx;
    Context context;
    private static ExpandableListDataPump mInstance;
     public ExpandableListDataPump(@Nullable Context context) {
    super();
    this.context=context;

    }
    public ArrayList<alta_guardado> mostrar(){
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("altas", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("alta_guardado_list", null);
        ArrayList<alta_guardado> listaContactos = new ArrayList<>();
            alta_guardado Alta_guardado;
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("title");
                    String author = jsonObject.getString("author");
                    String date = jsonObject.getString("date");
                    String content = jsonObject.getString("content");
                    List<String> titulos = new ArrayList<String>();
                    titulos.add("Title: " + title);
                    titulos.add("Autor: " + author);
                    titulos.add("Date: " + date);
                    titulos.add("Content: " + content);

                    expandableListDetail.put("Altas " + (i + 1), titulos);
                    Alta_guardado = new alta_guardado(title,author,date,content);
                    Alta_guardado.setAuthor(author);
                    listaContactos.add(Alta_guardado);
                 }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Ordenar el mapa de datos por el número de "Altas"
        Map<String, List<String>> sortedMap = new TreeMap<>(expandableListDetail);

        return listaContactos;
    }



    public ArrayList<alta_guardado> getData() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("altas", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("alta_guardado_list", null);

        ArrayList<alta_guardado> listaContactos = new ArrayList<>();

        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("title");
                    String author = jsonObject.getString("author");
                    String date = jsonObject.getString("date");
                    String content = jsonObject.getString("content");

                    alta_guardado Alta_guardado = new alta_guardado(title, author, date, content);
                    listaContactos.add(Alta_guardado);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return listaContactos;
    }



}
