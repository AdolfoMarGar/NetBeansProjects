/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import Modelos.Prestamo;
import java.util.List;

/**
 *
 * @author Adolfo
 */
public class Main {
    public static void main(String[] args)  
    {  
        ContactosDao contactosDAO = new ContactosDao();  
        Prestamo contactoRecuperado = null;   
        long idAEliminar = 0;   
 
        //Creamos tes instancias de Contacto  
        Prestamo contacto1 = new Prestamo();  
        Prestamo contacto2 = new Prestamo();  
        Prestamo contacto3 = new Prestamo();   
 
        //Guardamos las tres instancias, guardamos el id del contacto1 para usarlo despues   
 
        //Modificamos el contacto 2 y lo actualizamos  
        //contacto2.setNombre("Nuevo Contacto 2");  
 
        //Recuperamos el contacto1 de la base de datos  
//        System.out.println("Recuperamos a " + contactoRecuperado.getNombre());   
 
        //Eliminamos al contactoRecuperado (que es el contacto3)  
 
        //Obtenemos la lista de contactos que quedan en la base de datos y la mostramos  
        List<Prestamo> listaContactos = contactosDAO.obtenListaContactos();   
        System.out.println("Hay " + listaContactos.size() + "contactos en la base de datos");   
 
        for(Prestamo c : listaContactos)  
        {  
            System.out.println("-> " + c.getImporte());  
        }  
    } 
}
