package com.company;
import java.io.File;
public class Ej2 {
    public static void main(String[] args) {
        try {
            int cont = 0;
            //String arg = args[0];
            String arg = "C:\\Users\\Adolfo\\Documents";
            File dir = new File(arg);
            File[] g1 = dir.listFiles();
            bucle(g1, cont);

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

    public static void bucle(File []v, int cont){
        try {
            if (v != null) {
                for (File a : v) {
                    imp_pant(a, cont);
                    if (a.isDirectory()) {
                        cont++;
                        bucle(a.listFiles(), cont);
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

    public static void imp_pant(File a, int cont){
        if (cont > 0) {
            for (int i = 0; i < cont; i++) {
                System.out.print("\t");
            }
        }
        System.out.print(a.getName()+", "+a.getTotalSpace()+", "+a.getAbsolutePath());
        if(a.isFile())System.out.println(", Es archivo");
        if(a.isDirectory())System.out.println(", Es Directorio");
    }
}

