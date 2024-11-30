package net.javi.finalproyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.javi.finalproyecto.Clases.Configuracion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerRegistros extends AppCompatActivity {

    Button botonAgregar, botonBuscar;
    ListView listaContactos;
    EditText txtCriterio;
    Configuracion objConfiguracion = new Configuracion();
    String URL = objConfiguracion.urlWebservices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_registros);

        botonAgregar = (Button) findViewById(R.id.btnAgregar);
        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ventana = new Intent(VerRegistros.this, AgregarCliente.class);
                startActivity(ventana);
            }
        });

        txtCriterio = (EditText) findViewById(R.id.txtCriterio);
        botonBuscar = (Button) findViewById(R.id.btnBuscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    llenarLista();
                    InputMethodManager adminManager = (InputMethodManager) VerRegistros.this.getSystemService(VerRegistros.this.INPUT_METHOD_SERVICE);
                    adminManager.hideSoftInputFromWindow(txtCriterio.getWindowToken(),0);
                }catch (Exception error){
                    Toast.makeText(VerRegistros.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        listaContactos = (ListView) findViewById(R.id.lvContactos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            llenarLista();
        }catch (Exception error){
            Toast.makeText(VerRegistros.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    class AdaptadorListaContacto extends BaseAdapter {
        public JSONArray arregloDatos;
        @Override
        public int getCount() {
            return arregloDatos.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View v, ViewGroup parent) {
            v = getLayoutInflater().inflate(R.layout.fila_contactos,null);
            TextView txtTitulo = (TextView) v.findViewById(R.id.tvTituloFilaContacto);
            TextView txtTelefono = (TextView) v.findViewById(R.id.tvTelefonoFilaContacto);
            Button btnVer = (Button) v.findViewById(R.id.btnVerContacto);

            JSONObject objJSON = null;
            try {
                objJSON = arregloDatos.getJSONObject(position);
                final String Id_Cliente, Nombre, Celular, Placa_carro, reparacion;
                Id_Cliente = objJSON.getString("Id_Cliente");
                Nombre = objJSON.getString("Nombre");
                Celular = objJSON.getString("Celular");
                Placa_carro = objJSON.getString("Placa_carro");
                reparacion = objJSON.getString("reparacion");

                txtTitulo.setText(Nombre);
                txtTelefono.setText(Placa_carro);

                btnVer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent objDetalle = new Intent(VerRegistros.this,Editar.class);
                        objDetalle.putExtra("Id_Cliente",Id_Cliente);
                        objDetalle.putExtra("Nombre",Nombre);
                        objDetalle.putExtra("Celular",Celular);
                        objDetalle.putExtra("Placa_carro",Placa_carro);
                        objDetalle.putExtra("reparacion",reparacion);
                        startActivity(objDetalle);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return v;
        }
    }

    public void llenarLista(){
        final String criterio = txtCriterio.getText().toString();
        RequestQueue objetoPeticion = Volley.newRequestQueue(VerRegistros.this);
        StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject objJSONResultado = new JSONObject(response.toString());
                    JSONArray aDatosResultado = objJSONResultado.getJSONArray("resultado");
                    AdaptadorListaContacto miAdaptador = new AdaptadorListaContacto();
                    miAdaptador.arregloDatos = aDatosResultado;
                    listaContactos.setAdapter(miAdaptador);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VerRegistros.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("accion","mostrar");
                params.put("filtro", criterio);
                return params;
            }
        };
        objetoPeticion.add(peticion);
    }
}