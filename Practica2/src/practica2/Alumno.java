/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2;

/**
 *
 * @author Usuario_2DAM
 */
public class Alumno {
    private int num_mat;
    private double nota;
    
    public Alumno(int num_mat,double nota){
    this.num_mat=num_mat;
    this.nota=nota;
    }
    public Alumno(){}
    
    public int getNum_mat() {
        return num_mat;
    }

    public void setNum_mat(int num_mat) {
        this.num_mat = num_mat;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
    
}
