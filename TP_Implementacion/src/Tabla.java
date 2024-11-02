import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import excepciones.FormatoTablaInvalido;
import excepciones.IndiceInexistente;

public class Tabla<K,F> implements interfaces.Copiable<Tabla<K,F>>,interfaces.Visualizable<Tabla<K,F>>, interfaces.Proyectable<Tabla<K,F>,Etiqueta<F>,Etiqueta<K>>, interfaces.Ordenable<Tabla<K,F>,Etiqueta<F>>{ 
    //GENERICS
    // K -> Etiqueta de Columna
    // F -> Etiqueta de fila
    // ? -> Columna
    
    private LinkedHashMap<Etiqueta<K>, Columna<?>> tabla;
    private List<Etiqueta<F>> etiquetas_fila;


// ----------------------------------------- CONSTRUCTORES ------------------------------------- 
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


// ----------------------------------------- GETTERS ------------------------------------- 
    public Collection<Columna<?>> getListaColumnas() {
        return tabla.values();
    }
    
    public List<Etiqueta<F>> getEtiquetas_fila() {
        return etiquetas_fila;
    }
    
    public List<Etiqueta<K>> getEtiquetas_columna() {
        return new ArrayList<>(tabla.keySet());
    }
    
    public LinkedHashMap<Etiqueta<K>, Columna<?>> getTabla() {
        return tabla;
    }

    public Integer getCantidadColumnas(){
        return getEtiquetas_columna().size();
    }
 
    public Integer getCantidadFilas(){
        Collection<Columna<?>> columnas = getListaColumnas();
        Iterator<Columna<?>> iterador = columnas.iterator();
        Columna<?> primer = iterador.hasNext() ? iterador.next() : null;

        return primer.cantidadCeldas();
    }

// ----------------------------------------- METODOS PRIVADOS INTERNOS ------------------------------------- 
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


// ----------------------------------------- INFORMACION DE TABLA ------------------------------------- 
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
    public Tabla<K, F> head(int n) {
        // TODO Auto-generated method stub
        return null;
    }

// ----------------------------------------- PROYECTABLE ------------------------------------- 
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
    
    @Override
    public String toString() {
        return "Tabla [tabla=" + tabla + ", etiquetas_fila=" + etiquetas_fila + "]";
    }


// ----------------------------------------- COPIABLE ------------------------------------- 
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

    // ----------------------------------------- VISUALIZABLE-------------------------------------    
    @Override
    public void ver(Integer cantidad_caracteres) {
        // IMPRESION DE ETIQUETAS DE COLUMNA
        StringBuilder sb = new StringBuilder();
        int cantidad_espacios_iniciales = etiquetaMasGrandeFila() + 5;
                sb.append(" ".repeat(cantidad_espacios_iniciales)); //Agrego espacios iniciales
                
                // Etiquetas de columnas con espacio definido por cantidad_caracteres
                for (Etiqueta<K> etiqueta : tabla.keySet()) {
                    String nombre_columna = String.valueOf(etiqueta.getNombre());
        
                    if (nombre_columna.length() > cantidad_caracteres){
                        nombre_columna = nombre_columna.substring(0, cantidad_caracteres); // Cortar si es necesario
                        sb.append(nombre_columna);
                        sb.append(".".repeat(3)); // 3 puntos ... p indicarnque fue cortada
                        sb.append(" ".repeat(2)); // 2 espacios de separacion
                    }
                    else if (nombre_columna.length() < cantidad_caracteres){
                        Integer diferencia = cantidad_caracteres - nombre_columna.length();
                        sb.append(nombre_columna);
                        sb.append(" ".repeat(diferencia + 5));

                    }
                    else {
                        sb.append(nombre_columna);
                        sb.append(" ".repeat(5));
                    }
                }
                System.out.println(sb.toString());
                
                // IMPRESION DE FILAS
                int cantidadFilas = getCantidadFilas();
                for (int i = 0; i < cantidadFilas; i++) {
                    verFila(i, cantidad_caracteres);
                }
            }
            
    private Integer etiquetaMasGrandeFila() {
        Integer max = 0;
        for (Etiqueta<F> etiqueta : etiquetas_fila){
            String nombre_etiqueta = String.valueOf(etiqueta.getNombre());
            int largo = nombre_etiqueta.length();
            if (largo > max){
                max = largo;
            }
        }
        return max;
    }
        
    @Override
    public void verFila(Integer indice_fila, Integer cantidad_caracteres) {
        if (indice_fila >= etiquetas_fila.size()){
            throw new IndiceInexistente("No existe la fila " + String.valueOf(indice_fila));
        }
        StringBuilder sb = new StringBuilder();
        String etiqueta = String.valueOf(etiquetas_fila.get(indice_fila).getNombre());
        
        // Agregar la etiqueta de la fila
        sb.append(etiqueta).append(" ".repeat(5)); // Agrego espacios iniciales dsp de etiqueta
    
        // Iterar sobre columnas para obtener el valor
        for (Columna<?> columna : this.tabla.values()) {
            String valor = String.valueOf(columna.valorCelda(indice_fila));

            if (valor.length() > cantidad_caracteres) {
                valor = valor.substring(0, cantidad_caracteres); // Cortar si es necesario
                sb.append(valor);
                sb.append(".".repeat(3)); // 3 puntos ... p indicarnque fue cortada
                sb.append(" ".repeat(2)); // 2 espacios de separacion
            }
            else if (valor.length() < cantidad_caracteres) {
                sb.append(valor);
                int diferencia = cantidad_caracteres  - valor.length();
                sb.append(" ".repeat(diferencia+5)); // Relleno con espacios lo que falta
            }
            else {
                sb.append(valor);
                sb.append(" ".repeat(5)); // Relleno con espacios lo que falta
            }

        }    
        // Imprimir la fila construida
        System.out.println(sb.toString());
    }

    @Override
    public void verFila(String etiqueta_fila, Integer cantidad_caracteres) {
        int indice = -1;
        // Iterar sobre la lista de etiquetas_fila
        for (int i = 0; i < etiquetas_fila.size(); i++) {
            // Obtener el nombre de la etiqueta
            String nombreEtiqueta = String.valueOf(etiquetas_fila.get(i).getNombre());
            
            // Comparar con la etiqueta buscada
            if (nombreEtiqueta.equals(etiqueta_fila)) {
                indice = i;
                break; 
            }
        }
    
        // Verificar si se encontró el índice
        if (indice == -1) {
            System.out.println("La etiqueta de fila '" + etiqueta_fila + "' no fue encontrada.");
            return;
        }
    
        // Llamar al método verFila con el índice encontrado
        verFila(indice, cantidad_caracteres);
    }
    @Override
    public void verColumna(Integer indice_columna) {
        if (indice_columna > this.tabla.size()){
            throw new IndiceInexistente("No existe la columna " + String.valueOf(indice_columna));
        }

        int contador = 0;
        for (Map.Entry<Etiqueta<K>, Columna<?>> entrada : tabla.entrySet()) {
            if (contador == indice_columna) {
                String etiqueta = String.valueOf(entrada.getKey().getNombre());
                Columna<?> columna = entrada.getValue();
                System.out.println(etiqueta);
                System.out.println(columna.toString());
                break;
            }
            contador++;
        }
        
    }
    @Override
    public void verColumna(String etiqueta_columna) {
    // Convertir la etiqueta a tipo Etiqueta<String>
    Etiqueta<String> etiquetaKey = new Etiqueta<>(etiqueta_columna);
        
    // Obtener la columna correspondiente
    Columna<?> columna = tabla.get(etiquetaKey);
        if (columna == null){
            throw new IndiceInexistente("No existe la columna " + etiqueta_columna);
        }
        System.out.println(etiqueta_columna);
        System.out.println(columna.toString());
        
    }
}

