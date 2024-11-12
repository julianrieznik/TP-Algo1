import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AppTomy {
    public static void main(String[] args) throws Exception {
        
        GestorCSV gestor = new GestorCSV() ;
         
         
        Tabla<String, Integer> tabla = gestor.leer("TP_Implementacion/src/prueba.csv");
        
        System.out.println("Tabla Original:");
        tabla.ver(10,10);
        System.out.println("\n");

        List<String> grupos = new ArrayList<String>();
        grupos.add("Disciplina");
        grupos.add("Genero");

        List<String> orden = new ArrayList<String>();
        orden.add("Altura");
        
        System.out.println("Tabla Agrupada por : " + grupos );
        
        Tabla<String,String> resultante = tabla.filtrar("Retirado?",new Filtrador().generadorPredicate(Operador.IGUAL,"No")).groupbyTabla(grupos, MetodoAgregacion.Media, false).ordenar(orden, false);
        resultante.ver(10, 100);

        Tabla<String,String> resultanteSinNA = tabla.filtrar("Retirado?",new Filtrador().generadorPredicate(Operador.IGUAL,"No")).groupbyTabla(grupos, MetodoAgregacion.Media, true); //.ordenar(orden, false)
        resultanteSinNA.ver(10, 100);

        
        List<Etiqueta<String>> visualizacion = new ArrayList<Etiqueta<String>>();
        visualizacion.add(new Etiqueta<String>("Altura"));
        visualizacion.add(new Etiqueta<String>("Edad"));

        resultanteSinNA = resultanteSinNA.subtabla(null, visualizacion);
        gestor.escribirCSV(resultanteSinNA,"TP_Implementacion/src","Resultante");

        resultanteSinNA.ver(10, 100);
        
        

    }

}

