/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import Controladores.HibernateUtil;
import Modelos.Articulos;
import Modelos.Clientes;
import Modelos.EstadisticasClientes;
import Modelos.FacturasCab;
import Modelos.FacturasLin;
import Modelos.FacturasTot;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.exception.DataException;

/**
 *
 * @author Adolfo
 */
public class Metodos {
    private Session ss;
    private Transaction tx;  

    //metodo que abre una sesion de hibernate.
    public void iniciaOperacion() throws HibernateException  
    {  
        ss = HibernateUtil.getSessionFactory().openSession();  
        tx = ss.beginTransaction();  
    }
    
    //datos tabla Clientes obtenidos con criteria
    public ArrayList<Clientes> getListaClientes(){  
        ArrayList<Clientes>listaClientes = new ArrayList<>();
        try{  
            iniciaOperacion();
            Criteria crit = ss.createCriteria(Clientes.class);
            List<Clientes> lista_aux = crit.list();
            
            for(Clientes var:lista_aux){
                listaClientes.add(var);
            }
        }catch(HibernateException e){
            Util.mensaje_error("Error", e.getMessage());
        }finally{  
            ss.close();  
        }   
        return listaClientes;  
        
    }

    //datos tabla Articulos obtenidos con criteria
    public ArrayList<Articulos> getListaArticulos(){  
        ArrayList<Articulos>listaArticulos = new ArrayList<>();
        try{  
            iniciaOperacion();
            Criteria crit = ss.createCriteria(Articulos.class);
            List<Articulos> lista_aux= crit.list();
            for(Articulos var:lista_aux){
                listaArticulos.add(var);
            }
        }catch(HibernateException e){
            Util.mensaje_error("Error", e.getMessage());
        }finally{  
            ss.close();  
        }   
        return listaArticulos;  
    }
    
    public ArrayList<Articulos> getListaArticulosConsulta(String consulta){  
        ArrayList<Articulos>listaArticulos = new ArrayList<>();
        try{  
            iniciaOperacion();
            List<Articulos> lista_aux =  ss.createQuery(consulta).list();
            for(Articulos var:lista_aux){
                listaArticulos.add(var);
            }
        }catch(HibernateException e){
            Util.mensaje_error("Error.", "Error en la sintaxis de la sentencia.");
        }catch(Exception e){
            Util.mensaje_error("Error.", "Error en la sintaxis de la sentencia.");
        }finally{  
            ss.close();  
        }   
        return listaArticulos;  
    }
    
    public ArrayList<Clientes> getListaClientesConsulta(String consulta){  
        ArrayList<Clientes>lista = new ArrayList<>();
        try{  
            iniciaOperacion();
            List<Clientes> lista_aux =  ss.createQuery(consulta).list();
            for(Clientes var:lista_aux){
                lista.add(var);
            }
        }catch(HibernateException e){
            Util.mensaje_error("Error.", "Error en la sintaxis de la sentencia.");
        }catch(Exception e){
            Util.mensaje_error("Error.", "Error en la sintaxis de la sentencia.");
        }finally{  
            ss.close();  
        }   
        return lista;  
    }
    
    public ArrayList<FacturasCab> getListaFCabConsulta(String consulta){  
        ArrayList<FacturasCab>lista = new ArrayList<>();
        try{  
            iniciaOperacion();
            List<FacturasCab> lista_aux =  ss.createQuery(consulta).list();
            for(FacturasCab var:lista_aux){
                lista.add(var);
            }
        }catch(HibernateException e){
            Util.mensaje_error("Error.", "Error en la sintaxis de la sentencia.");
        }catch(Exception e){
            Util.mensaje_error("Error.", "Error en la sintaxis de la sentencia.");
        }finally{  
            ss.close();  
        }   
        return lista;  
    }
    
    public ArrayList<FacturasLin> getListaFLinConsulta(String consulta){  
        ArrayList<FacturasLin>lista = new ArrayList<>();
        try{  
            iniciaOperacion();
            List<FacturasLin> lista_aux =  ss.createQuery(consulta).list();
            for(FacturasLin var:lista_aux){
                lista.add(var);
            }
        }catch(HibernateException e){
            Util.mensaje_error("Error.", "Error en la sintaxis de la sentencia.");
        }catch(Exception e){
            Util.mensaje_error("Error.", "Error en la sintaxis de la sentencia.");
        }finally{  
            ss.close();  
        }   
        return lista;  
    }
    
    public ArrayList<FacturasTot> getListaFTotConsulta(String consulta){  
        ArrayList<FacturasTot>lista = new ArrayList<>();
        try{  
            iniciaOperacion();
            List<FacturasTot> lista_aux =  ss.createQuery(consulta).list();
            for(FacturasTot var:lista_aux){
                lista.add(var);
            }
        }catch(HibernateException e){
            Util.mensaje_error("Error.", "Error en la sintaxis de la sentencia.");
        }catch(Exception e){
            Util.mensaje_error("Error.", "Error en la sintaxis de la sentencia.");
        }finally{  
            ss.close();  
        }   
        return lista;  
    }
    
    
    //datos tabla FacturasCab obtenidos con criteria
    public ArrayList<FacturasCab> getListaFacturasCab(){  
        ArrayList<FacturasCab>listaFacturasCab = new ArrayList<>();
        try{  
            iniciaOperacion();
            Criteria crit = ss.createCriteria(FacturasCab.class);
            List<FacturasCab> lista_aux= crit.list();
            
            for(FacturasCab var:lista_aux){
                listaFacturasCab.add(var);
            }
        }catch(HibernateException e){
            Util.mensaje_error("Error", e.getMessage());
        }finally{  
            ss.close();  
        }   
        return listaFacturasCab;  
    }
 
    //datos tabla FacturasLin obtenidos con criteria
    public ArrayList<FacturasLin> getListaFacturasLin(){  
        ArrayList<FacturasLin>listaFacturasLin = new ArrayList<>();
        try{  
            iniciaOperacion();
            Criteria crit = ss.createCriteria(FacturasLin.class);
            List<FacturasLin> lista_aux= crit.list();
            
            for(FacturasLin var:lista_aux){
                listaFacturasLin.add(var);
            }
        }catch(HibernateException e){
            Util.mensaje_error("Error", e.getMessage());
        }finally{  
            ss.close();  
        }   
        return listaFacturasLin;  
    }
    
    //datos tabla FacturasTot obtenidos con criteria
    public ArrayList<FacturasTot> getListaFacturasTot(){  
        ArrayList<FacturasTot>listaFacturasTot = new ArrayList<>();
        try{  
            iniciaOperacion();
            Criteria crit = ss.createCriteria(FacturasTot.class);
            List<FacturasTot> lista_aux= crit.list();
            
            for(FacturasTot var:lista_aux){
                listaFacturasTot.add(var);
            }
        }catch(HibernateException e){
            Util.mensaje_error("Error", e.getMessage());
        }finally{  
            ss.close();  
        }   
        return listaFacturasTot;  
    }
    
    //datos tabla EstadisticasClientes obtenidos con criteria
    public ArrayList<EstadisticasClientes> getListaEstadisticasClientes(){  
        ArrayList<EstadisticasClientes>listaEstadisticasClientes = new ArrayList<>();
        try{  
            iniciaOperacion();
            Criteria crit = ss.createCriteria(EstadisticasClientes.class);
            List<EstadisticasClientes> lista_aux= crit.list();
            
            for(EstadisticasClientes var:lista_aux){
                listaEstadisticasClientes.add(var);
            }
        }catch(HibernateException e){
            Util.mensaje_error("Error", e.getMessage());
        }finally{  
            ss.close();  
        }   
        return listaEstadisticasClientes;  
    }
    
    //metodo que añade un objeto a traves de hibernate
    public void anadirObjeto(Object objeto){  
        try{  
            iniciaOperacion();  
            ss.save(objeto);  
            tx.commit();
        }catch(DataException de){
            Util.mensaje_error("Error al añadir.", "Error: Valor introducido fuera de los parametros");
        }catch (HibernateException he){  
            Util.mensaje_error("Error añadir", "Error: "+he.getMessage());
        }finally{  
            ss.close();  
        }  
    }
    
    //metodo que borra un objeto a traves de hibernate
    public void borrarObjeto(Object objeto){  
        try{  
            iniciaOperacion();  
            ss.delete(objeto);  
            tx.commit();  
        }catch(DataException de){
            Util.mensaje_error("Error al añadir.", "Error: Valor introducido fuera de los parametros");
        }catch (HibernateException he){  
            
        }finally{  
            ss.close();  
        }  
    }
    
    //metodo que actualiza un objeto a traves de hibernate
    public void actualizarObjeto(Object object){  
        try{  
            iniciaOperacion();  
            ss.update(object);
            tx.commit();  
        }catch (HibernateException he){  
            Util.mensaje_error("Error actualizar", "Error: "+he.getMessage());
        }finally{  
            ss.close();  
        }  
    }
}
