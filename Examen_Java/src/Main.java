
import java.util.*;
import java.io.*;

public class Main {



    /* E: lista_alumnos => ArrayList de los alumnos
       S: nada, muestra y gestiona las siguientes opciones del menú,
                que llamarán a los siguientes métodos especificados entre paréntesis(....)

        1.- Añadir alumno (anadir_alumno() )
        2.- Listado alumnos (listado_alumnos () )
        3.- Buscar alumno por DNI (buscar_alumno_teclado() )
        4.- Ordenar por nombre los alumnos ( Collections.sort() )
        5.- Eliminar un alumno por DNI (borrar_alumno () )
        6.- Poner una nueva falta (falta_alumno)
        7.- Guardar la lista de alumnos en el fichero de objetos: alumnos.dat (escribir_fichero() )
        8.- Leer el fichero de objetos: alumnos.dat, en el ArrayList de alumnos (leer_fichero () )
        0.- Salir (Sale del menú y termina el programa)

        Introduce opción:

     */
    public static void menu(ArrayList<Alumno> v) {

        int car;
        boolean salir=false;
        char eleccion;


        do {
            System.out.println("");
            System.out.println("");
            System.out.println("MENÚ\n");
            System.out.println("1.- Añadir Alumno.");
            System.out.println("2.- Listar Alumnos.");
            System.out.println("3.- Buscar alumnos por DNI.");
            System.out.println("4.- Ordenar por nombre los alumnos.");
            System.out.println("5.- Borrar alumno por DNI.");
            System.out.println("6.- Poner una nueva falta.");
            System.out.println("0.- Salir");
            System.out.println("");
            System.out.println("");

            car = Teclado.leer_entero("\nIntroduzca opción: ");

            System.out.println("\nOpción elegida = " + car);
            switch (car) {

                case 1:
                    anadir_alumno(v);
                    break;

                case 2:
                    listado_alumnos(v);
                    break;

                case 3:
                    buscar_alumno_teclado(v);
                    break;

                case 4:
                    v.sort(new Comparador_Alumno());
                    System.out.println("Arraylist ordenado.");
                    break;

                case 5:
                    borrar_alumno(v);
                    break;

                case 6:
                    falta_alumno(v);
                    break;


                case 0:
                    System.out.println("\nSaliendo del programa...");
                    salir = true;
                    break;


                default:
                    System.out.println("\n\nError, opción incorrecta:\n\n ");

            }
        }while(salir!=true);
    } // fin menu


    public static int buscar_alumno(ArrayList<Alumno> v, int DNI) {
        v.sort(new Comparador_Dni());
        ArrayList<Integer> aux = new ArrayList<Integer>();
        aux.clear();
        if(v.size()==0)return -1;
        else{
            for(int i=0;i<v.size();i++){
                aux.add(v.get(i).getDNI());
            }
            return Collections.binarySearch(aux,DNI);
        }
    } //Funciona

    public static void anadir_alumno(ArrayList<Alumno> v) {
        // ............ Completa el código ..................+
        String nombre;
        char aux;
        int dni, edad, faltas;
        boolean sexo, introducir=false;
        Alumno alumno;

        do {
            nombre=Teclado.leer_cadena2("Introduce el nombre del Alumno:");
            if(nombre.equals(""))System.out.println("Introduce un nombre.");
        }while (nombre.equals(""));

        do {
            dni=Teclado.leer_entero("Introduce el DNI: ");
            if(dni<0)System.out.println("Introduce un DNI posible.");
        }while (dni<0);

        do {
            edad=Teclado.leer_entero("Introduce la edad: ");
            if(edad<0 || edad>100)System.out.println("Introduce una edad posible.");
        }while (edad<0 || edad>100);

        do {
            aux=Teclado.leer_caracter("Introduce h para hombre o m para mujer: ");
            if(aux!='h'&& aux!='m')System.out.println("Introduce una de las opciones posibles.");
        }while (aux!='h'&& aux!='m');

        if(aux=='h')sexo=true;
        else sexo=false;

        do {
            faltas=Teclado.leer_entero("Introduce el Nº de faltas: ");
            if(faltas<0)System.out.println("Introduce un Nº posible.");
        }while (faltas<0);

        if(v.size()==0){
            alumno= new Alumno(nombre,dni,edad,sexo,faltas);
            v.add(alumno);
            System.out.println("Alumno añadido.");
            System.out.println(alumno);

        }else{
            if(buscar_alumno(v,dni)<0){
                alumno= new Alumno(nombre,dni,edad,sexo,faltas);
                v.add(alumno);
                System.out.println("Alumno añadido.");
                System.out.println(alumno);

            }else{
                System.out.println("No se pudo añadir alumno debido a que ese Dni ya esta guardado.");
            }
        }



    } //Funciona

    public static void listado_alumnos(ArrayList<Alumno> v) {
        if(v.size()==0)System.out.println("No hay Alumnos almacenados.");
        else {
            System.out.println("Listado Alumnos:");
            v.forEach(System.out::println);
        }
    } //Funciona

    public static void buscar_alumno_teclado(ArrayList<Alumno> v) {
        int dni_aux;
        do {
            dni_aux=Teclado.leer_entero("Introduce el dni que quieres buscar:");
            if(dni_aux<0)System.out.println("Introduce un DNI posible.");
        }while (dni_aux<0);

        if(buscar_alumno(v,dni_aux)>=0){
            System.out.println("¡Dni encontrado!");
            System.out.println("Se encuentra en la posición "+buscar_alumno(v,dni_aux)+".");
            System.out.println(v.get(buscar_alumno(v,dni_aux)));

        }else{
            System.out.println("Dni no encontrado.");

        }

    }//Funciona

    public static void borrar_alumno(ArrayList<Alumno> v) {
        int dni_aux;
        do {
            dni_aux = Teclado.leer_entero("Introduce el dni del alumnos que quieres borrar:");
            if (dni_aux < 0) System.out.println("Introduce un DNI posible.");
        } while (dni_aux < 0);
        if(buscar_alumno(v,dni_aux)>=0){
            System.out.println("Borrando alumno");
            System.out.println("Se encuentra en la posición "+buscar_alumno(v,dni_aux)+".");
            v.remove(buscar_alumno(v,dni_aux));
        }else{
            System.out.println("Dni no encontrado.");
        }


    }//Funciona

    public static void falta_alumno(ArrayList<Alumno> v) {

        int dni_aux;
        do {
            dni_aux = Teclado.leer_entero("Introduce el dni del alumnos que falto:");
            if (dni_aux < 0) System.out.println("Introduce un DNI posible.");
        } while (dni_aux < 0);

        if(buscar_alumno(v,dni_aux)>=0){
            System.out.println("Alumno encontrado");
            System.out.println("Se encuentra en la posición "+buscar_alumno(v,dni_aux)+".");
            v.get(buscar_alumno(v,dni_aux)).setFaltas(v.get(buscar_alumno(v,dni_aux)).getFaltas()+1);
        }else{
            System.out.println("Dni no encontrado.");
        }

    } // fin falta_alumno



    public static void main(String[] args) {

        // Crear con new un nuevo ArrayList de la clase Alumno
        ArrayList<Alumno> lista_alumnos = new ArrayList<Alumno>(); // completa el código
        Alumno al1=new Alumno("z",77244236,20,true,30);
        Alumno al2=new Alumno("a",12345678,20,true,30);
        Alumno al3=new Alumno("g",23456789,20,true,30);
        Alumno al4=new Alumno("z",3,20,true,30);
        Alumno al5=new Alumno("a",456,20,true,30);
        Alumno al6=new Alumno("g",99541,20,true,30);
        lista_alumnos.add(al1);
        lista_alumnos.add(al2);
        lista_alumnos.add(al3);
        lista_alumnos.add(al4);
        lista_alumnos.add(al5);
        lista_alumnos.add(al6);



        // Llamar al menu con el ArrayList creado antes
        menu(lista_alumnos); // completa el código
    }
}
