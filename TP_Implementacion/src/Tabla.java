import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tabla{
    private Map<Etiqueta, Columna> tabla;
    private List<Etiqueta> etiquetas_fila;

    public Tabla() {
        tabla = new LinkedHashMap<>();
        etiquetas_fila = new ArrayList<>();
    }
    public <E> Tabla(E[] etiquetaFilas, E[] etiquetaColumnas, E[][] columnas) {
        this();
    

        for (E elemento : etiquetaFilas){
            Etiqueta<E> etiqueta = new Etiqueta<>(elemento);
            etiquetas_fila.add(etiqueta);
        }
    
        // Inicializar columnas asociadas a etiquetas de columnas
        for (int i = 0; i < etiquetaColumnas.length; i++) {
            Etiqueta<E> etiquetaColumna = new Etiqueta<>(etiquetaColumnas[i]);
            
            // Crear  Columna para la etiqueta actual
            Columna<E> columna = new Columna<>(columnas[i]);
            
            // Agregar la columna a la tabla
            tabla.put(etiquetaColumna, columna);
        }
    }
}
