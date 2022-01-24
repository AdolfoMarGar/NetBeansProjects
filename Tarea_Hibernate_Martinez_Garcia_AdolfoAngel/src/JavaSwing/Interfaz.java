/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaSwing;

import Modelos.*;
import Servicios.Metodos;
import Servicios.Util;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static java.time.LocalDate.now;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Adolfo
 */
public class Interfaz extends javax.swing.JFrame {
    private Metodos metodos = new Metodos();
    ArrayList<Object[]> listaPrestamos = null;
    ArrayList<FormaPago> listaFormaPago = null;
    ArrayList<Recibo> listaRecibos = null;
    String []atributos_prestamos = {"NºPrestamo","Fecha","Importe","Importe pagado","Forma de pago"};
    String []atributos_recibos = {"NºPrestamo","NºRecibo","Fecha","Importe","Fecha de pago"};
    String Nrecibo_aPagar, Nprestamo_aPagar;

    public Interfaz() {
        initComponents();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setTitle("Practica Tema 03_03");
        listaPrestamos = metodos.getListaPrestamos();
        listaFormaPago = metodos.getListaFormaPago();
        listaRecibos = metodos.getListaRecibos();
        crearTablaPrestamos();
        rellenarComboBox();
        
    }
    
    private void limpiarTablaRecibos(){
        Object[][]datos=null;
        jTable_Recibos.setModel(new DefaultTableModel(datos, atributos_recibos));
    }
    
    private void rellenarComboBox(){
        for(int i=0;i<listaFormaPago.size();i++){
            jComboBox_FormaPago.addItem(listaFormaPago.get(i).getDescripcion());
        }
    }
    
    private void crearTablaRecibos(Prestamo prestamo){
        int cont=0;
        for(int i=0;i<listaRecibos.size();i++){
            //convierto a string pq el if no opera con BigDecimal
            if(prestamo.getNPrestamo().toString().equals(listaRecibos.get(i).getId().getNPrestamo().toString())){
                cont++;
            }
        }
        Object[][]datos = new Object[cont][5];
        int pos_vector=0;
        for(int i=0;i<listaRecibos.size();i++){
            //convierto a string pq el if no opera con BigDecimal
            if(prestamo.getNPrestamo().toString().equals(listaRecibos.get(i).getId().getNPrestamo().toString())){
                datos[pos_vector][0]=listaRecibos.get(i).getId().getNPrestamo();
                datos[pos_vector][1]=listaRecibos.get(i).getId().getNRecibo();
                datos[pos_vector][2]=listaRecibos.get(i).getFecha();
                datos[pos_vector][3]=listaRecibos.get(i).getImporte();
                datos[pos_vector][4]=listaRecibos.get(i).getFechaPagado();
                pos_vector ++;
            }
        }
        jTable_Recibos.setModel(new DefaultTableModel(datos, atributos_recibos));
        jTable_Recibos.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable_Recibos.getColumnModel().getColumn(0).setMinWidth(0);
        jTable_Recibos.getColumnModel().getColumn(0).setPreferredWidth(0);
    }
    
    private void crearTablaPrestamos(){
        Object[][] datos =new Object [listaPrestamos.size()][5];
        for(int j=0;j<listaPrestamos.size();j++){  
            Object[] aux_prestamo = listaPrestamos.get(j);
            datos[j]=aux_prestamo;
        }
        jTable_prestamos.setModel(new DefaultTableModel(datos, atributos_prestamos));
    }
    
    private Prestamo crearObjetoPrestamo(){
        
            BigDecimal NPrestamo = new BigDecimal(txt_nPrestamo.getText());
            BigDecimal importe = new BigDecimal(txt_importe.getText());
            Date fecha = null;
            FormaPago formapago = null;
            BigDecimal ImportePagado = new BigDecimal(0);
            try {
                String dateString=txt_fecha.getText(); 
                fecha =new SimpleDateFormat("dd/MM/yyyy").parse(dateString);

            }catch (Exception ex) {
                ex.getMessage();
            }
            
            for (int i = 0; i < listaFormaPago.size(); i++) {
                if (jComboBox_FormaPago.getSelectedItem().equals(listaFormaPago.get(i).getDescripcion())) {
                    formapago = listaFormaPago.get(i);
                }
            }
            
            Prestamo pres_aux = new Prestamo(NPrestamo,formapago,fecha,importe,ImportePagado);
            return pres_aux;
    }
    
    private void rellenarTxt(int posicion){
    
        txt_nPrestamo.setText(String.valueOf(jTable_prestamos.getValueAt(posicion, 0)));
        String ano = String.valueOf(jTable_prestamos.getValueAt(posicion, 1)).substring(0,4);
        String mes = String.valueOf(jTable_prestamos.getValueAt(posicion, 1)).substring(5,7);
        String dia = String.valueOf(jTable_prestamos.getValueAt(posicion, 1)).substring(8,10);
        txt_fecha.setText(dia+"/"+mes+"/"+ano);
        txt_importe.setText(String.valueOf(jTable_prestamos.getValueAt(posicion, 2)));
    }
    
    private void borrarRecibo(Prestamo prestamo){
        for(int i=0;i<listaRecibos.size();i++){
            //convierto a string pq el if no opera con BigDecimal
            if(prestamo.getNPrestamo().toString().equals(listaRecibos.get(i).getId().getNPrestamo().toString())){
                metodos.borrarObjeto(listaRecibos.get(i));

            }
        }
    }
    
    private void boton_añadir(){
        if(filtroInfo()){
            Prestamo p_aAñadir = crearObjetoPrestamo();
            metodos.guardarPrestamo(p_aAñadir);
            listaPrestamos.clear();
            listaPrestamos= metodos.getListaPrestamos();
            crearTablaPrestamos();
            Util.mensaje_info("Operación realizada.", "Se ha añadido correctamente el prestamo.");
        }else{
            Util.mensaje_error("Error", "Rellene correctamente todos los campos o haga click en el prestamo deseado.");
        }
    }
    
    private void boton_eliminar(){
        boolean borrar=false;
        if(filtroInfo()){
            Prestamo p_aBorrar=crearObjetoPrestamo();
            for(int i=0;i<listaPrestamos.size();i++){
                if(listaPrestamos.get(i)[0].toString().equals(p_aBorrar.getNPrestamo().toString())){
                    BigDecimal importePagado = new BigDecimal(listaPrestamos.get(i)[3].toString());
                    p_aBorrar.setImportePagado(importePagado);
                    if(p_aBorrar.getImportePagado().toString().equals("0")){
                        borrar = true;
                    }
                }
            }
            if(borrar){
                borrarRecibo(p_aBorrar);
                metodos.borrarObjeto(p_aBorrar); 
                listaPrestamos.clear();
                listaPrestamos = metodos.getListaPrestamos();
                crearTablaPrestamos();
                limpiarTablaRecibos();
                Util.mensaje_info("Operación realizada.", "Se ha borrado correctamente el prestamo.");
            }else{
               Util.mensaje_error("Error", "Error al borrar: Ese prestamo no existe o ya ha sido pagado.");
            }
        }else{
            Util.mensaje_error("Error", "Rellene correctamente todos los campos o haga click en el prestamo deseado.");
        }
    }
   
    //no funciona
//    private void crear_Json(){
//        ArrayList<Recibo> listaJson= new ArrayList<>();
//        
//        for(Recibo var: listaRecibos){
//            if(var.getId().getNPrestamo().toString().equals(Nprestamo_aPagar)){
//                listaJson.add(var);
//            }
//        }
//        
//        Gson gson = new Gson();
//        String JSON = gson.toJson(listaRecibos);
//         System.out.println(JSON);     
//        
//    }
    
    private void boton_actualizar(){
        boolean actualizar=false;
        if(filtroInfo()){
            Prestamo p_aActualizar = crearObjetoPrestamo();
            for(int i=0;i<listaPrestamos.size();i++){
                if(listaPrestamos.get(i)[0].toString().equals(p_aActualizar.getNPrestamo().toString())){
                    actualizar = true;
                    BigDecimal importePagado = new BigDecimal(listaPrestamos.get(i)[3].toString());
                    p_aActualizar.setImportePagado(importePagado);

                }
            }
            if(actualizar){
                metodos.actualizarObjeto(p_aActualizar);
                listaPrestamos.clear();
                listaPrestamos = metodos.getListaPrestamos();
                crearTablaPrestamos();
                listaRecibos.clear();
                listaRecibos = metodos.getListaRecibos();
                crearTablaRecibos(p_aActualizar);
            }else{
                Util.mensaje_error("Error", "Error al actualizar: Ese prestamo no existe.");
            }
        }else{
            Util.mensaje_error("Error", "Rellene correctamente todos los campos o haga click en el prestamo deseado.");
        }
    }
    
    private void boton_pagar(){
        Recibo r_aPagar=null;
        for(Recibo var: listaRecibos){
            if(var.getId().getNPrestamo().toString().equals(Nprestamo_aPagar)){
                if(Long.toString(var.getId().getNRecibo()).equals(Nrecibo_aPagar)){
                    r_aPagar = var;
                }
            }
        }
        if(r_aPagar==null){
            Util.mensaje_error("Error", "Recibo no encontrado o seleccionado.");
        }else{
            txt_reciboNprestamo.setText(r_aPagar.getId().getNPrestamo().toString());
            txt_reciboNrecibo.setText(Long.toString(r_aPagar.getId().getNRecibo()));
            txt_reciboImporte.setText(r_aPagar.getImporte().toString());
            txt_reciboFechapago.setText(now().toString());
            iniciar_jDialog(jDialog_Recibo);
        }
    }
    
    private void boton_si(){
        Recibo r_aPagar=null;
        Prestamo p_aPagar;
        BigDecimal nPrestamo=null;
        BigDecimal importe=null;
        BigDecimal importePagado=null;
        FormaPago formapago=null;
        Date fecha =null;
        
        //obtiene el recibo a pagar y lo almacena
        for(Recibo var: listaRecibos){
            if(var.getId().getNPrestamo().toString().equals(Nprestamo_aPagar)){
                if(Long.toString(var.getId().getNRecibo()).equals(Nrecibo_aPagar)){
                    r_aPagar = var;
                }
            }
        }
        if(r_aPagar.getFechaPagado()==null){

            //crea el objeto prestamo para actualizarlo con el importe pagado
            for(Object[] var: listaPrestamos){
                if(var[0].toString().equals(Nprestamo_aPagar)){

                    nPrestamo = new BigDecimal(var[0].toString());

                    importe = new BigDecimal(var[2].toString());
                    int total;
                    total = Integer.valueOf(var[3].toString());
                    total= total + Integer.valueOf(r_aPagar.getImporte().toString());
                    importePagado = new BigDecimal(total);
                    for(FormaPago fp : listaFormaPago){
                        if(fp.getCodigo().equals(var[4].toString())){
                            formapago = fp;
                        }
                    }
                    try {
                        String ano = String.valueOf(var[1]).substring(0,4);
                        String mes = String.valueOf(var[1]).substring(5,7);
                        String dia = String.valueOf(var[1]).substring(8,10);
                        String stringDate =(dia+"/"+mes+"/"+ano);
                        fecha =new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
                    } catch (ParseException ex) {
                        Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            p_aPagar = new Prestamo(nPrestamo, formapago, fecha, importe, importePagado);

            //obtenemos la fecha del sistema
            Date fecha_Ahora=null;
            try {
                String ano = String.valueOf(txt_reciboFechapago.getText()).substring(0,4);
                String mes = String.valueOf(txt_reciboFechapago.getText()).substring(5,7);
                String dia = String.valueOf(txt_reciboFechapago.getText()).substring(8,10);
                String stringDate =(dia+"/"+mes+"/"+ano);
                fecha_Ahora =new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
            } catch (ParseException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
            r_aPagar.setFechaPagado(fecha_Ahora);


            metodos.actualizarObjeto(p_aPagar);
            metodos.actualizarObjeto(r_aPagar);
            listaPrestamos.clear();
            listaRecibos.clear();
            listaPrestamos = metodos.getListaPrestamos();
            listaRecibos = metodos.getListaRecibos();
            crearTablaPrestamos();
            crearTablaRecibos(p_aPagar);
        }else{
            Util.mensaje_aviso("Operacion cancelada", "No se puede ejecutar ya que ese recibo esta ya pagado.");
        }
        
    }
    
    private void iniciar_jDialog(JDialog ventana){
        ventana.pack();
        ventana.setModal(true);
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);  
    }
    
    private boolean filtroInfo(){
        int cont=0;
        try{
            if (!txt_nPrestamo.getText().equals("")) {
                BigDecimal NPrestamo= new BigDecimal(txt_nPrestamo.getText());
                if(NPrestamo.intValue()<=0){
                    Util.mensaje_aviso("Error NºPrestamo", "Error en el dato introducido. Valor numérico negativo o 0.");
                }else cont++;
            }else{
                Util.mensaje_aviso("Error NºPrestamo", "Error en el dato introducido. Valor numérico necesario.");
            }
        }catch(NumberFormatException e){
            Util.mensaje_aviso("Error NºPrestamo", "Error en el dato introducido. Valor numérico necesario.");
        }
        
        Date fecha=null;
        try {  
            String dateString=txt_fecha.getText();  
            int ano = Integer.parseInt(dateString.substring(6));
            int dia = Integer.parseInt(dateString.substring(0, 2));
            int mes = Integer.parseInt(dateString.substring(3, 5));

            int filtrofecha=0;
            if(!dateString.equals("")&&dateString.length()==10){
                if(2100>ano && ano>1900){
                    filtrofecha++;
                    if(32>dia && dia>0){
                        filtrofecha++;
                        if(13>mes && mes>0){
                            filtrofecha++;
                            fecha =new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
                            cont++;
                        }
                    }
                }
                if (filtrofecha!=3)
                    Util.mensaje_aviso("Error fecha", "Formato para la fecha 'dd/MM/yyyy'. Ejemplo: '07/01/2000'");
            }else{
                Util.mensaje_aviso("Error fecha", "Formato para la fecha 'dd/MM/yyyy'. Ejemplo: '07/01/2000'");
            }
        } catch (Exception ex) {
            Util.mensaje_aviso("Error fecha", "Formato para la fecha 'dd/MM/yyyy'. Años entre 1900 y 2100. Ejemplo: '07/01/2000'");
        }

        try{
            if (!txt_importe.equals("")) {
                
                BigDecimal importe= new BigDecimal(txt_importe.getText());
                if(importe.intValue()<=0){
                    Util.mensaje_aviso("Error Importe", "Error en el dato introducido. Valor numérico negativo o 0");
                }else cont++;
                
            }else{
                Util.mensaje_aviso("Error Importe", "Error en el dato introducido. Valor numérico necesario");
            } 
        }catch(NumberFormatException e){
            Util.mensaje_aviso("Error Importe", "Error en el dato introducido. Valor numérico necesario.");
        }
        if(cont==3)return true;
        else return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog_Recibo = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_reciboNprestamo = new javax.swing.JTextField();
        txt_reciboNrecibo = new javax.swing.JTextField();
        txt_reciboFechapago = new javax.swing.JTextField();
        txt_reciboImporte = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btn_si = new javax.swing.JButton();
        btn_no = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btn_anadir = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        brn_actualizar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_nPrestamo = new javax.swing.JTextField();
        txt_fecha = new javax.swing.JTextField();
        txt_importe = new javax.swing.JTextField();
        btn_pagar = new javax.swing.JButton();
        jComboBox_FormaPago = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_prestamos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_Recibos = new javax.swing.JTable();

        jDialog_Recibo.setAlwaysOnTop(true);

        jLabel5.setText("Nº Prestamo: ");

        jLabel6.setText("Nº Recibo");

        jLabel7.setText("Fecha de Pago:");

        jLabel8.setText("Importe:");

        txt_reciboNprestamo.setEditable(false);

        txt_reciboNrecibo.setEditable(false);

        txt_reciboFechapago.setEditable(false);

        txt_reciboImporte.setEditable(false);

        jLabel9.setText("¿Confirmar pago?");

        btn_si.setText("Si");
        btn_si.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_siActionPerformed(evt);
            }
        });

        btn_no.setText("No");
        btn_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_noActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_reciboNrecibo)
                            .addComponent(txt_reciboNprestamo)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_reciboFechapago)
                            .addComponent(txt_reciboImporte)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(btn_si)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 172, Short.MAX_VALUE)
                        .addComponent(btn_no)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_reciboNprestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_reciboNrecibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_reciboFechapago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_reciboImporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(btn_si)
                    .addComponent(btn_no))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog_ReciboLayout = new javax.swing.GroupLayout(jDialog_Recibo.getContentPane());
        jDialog_Recibo.getContentPane().setLayout(jDialog_ReciboLayout);
        jDialog_ReciboLayout.setHorizontalGroup(
            jDialog_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_ReciboLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDialog_ReciboLayout.setVerticalGroup(
            jDialog_ReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_ReciboLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn_anadir.setText("Añadir");
        btn_anadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_anadirActionPerformed(evt);
            }
        });

        btn_eliminar.setText("Eliminar");
        btn_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarActionPerformed(evt);
            }
        });

        brn_actualizar.setText("Actualizar");
        brn_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brn_actualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_anadir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_eliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(brn_actualizar)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_anadir)
                    .addComponent(btn_eliminar)
                    .addComponent(brn_actualizar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Nº Prestamo:");

        jLabel2.setText("Fecha:");

        jLabel3.setText("Importe:");

        jLabel4.setText("Forma de pago:");

        btn_pagar.setText("PAGAR");
        btn_pagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pagarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_pagar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_nPrestamo)
                            .addComponent(txt_fecha)
                            .addComponent(txt_importe, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox_FormaPago, 0, 190, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_nPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox_FormaPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btn_pagar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable_prestamos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Nº Prestamo", "Fecha", "Importe", "Importe pagado", "Forma de pago"
            }
        ));
        jTable_prestamos.setCellSelectionEnabled(true);
        jTable_prestamos.setEnabled(false);
        jTable_prestamos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_prestamosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_prestamos);

        jTable_Recibos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "NºPrestamo", "Nº Recibo", "Fecha", "Importe", "Fecha de pago"
            }
        ));
        jTable_Recibos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable_Recibos.setEnabled(false);
        jTable_Recibos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_RecibosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable_Recibos);
        if (jTable_Recibos.getColumnModel().getColumnCount() > 0) {
            jTable_Recibos.getColumnModel().getColumn(0).setMinWidth(0);
            jTable_Recibos.getColumnModel().getColumn(0).setPreferredWidth(0);
            jTable_Recibos.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_pagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pagarActionPerformed
        boton_pagar();

    }//GEN-LAST:event_btn_pagarActionPerformed

    private void btn_anadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_anadirActionPerformed
        boton_añadir();
    }//GEN-LAST:event_btn_anadirActionPerformed

    private void jTable_prestamosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_prestamosMouseClicked
        int posicion = jTable_prestamos.rowAtPoint(evt.getPoint());
        rellenarTxt(posicion);
        crearTablaRecibos(crearObjetoPrestamo());
    }//GEN-LAST:event_jTable_prestamosMouseClicked

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        boton_eliminar();
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void brn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brn_actualizarActionPerformed
        boton_actualizar();
    }//GEN-LAST:event_brn_actualizarActionPerformed

    private void jTable_RecibosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_RecibosMouseClicked
        int posicion = jTable_Recibos.rowAtPoint(evt.getPoint());
        Nprestamo_aPagar = String.valueOf(jTable_Recibos.getValueAt(posicion, 0));
        Nrecibo_aPagar = String.valueOf(jTable_Recibos.getValueAt(posicion, 1));
    }//GEN-LAST:event_jTable_RecibosMouseClicked

    private void btn_siActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_siActionPerformed
        jDialog_Recibo.dispose();
        boton_si();
    }//GEN-LAST:event_btn_siActionPerformed

    private void btn_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_noActionPerformed
        jDialog_Recibo.dispose();
    }//GEN-LAST:event_btn_noActionPerformed

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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton brn_actualizar;
    private javax.swing.JButton btn_anadir;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_no;
    private javax.swing.JButton btn_pagar;
    private javax.swing.JButton btn_si;
    private javax.swing.JComboBox<String> jComboBox_FormaPago;
    private javax.swing.JDialog jDialog_Recibo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable_Recibos;
    private javax.swing.JTable jTable_prestamos;
    private javax.swing.JTextField txt_fecha;
    private javax.swing.JTextField txt_importe;
    private javax.swing.JTextField txt_nPrestamo;
    private javax.swing.JTextField txt_reciboFechapago;
    private javax.swing.JTextField txt_reciboImporte;
    private javax.swing.JTextField txt_reciboNprestamo;
    private javax.swing.JTextField txt_reciboNrecibo;
    // End of variables declaration//GEN-END:variables
}
