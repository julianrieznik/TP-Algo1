import java.util.ArrayList;
import java.util.List;

public class AppTomy {
    public static void main(String[] args) throws Exception {
        
        GestorCSV gestor = new GestorCSV() ;
         
         
        Tabla<String, Integer> tabla = gestor.leer("TP_Implementacion/src/prueba.csv");
        System.out.println("Tabla Original:");
        tabla.ver(10,100);
        System.out.println("\n");

        List<String> grupos = new ArrayList<String>();
        grupos.add("Pais");
        System.out.println("Tabla Agrupada por : " + grupos );
        tabla.groupbyTabla(grupos, MetodoAgregacion.Max,false).ver(20,100);
        System.out.println("\n");
        
        grupos.add("Genero");
        grupos.add("Retirado?");
        System.out.println("Tabla Agrupada por : " + grupos );
        tabla.groupbyTabla(grupos, MetodoAgregacion.Max,false).ver(20,100);

        tabla.resumen(true).ver(10,20);
         


    }

}

