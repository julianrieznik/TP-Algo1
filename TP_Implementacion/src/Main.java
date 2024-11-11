import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Etiquetas de filas
        String[] etiquetasFilas = { "Fila 0", "Fila 1", "Fila 2", "Fila 3", "FIla 4"};
        
        // Etiquetas de columnas
        String[] etiquetasColumnas = { "Nombre", "Apellido", "Edad", "Numero" };
        
        // Datos de columnas
        Object[][] columnas = {
            { "Julian", "Pedro", null, "Leandro", null},
            { "Perez", "Sanchez", "Rodriguez", "Gutierrez", null},
            {25,44,33,21, null},
            {2,4,3,1, null}
            //{null, null, null, null, null}
        };
        
         
        Tabla<String, String> nombres = new Tabla<>(etiquetasFilas, etiquetasColumnas, columnas);
        nombres.ver(20, 20);
        List<String> etiquetasOrden = new ArrayList<>();
        etiquetasOrden.add("Nombre");
        //etiquetasOrden.add("Apellido");
        Tabla<String, String> nueva = nombres.ordenar(etiquetasOrden, true);
        nueva.ver(20,20);
    }
}
