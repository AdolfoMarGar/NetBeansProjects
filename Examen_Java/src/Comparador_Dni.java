
import java.util.Comparator;

//Lo he hecho asi pq me parecio sencillo.

public class Comparador_Dni implements Comparator<Alumno> {


    @Override
    public int compare ( Alumno al1, Alumno al2 ){
        if(al1.getDNI()> al2.getDNI())return 1;
        else return -1;
    }
}

