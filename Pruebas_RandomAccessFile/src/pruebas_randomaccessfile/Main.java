/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebas_randomaccessfile;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adolfo
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        RandomAccessFile archivo;
        Alumno alumno1 = new Alumno(0,2.3);
        Alumno alumno2 = new Alumno(20,7.3);
        Alumno alumno3 = new Alumno(5,8.3);


        try{
            archivo = new RandomAccessFile("datos.txt","rw");
            archivo.seek(0*12);
            archivo.writeInt(alumno1.getNum_mat());
            archivo.writeDouble(alumno1.getNota());
            archivo.seek(20*12);
            archivo.writeInt(alumno2.getNum_mat());
            archivo.writeDouble(alumno2.getNota());
            archivo.seek(5*12);
            archivo.writeInt(alumno3.getNum_mat());
            archivo.writeDouble(alumno3.getNota());
            archivo.close();
        
        
        }catch(FileNotFoundException e){
            System.out.println("No sa encontrado el archivo");
        } catch (IOException ex) {
            System.out.println("Error escritura/lectura");
        }
        try{
            int num_mat;
            double nota;
            archivo = new RandomAccessFile("datos.txt","rw");
            num_mat=archivo.readInt();
            nota=archivo.readDouble();
            System.out.println("Alumno1");
            System.out.println("nºmat: "+num_mat);
            System.out.println("nota: "+nota);
            do{
                num_mat=archivo.readInt();
                nota=archivo.readDouble();
                System.out.println("Alumno1");
                System.out.println("nºmat: "+num_mat);
                System.out.println("nota: "+nota);
            }while(num_mat!=-1);
           
            
            
        
        
        }catch(EOFException ex){
            System.out.println("Fin del archivo");
        }catch(FileNotFoundException e){
            System.out.println("No sa encontrado el archivo");
        } catch (IOException ex) {
            System.out.println("Error escritura/lectura");
        }
    }
    
}
