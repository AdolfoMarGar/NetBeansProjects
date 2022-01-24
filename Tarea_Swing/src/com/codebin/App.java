package com.codebin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TimeZone;


public class App extends JFrame{
    ArrayList<String>datos=new ArrayList<String>();
    String consulta;
    int posicion=0;
    int tope;
    public static void creaTablaCoches (Connection con, String BD) throws SQLException {

        String cadena = "Create Table If Not Exists " + BD + ".coches_en_stock" +
                "(matricula varchar(8) NOT NULL," +
                "marca varchar(40) NOT NULL," +
                "modelo varchar(40) NOT NULL," +
                "color varchar(40) NOT NULL," +
                "año integer NOT NULL," +
                "precio integer NOT NULL," +
                "Primary Key (Matricula))";
        // el objeto de stmt de la clase Statement servirá para enviar órdenes SQL
        Statement stmt = null;

        try {
            // Llamada al constructor de la clase Statement con un objeto de la clase Connection
            stmt = con.createStatement();

            // Ejecutar sentencias DDL (Data Definition Language)
            // => como crear tablas o eliminarlas. También insertar, borrar o modificar filas de una tabla
            stmt.executeUpdate (cadena);

        } catch (SQLException e) {
            // Mostrar el error SQL producido
            printSQLException(e);

        } finally {
            // Cerrar el objeto Statement
            stmt.close();
        }

    }

    public static void cargaTablaCoches (Connection con, String BD, String fichero, ArrayList<String>v) throws SQLException {

        // el objeto de stmt de la clase Statement servirá para enviar órdenes SQL

        Statement stmt = null;

        try {
            boolean insertar = false;
            // Llamada al constructor de la clase Statement con un objeto de la clase Connection
            stmt = con.createStatement();

            // Abrir el fichero de texto para lectura usando un Buffer
            File f = new File(fichero);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);

            //Arraylist auxiliares
            ArrayList<String> matricula = new ArrayList<String>();
            ArrayList<String> marca = new ArrayList<String>();
            ArrayList<String> modelo = new ArrayList<String>();
            ArrayList<String> color = new ArrayList<String>();
            ArrayList<Integer> año = new ArrayList<Integer>();
            ArrayList<Integer> precio = new ArrayList<Integer>();

            // Lectura del fichero línea a línea con BufferedReader
            String linea;
            while ((linea = br.readLine()) != null) {
                // definir que los trozos o tokens están separados por espacios en blanco
                StringTokenizer trozo = new StringTokenizer(linea);

                matricula.add(trozo.nextToken());
                marca.add(trozo.nextToken());
                modelo.add(trozo.nextToken());
                color.add(trozo.nextToken());
                año.add(Integer.parseInt(trozo.nextToken()));
                precio.add(Integer.parseInt(trozo.nextToken()));
                // Crear la cadena que va a insertar los datos en la tabla COCHE
                // con los datos suministrados por la línea leída del fichero
            } // fin while


            if(v.size()==0){
                for (int i = 0; i < matricula.size(); i++) {
                    String comando = "Insert Into " + BD + ".coches_en_stock Values (" +
                            "'" + matricula.get(i) + "'," + // Matricula
                            "'" + marca.get(i) + "'," + // Marca
                            "'" + modelo.get(i) + "'," + // Modelo
                            "'" + color.get(i) + "'," + // Color
                            "" + año.get(i) + "," +   // Año
                            "" + precio.get(i) + ")";    // Precio

                    // Ejecutar el comando SQL que inserta los datos en la tabla
                    stmt.executeUpdate(comando);
                }
            }else {
                for (int i = 0; i < matricula.size(); i++) {
                    boolean introducir=true;
                    String auxm = matricula.get(i);

                    for (int j = 0; j < v.size(); j++) {
                        if (auxm.equals(v.get(j))) {
                            introducir=false;
                        }
                    }

                    if (introducir == true) {
                        String comando = "Insert Into " + BD + ".coches_en_stock Values (" +
                                "'" + matricula.get(i) + "'," + // Matricula
                                "'" + marca.get(i) + "'," + // Marca
                                "'" + modelo.get(i) + "'," + // Modelo
                                "'" + color.get(i) + "'," + // Color
                                "" + año.get(i) + "," +   // Año
                                "" + precio.get(i) + ")";    // Precio

                        // Ejecutar el comando SQL que inserta los datos en la tabla
                        stmt.executeUpdate(comando);

                    }
                }
            }

            // Cerrar el fichero
            if (fr != null)
                fr.close();

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Fichero no encontrado " + fichero);


        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Error de E/S " + fichero);
            e.printStackTrace();

        } catch (SQLException e) {
            // Mostrar el error SQL producido
            printSQLException(e);

        } finally {
            // Cerrar el objeto Statement
            stmt.close();
        }
    }

    public static void printSQLException (SQLException e) {
        e.printStackTrace (System.err);
        // Mostrar el código SQLState
        JOptionPane.showMessageDialog(null,"\nSQLState: " + e.getSQLState() );
        // Código de error numérico
        JOptionPane.showMessageDialog(null,"\nCódigo de error: " + e.getErrorCode() );
        // Descripción del error
        JOptionPane.showMessageDialog(null,"\nDescripción del error: " + e.getMessage() );

        // Obtener todos las causas del error
        Throwable t = e.getCause();
        int i = 1;
        while (t != null) {
            JOptionPane.showMessageDialog(null,"\nCausa: " + i + "ª: " + t);

            i ++;
            t = t.getCause();
        }

    }

    public static void rellenar_Arraylist(Connection con, String consulta, ArrayList<String>v) throws SQLException{
        Statement stmt = null;
        v.clear();
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery (consulta);

            while ( rs.next() ) {
                String matricula = rs.getString ("matricula");
                v.add(matricula);
            }
        } catch (SQLException e) {
            printSQLException(e);
        } finally {
            stmt.close();
        }
    }

    public static ArrayList<String> verCoche_siguiente (Connection con, String consulta, int posicion) throws SQLException {
        int contador=0;
        ArrayList<String>v=new ArrayList<String>();
        Statement stmt = null;
        try {

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery (consulta);
            while ( rs.next() ) {
                if(contador==posicion) {
                    v.add(rs.getString("matricula"));

                    v.add(rs.getString("marca"));

                    v.add(rs.getString("modelo"));

                    v.add(rs.getString("color"));

                    v.add(rs.getString("año"));

                    v.add(rs.getString("precio"));
                }
                contador++;

            }
        } catch (SQLException e) {
            // Mostrar el error SQL producido
            printSQLException(e);

        } finally {
            // Cerrar el objeto Statement
            stmt.close();
        }

        return v;

    }//funciona

    public static void añadirOborrarCoche(Connection con, String consulta) throws SQLException{
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate (consulta);
        } catch (SQLException e) {
            printSQLException(e);
        } finally {
            stmt.close();
        }
    }

    public static boolean compararMatriculas(ArrayList v,String matricula){
        boolean existe=false;
        for (int i = 0; i < v.size(); i++) {

            if (matricula.equals(v.get(i))) {
                existe=true;
            }



        }
        if (existe==true){
            return true;
        }
        else {
            return false;
        }
    }

    private JPanel Panel;

    private JButton siguiente;
    private JButton insertar;
    private JButton borrar;
    private JButton limpiar;
    private JButton cargarButton;

    private JTextField matricula;
    private JTextField marca;
    private JTextField modelo;
    private JTextField color;
    private JTextField año;
    private JTextField precio;
    private JTextField n_coches;

    private JLabel l_matricula;
    private JLabel l_marca;
    private JLabel l_modelo;
    private JLabel l_color;
    private JLabel l_año;
    private JLabel l_precio;
    private JLabel l_num_coches;


    public App(Connection con,ArrayList v) {

        n_coches.setText(posicion+"/"+String.valueOf(v.size()));
        tope=v.size();

        siguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consulta="Select * From concesionario.coches_en_stock";
                if(posicion<tope) {
                    try {
                        datos=verCoche_siguiente(con, consulta, posicion);
                    }  catch (SQLException a) {
                        // Mostrar el error SQL producido
                        printSQLException(a);
                    }
                    posicion++;
                    n_coches.setText(posicion+"/"+String.valueOf(v.size()));
                }else{
                    JOptionPane.showMessageDialog(null,"No hay mas coches en la base de datos.");
                }

                matricula.setText(datos.get(0));
                marca.setText(datos.get(1));
                modelo.setText(datos.get(2));
                color.setText(datos.get(3));
                año.setText(datos.get(4));
                precio.setText(datos.get(5));


            }
        });//funciona

        insertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int año_aux=0,precio_aux=0;
                boolean salir=false,insertar=false;

                if(matricula.getText().equals("")||matricula.getText().length()>8){
                    JOptionPane.showMessageDialog(null,"Introduzca un valor en ''Matrícula'' o uno menor a 8 de long.");

                }else{
                    if(compararMatriculas(v,matricula.getText())==false){
                        insertar=true;

                    }else{
                        JOptionPane.showMessageDialog(null,"Esa matricula ya esta en la BD.");
                    }
                }

                if(marca.getText().equals("")||marca.getText().length()>40){
                    JOptionPane.showMessageDialog(null,"Introduzca un valor en ''Marca'' o uno menor a 40 de long.");
                    insertar=false;
                }

                if(modelo.getText().equals("")||modelo.getText().length()>40){
                    JOptionPane.showMessageDialog(null,"Introduzca un valor en ''Modelo'' o uno menor a 40 de long.");
                    insertar=false;
                }

                if(color.getText().equals("")||color.getText().length()>40){
                    JOptionPane.showMessageDialog(null,"Introduzca un valor en ''Color'' o uno menor a 40 de long.");
                    insertar=false;
                }

                try {
                    if(año.getText().equals("")){
                        JOptionPane.showMessageDialog(null,"Introduzca un valor en ''Año''.");
                        insertar=false;
                    }else{
                        año_aux=Integer.parseInt(año.getText());


                    }
                }  catch (java.lang.NumberFormatException a) {

                    JOptionPane.showMessageDialog(null,"Introduzca un valor numerico en ''Año''.");
                    insertar=false;
                }

                try {
                    if(año.getText().equals("")){
                        JOptionPane.showMessageDialog(null,"Introduzca un valor en ''Año''.");
                        insertar=false;
                    }else{
                        año_aux=Integer.parseInt(año.getText());


                    }
                }  catch (java.lang.NumberFormatException a) {

                    JOptionPane.showMessageDialog(null,"Introduzca un valor numerico en ''Precio''.");
                    insertar=false;
                }

                if(precio.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Introduzca un valor en ''Precio''.");
                    insertar=false;
                }else{
                    precio_aux=Integer.parseInt(precio.getText());

                }

                if(insertar==true) {
                    consulta = ("INSERT INTO coches_en_stock (matricula, marca, modelo, color, año, precio) " +
                            "VALUES ('" + matricula.getText() + "', '" + marca.getText() + "', '" + modelo.getText() + "', '" + color.getText() + "', " + año_aux + ", " + precio_aux + ")");
                    try {
                        añadirOborrarCoche(con,consulta);
                        JOptionPane.showMessageDialog(null,"Datos insertados con exito.");
                        v.add(matricula.getText());
                        tope++;
                        n_coches.setText(posicion+"/"+String.valueOf(v.size()));
                    }  catch (SQLException a) {
                        // Mostrar el error SQL producido
                        printSQLException(a);
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"No ha sido insertado.");
                }

            }
        });//funciona

        borrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String aux=matricula.getText();
                if (compararMatriculas(v,aux)==true) {
                    consulta =  "DELETE FROM `concesionario`.`coches_en_stock`"+
                            "WHERE matricula = '" + aux + "';";
                    try {
                        añadirOborrarCoche(con,consulta);
                    }  catch (SQLException a) {
                        // Mostrar el error SQL producido
                        printSQLException(a);
                    }
                    for(int i=0;i<v.size();i++){
                        if(matricula.getText().equals(v.get(i))){
                            if(i+1==posicion)posicion--;
                            v.remove(i);
                            tope--;
                        }
                    }
                    n_coches.setText(posicion+"/"+String.valueOf(v.size()));
                    JOptionPane.showMessageDialog(null,"Datos borrados.");
                }else{
                    JOptionPane.showMessageDialog(null,"No coincide la matricula con ningun coche de la BD.");

                }


            }
        });//funciona

        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                matricula.setText("");
                marca.setText("");
                modelo.setText("");
                color.setText("");
                año.setText("");
                precio.setText("");
                JOptionPane.showMessageDialog(null,"Campos limpiados.");
            }
        });//funciona

        cargarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try {
                    cargaTablaCoches(con, "concesionario", "coches.txt",v);

                    rellenar_Arraylist(con,"Select * From concesionario.coches_en_stock",v);
                    posicion=0;
                    tope=v.size();
                }  catch (SQLException a) {
                    // Mostrar el error SQL producido
                    JOptionPane.showMessageDialog(null,"Error al cargar los datos.");
                    printSQLException(a);
                }

                n_coches.setText(posicion+"/"+String.valueOf(v.size()));
            }
        });

    }

    public static void main(String[] args) {
        ArrayList<String> matriculas = new ArrayList<String>();

        /*
        /Primero tienes que añadir/editar la ruta del jdbc.
        /Antes de ejecutar ejecutar la siguiente sentencia sql para crear la bd
        /"create database concesionario;"
        /Una vez creada funcionara el codigo de manera adecuada.
        /La tabla se llama coches_en_stock
        /Y todos los campos son en minuscula.
         */

        try {
            Connection connection = null;

            Class.forName("com.mysql.cj.jdbc.Driver");

            String horaLocal = TimeZone.getDefault().getID();

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/concesionario?serverTimezone=" + horaLocal, "root", "ekko007123");

            String consulta = "Select * From concesionario.coches_en_stock";

            rellenar_Arraylist(connection,consulta,matriculas);

            creaTablaCoches (connection, "concesionario");

            cargaTablaCoches (connection, "concesionario", "coches.txt",matriculas);

            rellenar_Arraylist(connection,consulta,matriculas);


            JFrame frame =new JFrame("Coches en stock.");
            frame.setContentPane(new App(connection,matriculas).Panel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);



        } catch (SQLException e) {
            // Mostrar el error SQL producido
            printSQLException(e);
        } catch (ClassNotFoundException cE) {
            JOptionPane.showMessageDialog(null,"Error no se ha encontrado la clase para el controlador JDBC: " + cE.toString());
        }


    }
}
// para enseñar mensaje
//JOptionPane.showMessageDialog(null,"Mensaje");