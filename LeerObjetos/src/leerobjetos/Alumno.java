import java.io.Serializable;
import java.util.Date;


public class Alumno implements Serializable{

    int matricula;
    String nombre;
    double notaMedia;
    Date fechaInicio;

    public Alumno(int matricula, String nombre, double notaMedia, Date fechaInicio) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.notaMedia = notaMedia;
        this.fechaInicio = fechaInicio;
    }
    
    public Alumno() {
        
        
    }


   

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getNotaMedia() {
        return notaMedia;
    }

    public void setNotaMedia(double notaMedia) {
        this.notaMedia = notaMedia;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

}
