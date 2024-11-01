public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        GestorCSV gestor = new GestorCSV() ;

        Tabla tabla = gestor.leer("TP_Implementacion/src/prueba.csv");

        gestor.escribirCSV(tabla, "TP_Implementacion/src");
    }
}
