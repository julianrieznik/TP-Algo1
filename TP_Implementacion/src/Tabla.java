import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import excepciones.FormatoTablaInvalido;

public class Tabla<K,F,C> implements interfaces.Copiable<Tabla<K,F,C>>{ 
    //GENERICS
    // K -> Etiqueta de Columna
    // F -> Etiqueta de fila
    // C -> Columna
    
    private Map<Etiqueta<K>, Columna<C>> tabla;
    private List<Etiqueta<F>> etiquetas_fila;

    public Tabla() {
        tabla = new LinkedHashMap<>();
        etiquetas_fila = new ArrayList<>();
    }

    //Constructor sin etiqueas de filas, numeradas
    public Tabla(K[] etiquetaColumnas, C[][] columnas) {
        this();

        if (!chequearLargoDeColumnas(etiquetaColumnas, columnas)) {
            //HACER EXCEPCION PROPIA
            throw new FormatoTablaInvalido("La cantidad de etiquetas de columnas debe coincidir con la cantidad de columnas.");
        }
        
        //Inicializar etiquetas numeradas
        for (Integer i = 0; i < columnas[0].length; i++){
            Etiqueta<Integer> etiqueta = new Etiqueta<>(i);
            //No chequeo pq son etiquetas de fila, no se van a usar para nada mas que para llamar filas.
            etiquetas_fila.add((Etiqueta<F>) etiqueta);
        }


    
        // Inicializar columnas asociadas a etiquetas de columnas
        for (int i = 0; i < etiquetaColumnas.length; i++) {
            Etiqueta<K> etiquetaColumna = new Etiqueta<>(etiquetaColumnas[i]);
            
            // Crear  Columna para la etiqueta actual
            Columna<C> columna = new Columna<>(columnas[i]);
            
            // Agregar la columna a la tabla
            tabla.put(etiquetaColumna, columna);
        }
    }

    //Constructor con etiquetas de filas personalizadas
    public Tabla(F[] etiquetaFilas, K[] etiquetaColumnas, C[][] columnas) {
        this();

        if (!chequearLargoDeColumnas(etiquetaColumnas, columnas)) {

            throw new FormatoTablaInvalido("La cantidad de etiquetas de columnas debe coincidir con la cantidad de columnas.");
        }

        for (F elemento : etiquetaFilas){
            Etiqueta<F> etiqueta = new Etiqueta<>(elemento);
            etiquetas_fila.add(etiqueta);
        }
    
        // Inicializar columnas asociadas a etiquetas de columnas
        for (int i = 0; i < etiquetaColumnas.length; i++) {
            Etiqueta<K> etiquetaColumna = new Etiqueta<>(etiquetaColumnas[i]);
            
            // Crear  Columna para la etiqueta actual
            Columna<C> columna = new Columna<>(columnas[i]);
            
            // Agregar la columna a la tabla
            tabla.put(etiquetaColumna, columna);
        }
    }

    private boolean chequearLargoDeColumnas(K[] etiquetaColumnas, C[][] columnas) {
        int cantidadEtiquetas = etiquetaColumnas.length;
        for(C[] col : columnas){
            if (col.length != cantidadEtiquetas) {
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

    public List<Etiqueta<F>> etiquetas_filas(){
        return etiquetas_fila;
    }

    public List<Etiqueta<K>> etiquetas_columnas() {
        return new ArrayList<>(tabla.keySet());
    }

    @Override
    public Tabla<K, F, C> copiar(Tabla<K,F,C> a_copiar) {
        Tabla<K,F,C> copia = new Tabla<>();

        // Copiar Etiquetas de fila
        for (Etiqueta<F> etiquetaFila : a_copiar.etiquetas_filas()) {
            copia.etiquetas_fila.add(new Etiqueta<>(etiquetaFila.nombre));
    }
        // Itero sobre la tabla
        for (Map.Entry<Etiqueta<K>, Columna<C>> entrada : a_copiar.tabla.entrySet()) {
            //Copio Etiquetas de columna
            Etiqueta<K> etiquetaColumna = new Etiqueta<>(entrada.getKey().nombre);

        // 
        Columna<C> columnaOriginal = entrada.getValue();
        List<Celda<C>> celdas = columnaOriginal.obtenerValores();
        Columna<C> columnaCopia = new Columna<>(celdas);

        copia.tabla.put(etiquetaColumna, columnaCopia);
    }

        return copia;
    }

}