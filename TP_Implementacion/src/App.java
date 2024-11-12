import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

public class App {
    public static void main(String[] args) throws Exception {
        /*
         * GestorCSV gestor = new GestorCSV() ;
         * 
         * 
         * Tabla<String, Integer> tabla =
         * gestor.leer("TP_Implementacion/src/prueba.csv");
         * System.out.println(tabla.getColumna(0).tipo());
         * 
         * gestor.escribirCSV(tabla, "TP_Implementacion/src","Output");
         * 
         * List<Etiqueta<String>> list_etiq_col = new ArrayList<Etiqueta<String>>();
         * List<Etiqueta<Integer>> list_etiq_fil = new ArrayList<Etiqueta<Integer>>();
         * list_etiq_col.add(new Etiqueta<String>("Columna2"));
         * list_etiq_col.add(new Etiqueta<String>("Columna3"));
         * list_etiq_fil.add(new Etiqueta<Integer>(1));
         * 
         * 
         * gestor.escribirCSV( tabla.subtabla(list_etiq_fil, list_etiq_col),
         * "TP_Implementacion/src","SubtablaTest");
         * gestor.escribirCSV( tabla.head(3), "TP_Implementacion/src","headTest");
         * gestor.escribirCSV( tabla.tail(2), "TP_Implementacion/src","TailTest");
         * 
         * List<Etiqueta<String>> list_etiq_col = new ArrayList<Etiqueta<String>>();
         * List<Etiqueta<String>> list_etiq_fil = new ArrayList<Etiqueta<String>>();
         * list_etiq_col.add(new Etiqueta<String>("Apellido"));
         * list_etiq_col.add(new Etiqueta<String>("Edad"));
         * list_etiq_fil.add(new Etiqueta<String>("Fila 0"));
         * list_etiq_fil.add(new Etiqueta<String>("Fila 2"));
  
         

         
        GestorCSV gestor = new GestorCSV() ;
         
         
        Tabla<String, Integer> tabla = gestor.leer("TP_Implementacion/src/prueba.csv");

        List<String> filtro = new ArrayList<String>();
        

        filtro.add("Columna2");
        filtro.add("Columna3");
        

        Tabla<String, Integer> filtrada = tabla.filtrar("Columna3", a -> a.equals(2) );
        //Tabla<String, Integer> filtrada = tabla.filtrar(filtro, [a,b] -> a.equals(1) && b.equals(2));
         
        gestor.escribirCSV(filtrada, "TP_Implementacion/src","Filtro");
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
        
        
        Tabla<String, String> nombres = new Tabla<>(etiquetasFilas,
        etiquetasColumnas, columnas);
        
        
        Object[] filaNueva = { "Pepe", "Luis", 22, 22};
        //nombres.agregarFila("Fila 4", filaNueva);


        Object[] columnaNueva = {true, false,true,true};
        nombres.agregarColumna("boolean", columnaNueva);
        nombres.ver(20, 20);
        //nombres.ver(20, 20);
        //nombres.cambiarTipoColumna("boolean", String.class);
        nombres.modificarCelda("boolean", "Fila 3", true);
        nombres.ver(20, 20);
        Double a = nombres.sum("Edad", true);
        System.out.println(a);
         
         /* 
         nombres.modificarCelda("Numero", "Fila 2", 22);
         nombres.ver(20, 20);

         nombres.verColumna("Numero");
         nombres.cambiarTipoColumna("Numero", Double.class);
         nombres.verColumna("Numero");
         nombres.cambiarTipoColumna("Numero", String.class);
         nombres.verColumna("Numero");
         nombres.cambiarTipoColumna("Numero", Boolean.class);
         */

            // Etiquetas de filas
    String[] etiquetasA = { "Fila 0", "Fila 1", "Fila 2", "Fila 3" };

    // Etiquetas de columnas
    String[] etiquetasB = { "Nombree", "Apellidoo", "Edadd", "nNumero" };

    // Datos de columnas
    Object[][] columnasS = {
        { "Ana", "Carlos", "Lucía", "Ricardo" },
        { "Gomez", "López", "Martínez", "Fernández" },
        { 27, 35, 29, 42 },
        { 5, 2, 6, 3 },
    };

    // Creación de la tabla
    Tabla<String, String> nombres2 = new Tabla<>(etiquetasB, columnasS);
    Tabla<String, String> concat = Tabla.concatenar(nombres,nombres2);
    concat.ver(7, 10);

    }

}

