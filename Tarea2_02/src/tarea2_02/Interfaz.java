/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2_02;

import static java.awt.Frame.MAXIMIZED_BOTH;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Adolfo
 */
public class Interfaz extends javax.swing.JFrame {
    boolean modo=false;
    Connection  conexion = null;
    DatabaseMetaData dbmd = null;
    ArrayList<String> lista_tablas = new ArrayList<>();
    ArrayList<String> lista_columnas = new ArrayList<>();
    String[] tipo = {"TABLE"};
    String tipo_dato;
    /**
     * Creates new form Interfaz
     */
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
        iniciar_jDialog(ventana_select);
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
        
    public void obtener_info_tablas(){
        ResultSet rs;  
        try {
            rs = dbmd.getTables(txt_usuario.getText(), txt_usuario.getText(), "%", tipo);
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
    
    public void actualizar_columnas(){
        jComboBox_Columnas.removeAllItems();

        for(int i=0;i<lista_columnas.size();i++){
                jComboBox_Columnas.addItem(lista_columnas.get(i));

            }
    }
    

        
    public void fichar(String dni){
        ResultSet rs; 
        String consulta;
        try {
            consulta = ( 
                "begin \n"+
                "fichar_dni('"+dni+"'); \n"+    
                "commit;\n"+
                "end;");
            Statement stmt = conexion.createStatement();
            rs = stmt.executeQuery(consulta);
            stmt.close();
            rs.close();
            JOptionPane.showMessageDialog(null,"Has fichado correctamente");   
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,ex+"DNI no encontrado. Recuerda que no es lo mismo mayuscula que minuscula.","Error",JOptionPane.ERROR_MESSAGE);        
        }
    }
    
    public void fichar_cerrar(String dni){
        ResultSet rs; 
        String consulta;
        try {
            consulta = ( 
                "begin \n"+
                "cerrar_fichaje('"+dni+"'); \n"+    
                "commit;\n"+
                "end;");
            Statement stmt = conexion.createStatement();
            rs = stmt.executeQuery(consulta);
            stmt.close();
            rs.close();
            JOptionPane.showMessageDialog(null,"Has cerrado la ficha correctamente");   
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,ex+"DNI no encontrado. Recuerda que no es lo mismo mayuscula que minuscula.","Error",JOptionPane.ERROR_MESSAGE);        
        }
    }
 
    public void nomina(String fecha){
        ResultSet rs; 
        String consulta;
        try {
            consulta = ( 
                "begin \n"+
                "nomina('"+fecha+"'); \n"+    
                "commit;\n"+
                "end;");
            Statement stmt = conexion.createStatement();
            rs = stmt.executeQuery(consulta);
            stmt.close();
            rs.close();
            JOptionPane.showMessageDialog(null,"Has cerrado la ficha correctamente");   
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,ex+"Error. Introduce la fecha en el siguiente formato: 'dd/mm/yyyy'.","Error",JOptionPane.ERROR_MESSAGE);        
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
    
    public void crearProcedimientos(){
        ResultSet rs; 
        String consulta;
        try {
            Statement stmt = conexion.createStatement();
            
            consulta = ("alter session set nls_date_format = 'dd/mm/yyyy hh24:mi:ss'");
            rs = stmt.executeQuery(consulta);
            System.out.println("Procedimiento 1 creado");

            consulta = (
                "create or replace procedure fichar_dni(numero in VARCHAR2)as"+"\n"+
                "cont int;"+"\n"+    
                "f_ini date;"+"\n"+
                "f_fin date;"+"\n"+
                "begin"+"\n"+
                "select count(*) into cont  from empleados where dni=numero;"+"\n"+
                "if cont = 1 then"+"\n"+
                "select count(*) into cont  from fichajes where dni=numero;"+"\n"+
                "if cont = 1 then"+"\n"+
                "UPDATE fichajes"+"\n"+
                "SET fechorafin = sysdate"+"\n"+
                "WHERE dni = numero;"+"\n"+
                "else"+"\n"+
                "select count(*) into cont  from fichajes;"+"\n"+
                "insert into fichajes"+"\n"+
                "values(cont,numero, sysdate, null);"+"\n"+
                "end if;"+"\n"+
                "end if;"+"\n"+
                "end;");
            rs = stmt.executeQuery(consulta);
            System.out.println("Procedimiento 2 creado");
            
            consulta = (
                "create or replace procedure cerrar_fichaje(numero in varchar2) as"+"\n"+
                "fec_fin date;"+"\n"+    
                "begin"+"\n"+
                "select fechorafin into fec_fin from fichajes where dni=numero;"+"\n"+
                "if fec_fin is null then"+"\n"+
                "UPDATE fichajes"+"\n"+
                "SET fechorafin = sysdate"+"\n"+
                "WHERE dni = numero;"+"\n"+
                "end if;"+"\n"+
                "delete from fichajes where dni =numero;"+"\n"+
                "end;");
            rs = stmt.executeQuery(consulta);
            System.out.println("Procedimiento 3 creado");
            
            consulta = (
                "create or replace trigger actualizar_horas"+"\n"+
                "before delete"+"\n"+    
                "on AD_TEMA02_FICHAJES.fichajes"+"\n"+
                "for each row"+"\n"+
                "declare"+"\n"+
                "hora_ini date;"+"\n"+
                "hora_fin date;"+"\n"+
                "dif_horas number;"+"\n"+
                "horas_min number;"+"\n"+
                "cont number;"+"\n"+
                "begin"+"\n"+
                "hora_ini := :old.fechoraini;"+"\n"+
                "hora_fin := :old.fechorafin;"+"\n"+
                "select horasmin into horas_min from empleados where dni = :old.dni;"+"\n"+
                "select 24 * (hora_fin - hora_ini) diff_hours into dif_horas from dual;"+"\n"+
                "select count(*) into cont from festivos  where trunc(fecha) = trunc(hora_ini);"+"\n"+
                "if cont =0 then"+"\n"+
                "if horas_min<dif_horas then"+"\n"+
                "dif_horas:= dif_horas-horas_min;"+"\n"+
                "insert into horas"+"\n"+
                "values(sysdate, :old.dni, dif_horas ,horas_min);"+"\n"+
                "else"+"\n"+
                "insert into horas"+"\n"+
                "values(sysdate, :old.dni, dif_horas ,null);"+"\n"+
                "end if;"+"\n"+
                "else"+"\n"+
                "insert into horas"+"\n"+
                "values(sysdate, :old.dni, null ,dif_horas);"+"\n"+
                "end if;"+"\n"+
                "end;");
            rs = stmt.executeQuery(consulta);
            System.out.println("Procedimiento 4 creado");
            
            consulta = (
                "create or replace procedure nomina(var1 in varchar2)as "+"\n"+
                "fec date; h number; he number; s number; se number; an number; me number; cont number; lim number; existe number; dni_var varchar2(10);"+"\n"+    
                "begin"+"\n"+
                "fec:= TO_DATE(var1,'dd/mm/yyyy');"+"\n"+
                "SELECT EXTRACT(MONTH FROM fec)into me FROM dual;"+"\n"+
                "SELECT EXTRACT(YEAR FROM fec)into an FROM dual;"+"\n"+
                "select count(*) into lim from horas where trunc(fecha)=trunc(fec);"+"\n"+
                "cont:=0;"+"\n"+
                "while cont < lim loop"+"\n"+
                "select dni into dni_var from horas where rownum=cont;"+"\n"+
                "select count(*)into existe from nominas where(dni=dni_var and anio=an and mes=me);"+"\n"+
                "if existe = 1 then"+"\n"+
                "select horas.horas into h from horas where (trunc(fecha)=trunc(fec) and rownum=cont);"+"\n"+
                "select horas.horase into he from horas where (trunc(fecha)=trunc(fec) and rownum=cont);"+"\n"+
                "s:= 7*h;"+"\n"+
                "se:=10*he;"+"\n"+
                "UPDATE nominas"+"\n"+
                "SET sueldoh = s,"+"\n"+
                "sueldohe=se"+"\n"+
                "WHERE (anio=an and mes = me and dni=dni_var);"+"\n"+
                "else"+"\n"+
                "select horas.horas into h from horas where (trunc(fecha)=trunc(fec) and rownum=cont);"+"\n"+
                "select horas.horase into he from horas where (trunc(fecha)=trunc(fec) and rownum=cont);"+"\n"+
                "s:= 7*h;"+"\n"+
                "se:=10*he;"+"\n"+
                "insert into nominas"+"\n"+
                "values(an,me,dni_var,s,se);"+"\n"+
                "end if;"+"\n"+
                "cont:= cont+1;"+"\n"+
                "end loop;"+"\n"+
                "end;");
            rs = stmt.executeQuery(consulta);
            System.out.println("Procedimiento 5 creado");
            
            
            
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"error, "+ex,"Error",JOptionPane.ERROR_MESSAGE);        
        }
    }
    
    public void cambiarModo(){
        jPanel_Nominas.setEnabled(modo);
        txt_nomina.setEnabled(modo);
        btn_nomina.setEnabled(modo);
       
        jPanel_Consultas.setEnabled(modo);
        jComboBox_Columnas.setEnabled(modo);
        jComboBox_Operador.setEnabled(modo);
        jComboBox_Tablas.setEnabled(modo);
        btn_Ejecutar.setEnabled(modo);
        txt_valor.setEnabled(modo);
        txt_ejecutar.setEnabled(modo);

    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ventana_select = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        btn_manager = new javax.swing.JButton();
        btn_empleado = new javax.swing.JButton();
        ventana_conex = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_contr = new javax.swing.JTextField();
        txt_usuario = new javax.swing.JTextField();
        txt_puerto = new javax.swing.JTextField();
        txt_servidor = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btn_Aceptar = new javax.swing.JButton();
        btn_Cancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel_Fichar = new javax.swing.JPanel();
        btn_fichar_ent = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txt_dni = new javax.swing.JTextField();
        btn_cerrar_fich = new javax.swing.JButton();
        jPanel_Nominas = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btn_nomina = new javax.swing.JButton();
        txt_nomina = new javax.swing.JTextField();
        jPanel_Consultas = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox_Tablas = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jComboBox_Columnas = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jComboBox_Operador = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txt_valor = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_ejecutar = new javax.swing.JTextField();
        btn_Ejecutar = new javax.swing.JButton();
        btn_modo = new javax.swing.JButton();
        btn_salir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        ventana_select.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                ventana_selectWindowClosing(evt);
            }
        });

        jLabel3.setText("Selecciona \"MANAGER\" o \"EMPLEADO\" según lo que seas.");

        btn_manager.setText("MANAGER");
        btn_manager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_managerActionPerformed(evt);
            }
        });

        btn_empleado.setText("EMPLEADO");
        btn_empleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_empleadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ventana_selectLayout = new javax.swing.GroupLayout(ventana_select.getContentPane());
        ventana_select.getContentPane().setLayout(ventana_selectLayout);
        ventana_selectLayout.setHorizontalGroup(
            ventana_selectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventana_selectLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ventana_selectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addGroup(ventana_selectLayout.createSequentialGroup()
                        .addComponent(btn_manager)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_empleado)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ventana_selectLayout.setVerticalGroup(
            ventana_selectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventana_selectLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ventana_selectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_manager)
                    .addComponent(btn_empleado))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ventana_conex.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ventana_conex.setTitle("Propiedades.");
        ventana_conex.setAlwaysOnTop(true);
        ventana_conex.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                ventana_conexWindowClosing(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Servidor: ");

        jLabel10.setText("Puerto: ");

        jLabel11.setText("Usuario: ");

        jLabel12.setText("Contraseña: ");

        txt_contr.setText("AD_TEMA02_FICHAJES");

        txt_usuario.setText("AD_TEMA02_FICHAJES");

        txt_puerto.setText("1521");

        txt_servidor.setText("localhost");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_servidor)
                    .addComponent(txt_puerto)
                    .addComponent(txt_usuario)
                    .addComponent(txt_contr))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_servidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txt_puerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txt_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Aceptar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 214, Short.MAX_VALUE)
                .addComponent(btn_Cancelar)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        ventana_conexLayout.setVerticalGroup(
            ventana_conexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventana_conexLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel_Fichar.setBorder(javax.swing.BorderFactory.createTitledBorder("FICHAR"));

        btn_fichar_ent.setText("FICHAR");
        btn_fichar_ent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_fichar_entActionPerformed(evt);
            }
        });

        jLabel4.setText("DNI");

        btn_cerrar_fich.setText("CERRAR FICHA");
        btn_cerrar_fich.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cerrar_fichActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_FicharLayout = new javax.swing.GroupLayout(jPanel_Fichar);
        jPanel_Fichar.setLayout(jPanel_FicharLayout);
        jPanel_FicharLayout.setHorizontalGroup(
            jPanel_FicharLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_FicharLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(txt_dni)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_fichar_ent)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_cerrar_fich)
                .addContainerGap())
        );
        jPanel_FicharLayout.setVerticalGroup(
            jPanel_FicharLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_FicharLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_FicharLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_fichar_ent)
                    .addComponent(jLabel4)
                    .addComponent(txt_dni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cerrar_fich))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel_Nominas.setBorder(javax.swing.BorderFactory.createTitledBorder("NOMINAS"));

        jLabel2.setText("FECHA:");

        btn_nomina.setText("NOMINA");
        btn_nomina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nominaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_NominasLayout = new javax.swing.GroupLayout(jPanel_Nominas);
        jPanel_Nominas.setLayout(jPanel_NominasLayout);
        jPanel_NominasLayout.setHorizontalGroup(
            jPanel_NominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_NominasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_nomina)
                .addGap(18, 18, 18)
                .addComponent(btn_nomina)
                .addContainerGap())
        );
        jPanel_NominasLayout.setVerticalGroup(
            jPanel_NominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_NominasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_NominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(btn_nomina)
                    .addComponent(txt_nomina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel_Consultas.setBorder(javax.swing.BorderFactory.createTitledBorder("CONSULTAS"));

        jLabel5.setText("Tablas:");

        jComboBox_Tablas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_TablasActionPerformed(evt);
            }
        });

        jLabel6.setText("Columnas:");

        jComboBox_Columnas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jComboBox_Columnas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_ColumnasActionPerformed(evt);
            }
        });

        jLabel7.setText("Operador:");

        jLabel8.setText("Valor:");

        txt_valor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_valorActionPerformed(evt);
            }
        });

        jLabel9.setText("Sentencia:");

        btn_Ejecutar.setText("Ejecutar");

        javax.swing.GroupLayout jPanel_ConsultasLayout = new javax.swing.GroupLayout(jPanel_Consultas);
        jPanel_Consultas.setLayout(jPanel_ConsultasLayout);
        jPanel_ConsultasLayout.setHorizontalGroup(
            jPanel_ConsultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ConsultasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_ConsultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_ConsultasLayout.createSequentialGroup()
                        .addGroup(jPanel_ConsultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_ConsultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox_Tablas, 0, 214, Short.MAX_VALUE)
                            .addComponent(jComboBox_Columnas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel_ConsultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel_ConsultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox_Operador, 0, 214, Short.MAX_VALUE)
                            .addComponent(txt_valor)))
                    .addGroup(jPanel_ConsultasLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_ejecutar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Ejecutar)
                .addContainerGap())
        );
        jPanel_ConsultasLayout.setVerticalGroup(
            jPanel_ConsultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ConsultasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_ConsultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox_Tablas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox_Operador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel_ConsultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox_Columnas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txt_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel_ConsultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txt_ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Ejecutar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_modo.setText("ELEGIR MODO");
        btn_modo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modoActionPerformed(evt);
            }
        });

        btn_salir.setText("SALIR");
        btn_salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salirActionPerformed(evt);
            }
        });

        tabla.setEnabled(false);
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_Fichar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_Nominas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_Consultas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_modo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_salir))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel_Fichar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_Nominas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel_Consultas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_salir)
                    .addComponent(btn_modo))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Funciona
    private void btn_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirActionPerformed
        JOptionPane.showMessageDialog(null,"Se va a cerrar la aplicación.");
        System.exit(0);
     
    }//GEN-LAST:event_btn_salirActionPerformed

    //funcion
    private void btn_modoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modoActionPerformed
        iniciar_jDialog(ventana_select);
    }//GEN-LAST:event_btn_modoActionPerformed

    //funciona
    private void btn_managerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_managerActionPerformed
        modo=true;
        cambiarModo();
        ventana_select.dispose();
    }//GEN-LAST:event_btn_managerActionPerformed

    //funciona
    private void btn_empleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_empleadoActionPerformed
        modo=false;
        cambiarModo();
        ventana_select.dispose();
    }//GEN-LAST:event_btn_empleadoActionPerformed

    
    
    private void btn_AceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AceptarActionPerformed
        conectar_bd();
        crearProcedimientos();
        obtener_info_tablas();
        obtener_info_columnas();
        actualizar_columnas();
        actualizar_Operadores();
        crearTabla();
        ventana_conex.dispose();
    }//GEN-LAST:event_btn_AceptarActionPerformed

    //funciona
    private void btn_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelarActionPerformed
        error_conex();
        ventana_conex.dispose();
    }//GEN-LAST:event_btn_CancelarActionPerformed

    //funciona
    private void ventana_conexWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_ventana_conexWindowClosing
        error_conex();
    }//GEN-LAST:event_ventana_conexWindowClosing

    //Funciona
    private void ventana_selectWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_ventana_selectWindowClosing
        cambiarModo();
    }//GEN-LAST:event_ventana_selectWindowClosing

    private void btn_fichar_entActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_fichar_entActionPerformed
        fichar(txt_dni.getText());
        crearTabla();
    }//GEN-LAST:event_btn_fichar_entActionPerformed

    private void jComboBox_TablasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_TablasActionPerformed
        obtener_info_columnas();
        actualizar_columnas();
        String con = "select * from " + jComboBox_Tablas.getSelectedItem().toString();
        txt_ejecutar.setText(con);
        crearTabla();    
    }//GEN-LAST:event_jComboBox_TablasActionPerformed

    private void jComboBox_ColumnasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_ColumnasActionPerformed
        if(jComboBox_Columnas.getItemCount()==lista_columnas.size()){
            obtener_tipo_dato(jComboBox_Columnas.getSelectedItem().toString());
            actualizar_Operadores();
        }       
    }//GEN-LAST:event_jComboBox_ColumnasActionPerformed

    private void btn_cerrar_fichActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cerrar_fichActionPerformed
        fichar_cerrar(txt_dni.getText());
        crearTabla();
    }//GEN-LAST:event_btn_cerrar_fichActionPerformed

    private void txt_valorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_valorActionPerformed
        String txt = "select * from " + jComboBox_Tablas.getSelectedItem().toString();
        txt = txt + " where "+jComboBox_Columnas.getSelectedItem().toString();
        txt = txt + " " + jComboBox_Operador.getSelectedItem().toString();
        txt = txt + " '" + txt_valor.getText()+"'";
        txt_ejecutar.setText(txt);
    }//GEN-LAST:event_txt_valorActionPerformed

    private void btn_nominaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nominaActionPerformed
        nomina(txt_nomina.getText());
        crearTabla();
    }//GEN-LAST:event_btn_nominaActionPerformed


    
    
    
    
    
    
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
    private javax.swing.JButton btn_cerrar_fich;
    private javax.swing.JButton btn_empleado;
    private javax.swing.JButton btn_fichar_ent;
    private javax.swing.JButton btn_manager;
    private javax.swing.JButton btn_modo;
    private javax.swing.JButton btn_nomina;
    private javax.swing.JButton btn_salir;
    private javax.swing.JComboBox<String> jComboBox_Columnas;
    private javax.swing.JComboBox<String> jComboBox_Operador;
    private javax.swing.JComboBox<String> jComboBox_Tablas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JPanel jPanel_Consultas;
    private javax.swing.JPanel jPanel_Fichar;
    private javax.swing.JPanel jPanel_Nominas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txt_contr;
    private javax.swing.JTextField txt_dni;
    private javax.swing.JTextField txt_ejecutar;
    private javax.swing.JTextField txt_nomina;
    private javax.swing.JTextField txt_puerto;
    private javax.swing.JTextField txt_servidor;
    private javax.swing.JTextField txt_usuario;
    private javax.swing.JTextField txt_valor;
    private javax.swing.JDialog ventana_conex;
    private javax.swing.JDialog ventana_select;
    // End of variables declaration//GEN-END:variables
}
