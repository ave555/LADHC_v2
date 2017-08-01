package ave.ladhc;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import ave.ladhc.util.BD;
import ave.ladhc.util.TextChangedListener;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver miReceptor;
    IntentFilter intentFilter;

    public static final int PICK_CONTACT_REQUEST = 1 ;
    private Uri contactUri;
    Spinner color;
    String datoscolor[]={"Escoje un color...","ROJO","VERDE","ROSA","PURPURA","AMARILLO","NARANJA","AZUL","AZUL CLARO"};
    private ListView contactsListView;
    private EditText txtNombre,txtTelefono;
    String txtColor;
    private Button btnAgregar;

    private IntentFilter myFilter;

    ArrayList<String> lista;
    ArrayAdapter adaptador;

    ListView listaDispositivos;
    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> dispVinculados; //declara nombre del conjunto de objetos.
    public static String EXTRA_ADDRESS = "device_address";
   String address = null;
    private ProgressDialog progress;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       //´´ myFilter = new IntentFilter();
        //´´myFilter.addAction("android.intent.action.MAIN");


        miReceptor=new MiBroadCastReceiver();
       intentFilter=new IntentFilter("android.intent.action.MAIN");

        inicializarComponenentes();
        //inicializarLista();
        inicializarTabs();
        ArrayAdapter<String> frutaadaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datoscolor);
        color.setAdapter(frutaadaptador);
        inicializaColores();




        }





   /* private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String myParam = intent.getExtras().getString("numero");
            if (myParam != null) {
                //Aquí ejecutais el método que necesiteis, por ejemplo actualizar //el número de notificaciones recibidas
                Toast.makeText(context, "Call from: " + myParam + " ", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, myFilter);
    }*/



     @Override
        public void onResume(){
            super.onResume();
            registerReceiver(miReceptor,intentFilter);
    }

    @Override
    public void onPause(){
        super.onPause();

        unregisterReceiver(miReceptor);
    }

    public class MiBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent i){
           Toast.makeText(context,"Recibiendo, " +
                   "valor reibido: " + i.getStringExtra("numero"),Toast.LENGTH_LONG).show();

        }
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
        contactsListView=(ListView) findViewById(R.id.listView);
        color=(Spinner)findViewById(R.id.spinnercolor);
        listaDispositivos = (ListView) findViewById(R.id.lista);


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
                    case 0:txtColor="     General";
                        break;
                    case 1:txtColor="     Rojo";
                        break;
                    case 2:txtColor="     Verde";
                        break;
                    case 3:txtColor="     Rosa";
                        break;
                    case 4:txtColor="     Purpura";
                        break;
                    case 5:txtColor="     Amarillo";
                        break;
                    case 6:txtColor="     Naranja";
                        break;
                    case 7:txtColor="     Azul";
                        break;
                    case 8:txtColor="     Azul Claro ";
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
       // Obtener instancias de los Views

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

    public void onclickbtnAgregarABD(View view) {


        if(contactUri!=null) {

            BD db= new BD(getApplicationContext(),null,null,1);
            String noombre = txtNombre.getText().toString();
            String teelefono = txtTelefono.getText().toString();
            String coolores=txtColor;
            String mensaje = db.guardar(noombre,teelefono,coolores);
            Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();

        }







        //String mesg=String.format("%s ha sido agregado a ala lista!", txtNombre.getText());
        //Toast.makeText(this,mesg, Toast.LENGTH_SHORT).show();
        btnAgregar.setEnabled(false);
        limpiarCampos();

        contactsListView=(ListView)findViewById(R.id.listView);
        BD db = new BD(getApplicationContext(),null,null,1);
        lista=db.llenar_lv();
        adaptador=new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        contactsListView.setAdapter(adaptador);





    }

    private void limpiarCampos() {
        txtNombre.getText().clear();
        txtTelefono.getText().clear();
        txtColor="";
        txtNombre.requestFocus();
    }



    //prender bluetooth/////////////////////////////////////////////////////////////////////



    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
    public void btbinculados(View view) {

        //Declaramos nuestros componenetes ralcionandolos con los del layout


        //Comprobamos que el dispositivo tiene bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (myBluetooth == null) {
            //Mostramos un mensaje, indicando al usuario que no tiene conexión bluetooth disponible
            Toast.makeText(getApplicationContext(), "Bluetooth no disponible", Toast.LENGTH_LONG).show();

            //finalizamos la aplicación
            finish();
        } else if (!myBluetooth.isEnabled()) {
            //Preguntamos al usuario si desea encender el bluetooth
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }
        //al selecionar btnVinculado se mostra los dispositivos vinculado
        listaDispositivosvinculados();
    }

    private void listaDispositivosvinculados() {

        dispVinculados = myBluetooth.getBondedDevices();  //Devuelve el conjunto de objetos BluetoothDevice que están enlazados (emparejados) al adaptador local.
        ArrayList list = new ArrayList(); //creamos un nuevo  array

        if (dispVinculados.size()>0) //  entramos si el tamaño de dispositivos vinculados es mayor a cero
        {
            for(BluetoothDevice bt : dispVinculados) //Es un for each, recorrera todos los elementos de la varialble disVinculados, que son de tipo BluetoothDevice
            // en cada iteración el valor de ese elemento  de tipo BluetoothDevice sera colocado en la variable indicada en
            //en este caso la bt, como ahí lo dice BluetoothDevice bt
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Obtenemos los nombres y direcciones MAC de los disp. vinculados
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No se han encontrado dispositivos vinculados", Toast.LENGTH_LONG).show();
        }
        //final indica que a esa variable solo se le puede asignar un valor u objeto una única vez.
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listaDispositivos.setAdapter(adapter);//guardamos los dispositivos en la ListView
        listaDispositivos.setOnItemClickListener(myListClickListener);//al hacer click en algun dispositivos se ejecuta la  función myListClickListener


    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        //Método de devolución de llamada a invocarse cuando se ha hecho clic en un elemento de este AdapterView.
        //(AdapterView<?> parent, View view, int position, long id)
        public void onItemClick (AdapterView<?> av,//el AdapterView donde ocurrió el clic.
                                 View v,           //La vista dentro del AdapterView que se hizo clic (esta será una vista proporcionada por el adaptador)
                                 int arg2,         //La posición de la vista en el adaptador.
                                 long arg3)       //Id de la fila del elemento al que se hizo clic.
        {
            // Obtener la dirección MAC del dispositivo, los últimos 17 caracteres en la vista
            String info = ((TextView) v).getText().toString();
            address = info.substring(info.length() - 17);
            new ConnectBT().execute(); //Llamar a la clase para conectarse
        }
    };

    public void DesconectarBt(View view) {
        if (btSocket!=null)
        {
            try
            {
                btSocket.close();
            }
            catch (IOException e)
            { msg("Error");}
        }
        Toast.makeText(getApplicationContext(), "Bluetooth Desconectado", Toast.LENGTH_LONG).show();
        //finish();
    }

    public void prender(View view) {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("led2".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(MainActivity.this, "Conectando Bluetooth...", "Espera!!!");
        }

        @Override
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//conectamos al dispositivo y chequeamos si esta disponible
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Conexión Fallida");
                finish();
            }
            else
            {
                msg("Conectado");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }





}
