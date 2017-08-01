package ave.ladhc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ave.ladhc.util.BD;

public class ListaBD extends AppCompatActivity {
    ListView lv;
    ArrayList<String> lista;
    ArrayAdapter adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_bd);
        lv=(ListView)findViewById(R.id.listas);
        BD db = new BD(getApplicationContext(),null,null,1);
        lista=db.llenar_lv();
        adaptador=new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        lv.setAdapter(adaptador);

    }
}
