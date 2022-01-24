
import java.util.Comparator;

// COMPLETA EL CÓDIGO CON LINEAS SUSPENSIVAS........

public class Comparador_Alumno implements Comparator<Alumno> {

// este método de comparación es para ordenar ascendentemente por el nombre
    @Override
    public int compare ( Alumno al1, Alumno al2 ){
        return al1.getNombre().compareTo(al2.getNombre());
    }
}

