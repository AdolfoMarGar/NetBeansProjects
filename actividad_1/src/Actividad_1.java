import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class Actividad_1 extends JFrame {
    JPanel Panel;

    JButton boton_1;
    JButton boton_2;

    JFormattedTextField jf_1;
    JFormattedTextField jf_2;

    JTable tabla;
    JScrollPane scroll;
    DefaultTableModel modelo;

    final String[] columnas = {"Nombre", "Extensión", "Tamaño", "F/D"};
    static String[][] data ={{"","","",""}};

    public Actividad_1 (){
        tabla = new JTable();
        scroll = new JScrollPane(tabla);
        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(columnas);
        //tabla.setModel(modelo);

        boton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               f_boton1(jf_1.getText());
            }
        });
        boton_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              //filtrar_info();
            }
        });
    }

    private void f_boton1(String ruta){
        File aux = new File(ruta);
        if (!aux.isDirectory()){
            ruta = aux.getAbsolutePath();
        }
        data = pillar_info(ruta);
        System.out.println("Length data = "+data.length);
        for (String[] i : data) {
            for (int j = 0; j < 4; j++) {
                System.out.println(i[j]);

            }
            modelo.addRow(data);
        }

        System.out.println();
    }

    private static void filtrar_info(){
    }

    private static String[][] pillar_info(String ruta){
        File dir = new File(ruta);
        File []v = dir.listFiles();

        String informacion[][] = new String[v.length][4];

        for (int i=0;i<v.length;i++){
            informacion [i][0] = v[i].getName();
            if (v[i].isDirectory()){
                informacion [i][1] = " ";
                informacion [i][3] = "Es un directorio.";
            }else{
                informacion [i][3] = "Es un archivo.";
                informacion [i][1] = " extension del archivo";
            }
            informacion [i][2] = Long.toString(v[i].getTotalSpace());
        }
        return informacion;
    }

    public static void main (String[] args){
        JFrame frame =new JFrame("Explorador de archivos.");
        frame.setDefaultLookAndFeelDecorated(true);
        frame.setExtendedState(MAXIMIZED_BOTH);
        frame.setContentPane(new Actividad_1().Panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void ver_tabla (JTable tabla){
        modelo = new DefaultTableModel(data,columnas);
        tabla.setModel(modelo);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        tabla.setModel(modelo);
    }
}
