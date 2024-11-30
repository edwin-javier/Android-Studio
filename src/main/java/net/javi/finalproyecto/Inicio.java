package net.javi.finalproyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.BitSet;

public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Button botonCliente, botonNC, botonSalir;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        botonCliente = (Button) findViewById(R.id.btnVer);
        botonNC = (Button) findViewById(R.id.btntNuevo);
        botonSalir = (Button) findViewById(R.id.Salir);

        botonCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ver();
            }
        });

        botonNC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregar();
            }
        });

        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salir();
            }
        });
    }

    private void agregar(){
        Intent actividad = new Intent(Inicio.this, AgregarCliente.class);
        startActivity(actividad);
        Inicio.this.finish();
    }

    private void ver(){
        Intent actividad = new Intent(Inicio.this, VerRegistros.class);
        startActivity(actividad);
        Inicio.this.finish();
    }

    private void salir(){
        Intent actividad = new Intent(Inicio.this, MainActivity.class);
        Toast.makeText(Inicio.this, "Saliendo", Toast.LENGTH_SHORT).show();
        startActivity(actividad);
        Inicio.this.finish();
    }
}