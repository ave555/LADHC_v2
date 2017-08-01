package ave.ladhc.util;

/**
 * Created by Lenovo on 30/07/2017.
 */

public class Contactos {
    private String Nombre,Telefono,Color;

    public Contactos(String nombre, String telefono, String color) {
        Nombre = nombre;
        Telefono = telefono;
        Color = color;
    }

    //<editor-fold desc="metodos GET">
    public String getNombre() {
        return Nombre;
    }

    public String getTelefono() {
        return Telefono;
    }

    public String getColor() {
        return Color;
    }
    //</editor-fold>
    //<editor-fold desc="metod SET">
    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public void setColor(String color) {
        Color = color;
    }
    //</editor-fold>
}

