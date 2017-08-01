package ave.ladhc;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;

import ave.ladhc.util.BD;
import ave.ladhc.util.Contactos;
import ave.ladhc.util.TextChangedListener;

public class MainActivity extends AppCompatActivity {
    public static final int PICK_CONTACT_REQUEST = 1 ;
    private Uri contactUri;
    Spinner color;
    String datoscolor[]={"Escoje un color...","ROJO","VERDE","ROSA","PURPURA","AMARILLO","NARANJA","AZUL","AZUL CLARO"};
    private ArrayAdapter<Contactos>adapter;
    private ListView contactsListView;
    private EditText txtNombre,txtTelefono;
   String txtColor;
    private Button btnAgregar;
    ListView lv;
    ArrayList<String> lista;
    ArrayAdapter adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarComponenentes();
        inicializarLista();
        inicializarTabs();
        ArrayAdapter<String> frutaadaptador=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,datoscolor);
        color.setAdapter(frutaadaptador);
        inicializaColores();

    }

    private void inicializarLista() {

        //contactsListView=(ListView)findViewById(R.id.listas);
            BD db = new BD(getApplicationContext(),null,null,1);
            lista=db.llenar_lv();
            adaptador=new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        contactsListView.setAdapter(adaptador);



    }
    private void inicializarTabs() {
        TabHost tabHost=(TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec spec = tabHost.newTabSpec("tab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Crear");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tab1");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Lista");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Dispositivos");
        tabHost.addTab(spec);

    }

    private void inicializarComponenentes() {
        txtNombre=(EditText) findViewById(R.id.ediNombre);
        txtTelefono=(EditText) findViewById(R.id.ediTelefono);
        //txtColor=(EditText) findViewById(R.id.ediColor);
        contactsListView=(ListView) findViewById(R.id.listView);
        color=(Spinner)findViewById(R.id.spinnercolor);





        txtNombre.addTextChangedListener(new TextChangedListener(){
            @Override//este metodo se va ejcutar cada que el usuario escriba algo
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //super.onTextChanged(s, start, before, count);
                btnAgregar = (Button)findViewById(R.id.btnAgregar);
                btnAgregar.setEnabled(!s.toString().trim().isEmpty());
            }
        });
    }

    private void inicializaColores() {
        color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case 0:txtColor="-General";
                        break;
                    case 1:txtColor="-Rojo";
                        break;
                    case 2:txtColor="-Verde";
                        break;
                    case 3:txtColor="-Rosa";
                        break;
                    case 4:txtColor="-Purpura";
                        break;
                    case 5:txtColor="-Amarillo";
                        break;
                    case 6:txtColor="-Naranja";
                        break;
                    case 7:txtColor="-Azul";
                        break;
                    case 8:txtColor="-Azul Claro ";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void initBusquedaContactos(View v){

        /*
        Crear un intent para seleccionar un contacto del dispositivo
         */
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        /*
        Iniciar la actividad esperando respuesta a través
        del canal PICK_CONTACT_REQUEST
         */
        startActivityForResult(i, PICK_CONTACT_REQUEST);
    }  //busqueda de contactos


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                contactUri = intent.getData();
                renderContact(contactUri);
            }
        }
    }//revisar resultados de contacto

    private void renderContact(Uri uri) {
        /*
        Obtener instancias de los Views
         */
        //TextView contactName = (TextView)findViewById(R.id.contactName);
       // TextView contactPhone = (TextView)findViewById(R.id.contactPhone);

        //nombre=(TextView) findViewById(R.id.contactName);
        //telefono=(TextView) findViewById(R.id.contactPhone);


        /*
        Setear valores
         */
        txtNombre.setText(getName(uri));
        txtTelefono.setText(getPhone(uri));
    }       //guardar nombre  y telefono

    private String getPhone(Uri uri) {
        /*
        Variables temporales para el id y el teléfono
         */
        String id = null;
        String phone = null;

        /************* PRIMERA CONSULTA ************/
        /*
        Obtener el _ID del contacto
         */
        Cursor contactCursor = getContentResolver().query(
                uri,
                new String[]{ContactsContract.Contacts._ID},
                null,
                null,
                null);


        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();

        /************* SEGUNDA CONSULTA ************/
        /*
        Sentencia WHERE para especificar que solo deseamos
        números de telefonía móvil
         */
        String selectionArgs =
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE+"= " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

        /*
        Obtener el número telefónico
         */
        Cursor phoneCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
                selectionArgs,
                new String[] { id },
                null
        );
        if (phoneCursor.moveToFirst()) {
            phone = phoneCursor.getString(0);
        }
        phoneCursor.close();

        return phone;
    }         //obtenemos telefono

    private String getName(Uri uri) {

        /*
        Valor a retornar
         */
        String name = null;

         /*
        Obtener una instancia del Content Resolver
         */
        ContentResolver contentResolver = getContentResolver();

        /*
        Consultar el nombre del contacto
         */
        Cursor c = contentResolver.query(
                uri,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                null,
                null,
                null);

        if(c.moveToFirst()){
            name = c.getString(0);
        }

        /*
        Cerramos el cursor
         */
        c.close();

        return name;
    }          //ontenemos nombre

    public void onclickbtnAgregar(View view) {


        if(contactUri!=null) {

            BD db= new BD(getApplicationContext(),null,null,1);
            String noombre = txtNombre.getText().toString();
            String teelefono = txtTelefono.getText().toString();
            String coolores=txtColor;
            String mensaje = db.guardar(noombre,teelefono,coolores);
            Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();

        }






        agregarContacto(
                txtNombre.getText().toString(),
                txtTelefono.getText().toString(),
                txtColor
        );
        String mesg=String.format("%s ha sido agregado a ala lista!", txtNombre.getText());
        Toast.makeText(this,mesg, Toast.LENGTH_SHORT).show();
        btnAgregar.setEnabled(false);
        limpiarCampos();




    }

    private void agregarContacto(String nombre, String telefono, String color) {
        Contactos nuevo= new Contactos(nombre,telefono,color);
        adapter.add(nuevo);
    }

    private void limpiarCampos() {
        txtNombre.getText().clear();
        txtTelefono.getText().clear();
        txtColor="";
        txtNombre.requestFocus();
    }

    public void listaon (View v){

        Intent intento = new Intent(MainActivity.this,ListaBD.class);
        startActivity(intento);
    }



}
