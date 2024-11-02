public class App {
    public static void main(String[] args) throws Exception {
        GestorCSV gestor = new GestorCSV() ;

       //  Tabla<Integer, String> tabla = gestor.leer("TP_Implementacion/src/prueba.csv");
        // tabla.verFila(1,8);

        //gestor.escribirCSV(tabla, "TP_Implementacion/src");

        // Etiquetas de filas
        String[] etiquetasFilas = { "Fila 1", "Fila 2", "Fila 3", "Fila 4" };
        
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
        nombres.ver(20);
        nombres.verFila("Fila 2", 3);
        nombres.verColumna("Nombre");

    }
}
