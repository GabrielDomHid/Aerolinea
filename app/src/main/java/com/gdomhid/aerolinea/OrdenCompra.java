package com.gdomhid.aerolinea;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class OrdenCompra extends AppCompatActivity {
    public ArrayList<String> msg = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordencompra);
        initialize();
    }

    private void initialize() {
        Intent intent = getIntent(); //recibir la informacion
        if (intent != null) {
            msg = intent.getStringArrayListExtra("pagar"); //sacar el array
            if (!msg.isEmpty()) {
                for (String x : msg) { //recorrer el array y comprobar cuales hay para mostrarlo en pantalla
                    compruebaExtra(x);
                }
            }
        }
        Button bConfirmar = findViewById(R.id.bConfirmar);
        bConfirmar.setOnClickListener((View v) -> {
            Intent intent2 = new Intent(this, MainActivity.class);
            startActivity(intent2);
        });
    }

    /**
     * Con el string q le llega compruebo cual es y muestro la informacion en la interfaz
     * @param s opcion elegida por el cliente
     */
    private void compruebaExtra(String s){
        TextView nomCom = findViewById(R.id.tvNombreCompleto), tlf = findViewById(R.id.tvTlf),
                dir = findViewById(R.id.tvDireccionCompleta),correo = findViewById(R.id.tvCorreo),
                mr = findViewById(R.id.tvMoRe),pc = findViewById(R.id.tvPriClase),av = findViewById(R.id.tvAsiVen),
                mas = findViewById(R.id.tvViaMas),des = findViewById(R.id.tvDes),alm = findViewById(R.id.tvAlmu),
                cena = findViewById(R.id.tvCena),seg = findViewById(R.id.tvSegAdi);
        nomCom.setText(msg.get(3) + " " + msg.get(4));
        dir.setText(msg.get(5));
        tlf.setText(msg.get(6));
        correo.setText(msg.get(7));
        if (s.compareToIgnoreCase("mrYes") == 0) mr.setText(R.string.tvyes);
        else if (s.compareToIgnoreCase("pcYes") == 0) pc.setText(R.string.tvyes);
        else if (s.compareToIgnoreCase("avYes") == 0) av.setText(R.string.tvyes);
        else if (s.compareToIgnoreCase("mascotaYes") == 0) mas.setText(R.string.tvyes);
        else if (s.compareToIgnoreCase("desayunoYes") == 0) des.setText(R.string.tvyes);
        else if (s.compareToIgnoreCase("almuerzoYes") == 0) alm.setText(R.string.tvyes);
        else if (s.compareToIgnoreCase("cenaYes") == 0) cena.setText(R.string.tvyes);
        else if (s.compareToIgnoreCase("seguroYes") == 0) seg.setText(R.string.tvyes);
    }
}