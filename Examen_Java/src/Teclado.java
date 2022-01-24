import java.util.Scanner;

public class Teclado {

    public static String leer_cadena (String mensaje) {
        System.out.print (mensaje);
        String cadena = System.console().readLine();
        return (cadena);
    }

    public static int leer_entero (String mensaje) {
        // crea objeto entrada de la clase Scanner para obtener la entrada de la terminal de comandos
        Scanner entrada = new Scanner (System.in);

        int num1 = 0;

        System.out.print (mensaje);

        // nos aseguramos de controlar posibles errores en la lectura
        try {
            num1 = entrada.nextInt();
        }
        // mostramos mensaje de error en caso de lectura incorrecta (p.e leer una letra en vez de un nº)
        catch (Exception e){
            System.err.println ("Error de lectura del número. Código de error = " + e);
            e.printStackTrace();
            System.exit(1);
        }

        return num1;
    } // fin leer entero

    public static String leer_cadena2 (String mensaje) {
        // ************* LEER CADENAS DE CARACTERES con nextLine de Scanner, FALLA a veces **********
        // OjO!!! puede que te falle la lectura de texto,
        // al leer muchas veces (si el buffer de entrada se llena)

        // crea objeto entrada de la clase Scanner para obtener la entrada de la terminal de comandos
        Scanner entrada = new Scanner (System.in);

        String texto="";

        System.out.print (mensaje);
        // nos aseguramos de controlar posibles errores en la lectura
        try {
            texto = entrada.nextLine();
        }
        // mostramos mensaje de error en caso de lectura incorrecta
        catch (Exception e){
            System.err.println ("Error de lectura del número. Código de error = " + e);
            e.printStackTrace();
            System.exit(1);
        }

        return texto;

    }// fin leer_cadena2

    /* in: recibe una cadena
         out: devuelve un double leído desde teclado, muestra por pantalla mensaje
        */
    public static double leer_double (String mensaje) {
        Scanner entrada = new Scanner (System.in);

        // leer un real de doble precisión desde la consola
        double real1=0;
        System.out.print (mensaje);

        // nos aseguramos de controlar posibles errores en la lectura
        try {
            real1 = entrada.nextDouble(); // OjO hay que poner la coma como separador decimal
        }
        // mostramos mensaje de error en caso de lectura incorrecta (p.e leer una letra en vez de un nº)
        catch (Exception e){
            System.err.println ("Error de lectura del número. Código de error = " + e);
            e.printStackTrace();
            System.exit(1);
        }

        // devolver el real leído desde teclado
        return real1;
    } // fin leer_double

    public static char leer_caracter(String mensaje){
        Scanner entrada= new Scanner (System.in);
        String texto="";

        System.out.print (mensaje);
        try {
            texto = entrada.nextLine();
        }
        catch (Exception e) {
            System.err.println("error de lectura. codigo de error = " +e);
            e.printStackTrace();
        }
        char caracter=texto.charAt(0);
        return caracter;
    }


    public static void main (String args[]) {

        String cadena = leer_cadena2 ("Introduce una cadena2: ");
        System.out.println ("La cadena leida es: " + cadena);

        String palabras = leer_cadena ("Introduce una cadena: ");
        System.out.println ("La cadena leida es: " + palabras);

        int numero = leer_entero ("Introduce un número: ");
        System.out.println ("El número leido es: " + numero);

    } // fin método main

} // fin clase teclado
