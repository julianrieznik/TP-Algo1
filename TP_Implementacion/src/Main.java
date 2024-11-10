import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Etiquetas de filas
        String[] etiquetasFilas = { "Fila 0", "Fila 1", "Fila 2", "Fila 3" };
        
        // Etiquetas de columnas
        String[] etiquetasColumnas = { "Nombre", "Apellido", "Edad", "Numero" };
        
        // Datos de columnas
        Object[][] columnas = {
            { "Julian", "Pedro", null, "Leandro"},
            { "Perez", "Sanchez", "Rodriguez", "Gutierrez"},
            {25,44,33,21},
            {2,4,3,1}
        };
        
         
        Tabla<String, String> nombres = new Tabla<>(etiquetasFilas, etiquetasColumnas, columnas);
        nombres.ver(20, 20);
        List<String> etiquetasOrden = new ArrayList<>();
        etiquetasOrden.add("Nombre");
        Tabla<String, String> nueva = nombres.ordenar(etiquetasOrden, false);
        nueva.ver(20,20);
    }
}
