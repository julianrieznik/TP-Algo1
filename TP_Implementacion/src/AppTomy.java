import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

public class AppTomy {
    public static void main(String[] args) throws Exception {
        
        GestorCSV gestor = new GestorCSV() ;
         
         
        Tabla<String, Integer> tabla = gestor.leer("TP_Implementacion/src/prueba.csv");

        List<String> filtro = new ArrayList<String>();
        List<Predicate<Celda>> criterios = new ArrayList<Predicate<Celda>>();
        

        filtro.add("Columna2");
        filtro.add("Columna3");

        //Predicate<Celda> pred1 = a -> a.igual(7);
        //Predicate<Celda> pred2 = a -> a.igual(2);

        criterios.add(new Filtrador().generadorPredicate(Operador.IGUAL, 7));
        criterios.add(new Filtrador().generadorPredicate(Operador.IGUAL, 2));
        
        Tabla<String, Integer> filtrada = tabla.filtrar(filtro,criterios,OperadorLogico.AND);
         
        gestor.escribirCSV(filtrada, "TP_Implementacion/src","Filtro");
    }

}

