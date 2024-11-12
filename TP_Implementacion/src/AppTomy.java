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
        tabla.groupbyTabla(grupos, MetodoAgregacion.Max,false).ver(10,100);
        System.out.println("\n");
        
        grupos.add("Genero");
        System.out.println("Tabla Agrupada por : " + grupos );
        Tabla<String,String> agrupada = tabla.groupbyTabla(grupos, MetodoAgregacion.DesvioEstandar,true);
        agrupada.ver(10, 100);

         


    }

}

