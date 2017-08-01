package ave.ladhc.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Lenovo on 31/07/2017.
 */

public class BD extends SQLiteOpenHelper{

    public BD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "prueba", factory, 1);
}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table datos(nombre text, telefono text, color text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //no se ocupa
    }
    public String guardar(String noombre, String teelefono, String coolores){
        String mensaje ="";
        SQLiteDatabase database= this.getWritableDatabase();
        ContentValues contenedor = new ContentValues();
        contenedor.put("nombre",noombre);
        contenedor.put ("telefono",teelefono);
        contenedor.put ("color",coolores);
        try {
            database.insertOrThrow("datos",null,contenedor);
            mensaje = "Contacto guardado";

        }catch (SQLException e){
            mensaje = "Error, contacto no guardado";


        }
        database.close();
        return mensaje;
    }

    public String[] buscar_reg(String buscar){
        String[] datos=new String[4];
        SQLiteDatabase database= this.getWritableDatabase();
        String q= "SELECT * FROM datos WHERE nombre='"+buscar+"'";
        Cursor registros = database.rawQuery(q,null);
        if(registros.moveToFirst()){
            for(int i=0;i<3;i++){
                datos[i]=registros.getString(i);
            }
            datos[3]="encontrado";
        }else{
            datos[3]="no se encontro a "+ buscar;
        }
        database.close();
        return datos;
    }

    public String eliminar(String Nombre){
        String mensaje ="";
        SQLiteDatabase database = this.getWritableDatabase();
        int cantidad = database.delete("datos","nombre='"+Nombre+"'",null);
        if (cantidad !=0){
            mensaje = "eliminado corectamante";
        }else{
            mensaje="no existe";
        }
        database.close();
        return mensaje;
    }

    public ArrayList llenar_lv(){
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        String q = "SELECT * FROM datos";
        Cursor registros = database.rawQuery(q, null);
        if(registros.moveToFirst()){
            do{
                lista.add(registros.getString(0));
                lista.add(registros.getString(2));
            }while (registros.moveToNext());
        }
        database.close();
        return lista;

    }

}

