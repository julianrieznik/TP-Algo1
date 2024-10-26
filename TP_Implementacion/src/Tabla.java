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

    //Constructor sin etiqueas de filas, numeradas
    public <E> Tabla(E[] etiquetaColumnas, E[][] columnas) {
        this();

        if (etiquetaColumnas.length != columnas.length) {
            //HACER EXCEPCION PROPIA
            throw new IllegalArgumentException("La cantidad de etiquetas de columnas debe coincidir con la cantidad de columnas.");
        }
        
        //Inicializar etiquetas numeradas
        for (int i = 0; i < columnas[0].length; i++){
            Etiqueta<Integer> etiqueta = new Etiqueta<>(i);
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

    //Constructor con etiquetas de filas personalizadas
    public <E> Tabla(E[] etiquetaFilas, E[] etiquetaColumnas, E[][] columnas) {
        this();

        if (!chequearLargoDeColumnas(columnas)) {
            //HACER EXCEPCION PROPIA
            throw new IllegalArgumentException("La cantidad de etiquetas de columnas debe coincidir con la cantidad de columnas.");
        }

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

    private <E> boolean chequearLargoDeColumnas(E[][] columnas) {
        int largo = columnas[0].length;
        for(E[] col : columnas){
            if (col.length != largo) {
                return false;
            }
        }
        return true;
    }

    public Integer nfilas(){
        return etiquetas_fila.size();
    }

    public Integer ncols(){
        return tabla.size();
    }

    public List<Etiqueta> etiquetas_filas(){
        return etiquetas_fila;
    }

    public <E> List<Etiqueta<E>> etiquetas_columnas() {
        return new ArrayList<>(tabla.keySet());
    }

}
