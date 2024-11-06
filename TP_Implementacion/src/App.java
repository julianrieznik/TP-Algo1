import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
          /*
        GestorCSV gestor = new GestorCSV() ;

        
        Tabla<String, Integer> tabla = gestor.leer("TP_Implementacion/src/prueba.csv");
        //gestor.escribirCSV(tabla, "TP_Implementacion/src");

        List<Etiqueta<String>> list_etiq_col = new ArrayList<Etiqueta<String>>();
        List<Etiqueta<Integer>> list_etiq_fil = new ArrayList<Etiqueta<Integer>>();
        list_etiq_col.add(new Etiqueta<String>("Columna2"));
        list_etiq_col.add(new Etiqueta<String>("Columna3"));
        list_etiq_fil.add(new Etiqueta<Integer>(1));


        gestor.escribirCSV( tabla.subtabla(list_etiq_fil, list_etiq_col), "TP_Implementacion/src","SubtablaTest");
        gestor.escribirCSV( tabla.head(3), "TP_Implementacion/src","headTest");
        gestor.escribirCSV( tabla.tail(2), "TP_Implementacion/src","TailTest");

          */
      

        // Etiquetas de filas
        String[] etiquetasFilas = { "Fila 0", "Fila 1", "Fila 2", "Fila 3" };
        
        // Etiquetas de columnas
        String[] etiquetasColumnas = { "Nombre", "Apellido", "Edad", "Numero" };
        
        // Datos de columnas
        Object[][] columnas = {
            { "Julian", "Pedro", "Maria", "Leandro" },
            { "Perez", "Sanchez", "Rodriguez", "Gutierrez" },
            {25,44,33,21},
            {2,4,3,1},
        };
       

        Tabla<String, String> nombres = new Tabla<>(etiquetasFilas, etiquetasColumnas, columnas);
        List<Etiqueta<String>> list_etiq_col = new ArrayList<Etiqueta<String>>();
        List<Etiqueta<String>> list_etiq_fil = new ArrayList<Etiqueta<String>>();
        list_etiq_col.add(new Etiqueta<String>("Apellido"));
        list_etiq_col.add(new Etiqueta<String>("Edad"));
        list_etiq_fil.add(new Etiqueta<String>("Fila 0"));
        list_etiq_fil.add(new Etiqueta<String>("Fila 2"));
        
       // gestor.escribirCSV(nombres.subtabla(list_etiq_fil,list_etiq_col ), "TP_Implementacion/src");

        
        
       
        nombres.verFila("Fila 2", 3);
        nombres.verColumna("Nombre");

        Tabla<String, String> copianombres = new Tabla<>();
        copianombres.copiar(nombres);
        //nombres.ver(20,2);
        copianombres.ver(20,4);
        copianombres.eliminarColumna("Nombre");
        System.out.println();
        copianombres.ver(20,2);
        copianombres.eliminarFila(1);
        System.out.println();
        copianombres.ver(20,2);
        nombres.ver(20, 20);

        Object[] filaNueva = { "Pepe", "Luis", 22.0, 22};
        nombres.agregarFila(filaNueva);

        nombres.ver(20, 20);
        Object[] columnaNueva = {true, false,true,2,true};
        nombres.agregarColumna("boolean", columnaNueva);
        nombres.ver(20, 20);

    }
}
