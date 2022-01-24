/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import Controladores.HibernateUtil;
import Modelos.Prestamo;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Adolfo
 */
public class Info {
    private Session ss;
    private Transaction tx;  

    private void iniciaOperacion() throws HibernateException  
    {  
        ss = HibernateUtil.getSessionFactory().openSession();  
        tx = ss.beginTransaction();  
    } 
   
    
    public List<Prestamo> getListaPrestamos(){  
        List<Prestamo> listaPrestamo = null;   
 
        try{  
            iniciaOperacion();  
            listaPrestamo = ss.createQuery("from Prestamo").list();
        }catch(Exception e){
            Util.mensaje_error("Error", e.getMessage());
        }finally{  
            ss.close();  
        }   
 
        return listaPrestamo;  
    }
    
    
}
