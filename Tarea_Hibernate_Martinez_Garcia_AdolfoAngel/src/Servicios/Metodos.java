/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import Controladores.HibernateUtil;
import Modelos.FormaPago;
import Modelos.Prestamo;
import Modelos.Recibo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;

/**
 *
 * @author Adolfo
 */
public class Metodos {
    private Session ss;
    private Transaction tx;  

    public void iniciaOperacion() throws HibernateException  
    {  
        ss = HibernateUtil.getSessionFactory().openSession();  
        tx = ss.beginTransaction();  
    } 
   
    //datos de la tabla prestamos obtenidos atraves de hql
    //convierto el objeto prestamo en otro pq me daba un error al manejar el objeto luego
     public ArrayList<Object[]> getListaPrestamos(){  
        ArrayList<Object[]>listaPrestamo = new ArrayList<>();
        Prestamo aux_pres;
        try{  
            iniciaOperacion();
            List<Prestamo> lista_aux = ss.createQuery("from Prestamo").list();
            for(Prestamo var: lista_aux){
                aux_pres=var;
                Object [] forma_pago ={
                    aux_pres.getNPrestamo(),aux_pres.getFecha(),aux_pres.getImporte(),
                    aux_pres.getImportePagado(),aux_pres.getFormaPago().getCodigo()
                };
                listaPrestamo.add(forma_pago);
            }
            
        }catch(HibernateException e){
            Util.mensaje_error("Error", e.getMessage());
        }finally{  
            ss.close();  
        }   
        return listaPrestamo;  
    }

    
    //datos tabla formapago obtenidos con criteria
    public ArrayList<FormaPago> getListaFormaPago(){  
        ArrayList<FormaPago>listaFormaPago = new ArrayList<>();
        FormaPago aux_fp=null;
        try{  
            iniciaOperacion();
            Criteria crit = ss.createCriteria(FormaPago.class);
            List<FormaPago> lista_aux= crit.list();
            
            for(FormaPago var:lista_aux){
                listaFormaPago.add(var);
            }
        }catch(HibernateException e){
            Util.mensaje_error("Error", e.getMessage());
        }finally{  
            ss.close();  
        }   
        return listaFormaPago;  
    }
    
    //datos de la tabla recibos obtenidos a traves de criteria
    public ArrayList<Recibo> getListaRecibos(){  
        ArrayList<Recibo>listaRecibos = new ArrayList<>();
        FormaPago aux_recibo=null;
        try{  
            iniciaOperacion();
            Criteria crit = ss.createCriteria(Recibo.class);
            List<Recibo> lista_aux= crit.list();
            
            for(Recibo var:lista_aux){
                listaRecibos.add(var);
            }
        }catch(HibernateException e){
            Util.mensaje_error("Error", e.getMessage());
        }finally{  
            ss.close();  
        }   
        return listaRecibos;  
    }
    
    //metodo que borra un objeto (prestamo/recibo) a traves de hibernate
    public void borrarObjeto(Object objeto){  
        try{  
            iniciaOperacion();  
            ss.delete(objeto);  
            tx.commit();  
        }catch(DataException de){
            Util.mensaje_error("Error al añadir.", "Error: Valor introducido fuera de los parametros");
        }catch (HibernateException he){  
            Util.mensaje_error("Error", "Error: "+he.getMessage());
        }finally{  
            ss.close();  
        }  
    }
    
    //metodo que actualiza un objeto (prestamo/recibo) a traves de hibernate
    public void actualizarObjeto(Object object){  
        try{  
            iniciaOperacion();  
            ss.update(object);  
            tx.commit();  
        }catch (HibernateException he){  
            he.getMessage();
        }finally{  
            ss.close();  
        }  
    }   
    
    //Metodo que guarda un prestamo a traves de hibernate
    public void guardarPrestamo(Prestamo prestamo){
        try{  
            BigDecimal idPrestamo = prestamo.getNPrestamo();
            iniciaOperacion();  
            idPrestamo = (BigDecimal) ss.save(prestamo);  
            tx.commit();  
        }catch(ConstraintViolationException cve){
            Util.mensaje_aviso("Error al añadir.", "No añadido: Clave Primaria repetida");
        }catch(DataException de){
            Util.mensaje_error("Error al añadir.", "Error: Valor introducido fuera de los parametros");
        }finally{  
            ss.close();  
        }   
 
    }   
    
}
