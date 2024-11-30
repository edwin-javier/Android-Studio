package net.javi.finalproyecto;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

public class Editar extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    EditText nombre,placa,numero,reparacion;
    String Id_Cliente;
    Button btnRegresar, btnEliminar, btnModificar, botonLlamar;
    Configuracion objConfiguracion = new Configuracion();
    String URL = objConfiguracion.urlWebservices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        nombre = (EditText) findViewById(R.id.txtNombreModificar);
        placa = (EditText) findViewById(R.id.txtPlacaModificar);
        numero = (EditText) findViewById(R.id.txtTelModificar);
        reparacion = (EditText) findViewById(R.id.txtRepaModificar);
        btnRegresar = (Button) findViewById(R.id.btnRegresarModificar);
        btnEliminar = (Button) findViewById(R.id.btnEliminarModificar);
        botonLlamar = (Button) findViewById(R.id.btnLlamar);
        btnModificar = (Button) findViewById(R.id.btnGuardarContactoModificar);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialogo = new AlertDialog
                        .Builder(Editar.this)
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Confirmaron eliminar
                                eliminar();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //cancelaron la eliminacion
                                dialog.dismiss();
                            }
                        })
                        .setTitle("Confirmar")
                        .setMessage("¿Desea eliminar este contacto?")
                        .create();
                dialogo.show();
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificar();
            }
        });

        botonLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamar();
            }
        });
    }

    private void llamar()
    {
        String num = numero.getText().toString();

        if (num.trim().length() > 0)
        {
            if (ContextCompat.checkSelfPermission(Editar.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Editar.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }else{
                String dial = "tel:" + num;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }else
        {
            Toast.makeText(Editar.this, "", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminar(){
        try{
            RequestQueue objetoPeticion = Volley.newRequestQueue(Editar.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("1")){
                            Toast.makeText(Editar.this, "Contacto Eliminado con exito", Toast.LENGTH_SHORT).show();
                            regresar();
                        }else{
                            Toast.makeText(Editar.this, "Error: "+ estado, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Editar.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("accion","eliminar_contacto");
                    params.put("Id_Cliente", Id_Cliente);
                    return params;
                }
            };
            objetoPeticion.add(peticion);
        }catch (Exception error){
            Toast.makeText(Editar.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void modificar(){
        try{
            RequestQueue objetoPeticion = Volley.newRequestQueue(Editar.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("1")){
                            Toast.makeText(Editar.this, "Contacto Modificado con exito", Toast.LENGTH_SHORT).show();
                            regresar();
                        }else{
                            Toast.makeText(Editar.this, "Error: "+ estado, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Editar.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("accion","modificar_cliente");
                    params.put("Nombre", nombre.getText().toString());
                    params.put("Celular", numero.getText().toString());
                    params.put("Placa_carro", placa.getText().toString());
                    params.put("reparacion", reparacion.getText().toString());
                    params.put("Id_Cliente", Id_Cliente);
                    return params;
                }
            };
            objetoPeticion.add(peticion);
        }catch (Exception error){
            Toast.makeText(Editar.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void regresar() {
        Intent actividad = new Intent(Editar.this, VerRegistros.class);
        startActivity(actividad);
        Editar.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Verificando si nos están enviando datos
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            try{
                //Capturando los datos de la Actividad
                String elNombre, laplaca, elTelefono, laReparacion, elIDPersona;
                elNombre = extras.getString("Nombre");
                laplaca = extras.getString("Placa_carro");
                laReparacion = extras.getString("reparacion");
                elTelefono = extras.getString("Celular");
                elIDPersona = extras.getString("Id_Cliente");

                //Asignando los datos a los controles y variables globales
                this.Id_Cliente = elIDPersona;
                nombre.setText(elNombre);
                placa.setText(laplaca);
                reparacion.setText(laReparacion);
                numero.setText(elTelefono);
            }catch (Exception e){
                Toast.makeText(Editar.this, "Error Interno: "+ e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}