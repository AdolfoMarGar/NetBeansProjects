/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Adolfo
 */


public class Interfaz extends javax.swing.JFrame {
    ArrayList <Alumno> lista= new ArrayList <>();
    RandomAccessFile fichero;

    public Interfaz() {
        initComponents();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setTitle("Practica 2");
        leerDatos();
        crearTabla();
    }
    
    private void mostrar1_Tabla(int pos){
        Object[][] datos =new Object [lista.size()][2];
        datos[0][0]=lista.get(pos).getNum_mat();
        datos [0][1] = lista.get(pos).getNota();
        
        tabla.setModel(new DefaultTableModel(datos, new String [] {"Nº Matricula", "Nota Media"}));
    }
    
    private void mostrar_TablaSeleccion(ArrayList<Integer> lista_aux){
        Object[][] datos =new Object [lista_aux.size()][2];
        for(int i=0;i<lista_aux.size();i++){
            datos[i][0]=lista.get(lista_aux.get(i)).getNum_mat();
            datos [i][1] = lista.get(lista_aux.get(i)).getNota();
        }
        tabla.setModel(new DefaultTableModel(datos, new String [] {"Nº Matricula", "Nota Media"}));
    }
    
    private void crearTabla(){     
        Object[][] datos =new Object [lista.size()][2];
        for(int i=0;i<lista.size();i++){
            datos[i][0]=lista.get(i).getNum_mat();
            datos [i][1] = lista.get(i).getNota();
        }
        tabla.setModel(new DefaultTableModel(datos, new String [] {"Nº Matricula", "Nota Media"}));
    }//perfecto, lee objetos Alumno desde arraylist y los pone en la tabla
    
    private void limpiarTabla(){
        tabla.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {"Nº Matricula","Nota Media"}
        ));
    }//Limpia la tabla
    
    private void limpiarArraylist(){
        lista.clear();
    }//Limpia el arraylist

    private void leerDatos (){
        int num;
        double nota;
        try {
            fichero = new RandomAccessFile("datos.txt","rw");
            fichero.seek(0);
            num=fichero.read();
            if(num!=-1){
                int i=1;
                do{
                    fichero.seek(i*12);
                    num=fichero.readInt();
                    if(num!=0){
                        nota=fichero.readDouble();
                        lista.add(new Alumno(num,nota));
                    }
                    i++;
                }while(num!=-1);
            }
            fichero.close();
        }catch(FileNotFoundException fnfex){
            JOptionPane.showMessageDialog(null, "Fichero no encontrado. \n"
                    + "Se ha creado uno nuevo en la carpeta del proyecto.");
        }
        catch (EOFException eox) {
            JOptionPane.showMessageDialog(null, "Fin de la lectura del archivo.");

        }catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error en la lectura del archivo.");
        }

    }//Lee datos del fichero seleccionado por el RandomAccessFile
    
    private void escribirDatos(int pos){
        double nota=-1;
        int aux=-1;
        //pos = nº matricula
        try {
                fichero= new RandomAccessFile("datos.txt","rw");
                fichero.seek(pos*12);
                for(int i=0;i<lista.size();i++){
                    if(pos==lista.get(i).getNum_mat()){
                        aux=lista.get(i).getNum_mat();
                        nota=lista.get(i).getNota();
                        break;
                    }
                }
                if(aux!=-1){
                    fichero.writeInt(aux);
                    fichero.writeDouble(nota);
                }
            fichero.close();
        }catch (EOFException eox) {
            JOptionPane.showMessageDialog(null, "Fin de la lectura del archivo.");

        }catch (FileNotFoundException eox) {
            JOptionPane.showMessageDialog(null, "Archivo no econtrado.");

        }catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Fin de la lectura del archivo.");
        }
    }//perefecto
    
    private void borrarDatos(int pos_fichero,int pos_lista) {
        try {
            fichero = new RandomAccessFile("datos.txt","rw");
            fichero.seek(pos_fichero*12);
            fichero.writeInt(0);
            fichero.writeDouble(0);
            lista.remove(pos_lista);
            fichero.close();
        }catch(FileNotFoundException fnfex){
            JOptionPane.showMessageDialog(null, "Fichero no encontrado. \n"
                    + "Se ha creado uno nuevo en la carpeta del proyecto.");
        }
        catch (EOFException eox) {
            JOptionPane.showMessageDialog(null, "Fin de la lectura del archivo.");

        }catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error en la lectura del archivo.");
        }

    }
    
    private void btn_Añadir(){
        int num;
        double nota;
        boolean filtro=false;
        try{
            num = Integer.parseInt(txt_numAlta.getText());
            nota = Double.parseDouble(txt_notaAlta.getText());
        
            if(num>0&&(nota<=10&&nota>=0)){
                Alumno temp = new Alumno(num,nota);
                if(lista.isEmpty()) {
                    lista.add(temp);
                    escribirDatos(temp.getNum_mat());
                }else{
                    for(int i=0;i<lista.size();i++){
                        if(num!=lista.get(i).getNum_mat()){
                            filtro=true;
                        }else{
                            filtro=false;
                            break;
                        }
                    }
                    if(filtro){
                        lista.add(temp);
                        escribirDatos(num);
                    }else{
                        JOptionPane.showMessageDialog(null, "Matricula ya existente. \n "
                                + "Ingrese otra.");
                    }                    
                }
            }else{
                JOptionPane.showMessageDialog(null, 
                    "Error al rellenar los campos. \n"
                    + "Nº Matricula: Nº natural (Mayor que 0)\n"
                    + "Nota Media: Entre 0 y 10 incluidos con posibilidad de decimales\n"
                    + "Ejemplos:\n"
                    + "\t -Nº Matricula: 1 \n"
                    + "\t -Nota Media: 5.5 \n"
                );
            }
        }catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(null, 
                "Error al rellenar los campos. \n"
                + "Nº Matricula: Nº natural (Mayor que 0)\n"
                + "Nota Media: Entre 0 y 10 incluidos con posibilidad de decimales\n"
                + "Ejemplos:\n"
                + "\t -Nº Matricula: 1 \n"
                + "\t -Nota Media: 5.5 \n"
            );
        }
    }//perefecto
    
    private void btn_Modificar(){
        int num;
        int aux=-1;
        double nota;
        boolean filtro=false;
        try{
            num = Integer.parseInt(txt_numMod.getText());
            nota = Double.parseDouble(txt_notaMod.getText());
        
            if(num>0&&(nota<=10&&nota>=0)){
                Alumno temp = new Alumno(num,nota);
                if(lista.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay ningun dato introducido.");
                }else{
                    for(int i=0;i<lista.size();i++){
                        if(num!=lista.get(i).getNum_mat()){
                            filtro=false;
                        }else{
                            filtro=true;
                            aux=i;
                            break;
                        }
                    }
                    if(filtro){
                        if(aux!=-1)lista.get(aux).setNota(nota);
                        escribirDatos(num);
                    }else{
                        JOptionPane.showMessageDialog(null, "Matricula no existente. \n"
                                + "Ingrese una ya registrada.");
                    }                    
                }
            }else{
                JOptionPane.showMessageDialog(null, 
                    "Error al rellenar los campos. \n"
                    + "Nº Matricula: Nº natural (Mayor que 0)\n"
                    + "Nota Media: Entre 0 y 10 incluidos con posibilidad de decimales\n"
                    + "Ejemplos:\n"
                    + "\t -Nº Matricula: 1 \n"
                    + "\t -Nota Media: 5.5 \n"
                );
            }
        }catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(null, 
                "Error al rellenar los campos. \n"
                + "Nº Matricula: Nº natural (Mayor que 0)\n"
                + "Nota Media: Entre 0 y 10 incluidos con posibilidad de decimales\n"
                + "Ejemplos:\n"
                + "\t -Nº Matricula: 1 \n"
                + "\t -Nota Media: 5.5 \n"
            );
        }
    }//perefecto
    
    private void btn_Borrar(){
        int num;
        int aux=-1;
        boolean filtro=false;
        try{
            num = Integer.parseInt(txt_numBorrar.getText());
            if(num>0){
                if(lista.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay ningun dato introducido.");
                }else{
                    for(int i=0;i<lista.size();i++){
                        if(num!=lista.get(i).getNum_mat()){
                            filtro=false;
                        }else{
                            filtro=true;
                            aux=i;
                            break;
                        }
                    }
                    if(filtro){
                        borrarDatos(lista.get(aux).getNum_mat(),aux);
                    }else{
                        JOptionPane.showMessageDialog(null, 
                            "Matricula no existente. \n"
                            + "Ingrese una ya registrada."
                        );
                    }                    
                }
            }else{
                JOptionPane.showMessageDialog(null, 
                    "Error al rellenar los campos. \n"
                    + "Nº Matricula: Nº natural (Mayor que 0)\n"
                    + "Nota Media: Entre 0 y 10 incluidos con posibilidad de decimales\n"
                    + "Ejemplos:\n"
                    + "\t -Nº Matricula: 1 \n"
                    + "\t -Nota Media: 5.5 \n"
                );
            }
        }catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(null, 
                "Error al rellenar los campos. \n"
                + "Nº Matricula: Nº natural (Mayor que 0)\n"
                + "Nota Media: Entre 0 y 10 incluidos con posibilidad de decimales\n"
                + "Ejemplos:\n"
                + "\t -Nº Matricula: 1 \n"
                + "\t -Nota Media: 5.5 \n"
            );
        }
    }
    
    private void btn_Buscar(){
        int num;
        int aux=-1;
        boolean filtro=false;
        try{
            num = Integer.parseInt(txt_numBuscar.getText());
            if(num>0){
                if(lista.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay ningun dato introducido.");
                }else{
                    for(int i=0;i<lista.size();i++){
                        if(num!=lista.get(i).getNum_mat()){
                            filtro=false;
                        }else{
                            filtro=true;
                            aux=i;
                            break;
                        }
                    }
                    if(filtro){
                        limpiarTabla();
                        mostrar1_Tabla(aux);
                    }else{
                        limpiarTabla();
                        crearTabla();
                        JOptionPane.showMessageDialog(null, 
                            "Matricula no existente. \n"
                            + "Ingrese una ya registrada."
                        );
                    }                    
                }
            }else{
                JOptionPane.showMessageDialog(null, 
                    "Error al rellenar los campos. \n"
                    + "Nº Matricula: Nº natural (Mayor que 0)\n"
                    + "Nota Media: Entre 0 y 10 incluidos con posibilidad de decimales\n"
                    + "Ejemplos:\n"
                    + "\t -Nº Matricula: 1 \n"
                    + "\t -Nota Media: 5.5 \n"
                );
            }
        }catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(null, 
                "Error al rellenar los campos. \n"
                + "Nº Matricula: Nº natural (Mayor que 0)\n"
                + "Nota Media: Entre 0 y 10 incluidos con posibilidad de decimales\n"
                + "Ejemplos:\n"
                + "\t -Nº Matricula: 1 \n"
                + "\t -Nota Media: 5.5 \n"
            );
        }
    }
    
    private void btn_BuscarNota(){
        ArrayList<Integer> lista_aux= new ArrayList<>();
        double nota;
        int aux=-1;
        boolean filtro=false;
        int cont=0;
        try{
            nota = Double.parseDouble(txt_notaBuscar.getText());
            if(nota<=10&&nota>=0){
                if(lista.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay ningun dato introducido.");
                }else{
                    for(int i=0;i<lista.size();i++){
                        if(nota!=lista.get(i).getNota()){
                            filtro=false;
                        }else{
                            filtro=true;
                            aux=i;
                        }
                        if(filtro){
                            lista_aux.add(aux);
                            filtro=false;
                            cont=1;
                        }
                    }
                    
                    if(cont==0){
                        limpiarTabla();
                        crearTabla();
                        JOptionPane.showMessageDialog(null, 
                            "Nota no encontrada."
                        );
                    }else{
                        mostrar_TablaSeleccion(lista_aux);
                    }                    
                }
            }else{
                JOptionPane.showMessageDialog(null, 
                    "Error al rellenar los campos. \n"
                    + "Nº Matricula: Nº natural (Mayor que 0)\n"
                    + "Nota Media: Entre 0 y 10 incluidos con posibilidad de decimales\n"
                    + "Ejemplos:\n"
                    + "\t -Nº Matricula: 1 \n"
                    + "\t -Nota Media: 5.5 \n"
                );
            }
        }catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(null, 
                "Error al rellenar los campos. \n"
                + "Nº Matricula: Nº natural (Mayor que 0)\n"
                + "Nota Media: Entre 0 y 10 incluidos con posibilidad de decimales\n"
                + "Ejemplos:\n"
                + "\t -Nº Matricula: 1 \n"
                + "\t -Nota Media: 5.5 \n"
            );
        }
    }
    
    private void btn_BorrarFichero(){
        if(lista.isEmpty()){
            JOptionPane.showMessageDialog(null, "El fichero esta vacio.");
        }else{
            for(int i=0; i<lista.size();i++){
                borrarDatos(lista.get(i).getNum_mat(),i);
                i--;
            }
        
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_numAlta = new javax.swing.JFormattedTextField();
        txt_notaAlta = new javax.swing.JFormattedTextField();
        boton_alta = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_numMod = new javax.swing.JFormattedTextField();
        txt_notaMod = new javax.swing.JFormattedTextField();
        boton_modificar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txt_numBorrar = new javax.swing.JFormattedTextField();
        boton_borrar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txt_numBuscar = new javax.swing.JFormattedTextField();
        boton_buscar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        btn_BorrarTabla = new javax.swing.JButton();
        btn_MostrarTabla = new javax.swing.JButton();
        btn_BorrarFichero = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_notaBuscar = new javax.swing.JTextField();
        boton_buscarNota = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Alta");

        jLabel2.setText("Nº Matricula:");

        jLabel3.setText("Nota:");

        boton_alta.setText("Alta");
        boton_alta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_altaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(boton_alta))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_numAlta))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(45, 45, 45)
                        .addComponent(txt_notaAlta)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_numAlta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_notaAlta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(boton_alta)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setText("Modificar");

        jLabel6.setText("Nº Matricula:");

        jLabel7.setText("Nota:");

        boton_modificar.setText("Modificar");
        boton_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_modificarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(boton_modificar))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_numMod, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_notaMod, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_numMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_notaMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(boton_modificar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setText("Borrar");

        jLabel10.setText("Nº Matricula:");

        boton_borrar.setText("Borrar");
        boton_borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_borrarActionPerformed(evt);
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
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(boton_borrar))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_numBorrar)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txt_numBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(boton_borrar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel11.setText("Buscar Nº Matricula");

        jLabel13.setText("Nº Matricula:");

        boton_buscar.setText("Buscar");
        boton_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_buscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(boton_buscar))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_numBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_numBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(boton_buscar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btn_BorrarTabla.setText("Borrar Tabla");
        btn_BorrarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BorrarTablaActionPerformed(evt);
            }
        });

        btn_MostrarTabla.setText("Mostrar Tabla");
        btn_MostrarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_MostrarTablaActionPerformed(evt);
            }
        });

        btn_BorrarFichero.setText("Borrar Fich.");
        btn_BorrarFichero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BorrarFicheroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btn_BorrarFichero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_BorrarTabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_MostrarTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_BorrarTabla)
                    .addComponent(btn_MostrarTabla))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_BorrarFichero)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setText("Buscar Nota");

        jLabel8.setText("Nota:");

        boton_buscarNota.setText("Buscar");
        boton_buscarNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_buscarNotaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txt_notaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(boton_buscarNota)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_notaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(boton_buscarNota)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nº Matricula", "Nota"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla.setEnabled(false);
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void boton_altaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_altaActionPerformed
        btn_Añadir();
        limpiarTabla();
        crearTabla();
    }//GEN-LAST:event_boton_altaActionPerformed

    private void boton_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_modificarActionPerformed
        btn_Modificar();
        limpiarTabla();
        crearTabla();
    }//GEN-LAST:event_boton_modificarActionPerformed

    private void boton_borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_borrarActionPerformed
        btn_Borrar();
        limpiarTabla();
        crearTabla();
    }//GEN-LAST:event_boton_borrarActionPerformed

    private void boton_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_buscarActionPerformed
        btn_Buscar();
    }//GEN-LAST:event_boton_buscarActionPerformed

    private void btn_MostrarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_MostrarTablaActionPerformed
        limpiarTabla();
        crearTabla();
    }//GEN-LAST:event_btn_MostrarTablaActionPerformed

    private void btn_BorrarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BorrarTablaActionPerformed
        limpiarTabla();
    }//GEN-LAST:event_btn_BorrarTablaActionPerformed

    private void boton_buscarNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_buscarNotaActionPerformed
        btn_BuscarNota();
    }//GEN-LAST:event_boton_buscarNotaActionPerformed

    private void btn_BorrarFicheroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BorrarFicheroActionPerformed
        btn_BorrarFichero();
    }//GEN-LAST:event_btn_BorrarFicheroActionPerformed

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
    private javax.swing.JButton boton_alta;
    private javax.swing.JButton boton_borrar;
    private javax.swing.JButton boton_buscar;
    private javax.swing.JButton boton_buscarNota;
    private javax.swing.JButton boton_modificar;
    private javax.swing.JButton btn_BorrarFichero;
    private javax.swing.JButton btn_BorrarTabla;
    private javax.swing.JButton btn_MostrarTabla;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla;
    private javax.swing.JFormattedTextField txt_notaAlta;
    private javax.swing.JTextField txt_notaBuscar;
    private javax.swing.JFormattedTextField txt_notaMod;
    private javax.swing.JFormattedTextField txt_numAlta;
    private javax.swing.JFormattedTextField txt_numBorrar;
    private javax.swing.JFormattedTextField txt_numBuscar;
    private javax.swing.JFormattedTextField txt_numMod;
    // End of variables declaration//GEN-END:variables
}
