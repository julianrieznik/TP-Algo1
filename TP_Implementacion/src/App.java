public class App {
    public static void main(String[] args) throws Exception {
       // GestorCSV gestor = new GestorCSV() ;

        //Tabla<Integer, String> tabla = gestor.leer("TP_Implementacion/src/prueba.csv");
        //tabla.verFila(1,8);

        //gestor.escribirCSV(tabla, "TP_Implementacion/src");

        

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

        Tabla<String, String> nombres = new Tabla<>(etiquetasColumnas, columnas);
        nombres.ver(20, 20);

        Object[] filaNueva = { "Pepe", "Luis", 22.0, 22};
        nombres.agregarFila(filaNueva);

        nombres.ver(20, 20);
        Object[] columnaNueva = {true, false,true,2,true};
        nombres.agregarColumna("boolean", columnaNueva);
        nombres.ver(20, 20);
    }
}
