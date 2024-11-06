import excepciones.ColumnaInvalida;
import excepciones.EtiquetaInvalida;
import excepciones.FilaInvalida;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import excepciones.FormatoTablaInvalido;
import excepciones.IndiceInexistente;
import excepciones.ValorNoAgregable;

public class Tabla<K,F> implements interfaces.Agregable<Tabla<K,F>>, interfaces.Copiable<Tabla<K,F>>,interfaces.Visualizable<Tabla<K,F>>, interfaces.Proyectable<Tabla<K,F>,Etiqueta<K>,Etiqueta<F>>, interfaces.Ordenable<Tabla<K,F>,Etiqueta<F>>{ 
    //GENERICS
    // K -> Etiqueta de Columna
    // F -> Etiqueta de fila
    // ? -> Columna

    private LinkedHashMap<Etiqueta<K>, Columna<?>> tabla;
    private List<Etiqueta<F>> etiquetas_fila;

    // ----------------------------------------- CONSTRUCTORES
    // -------------------------------------
    public Tabla() {
        tabla = new LinkedHashMap<>();
        etiquetas_fila = new ArrayList<>();
    }

    // Constructor sin etiqueas de filas, numeradas
    public Tabla(K[] etiquetaColumnas, Object[][] columnas) {
        this();

        if (!chequearLargoDeFilas(etiquetaColumnas, columnas)) {
            // HACER EXCEPCION PROPIA
            throw new FormatoTablaInvalido(
                    "La cantidad de etiquetas de columnas debe coincidir con el largo de las filas.");
        }
        if (!chequearLargoDeColumnas(columnas)) {
            // HACER EXCEPCION PROPIA
            throw new FormatoTablaInvalido("El largo de las columnas debe ser el mismo para todas las columnas.");
        }

        // Inicializar etiquetas numeradas
        for (Integer i = 0; i < columnas[0].length; i++) {
            Etiqueta<Integer> etiqueta = new Etiqueta<>(i);
            // No chequeo pq son etiquetas de fila, no se van a usar para nada mas que para
            // llamar filas.
            etiquetas_fila.add((Etiqueta<F>) etiqueta);
        }

        // Inicializar columnas asociadas a etiquetas de columnas
        for (int i = 0; i < etiquetaColumnas.length; i++) {
            Etiqueta<K> etiquetaColumna = new Etiqueta<>(etiquetaColumnas[i]);

            // Crear Columna para la etiqueta actual
            Columna<?> columna = castearColumna(columnas[i]);

            // Agregar la columna a la tabla
            tabla.put(etiquetaColumna, columna);
        }
    }

    // Constructor con etiquetas de filas personalizadas
    public Tabla(F[] etiquetaFilas, K[] etiquetaColumnas, Object[][] columnas) {
        this();

        if (!chequearLargoDeFilas(etiquetaColumnas, columnas)) {
            // HACER EXCEPCION PROPIA
            throw new FormatoTablaInvalido(
                    "La cantidad de etiquetas de columnas debe coincidir con el largo de las filas.");
        }
        if (!chequearLargoDeColumnas(columnas)) {
            // HACER EXCEPCION PROPIA
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

    //Constructor con lista de etiqFilas, etiqColumnas y lista de Columnas. Uso interno
    public Tabla(List<Etiqueta<F>> etiquetasFilas, List<Etiqueta<K>> etiquetasColumnas, List<Columna<?>> listColumnas) {
        this();

        if (etiquetasColumnas.size() != listColumnas.size())
            throw new FormatoTablaInvalido(
                    "La cantidad de etiquetas de columnas debe coincidir con el largo de las filas.");
        if (!chequearLargoDeColumnas(listColumnas))
            throw new FormatoTablaInvalido("El largo de las columnas debe ser el mismo para todas las columnas.");
        if (etiquetasFilas.size() != listColumnas.get(0).cantidadCeldas())
            throw new FormatoTablaInvalido(
                    "La cantidad de etiquetas de filas debe coincidir con el largo de columnas.");

        this.etiquetas_fila = etiquetasFilas;

        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            tabla.put(etiquetasColumnas.get(i), listColumnas.get(i));
        }

    }

    // ----------------------------------------- GETTERS
    // -------------------------------------
    public List<Columna<?>> getListaColumnas() {

        List<Columna<?>> lista_columnas = new ArrayList<Columna<?>>();

        for (Columna<?> columna : tabla.values()) {
            lista_columnas.add(columna);
        }

        return lista_columnas;
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

    public Columna<?> getColumna(Integer indice_columna) throws IndiceInexistente {
        int contador = 0;
        for (Map.Entry<Etiqueta<K>, Columna<?>> entrada : tabla.entrySet()) {
            if (contador == indice_columna)
                return entrada.getValue();
            contador++;
        }
        throw new IndiceInexistente("No existe la columna " + String.valueOf(indice_columna));

    }

    public Integer getCantidadColumnas() {
        return getEtiquetas_columna().size();
    }

    public Integer getCantidadFilas() {
        return etiquetas_filas().size();
    }

    // ----------------------------------------- METODOS PRIVADOS INTERNOS
    // -------------------------------------
    private boolean chequearLargoDeColumnas(Object[][] columnas) {
        int largoColumna = columnas[0].length;
        for (Object[] col : columnas) {
            if (col.length != largoColumna) {
                return false;
            }
        }
        return true;
    }

    private boolean chequearLargoDeColumnas(List<Columna<?>> columnas) {
        int largoColumna = columnas.get(0).cantidadCeldas();

        for (Columna<?> col : columnas) {
            if (col.cantidadCeldas() != largoColumna) {
                return false;
            }
        }
        return true;
    }

    private boolean chequearLargoDeFilas(K[] etiquetaColumnas, Object[][] columnas) {
        return etiquetaColumnas.length == columnas.length;
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
        // Extraer los valores de tipo Celda si corresponde
        Object[] valoresReales = new Object[lista.length];
        for (int i = 0; i < lista.length; i++) {
            if (lista[i] instanceof Celda) {
                // Si el elemento es una celda (como en copiar), le extraigo el valor
                valoresReales[i] = ((Celda) lista[i]).obtenerValor();
            } else {
                valoresReales[i] = lista[i]; // Si no, simplemente asigna el valor
            }
        }

        if (esNumerica(valoresReales)) {
            Number[] listaCasteada = new Number[valoresReales.length];
            for (int j = 0; j < valoresReales.length; j++) {
                listaCasteada[j] = (Number) valoresReales[j]; // Castear cada elemento a Number
            }
            return new Columna<>(listaCasteada);
        }

        if (esBooleana(valoresReales)) {
            Boolean[] listaCasteada = new Boolean[valoresReales.length];
            for (int j = 0; j < valoresReales.length; j++) {
                listaCasteada[j] = (Boolean) valoresReales[j];
            }
            return new Columna<>(listaCasteada);
        }

        // Asumir que es String si no es numérica ni booleana
        String[] listaCasteada = new String[valoresReales.length];
        for (int j = 0; j < valoresReales.length; j++) {
            listaCasteada[j] = String.valueOf(valoresReales[j]);
        }
        return new Columna<>(listaCasteada);
    }

    // ----------------------------------------- INFORMACION DE TABLA
    // -------------------------------------
    public Integer nfilas() {
        return etiquetas_fila.size();
    }

    public Integer ncols() {
        return tabla.size();
    }

    public List<Etiqueta<F>> etiquetas_filas() {
        return etiquetas_fila;
    }

    public List<Etiqueta<K>> etiquetas_columnas() {
        return new ArrayList<>(tabla.keySet());
    }

    // ----------------------------------------- PROYECTABLE
    // -------------------------------------
    @Override
    public Tabla<K, F> subtabla(List<Etiqueta<F>> listFilas, List<Etiqueta<K>> listColumnas ) {
        if (listColumnas.size() == 0 ) listColumnas = getEtiquetas_columna();

        Tabla<K, F> tablaNueva = subtablaColumnas(listColumnas);

        if (listFilas.size() == 0)
            return tablaNueva;
        else
            return tablaNueva.subtablaFilas(listFilas);
    }

    public Tabla<K, F> subtablaColumnas(List<Etiqueta<K>> listColumnas) throws EtiquetaInvalida{
        LinkedHashMap<Etiqueta<K>, Columna<?>> tablaNueva;
        List<Columna<?>> listaColumnasNueva = new ArrayList<Columna<?>>();

        List<Etiqueta<K>> etiquetasColumnasOriginal = getEtiquetas_columna();

        for (int i = 0; i < listColumnas.size(); i++) {
            boolean existe = false;
            for (int j = 0; j < etiquetasColumnasOriginal.size(); j++) {
                if (listColumnas.get(i).equals(etiquetasColumnasOriginal.get(j))){
                    listaColumnasNueva.add(getColumna(j));
                    existe = true;
                    break;
                }   
            }
            if (!existe) throw new EtiquetaInvalida("La etiqueta " + listColumnas.get(i).getNombre() + " no existe en el encabezado.");
        }
        return new Tabla<K, F>(getEtiquetas_fila(), listColumnas, listaColumnasNueva);
    }

    public Tabla<K, F> subtablaFilas(List<Etiqueta<F>> listFilas) throws EtiquetaInvalida {
        List<Integer> indices = indiceFilas(listFilas);
        List<Columna<?>> listaColumnasNueva = new ArrayList<Columna<?>>();

        for (Columna<?> col : getListaColumnas()) {
            listaColumnasNueva.add(col.subColumna(indices));
        }
        return new Tabla<K, F>(listFilas, getEtiquetas_columna(), listaColumnasNueva);
    }

    private List<Integer> indiceFilas(List<Etiqueta<F>> listFilas) throws EtiquetaInvalida {
        List<Etiqueta<F>> etiquetasFilasOriginal = getEtiquetas_fila();
        if (etiquetasFilasOriginal.size() == 0) throw new EtiquetaInvalida("La tabla no posee etiquetas de fila.");
        List<Integer> indices = new ArrayList<Integer>();

        for (Integer j = 0; j < listFilas.size(); j++) {
            boolean existe = false;
            for (Integer i = 0; i < etiquetasFilasOriginal.size(); i++) {
                if (etiquetasFilasOriginal.get(i).equals(listFilas.get(j))){
                    indices.add(i);
                    existe = true;
                }
            }
            if (!existe) throw new EtiquetaInvalida("La etiqueta " + listFilas.get(j).getNombre() + " no existe en las etiquetas de fila.");
        }
        return indices;
    }

    @Override
    public Tabla<K, F> head(int n) {
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

    // ----------------------------------------- COPIABLE
    // -------------------------------------
    @Override
    public void copiar(Tabla<K, F> a_copiar) {

        // Copiar Etiquetas de fila
        for (Etiqueta<F> etiquetaFila : a_copiar.etiquetas_filas()) {
            this.etiquetas_fila.add(new Etiqueta<>(etiquetaFila.getNombre()));
        }

        // Iterar sobre la tabla
        for (Map.Entry<Etiqueta<K>, Columna<?>> entrada : a_copiar.tabla.entrySet()) {
            // Copiar Etiquetas de columna
            Etiqueta<K> etiquetaColumna = new Etiqueta<>(entrada.getKey().getNombre());

            // Inferir tipo de dato y crear columna copia
            Columna<?> columnaOriginal = entrada.getValue();
            Columna<?> columnaCopia = castearColumna(columnaOriginal.obtenerValores().toArray());

            // Agregar la columna copiada a la nueva tabla
            this.tabla.put(etiquetaColumna, columnaCopia);
        }
    }

    // -----------------------------------------
    // VISUALIZABLE-------------------------------------
    @Override
    public void ver(Integer cantidad_caracteres, Integer cantidad_filas) {
        if (tabla.isEmpty()) {
            System.out.println("La tabla está vacía.");
            return;
        }

        imprimirEtiquetasDeColumnas(cantidad_caracteres);

        if (cantidad_filas > getCantidadFilas()) {
            cantidad_filas = getCantidadFilas();
        }
        for (int i = 0; i < cantidad_filas; i++) {
            verFila(i, cantidad_caracteres);
        }
    }

    private Integer etiquetaMasGrandeFila() {
        Integer max = 0;
        for (Etiqueta<F> etiqueta : etiquetas_fila) {
            String nombre_etiqueta = String.valueOf(etiqueta.getNombre());
            int largo = nombre_etiqueta.length();
            if (largo > max) {
                max = largo;
            }
        }
        return max;
    }

    @Override
    public void verFila(Integer indice_fila, Integer cantidad_caracteres) {
        if (indice_fila >= etiquetas_fila.size()) {
            throw new IndiceInexistente("No existe la fila " + String.valueOf(indice_fila));
        }
        StringBuilder sb = new StringBuilder();
        String etiqueta = String.valueOf(etiquetas_fila.get(indice_fila).getNombre());

        // Agregar la etiqueta de la fila
        sb.append(etiqueta).append(repeat(" ", 5)); // Agrego espacios iniciales dsp de etiqueta

        // Iterar sobre columnas para obtener el valor
        for (Columna<?> columna : this.tabla.values()) {
            String valor = String.valueOf(columna.valorCelda(indice_fila));

            if (valor.length() > cantidad_caracteres) {
                valor = valor.substring(0, cantidad_caracteres); // Cortar si es necesario
                sb.append(valor);
                sb.append(repeat(".", 3)); // 3 puntos ... p indicarnque fue cortada
                sb.append(repeat(" ", 2)); // 2 espacios de separacion
            } else if (valor.length() < cantidad_caracteres) {
                sb.append(valor);
                int diferencia = cantidad_caracteres - valor.length();
                sb.append(repeat(" ", diferencia + 5)); // Relleno con espacios lo que falta
            } else {
                sb.append(valor);
                sb.append(repeat(" ", 5)); // Relleno con espacios lo que falta
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
        if (indice_columna > this.tabla.size()) {
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
        // Generar Etiqueta
        Etiqueta<String> etiquetaKey = new Etiqueta<>(etiqueta_columna);

        // Obtener la columna correspondiente
        Columna<?> columna = tabla.get(etiquetaKey);
        if (columna == null) {
            throw new IndiceInexistente("No existe la columna " + etiqueta_columna);
        }
        System.out.println(etiqueta_columna);
        System.out.println(columna.toString());

    }

    private void imprimirEtiquetasDeColumnas(Integer cantidad_caracteres) {
        StringBuilder sb = new StringBuilder();
        int cantidad_espacios_iniciales = etiquetaMasGrandeFila() + 5;
        sb.append(repeat(" ", cantidad_espacios_iniciales)); // Agrego espacios iniciales

        // Etiquetas de columnas con espacio definido por cantidad_caracteres
        for (Etiqueta<K> etiqueta : tabla.keySet()) {
            String nombre_columna = String.valueOf(etiqueta.getNombre());

            if (nombre_columna.length() > cantidad_caracteres) {
                nombre_columna = nombre_columna.substring(0, cantidad_caracteres); // Cortar si es necesario
                sb.append(nombre_columna);
                sb.append(repeat(".", 3)); // 3 puntos ... p indicarnque fue cortada
                sb.append(repeat(" ", 2)); // 2 espacios de separacion
            } else if (nombre_columna.length() < cantidad_caracteres) {
                Integer diferencia = cantidad_caracteres - nombre_columna.length();
                sb.append(nombre_columna);
                sb.append(repeat(" ", diferencia + 5));

            } else {
                sb.append(nombre_columna);
                sb.append(repeat(" ", 5));
            }
        }
        System.out.println(sb.toString());
    }

    private String repeat(String caracter, int cantidad) {
        // Replico el metodo String.repeat(int cantidad) de Java 11 para mas
        // portabilidad
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < cantidad; i++) {
            string.append(caracter);
        }
        return string.toString();
    }

    // -------------------------ELIMINACION----------------------------
    public void eliminarColumna(String etiqueta_columna) {
        Etiqueta<String> etiqueta = new Etiqueta<>(etiqueta_columna);

        Columna<?> eliminada = tabla.remove(etiqueta);
        if (eliminada == null) {
            System.out.println("Nombre de columna incorrecto: " + etiqueta_columna);
        }
    }

    public void eliminarFila(int indice_fila) {

        if (indice_fila >= getCantidadFilas()) {
            throw new IndiceInexistente("No existe la fila " + String.valueOf(indice_fila));
        }

        for (Columna<?> columna : tabla.values()) {
            columna.eliminarValor(indice_fila);
        }
    }
    // ----------------------------AGREGABLE----------------------

    @Override
    public void agregarFila(String etiqueta, Object[] fila) {
        //FALTA MANEJO SI ETIQUETAS SON NUMEROS
        Etiqueta<F> etiqueta_fila = new Etiqueta<>((F)etiqueta);
        this.etiquetas_fila.add(etiqueta_fila);
        agregarFila(fila);
    }
    
    @Override
    public void agregarFila(Object[] fila) {
        try { 
            F num = etiquetas_fila.get(etiquetas_fila.size() -1).getNombre(); // Obtengo valor de ultima etiqueta de numero
            Integer valor = (Integer) num;
            Etiqueta<Integer> etiqueta = new Etiqueta<>(valor+1);
            etiquetas_fila.add((Etiqueta<F>) etiqueta);
            
        } 
        catch (ClassCastException e) { 
            throw new FilaInvalida("Las etiquetas de fila son Strings, usar metodo agregarFila(String etiqueta, Object[] fila)");
        }
        
        

        if (fila.length != getCantidadColumnas()) {
            throw new FilaInvalida("La cantidad de elementos en la fila no coincide con la cantidad de columnas.");
        }

        List<Columna<?>> lista_columnas = getListaColumnas();
        
        for (int i = 0; i < fila.length; i++){
            Object dato = fila[i];
            lista_columnas.get(i).agregarValor(dato);
        }
    }

    @Override
    public void agregarColumna(String etiq, Object[] columna) {
        if (columna.length != getCantidadFilas()){
            throw new ColumnaInvalida("La columna nueva deber ser de largo " + String.valueOf(getCantidadFilas()));
        }
    
        Etiqueta<String> etiqueta = new Etiqueta<>(etiq); 
        Columna<?> columnaCasteada = castearColumna(columna);

        tabla.put((Etiqueta<K>)etiqueta, columnaCasteada);
    }

}
