import java.util.ArrayList;
import java.util.List;

public class AppTomy {
    public static void main(String[] args) throws Exception {
        
        GestorCSV gestor = new GestorCSV() ;
         
        //---------------- INICIALIZACIÓN TABLA ----------------------
        Tabla<String, Integer> tabla = gestor.leer("TP_Implementacion/src/prueba.csv");
        
        System.out.println("Tabla Original:");
        tabla.ver(10,10);
        System.out.println("\n");
        

        System.out.println("Resumen: ");
        Tabla<String, String> resumen = tabla.resumen(true);
        resumen.ver(8, 3);
        System.out.println("\n");
        
        //---------------- FILTRO Y ORDEN----------------------


        List<String> grupos = new ArrayList<String>();
        grupos.add("Disciplina");
        grupos.add("Genero");

        List<String> orden = new ArrayList<String>();
        orden.add("Altura");
        
        System.out.println("Tabla Agrupada por : " + grupos );
        
        Tabla<String,String> noRetirados = tabla.filtrar("Retirado?",new Filtrador().generadorPredicate(Operador.IGUAL,"No")).groupbyTabla(grupos, MetodoAgregacion.Media, false).ordenar(orden, false);
        
        noRetirados.ver(10, 10);
        System.out.println("\n");
        noRetirados.rellenarNA("Peso", 77);
        noRetirados.ver(10, 3);
        System.out.println("\n");
        
        
        
        List<Etiqueta<String>> visualizacion = new ArrayList<Etiqueta<String>>();
        visualizacion.add(new Etiqueta<String>("Altura"));
        visualizacion.add(new Etiqueta<String>("Edad"));

        Tabla<String, String> seleccion = noRetirados.subtabla(null, visualizacion);
        seleccion.ver(10, 5);
        
        gestor.escribirCSV(seleccion,"TP_Implementacion/src","Resultante");

        
        /*  Extra 
        Columna<String> nombres = tabla.obtenerColumna("Nombre");
        String messi = nombres.valorCelda(2);

        System.out.println(messi);
        
        System.out.println("\n");
        tabla.verFila(20, 10);

        Object[] fila_nueva = {"Fernando", "Gago", "Argentina", "M" , 38, 1.78, 84.4,"Retirado", "Futbol"};
        //Falta "Retirado" despues del peso
        //tabla.cambiarTipoColumna("Peso", Double.class);
        tabla.agregarFila(fila_nueva);

        tabla.tail(1).ver(10,10);
        */
 

    }

}

