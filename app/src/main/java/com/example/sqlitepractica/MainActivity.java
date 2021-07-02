package com.example.sqlitepractica;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText criterioBusquedaCodigo;
    SQLiteDatabase db = null;
    private TextView resultado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        criterioBusquedaCodigo = (EditText)findViewById(R.id.EditText01);
        resultado = (TextView) findViewById(R.id.resultado);

    }
    public void crearBd(View view) {
        BaseDatosHelper usdbh = new BaseDatosHelper(this, "DBUsuarios", null, 1);
        db = usdbh.getWritableDatabase();
        //Si hemos abierto correctamente la base de datos
        if (db != null) {
            //insertamos 5 usiarios de ejemplo
            for (int i = 1; i <= 15; i++) {
                //Generamos los datos
                int codigo = i;
                String nombre = "Usuario creado " + i;
                String contrasenia = "Contraseña " + i;
                //Insertamos los datos en ala tabla Usuarios
                db.execSQL("INSERT INTO Usuarios (codigo, nombre, contrasenia)" + " VALUES (" + codigo + ", '" + nombre + "','" + contrasenia + "')");
                //execSQL  ALTA BAJA MODIFICACION o CREACION :ejecuta  instrucciones de acción NSERT INTO,, modificar, alta , baja o de creación   CREATE TABLE, I
                // si es de recuperación de datos o consultar usaremos el rawQuery
                // para consultar la bbdd suelo usar rawQuery, se pueden poner todas las consultas que se quiere del ANSI del 92
            }
            Log.d(TAG, "INSERTADOs cinco registros !!!!: ");
            //CERRAMOS LA Base de datos
            //db.close();
        }
    }

    public void recuperarDatos(View view) {
        try {
            String[] args = new String [] {criterioBusquedaCodigo.getText().toString()};

            String codigo = "", nombre = "", contrasenia = "";
            BaseDatosHelper usdbh = new BaseDatosHelper(this, "DBUsuarios", null, 1);

            SQLiteDatabase db = usdbh.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT codigo,nombre, contrasenia FROM Usuarios where codigo=?", args);
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    codigo = c.getString(0);
                    nombre = c.getString(1);
                    contrasenia = c.getString(2);
                } while (c.moveToNext());
            }

            this.resultado.setText("Codigo usuario: " + codigo + "Nombre " + nombre + "Contraseña " + contrasenia);
        } catch (Exception e) {
            Log.d(TAG, "ERROR: " + e.toString());

        }
    }

        public void consultarDatos (View view){
            String[] args = new String [] {criterioBusquedaCodigo.getText().toString()};

            String codigo = "", nombre = "", contrasenia = "";
            BaseDatosHelper usdbh = new BaseDatosHelper(this, "DBUsuarios", null, 1);

            SQLiteDatabase db = usdbh.getReadableDatabase();

            String sql = "Select * from Usuarios where codigo =?";

            //la variable cursor c recoge todos los registros que ha sido seleccionado por el acceso a la bbdd
            Cursor c = db.rawQuery(sql, args);
            c.moveToFirst();
            int IdIndex = c.getColumnIndexOrThrow("nombre");

            ArrayList<String> miArrayList = new ArrayList<>();

            //c.moveToFirst() es un boolean si ha encontrado el primer registro y ha devuelto datos
            // si da falso no hay ningún registro porque no se puede mover al primero porque no existe

            do {
                miArrayList.add(c.getString(IdIndex)); // ó idIndex }
                Log.i("Consulta rawQuery", String.valueOf(miArrayList));
            } while (c.moveToNext());

            this.resultado.setText(String.valueOf(miArrayList));
        }


    }