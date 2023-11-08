package com.example.invenio_talet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class show_list extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView txtBuscar;
    RecyclerView listaContactos;
    ArrayList<alta_guardado> listaArrayContactos;
    ListaContactosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        txtBuscar = findViewById(R.id.txtBuscar);
        listaContactos = findViewById(R.id.listaContactos);
        listaContactos.setLayoutManager(new LinearLayoutManager(this));

        ExpandableListDataPump dbContactos = new ExpandableListDataPump(this);
        listaArrayContactos = dbContactos.getData();

        adapter = new ListaContactosAdapter(listaArrayContactos);
        listaContactos.setAdapter(adapter);

        txtBuscar.setOnQueryTextListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuNuevo:
                nuevoRegistro();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void nuevoRegistro() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filtrado(s);
        performSearchRequest(s);

        return false;
    }

    private void performSearchRequest(String query) {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST ,"http://201.149.23.34:80/STE/document.php?apicall=find",response -> {
             try {
                JSONObject jsonObject = new JSONObject(response);
                Context context = getApplicationContext();
                CharSequence text = "Buscando...";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                if(!jsonObject.getBoolean("error")){
                    JSONObject userJson = jsonObject.getJSONObject("select");
                    Log.e("recibi", String.valueOf(userJson));
                    alta_guardado_local(
                            userJson.getString("title"),
                            userJson.getString("author"),
                            userJson.getString("content"),
                            userJson.getString("dateT")
                    );

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"Error",Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.e("Envia",query);
                params.put("busca",query);

                return params;

            }
        };

        volleySingleton.getInstance(getApplication()).addToRequestQueue(stringRequest2);
    }

    private void alta_guardado_local(String title, String author, String content, String date) {
        System.out.println("datos recibido 2"+title+author+content+date);
        SharedPreferences sharedPreferences = this.getApplication().getSharedPreferences("altas", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("alta_guardado_list", null);
        ArrayList<alta_guardado> existingDataList = new ArrayList<>();

        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String existingTitle = jsonObject.getString("title");
                    String existingAuthor = jsonObject.getString("author");
                    String existingContent = jsonObject.getString("content");
                    String existingDate = jsonObject.getString("date");

                    existingDataList.add(new alta_guardado(existingTitle, existingAuthor, existingContent, existingDate));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        boolean dataAlreadyExists = false;
        for (alta_guardado newData : existingDataList) {
            if (newData.getTitle().equals(title) && newData.getAuthor().equals(author) && newData.getContent().equals(content) && newData.getDate().equals(date)) {
                dataAlreadyExists = true;
                break;
            }
        }

        if (!dataAlreadyExists) {
            existingDataList.add(new alta_guardado(title,author,content,date));
            String updatedJson = new Gson().toJson(existingDataList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("alta_guardado_list", updatedJson);
            editor.apply();
        }

    }
}
