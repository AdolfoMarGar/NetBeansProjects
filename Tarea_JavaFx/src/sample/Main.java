package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.logging.Handler;

public class Main extends Application {
    static int posicion=0;
    //labels
    static Label lbMatricula = new Label("Matricula:");
    static Label lbMarca = new Label("Marca:");
    static Label lbModelo = new Label("Modelo:");
    static Label lbColor = new Label("Color:");
    static Label lbAño = new Label("Año:");
    static Label lbPrecio = new Label("Precio:");
    static Label lbNum_Coches =new Label("Nº de coches: ");

    //textFields
    static TextField tfMatricula = new TextField();
    static TextField tfMarca = new TextField();
    static TextField tfModelo = new TextField();
    static TextField tfColor = new TextField();
    static TextField tfAño = new TextField();
    static TextField tfPrecio = new TextField();

    //botones
    static Button btSiguiente = new Button("Siguiente");
    static  Button btInsertar = new Button("Insertar");
    static Button btBorrar = new Button("Borrar");
    static Button btLimpiar = new Button("Limpiar");
    static  Button btCargar = new Button("Cargar");



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

    public static void cargaTablaCoches (Connection con, String BD, String fichero, ArrayList<String> v) throws SQLException {

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

    @Override
    public void start(Stage escenario_principal){
        try{
            Connection con;

            Class.forName("com.mysql.cj.jdbc.Driver");

            String horaLocal = TimeZone.getDefault().getID();

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/concesionario?serverTimezone=" + horaLocal, "root", "ekko007123");
            ArrayList <String> matriculas= new ArrayList<String>();

            String consulta = "Select * From concesionario.coches_en_stock";

            rellenar_Arraylist(con,consulta,matriculas);

            creaTablaCoches (con, "concesionario");

            cargaTablaCoches (con, "concesionario", "coches.txt",matriculas);

            rellenar_Arraylist(con,consulta,matriculas);


            //pane
            VBox raiz = new VBox();
            raiz.setPadding(new Insets(40));
            raiz.setSpacing(10);
            raiz.setAlignment(Pos.CENTER);

            //añadiendo al pane
            raiz.getChildren().addAll(lbMatricula, tfMatricula);
            raiz.getChildren().addAll(lbMarca, tfMarca);
            raiz.getChildren().addAll(lbModelo, tfModelo);
            raiz.getChildren().addAll(lbColor, tfColor);
            raiz.getChildren().addAll(lbAño, tfAño);
            raiz.getChildren().addAll(lbPrecio, tfPrecio);
            raiz.getChildren().addAll(btSiguiente,btInsertar,btBorrar,btLimpiar,btCargar);
            raiz.getChildren().add(lbNum_Coches);

            //Escena
            Scene escena = new Scene (raiz, 400, 600);
            escenario_principal.setTitle("Tarea Java Fx.");
            escenario_principal.setScene(escena);

            //siempre va al final para mostrar la escena
            escenario_principal.show();

            //Metodos botones y funcionamiento
            btSiguiente.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    handler(e, con, matriculas, posicion);
                    posicion++;
                }
            });
            btBorrar.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    handler(e, con, matriculas, posicion);
                    posicion--;
                }
            });
            btCargar.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    handler(e, con, matriculas, posicion);
                    posicion=0;
                }
            });
            btInsertar.setOnAction(e -> handler(e, con, matriculas, posicion));
            btLimpiar.setOnAction(e -> handler(e, con, matriculas, posicion));

        }catch (SQLException e) {
            // Mostrar el error SQL producido
            printSQLException(e);
        }catch(Exception f){
            f.printStackTrace();
        }
    }



    public static void handler (ActionEvent handle, Connection con, ArrayList<String> v, int posicion){


        ArrayList<String>datos=new ArrayList<String>();
        String consulta;
        int tope=v.size()-1;
        int opcion=-1;
        lbNum_Coches.setText("Nº de coches: "+posicion+"/"+String.valueOf(v.size()));

        Object aux = handle.getSource();

        if (aux==btSiguiente)opcion=1;
        else{
            if(aux==btInsertar)opcion=2;
            else{
                if(aux==btBorrar)opcion=3;
                else{
                    if(aux==btLimpiar)opcion=4;
                    else opcion=5;
                }
            }
        }

        try {

            tope=v.size();
            System.out.println("Nº de coches: "+posicion+"/"+String.valueOf(v.size()));
            switch (opcion) {
                case 1:
                    System.out.println("ejecutado"+posicion);
                    consulta="Select * From concesionario.coches_en_stock";
                    if(posicion<tope) {
                        datos=verCoche_siguiente(con, consulta, posicion);
                        posicion++;
                        lbNum_Coches.setText("Nº de coches: "+posicion+"/"+String.valueOf(v.size()));
                    }

                    tfMatricula.setText(datos.get(0));
                    tfMarca.setText(datos.get(1));
                    tfModelo.setText(datos.get(2));
                    tfColor.setText(datos.get(3));
                    tfAño.setText(datos.get(4));
                    tfPrecio.setText(datos.get(5));

                    break;

                case 2:

                    break;

                case 3:
                    String aux_1=tfMatricula.getText();
                    if (compararMatriculas(v,aux_1)==true) {
                        consulta =  "DELETE FROM `concesionario`.`coches_en_stock`"+
                                "WHERE matricula = '" + aux_1 + "';";
                        try {
                            añadirOborrarCoche(con,consulta);
                        }  catch (SQLException a) {
                            // Mostrar el error SQL producido
                            printSQLException(a);
                        }
                        for(int i=0;i<v.size();i++){
                            if(tfMatricula.getText().equals(v.get(i))){
                                if(i+1==posicion)posicion--;
                                v.remove(i);
                            }
                        }
                        lbNum_Coches.setText("Nº de coches: "+posicion+"/"+String.valueOf(v.size()));
                    }
                    break;

                case 4:
                    tfMatricula.setText("");
                    tfMarca.setText("");
                    tfModelo.setText("");
                    tfColor.setText("");
                    tfAño.setText("");
                    tfPrecio.setText("");
                    break;


                case 5:
                    try {
                        cargaTablaCoches(con, "concesionario", "coches.txt",v);
                        rellenar_Arraylist(con,"Select * From concesionario.coches_en_stock",v);

                    }  catch (SQLException a) {
                        // Mostrar el error SQL producido
                        System.out.println("Error al cargar los datos.");
                    }

            }
        }catch (SQLException e) {
            // Mostrar el error SQL producido
            printSQLException(e);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
