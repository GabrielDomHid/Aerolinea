package com.gdomhid.aerolinea;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class Pago extends AppCompatActivity {
    public ArrayList<String> msg = new ArrayList<String>();
    private Double precio = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);
        initialize();
    }

    private void initialize() {
        Intent intent = getIntent();// Recupero los datos que me pasan de la otra actividad
        if (intent != null) {
            msg = intent.getStringArrayListExtra("comprar"); //relleno mi array con el que me pasan de datos
            //generaPrecio();
            if (!msg.isEmpty()){ //si el array no esta vacio
                for (String x : msg) { //recorro el array
                    precio += generaPrecio(x);//por cada string que leemos del fichero agrega un precio
                }
                precio += generaPrecioViaje(); //precio del viaje
            }
        }
        TextView tvPrecio = findViewById(R.id.tvPrecio);
        tvPrecio.setText(String.valueOf(precio)); //asigno el precio a la interfaz

        Button bPagar = findViewById(R.id.bPagar);
        bPagar.setOnClickListener((View v) -> { //cuando hago clic en pagar le vuelvo a pasar a la siguiente activdad el array con los datos
            Intent intent2 = new Intent(this, OrdenCompra.class);
            intent2.putStringArrayListExtra("pagar", msg);
            startActivity(intent2); //inicio la 3 actividad
        });
    }

    /**
     * Comprueba que campo le entra y segun el que sea devuelve la cantidad que cuesta el extra
     * @param s los extras, como el desayuno por ejemplo
     * @return devuelve el precio del desayuno para sumarlo al precio
     */
    private Double generaPrecio(String s){
        Double count = 0.0;
        if (s.compareToIgnoreCase("mrYes") == 0) count = 5.0;
        else if (s.compareToIgnoreCase("pcYes") == 0) count = 75.0;
        else if (s.compareToIgnoreCase("avYes") == 0) count = 30.0;
        else if (s.compareToIgnoreCase("mascotaYes") == 0) count = 37.5;
        else if (s.compareToIgnoreCase("desayunoYes") == 0) count = 12.5;
        else if (s.compareToIgnoreCase("almuerzoYes") == 0) count = 15.0;
        else if (s.compareToIgnoreCase("cenaYes") == 0) count = 20.0;
        else if (s.compareToIgnoreCase("seguroYes") == 0) count = 25.0;
        else if (s.compareToIgnoreCase("ViajePreferente") == 0) count = 224.5;
        return count;
    }

    /**
     * Partiendo de la base del origen destino y la fecha en que se realiza el viaje
     * creo un for por cada string para generar el numero ascii de cada letra y luego
     * sumar todos esos numeros para que se convierta en la semilla del numero aleatorio que
     * se creara mas tarde como precio del billete sin contar extras
     * @return numero de la semilla
     */
    private int generaSemilla(){
        int contador = 0; //esta variable contará la suma del valor ASCII de cada letra
        String origen = msg.get(0), destino = msg.get(1), fecha = msg.get(2);
        for (int i=0; i < origen.length(); i++){
            contador = contador + origen.codePointAt(i);
        }
        for (int i=0; i < destino.length(); i++){
            contador = contador + destino.codePointAt(i);
        }
        for (int i=0; i < fecha.length(); i++){
            contador = contador + fecha.codePointAt(i);
        }
        return contador;
    }

    /**
     *  Partiendo de la semilla que generamos creamos un numero aleatorio entre 100 y 1000
     *  que seran el precio del billete sin extras
     * @return precio del billete
     */
    private int generaPrecioViaje(){
        int semilla = generaSemilla();
        Random aleatorio = new Random(System.currentTimeMillis());
        aleatorio.setSeed(semilla);
        return aleatorio.nextInt(901)+100;
    }
}
