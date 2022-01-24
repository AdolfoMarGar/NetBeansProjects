import java.io.Serializable;

// NO MODIFIQUES ESTA CLASE....................

public class Alumno implements Serializable {
    // Atributos
    private String nombre;
    private int DNI;
    private int edad;
    private boolean sexo; // masculino = true, femenino = false
    private int faltas;

    // Constructor


    public Alumno(String nombre, int DNI, int edad, boolean sexo, int faltas) {
        this.nombre = nombre;
        this.DNI = DNI;
        this.edad = edad;
        this.sexo = sexo;
        this.faltas = faltas;
    }

    // Getter y Setter

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDNI() {
        return DNI;
    }

    public void setDNI(int DNI) {
        this.DNI = DNI;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public boolean isSexo() {
        return sexo;
    }

    public void setSexo(boolean sexo) {
        this.sexo = sexo;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    @Override
    public String toString() {
        String cad = "masculino";
        if (sexo == true)
            cad = "masculino";
        else
            cad = "femenino";

        return "Alumno{" +
                "nombre='" + nombre + '\'' +
                ", DNI=" + DNI +
                ", edad=" + edad +
                ", sexo=" + cad +
                ", faltas=" + faltas +
                '}';
    }
}
