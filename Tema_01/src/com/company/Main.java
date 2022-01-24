package com.company;
import java.io.File;
public class Main {

    public static void bucle(File []v, int cont, String args){
        try {
            if (v != null) {
                for (File a : v) {
                    imp_pant(a, cont, args);
                    if (a.isDirectory()) {
                        cont++;
                        bucle(a.listFiles(), cont, args);
                        cont--;
                    }
                }
            }
        }catch(NullPointerException e){
            System.out.print("No se ha encontrado el directorio o archivo");
        }catch(SecurityException e){
            System.out.print("Error al acceder al directorio o archivo");
        }
    }

    public static void imp_pant(File a, int cont, String args){
        System.out.print(args+":\\");
        if (cont > 0) {
            for (int i = 0; i < cont; i++) {
                System.out.print("...\\");
            }
        }
        System.out.println(a.getName());
    }

    public static void main(String[] args) {
        try {
            int cont = 0;

            String arg = args[0];
            //String arg = "C:\\Users\\Adolfo\\Documents";

            File dir = new File(arg);
            File[] g1 = dir.listFiles();

            bucle(g1, cont, arg);

        }catch(NullPointerException e){
            System.out.print("No se ha encontrado el directorio o archivo");
        }catch(IllegalArgumentException e){
            System.out.print("No son correctos los parametros introducidos");
        }catch(SecurityException e){
            System.out.print("Error al acceder al directorio o archivo");
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.print("Error al reccorrer el array por longitud");
        }
    }
}