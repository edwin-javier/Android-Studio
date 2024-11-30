package net.javi.finalproyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.javi.finalproyecto.Clases.Configuracion;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AgregarCliente extends AppCompatActivity {

    Button botonGuardar,botonVolver;
    EditText nombre,placa,numero,reparacion;
    Configuracion objConfiguracion = new Configuracion();
    String URL = objConfiguracion.urlWebservices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cliente);

        nombre = (EditText) findViewById(R.id.txtNombre);
        placa = (EditText) findViewById(R.id.txtPlaca);
        numero = (EditText) findViewById(R.id.txtNumero);
        reparacion = (EditText) findViewById(R.id.txtReparacion);
        botonGuardar = (Button) findViewById(R.id.btnNCliente);
        botonVolver = (Button) findViewById(R.id.btnVolver);

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean retorno=true;

                if (nombre.getText().toString().isEmpty()){
                    nombre.setError("Nombre");
                    retorno = false;
                }else if (placa.getText().toString().isEmpty()){
                    placa.setError("Número de Placa");
                    retorno = false;
                }else if (numero.getText().toString().isEmpty()){
                    numero.setError("Número de teléfono");
                    retorno = false;
                }else {
                    guardar();
                }
            }
        });

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });
    }

    private void guardar(){
        try{
            RequestQueue objetoPeticion = Volley.newRequestQueue(AgregarCliente.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("1")){
                            Toast.makeText(AgregarCliente.this, "Cliente Registrado con exito", Toast.LENGTH_SHORT).show();
                            nombre.setFocusableInTouchMode(true); nombre.requestFocus();
                            nombre.setText("");
                            numero.setText("");
                            placa.setText("");
                            reparacion.setText("");
                        }else{
                            Toast.makeText(AgregarCliente.this, "Error: "+ estado, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AgregarCliente.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("accion","Ncliente");
                    params.put("Nombre", nombre.getText().toString());
                    params.put("Celular", numero.getText().toString());
                    params.put("Placa_carro", placa.getText().toString());
                    params.put("reparacion", reparacion.getText().toString());
                    return params;
                }
            };
            objetoPeticion.add(peticion);
        }catch (Exception error){
            Toast.makeText(AgregarCliente.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void regresar(){
        Intent actividad = new Intent(AgregarCliente.this, Inicio.class);
        startActivity(actividad);
        AgregarCliente.this.finish();
    }
}