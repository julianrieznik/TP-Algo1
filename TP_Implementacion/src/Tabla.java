import excepciones.ColumnaInvalida;
import excepciones.EtiquetaInvalida;
import excepciones.FilaInvalida;
import excepciones.FiltroInvalido;
import excepciones.FormatoTablaInvalido;
import excepciones.IndiceInexistente;
import excepciones.OperacionInvalida;
import excepciones.OrdenInvalido;
import excepciones.TipoDeColumnaInvalido;
import interfaces.Rellenable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class Tabla<K, F> implements interfaces.Agregable<Tabla<K, F>, F>, interfaces.Copiable<Tabla<K, F>>,
        interfaces.Visualizable<Tabla<K, F>>, interfaces.Proyectable<Tabla<K, F>, Etiqueta<K>, Etiqueta<F>>,
        interfaces.Ordenable<Tabla<K, F>, K>, interfaces.Filtrable<Tabla<K, F>, K, Celda, OperadorLogico>,
        Rellenable<K> {
    // GENERICS
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
    public Tabla(K[] etiquetaColumnas, Object[][] columnas) throws FormatoTablaInvalido {
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
    public Tabla(F[] etiquetaFilas, K[] etiquetaColumnas, Object[][] columnas) throws FormatoTablaInvalido {
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

    // Constructor con lista de etiqFilas, etiqColumnas y lista de Columnas. Uso
    // interno
    private Tabla(List<Etiqueta<F>> etiquetasFilas, List<Etiqueta<K>> etiquetasColumnas, List<Columna<?>> listColumnas)
            throws FormatoTablaInvalido {
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

    // Constructor con lista de etiqFilas, etiqColumnas y vector de columnas. Uso
    // Uso en groupby
    private Tabla(List<Etiqueta<F>> etiquetasFilas, List<Etiqueta<K>> etiquetasColumnas, Object[][] columnas)
            throws FormatoTablaInvalido {
        this();

        if (etiquetasColumnas.size() != columnas.length) {
            // HACER EXCEPCION PROPIA
            throw new FormatoTablaInvalido(
                    "La cantidad de etiquetas de columnas debe coincidir con el largo de las filas.");
        }
        if (!chequearLargoDeColumnas(columnas)) {
            // HACER EXCEPCION PROPIA
            throw new FormatoTablaInvalido("El largo de las columnas debe ser el mismo para todas las columnas.");
        }
        if (etiquetasFilas.size() != columnas[0].length)
            throw new FormatoTablaInvalido(
                    "La cantidad de etiquetas de filas debe coincidir con el largo de columnas.Largo columnas: "
                            + columnas[0].length + " y las etiquetas son: " + etiquetasFilas.size());

        // Inicializar etiquetas de filas
        this.etiquetas_fila = etiquetasFilas;

        // Inicializar columnas asociadas a etiquetas de columnas
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            tabla.put(etiquetasColumnas.get(i), new Columna<>(columnas[i]));
        }

    }

    // ----------------------------------------- GETTERS
    // -------------------------------------
    public List<Columna<?>> getListaColumnas() {

        List<Columna<?>> lista_columnas = new ArrayList<>();

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

    public Integer getCantidadColumnasNumericas() {
        Integer total = 0;
        for (Columna<?> columna : getListaColumnas()) {
            String tipo = columna.tipo();
            if (!tipo.equals("Object") && !tipo.equals("String") && !tipo.equals("Boolean")) {
                total++;
            }
        }
        return total;
    }

    public <T> Columna<T> obtenerColumna(K nombreColumna) {
        Etiqueta<K> etiqueta = new Etiqueta<>(nombreColumna);

        for (Map.Entry<Etiqueta<K>, Columna<?>> entrada : tabla.entrySet()) {
            if (entrada.getKey().equals(etiqueta)) {
                Columna<T> columna = (Columna<T>) entrada.getValue();
                return columna;
            }
        }
        throw new ColumnaInvalida("No existe la columna " + nombreColumna);
    }

    public Etiqueta<K> obtenerEtiqueta(K nombre) {
        Etiqueta<K> etiqueta = new Etiqueta<>(nombre);
        List<Etiqueta<K>> lista = getEtiquetas_columna();
        for (Etiqueta<K> eti : lista) {
            if (eti.equals(etiqueta)) {
                return eti;
            }
        }
        throw new ColumnaInvalida("No existe la columna " + String.valueOf(nombre));
    }

    public List<Etiqueta<K>> getEtiquetasColumnasNumericas() {
        List<Etiqueta<K>> etiquetasNumericas = new ArrayList<>();

        for (Map.Entry<Etiqueta<K>, Columna<?>> entry : tabla.entrySet()) {
            Columna<?> columna = entry.getValue();

            if (!columna.tipo().equals("String") && !columna.tipo().equals("Boolean")
                    && !columna.tipo().equals("Object")) {
                etiquetasNumericas.add(entry.getKey());
            }
        }

        return etiquetasNumericas;
    }

    public <T> T obtenerValorCelda(K col, F fila) {
        Integer indice = null;

        for (Etiqueta<F> etiq : etiquetas_fila) {
            if (etiq.equals(new Etiqueta<F>(fila))) {
                indice = etiquetas_fila.indexOf(etiq);
                break;
            }
        }

        if (indice == null) {
            throw new FilaInvalida("No existe la fila " + String.valueOf(fila));
        }

        Columna<T> columna = obtenerColumna(col);
        T valor = columna.valorCelda(indice);
        return valor;
    }

    public String obtenerTipoCelda(K col, F fila) {
        Integer indice = null;

        for (Etiqueta<F> etiq : etiquetas_fila) {
            if (etiq.equals(new Etiqueta<F>(fila))) {
                indice = etiquetas_fila.indexOf(etiq);
                break;
            }
        }

        if (indice == null) {
            throw new FilaInvalida("No existe la fila " + String.valueOf(fila));
        }
        Columna<?> columna = obtenerColumna(col);
        String tipo = columna.tipoCelda(indice);
        return tipo;
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
                if (elemento != null) {
                    Number numero = (Number) elemento;
                }
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
                if (elemento != null) {
                    Boolean bool = (Boolean) elemento;
                }
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
    public Tabla<K, F> subtabla(List<Etiqueta<F>> listFilas, List<Etiqueta<K>> listColumnas) {
        if (listColumnas == null)
            listColumnas = getEtiquetas_columna();

        Tabla<K, F> tablaNueva = subtablaColumnas(listColumnas);

        if (listFilas == null)
            return tablaNueva;
        else
            return tablaNueva.subtablaFilas(listFilas);
    }


    @Override
    public Tabla<K, F> subtablaColumnas(List<Etiqueta<K>> listColumnas) throws EtiquetaInvalida {
        if (listColumnas.isEmpty())
            throw new EtiquetaInvalida("Debe proporcionar al menos 1 etiqueta para generar la subtabla.");
        List<Columna<?>> listaColumnasNueva = new ArrayList<>();

        List<Etiqueta<K>> etiquetasColumnasOriginal = getEtiquetas_columna();

        for (int i = 0; i < listColumnas.size(); i++) {
            boolean existe = false;
            for (int j = 0; j < etiquetasColumnasOriginal.size(); j++) {
                if (listColumnas.get(i).equals(etiquetasColumnasOriginal.get(j))) {
                    listaColumnasNueva.add(getColumna(j));
                    existe = true;
                    break;
                }
            }
            if (!existe)
                throw new EtiquetaInvalida(
                        "La etiqueta " + listColumnas.get(i).getNombre() + " no existe en el encabezado.");
        }
        return new Tabla<>(getEtiquetas_fila(), listColumnas, listaColumnasNueva);
    }

    @Override
    public Tabla<K, F> subtablaFilas(List<Etiqueta<F>> listFilas) throws EtiquetaInvalida {
        if (listFilas.isEmpty())
            throw new EtiquetaInvalida("Debe proporcionar al menos 1 etiqueta para generar la subtabla.");
        List<Integer> indices = indiceFilas(listFilas);
        List<Columna<?>> listaColumnasNueva = new ArrayList<>();

        for (Columna<?> col : getListaColumnas()) {
            listaColumnasNueva.add(col.subColumna(indices));
        }
        return new Tabla<>(listFilas, getEtiquetas_columna(), listaColumnasNueva);
    }

    private List<Integer> indiceFilas(List<Etiqueta<F>> listFilas) throws EtiquetaInvalida {
        List<Etiqueta<F>> etiquetasFilasOriginal = getEtiquetas_fila();
        if (etiquetasFilasOriginal.isEmpty())
            throw new EtiquetaInvalida("La tabla no posee etiquetas de fila.");
        List<Integer> indices = new ArrayList<>();

        for (Integer j = 0; j < listFilas.size(); j++) {
            boolean existe = false;
            for (Integer i = 0; i < etiquetasFilasOriginal.size(); i++) {
                if (etiquetasFilasOriginal.get(i).equals(listFilas.get(j))) {
                    indices.add(i);
                    existe = true;
                }
            }
            if (!existe)
                throw new EtiquetaInvalida(
                        "La etiqueta " + listFilas.get(j).getNombre() + " no existe en las etiquetas de fila.");
        }
        return indices;
    }

    @Override
    public Tabla<K, F> head(int n) throws IndiceInexistente {
        if (n == 0)
            throw new IndiceInexistente("El indice 0 no es válido");
        if (n > getCantidadFilas())
            throw new IndiceInexistente("La tabla posee menos de " + n + " filas.");

        return subtablaFilas(getEtiquetas_fila().subList(0, n));
    }

    @Override
    public Tabla<K, F> tail(int n) throws IndiceInexistente {
        if (n == 0)
            throw new IndiceInexistente("El indice 0 no es válido");
        if (n > getCantidadFilas())
            throw new IndiceInexistente("La tabla posee menos de " + n + " filas.");

        return subtablaFilas(getEtiquetas_fila().subList(getCantidadFilas() - n, getCantidadFilas()));
    }

    // -------------------------------------ORDENAR--------------------------------------------
    /*
     * @Override
     * public Tabla<K, F> ordenar(List<K> lista, boolean ascendente) {
     * // TODO Auto-generated method stub
     * throw new UnsupportedOperationException("Unimplemented method 'ordenar'");
     * }
     */

    @Override
    public Tabla<K, F> ordenar(List<K> lista, boolean ascendente) {

        List<Etiqueta<K>> listEtiq = new ArrayList<>();
        for (K elem : lista) {
            Etiqueta<K> etiqueta_columna = new Etiqueta<>(elem);
            listEtiq.add(etiqueta_columna);
        }

        if (listEtiq.size() != 1) {
            throw new IllegalArgumentException("Orden inválido. Ingresar una columna.");
        }

        Columna<?> colum = null;

        for (Map.Entry<Etiqueta<K>, Columna<?>> entrada : tabla.entrySet()) {
            if (entrada.getKey().equals(listEtiq.get(0))) {
                colum = entrada.getValue().copiaProfunda();
            }
        }

        // Trato los null antes de ordenar
        if (colum.tieneNA()) {
            if (ascendente == true) {
                if (colum.obtenerValorNoNulo() instanceof Double) {
                    Double aux = colum.maximoValorDouble();
                    colum.rellenarNA(aux);
                } else if (colum.obtenerValorNoNulo() instanceof Integer) {
                    Integer aux = colum.maximoValorInteger();
                    colum.rellenarNA(aux);
                } else if (colum.obtenerValorNoNulo() instanceof String) {
                    colum.rellenarNA("Z");
                } else if (colum.obtenerValorNoNulo() instanceof Boolean) {
                    colum.rellenarNA(true);
                }
            } else {
                if (colum.obtenerValorNoNulo() instanceof Double) {
                    Number aux = colum.minimoValorDouble();
                    colum.rellenarNA(aux);
                } else if (colum.obtenerValorNoNulo() instanceof Integer) {
                    Number aux = colum.minimoValorInteger();
                    colum.rellenarNA(aux);
                } else if (colum.obtenerValorNoNulo() instanceof String) {
                    colum.rellenarNA("A");
                } else if (colum.obtenerValorNoNulo() instanceof Boolean) {
                    colum.rellenarNA(false);
                }
            }

        }

        final Columna<?> columna = colum;

        if (!columna.todosSonNull()) {
            // Crear una lista de índices (0, 1, 2, ...) para representar las posiciones
            // originales
            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < columna.cantidadCeldas(); i++) {
                indices.add(i);
            }

            if (ascendente == true) {
                if (columna.obtenerValorNoNulo() instanceof Number) {

                    indices.sort((i, j) -> Double.compare(((Number) columna.valorCelda(i)).doubleValue(),
                            ((Number) columna.valorCelda(j)).doubleValue())); // columna numérica ordenada
                                                                              // ascendentemente.

                } else if (columna.obtenerValorNoNulo() instanceof String) {

                    Collections.sort(indices, Comparator.comparing(index -> (String) columna.aListaGenerica()[index]));
                    // Collections.sort(columna.obtenerValores(), (c1, c2)
                    // ->((String)c1.obtenerValor()).compareTo((String)c2.obtenerValor()));

                } else if (columna.obtenerValorNoNulo() instanceof Boolean) {
                    indices.sort((i1, i2) -> Boolean.compare(((Boolean) columna.valorCelda(i1)),
                            ((Boolean) columna.valorCelda(i2))));
                }

            } else if (ascendente == false) {

                if (columna.obtenerValorNoNulo() instanceof Number) {

                    indices.sort((i, j) -> Double.compare(((Number) columna.valorCelda(j)).doubleValue(),
                            ((Number) columna.valorCelda(i)).doubleValue())); // columna numérica ordenada
                                                                              // ascendentemente.

                } else if (columna.obtenerValorNoNulo() instanceof String) {

                    Collections.sort(indices, Comparator
                            .comparing((Integer index) -> (String) columna.aListaGenerica()[index]).reversed());
                    // Collections.sort(columna.obtenerValores(), (c1, c2) ->
                    // ((String)c2.obtenerValor()).compareTo((String)c1.obtenerValor()));

                } else if (columna.obtenerValorNoNulo() instanceof Boolean) {
                    indices.sort((i1, i2) -> Boolean.compare(!((Boolean) columna.valorCelda(i1)),
                            !((Boolean) columna.valorCelda(i2))));
                }
            }

            List<Etiqueta<K>> listEtiquetaFila = new ArrayList<>();
            List<Etiqueta<K>> listEtiquetasCol = new ArrayList<>();
            List<Columna<?>> listColumnas = new ArrayList<>();

            // Reordenar todas las columnas usando los índices ordenados
            for (Map.Entry<Etiqueta<K>, Columna<?>> entry : tabla.entrySet()) {
                Columna<?> columnaSinOrden = entry.getValue();
                listEtiquetasCol.add(entry.getKey());

                Columna<?> columnaOrdenada = new Columna<>(new ArrayList<>());
                for (int index : indices) {
                    columnaOrdenada.agregarValor(columnaSinOrden.obtenerCelda(index).obtenerValor());
                }
                listColumnas.add(columnaOrdenada);
            }
            return new Tabla<>(this.etiquetas_fila, listEtiquetasCol, listColumnas);
        } else {
            throw new IndiceInexistente("La columna seleccionada está vacía");
        }
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
        StringBuilder columnas = new StringBuilder();
        columnas.append("Columnas: ");
        for (Map.Entry<Etiqueta<K>, Columna<?>> entrada : this.tabla.entrySet()) {
            String nombre = String.valueOf(entrada.getKey().getNombre());
            String tipo = entrada.getValue().tipo();
            columnas.append(nombre + "[" + tipo + "] ");
        }
        System.out.println(columnas.toString());

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
        Integer diferencia = etiquetaMasGrandeFila() - etiqueta.length() + 5;
        sb.append(etiqueta).append(repeat(" ", diferencia)); // Agrego espacios iniciales dsp de etiqueta

        // Iterar sobre columnas para obtener el valor
        for (Columna<?> columna : this.tabla.values()) {
            String valor = "";
            if (columna.valorCelda(indice_fila) == null || columna.valorCelda(indice_fila) == "null")
                valor = "NA";
            else
                valor = String.valueOf(columna.valorCelda(indice_fila));

            if (valor.length() > cantidad_caracteres) {
                valor = valor.substring(0, cantidad_caracteres); // Cortar si es necesario
                sb.append(valor);
                sb.append(repeat(".", 3)); // 3 puntos ... p indicarnque fue cortada
                sb.append(repeat(" ", 2)); // 2 espacios de separacion
            } else if (valor.length() < cantidad_caracteres) {
                sb.append(valor);
                int dif = cantidad_caracteres - valor.length();
                sb.append(repeat(" ", dif + 5)); // Relleno con espacios lo que falta
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
    public void eliminarColumna(K etiqueta_columna) {
        Etiqueta<K> etiqueta = new Etiqueta<>(etiqueta_columna);

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
    public void agregarFila(F etiqueta, Object[] fila) {
        // FALTA MANEJO SI ETIQUETAS SON NUMEROS
        Etiqueta<F> etiqueta_fila = new Etiqueta<>((F) etiqueta);
        this.etiquetas_fila.add(etiqueta_fila);
        agregarFilaSinEtiqueta(fila);
    }

    @Override
    public void agregarFila(Object[] fila) {
        try {
            F num = etiquetas_fila.get(etiquetas_fila.size() - 1).getNombre();
            Integer valor = (Integer) num;
            Etiqueta<Integer> etiqueta = new Etiqueta<>(valor + 1);
            etiquetas_fila.add((Etiqueta<F>) etiqueta);

        } catch (ClassCastException e) {
            throw new FilaInvalida(
                    "Las etiquetas de fila son Strings, usar metodo agregarFila(String etiqueta, Object[] fila)");
        }
        agregarFilaSinEtiqueta(fila);
    }

    private void agregarFilaSinEtiqueta(Object[] fila) {
        if (fila.length != getCantidadColumnas()) {
            throw new FilaInvalida("La cantidad de elementos en la fila no coincide con la cantidad de columnas.");
        }

        List<Columna<?>> lista_columnas = getListaColumnas();

        for (int i = 0; i < fila.length; i++) {
            Object dato = fila[i];
            lista_columnas.get(i).agregarValor(dato);
        }
    }

    @Override
    public void agregarColumna(String etiq, Object[] columna) {
        if (columna.length != getCantidadFilas()) {
            throw new ColumnaInvalida("La columna nueva deber ser de largo " + String.valueOf(getCantidadFilas()));
        }
        try {
            K etiqueta_col = (K) etiq;
            Etiqueta<K> etiqueta = new Etiqueta<>(etiqueta_col);
            if (this.tabla.containsKey(etiqueta)) {
                throw new ColumnaInvalida("La columna " + etiq + " ya existe");
            }
            Columna<?> columnaCasteada = castearColumna(columna);
            tabla.put((Etiqueta<K>) etiqueta, columnaCasteada);
        } catch (ClassCastException e) {
            throw new EtiquetaInvalida("Etiqueta de columna invalida");
        }
    }

    private void agregarColumna(String etiq, Columna<?> columna) {
        if (columna.obtenerValores().size() != getCantidadFilas()) {
            throw new ColumnaInvalida("La columna nueva deber ser de largo " + String.valueOf(getCantidadFilas()));
        }
        K etiqueta_col = (K) etiq;
        Etiqueta<K> etiqueta = new Etiqueta<>(etiqueta_col);
        if (this.tabla.containsKey(etiqueta)) {
            throw new ColumnaInvalida("La columna " + etiq + " ya existe");
        }
        this.tabla.put(etiqueta, columna);
    }

    // --------------------------------SOBREESCRITURA---------
    public void modificarCelda(K columna, F fila, Object valor_nuevo) {
        Etiqueta<F> etiqueta_fila = new Etiqueta<>(fila);
        Integer indice_fila = etiquetas_filas().indexOf(etiqueta_fila);

        if (indice_fila == -1) {
            throw new FilaInvalida("No existe la fila " + String.valueOf(fila));
        }
        Etiqueta<K> etiqueta_columna = new Etiqueta<>(columna);

        try {
            tabla.get(etiqueta_columna).modificarValorCelda(indice_fila, valor_nuevo);
        } catch (NullPointerException e) {
            throw new ColumnaInvalida("No existe la columna " + String.valueOf(columna));
        }
    }

    public void cambiarTipoColumna(K nombreColumna, Class<?> clase) {
        Columna<?> columna = obtenerColumna(nombreColumna);
        Object[] arrayObject = columna.aListaGenerica();
        Etiqueta<K> etiqueta = obtenerEtiqueta(nombreColumna);

        if (Number.class.isAssignableFrom(clase)) {
            if (!esNumerica(arrayObject)) {
                throw new TipoDeColumnaInvalido(
                        "No se puede castear la columna " + nombreColumna + " a " + String.valueOf(clase));
            }

            if (clase == Integer.class) {
                Integer[] nuevoArray = Arrays.stream(arrayObject)
                        .map(obj -> ((Number) obj).intValue())
                        .toArray(Integer[]::new);
                Columna<Integer> nuevaColumna = new Columna<>(nuevoArray);
                this.tabla.put(etiqueta, nuevaColumna);
            }

            if (clase == Double.class) {
                Double[] nuevoArray = Arrays.stream(arrayObject)
                        .map(obj -> ((Number) obj).doubleValue())
                        .toArray(Double[]::new);
                Columna<Double> nuevaColumna = new Columna<>(nuevoArray);
                this.tabla.put(etiqueta, nuevaColumna);
            }

            if (clase == Float.class) {
                Float[] nuevoArray = Arrays.stream(arrayObject)
                        .map(obj -> ((Number) obj).floatValue())
                        .toArray(Float[]::new);
                Columna<Float> nuevaColumna = new Columna<>(nuevoArray);
                this.tabla.put(etiqueta, nuevaColumna);
            }

        }

        else if (clase == String.class) {
            String[] nuevoArray = Arrays.stream(arrayObject)
                    .map(obj -> obj == null ? "" : obj.toString())
                    .toArray(String[]::new);
            Columna<String> nuevaColumna = new Columna<>(nuevoArray);
            this.tabla.put(etiqueta, nuevaColumna);
        }

        else if (clase == Boolean.class) {

            if (!esBooleana(arrayObject)) {
                throw new TipoDeColumnaInvalido("No se puede castear la columna " + nombreColumna + " a Boolean.");
            }

            Boolean[] nuevoArray = Arrays.stream(arrayObject)
                    .map(obj -> (Boolean) obj)
                    .toArray(Boolean[]::new);
            Columna<Boolean> nuevaColumna = new Columna<>(nuevoArray);
            this.tabla.put(etiqueta, nuevaColumna);
        }

        else {
            throw new TipoDeColumnaInvalido(
                    "Tipos de Columna admitidos: String.class, Double.class, Float.class, Integer.class, Boolean.class");
        }
    }

    // --------------------------------FILTRADO---------
    @Override
    public Tabla<K, F> filtrar(K etiq, Predicate<Celda> criterio) throws FiltroInvalido {
        Etiqueta<K> enueva = new Etiqueta<>(etiq);
        if (!tabla.keySet().contains(enueva))
            throw new FiltroInvalido("La etiqueta " + etiq + " no se encuentra en el encabezado de la tabla.");

        Columna<?> colFiltrada = tabla.get(enueva);
        List<Etiqueta<F>> eFilas = new ArrayList<>();

        for (int i = 0; i < getCantidadFilas(); i++) {
            if (criterio.test(colFiltrada.getCelda(i)))
                eFilas.add(etiquetas_fila.get(i));
        }
        if (eFilas.isEmpty())
            throw new FiltroInvalido("El criterio de filtro no arrojó ningún resultado.");
        return subtablaFilas(eFilas);
    }

    @Override
    public Tabla<K, F> filtrar(List<K> etiq, List<Predicate<Celda>> criterio) throws FiltroInvalido {
        if (etiq.size() != criterio.size())
            throw new FiltroInvalido("La cantidad de columnas y cantidad de criterios debe ser la misma.");
        if (etiq.size() > 1)
            throw new FiltroInvalido(
                    "Para filtrar por más de una columna debe proporcionar un operador lógico como tercer parámetro: filtrar(List<K>, List<Predicate<Celda>, OperadorLogico)");
        return filtrar(etiq.get(0), criterio.get(0));
    }

    @Override
    public Tabla<K, F> filtrar(List<K> etiq, List<Predicate<Celda>> criterio, OperadorLogico operador)
            throws FiltroInvalido {
        if (etiq.size() != criterio.size())
            throw new FiltroInvalido("La cantidad de columnas y cantidad de criterios debe ser la misma.");
        if (etiq.size() == 1)
            return filtrar(etiq, criterio);
        List<Columna<?>> colFiltradas = new ArrayList<>();

        List<Etiqueta<K>> etiquetas = new ArrayList<>();
        for (K e : etiq) {
            Etiqueta<K> enueva = new Etiqueta<>(e);
            if (!tabla.keySet().contains(enueva))
                throw new FiltroInvalido("La etiqueta " + e + " no se encuentra en el encabezado de la tabla.");
            etiquetas.add(enueva);
            colFiltradas.add(tabla.get(enueva));
        }

        List<Tabla<K, F>> listaTablas = new ArrayList<Tabla<K, F>>();

        for (int i = 0; i < colFiltradas.size(); i++) {
            listaTablas.add(filtrar(etiq.get(i), criterio.get(i)));
        }
        return ConcatenarOperando(listaTablas, operador, 0, new Tabla<K, F>());
    }

    private Tabla<K, F> ConcatenarOperando(List<Tabla<K, F>> listaTablas, OperadorLogico operador, Integer indice,
            Tabla<K, F> resultante) {
        if (indice + 1 == listaTablas.size()) {
            return resultante;
        }

        if (operador == OperadorLogico.AND) {
            return ConcatenarOperando(listaTablas, operador, indice + 1,
                    TablaAnd(listaTablas.get(indice), listaTablas.get(indice + 1)));
        } else {
            return ConcatenarOperando(listaTablas, operador, indice + 1,
                    TablaOr(listaTablas.get(indice), listaTablas.get(indice + 1)));
        }

    }

    private Tabla<K, F> TablaAnd(Tabla<K, F> t1, Tabla<K, F> t2) throws FiltroInvalido {
        /*
         * List<Etiqueta<F>> filasResultantes = new ArrayList<Etiqueta<F>>();
         * for(Etiqueta<F> eFila1 : t1.etiquetas_fila){
         * for(Etiqueta<F> eFila2 : t2.etiquetas_fila)
         * if(eFila1.equals(eFila2)) filasResultantes.add(eFila1);
         * }
         */

        Set<Etiqueta<F>> eFilas1 = new HashSet<Etiqueta<F>>(t1.etiquetas_fila);
        Set<Etiqueta<F>> eFilas2 = new HashSet<Etiqueta<F>>(t2.etiquetas_fila);
        eFilas1.retainAll(eFilas2);

        if (eFilas1.isEmpty())
            throw new FiltroInvalido("El criterio de filtro no arrojó ningún resultado.");

        return t1.subtablaFilas(new ArrayList<Etiqueta<F>>(eFilas1));
    }

    private Tabla<K, F> TablaOr(Tabla<K, F> t1, Tabla<K, F> t2) {

        Set<Etiqueta<F>> cjtoFilas1 = new HashSet<Etiqueta<F>>(t1.etiquetas_fila);
        Set<Etiqueta<F>> cjtoFilas2 = new HashSet<Etiqueta<F>>(t2.etiquetas_fila);
        cjtoFilas1.addAll(cjtoFilas2);

        for (Etiqueta<F> eFila : t2.etiquetas_fila) {
            if (!t1.getEtiquetas_fila().contains(eFila)) {
                Object[] fila = new Object[t1.getCantidadColumnas()];
                Integer indice = t2.getIndiceFilas(eFila);
                for (int i = 0; i < t2.getCantidadColumnas(); i++)
                    fila[i] = t2.getListaColumnas().get(i).valorCelda(indice);
                t1.agregarFila(eFila.nombre, fila);
            }
        }

        if (cjtoFilas1.isEmpty())
            throw new FiltroInvalido("El criterio de filtro no arrojó ningún resultado.");

        return t1.subtablaFilas(new ArrayList<Etiqueta<F>>(cjtoFilas1));
    }

    public Integer getIndiceFilas(Etiqueta<F> etiqueta) {
        for (int i = 0; i < this.etiquetas_fila.size(); i++) {
            if (etiqueta.equals(etiquetas_fila.get(i)))
                return i;
        }
        return -1;
    }

    public Tabla<K, String> groupbyTabla(List<K> nombre_etiquetas, MetodoAgregacion operacion, Boolean omitirNA)
            throws FiltroInvalido {

        List<Etiqueta<K>> etiquetasAFiltrar = new ArrayList<Etiqueta<K>>();
        List<HashSet<String>> valores = new ArrayList<HashSet<String>>();

        for (K nombre_etiqueta : nombre_etiquetas) {
            Etiqueta<K> enueva = new Etiqueta<K>(nombre_etiqueta);
            if (!tabla.keySet().contains(enueva))
                throw new FiltroInvalido(
                        "La etiqueta " + nombre_etiqueta + " no se encuentra en el encabezado de la tabla.");

            etiquetasAFiltrar.add(enueva);
            HashSet<String> set = new HashSet<>();
            for (Celda<?> c : tabla.get(enueva).obtenerValores())
                set.add(c.obtenerValor().toString());

            valores.add(set);
        }
        List<String> valoresCombinados = generarCombinaciones(valores, 0, "");

        List<Etiqueta<K>> etiquetasNuevasColumna = new ArrayList<Etiqueta<K>>();
        List<Etiqueta<String>> etiquetasNuevasFila = new ArrayList<Etiqueta<String>>();

        for (Etiqueta<K> e : getEtiquetas_columna()) {
            if (!etiquetasAFiltrar.contains(e)
                    && (tabla.get(e).tipo().equals("Integer") || tabla.get(e).tipo().equals("Double"))) {
                etiquetasNuevasColumna.add(e);
            }
        }
        Object[][] columnasNuevas = new Number[etiquetasNuevasColumna.size()][valoresCombinados.size()];

        List<Etiqueta<K>> etiquetasParaOperar = new ArrayList<Etiqueta<K>>(etiquetasAFiltrar);
        etiquetasParaOperar.addAll(etiquetasNuevasColumna);

        for (int i = 0; i < valoresCombinados.size(); i++) {
            etiquetasNuevasFila.add(new Etiqueta<String>(valoresCombinados.get(i)));
            String[] valoresAFiltrar = valoresCombinados.get(i).split(",");
            List<Predicate<Celda>> filtros = new ArrayList<Predicate<Celda>>();
            List<Number> resultados = new ArrayList<Number>();

            for (String valor : valoresAFiltrar)
                filtros.add(new Filtrador().generadorPredicate(Operador.IGUAL, convertirTipo(valor)));

            try {
                Tabla<K, F> tablaFiltrada = subtablaColumnas(etiquetasParaOperar).filtrar(nombre_etiquetas, filtros,
                        OperadorLogico.AND);

                if (operacion == MetodoAgregacion.Max)
                    resultados = tablaFiltrada.max(etiquetasNuevasColumna, omitirNA);
                else if (operacion == MetodoAgregacion.Min)
                    resultados = tablaFiltrada.min(etiquetasNuevasColumna, omitirNA);
                else if (operacion == MetodoAgregacion.Count)
                    resultados = tablaFiltrada.count(etiquetasNuevasColumna);
                else if (operacion == MetodoAgregacion.Sum)
                    resultados = tablaFiltrada.sum(etiquetasNuevasColumna, omitirNA);
                else if (operacion == MetodoAgregacion.Media)
                    resultados = tablaFiltrada.promedio(etiquetasNuevasColumna, omitirNA);
                else if (operacion == MetodoAgregacion.Varianza)
                    resultados = tablaFiltrada.varianza(etiquetasNuevasColumna, omitirNA);
                else if (operacion == MetodoAgregacion.DesvioEstandar)
                    resultados = tablaFiltrada.desvioEstandar(etiquetasNuevasColumna, omitirNA);

                for (int j = 0; j < resultados.size(); j++) {
                    columnasNuevas[j][i] = resultados.get(j);
                }
            } catch (FiltroInvalido e) {
                continue;
            }
        }

        return new Tabla<K, String>(etiquetasNuevasFila, etiquetasNuevasColumna, columnasNuevas);

    }

    private List<String> generarCombinaciones(List<HashSet<String>> conjuntos, int indice, String combinacionActual) {
        List<String> resultado = new ArrayList<>();

        if (indice == conjuntos.size()) {
            resultado.add(combinacionActual.substring(1)); // Quita la coma inicial
            return resultado;
        }

        HashSet<String> conjuntoActual = conjuntos.get(indice);
        for (String elemento : conjuntoActual) {
            resultado.addAll(generarCombinaciones(conjuntos, indice + 1, combinacionActual + "," + elemento));
        }

        return resultado;
    }

    // --------------------------------Rellenar NA------------------

    @Override
    public void rellenarNA(K eColumna, Object valor) {
        Etiqueta<K> enueva = new Etiqueta<>(eColumna);
        if (!tabla.keySet().contains(enueva))
            throw new EtiquetaInvalida("La etiqueta " + eColumna + " no se encuentra en el encabezado de la tabla.");

        tabla.get(enueva).rellenarNA(valor);

    }

    // --------------------------------CONCATENACION------------------

    public static <K, F> Tabla<K, F> concatenar(Tabla<K, F> a, Tabla<K, ?> b) {
        if (!(a.getCantidadFilas().equals(b.getCantidadFilas()))) {
            throw new FormatoTablaInvalido("Las tablas no tienen la misma cantidad de filas");
        }

        List<Columna<?>> columnas = a.getListaColumnas();

        for (Columna<?> col : b.getListaColumnas()) {
            columnas.add(col);
        }

        List<Etiqueta<F>> etiquetasFila = a.etiquetas_filas();

        List<Etiqueta<K>> etiquetasCol = a.etiquetas_columnas();

        for (Etiqueta<K> etiq : b.etiquetas_columnas()) {
            etiquetasCol.add(etiq);
        }

        return new Tabla<K, F>(etiquetasFila, etiquetasCol, columnas);

    }

    // ----------------------------------SUMARIZACION-----------------
    public <T> Double sum(K col, Boolean omitirNA) {
        Columna<T> columna = obtenerColumna(col);
        Double suma = columna.sum(omitirNA);
        return suma;
    }

    private <T> List<Number> sum(List<Etiqueta<K>> eColumnas, Boolean omitirNA) {
        List<Number> res = new ArrayList<Number>();
        for (Etiqueta<K> etiq : eColumnas)
            try {
                res.add(sum(etiq.nombre, omitirNA));
            } catch (ColumnaInvalida e) {
                continue;
            }
        return res;
    }

    public <T> Number max(K col, Boolean omitirNA) {
        Columna<T> columna = obtenerColumna(col);
        Number maximo = columna.max(omitirNA);
        return maximo;
    }

    private <T> List<Number> max(List<Etiqueta<K>> eColumnas, Boolean omitirNA) {
        List<Number> res = new ArrayList<Number>();
        for (Etiqueta<K> etiq : eColumnas)
            try {
                res.add(max(etiq.nombre, omitirNA));
            } catch (ColumnaInvalida e) {
                continue;
            }
        return res;
    }

    public <T> Number min(K col, Boolean omitirNA) {
        Columna<T> columna = obtenerColumna(col);
        Number minimo = columna.min(omitirNA);
        return minimo;
    }

    private <T> List<Number> min(List<Etiqueta<K>> eColumnas, Boolean omitirNA) {
        List<Number> res = new ArrayList<Number>();
        for (Etiqueta<K> etiq : eColumnas)
            try {
                res.add(min(etiq.nombre, omitirNA));
            } catch (ColumnaInvalida e) {
                continue;
            }
        return res;
    }

    public <T> Double promedio(K col, Boolean omitirNA) {
        Columna<T> columna = obtenerColumna(col);
        Double promedio = columna.promedio(omitirNA);
        return promedio;
    }

    private <T> List<Number> promedio(List<Etiqueta<K>> eColumnas, Boolean omitirNA) {
        List<Number> res = new ArrayList<Number>();
        for (Etiqueta<K> etiq : eColumnas)
            try {
                res.add(promedio(etiq.nombre, omitirNA));
            } catch (ColumnaInvalida e) {
                continue;
            }
        return res;
    }

    public <T> Integer count(K col) {
        Columna<T> columna = obtenerColumna(col);
        Integer cuenta = columna.count();
        return cuenta;
    }

    private <T> List<Number> count(List<Etiqueta<K>> eColumnas) {
        List<Number> res = new ArrayList<Number>();
        for (Etiqueta<K> etiq : eColumnas)
            try {
                res.add(count(etiq.nombre));
            } catch (ColumnaInvalida e) {
                continue;
            }
        return res;
    }

    public <T> Double varianza(K col, Boolean omitirNA) {
        Columna<T> columna = obtenerColumna(col);
        Double varianza = columna.varianza(omitirNA);
        return varianza;
    }

    private <T> List<Number> varianza(List<Etiqueta<K>> eColumnas, Boolean omitirNA) {
        List<Number> res = new ArrayList<Number>();
        for (Etiqueta<K> etiq : eColumnas)
            try {
                res.add(varianza(etiq.nombre, omitirNA));
            } catch (ColumnaInvalida e) {
                continue;
            }
        return res;
    }

    public <T> Double desvioEstandar(K col, Boolean omitirNA) {
        Columna<T> columna = obtenerColumna(col);
        Double sd = columna.desvioEstandar(omitirNA);
        return sd;
    }

    private <T> List<Number> desvioEstandar(List<Etiqueta<K>> eColumnas, Boolean omitirNA) {
        List<Number> res = new ArrayList<Number>();
        for (Etiqueta<K> etiq : eColumnas)
            try {
                res.add(desvioEstandar(etiq.nombre, omitirNA));
            } catch (ColumnaInvalida e) {
                continue;
            }
        return res;
    }

    public Tabla<String, String> resumen(Boolean omitirNA) {
        int contador = 0;

        // Inicializo columnas de tabla nueva
        Integer columnasNumericas = getCantidadColumnasNumericas();
        Double[] sumas = new Double[columnasNumericas];
        Number[] maximos = new Number[columnasNumericas];
        Number[] minimos = new Number[columnasNumericas];
        Double[] promedios = new Double[columnasNumericas];
        Integer[] cantidades = new Integer[columnasNumericas];
        Double[] varianzas = new Double[columnasNumericas];
        Double[] desvios = new Double[columnasNumericas];
        // Etiquetas de filas y columnas
        List<Etiqueta<K>> etiquetasColumnas = getEtiquetasColumnasNumericas();
        String[] arrayetiquetaFilas = new String[columnasNumericas];
        String[] arrayetiquetasColumnas = { "Suma", "Max", "Min", "Promedio", "Cantidad", "Varianza", "SD" }; // Cambié
                                                                                                              // el
                                                                                                              // orden
                                                                                                              // para
        // coincidir con la matriz

        // Matriz para almacenar los valores de las 7 operaciones
        Object[][] columnas = new Object[7][columnasNumericas]; // 7 operaciones y 'columnasNumericas' columnas

        for (Columna<?> columna : getListaColumnas()) {
            if (columna.tipo().equals("String") || columna.tipo().equals("Boolean")) {
                continue;
            }

            // Asignación de valores por cada columna (que pasa ser fila)
            Double sum = columna.sum(omitirNA);
            sumas[contador] = sum;
            columnas[0][contador] = sum; // Suma en la columna 0

            Number max = columna.max(omitirNA);
            maximos[contador] = max;
            columnas[1][contador] = max; // Max en la columna 1

            Number min = columna.min(omitirNA);
            minimos[contador] = min;
            columnas[2][contador] = min; // Min en la columna 2

            Double promedio = columna.promedio(omitirNA);
            promedios[contador] = promedio;
            columnas[3][contador] = promedio; // Promedio en la columna 3

            Integer cuenta = columna.count();
            cantidades[contador] = cuenta;
            columnas[4][contador] = cuenta; // Cuenta en la columna 4

            Double varianza = columna.varianza(omitirNA);
            varianzas[contador] = varianza;
            columnas[5][contador] = varianza; // Varianza en la columna 5

            Double desvioEstandar = columna.desvioEstandar(omitirNA);
            desvios[contador] = desvioEstandar;
            columnas[6][contador] = desvioEstandar; // Varianza en la columna 6

            // Etiqueta de fila
            arrayetiquetaFilas[contador] = String.valueOf(etiquetasColumnas.get(contador).getNombre());

            contador++;
        }

        Tabla<String, String> resumen = new Tabla<>(arrayetiquetaFilas, arrayetiquetasColumnas, columnas);

        return resumen;
    }

    private static Object convertirTipo(String valor) {

        // Intentar convertir a Boolean
        if (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(valor);
        }

        // Intentar convertir a Number (Integer o Double)
        try {
            if (valor.contains(".")) {
                return Double.parseDouble(valor);
            } else {
                return Integer.parseInt(valor);
            }
        } catch (NumberFormatException e) {
            // Si falla la conversión a Number, retornar el valor como String
            return valor;
        }
    }

}