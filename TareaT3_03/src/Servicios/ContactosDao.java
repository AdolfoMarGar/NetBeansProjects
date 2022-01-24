/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import Controladores.HibernateUtil;
import Modelos.Prestamo;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Adolfo
 */
public class ContactosDao {
    private Session sesion;  
    private Transaction tx;   
 
    public long guardaContacto(Prestamo contacto) throws HibernateException  
    {  
        long id = 0;   
 
        try  
        {  
            iniciaOperacion();  
            id = (Long) sesion.save(contacto);  
            tx.commit();  
        } catch (HibernateException he)  
        {  
            manejaExcepcion(he);  
            throw he;  
        } finally  
        {  
            sesion.close();  
        }   
 
        return id;  
    }   
 
    public void actualizaContacto(Prestamo contacto) throws HibernateException  
    {  
        try  
        {  
            iniciaOperacion();  
            sesion.update(contacto);  
            tx.commit();  
        } catch (HibernateException he)  
        {  
            manejaExcepcion(he);  
            throw he;  
        } finally  
        {  
            sesion.close();  
        }  
    }   
 
    public void eliminaContacto(Prestamo contacto) throws HibernateException  
    {  
        try  
        {  
            iniciaOperacion();  
            sesion.delete(contacto);  
            tx.commit();  
        } catch (HibernateException he)  
        {  
            manejaExcepcion(he);  
            throw he;  
        } finally  
        {  
            sesion.close();  
        }  
    }
    public Prestamo obtenContacto(long idContacto) throws HibernateException  
    {  
        Prestamo contacto = null;   
        try  
        {  
            iniciaOperacion();  
            contacto = (Prestamo) sesion.get(Prestamo.class, idContacto);  
        } finally  
        {  
            sesion.close();  
        }   
 
        return contacto;  
    }   
 
    public List<Prestamo> obtenListaContactos() throws HibernateException  
    {  
        List<Prestamo> listaContactos = null;   
 
        try  
        {  
            iniciaOperacion();  
            listaContactos = sesion.createQuery("from Prestamo").list();  
        } finally  
        {  
            sesion.close();  
        }   
 
        return listaContactos;  
    }   
 
    private void iniciaOperacion() throws HibernateException  
    {  
        sesion = HibernateUtil.getSessionFactory().openSession();  
        tx = sesion.beginTransaction();  
    }   
 
    private void manejaExcepcion(HibernateException he) throws HibernateException  
    {  
        tx.rollback();  
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he);  
    }  
} 

