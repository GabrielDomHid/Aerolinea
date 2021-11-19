package com.gdomhid.aerolinea;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "comprar";
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        Button bbuy = findViewById(R.id.bComprar);
        bbuy.setOnClickListener((View v) ->{
            Toast toast = Toast.makeText(this,"Se está procesando tu compra...", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            sendMessage(false);
        });
        //Creamos un dialogo de alerta para confirmar algo y tenemos boton de si y no que cada uno hace una cosa
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ImageButton ibPreferente = findViewById(R.id.ibPreferente);
        ibPreferente.setOnClickListener((View v) -> {
            builder.setMessage("¿Estás seguro de comprar el viaje en preferente? Esto conlleva un coste adicional.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() { //Si pulsamos el si nos muestra el toast
                        public void onClick(DialogInterface dialog, int id) {              // y llama a sendMessage con el true
                            finish();
                            Toast.makeText(getApplicationContext(),"Perfecto, se está procesando tu compra.",
                                    Toast.LENGTH_SHORT).show();
                            sendMessage(true);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel(); //sacamos un snackbar como mensaje de error
                            Snackbar snackbar = Snackbar
                                    .make(v, "Se ha cancelado.", Snackbar.LENGTH_LONG)
                                    .setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                        }
                                    });
                            snackbar.setActionTextColor(Color.RED);;
                            snackbar.show();
                            //Toast.makeText(getApplicationContext(),"Se ha cancelado la compra preferente.",
                            //Toast.LENGTH_SHORT).show();
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Viaje Preferente");
            alert.show();
        });
        //Fecha
        EditText editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setText(FechaFull());// Le asigno por defecto la fecha del dia de hoy, este campo siempre tendra un dato como minimo
        editTextDate.setInputType(InputType.TYPE_NULL);
        editTextDate.setOnClickListener((View v) -> { //cuando hacemos clic aparece el datepicker
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(MainActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year); //ponemos el dia que hemos señalado
                        }// en el mes le sumo uno porque me da los meses del 0 al 11, para arreglar eso +1
                    }, year, month, day);
            picker.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId(); //compruebo que id he seleccionado y muestro el toast
        if (id == R.id.mDatosPersonales || id == R.id.mDocumentosViaje || id == R.id.mCompañeros
                || id == R.id.mViajes || id == R.id.mTajertasEmbarque || id == R.id.mInfoVuelo
                || id == R.id.mInfoActualizada || id == R.id.mValesRegalo || id == R.id.mRevistaVuelo
                || id == R.id.mTerminosCondiciones || id == R.id.mCentroAyuda || id == R.id.mContacto
                || id == R.id.mPrivacidad || id == R.id.mConfigPrivacidad){
            muestraToast(3);
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    /**
     * Recoge todos los campos y comprueba que estan rellenos, recoge los datos de cada uno los introduce
     * en un arraylist y si todo esta correcto inicia la nueva actividad con todos los datos
     * @param preferente true si el viaje es en preferente o no, si hace clic en el boton de comprar
     *                   preferente esto sera true y se le añade un precio extra
     */
    public void sendMessage(boolean preferente) {
        ArrayList<String> msg = new ArrayList<String>();
        Intent intent = new Intent(this, Pago.class);
        boolean validaCampos = false; //Para validar los campos, si alguno lo pone en true no pasa a la siguiente actividad
        //recojo los campos
        //Destinos
        Spinner origen = findViewById(R.id.sPaisesOrigen);
        msg.add(origen.getSelectedItem().toString());
        Spinner destino = findViewById(R.id.sPaisesDestino);
        msg.add(destino.getSelectedItem().toString());
        EditText date = findViewById(R.id.editTextDate);
        msg.add(date.getText().toString());
        //Tratamiento
        EditText nombre = findViewById(R.id.edtNombre);
        if (!nombre.getText().toString().isEmpty()){ msg.add(nombre.getText().toString()); validaCampos=true;} else { muestraToast(1); }
        EditText apellidos = findViewById(R.id.edtApellidos);
        if (!apellidos.getText().toString().isEmpty()){ msg.add(apellidos.getText().toString()); validaCampos=true;} else { muestraToast(1);}
        EditText direccion = findViewById(R.id.edtDireccion);
        if (!direccion.getText().toString().isEmpty()){ msg.add(direccion.getText().toString()); validaCampos=true;} else { muestraToast(1);}
        EditText tlf = findViewById(R.id.edtTelefono);
        if (!tlf.getText().toString().isEmpty()){ msg.add(tlf.getText().toString()); validaCampos=true;} else { muestraToast(1);}
        EditText email = findViewById(R.id.edtEmail);
        if (!email.getText().toString().isEmpty()){ msg.add(email.getText().toString()); validaCampos=true;} else { muestraToast(1); }
        //Extras
        RadioButton rbyes = findViewById(R.id.rbYes);
        if (rbyes.isChecked()){ msg.add("mrYes"); }
        CheckBox pc = findViewById(R.id.cbPrimeraClase);
        if (pc.isChecked()){ msg.add("pcYes"); }
        CheckBox av = findViewById(R.id.cbAsientoVentanilla);
        if (av.isChecked()){msg.add("avYes");}
        CheckBox mascota = findViewById(R.id.cbMascota);
        if (mascota.isChecked()){msg.add("mascotaYes");}
        CheckBox desa = findViewById(R.id.cbDesayuno);
        if (desa.isChecked()){msg.add("desayunoYes");}
        CheckBox almu = findViewById(R.id.cbAlmuerzo);
        if (almu.isChecked()){msg.add("almuerzoYes");}
        CheckBox cena = findViewById(R.id.cbCena);
        if (cena.isChecked()){msg.add("cenaYes");}
        Switch seguro = findViewById(R.id.switchSeguro);
        if (seguro.isChecked()) { msg.add("seguroYes");}
        //preferente
        if (preferente){ msg.add("ViajePreferente");}
        //añado el arraylist al bundle para enviarlo
        CheckBox cbterminos = findViewById(R.id.cbterminos);
        if (validaCampos && cbterminos.isChecked()) {
            intent.putStringArrayListExtra(EXTRA_MESSAGE, msg);
            startActivity(intent);
        } else { muestraToast(2);}
    }

    /**
     *  Saca la fecha del dia formateada.
     * @return la fecha en un string de hoy
     */
    public static String FechaFull() {
        Calendar calendario = new GregorianCalendar();
        return String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))
                + "/" + String.valueOf(calendario.get(Calendar.MONTH)+1)
                + "/" + String.valueOf(calendario.get(Calendar.YEAR));
    }

    /**
     * Genera un toast personalizado
     */
    public void muestraToast(int num){
        if (num == 1) {
            Toast toast = Toast.makeText(this, "¡ERROR!, Rellene todos los campos", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (num == 2) {
            Toast toast = Toast.makeText(this, "¡ERROR!, Debe aceptar los terminos para comprar.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (num == 3) {
            Toast toast = Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}