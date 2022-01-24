package com.company;
import java.io.File;

public class Ej1 {
    public static void main(String[] args) throws NullPointerException {
        File ubicacion = new File(".");

            String [] lista = ubicacion.list();
            if (lista!=null) {
                for (String a : lista) {
                    System.out.println(a);
                }
            }

    }
}
