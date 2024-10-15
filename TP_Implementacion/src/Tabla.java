import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public <E> Tabla(E[] etiquetafilas, E[] etiquetacolumnas, E[][] columnas) {
        this();
        for (E elemento : etiquetafilas){
            Etiqueta<E> etiqueta = new Etiqueta(elemento);
            etiquetas_fila.add(etiqueta);
        }
    }

}
