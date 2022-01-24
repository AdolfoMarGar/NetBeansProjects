package MegaProyectoInfernal;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Usuario_2DAM
 */
public class Alumno {
    int num_mat, edad;
    String nombre;
    Date fecha_nac;
    double nota_med;

    public Alumno(){}

    public Alumno(int num_mat,String nombre,Date fecha_nac,double nota_med,int edad){
        this.num_mat=num_mat;
        this.nombre=nombre;
        this.fecha_nac=fecha_nac;
        this.nota_med=nota_med;
        this.edad=generarEdad(fecha_nac);

    }
    
    private int generarEdad(Date fecha){
        int año, calculo_edad;
        LocalDate fecha_actual = LocalDate.now();
        año=fecha.getYear()+1900;
        calculo_edad=(fecha_actual.getYear())-año;
        
        return calculo_edad;
    }

    public int getNum_mat() {
        return num_mat;
    }

    public void setNum_mat(int num_mat) {
        this.num_mat = num_mat;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(Date fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public double getNota_med() {
        return nota_med;
    }

    public void setNota_med(double nota_med) {
        this.nota_med = nota_med;
    }
}
