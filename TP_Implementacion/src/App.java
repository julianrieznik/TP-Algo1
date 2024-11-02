public class App {
    public static void main(String[] args) throws Exception {
        GestorCSV gestor = new GestorCSV() ;

        Tabla tabla = gestor.leer("TP_Implementacion/src/prueba.csv");
        tabla.ver();

        //gestor.escribirCSV(tabla, "TP_Implementacion/src");
    }
}
