package net.javi.finalproyecto;

import android.app.ProgressDialog;
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

public class CrearUsuario extends AppCompatActivity {

    Button botonAgregar, botoRegresar;
    EditText usuario,contra;
    Configuracion objConfiguracion = new Configuracion();
    String URL = objConfiguracion.urlWebservices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);


        usuario = (EditText) findViewById(R.id.Usuario);
        contra = (EditText) findViewById(R.id.Contraseña);
        botonAgregar = (Button) findViewById(R.id.btnCrear);
        botoRegresar = (Button) findViewById(R.id.btnRegresar);

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean retorno=true;

                if (usuario.getText().toString().isEmpty()){
                    usuario.setError("Escriba el Usuario");
                    retorno = false;
                }else if (contra.getText().toString().isEmpty()){
                    contra.setError("Escriba la Contraseña");
                    retorno = false;
                }else {
                    Registrar();
                }
            }
        });

        botoRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });
    }

    private void Registrar() {
        try{
            RequestQueue objetoPeticion = Volley.newRequestQueue(CrearUsuario.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("1")){
                            Intent actividad = new Intent(CrearUsuario.this, MainActivity.class);
                            startActivity(actividad);
                            CrearUsuario.this.finish();
                            Toast.makeText(CrearUsuario.this, "Usuario registrado con exito", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CrearUsuario.this, "Usuario existente", Toast.LENGTH_LONG).show();
                            usuario.setFocusableInTouchMode(true); usuario.requestFocus();
                            usuario.setText("");
                            contra.setText("");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(CrearUsuario.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("accion","Crear_Usuario");
                    params.put("Usuario", usuario.getText().toString());
                    params.put("Contraseña", contra.getText().toString());

                    return params;
                }
            };
            objetoPeticion.add(peticion);
        }catch (Exception error){
            Toast.makeText(CrearUsuario.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void regresar(){
        Intent actividad = new Intent(CrearUsuario.this, MainActivity.class);
        startActivity(actividad);
        CrearUsuario.this.finish();
    }
}