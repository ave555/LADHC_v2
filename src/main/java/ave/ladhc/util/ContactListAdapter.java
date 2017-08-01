package ave.ladhc.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ave.ladhc.R;

/**
 * Created by Lenovo on 28/07/2017.
 */

public class ContactListAdapter extends ArrayAdapter<Contactos>{

    private Activity ctx;//hace ilucion a que es un contexto

    public  ContactListAdapter(Activity context, List<Contactos> contactos){
        super (context, R.layout.listviewcontacts,contactos);
        this.ctx=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        if (view == null) {
            view = ctx.getLayoutInflater().inflate(R.layout.listviewcontacts, parent, false);
        }
        Contactos actual = this.getItem(position);
        inicializarCamposdeDatos(view, actual);
        return view;

    }

    private void inicializarCamposdeDatos(View view, Contactos actual) {
        TextView textview=(TextView) view.findViewById(R.id.viewNombre);
        textview.setText(actual.getNombre());
        textview=(TextView) view.findViewById(R.id.viewTelefono);
        textview.setText(actual.getTelefono());
        textview=(TextView) view.findViewById(R.id.viewColor);
        textview.setText(actual.getColor());
    }
}