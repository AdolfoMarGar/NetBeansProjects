/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2_01;

import static java.awt.Frame.MAXIMIZED_BOTH;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Adolfo
 */
public class Interfaz extends javax.swing.JFrame {
    Connection  conexion = null;
    DatabaseMetaData dbmd = null;
    ArrayList<String> lista_tablas = new ArrayList<>();
    ArrayList<String> lista_columnas = new ArrayList<>();
    String[] tipo = {"TABLE"};
    String tipo_dato;

    public Interfaz() {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(ClassNotFoundException ex){
            JOptionPane.showMessageDialog(null,"JDBC no encontrado. Se va a cerrar la aplicación.","Error",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        initComponents();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setTitle("Practica Tema 02_01");
        iniciar_jDialog(ventana_conex);
    }
    
    private void iniciar_jDialog(JDialog ventana){
        ventana.pack();
        ventana.setModal(true);
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);  
    }
    
    public void error_conex(){
        ventana_conex.dispose();
        JOptionPane.showMessageDialog(null,"Conexión fallida. Se va a cerrar la aplicación.","Error",JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
    
    public void conectar_bd(){
        try {
            conexion = DriverManager.getConnection(
                "jdbc:oracle:thin:@"+txt_servidor.getText()+":"
                +txt_puerto.getText()+":xe"
                ,txt_usuario.getText()
                ,txt_contr.getText()
            );
            
            dbmd = conexion.getMetaData();
            
        } catch (SQLException ex) {
            error_conex();
        }
    }
    
    public void obtener_info_tablas(){
        ResultSet rs;  
        try {
            rs = dbmd.getTables("AD_TEMA_03_FACTURAS", "AD_TEMA_03_FACTURAS", "%", tipo);
            while(rs.next()) { 
                lista_tablas.add(rs.getString("TABLE_NAME"));
            }     
            
            for(int i=0;i<lista_tablas.size();i++){
                jComboBox_Tablas.addItem(lista_tablas.get(i));
            }   
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error: "+ex,"Error",JOptionPane.ERROR_MESSAGE);        
        }
    }
    
    public void actualizar_columnas(){
        jComboBox_Columnas.removeAllItems();

        for(int i=0;i<lista_columnas.size();i++){
                jComboBox_Columnas.addItem(lista_columnas.get(i));

            }
    }
    

    
    public void obtener_info_columnas(){
        
        lista_columnas.clear();
        ResultSet rs; 
        String consulta;
        try {
            consulta = ("select * from "+jComboBox_Tablas.getSelectedItem().toString());
            Statement stmt = conexion.createStatement();
            rs = stmt.executeQuery(consulta);
            ResultSetMetaData rsmd = rs.getMetaData();
            int pos_tabla=rsmd.getColumnCount();
            for(int i=0;i<pos_tabla;i++){
                lista_columnas.add(rsmd.getColumnName(i+1));
            }
            tipo_dato=rsmd.getColumnTypeName(1);
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error: "+ex,"Error",JOptionPane.ERROR_MESSAGE);        
        }
    }

    public void crear_cabeceraTabla(){
        String []aux = new String [lista_columnas.size()];
        for(int i=0;i<lista_columnas.size();i++){
                aux[i]=lista_columnas.get(i);
            }
        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},aux));
    }
    
    public void obtener_tipo_dato(String tab){
        ResultSet rs; 
        String consulta;
        try {
            consulta = ("select "+tab+" from "+jComboBox_Tablas.getSelectedItem().toString());
            Statement stmt = conexion.createStatement();
            rs = stmt.executeQuery(consulta);
            ResultSetMetaData rsmd = rs.getMetaData();
            tipo_dato=rsmd.getColumnTypeName(1);

            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error: "+ex,"Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void actualizar_Operadores(){
        jComboBox_Operador.removeAllItems();

        switch(tipo_dato){
            case "VARCHAR2" :
                jComboBox_Operador.addItem("LIKE");
                jComboBox_Operador.addItem("=");

               break;

            case "NUMBER" :
                jComboBox_Operador.addItem("<");
                jComboBox_Operador.addItem(">");
                jComboBox_Operador.addItem("=");
                jComboBox_Operador.addItem("<=");
                jComboBox_Operador.addItem(">=");

               break;

            case "DATE" :
                jComboBox_Operador.addItem("<");
                jComboBox_Operador.addItem(">");
                jComboBox_Operador.addItem("=");
                jComboBox_Operador.addItem("<=");
                jComboBox_Operador.addItem(">=");               
                
                break; 
        }
    }
    
    private void crearTabla(){  
        int contador=0;
        int registros=-1;
        ResultSet rs; 
        String consulta;
        try {
            consulta = ("select count(*) from "+jComboBox_Tablas.getSelectedItem().toString());
            Statement stmt = conexion.createStatement();
            rs = stmt.executeQuery(consulta);
            while(rs.next()==true){
                registros = rs.getInt("count(*)"); 
            }
            consulta = txt_ejecutar.getText();
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(consulta);
            if(registros!=-1){
                Object[][] datos =new Object [registros][lista_columnas.size()];
                    while(rs.next()) {
                        for(int j=0;j<lista_columnas.size();j++){
                            datos[contador][j]=rs.getString(j+1);
                        }
                        contador++;
                    }
                String []aux = new String [lista_columnas.size()];
                for(int i=0;i<lista_columnas.size();i++){
                    aux[i]=lista_columnas.get(i);
                }
                tabla.setModel(new DefaultTableModel(datos, aux));
            }
            
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error: "+ex,"Error",JOptionPane.ERROR_MESSAGE);
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

        ventana_conex = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_contr = new javax.swing.JTextField();
        txt_usuario = new javax.swing.JTextField();
        txt_puerto = new javax.swing.JTextField();
        txt_servidor = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btn_Aceptar = new javax.swing.JButton();
        btn_Cancelar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox_Tablas = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jComboBox_Columnas = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jComboBox_Operador = new javax.swing.JComboBox<>();
        txt_valor = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btn_Ejecutar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txt_ejecutar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        ventana_conex.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ventana_conex.setTitle("Propiedades.");
        ventana_conex.setAlwaysOnTop(true);
        ventana_conex.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                ventana_conexWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Servidor: ");

        jLabel2.setText("Puerto: ");

        jLabel3.setText("Usuario: ");

        jLabel4.setText("Contraseña: ");

        txt_contr.setText("AD_TEMA_03_FACTURAS");

        txt_usuario.setText("AD_TEMA_03_FACTURAS");

        txt_puerto.setText("1521");

        txt_servidor.setText("localhost");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_servidor)
                    .addComponent(txt_puerto)
                    .addComponent(txt_usuario)
                    .addComponent(txt_contr))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_servidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_puerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_contr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_Aceptar.setText("Aceptar");
        btn_Aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AceptarActionPerformed(evt);
            }
        });

        btn_Cancelar.setText("Cancelar");
        btn_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Aceptar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 214, Short.MAX_VALUE)
                .addComponent(btn_Cancelar)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Aceptar)
                    .addComponent(btn_Cancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ventana_conexLayout = new javax.swing.GroupLayout(ventana_conex.getContentPane());
        ventana_conex.getContentPane().setLayout(ventana_conexLayout);
        ventana_conexLayout.setHorizontalGroup(
            ventana_conexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventana_conexLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ventana_conexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        ventana_conexLayout.setVerticalGroup(
            ventana_conexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventana_conexLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel5.setText("Tablas:");

        jComboBox_Tablas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_TablasActionPerformed(evt);
            }
        });

        jLabel6.setText("Columnas");

        jComboBox_Columnas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jComboBox_Columnas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_ColumnasActionPerformed(evt);
            }
        });

        jLabel7.setText("Operador");

        txt_valor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_valorActionPerformed(evt);
            }
        });

        jLabel8.setText("Valor");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox_Tablas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel6)
                    .addComponent(jComboBox_Columnas, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox_Operador, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(txt_valor, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox_Tablas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_Columnas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox_Operador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setEnabled(false);

        btn_Ejecutar.setText("Ejecutar");
        btn_Ejecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EjecutarActionPerformed(evt);
            }
        });

        jLabel9.setText("Sentencia:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(txt_ejecutar)
                .addGap(18, 18, 18)
                .addComponent(btn_Ejecutar)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Ejecutar)
                    .addComponent(jLabel9)
                    .addComponent(txt_ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla.setEnabled(false);
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_AceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AceptarActionPerformed
        conectar_bd();
        obtener_info_tablas();
        crear_cabeceraTabla();
        actualizar_columnas();
        actualizar_Operadores();
        String con = "select * from " + jComboBox_Tablas.getSelectedItem().toString();
        crearTabla();
        ventana_conex.dispose();
    }//GEN-LAST:event_btn_AceptarActionPerformed

    private void btn_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelarActionPerformed
        // TODO add your handling code here:
        error_conex();
        ventana_conex.dispose();
    }//GEN-LAST:event_btn_CancelarActionPerformed

    private void ventana_conexWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_ventana_conexWindowClosing
        error_conex();
    }//GEN-LAST:event_ventana_conexWindowClosing

    private void jComboBox_TablasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_TablasActionPerformed
        obtener_info_columnas();
        crear_cabeceraTabla();
        actualizar_columnas();
        String con = "select * from " + jComboBox_Tablas.getSelectedItem().toString();
        txt_ejecutar.setText(con);
        crearTabla();
                
    }//GEN-LAST:event_jComboBox_TablasActionPerformed

    private void jComboBox_ColumnasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_ColumnasActionPerformed
        // el if esta ahi para que no se active el listener cada vez que se ejecute  jComboBox_Columnas.addItem("lo que sea");
        // asi solo se ejecuta cuando esten todos los items añadidos ya que tienen que ser la misma cantida que los almacenados en el arraylist
        if(jComboBox_Columnas.getItemCount()==lista_columnas.size()){
            obtener_tipo_dato(jComboBox_Columnas.getSelectedItem().toString());
            actualizar_Operadores();
        }
    }//GEN-LAST:event_jComboBox_ColumnasActionPerformed

    private void btn_EjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EjecutarActionPerformed
        //actualizar_Operadores();
        crearTabla();
    }//GEN-LAST:event_btn_EjecutarActionPerformed

    private void txt_valorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_valorActionPerformed
        // TODO add your handling code here:
        String txt = "select * from " + jComboBox_Tablas.getSelectedItem().toString();
        txt = txt + " where "+jComboBox_Columnas.getSelectedItem().toString();
        txt = txt + " " + jComboBox_Operador.getSelectedItem().toString();
        txt = txt + " '" + txt_valor.getText()+"'";
        txt_ejecutar.setText(txt);
    }//GEN-LAST:event_txt_valorActionPerformed

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
    private javax.swing.JButton btn_Aceptar;
    private javax.swing.JButton btn_Cancelar;
    private javax.swing.JButton btn_Ejecutar;
    private javax.swing.JComboBox<String> jComboBox_Columnas;
    private javax.swing.JComboBox<String> jComboBox_Operador;
    private javax.swing.JComboBox<String> jComboBox_Tablas;
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
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txt_contr;
    private javax.swing.JTextField txt_ejecutar;
    private javax.swing.JTextField txt_puerto;
    private javax.swing.JTextField txt_servidor;
    private javax.swing.JTextField txt_usuario;
    private javax.swing.JTextField txt_valor;
    private javax.swing.JDialog ventana_conex;
    // End of variables declaration//GEN-END:variables
}
