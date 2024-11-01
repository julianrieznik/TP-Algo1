import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import excepciones.FormatoTablaInvalido;

public class Tabla<K,F> implements interfaces.Copiable<Tabla<K,F>>, interfaces.Proyectable<Tabla<K,F>,Etiqueta<F>,Etiqueta<K>>, interfaces.Ordenable<Tabla<K,F>,Etiqueta<F>>{ 
    //GENERICS
    // K -> Etiqueta de Columna
    // F -> Etiqueta de fila
    // ? -> Columna
    
    private Map<Etiqueta<K>, Columna<?>> tabla;
    private List<Etiqueta<F>> etiquetas_fila;

    public Tabla() {
        tabla = new LinkedHashMap<>();
        etiquetas_fila = new ArrayList<>();
    }

    //Constructor sin etiqueas de filas, numeradas
    public Tabla(K[] etiquetaColumnas, Object[][] columnas) {
        this();

        if (!chequearLargoDeFilas(etiquetaColumnas, columnas)) {
            //HACER EXCEPCION PROPIA
            throw new FormatoTablaInvalido("La cantidad de etiquetas de columnas debe coincidir con el largo de las filas.");
        }
        if (!chequearLargoDeColumnas(columnas)) {
            //HACER EXCEPCION PROPIA
            throw new FormatoTablaInvalido("El largo de las columnas debe ser el mismo para todas las columnas.");
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
            Columna<?> columna = castearColumna(columnas[i]);
            
            // Agregar la columna a la tabla
            tabla.put(etiquetaColumna, columna);
        }
    }

    // Constructor con etiquetas de filas personalizadas
    public Tabla(F[] etiquetaFilas, K[] etiquetaColumnas, Object[][] columnas) {
        this();

        if (!chequearLargoDeFilas(etiquetaColumnas, columnas)) {
            //HACER EXCEPCION PROPIA
            throw new FormatoTablaInvalido("La cantidad de etiquetas de columnas debe coincidir con el largo de las filas.");
        }
        if (!chequearLargoDeColumnas(columnas)) {
            //HACER EXCEPCION PROPIA
            throw new FormatoTablaInvalido("El largo de las columnas debe ser el mismo para todas las columnas.");
        }

        // Inicializar etiquetas de filas
        for (F elemento : etiquetaFilas) {
            Etiqueta<F> etiqueta = new Etiqueta<>(elemento);
            etiquetas_fila.add(etiqueta);
        }

        // Inicializar columnas asociadas a etiquetas de columnas
        for (int i = 0; i < etiquetaColumnas.length; i++) {
            Etiqueta<K> etiquetaColumna = new Etiqueta<>(etiquetaColumnas[i]);
            
            Columna<?> columna = castearColumna(columnas[i]);
            
            tabla.put(etiquetaColumna, columna); // Agregar la columna casteada
        }
        
    }


    private boolean chequearLargoDeColumnas(Object[][] columnas) {
        int largoColumna = columnas[0].length;
        for(Object[] col : columnas){
            if (col.length != largoColumna) {
                return false;
            }
        }
        return true;
    }

    private boolean chequearLargoDeFilas(K[] etiquetaColumnas, Object[][] columnas) {
        return etiquetaColumnas.length == columnas.length;
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
    public Tabla<K, F> copiar(Tabla<K, F> a_copiar) {
        Tabla<K, F> copia = new Tabla<>();
    
        // Copiar Etiquetas de fila
        for (Etiqueta<F> etiquetaFila : a_copiar.etiquetas_filas()) {
            copia.etiquetas_fila.add(new Etiqueta<>(etiquetaFila.nombre));
        }
    
        // Iterar sobre la tabla
        for (Map.Entry<Etiqueta<K>, Columna<?>> entrada : a_copiar.tabla.entrySet()) {
            // Copiar Etiquetas de columna
            Etiqueta<K> etiquetaColumna = new Etiqueta<>(entrada.getKey().nombre);
    
            // Inferir tipo de dato y crear columna copia
            Columna<?> columnaOriginal = entrada.getValue();
            Columna<?> columnaCopia = castearColumna(columnaOriginal.obtenerValores().toArray());
    
            // Agregar la columna copiada a la nueva tabla
            copia.tabla.put(etiquetaColumna, columnaCopia);
        }
    
        return copia;
    }

    private boolean esNumerica(Object[] columna) {
        for (Object elemento : columna) {
            try {
                // Casteo a number
                Number numero = (Number) elemento; 
            } catch (ClassCastException e) {
                // Si hay excepcion, no es Number, devuelvo false
                return false;
            }
        }
        // Si todos los elementos son convertibles a Number, devolvemos true
        return true;
    }

    private boolean esBooleana(Object[] columna) {
        for (Object elemento : columna) {
            try {
                // Casteo a number
                Boolean bool = (Boolean) elemento; 
            } catch (ClassCastException e) {
                // Si hay excepcion, no es Boolean, devuelvo false
                return false;
            }
        }
        // Si todos los elementos son convertibles a Boolean, devolvemos true
        return true;
    }

    private Columna<?> castearColumna(Object[] lista) {
        if (esNumerica(lista)) {
            Number[] listaCasteada = new Number[lista.length]; 
            for (int j = 0; j < lista.length; j++) {
                listaCasteada[j] = (Number) lista[j]; // Castear cada elemento
            }
            // Retornar la columna casteada a Number
            return new Columna<>(listaCasteada);
        }
    
        if (esBooleana(lista)) {
            Boolean[] listaCasteada = new Boolean[lista.length]; 
            for (int j = 0; j < lista.length; j++) {
                listaCasteada[j] = (Boolean) lista[j]; 
            }
            // Retornar la columna casteada a Boolean
            return new Columna<>(listaCasteada);
        }
    
        // Si no es numérica ni booleana, asumimos que es String
        String[] listaCasteada = new String[lista.length]; 
        for (int j = 0; j < lista.length; j++) {
            listaCasteada[j] = (String) lista[j]; 
        }
        // Retornar la columna casteada a String
        return new Columna<>(listaCasteada);
    }

    @Override
    public Tabla<K, F> head(int n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Tabla<K, F> subtabla(List<Etiqueta<K>> listFilas, List<Etiqueta<F>> listColumnas) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Tabla<K, F> subtabla(List<Etiqueta<F>> list, boolean FoC) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Tabla<K, F> tail(int n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Tabla<K, F> ordenar(List<Etiqueta<F>> lista, boolean asc_desc) {
        // TODO Auto-generated method stub
        return null;
    }

}

