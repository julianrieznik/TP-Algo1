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
        
        Tabla<String,String> noRetirados = tabla.filtrar("Retirado?",new Filtrador().generadorPredicate(Operador.IGUAL,"Noo")).groupbyTabla(grupos, MetodoAgregacion.Media, false).ordenar(orden, false);
        // "Noo"
        noRetirados.ver(10, 100);
        System.out.println("\n");
        System.out.println("Rellenamos NA de Peso y mostramos top 10 \n");
        noRetirados.rellenarNA("Peso", 77.2);
        noRetirados.head(10).ver(10,100);;
        System.out.println("\n");
        
        
        
        List<Etiqueta<String>> visualizacionCol = new ArrayList<Etiqueta<String>>();
        List<Etiqueta<String>> visualizacionFil = new ArrayList<Etiqueta<String>>();
        visualizacionCol.add(new Etiqueta<String>("Altura"));
        visualizacionCol.add(new Etiqueta<String>("Edad"));
        visualizacionFil.add(new Etiqueta<String>("Fútbol,F"));
        visualizacionFil.add(new Etiqueta<String>("Gimnasia,M"));

        Tabla<String, String> seleccion = noRetirados.subtabla(visualizacionFil, visualizacionCol);
        System.out.println("Hacemos un slice: \n");
        seleccion.ver(10, 100);
        
        gestor.escribirCSV(seleccion,"TP_Implementacion/src","Resultante");

        System.out.println("Generamos otra instancia con copia profunda: \n");
        Tabla<String, String> copia = seleccion.clone();
        copia.ver(10, 100);

        
        /*  Extra  
        Columna<String> nombres = tabla.obtenerColumna("Nombr"); //Nombre
        String messi = nombres.valorCelda(2);

        System.out.println(messi);
        
        System.out.println("\n");
        tabla.verFila(200, 10); //indice 20

        Object[] fila_nueva = {"Fernando", "Gago", "Argentina", "M" , 38, 1.78, 84.4, "Futbol"};
        //Falta "Retirado" despues del peso
        tabla.cambiarTipoColumna("Peso", Boolean.class);
        //tabla.cambiarTipoColumna("Peso", Double.class);
        tabla.agregarFila(fila_nueva);

        tabla.tail(1).ver(10,10);
        
        */

    }

}

