package Interfaces;

import Modelos.*;
import Servicios.*;
import java.awt.Component;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JDialog;
import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.*;
import org.w3c.dom.DOMImplementation;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author Adolfo
 */

public class Interfaz extends javax.swing.JFrame {
    //Creamos un objeto Metodos para utilizar las funcinalidades programadas en la clase Metodos del paquete Servicios.
    private final Metodos metodos = new Metodos();

    //Declaración de variables globales auxiliares.
    Articulos art_dat = null;
    Clientes cli_dat = null;
    FacturasCab fCab_dat = null;
    FacturasLin fLin_dat = null;
    FacturasTot fTot_dat = null;
    EstadisticasClientes eCli_mod = null;
    //Posicion en la jTable del elemento seleccionado
    int posicion = -1;
    //Para las consultas
    String con_base_Art ="FROM Articulos as A WHERE A.";
    String con_base_Cli ="FROM Clientes as C WHERE C.";
    String con_base_FCab ="FROM FacturasCab as FCab WHERE FCab.";
    String con_base_FLin ="FROM FacturasLin as FLin WHERE FLin.";
    String con_base_FTot ="FROM FacturasTot as FTot WHERE FTot.";


    //Declaración de los arraylist que se usan.
    ArrayList<Articulos> listaArticulos = null;
    ArrayList<Clientes> listaClientes = null;
    ArrayList<FacturasCab> listaFacturasCab = null;
    ArrayList<FacturasLin> listaFacturasLin = null;
    ArrayList<FacturasTot> listaFacturasTot = null;
    ArrayList<EstadisticasClientes> listaEst_Clientes = null;
    ArrayList<Articulos> listaArtConsulta = null;
    ArrayList<Clientes> listaCliConsulta = null;
    ArrayList<FacturasCab> listaFCabConsulta = null;
    ArrayList<FacturasLin> listaFLinConsulta = null;
    ArrayList<FacturasTot> listaFTotConsulta = null;


    //Declaración de las cabeceras de las tablas.
    String []atributos_articulos = {"Referencia","Descripcion","Precio","IVA","Stock"};
    String []atributos_clientes = {"DNI/CIF","Nombre"};
    String []atributos_FacturasCab = {"Nº Factura","Fecha","DNI/CIF"};
    String []atributos_FacturasLin = {"Nº Factura","Linea","Referencia","Cantidad","Precio","Descuento","IVA"};
    String []atributos_FacturasTot = {"Nº Factura","Importe base","Importe Dtos","Importe IVA","Total factura"};
    String []atributos_estClientes = {"Año","Mes(num)","Mes","DNI/CIF","Nombre","Suma Dtos","Suma IVA","Suma total"};

    //Segun la tabla seleccionada en el jComboBox de tablas en el JFrame 
    //rellena la jTable con los datos pertinentes
    private void getTablaSeleccionadaJCombo(){
        String tabla_sel = JCombo_Tablas.getSelectedItem().toString();
        switch(tabla_sel){
            case "Articulos" :
                crearTablaArticulos();
                jButton_AltaPrincipal.setEnabled(true);
                jButton_BajaPrincipal.setEnabled(true);
            break;

            case "Clientes" :
                crearTablaClientes();
                jButton_AltaPrincipal.setEnabled(true);
                jButton_BajaPrincipal.setEnabled(true);
            break;

            case "FacturasLin" :
                crearTablaFacturasLin();
                jButton_AltaPrincipal.setEnabled(true);
                jButton_BajaPrincipal.setEnabled(true);
            break;

            case "FacturasCab" :
                crearTablaFacturasCab();
                jButton_AltaPrincipal.setEnabled(true);
                jButton_BajaPrincipal.setEnabled(true);
            break;

            case "FacturasTot" :
                crearTablaFacturasTot();
                jButton_AltaPrincipal.setEnabled(false);
                jButton_BajaPrincipal.setEnabled(false);

            break;

            case "EstadisticasClientes" :
                crearTablaEst_Clientes();
                jButton_AltaPrincipal.setEnabled(false);
                jButton_BajaPrincipal.setEnabled(false);
            break;

        }
    }

    //Inicia un jDialog u otro segun la tabla seleccionada
    private void getTablaSeleccionadaJButton(){
        String tabla_sel = JCombo_Tablas.getSelectedItem().toString();
        switch(tabla_sel){
            case "Articulos" :
                iniciar_jDialog(jDialog_Articulo);
            break;

            case "Clientes" :
                iniciar_jDialog(jDialog_Cliente);
            break;

            case "FacturasLin" :
                jComboBox_referenciaFLin.removeAllItems();
                jComboBox_nfacturaFLin.removeAllItems();

                rellenarJComboReferenciaFLin();
                rellenarJComboNFacturaFLin();
                selecJCombo();
                iniciar_jDialog(jDialog_FacturaLin);
            break;

            case "FacturasCab" :
                jComboBox_dniFCab.removeAllItems();
                rellenarJComboDniFCab();
                iniciar_jDialog(jDialog_FacturaCab);
            break;
        }
    }
    
    //Inicia un jDialog u otro segun la tabla seleccionada
    private void getTablaSeleccionadaConsulta(){
        String tabla_sel = JCombo_Tablas.getSelectedItem().toString();
        switch(tabla_sel){
            case "Articulos" :
                iniciar_jDialog(jDialog_Consulta_Art);
            break;

            case "Clientes" :
                iniciar_jDialog(jDialog_Consulta_Cli);
            break;

            case "FacturasLin" :
                iniciar_jDialog(jDialog_Consulta_FLin);
            break;

            case "FacturasCab" :
                iniciar_jDialog(jDialog_Consulta_FCab);
            break;
            
            case "FacturasTot" :
                iniciar_jDialog(jDialog_Consulta_FTot);
            break;
            
        }
    }
    
    //selecciona las posiciones de los jcombobox del jdialog de FLin si pos !=-1
    private void selecJCombo(){
        if(posicion!=-1){
            int cont=0;
            String ref = String.valueOf(tabla.getValueAt(posicion, 2));
            String nfac = String.valueOf(tabla.getValueAt(posicion, 0));
            int pos_ref=0;
            int pos_nfac=0;

            for(Articulos var : listaArticulos){
                if(var.getReferencia().equals(ref)){
                    pos_ref=cont;
                }
                cont++;
            }
            jComboBox_referenciaFLin.setSelectedIndex(pos_ref);

            cont=0;
            for(FacturasCab var : listaFacturasCab){
                if(var.getNumfac()==Long.valueOf(nfac)){
                    pos_nfac=cont;
                }
                cont++;
            }
            jComboBox_nfacturaFLin.setSelectedIndex(pos_nfac);
        }
    }
    
    //Rellena los jTextField y demas elementos pertinentes si se ha seleccionado
    //algo en el jTable.
    private void getTablaSeleccionadaJtable(int posicion){
        String tabla_sel = JCombo_Tablas.getSelectedItem().toString();
        switch(tabla_sel){
            case "Articulos" :
                txt_referenciaArt.setText(String.valueOf(tabla.getValueAt(posicion, 0)));
                txt_descripcionArt.setText(String.valueOf(tabla.getValueAt(posicion, 1)));
                txt_precioArt.setText(String.valueOf(tabla.getValueAt(posicion, 2)));
                String iva_if = String.valueOf(tabla.getValueAt(posicion, 3));
                if(iva_if.equals("4")){
                    jComboBox_ivaArt.setSelectedIndex(0);
                }else{
                    if(iva_if.equals("10")){
                        jComboBox_ivaArt.setSelectedIndex(1);
                    }else{
                        jComboBox_ivaArt.setSelectedIndex(2);
                    }
                }
                txt_stockArt.setText(String.valueOf(tabla.getValueAt(posicion, 4)));
                
            break;

            case "Clientes" :
                txt_dniCli.setText(String.valueOf(tabla.getValueAt(posicion, 0)));
                txt_nombreCli.setText(String.valueOf(tabla.getValueAt(posicion, 1)));
                
            break;

            case "FacturasLin" :
                txt_lineaFLin.setText(String.valueOf(tabla.getValueAt(posicion, 1)));
                txt_cantidadFLin.setText(String.valueOf(tabla.getValueAt(posicion, 3)));
                txt_descuentoFLin.setText(String.valueOf(tabla.getValueAt(posicion, 5)));
              
            break;

            case "FacturasCab" :           
                txt_nfacturaFCab.setText(String.valueOf(tabla.getValueAt(posicion, 0)));
                txt_fechaFCab.setText(String.valueOf(tabla.getValueAt(posicion, 1)));
                
            break;

        }
    }

    //Rellena el jComboBox con la referencias de los articulos a las lineas de las facturas
    private void rellenarJComboReferenciaFLin(){
        for(Articulos var :listaArticulos){
            jComboBox_referenciaFLin.addItem(var.getReferencia());
        }
    }
    
    //Rellena el jComboBox con los dni/cif de los clientes a las cabeceras de las facturas
    private void rellenarJComboDniFCab(){
        for(Clientes var : listaClientes){
            jComboBox_dniFCab.addItem(var.getDnicif());
        }
    }
    
    private void rellenarJComboNFacturaFLin(){
        for(FacturasCab var : listaFacturasCab){
            jComboBox_nfacturaFLin.addItem(String.valueOf(var.getNumfac()));
        }
    }

    //Metodo que devuelve un String[] que contiene la cabecera de la jTable segun un jComboBox
    private String[] getCabecera(){
        String tabla_sel = JCombo_Tablas.getSelectedItem().toString();
        switch(tabla_sel){
            case "Articulos" :
                return atributos_articulos;

            case "Clientes" :
                return atributos_clientes;

            case "FacturasLin" :
                return atributos_FacturasLin;

            case "FacturasCab" :
                return atributos_FacturasCab;

            case "FacturasTot" :
                return atributos_FacturasTot;

            case "EstadisticasClientes" :
                return atributos_estClientes;

            default:
                return null;
        }
    }
    
    //Metodo que inicia un jDialog
    private void iniciar_jDialog(JDialog ventana){
        ventana.pack();
        ventana.setModal(true);
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    //rellena la jTable con la tabla Estadisticas Clientes
    private void crearTablaEst_Clientes(){
        String []cabecera = getCabecera();
        int size = cabecera.length;
        Object [][] datos=null;
        if (listaEst_Clientes.isEmpty()){
            datos = new Object [][] {};
        }else{
            datos = new Object[listaEst_Clientes.size()][size];
            for(int i=0;i<listaEst_Clientes.size();i++){
                datos[i][0]=listaEst_Clientes.get(i).getId().getAnio();
                datos[i][1]=listaEst_Clientes.get(i).getId().getMesNum();
                datos[i][2]=listaEst_Clientes.get(i).getMesNom();
                datos[i][3]=listaEst_Clientes.get(i).getClientes().getDnicif();
                datos[i][4]=listaEst_Clientes.get(i).getClientes().getNombrecli();
                datos[i][5]=listaEst_Clientes.get(i).getSumaDtos();
                datos[i][6]=listaEst_Clientes.get(i).getSumaIva();
                datos[i][7]=listaEst_Clientes.get(i).getSumaTotales();
            }
        }
        tabla.setModel(new DefaultTableModel(datos,cabecera));
    }

    //rellena la jTable con la tabla FacturasTot
    private void crearTablaFacturasTot(){
        String []cabecera = getCabecera();
        int size = cabecera.length;
        Object [][] datos=null;
        if (listaFacturasTot.isEmpty()){
            datos = new Object [][] {};
        }else{
            datos = new Object[listaFacturasTot.size()][size];
            for(int i=0;i<listaFacturasTot.size();i++){
                datos[i][0]=listaFacturasTot.get(i).getNumfac();
                datos[i][1]=listaFacturasTot.get(i).getBaseimp();
                datos[i][2]=listaFacturasTot.get(i).getImpDto();
                datos[i][3]=listaFacturasTot.get(i).getImpIva();
                datos[i][4]=listaFacturasTot.get(i).getTotalfac();
            }
        }
        tabla.setModel(new DefaultTableModel(datos,cabecera));
    }
    
    //rellena la jTable con la tabla FacturasTot
    private void crearTablaFacturasTotConsulta(){
        String []cabecera = getCabecera();
        int size = cabecera.length;
        Object [][] datos=null;
        if (listaFTotConsulta.isEmpty()){
            datos = new Object [][] {};
        }else{
            datos = new Object[listaFTotConsulta.size()][size];
            for(int i=0;i<listaFTotConsulta.size();i++){
                datos[i][0]=listaFTotConsulta.get(i).getNumfac();
                datos[i][1]=listaFTotConsulta.get(i).getBaseimp();
                datos[i][2]=listaFTotConsulta.get(i).getImpDto();
                datos[i][3]=listaFTotConsulta.get(i).getImpIva();
                datos[i][4]=listaFTotConsulta.get(i).getTotalfac();
            }
        }
        jTable_ConsultaFTot.setModel(new DefaultTableModel(datos,cabecera));
    }

    //rellena la jTable con la tabla Estadisticas Clientes
    private void crearTablaFacturasCab(){
        String []cabecera = getCabecera();
        int size = cabecera.length;
        Object [][] datos=null;
        if (listaFacturasCab.isEmpty()){
            datos = new Object [][] {};
        }else{
            datos = new Object[listaFacturasCab.size()][size];
            for(int i=0;i<listaFacturasCab.size();i++){
                datos[i][0]=listaFacturasCab.get(i).getNumfac();
                datos[i][1]=listaFacturasCab.get(i).getFechafac();
                datos[i][2]=listaFacturasCab.get(i).getClientes().getDnicif();
            }
        }
        tabla.setModel(new DefaultTableModel(datos,cabecera));
    }

    //rellena la jTable con la tabla Estadisticas Clientes
    private void crearTablaFacturasCabConsulta(){
        String []cabecera = getCabecera();
        int size = cabecera.length;
        Object [][] datos=null;
        if (listaFCabConsulta.isEmpty()){
            datos = new Object [][] {};
        }else{
            datos = new Object[listaFCabConsulta.size()][size];
            for(int i=0;i<listaFCabConsulta.size();i++){
                datos[i][0]=listaFCabConsulta.get(i).getNumfac();
                datos[i][1]=listaFCabConsulta.get(i).getFechafac();
                datos[i][2]=listaFCabConsulta.get(i).getClientes().getDnicif();
            }
        }
        jTable_ConsultaFCab.setModel(new DefaultTableModel(datos,cabecera));
    }

    //rellena la jTable con la tabla Clientes
    private void crearTablaClientes(){
        String []cabecera = getCabecera();
        int size = cabecera.length;
        Object [][] datos=null;
        if (listaClientes.isEmpty()){
            datos = new Object [][] {};
        }else{
            datos = new Object[listaClientes.size()][size];
            for(int i=0;i<listaClientes.size();i++){
                datos[i][0]=listaClientes.get(i).getDnicif();
                datos[i][1]=listaClientes.get(i).getNombrecli();
            }
        }
        tabla.setModel(new DefaultTableModel(datos,cabecera));
    }
    
    //rellena la jTable con la tabla Clientes
    private void crearTablaClientesConsulta(){
        if(!listaCliConsulta.isEmpty()){
            String []cabecera = getCabecera();
            int size = cabecera.length;
            Object [][] datos=null;
            if (listaCliConsulta.isEmpty()){
                datos = new Object [][] {};
            }else{
                datos = new Object[listaCliConsulta.size()][size];
                for(int i=0;i<listaCliConsulta.size();i++){
                    datos[i][0]=listaCliConsulta.get(i).getDnicif();
                    datos[i][1]=listaCliConsulta.get(i).getNombrecli();
                }
            }
            jTable_ConsultaCli.setModel(new DefaultTableModel(datos,cabecera));
        }else{
            Util.mensaje_aviso("Operacion realizada", "No se han encontrado resultados.");
        }
    }

    //rellena la jTable con la tabla FacturasLin
    private void crearTablaFacturasLin(){
        String []cabecera = getCabecera();
        int size = cabecera.length;
        Object [][] datos=null;
        if (listaFacturasLin.isEmpty()){
            datos = new Object [][] {};
        }else{
            datos = new Object[listaFacturasLin.size()][size];
            for(int i=0;i<listaFacturasLin.size();i++){
                datos[i][0]=listaFacturasLin.get(i).getId().getNumfac();
                datos[i][1]=listaFacturasLin.get(i).getId().getLineafac();
                datos[i][2]=listaFacturasLin.get(i).getArticulos().getReferencia();
                datos[i][3]=listaFacturasLin.get(i).getCantidad();
                datos[i][4]=listaFacturasLin.get(i).getPrecio();
                datos[i][5]=listaFacturasLin.get(i).getDtolinea();
                datos[i][6]=listaFacturasLin.get(i).getIvalinea();
            }
        }
        tabla.setModel(new DefaultTableModel(datos,cabecera));
    }
    
    //rellena la jTable con la tabla FacturasLin
    private void crearTablaFacturasLinConsulta(){
        String []cabecera = getCabecera();
        int size = cabecera.length;
        Object [][] datos=null;
        if (listaFLinConsulta.isEmpty()){
            datos = new Object [][] {};
        }else{
            datos = new Object[listaFLinConsulta.size()][size];
            for(int i=0;i<listaFLinConsulta.size();i++){
                datos[i][0]=listaFLinConsulta.get(i).getId().getNumfac();
                datos[i][1]=listaFLinConsulta.get(i).getId().getLineafac();
                datos[i][2]=listaFLinConsulta.get(i).getArticulos().getReferencia();
                datos[i][3]=listaFLinConsulta.get(i).getCantidad();
                datos[i][4]=listaFLinConsulta.get(i).getPrecio();
                datos[i][5]=listaFLinConsulta.get(i).getDtolinea();
                datos[i][6]=listaFLinConsulta.get(i).getIvalinea();
            }
        }
        jTable_ConsultaFLin.setModel(new DefaultTableModel(datos,cabecera));
    }

    //rellena la jTable con la tabla Articulos
    private void crearTablaArticulos(){
        String []cabecera = getCabecera();
        int size = cabecera.length;
        Object [][] datos=null;
        if (listaArticulos.isEmpty()){
            datos = new Object [][] {};
        }else{
            datos = new Object[listaArticulos.size()][size];
            for(int i=0;i<listaArticulos.size();i++){
                datos[i][0]=listaArticulos.get(i).getReferencia();
                datos[i][1]=listaArticulos.get(i).getDescripcion();
                datos[i][2]=listaArticulos.get(i).getPrecio();
                datos[i][3]=listaArticulos.get(i).getPorciva();
                datos[i][4]=listaArticulos.get(i).getStock();


            }
        }
        tabla.setModel(new DefaultTableModel(datos,cabecera));
    }
    
    //rellena la jTable con la tabla Articulos
    private void crearTablaArticulosConsulta(){
        String []cabecera = getCabecera();
        int size = cabecera.length;
        Object [][] datos=null;
        if (listaArtConsulta.isEmpty()){
            datos = new Object [][] {};
            Util.mensaje_info("Operacion realizada.", "No se ha encontrado articulos segun sus filtros.");
        }else{
            datos = new Object[listaArtConsulta.size()][size];
            for(int i=0;i<listaArtConsulta.size();i++){
                datos[i][0]=listaArtConsulta.get(i).getReferencia();
                datos[i][1]=listaArtConsulta.get(i).getDescripcion();
                datos[i][2]=listaArtConsulta.get(i).getPrecio();
                datos[i][3]=listaArtConsulta.get(i).getPorciva();
                datos[i][4]=listaArtConsulta.get(i).getStock();


            }
        }
        jTable_ConsultaArt.setModel(new DefaultTableModel(datos,cabecera));
    }

    //Metodo que inicia la interfaz y ejecuta los metodos pertinentes para su correcto funcionamiento
    public Interfaz() {
        initComponents();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setTitle("Practica Tema 03_03");
        listaArticulos= metodos.getListaArticulos();
        listaClientes = metodos.getListaClientes();
        listaFacturasCab = metodos.getListaFacturasCab();
        listaFacturasLin = metodos.getListaFacturasLin();
        listaFacturasTot = metodos.getListaFacturasTot();
        listaEst_Clientes = metodos.getListaEstadisticasClientes();
        getTablaSeleccionadaJCombo();
        rellenarJComboReferenciaFLin();
        rellenarJComboNFacturaFLin();

    }

    //Filtra el formate jTextFields segun los estandares de Clientes
    private boolean filtroCliente(){
        int cont = 0;
        String dni = txt_dniCli.getText();
        String nombre = txt_nombreCli.getText();
        if(!dni.equals("")){
            cont++;
        }else{
            Util.mensaje_aviso("DNI/CIF error.", "Rellene el campo DNI/CIF.");
        }
        if(!nombre.equals("")){
            cont++;
        }else{
            Util.mensaje_aviso("Nombre error.", "Rellene el campo de nombre.");
        }
        if(cont==2){
            return true;
        }else{
            return false;
        }
    }

    //Crea un objeto cliente y lo almacena en un objeto cliente global
    private void crearCliente(){
        String dni = txt_dniCli.getText();
        String nombre = txt_nombreCli.getText();
        cli_dat = new Clientes(dni, nombre);
    }

    //Filtra el formate jTextFields segun los estandares de Articulos
    private boolean filtroArticulo(){
        String referencia = txt_referenciaArt.getText();
        String descripcion = txt_descripcionArt.getText();
        BigDecimal precio=null;
        int stock_aux=0;
        int precio_aux = 0;
        try{
            precio = new BigDecimal(txt_precioArt.getText());
            precio_aux = Integer.valueOf(precio.toString());
        }catch(Exception e){
            Util.mensaje_aviso("Error formato.", "Introduzca un número en 'precio'.");
        }
        try{
            BigDecimal stock = new BigDecimal(txt_stockArt.getText());
            stock_aux = Integer.valueOf(stock.toString());
        }catch(Exception e){
            Util.mensaje_aviso("Error formato.", "Introduzca un número en 'stock'.");
        }
        int cont =0;
        if(!referencia.equals("")){
            cont++;
        }else{
            Util.mensaje_aviso("Error formato.", "Introduzca una referencia.");
        }
        
        if(!descripcion.equals("")){
            cont++;
        }else{
            Util.mensaje_aviso("Error formato.", "Introduzca una descripción.");
        }
        
        if(precio_aux<=0){
            Util.mensaje_aviso("Error formato.", "Introduzca un precio mayor a 0.");
        }else{
            cont ++;
        }
        
        if(stock_aux<0){
            Util.mensaje_aviso("Error formato.", "Introduzca un stock mayor o igual 0.");
        }else{
            cont ++;
        }
        if(cont==4){
            return true;
        }else{
            return false;
        }
    }
    
    //Crea un objeto articulo y lo almacena en un objeto articulo global
    private void crearArticulo(){
        String referencia = txt_referenciaArt.getText();
        String descripcion = txt_descripcionArt.getText();
        BigDecimal precio = new BigDecimal(txt_precioArt.getText());
        BigDecimal iva = new BigDecimal(jComboBox_ivaArt.getSelectedItem().toString());
        BigDecimal stock = new BigDecimal(txt_stockArt.getText());
        art_dat = new Articulos(referencia, descripcion, precio, iva, stock);
    }
    
    //Filtra el formate jTextFields segun los estandares de FacturaCab
    private boolean filtroFacturaCab(){
        int cont =0;
        long nfactura=0;
        
        try{
            nfactura = Long.valueOf(txt_nfacturaFCab.getText());
        }catch(NumberFormatException e){
            Util.mensaje_aviso("Error formato.", "Introduzca un número en 'Nº factura'.");
        }
        if(nfactura<=0){
            Util.mensaje_aviso("Error formato.", "Introduzca un número mayor que 0 en Nº factura.");
        }else{
            cont++;
        }
        
        String stringdate = String.valueOf(txt_fechaFCab.getText());
        try{
            int ano = Integer.valueOf(stringdate.substring(0,4));
            int mes = Integer.valueOf(stringdate.substring(5,7));
            int dia = Integer.valueOf(stringdate.substring(8,10));
            Date fecha= new Date(ano-1900, mes-1, dia);
            if(ano>1900 && ano<2200){
                if(mes>0 && mes<13){
                    if(dia>0 && dia<31){
                        cont++;
                    }else{
                        Util.mensaje_aviso("Error formato.", "Introduzca una día razonable.");
                    }
                }else{
                    Util.mensaje_aviso("Error formato.", "Introduzca un mes razonable.");
                }
            }else{
                Util.mensaje_aviso("Error formato.", "Introduzca un año razonable.");
            }
            
        }catch(Exception e ){
            Util.mensaje_aviso("Error formato.", "Introduzca una fecha con este formato 'año-mes-dia' ejemplo: '2000-11-21'.");
        }
                
        
        if(cont==2){
            return true;
        }else{
            return false;
        }
        
        
    }
    
    //Crea un objeto FacturaCab y lo almacena en un objeto FacturaCab global
    private void crearFacturaCab(){
        long nfactura = Long.valueOf(txt_nfacturaFCab.getText());
        for(Clientes var : listaClientes){
            if(var.getDnicif().equals(jComboBox_dniFCab.getSelectedItem().toString())){
                cli_dat= var;
            }
        }
        String stringdate = txt_fechaFCab.getText();
        int ano = Integer.valueOf(stringdate.substring(0,4));
        int mes = Integer.valueOf(stringdate.substring(5,7));
        int dia = Integer.valueOf(stringdate.substring(8,10));
        Date fecha= new Date(ano-1900, mes-1, dia);
        
        fCab_dat= new FacturasCab(nfactura, cli_dat, fecha);
        
    }

    //Filtra el formate jTextFields segun los estandares de FacturaLin
    private boolean filtroFacturaLin(){
        int cont=0;
        long nlinea=0;
        boolean nlinea_existe = false;
        
        try{
            nlinea = Long.valueOf(txt_lineaFLin.getText());
        }catch(NumberFormatException e){
            Util.mensaje_aviso("Error formato.", "Introduzca un número en 'Nº linea'.");
        }
        if(nlinea<=0){
            Util.mensaje_aviso("Error formato.", "Introduzca un número mayor que 0 en Nº linea.");
        }else{
            cont++;
        }
        long nfactura = Long.valueOf(jComboBox_nfacturaFLin.getSelectedItem().toString());
        
        
        int stock =0;
        for(Articulos var : listaArticulos){
            if(jComboBox_referenciaFLin.getSelectedItem().toString().equals(var.getReferencia())){
                stock = Integer.valueOf(var.getStock().toString());
            }
        }
        BigDecimal cantidadBD = new BigDecimal(0);

        try{
            cantidadBD= new BigDecimal(txt_cantidadFLin.getText());
        }catch(NumberFormatException e){
            Util.mensaje_aviso("Error formato.", "Introduzca un número en cantidad.");
        }
        int cantidad=Integer.valueOf(cantidadBD.toString());
        if(cantidad<stock){
            cont++;
        }else{
            Util.mensaje_aviso("Error formato.", "Introduzca una cantidad menor al stock.");
        }
        if(cantidad>0){
            cont++;
        }else{
            Util.mensaje_aviso("Error formato.", "Introduzca una cantidad mayor a 0.");
        }
        BigDecimal dtoBD = new BigDecimal(0);
        try{
            dtoBD= new BigDecimal(txt_descuentoFLin.getText());
        }catch(NumberFormatException e){
            Util.mensaje_aviso("Error formato.", "Introduzca un número en descuento.");
        }
        int dto = Integer.valueOf(dtoBD.toString());
        if(dto<0 || dto>100){
            Util.mensaje_aviso("Error formato.", "Introduzca un descuento entre 0 y 100.");
        }else{
            cont++;
        }

        if(cont ==4){
            return true;
        }else{
            return false;
        }
    }
    
    // crea facturaLin 
    private void crearFacturaLin(){
        long nfactura = Long.valueOf(jComboBox_nfacturaFLin.getSelectedItem().toString());
        long nlinea = Long.valueOf(txt_lineaFLin.getText());
        FacturasLinId idLin = new FacturasLinId(nfactura, nlinea);
        for(Articulos var : listaArticulos){
            if(var.getReferencia().equals(jComboBox_referenciaFLin.getSelectedItem().toString())){
                art_dat=var;
            }
        }
        for(FacturasCab var : listaFacturasCab){
            if(var.getNumfac()==nfactura){
                fCab_dat=var;
            }
        }
        BigDecimal cant = new BigDecimal(txt_cantidadFLin.getText());
        BigDecimal dto = new BigDecimal(txt_descuentoFLin.getText());        
        
        fLin_dat= new FacturasLin(idLin, art_dat, fCab_dat, cant, art_dat.getPrecio(), dto, art_dat.getPorciva());
        
    }
    
    //Suma bigDecimals y los setea en fTot_dat
    private void sumarBigDecimal(BigDecimal b,BigDecimal d,BigDecimal i,BigDecimal t){
        double base = Double.valueOf(fTot_dat.getBaseimp().toString());
        double dto = Double.valueOf(fTot_dat.getImpDto().toString());
        double iva = Double.valueOf(fTot_dat.getImpIva().toString());
        double total = Double.valueOf(fTot_dat.getTotalfac().toString());
        double base1 = Double.valueOf(b.toString());
        double dto1 = Double.valueOf(d.toString());
        double iva1 = Double.valueOf(i.toString());
        double total1 = Double.valueOf(t.toString());
        base = base+base1;
        iva=iva+iva1;
        dto=dto+dto1;
        total=total+total1;
        
        BigDecimal aux = new BigDecimal(base);
        fTot_dat.setBaseimp(aux);
        
        aux = new BigDecimal(dto);
        fTot_dat.setImpDto(aux);
        
        aux = new BigDecimal(iva);
        fTot_dat.setImpIva(aux);
        
        aux = new BigDecimal(total);
        fTot_dat.setTotalfac(aux);   
    }
    
    //Crea un xml y lo exporta
    private void crearXML(long nfact,String ruta, String nombre){
        try {
            // Creo una instancia de DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Creo un documentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Creo un DOMImplementation
            DOMImplementation implementation = builder.getDOMImplementation();
 
            // Creo un documento con un elemento raiz
            Document documento = implementation.createDocument(null, "Archivo_XML", null);
            documento.setXmlVersion("1.0");

            Element factura = documento.createElement("Factura");

            //Creamos cabecera y la añadimos a la factura
            for(FacturasCab var: listaFacturasCab){
                if(nfact==var.getNumfac()){
                    Element facturaCab = documento.createElement("FacturaCab");

                    Element numfac = documento.createElement("numfac");
                    Element fecha = documento.createElement("fechafac");
                    Element dnicif = documento.createElement("dnicif");
            
                    Text txt_numfac = documento.createTextNode(String.valueOf(nfact));
                    Text txt_fecha = documento.createTextNode(var.getFechafac().toString());
                    Text txt_dni = documento.createTextNode(var.getClientes().getDnicif());
                    
                    numfac.appendChild(txt_numfac);
                    dnicif.appendChild(txt_dni);
                    fecha.appendChild(txt_fecha);
                    
                    facturaCab.appendChild(numfac);
                    facturaCab.appendChild(fecha);
                    facturaCab.appendChild(dnicif);
                    factura.appendChild(facturaCab);
                }
            }
            
            // FacturaLin
            for(FacturasLin var : listaFacturasLin){
                if(nfact==var.getId().getNumfac()){
                                       
                    Element facturaLin = documento.createElement("FacturaLin");
                    
                    Element numfac = documento.createElement("numfac");
                    Element lineafac = documento.createElement("lineafac");
                    Element referencia = documento.createElement("referencia");
                    Element cantidad = documento.createElement("cantidad");
                    Element precio = documento.createElement("precio");
                    Element dtolinea = documento.createElement("dtolinea");
                    Element ivalinea = documento.createElement("ivalinea");
                    
                    Text txt_numfac = documento.createTextNode(String.valueOf(nfact));
                    Text txt_nlinea = documento.createTextNode(String.valueOf(var.getId().getLineafac()));
                    Text txt_referencia = documento.createTextNode(var.getArticulos().getReferencia());
                    Text txt_cant = documento.createTextNode(var.getCantidad().toString());
                    Text txt_precio= documento.createTextNode(var.getPrecio().toString());
                    Text txt_dto= documento.createTextNode(var.getDtolinea().toString());
                    Text txt_iva= documento.createTextNode(var.getIvalinea().toString());
                    
                    numfac.appendChild(txt_numfac);
                    lineafac.appendChild(txt_nlinea);
                    referencia.appendChild(txt_referencia);
                    cantidad.appendChild(txt_cant);
                    precio.appendChild(txt_precio);
                    dtolinea.appendChild(txt_dto);
                    ivalinea.appendChild(txt_iva);
                    
                    facturaLin.appendChild(numfac);
                    facturaLin.appendChild(lineafac);
                    facturaLin.appendChild(referencia);
                    facturaLin.appendChild(cantidad);
                    facturaLin.appendChild(precio);
                    facturaLin.appendChild(dtolinea);
                    facturaLin.appendChild(ivalinea);
                    
                    factura.appendChild(facturaLin);
                }
            }
 
            // FacturaTot
            for(FacturasTot var : listaFacturasTot){
                if(nfact == var.getNumfac()){
                    Element facturaTot = documento.createElement("FacturaTot");
                    
                    Element numfac = documento.createElement("numfac");
                    Element baseimp = documento.createElement("baseimp");
                    Element impDto = documento.createElement("impDto");
                    Element impIva = documento.createElement("impIva");
                    Element totalfac = documento.createElement("totalfac");
                    
                    Text txt_numfac = documento.createTextNode(String.valueOf(nfact));
                    Text txt_baseimp = documento.createTextNode(var.getBaseimp().toString());
                    Text txt_impDto = documento.createTextNode(var.getImpDto().toString());
                    Text txt_impIva = documento.createTextNode(var.getImpIva().toString());
                    Text txt_totalfac = documento.createTextNode(var.getTotalfac().toString());
                    
                    
                    numfac.appendChild(txt_numfac);
                    baseimp.appendChild(txt_baseimp);
                    impDto.appendChild(txt_impDto);
                    impIva.appendChild(txt_impIva);
                    totalfac.appendChild(txt_totalfac);
                    
                    facturaTot.appendChild(numfac);
                    facturaTot.appendChild(baseimp);
                    facturaTot.appendChild(impDto);
                    facturaTot.appendChild(impIva);
                    facturaTot.appendChild(totalfac);
                    
                    factura.appendChild(facturaTot);
                }
            }
            
            documento.getDocumentElement().appendChild(factura);
            
            // Asocio el source con el Document
            Source source = new DOMSource(documento);
            // Creo el Result, indicado que fichero se va a crear
            Result result = new StreamResult(new File(ruta+nombre+".xml"));
 
            // Creo un transformer, se crea el fichero XML
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);
 
        } catch (ParserConfigurationException | TransformerException ex) {
            Util.mensaje_error("Error.", "Error:"+ex);
        }
    }

    //Lee un archivo xml y lo importa
    private void leerXML(File archivo){
        try {
            ArrayList<FacturasLin> lista_FLinImport=new ArrayList<>();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(archivo);
            document.getDocumentElement().normalize();
                        
            NodeList nListFcab = document.getElementsByTagName("FacturaCab");
            
            long numfac = 0;
            Date fecha = null; 
            boolean existeDni=false;
            boolean existeFCab=false;
            
            for (int temp = 0; temp < nListFcab.getLength(); temp++) {
                Node nNode = nListFcab.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    numfac = Long.valueOf(eElement.getElementsByTagName("numfac").item(0).getTextContent());
                    String stringdate =  eElement.getElementsByTagName("fechafac").item(0).getTextContent();
                    String dni = eElement.getElementsByTagName("dnicif").item(0).getTextContent();
                    int ano = Integer.valueOf(stringdate.substring(0,4));
                    int mes = Integer.valueOf(stringdate.substring(5,7));
                    int dia = Integer.valueOf(stringdate.substring(8,10));
                    fecha= new Date(ano-1900, mes-1, dia);
                    
                    for(Clientes var : listaClientes){
                        if(dni.equals(var.getDnicif())){
                            cli_dat=var;
                            existeDni=true;
                        }
                    }
                    fCab_dat = new FacturasCab(numfac, cli_dat, fecha);
                }
            }  
            
            for(FacturasCab var : listaFacturasCab){
                if(var.getNumfac()==numfac){
                    existeFCab=true;
                }
            }
            
            if(!existeFCab && existeDni){
                //metodos.anadirObjeto(fCab_dat);
                NodeList nListFLin = document.getElementsByTagName("FacturaLin");
                int cont =0;
                for (int temp = 0; temp < nListFLin.getLength(); temp++) {
                    Node nNode = nListFLin.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        long lineafac = Long.valueOf(eElement.getElementsByTagName("lineafac").item(0).getTextContent());
                        FacturasLinId id = new FacturasLinId(numfac, lineafac);
                        for(Articulos var : listaArticulos){
                            if(eElement.getElementsByTagName("referencia").item(0).getTextContent().equals(var.getReferencia())){
                                art_dat=var;   
                                cont++;
                            }
                        }
                        BigDecimal cantidad = new BigDecimal(eElement.getElementsByTagName("cantidad").item(0).getTextContent());
                        BigDecimal precio = new BigDecimal(eElement.getElementsByTagName("precio").item(0).getTextContent());
                        BigDecimal dtolinea = new BigDecimal(eElement.getElementsByTagName("dtolinea").item(0).getTextContent());
                        BigDecimal ivalinea = new BigDecimal(eElement.getElementsByTagName("ivalinea").item(0).getTextContent());
                        
                        fLin_dat = new FacturasLin(id, art_dat, fCab_dat, cantidad, precio, dtolinea, ivalinea);
                        lista_FLinImport.add(fLin_dat);
                        
                    }
                }
                
                if(cont==nListFLin.getLength()){
                    NodeList nListFTot = document.getElementsByTagName("FacturaTot");
                    for (int temp = 0; temp < nListFTot.getLength(); temp++) {
                        Node nNode = nListFTot.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            BigDecimal baseimp = new BigDecimal(eElement.getElementsByTagName("baseimp").item(0).getTextContent());
                            BigDecimal impDto = new BigDecimal(eElement.getElementsByTagName("impDto").item(0).getTextContent());
                            BigDecimal impIva = new BigDecimal(eElement.getElementsByTagName("impIva").item(0).getTextContent());
                            BigDecimal totalfac = new BigDecimal(eElement.getElementsByTagName("totalfac").item(0).getTextContent());
                            fTot_dat = new FacturasTot(fCab_dat, baseimp, impDto, impIva, totalfac);
                            
                            metodos.anadirObjeto(fCab_dat);
                            for(FacturasLin var : lista_FLinImport){
                                metodos.anadirObjeto(var);
                            }
                            metodos.anadirObjeto(fTot_dat);
                            listaFacturasCab.clear();
                            listaFacturasLin.clear();
                            listaFacturasTot.clear();
                            listaFacturasCab = metodos.getListaFacturasCab();
                            listaFacturasLin = metodos.getListaFacturasLin();
                            listaFacturasTot = metodos.getListaFacturasTot();
                            getTablaSeleccionadaJCombo();
                            Util.operacion_realizada();
                            
                        }
                    }
                }else{
                    Util.mensaje_error("Error.", "Intentando importar articulos no existentes en la base de datos.");
                }
                
            }
            
            
        }catch(Exception e) {
            Util.mensaje_error("Error.", "Error: "+e);
        } 
    }
    
    //Lee un xml y lo importa al dni designado
    private void importarOferta(File archivo, String dni){
        try {
            ArrayList<FacturasLin> lista_FLinImport=new ArrayList<>();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(archivo);
            document.getDocumentElement().normalize();
                        
            boolean ejecutar = false;
            for(Clientes var : listaClientes){
                if(var.getDnicif().equals(dni)){
                    ejecutar = true;
                    cli_dat=var;
                }
            }
            if(ejecutar){
                NodeList nListFcab = document.getElementsByTagName("FacturaCab");
                long numfac = 0;
                Date fecha = null; 
                
                
                for (int temp = 0; temp < nListFcab.getLength(); temp++) {
                    Node nNode = nListFcab.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        numfac = Long.valueOf(eElement.getElementsByTagName("numfac").item(0).getTextContent());


                        String stringdate =  eElement.getElementsByTagName("fechafac").item(0).getTextContent();
                        int ano = Integer.valueOf(stringdate.substring(0,4));
                        int mes = Integer.valueOf(stringdate.substring(5,7));
                        int dia = Integer.valueOf(stringdate.substring(8,10));
                        fecha= new Date(ano-1900, mes-1, dia);
                        for(FacturasCab var2 : listaFacturasCab){
                            System.out.println("numfac en lista:"+var2.getNumfac());
                            do{
                                numfac++;
                                System.out.println("var numfac: "+numfac);
                            }while(var2.getNumfac()==numfac);
                        }
                        fCab_dat = new FacturasCab(numfac, cli_dat, fecha);
                        System.out.println("Se añade cabecera");
                        metodos.anadirObjeto(fCab_dat);
                    }
                }
                
                NodeList nListFLin = document.getElementsByTagName("FacturaLin");
                int cont =0;
                long lineafac = 1;
                for (int temp = 0; temp < nListFLin.getLength(); temp++) {
                    Node nNode = nListFLin.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        FacturasLinId id = new FacturasLinId(numfac, lineafac);
                        for(Articulos var : listaArticulos){
                            if(eElement.getElementsByTagName("referencia").item(0).getTextContent().equals(var.getReferencia())){
                                art_dat=var;   
                                cont++;
                            }
                        }
                        BigDecimal cantidad = new BigDecimal(eElement.getElementsByTagName("cantidad").item(0).getTextContent());
                        BigDecimal precio = new BigDecimal(eElement.getElementsByTagName("precio").item(0).getTextContent());
                        BigDecimal dtolinea = new BigDecimal(eElement.getElementsByTagName("dtolinea").item(0).getTextContent());
                        BigDecimal ivalinea = new BigDecimal(eElement.getElementsByTagName("ivalinea").item(0).getTextContent());

                        fLin_dat = new FacturasLin(id, art_dat, fCab_dat, cantidad, precio, dtolinea, ivalinea);
                        System.out.println("Se añade FLin");
                        lista_FLinImport.add(fLin_dat);
                        lineafac++;
                    }
                }

                if(cont==nListFLin.getLength()){
                    fTot_dat = new FacturasTot(fCab_dat);
                    NodeList nListFTot = document.getElementsByTagName("FacturaTot");
                    for (int temp = 0; temp < nListFTot.getLength(); temp++) {
                        Node nNode = nListFTot.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            BigDecimal baseimp = new BigDecimal(eElement.getElementsByTagName("baseimp").item(0).getTextContent());
                            BigDecimal impDto = new BigDecimal(eElement.getElementsByTagName("impDto").item(0).getTextContent());
                            BigDecimal impIva = new BigDecimal(eElement.getElementsByTagName("impIva").item(0).getTextContent());
                            BigDecimal totalfac = new BigDecimal(eElement.getElementsByTagName("totalfac").item(0).getTextContent());
                            try{
                                sumarBigDecimal(impIva, impDto, impIva, impDto);
                            }catch(Exception e){
                                fTot_dat.setBaseimp(baseimp);
                                fTot_dat.setImpDto(impDto);
                                fTot_dat.setImpIva(impIva);
                                fTot_dat.setTotalfac(totalfac);
                            }
                            fTot_dat.setFacturasCab(fCab_dat);

                            for(FacturasLin var : lista_FLinImport){
                                metodos.anadirObjeto(var);
                            }
                            metodos.anadirObjeto(fTot_dat);
                            listaFacturasCab.clear();
                            listaFacturasLin.clear();
                            listaFacturasTot.clear();
                            listaFacturasCab = metodos.getListaFacturasCab();
                            listaFacturasLin = metodos.getListaFacturasLin();
                            listaFacturasTot = metodos.getListaFacturasTot();
                            getTablaSeleccionadaJCombo();
                            Util.operacion_realizada();
                        }
                    }
                }else{
                    Util.mensaje_error("Error.", "Intentando importar articulos no existentes en la base de datos.");
                }

                
            }else{
                Util.mensaje_aviso("Error.", "Dni no encontrado en la bd.");
            }
        }catch(Exception e) {
            Util.mensaje_error("Error.", "Error: "+e);
        } 
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog_Cliente = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_dniCli = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_nombreCli = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jButton_altaCli = new javax.swing.JButton();
        jButton_cancelarCli = new javax.swing.JButton();
        jButton_modCli = new javax.swing.JButton();
        jButton_bajaCli = new javax.swing.JButton();
        jDialog_Articulo = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txt_referenciaArt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_precioArt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jComboBox_ivaArt = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        txt_stockArt = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txt_descripcionArt = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jButton_altaArt = new javax.swing.JButton();
        jButton_cancelarArt = new javax.swing.JButton();
        jButton_modArt = new javax.swing.JButton();
        jButton_bajaArt = new javax.swing.JButton();
        jDialog_FacturaCab = new javax.swing.JDialog();
        jPanel9 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txt_nfacturaFCab = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txt_fechaFCab = new javax.swing.JTextField();
        jComboBox_dniFCab = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        jButton_altaFCab = new javax.swing.JButton();
        jButton_cancelarFCab = new javax.swing.JButton();
        jButton_modFCab = new javax.swing.JButton();
        jButton_bajaFCab = new javax.swing.JButton();
        jDialog_FacturaLin = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txt_lineaFLin = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jComboBox_referenciaFLin = new javax.swing.JComboBox<>();
        txt_cantidadFLin = new javax.swing.JTextField();
        txt_descuentoFLin = new javax.swing.JTextField();
        jComboBox_nfacturaFLin = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        jButton_altaFLin = new javax.swing.JButton();
        jButton_cancelarFLin = new javax.swing.JButton();
        jButton_modificacionFLin = new javax.swing.JButton();
        jButton_bajaFLin = new javax.swing.JButton();
        jDialog_Consulta_Art = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox_opeRefArt = new javax.swing.JComboBox<>();
        txt_consultaRefArt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jComboBox_opePreArt = new javax.swing.JComboBox<>();
        txt_consultaPreArt = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jComboBox_opeStoArt = new javax.swing.JComboBox<>();
        txt_consultaStoArt = new javax.swing.JTextField();
        txt_consutaArt = new javax.swing.JTextField();
        jButton_AplicarRefArt = new javax.swing.JButton();
        jButton_AplicarPreArt = new javax.swing.JButton();
        jButton_AplicarStoArt = new javax.swing.JButton();
        jButton_EjecutarArt = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_ConsultaArt = new javax.swing.JTable();
        jDialog_Consulta_Cli = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jComboBox_opeDniCli = new javax.swing.JComboBox<>();
        txt_consultaDniCli = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jComboBox_opeNomCli = new javax.swing.JComboBox<>();
        txt_consultaNomCli = new javax.swing.JTextField();
        txt_consutaCli = new javax.swing.JTextField();
        jButton_AplicarDniCLi = new javax.swing.JButton();
        jButton_AplicarNomCli = new javax.swing.JButton();
        jButton_EjecutarCli = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable_ConsultaCli = new javax.swing.JTable();
        jDialog_Consulta_FCab = new javax.swing.JDialog();
        jPanel13 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jComboBox_opeNFacFCab = new javax.swing.JComboBox<>();
        txt_consultaNFacFCab = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jComboBox_opeDniFCab = new javax.swing.JComboBox<>();
        txt_consultaDniFCab = new javax.swing.JTextField();
        txt_consutaFCab = new javax.swing.JTextField();
        jButton_AplicarNFacCab = new javax.swing.JButton();
        jButton_AplicarDniFCab = new javax.swing.JButton();
        jButton_EjecutarFCab = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable_ConsultaFCab = new javax.swing.JTable();
        jDialog_Consulta_FLin = new javax.swing.JDialog();
        jPanel14 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jComboBox_opeNFacFLin = new javax.swing.JComboBox<>();
        txt_consultaNFacFLin = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jComboBox_opeRefFLin = new javax.swing.JComboBox<>();
        txt_consultaRefFLin = new javax.swing.JTextField();
        txt_consutaFLin = new javax.swing.JTextField();
        jButton_AplicarNFacLin = new javax.swing.JButton();
        jButton_AplicarRefFLin = new javax.swing.JButton();
        jButton_EjecutarFLin = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable_ConsultaFLin = new javax.swing.JTable();
        jDialog_Consulta_FTot = new javax.swing.JDialog();
        jPanel15 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jComboBox_opeNFacFTot = new javax.swing.JComboBox<>();
        txt_consultaNFacFTot = new javax.swing.JTextField();
        txt_consutaFTot = new javax.swing.JTextField();
        jButton_AplicarNFacFTot = new javax.swing.JButton();
        jButton_EjecutarFTot = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable_ConsultaFTot = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        JCombo_Tablas = new javax.swing.JComboBox<>();
        jButton_AltaPrincipal = new javax.swing.JButton();
        jButton_BajaPrincipal = new javax.swing.JButton();
        jButton_ConsultaPrincipal = new javax.swing.JButton();
        jButton_Generar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton_Exportar = new javax.swing.JButton();
        jButton_Importar = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        txt_NfacturaExportar = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txt_NombreArchivo = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        txt_dniOferta = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();

        jDialog_Cliente.setTitle("Alta/Actualización Articulos");
        jDialog_Cliente.setResizable(false);

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setText("DNI/CIF:");

        jLabel3.setText("Nombre:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_nombreCli, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                    .addComponent(txt_dniCli))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_dniCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_nombreCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton_altaCli.setText("Alta");
        jButton_altaCli.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_altaCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_altaCliActionPerformed(evt);
            }
        });

        jButton_cancelarCli.setText("Cancelar");
        jButton_cancelarCli.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_cancelarCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_cancelarCliActionPerformed(evt);
            }
        });

        jButton_modCli.setText("Modificación");
        jButton_modCli.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_modCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_modCliActionPerformed(evt);
            }
        });

        jButton_bajaCli.setText("Baja");
        jButton_bajaCli.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_bajaCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_bajaCliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton_altaCli)
                .addGap(18, 18, 18)
                .addComponent(jButton_modCli)
                .addGap(18, 18, 18)
                .addComponent(jButton_bajaCli)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 213, Short.MAX_VALUE)
                .addComponent(jButton_cancelarCli)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_altaCli)
                    .addComponent(jButton_cancelarCli)
                    .addComponent(jButton_modCli)
                    .addComponent(jButton_bajaCli))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog_ClienteLayout = new javax.swing.GroupLayout(jDialog_Cliente.getContentPane());
        jDialog_Cliente.getContentPane().setLayout(jDialog_ClienteLayout);
        jDialog_ClienteLayout.setHorizontalGroup(
            jDialog_ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_ClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog_ClienteLayout.setVerticalGroup(
            jDialog_ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_ClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jDialog_Articulo.setTitle("Alta/Actualización Articulo");
        jDialog_Articulo.setResizable(false);

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setText("Referencia:");

        jLabel8.setText("Descripción:");

        jLabel9.setText("Precio:");

        jLabel10.setText("% IVA:");

        jComboBox_ivaArt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "4", "10", "21" }));

        jLabel11.setText("Stock:");

        txt_descripcionArt.setColumns(20);
        txt_descripcionArt.setRows(5);
        jScrollPane3.setViewportView(txt_descripcionArt);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_precioArt)
                    .addComponent(jComboBox_ivaArt, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_stockArt)
                    .addComponent(txt_referenciaArt)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_referenciaArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 113, Short.MAX_VALUE))
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(txt_precioArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jComboBox_ivaArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txt_stockArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButton_altaArt.setText("Alta");
        jButton_altaArt.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_altaArt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_altaArtActionPerformed(evt);
            }
        });

        jButton_cancelarArt.setText("Cancelar");
        jButton_cancelarArt.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_cancelarArt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_cancelarArtActionPerformed(evt);
            }
        });

        jButton_modArt.setText("Modificación");
        jButton_modArt.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_modArt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_modArtActionPerformed(evt);
            }
        });

        jButton_bajaArt.setText("Baja");
        jButton_bajaArt.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_bajaArt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_bajaArtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton_altaArt)
                .addGap(18, 18, 18)
                .addComponent(jButton_modArt)
                .addGap(18, 18, 18)
                .addComponent(jButton_bajaArt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton_cancelarArt)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_altaArt)
                    .addComponent(jButton_cancelarArt)
                    .addComponent(jButton_modArt)
                    .addComponent(jButton_bajaArt))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog_ArticuloLayout = new javax.swing.GroupLayout(jDialog_Articulo.getContentPane());
        jDialog_Articulo.getContentPane().setLayout(jDialog_ArticuloLayout);
        jDialog_ArticuloLayout.setHorizontalGroup(
            jDialog_ArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_ArticuloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_ArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog_ArticuloLayout.setVerticalGroup(
            jDialog_ArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_ArticuloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jDialog_FacturaCab.setTitle("Alta/Actualización Articulo");
        jDialog_FacturaCab.setResizable(false);

        jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel14.setText("Nº Factura:");

        jLabel15.setText("Fecha:");

        jLabel16.setText("DNI/CIF:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16))
                .addGap(20, 20, 20)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_fechaFCab, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                    .addComponent(txt_nfacturaFCab)
                    .addComponent(jComboBox_dniFCab, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txt_nfacturaFCab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txt_fechaFCab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jComboBox_dniFCab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jButton_altaFCab.setText("Alta");
        jButton_altaFCab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_altaFCab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_altaFCabActionPerformed(evt);
            }
        });

        jButton_cancelarFCab.setText("Cancelar");
        jButton_cancelarFCab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_cancelarFCab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_cancelarFCabActionPerformed(evt);
            }
        });

        jButton_modFCab.setText("Modificación");
        jButton_modFCab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_modFCab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_modFCabActionPerformed(evt);
            }
        });

        jButton_bajaFCab.setText("Baja");
        jButton_bajaFCab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_bajaFCab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_bajaFCabActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton_altaFCab)
                .addGap(18, 18, 18)
                .addComponent(jButton_modFCab)
                .addGap(18, 18, 18)
                .addComponent(jButton_bajaFCab)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton_cancelarFCab)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_altaFCab)
                    .addComponent(jButton_cancelarFCab)
                    .addComponent(jButton_modFCab)
                    .addComponent(jButton_bajaFCab))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog_FacturaCabLayout = new javax.swing.GroupLayout(jDialog_FacturaCab.getContentPane());
        jDialog_FacturaCab.getContentPane().setLayout(jDialog_FacturaCabLayout);
        jDialog_FacturaCabLayout.setHorizontalGroup(
            jDialog_FacturaCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_FacturaCabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_FacturaCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog_FacturaCabLayout.setVerticalGroup(
            jDialog_FacturaCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_FacturaCabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDialog_FacturaLin.setTitle("Alta/Actualización FacturasLin\n");
        jDialog_FacturaLin.setResizable(false);

        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel19.setText("Nº Factura:");

        jLabel20.setText("Linea:");

        jLabel21.setText("Referencia:");

        jLabel22.setText("Cantidad:");

        jLabel24.setText("Descuento:");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel19))
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(txt_lineaFLin, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBox_nfacturaFLin, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel22))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox_referenciaFLin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_cantidadFLin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_descuentoFLin, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jComboBox_nfacturaFLin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txt_lineaFLin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jComboBox_referenciaFLin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txt_cantidadFLin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(txt_descuentoFLin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton_altaFLin.setText("Alta");
        jButton_altaFLin.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_altaFLin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_altaFLinActionPerformed(evt);
            }
        });

        jButton_cancelarFLin.setText("Cancelar");
        jButton_cancelarFLin.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton_modificacionFLin.setText("Modificación");
        jButton_modificacionFLin.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_modificacionFLin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_modificacionFLinActionPerformed(evt);
            }
        });

        jButton_bajaFLin.setText("Baja");
        jButton_bajaFLin.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton_bajaFLin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_bajaFLinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton_altaFLin)
                .addGap(18, 18, 18)
                .addComponent(jButton_modificacionFLin)
                .addGap(18, 18, 18)
                .addComponent(jButton_bajaFLin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton_cancelarFLin)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_altaFLin)
                    .addComponent(jButton_cancelarFLin)
                    .addComponent(jButton_modificacionFLin)
                    .addComponent(jButton_bajaFLin))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog_FacturaLinLayout = new javax.swing.GroupLayout(jDialog_FacturaLin.getContentPane());
        jDialog_FacturaLin.getContentPane().setLayout(jDialog_FacturaLinLayout);
        jDialog_FacturaLinLayout.setHorizontalGroup(
            jDialog_FacturaLinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_FacturaLinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_FacturaLinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog_FacturaLinLayout.setVerticalGroup(
            jDialog_FacturaLinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_FacturaLinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jDialog_Consulta_Art.setTitle("Consulta Articulos.");

        jLabel4.setText("Consulta:");

        jLabel5.setText("Filtro por referencia:");

        jComboBox_opeRefArt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "LIKE" }));

        jLabel6.setText("Filtro por precio:");

        jComboBox_opePreArt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "<=", "<", ">=", ">" }));

        jLabel12.setText("Filtro por stock:");

        jComboBox_opeStoArt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "<=", "<", ">=", ">" }));

        jButton_AplicarRefArt.setText("Aplicar");
        jButton_AplicarRefArt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AplicarRefArtActionPerformed(evt);
            }
        });

        jButton_AplicarPreArt.setText("Aplicar");
        jButton_AplicarPreArt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AplicarPreArtActionPerformed(evt);
            }
        });

        jButton_AplicarStoArt.setText("Aplicar");
        jButton_AplicarStoArt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AplicarStoArtActionPerformed(evt);
            }
        });

        jButton_EjecutarArt.setText("Ejecutar");
        jButton_EjecutarArt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EjecutarArtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jComboBox_opePreArt, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox_opeRefArt, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox_opeStoArt, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_consultaStoArt)
                            .addComponent(txt_consultaRefArt, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_consultaPreArt)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txt_consutaArt)))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_EjecutarArt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton_AplicarRefArt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_AplicarPreArt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_AplicarStoArt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox_opeRefArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_consultaRefArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_AplicarRefArt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox_opePreArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_consultaPreArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_AplicarPreArt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jComboBox_opeStoArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_consultaStoArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_AplicarStoArt))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_consutaArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_EjecutarArt))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable_ConsultaArt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTable_ConsultaArt);

        javax.swing.GroupLayout jDialog_Consulta_ArtLayout = new javax.swing.GroupLayout(jDialog_Consulta_Art.getContentPane());
        jDialog_Consulta_Art.getContentPane().setLayout(jDialog_Consulta_ArtLayout);
        jDialog_Consulta_ArtLayout.setHorizontalGroup(
            jDialog_Consulta_ArtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_Consulta_ArtLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_Consulta_ArtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog_Consulta_ArtLayout.setVerticalGroup(
            jDialog_Consulta_ArtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_Consulta_ArtLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addContainerGap())
        );

        jDialog_Consulta_Cli.setTitle("Consulta Clientes.");

        jLabel13.setText("Consulta:");

        jLabel17.setText("Filtro por DNI/CIF:");

        jComboBox_opeDniCli.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "LIKE" }));

        jLabel18.setText("Filtro por nombre:");

        jComboBox_opeNomCli.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "LIKE" }));

        jButton_AplicarDniCLi.setText("Aplicar");
        jButton_AplicarDniCLi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AplicarDniCLiActionPerformed(evt);
            }
        });

        jButton_AplicarNomCli.setText("Aplicar");
        jButton_AplicarNomCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AplicarNomCliActionPerformed(evt);
            }
        });

        jButton_EjecutarCli.setText("Ejecutar");
        jButton_EjecutarCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EjecutarCliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jComboBox_opeNomCli, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox_opeDniCli, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_consultaDniCli, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                            .addComponent(txt_consultaNomCli))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton_AplicarDniCLi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_AplicarNomCli, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(txt_consutaCli)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_EjecutarCli)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jComboBox_opeDniCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_consultaDniCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_AplicarDniCLi))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jComboBox_opeNomCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_consultaNomCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_AplicarNomCli))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txt_consutaCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_EjecutarCli))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable_ConsultaCli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(jTable_ConsultaCli);

        javax.swing.GroupLayout jDialog_Consulta_CliLayout = new javax.swing.GroupLayout(jDialog_Consulta_Cli.getContentPane());
        jDialog_Consulta_Cli.getContentPane().setLayout(jDialog_Consulta_CliLayout);
        jDialog_Consulta_CliLayout.setHorizontalGroup(
            jDialog_Consulta_CliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_Consulta_CliLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_Consulta_CliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog_Consulta_CliLayout.setVerticalGroup(
            jDialog_Consulta_CliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_Consulta_CliLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDialog_Consulta_FCab.setTitle("Consulta FacturasCab.");

        jLabel25.setText("Consulta:");

        jLabel26.setText("Filtro por Nº Factura:");

        jComboBox_opeNFacFCab.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "<=", "<", ">=", ">" }));

        jLabel27.setText("Filtro por DNI/CIF:");

        jComboBox_opeDniFCab.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "LIKE" }));

        jButton_AplicarNFacCab.setText("Aplicar");
        jButton_AplicarNFacCab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AplicarNFacCabActionPerformed(evt);
            }
        });

        jButton_AplicarDniFCab.setText("Aplicar");
        jButton_AplicarDniFCab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AplicarDniFCabActionPerformed(evt);
            }
        });

        jButton_EjecutarFCab.setText("Ejecutar");
        jButton_EjecutarFCab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EjecutarFCabActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jComboBox_opeDniFCab, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox_opeNFacFCab, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_consultaNFacFCab, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                            .addComponent(txt_consultaDniFCab))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton_AplicarNFacCab, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_AplicarDniFCab, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(18, 18, 18)
                        .addComponent(txt_consutaFCab)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_EjecutarFCab)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jComboBox_opeNFacFCab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_consultaNFacFCab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_AplicarNFacCab))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jComboBox_opeDniFCab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_consultaDniFCab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_AplicarDniFCab))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txt_consutaFCab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_EjecutarFCab))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable_ConsultaFCab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(jTable_ConsultaFCab);

        javax.swing.GroupLayout jDialog_Consulta_FCabLayout = new javax.swing.GroupLayout(jDialog_Consulta_FCab.getContentPane());
        jDialog_Consulta_FCab.getContentPane().setLayout(jDialog_Consulta_FCabLayout);
        jDialog_Consulta_FCabLayout.setHorizontalGroup(
            jDialog_Consulta_FCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_Consulta_FCabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_Consulta_FCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog_Consulta_FCabLayout.setVerticalGroup(
            jDialog_Consulta_FCabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_Consulta_FCabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                .addContainerGap())
        );

        jDialog_Consulta_FLin.setTitle("Consulta FacturasLin.");

        jLabel28.setText("Consulta:");

        jLabel29.setText("Filtro por Nº Factura:");

        jComboBox_opeNFacFLin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "<=", "<", ">=", ">" }));

        jLabel30.setText("Filtro por referencia:");

        jComboBox_opeRefFLin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "LIKE" }));

        jButton_AplicarNFacLin.setText("Aplicar");
        jButton_AplicarNFacLin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AplicarNFacLinActionPerformed(evt);
            }
        });

        jButton_AplicarRefFLin.setText("Aplicar");
        jButton_AplicarRefFLin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AplicarRefFLinActionPerformed(evt);
            }
        });

        jButton_EjecutarFLin.setText("Ejecutar");
        jButton_EjecutarFLin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EjecutarFLinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jComboBox_opeRefFLin, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox_opeNFacFLin, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_consultaNFacFLin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                            .addComponent(txt_consultaRefFLin))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton_AplicarNFacLin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_AplicarRefFLin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(18, 18, 18)
                        .addComponent(txt_consutaFLin)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_EjecutarFLin)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jComboBox_opeNFacFLin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_consultaNFacFLin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_AplicarNFacLin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jComboBox_opeRefFLin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_consultaRefFLin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_AplicarRefFLin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txt_consutaFLin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_EjecutarFLin))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable_ConsultaFLin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(jTable_ConsultaFLin);

        javax.swing.GroupLayout jDialog_Consulta_FLinLayout = new javax.swing.GroupLayout(jDialog_Consulta_FLin.getContentPane());
        jDialog_Consulta_FLin.getContentPane().setLayout(jDialog_Consulta_FLinLayout);
        jDialog_Consulta_FLinLayout.setHorizontalGroup(
            jDialog_Consulta_FLinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_Consulta_FLinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_Consulta_FLinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog_Consulta_FLinLayout.setVerticalGroup(
            jDialog_Consulta_FLinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_Consulta_FLinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                .addContainerGap())
        );

        jDialog_Consulta_FTot.setTitle("Consulta FacturasCab.");

        jLabel31.setText("Consulta:");

        jLabel32.setText("Filtro por Nº Factura:");

        jComboBox_opeNFacFTot.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "<=", "<", ">=", ">" }));

        jButton_AplicarNFacFTot.setText("Aplicar");
        jButton_AplicarNFacFTot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AplicarNFacFTotActionPerformed(evt);
            }
        });

        jButton_EjecutarFTot.setText("Ejecutar");
        jButton_EjecutarFTot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EjecutarFTotActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox_opeNFacFTot, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_consultaNFacFTot, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                        .addGap(26, 26, 26)
                        .addComponent(jButton_AplicarNFacFTot))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(18, 18, 18)
                        .addComponent(txt_consutaFTot)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_EjecutarFTot)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jComboBox_opeNFacFTot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_consultaNFacFTot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_AplicarNFacFTot))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txt_consutaFTot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_EjecutarFTot))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable_ConsultaFTot.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane7.setViewportView(jTable_ConsultaFTot);

        javax.swing.GroupLayout jDialog_Consulta_FTotLayout = new javax.swing.GroupLayout(jDialog_Consulta_FTot.getContentPane());
        jDialog_Consulta_FTot.getContentPane().setLayout(jDialog_Consulta_FTotLayout);
        jDialog_Consulta_FTotLayout.setHorizontalGroup(
            jDialog_Consulta_FTotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_Consulta_FTotLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_Consulta_FTotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog_Consulta_FTotLayout.setVerticalGroup(
            jDialog_Consulta_FTotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_Consulta_FTotLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Tabla: ");

        JCombo_Tablas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Articulos", "Clientes", "FacturasCab", "FacturasLin", "FacturasTot", "EstadisticasClientes" }));
        JCombo_Tablas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JCombo_TablasActionPerformed(evt);
            }
        });

        jButton_AltaPrincipal.setText("ALTA / MODIFICACIÓN");
        jButton_AltaPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AltaPrincipalActionPerformed(evt);
            }
        });

        jButton_BajaPrincipal.setText("BAJA");
        jButton_BajaPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_BajaPrincipalActionPerformed(evt);
            }
        });

        jButton_ConsultaPrincipal.setText("CONSULTA");
        jButton_ConsultaPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ConsultaPrincipalActionPerformed(evt);
            }
        });

        jButton_Generar.setText("Generar estadisticas clientes");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JCombo_Tablas, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton_Generar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton_AltaPrincipal)
                .addGap(18, 18, 18)
                .addComponent(jButton_BajaPrincipal)
                .addGap(18, 18, 18)
                .addComponent(jButton_ConsultaPrincipal)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(JCombo_Tablas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_AltaPrincipal)
                    .addComponent(jButton_BajaPrincipal)
                    .addComponent(jButton_ConsultaPrincipal)
                    .addComponent(jButton_Generar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla.setEnabled(false);
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);

        jButton_Exportar.setText("Exportar Factura");
        jButton_Exportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ExportarActionPerformed(evt);
            }
        });

        jButton_Importar.setText("Importar Factura");
        jButton_Importar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ImportarActionPerformed(evt);
            }
        });

        jLabel23.setText("Nº Factura:");

        jLabel33.setText("Nombre archivo:");

        jButton1.setText("Importar oferta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel34.setText("DNI/CIF:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_dniOferta, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_NfacturaExportar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_NombreArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton_Exportar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton_Importar)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Exportar)
                    .addComponent(jButton_Importar)
                    .addComponent(jLabel23)
                    .addComponent(txt_NfacturaExportar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(txt_NombreArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(txt_dniOferta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Rellena jTable
    private void JCombo_TablasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JCombo_TablasActionPerformed
        getTablaSeleccionadaJCombo();
    }//GEN-LAST:event_JCombo_TablasActionPerformed

    //Abre jDialog
    private void jButton_AltaPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AltaPrincipalActionPerformed
        getTablaSeleccionadaJButton();
    }//GEN-LAST:event_jButton_AltaPrincipalActionPerformed

    //Obtiene posicion elemento clickeado en jTable
    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        posicion = tabla.rowAtPoint(evt.getPoint());
        getTablaSeleccionadaJtable(posicion);
    }//GEN-LAST:event_tablaMouseClicked

    //Alta de cliente
    private void jButton_altaCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_altaCliActionPerformed
        if(filtroCliente()){
            crearCliente();
            int cont =0;
            for (Clientes var : listaClientes){
                if(cli_dat.getDnicif().equals(var.getDnicif())){
                    cont++;
                }
            }
            if(cont==0){
                metodos.anadirObjeto(cli_dat);
                listaClientes.clear();
                listaClientes = metodos.getListaClientes();
                getTablaSeleccionadaJCombo();
                Util.operacion_realizada();
                jDialog_Cliente.dispose();
            }else{
                Util.mensaje_aviso("Error añadir cliente.", "El DNI/CIF introducido ya existe en la base de datos.");
            }
        }

    }//GEN-LAST:event_jButton_altaCliActionPerformed

    //Modificación de cliente
    private void jButton_modCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_modCliActionPerformed
        if(filtroCliente()){
            crearCliente();
            int cont =0;
            for (Clientes var : listaClientes){
                if(cli_dat.getDnicif().equals(var.getDnicif())){
                    cont++;
                }
            }
            if(cont==1){
                metodos.actualizarObjeto(cli_dat);
                listaClientes.clear();
                listaClientes = metodos.getListaClientes();
                getTablaSeleccionadaJCombo();
                Util.operacion_realizada();
                jDialog_Cliente.dispose();
            }else{
                Util.mensaje_aviso("Error actualizar cliente.", "El DNI/CIF introducido no existe en la base de datos.");
            }
        }
    }//GEN-LAST:event_jButton_modCliActionPerformed

    //Cierra jDialog
    private void jButton_cancelarCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_cancelarCliActionPerformed
        jDialog_Cliente.dispose();
        Util.mensaje_info("Operación cancelada.", "Ha cancelado la operación.");
    }//GEN-LAST:event_jButton_cancelarCliActionPerformed

    //Alta de articulo    
    private void jButton_altaArtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_altaArtActionPerformed
        if(filtroArticulo()){
            crearArticulo();
            int cont =0;
            for (Articulos var : listaArticulos){
                if(art_dat.getReferencia().equals(var.getReferencia())){
                    cont++;
                }
            }
            for (Articulos var : listaArticulos){
                if(art_dat.getDescripcion().equals(var.getDescripcion())){
                    cont--;
                }
            }
            if(cont==0){
                metodos.anadirObjeto(art_dat);
                listaArticulos.clear();
                listaArticulos = metodos.getListaArticulos();
                getTablaSeleccionadaJCombo();
                Util.operacion_realizada();
                jDialog_Articulo.dispose();
            }else{
                Util.mensaje_aviso("Error añadir articulo.", "La referencia o descripción introducida ya existe en la base de datos.");
            }
        }
    }//GEN-LAST:event_jButton_altaArtActionPerformed

    //Modificación de articulo
    private void jButton_modArtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_modArtActionPerformed
        if(filtroArticulo()){
            crearArticulo();
            int cont =0;
            boolean errordni = true;
            if(!art_dat.getDescripcion().equals(txt_descripcionArt.getText())){
                for (Articulos var : listaArticulos){
                    if(art_dat.getDescripcion().equals(var.getDescripcion())){
                        Util.mensaje_aviso("Error actualizar articulo.", "La descripción introducida ya existe en la base de datos.");
                        errordni = false;
                        cont--;
                    }
                }
            }
            
            for (Articulos var : listaArticulos){
                if(art_dat.getReferencia().equals(var.getReferencia())){
                    var.setDescripcion(art_dat.getDescripcion());
                    var.setPorciva(art_dat.getPorciva());
                    var.setPrecio(art_dat.getPrecio());
                    var.setStock(art_dat.getStock());
                    art_dat = var;
                    cont++;
                }
            }
            
            if(cont!=0){
                metodos.actualizarObjeto(art_dat);
                listaArticulos.clear();
                listaArticulos = metodos.getListaArticulos();
                getTablaSeleccionadaJCombo();
                Util.operacion_realizada();
                jDialog_Articulo.dispose();                
            }else{
                if(errordni){
                    Util.mensaje_aviso("Error actualizar articulo.", "La referencia introducida no existe en la base de datos.");
                }
            }
        }
    }//GEN-LAST:event_jButton_modArtActionPerformed

    //Cierra jDialog
    private void jButton_cancelarArtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_cancelarArtActionPerformed
        jDialog_Articulo.dispose();
        Util.mensaje_info("Operación cancelada.", "Ha cancelado la operación.");
    }//GEN-LAST:event_jButton_cancelarArtActionPerformed

    //Alta de FacturaCab
    private void jButton_altaFCabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_altaFCabActionPerformed
        if(filtroFacturaCab()){
            crearFacturaCab();
            fTot_dat = new FacturasTot(fCab_dat);
            int cont =0;
            for (FacturasCab var : listaFacturasCab){
                if(fCab_dat.getNumfac()==var.getNumfac()){
                    cont++;
                }
            }
            if(cont==0){
                metodos.anadirObjeto(fCab_dat);
                metodos.anadirObjeto(fTot_dat);
                listaFacturasCab.clear();
                listaFacturasTot.clear();
                listaFacturasTot = metodos.getListaFacturasTot();
                listaFacturasCab = metodos.getListaFacturasCab();
                getTablaSeleccionadaJCombo();
                Util.operacion_realizada();
                jDialog_FacturaCab.dispose();
            }else{
                Util.mensaje_aviso("Error añadir FacturaCab.", "El Nº factura introducido ya existe en la base de datos.");
            }
        }
    }//GEN-LAST:event_jButton_altaFCabActionPerformed

    //Modificación de FacturaCab
    private void jButton_modFCabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_modFCabActionPerformed
        if(filtroFacturaCab()){
            crearFacturaCab();
            boolean ejecutar = false;
            int cont =0;
            for (FacturasCab var : listaFacturasCab){
                if(fCab_dat.getNumfac()==var.getNumfac()){
                    cont++;
                }
            }
            
            for(FacturasTot var : listaFacturasTot){
                if(var.getFacturasCab().getNumfac()==fCab_dat.getNumfac()){
                    ejecutar = true;
                    fTot_dat = var;
                    for(Clientes var2 : listaClientes){
                        if(var2.getDnicif().equals(jComboBox_dniFCab.getSelectedItem().toString())){
                            cli_dat= var2;
                        }
                    }
                    fTot_dat.getFacturasCab().setClientes(cli_dat);
                }
            }
            
            if(cont==1){
                for(FacturasLin var : listaFacturasLin){
                    if(fCab_dat.getNumfac()== var.getFacturasCab().getNumfac()){
                        var.setFacturasCab(fCab_dat);
                        metodos.actualizarObjeto(var);
                    }
                }
                listaFacturasLin.clear();
                listaFacturasLin = metodos.getListaFacturasLin();
                metodos.actualizarObjeto(fCab_dat);
                if(ejecutar){
                    metodos.borrarObjeto(fTot_dat);
                    metodos.anadirObjeto(fTot_dat);
                }
                listaFacturasCab.clear();
                listaFacturasCab = metodos.getListaFacturasCab();
                listaFacturasTot.clear();
                listaFacturasTot = metodos.getListaFacturasTot();
                getTablaSeleccionadaJCombo();
                Util.operacion_realizada();
                jDialog_FacturaCab.dispose();
                                
            }else{
                Util.mensaje_aviso("Error actualizar FacturaCab.", "El Nº factura introducido no existe en la base de datos.");
            }
        }
    }//GEN-LAST:event_jButton_modFCabActionPerformed

    //Cierra jDialog
    private void jButton_cancelarFCabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_cancelarFCabActionPerformed
        jDialog_FacturaCab.dispose();
        Util.mensaje_info("Operación cancelada.", "Ha cancelado la operación.");
    }//GEN-LAST:event_jButton_cancelarFCabActionPerformed

    //Abre jDialog
    private void jButton_BajaPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BajaPrincipalActionPerformed
        getTablaSeleccionadaJButton();
    }//GEN-LAST:event_jButton_BajaPrincipalActionPerformed

    //Baja de Articulo
    private void jButton_bajaArtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_bajaArtActionPerformed
        if(filtroArticulo()){
            crearArticulo();
            boolean borrar = false;
            boolean no_existe = false;
            boolean art_facLin= false;
            for (Articulos var :listaArticulos){
                if(var.getReferencia().equals(art_dat.getReferencia())){
                    borrar = true;
                    no_existe = true;
                }
            }
            for (FacturasLin var : listaFacturasLin){
                if(var.getArticulos().getReferencia().equals(art_dat.getReferencia())){
                    art_facLin = true;
                    borrar = false;
                }

            }

            if(borrar){
                metodos.borrarObjeto(art_dat);
                listaArticulos.clear();
                listaArticulos = metodos.getListaArticulos();
                getTablaSeleccionadaJCombo();
                Util.operacion_realizada();
                jDialog_Articulo.dispose();
            }else{
                if(!no_existe){
                    Util.mensaje_aviso("Operacion no realizada.", "No se ha encontrado el articulo.");
                }
                if(art_facLin){
                    Util.mensaje_aviso("Operacion no realizada.", "Articulo relacionado a una factura.");
                }
            }
        }
    }//GEN-LAST:event_jButton_bajaArtActionPerformed

    //Baja de Cliente
    private void jButton_bajaCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_bajaCliActionPerformed
        if(filtroCliente()){   
            crearCliente();
            boolean borrar = false;
            boolean no_existe = false;
            boolean cli_facCab= false;
            for (Clientes var :listaClientes){
                if(var.getDnicif().equals(cli_dat.getDnicif())){
                    borrar = true;
                    no_existe = true;
                }
            }
            for (FacturasCab var : listaFacturasCab){
                if(var.getClientes().getDnicif().equals(cli_dat.getDnicif())){
                    cli_facCab = true;
                    borrar = false;
                }

            }

            if(borrar){
                metodos.borrarObjeto(cli_dat);
                listaClientes.clear();
                listaClientes = metodos.getListaClientes();
                getTablaSeleccionadaJCombo();
                Util.operacion_realizada();
                jDialog_Cliente.dispose();
            }else{
                if(!no_existe){
                    Util.mensaje_aviso("Operacion no realizada.", "No se ha encontrado el cliente.");
                }
                if(cli_facCab){
                    Util.mensaje_aviso("Operacion no realizada.", "Cliente relacionado a una factura.");
                }
            }
        }
    }//GEN-LAST:event_jButton_bajaCliActionPerformed

    //Baja de FacturaCab
    private void jButton_bajaFCabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_bajaFCabActionPerformed
        if(filtroFacturaCab()){   
            crearFacturaCab();
            boolean borrar = false;
            boolean ftot_existe= false;
            for (FacturasCab var :listaFacturasCab){
                if(var.getNumfac()==fCab_dat.getNumfac()){
                    borrar = true;
                }
            }
            for (FacturasTot var : listaFacturasTot){
                if(var.getFacturasCab().getNumfac()==fCab_dat.getNumfac()){
                    ftot_existe = true;
                    fTot_dat=var;
                }

            }

            if(borrar){
                for(FacturasLin var : listaFacturasLin){
                    if(fCab_dat.getNumfac()== var.getFacturasCab().getNumfac()){
                        metodos.borrarObjeto(var);
                    }
                }
                listaFacturasLin.clear();
                listaFacturasLin = metodos.getListaFacturasLin();
                
                metodos.borrarObjeto(fCab_dat);
                listaFacturasCab.clear();
                listaFacturasCab = metodos.getListaFacturasCab();

                if(ftot_existe){
                    metodos.borrarObjeto(fTot_dat);
                    listaFacturasTot.clear();
                    listaFacturasTot = metodos.getListaFacturasTot();
                }

                getTablaSeleccionadaJCombo();
                Util.operacion_realizada();
                jDialog_Cliente.dispose();
            }else{
                
                Util.mensaje_aviso("Operacion no realizada.", "No se ha encontrado el cliente.");
                
            }
        }
    }//GEN-LAST:event_jButton_bajaFCabActionPerformed

    //Alta de FacturaLin
    private void jButton_altaFLinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_altaFLinActionPerformed
        if(filtroFacturaLin()){
            crearFacturaLin();
            double stock = Double.valueOf(art_dat.getStock().toString());
            double cant = Double.valueOf(fLin_dat.getCantidad().toString());
            BigDecimal total = new BigDecimal(stock-cant);
            art_dat.setStock(total);
            
            
            double precio = Double.valueOf(art_dat.getPrecio().toString());
            double iva = Double.valueOf(art_dat.getPorciva().toString());
            iva =iva /100;
            double dto = Double.valueOf(fLin_dat.getDtolinea().toString());
            dto = dto/100;
            double importe_base = precio*cant;
            double importe_dto =importe_base*dto;
            double importe_iva = (importe_base-importe_dto)*iva;
            BigDecimal imp_base = new BigDecimal(importe_base);
            BigDecimal imp_dto = new BigDecimal(importe_dto);
            BigDecimal imp_iva = new BigDecimal(importe_iva);
            BigDecimal imp_total = new BigDecimal(importe_base-importe_dto+importe_iva);
            
            for(FacturasTot var : listaFacturasTot){
                if(var.getFacturasCab().getNumfac()==fCab_dat.getNumfac()){
                    fTot_dat = var;
                }
            }
            
            try {
                sumarBigDecimal(imp_base, imp_dto, imp_iva, imp_total);
                
            } catch (NullPointerException e) {
                fTot_dat.setBaseimp(imp_base);
                fTot_dat.setImpDto(imp_dto);
                fTot_dat.setImpIva(imp_iva);
                fTot_dat.setTotalfac(imp_total);
            }
            fTot_dat.setFacturasCab(fCab_dat);

            long nlinea = Long.valueOf(txt_lineaFLin.getText());
            long nfactura = Long.valueOf(jComboBox_nfacturaFLin.getSelectedItem().toString());
            boolean ejecutar=true;
            for(FacturasLin var : listaFacturasLin){
                if(var.getId().getLineafac()==nlinea){
                    if(var.getId().getNumfac()==nfactura) {
                        Util.mensaje_aviso("Error formato.", "Introduzca un  Nº linea que no exista.");
                        ejecutar=false;
                    }
                }
            }
            if(ejecutar){
                
                metodos.actualizarObjeto(art_dat);
                metodos.anadirObjeto(fLin_dat);
                metodos.borrarObjeto(fTot_dat);
                metodos.anadirObjeto(fTot_dat);
                listaFacturasLin.clear();
                listaFacturasLin = metodos.getListaFacturasLin();
                listaFacturasTot.clear();
                listaFacturasTot = metodos.getListaFacturasTot();
                listaArticulos.clear();
                listaArticulos = metodos.getListaArticulos();
                getTablaSeleccionadaJCombo();
                Util.operacion_realizada();
                jDialog_FacturaLin.dispose(); 
            }
                
        }
    }//GEN-LAST:event_jButton_altaFLinActionPerformed

    //Modificación de FacturaLin
    private void jButton_modificacionFLinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_modificacionFLinActionPerformed
        if(filtroFacturaLin()){
            crearFacturaLin();
            FacturasLin fLin_Antigua= new FacturasLin();
            Articulos art_Antiguo = new Articulos();
            for(FacturasLin var : listaFacturasLin){
                if( var.getId().equals(fLin_dat.getId())){
                    fLin_Antigua = var;
                    art_Antiguo = var.getArticulos();
                    
                }
            }
            
            //Calculamos stock
            double stock = Double.valueOf(art_dat.getStock().toString());
            double cant_a = Double.valueOf(fLin_Antigua.getCantidad().toString());
            double cant_n = Double.valueOf(fLin_dat.getCantidad().toString());
            stock = stock+cant_a-cant_n;
            BigDecimal stock_total = new BigDecimal(stock);
            art_dat.setStock(stock_total);
            //art_dat listo para actualizar stock

            metodos.actualizarObjeto(art_dat);
            metodos.actualizarObjeto(fLin_dat);
            listaFacturasLin.clear();
            listaFacturasLin = metodos.getListaFacturasLin();
            listaArticulos.clear();
            listaArticulos = metodos.getListaArticulos();            
            
            
            //Calculamos importes totales
            double imp_base_total=0;
            double imp_dto_total=0;
            double imp_iva_total=0;
            double imp_total_total=0;
            
            for(FacturasTot var : listaFacturasTot){
                if(var.getFacturasCab().getNumfac()==fCab_dat.getNumfac()){
                    fTot_dat = var;
                }
            }
            for(FacturasLin var_fLin : listaFacturasLin){
                if (var_fLin.getId().getNumfac()== fTot_dat.getNumfac()){
                    for(Articulos var_art: listaArticulos){
                        if(var_art.getReferencia().equals(var_fLin.getArticulos().getReferencia())){
                            double precio = Double.valueOf(var_art.getPrecio().toString());
                            double cantidad = Double.valueOf(var_fLin.getCantidad().toString());
                            double importe_base = precio*cantidad;

                            double dto = Double.valueOf(var_fLin.getDtolinea().toString());
                            dto = dto/100;
                            double importe_dto =importe_base*dto;

                            double iva = Double.valueOf(var_art.getPorciva().toString());
                            iva =iva /100;
                            double importe_iva = (importe_base-importe_dto)*iva;

                            imp_base_total=imp_base_total+importe_base;
                            imp_dto_total=imp_dto_total+importe_dto;
                            imp_iva_total=imp_iva_total+importe_iva;
                            imp_total_total=imp_total_total+importe_base-importe_dto+importe_iva;
                        }
                    }
                }
            }
            BigDecimal imp_base = new BigDecimal(imp_base_total);
            BigDecimal imp_dto = new BigDecimal(imp_dto_total);
            BigDecimal imp_iva = new BigDecimal(imp_iva_total);
            BigDecimal imp_total = new BigDecimal(imp_total_total);

            fTot_dat.setFacturasCab(fCab_dat);
            fTot_dat.setBaseimp(imp_base);
            fTot_dat.setImpDto(imp_dto);
            fTot_dat.setImpIva(imp_iva);
            fTot_dat.setTotalfac(imp_total);
            
            metodos.borrarObjeto(fTot_dat);
            metodos.anadirObjeto(fTot_dat);

            listaFacturasTot.clear();
            listaFacturasTot = metodos.getListaFacturasTot();

            getTablaSeleccionadaJCombo();
            Util.operacion_realizada();
            jDialog_FacturaLin.dispose();
        }
    }//GEN-LAST:event_jButton_modificacionFLinActionPerformed

    //Baja de FacturaLin
    private void jButton_bajaFLinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_bajaFLinActionPerformed
        if(filtroFacturaLin()){
            crearFacturaLin();
            FacturasLin fLin_Antigua= new FacturasLin();
            Articulos art_Antiguo = new Articulos();
            for(FacturasLin var : listaFacturasLin){
                if( var.getId().equals(fLin_dat.getId())){
                    fLin_Antigua = var;
                    art_Antiguo = var.getArticulos();
                    
                }
            }
            
            //Calculamos stock
            double stock = Double.valueOf(art_dat.getStock().toString());
            double cant_n = Double.valueOf(fLin_dat.getCantidad().toString());
            stock = stock+cant_n;
            BigDecimal stock_total = new BigDecimal(stock);
            art_dat.setStock(stock_total);
            //art_dat listo para actualizar stock

            metodos.actualizarObjeto(art_dat);
            metodos.borrarObjeto(fLin_dat);
            listaFacturasLin.clear();
            listaFacturasLin = metodos.getListaFacturasLin();
            listaArticulos.clear();
            listaArticulos = metodos.getListaArticulos();            
            
            
            //Calculamos importes totales
            double imp_base_total=0;
            double imp_dto_total=0;
            double imp_iva_total=0;
            double imp_total_total=0;
            
            for(FacturasTot var : listaFacturasTot){
                if(var.getFacturasCab().getNumfac()==fCab_dat.getNumfac()){
                    fTot_dat = var;
                }
            }
            for(FacturasLin var_fLin : listaFacturasLin){
                if (var_fLin.getId().getNumfac()== fTot_dat.getNumfac()){
                    for(Articulos var_art: listaArticulos){
                        if(var_art.getReferencia().equals(var_fLin.getArticulos().getReferencia())){
                            double precio = Double.valueOf(var_art.getPrecio().toString());
                            double cantidad = Double.valueOf(var_fLin.getCantidad().toString());
                            double importe_base = precio*cantidad;

                            double dto = Double.valueOf(var_fLin.getDtolinea().toString());
                            dto = dto/100;
                            double importe_dto =importe_base*dto;

                            double iva = Double.valueOf(var_art.getPorciva().toString());
                            iva =iva /100;
                            double importe_iva = (importe_base-importe_dto)*iva;

                            imp_base_total=imp_base_total+importe_base;
                            imp_dto_total=imp_dto_total+importe_dto;
                            imp_iva_total=imp_iva_total+importe_iva;
                            imp_total_total=imp_total_total+importe_base-importe_dto+importe_iva;
                        }
                    }
                }
            }
            BigDecimal imp_base = new BigDecimal(imp_base_total);
            BigDecimal imp_dto = new BigDecimal(imp_dto_total);
            BigDecimal imp_iva = new BigDecimal(imp_iva_total);
            BigDecimal imp_total = new BigDecimal(imp_total_total);

            fTot_dat.setFacturasCab(fCab_dat);
            fTot_dat.setBaseimp(imp_base);
            fTot_dat.setImpDto(imp_dto);
            fTot_dat.setImpIva(imp_iva);
            fTot_dat.setTotalfac(imp_total);
            
            metodos.borrarObjeto(fTot_dat);
            metodos.anadirObjeto(fTot_dat);

            listaFacturasTot.clear();
            listaFacturasTot = metodos.getListaFacturasTot();

            getTablaSeleccionadaJCombo();
            Util.operacion_realizada();
            jDialog_FacturaLin.dispose();
        }
    }//GEN-LAST:event_jButton_bajaFLinActionPerformed

    //Abre jDialog
    private void jButton_ConsultaPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ConsultaPrincipalActionPerformed
        getTablaSeleccionadaConsulta();
                
    }//GEN-LAST:event_jButton_ConsultaPrincipalActionPerformed

    //Crea consulta segun referencia de articulo
    private void jButton_AplicarRefArtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AplicarRefArtActionPerformed
        
        if(!txt_consultaRefArt.getText().equals("")){
            String consulta = con_base_Art+"referencia "+jComboBox_opeRefArt.getSelectedItem().toString();
            consulta = consulta +" '"+txt_consultaRefArt.getText()+"' ";
            txt_consutaArt.setText(consulta);
        }else{
            Util.mensaje_aviso("Error.", "Introduce una referencia.");
        }
        
    }//GEN-LAST:event_jButton_AplicarRefArtActionPerformed

    //Crea consulta segun precio articulo
    private void jButton_AplicarPreArtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AplicarPreArtActionPerformed
        if(!txt_consultaPreArt.getText().equals("")){
            try{
                double precio = Double.valueOf(txt_consultaPreArt.getText());
                if (precio<0){
                    Util.mensaje_aviso("Error.", "Introduce un valor positivo o 0.");
                }else{
                    String consulta = con_base_Art+"precio "+jComboBox_opePreArt.getSelectedItem().toString();
                    consulta = consulta +" "+txt_consultaPreArt.getText();
                    txt_consutaArt.setText(consulta);
                }

            }catch(Exception e){
                Util.mensaje_aviso("Error", "Introduce un numero correcto. Para decimales utilice el '.' .");
            }

        }else{
            Util.mensaje_aviso("Error.", "Introduce una referencia.");
        }

    }//GEN-LAST:event_jButton_AplicarPreArtActionPerformed

    //Crea consulta segun stock articulo
    private void jButton_AplicarStoArtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AplicarStoArtActionPerformed
        if(!txt_consultaStoArt.getText().equals("")){
            try{
                double precio = Double.valueOf(txt_consultaStoArt.getText());
                if (precio<0){
                    Util.mensaje_aviso("Error.", "Introduce un valor positivo o 0.");
                }else{
                    String consulta = con_base_Art+"stock "+jComboBox_opeStoArt.getSelectedItem().toString();
                    consulta = consulta +" "+txt_consultaStoArt.getText();
                    txt_consutaArt.setText(consulta);
                }

            }catch(Exception e){
                Util.mensaje_aviso("Error", "Introduce un numero correcto. Para decimales utilice el '.' .");
            }

        }else{
            Util.mensaje_aviso("Error.", "Introduce una referencia.");
        }// TODO add your handling code here:
    }//GEN-LAST:event_jButton_AplicarStoArtActionPerformed

    //Ejecuta consulta y rellena tabla
    private void jButton_EjecutarArtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EjecutarArtActionPerformed
        if(txt_consutaArt.getText().equals("")){
            Util.mensaje_aviso("Error.", "Introduce una consulta.");
        }else{
            listaArtConsulta = metodos.getListaArticulosConsulta(txt_consutaArt.getText());
            crearTablaArticulosConsulta();
        }
    }//GEN-LAST:event_jButton_EjecutarArtActionPerformed

    //Crea consulta segun dni cliente
    private void jButton_AplicarDniCLiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AplicarDniCLiActionPerformed
        if(!txt_consultaDniCli.getText().equals("")){
            String consulta = con_base_Cli+"dnicif "+jComboBox_opeDniCli.getSelectedItem().toString();
            consulta = consulta +" '"+txt_consultaDniCli.getText()+"' ";
            txt_consutaCli.setText(consulta);
        }else{
            Util.mensaje_aviso("Error.", "Introduce un DNI/CIF.");
        }

    }//GEN-LAST:event_jButton_AplicarDniCLiActionPerformed

    //Crea consulta segun nombre cliente
    private void jButton_AplicarNomCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AplicarNomCliActionPerformed
        if(!txt_consultaNomCli.getText().equals("")){
            String consulta = con_base_Cli+"nombre "+jComboBox_opeNomCli.getSelectedItem().toString();
            consulta = consulta +" '"+txt_consultaNomCli.getText()+"' ";
            txt_consutaCli.setText(consulta);
        }else{
            Util.mensaje_aviso("Error.", "Introduce un nombre.");
        }

    }//GEN-LAST:event_jButton_AplicarNomCliActionPerformed

    //Ejecuta consulta y rellena tabla
    private void jButton_EjecutarCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EjecutarCliActionPerformed
        if(txt_consutaCli.getText().equals("")){
            Util.mensaje_aviso("Error.", "Introduce una consulta.");
        }else{
            listaCliConsulta = metodos.getListaClientesConsulta(txt_consutaCli.getText());
            crearTablaClientesConsulta();
        }

    }//GEN-LAST:event_jButton_EjecutarCliActionPerformed

    //Crea consulta segun Nºfac de Fcab
    private void jButton_AplicarNFacCabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AplicarNFacCabActionPerformed
        if(!txt_consultaNFacFCab.getText().equals("")){
            try{
                long nfact = Long.valueOf(txt_consultaNFacFCab.getText());
                if (nfact<=0){
                    Util.mensaje_aviso("Error.", "Introduce un valor positivo.");
                }else{
                    String consulta = con_base_FCab+"numfac "+jComboBox_opeNFacFCab.getSelectedItem().toString();
                    consulta = consulta +" "+txt_consultaNFacFCab.getText();
                    txt_consutaFCab.setText(consulta);
                }

            }catch(Exception e){
                Util.mensaje_aviso("Error", "Introduce un numero correcto.");
            }

        }else{
            Util.mensaje_aviso("Error.", "Introduce un Nº de factura.");
        }
        
    }//GEN-LAST:event_jButton_AplicarNFacCabActionPerformed

    //Ejecuta consulta y rellena tabla
    private void jButton_EjecutarFCabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EjecutarFCabActionPerformed
        if(txt_consutaFCab.getText().equals("")){
            Util.mensaje_aviso("Error.", "Introduce una consulta.");
        }else{
            listaFCabConsulta = metodos.getListaFCabConsulta(txt_consutaFCab.getText());
            if(listaFCabConsulta.isEmpty()){
                Util.mensaje_info("Operacion realizada.", "No se han encontrado resultados.");
            }else{
                crearTablaFacturasCabConsulta();
            }
        }      
    }//GEN-LAST:event_jButton_EjecutarFCabActionPerformed

    //Crea consulta segun dni de Fcab
    private void jButton_AplicarDniFCabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AplicarDniFCabActionPerformed
        if(!txt_consultaDniFCab.getText().equals("")){
            String consulta = con_base_FCab+"clientes.dnicif "+jComboBox_opeDniFCab.getSelectedItem().toString();
            consulta = consulta +" '"+txt_consultaDniFCab.getText()+"' ";
            txt_consutaFCab.setText(consulta);
        }else{
            Util.mensaje_aviso("Error.", "Introduce un DNI/CIF.");
        }
    }//GEN-LAST:event_jButton_AplicarDniFCabActionPerformed

    //Crea consulta segun Nºfac de FLin
    private void jButton_AplicarNFacLinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AplicarNFacLinActionPerformed
        if(!txt_consultaNFacFLin.getText().equals("")){
            try{
                long nfact = Long.valueOf(txt_consultaNFacFLin.getText());
                if (nfact<=0){
                    Util.mensaje_aviso("Error.", "Introduce un valor positivo.");
                }else{
                    String consulta = con_base_FLin+"id.numfac "+jComboBox_opeNFacFLin.getSelectedItem().toString();
                    consulta = consulta +" "+txt_consultaNFacFLin.getText();
                    txt_consutaFLin.setText(consulta);
                }

            }catch(Exception e){
                Util.mensaje_aviso("Error", "Introduce un numero correcto.");
            }

        }else{
            Util.mensaje_aviso("Error.", "Introduce un Nº de factura.");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton_AplicarNFacLinActionPerformed

    //Crea consulta segun ref de FLin
    private void jButton_AplicarRefFLinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AplicarRefFLinActionPerformed
        if(!txt_consultaRefFLin.getText().equals("")){
            String consulta = con_base_FLin+"articulos.referencia "+jComboBox_opeRefFLin.getSelectedItem().toString();
            consulta = consulta +" '"+txt_consultaRefFLin.getText()+"' ";
            txt_consutaFLin.setText(consulta);
        }else{
            Util.mensaje_aviso("Error.", "Introduce una referencia.");
        }
    }//GEN-LAST:event_jButton_AplicarRefFLinActionPerformed

    //Ejecuta consulta y rellena tabla
    private void jButton_EjecutarFLinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EjecutarFLinActionPerformed
        if(txt_consutaFLin.getText().equals("")){
            Util.mensaje_aviso("Error.", "Introduce una consulta.");
        }else{
            listaFLinConsulta = metodos.getListaFLinConsulta(txt_consutaFLin.getText());
            if(listaFLinConsulta.isEmpty()){
                Util.mensaje_info("Operacion realizada.", "No se han encontrado resultados.");
            }else{
                crearTablaFacturasLinConsulta();
            }
        }  
    }//GEN-LAST:event_jButton_EjecutarFLinActionPerformed

    //Crea consulta segun Nºfac de FTot
    private void jButton_AplicarNFacFTotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AplicarNFacFTotActionPerformed
        if(!txt_consultaNFacFTot.getText().equals("")){
            try{
                long nfact = Long.valueOf(txt_consultaNFacFTot.getText());
                if (nfact<=0){
                    Util.mensaje_aviso("Error.", "Introduce un valor positivo.");
                }else{
                    String consulta = con_base_FTot+"id.numfac "+jComboBox_opeNFacFTot.getSelectedItem().toString();
                    consulta = consulta +" "+txt_consultaNFacFTot.getText();
                    txt_consutaFTot.setText(consulta);
                }

            }catch(Exception e){
                Util.mensaje_aviso("Error", "Introduce un numero correcto.");
            }

        }else{
            Util.mensaje_aviso("Error.", "Introduce un Nº de factura.");
        }
    }//GEN-LAST:event_jButton_AplicarNFacFTotActionPerformed

    //Ejecuta consulta y rellena tabla
    private void jButton_EjecutarFTotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EjecutarFTotActionPerformed
        if(txt_consutaFTot.getText().equals("")){
            Util.mensaje_aviso("Error.", "Introduce una consulta.");
        }else{
            listaFTotConsulta = metodos.getListaFTotConsulta(txt_consutaFTot.getText());
            if(listaFTotConsulta.isEmpty()){
                Util.mensaje_info("Operacion realizada.", "No se han encontrado resultados.");
            }else{
                crearTablaFacturasTotConsulta();
            }
        }  
    }//GEN-LAST:event_jButton_EjecutarFTotActionPerformed

    //crea xml
    private void jButton_ExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ExportarActionPerformed
        if(!txt_NfacturaExportar.getText().equals("")){
            boolean aviso1 = true;
            boolean aviso2 = true;
            boolean crearXml = false;
            long numfac=0;

            for(FacturasCab var : listaFacturasCab){
                if(var.getNumfac()==Long.valueOf(txt_NfacturaExportar.getText())){
                    aviso1=false;
                    for(FacturasLin var2:listaFacturasLin){
                        if(var2.getId().getNumfac()==Long.valueOf(txt_NfacturaExportar.getText())){
                            aviso2=false;
                            crearXml=true;
                            numfac=var.getNumfac();

                        }
                    }
                }
            }
            if(crearXml){
                if(!txt_NombreArchivo.getText().equals("")){
                    JFileChooser selecDireccion = new JFileChooser();
                    selecDireccion.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    Component parent = null;
                    int returnVal = selecDireccion.showSaveDialog(parent);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        String ruta = selecDireccion.getSelectedFile().getPath();
                        crearXML(numfac, ruta+"/", txt_NombreArchivo.getText());
                        Util.mensaje_info("Factura exportada.", "Archivo .xml creado correctamente.");
                    }else{
                        Util.mensaje_aviso("Error.", "Seleccione un directorio.");
                    } 
                }else{
                    Util.mensaje_aviso("Error.", "Introduce un nombre para el archivo.");
                }                     
            }
            if(aviso1){
                Util.mensaje_aviso("Error.", "No existe ese Nº de factura.");
            }
            if(aviso2){
                Util.mensaje_aviso("Error.", "Ese Nº de factura no tiene FLineas.");
            }
        }else{
            Util.mensaje_aviso("Error.", "Introduce un Nº de factura para exportar.");
        }
        
    }//GEN-LAST:event_jButton_ExportarActionPerformed

    //lee xml
    private void jButton_ImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ImportarActionPerformed
        JFileChooser selectArchivo = new JFileChooser();
        selectArchivo.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filtro=new FileNameExtensionFilter("XML","xml");
        selectArchivo.setFileFilter(filtro);
        Component parent = null;
        int returnVal = selectArchivo.showSaveDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f=selectArchivo.getSelectedFile();
            leerXML(f);
        }else{
            Util.mensaje_aviso("Error.", "Seleccione un archivo.");
        }
    }//GEN-LAST:event_jButton_ImportarActionPerformed

    //lee xml oferta
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser selectArchivo = new JFileChooser();
        selectArchivo.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filtro=new FileNameExtensionFilter("XML","xml");
        selectArchivo.setFileFilter(filtro);
        Component parent = null;
        int returnVal = selectArchivo.showSaveDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f=selectArchivo.getSelectedFile();
            if(!txt_dniOferta.getText().equals("")){
                importarOferta(f, txt_dniOferta.getText());
            }else{
                Util.mensaje_aviso("Error.", "Introduce un DNI/CIF.");
            }
        }else{
            Util.mensaje_aviso("Error.", "Seleccione un archivo.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    
    
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> JCombo_Tablas;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton_AltaPrincipal;
    private javax.swing.JButton jButton_AplicarDniCLi;
    private javax.swing.JButton jButton_AplicarDniFCab;
    private javax.swing.JButton jButton_AplicarNFacCab;
    private javax.swing.JButton jButton_AplicarNFacFTot;
    private javax.swing.JButton jButton_AplicarNFacLin;
    private javax.swing.JButton jButton_AplicarNomCli;
    private javax.swing.JButton jButton_AplicarPreArt;
    private javax.swing.JButton jButton_AplicarRefArt;
    private javax.swing.JButton jButton_AplicarRefFLin;
    private javax.swing.JButton jButton_AplicarStoArt;
    private javax.swing.JButton jButton_BajaPrincipal;
    private javax.swing.JButton jButton_ConsultaPrincipal;
    private javax.swing.JButton jButton_EjecutarArt;
    private javax.swing.JButton jButton_EjecutarCli;
    private javax.swing.JButton jButton_EjecutarFCab;
    private javax.swing.JButton jButton_EjecutarFLin;
    private javax.swing.JButton jButton_EjecutarFTot;
    private javax.swing.JButton jButton_Exportar;
    private javax.swing.JButton jButton_Generar;
    private javax.swing.JButton jButton_Importar;
    private javax.swing.JButton jButton_altaArt;
    private javax.swing.JButton jButton_altaCli;
    private javax.swing.JButton jButton_altaFCab;
    private javax.swing.JButton jButton_altaFLin;
    private javax.swing.JButton jButton_bajaArt;
    private javax.swing.JButton jButton_bajaCli;
    private javax.swing.JButton jButton_bajaFCab;
    private javax.swing.JButton jButton_bajaFLin;
    private javax.swing.JButton jButton_cancelarArt;
    private javax.swing.JButton jButton_cancelarCli;
    private javax.swing.JButton jButton_cancelarFCab;
    private javax.swing.JButton jButton_cancelarFLin;
    private javax.swing.JButton jButton_modArt;
    private javax.swing.JButton jButton_modCli;
    private javax.swing.JButton jButton_modFCab;
    private javax.swing.JButton jButton_modificacionFLin;
    private javax.swing.JComboBox<String> jComboBox_dniFCab;
    private javax.swing.JComboBox<String> jComboBox_ivaArt;
    private javax.swing.JComboBox<String> jComboBox_nfacturaFLin;
    private javax.swing.JComboBox<String> jComboBox_opeDniCli;
    private javax.swing.JComboBox<String> jComboBox_opeDniFCab;
    private javax.swing.JComboBox<String> jComboBox_opeNFacFCab;
    private javax.swing.JComboBox<String> jComboBox_opeNFacFLin;
    private javax.swing.JComboBox<String> jComboBox_opeNFacFTot;
    private javax.swing.JComboBox<String> jComboBox_opeNomCli;
    private javax.swing.JComboBox<String> jComboBox_opePreArt;
    private javax.swing.JComboBox<String> jComboBox_opeRefArt;
    private javax.swing.JComboBox<String> jComboBox_opeRefFLin;
    private javax.swing.JComboBox<String> jComboBox_opeStoArt;
    private javax.swing.JComboBox<String> jComboBox_referenciaFLin;
    private javax.swing.JDialog jDialog_Articulo;
    private javax.swing.JDialog jDialog_Cliente;
    private javax.swing.JDialog jDialog_Consulta_Art;
    private javax.swing.JDialog jDialog_Consulta_Cli;
    private javax.swing.JDialog jDialog_Consulta_FCab;
    private javax.swing.JDialog jDialog_Consulta_FLin;
    private javax.swing.JDialog jDialog_Consulta_FTot;
    private javax.swing.JDialog jDialog_FacturaCab;
    private javax.swing.JDialog jDialog_FacturaLin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTable_ConsultaArt;
    private javax.swing.JTable jTable_ConsultaCli;
    private javax.swing.JTable jTable_ConsultaFCab;
    private javax.swing.JTable jTable_ConsultaFLin;
    private javax.swing.JTable jTable_ConsultaFTot;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txt_NfacturaExportar;
    private javax.swing.JTextField txt_NombreArchivo;
    private javax.swing.JTextField txt_cantidadFLin;
    private javax.swing.JTextField txt_consultaDniCli;
    private javax.swing.JTextField txt_consultaDniFCab;
    private javax.swing.JTextField txt_consultaNFacFCab;
    private javax.swing.JTextField txt_consultaNFacFLin;
    private javax.swing.JTextField txt_consultaNFacFTot;
    private javax.swing.JTextField txt_consultaNomCli;
    private javax.swing.JTextField txt_consultaPreArt;
    private javax.swing.JTextField txt_consultaRefArt;
    private javax.swing.JTextField txt_consultaRefFLin;
    private javax.swing.JTextField txt_consultaStoArt;
    private javax.swing.JTextField txt_consutaArt;
    private javax.swing.JTextField txt_consutaCli;
    private javax.swing.JTextField txt_consutaFCab;
    private javax.swing.JTextField txt_consutaFLin;
    private javax.swing.JTextField txt_consutaFTot;
    private javax.swing.JTextArea txt_descripcionArt;
    private javax.swing.JTextField txt_descuentoFLin;
    private javax.swing.JTextField txt_dniCli;
    private javax.swing.JTextField txt_dniOferta;
    private javax.swing.JTextField txt_fechaFCab;
    private javax.swing.JTextField txt_lineaFLin;
    private javax.swing.JTextField txt_nfacturaFCab;
    private javax.swing.JTextField txt_nombreCli;
    private javax.swing.JTextField txt_precioArt;
    private javax.swing.JTextField txt_referenciaArt;
    private javax.swing.JTextField txt_stockArt;
    // End of variables declaration//GEN-END:variables
}
