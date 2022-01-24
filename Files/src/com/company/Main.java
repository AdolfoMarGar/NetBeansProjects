package com.company;

import java.io.*;

public class Main {

    public static void leer (File archivo) throws FileNotFoundException {
        int aux;
        FileInputStream FiS = new FileInputStream(archivo);
        try{
            do {
                aux=FiS.read();
                if (aux!=-1)System.out.println(aux);
            }while (aux!=-1);
            FiS.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void escribir(File archivo) throws FileNotFoundException {
        FileOutputStream FoS = new FileOutputStream(archivo);
        try{
            for (int i=0;i<=150;i++){
                FoS.write(i);
            }
            FoS.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            File a = new File("C:\\Users\\Usuario_2DAM\\Desktop\\Archivo.txt");
            escribir(a);
            leer(a);
        }catch(FileNotFoundException ex){
            System.out.println("Se ha encontrado una excepcion"+ex.getMessage());
        }
    }
}
