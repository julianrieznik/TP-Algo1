import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

public class AppTomy {
    public static void main(String[] args) throws Exception {

        //--------------INICIALIZACION CON ARREGLOS NATIVOS DE JAVA----------------------------

        String[] etiquetasFilas = { "Fila 0", "Fila 1", "Fila 2", "Fila 3" };
        String[] etiquetasColumnas = { "Nombre", "Apellido", "Edad", "Numero" };
        Object[][] columnas = {
        { "Julian", "Pedro", "Maria", "Leandro" },
        { "Perez", "Sanchez", null, "Gutierrez" },
        {25,44,null,21},
        {2,4,3,1},
        };
        
        
        Tabla<String, String> personas = new Tabla<>(etiquetasFilas,
        etiquetasColumnas, columnas);

   
        Tabla<String, String> resumenPersonas = personas.resumen(true);
        resumenPersonas.ver(20, 20);
        /*
        personas.ver(20, 20);
        
        Object[] filaNueva = { "Leon", "Martinez", null, 22};
        personas.agregarFila("Fila nueva", filaNueva);
        personas.ver(20, 20);
        System.out.println(personas.max("Edad", true));
        System.out.println(personas.min("Edad", true));
        System.out.println(personas.sum("Edad", true));
        System.out.println(personas.promedio("Edad", true));


        Object[] columnaNueva = {true, false,true,true,false};
        personas.agregarColumna("Aprobado", columnaNueva);

        personas.ver(20, 20);

        Tabla<String, String> copiaPersonas = new Tabla<>();
        copiaPersonas.copiar(personas);
        
        copiaPersonas.verFila("Fila nueva", 10);
        copiaPersonas.eliminarFila(4);
        copiaPersonas.verColumna("Nombre");

        //----------------------INICIALIZACION DE TABLA DESDE CSV-----------------------------------
        GestorCSV gestor = new GestorCSV() ;
         
         
        Tabla<String, Integer> tabla = gestor.leer("TP_Implementacion/src/prueba.csv");

        List<String> filtro = new ArrayList<String>();
        List<Predicate<Celda>> criterios = new ArrayList<Predicate<Celda>>();
        

        filtro.add("y");
        filtro.add("z");

        //Predicate<Celda> pred1 = a -> a.igual(7);
        //Predicate<Celda> pred2 = a -> a.igual(2);

        criterios.add(Filtrador.generadorPredicate(Operador.MAYOR, 5));
        criterios.add(Filtrador.generadorPredicate(Operador.IGUAL, 2));
        
        //Tabla<String, Integer> filtrada = tabla.filtrar(filtro,criterios,OperadorLogico.AND);
         
        //gestor.escribirCSV(filtrada, "TP_Implementacion/src","Filtro");
        gestor.escribirCSV(tabla, "TP_Implementacion/src","Output");



        tabla.ver(20,100);
        tabla.rellenarNA("y", 3);
        tabla.ver(20,100);


        //CONCATENACION

        Tabla concat = Tabla.concatenar(personas, tabla);
        concat.ver(7, 20);


        
    }
        */
    }
}

