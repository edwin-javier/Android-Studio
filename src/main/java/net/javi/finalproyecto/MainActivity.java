package net.javi.finalproyecto;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.javi.finalproyecto.Clases.Configuracion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText usuario,pass;
    Button botonIngresar;

    String str_usuario,str_pass;
    Configuracion objConfiguracion = new Configuracion();
    String URL = objConfiguracion.urlWebservices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonIngresar = (Button) findViewById(R.id.btnIngresar);
        usuario = (EditText) findViewById(R.id.txtUsuario);
        pass = (EditText) findViewById(R.id.txtContra);

        /*validar que los campos no esten vacios*/
        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean retorno=true;
                if (usuario.getText().toString().isEmpty()){
                    usuario.setError("Escriba el Usuario");
                    retorno = false;
                }else if (pass.getText().toString().isEmpty()){
                    pass.setError("Escriba la Contraseña");
                    retorno = false;
                }else {
                    Ingresar();
                }
            }
        });
    }

    private void Ingresar() {
        try{
            RequestQueue objetoPeticion = Volley.newRequestQueue(MainActivity.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("1")){
                            Intent actividad = new Intent(MainActivity.this, Inicio.class);
                            startActivity(actividad);
                            MainActivity.this.finish();
                            Toast.makeText(MainActivity.this, "Iniciando Sesión", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            usuario.setFocusableInTouchMode(true); usuario.requestFocus();
                            usuario.setText("");
                            pass.setText("");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("accion","login");
                    params.put("Usuario", usuario.getText().toString());
                    params.put("Contraseña", pass.getText().toString());

                    return params;
                }
            };
            objetoPeticion.add(peticion);
        }catch (Exception error){
            Toast.makeText(MainActivity.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

        private void Crear(){
            Intent actividad = new Intent(MainActivity.this, CrearUsuario.class);
            startActivity(actividad);
            MainActivity.this.finish();
        }
}








