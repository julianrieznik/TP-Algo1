import excepciones.ColumnaInvalida;
import excepciones.EtiquetaInvalida;
import excepciones.FilaInvalida;
import excepciones.FiltroInvalido;
import excepciones.FormatoTablaInvalido;
import excepciones.IndiceInexistente;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class Tabla<K, F> implements interfaces.Agregable<Tabla<K, F>>, interfaces.Copiable<Tabla<K, F>>,
        interfaces.Visualizable<Tabla<K, F>>, interfaces.Proyectable<Tabla<K, F>, Etiqueta<K>, Etiqueta<F>>,
        interfaces.Ordenable<Tabla<K, F>, K>, interfaces.Filtrable<Tabla<K, F>, K, Object> {
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

    // Constructor con lista de etiqFilas, etiqColumnas y lista de Columnas. Uso
    // interno
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
    public Tabla<K, F> subtabla(List<Etiqueta<F>> listFilas, List<Etiqueta<K>> listColumnas) {
        if (listColumnas.isEmpty())
            listColumnas = getEtiquetas_columna();

        Tabla<K, F> tablaNueva = subtablaColumnas(listColumnas);

        if (listFilas.isEmpty())
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


    //-------------------------------------ORDENAR--------------------------------------------
    @Override
    public Tabla<K, F> ordenar(List<K> lista, boolean asc_desc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ordenar'");
    }
    /* 
    @Override
    public Tabla<K,F> ordenar(List<K> lista, boolean asc_desc) {
        List<Etiqueta<K>> listEtiq = new ArrayList<>();
        for (K elem: lista){ 
            Etiqueta<K> etiqueta_columna = new Etiqueta<>(elem);
            listEtiq.add(etiqueta_columna);
        }
        //Tabla<Etiqueta<K>, Columna<?>> tablaOriginal = new Tabla<>(this.getEtiquetas_columna(), this.getTabla());
        //Tabla<Etiqueta<K>, Columna<?>> tablaOrdenada = new Tabla<>();

        //tablaOrdenada.copiar(this);;
        Columna<?> columna = null;

        for (Map.Entry<Etiqueta<K>, Columna<?>> entrada : tabla.entrySet()) {
            if (entrada.getKey().equals(listEtiq.get(0))){
                columna = entrada.getValue();
            }
            
        }
        if (columna != null) {
            // Crear una lista de índices (0, 1, 2, ...) para representar las posiciones originales
            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < columna.cantidadCeldas(); i++) {
                indices.add(i);
            }

            if (asc_desc == true) {
                if (columna.valorCelda(0) instanceof Number) {

                    indices.sort((i, j) -> Double.compare(((Number)columna.valorCelda(i)).doubleValue(), ((Number)columna.valorCelda(j)).doubleValue())); //columna numérica ordenada ascendentemente. 

                }else if(columna.valorCelda(0) instanceof String) {

                    //pendiente de revisar. puede que tenga error. 
                    Collections.sort(columna.obtenerValores(), (c1, c2) ->((String)c1.obtenerValor()).compareTo((String)c2.obtenerValor()));

                }else if (columna.valorCelda(0) instanceof Boolean) {
                    indices.sort((i1, i2) -> Boolean.compare(!((Boolean)columna.valorCelda(i1)), !((Boolean)columna.valorCelda(i2))));  
                }

            }else if (asc_desc == false) {

                if (columna.valorCelda(0) instanceof Number) {

                    indices.sort((i, j) -> Double.compare(((Number)columna.valorCelda(j)).doubleValue(), ((Number)columna.valorCelda(i)).doubleValue())); //columna numérica ordenada ascendentemente. 

                }else if(columna.valorCelda(0) instanceof String) {

                    //pendiente de revisar. puede que tenga error. 
                    Collections.sort(columna.obtenerValores(), (c1, c2) -> ((String)c2.obtenerValor()).compareTo((String)c1.obtenerValor()));

                }else if (columna.valorCelda(0) instanceof Boolean) {
                    indices.sort((i1, i2) -> Boolean.compare(((Boolean)columna.valorCelda(i1)), ((Boolean)columna.valorCelda(i2))));  
                }
            }

            List<Etiqueta<K>> listEtiquetasCol = new ArrayList<Etiqueta<K>>();
            List<Columna<?>> listColumnas = new ArrayList<Columna<?>>();
            
            // Reordenar todas las columnas usando los índices ordenados
            for (Map.Entry<Etiqueta<K>, Columna<?>> entry : tabla.entrySet()) {
                Columna<?> columnaSinOrden = entry.getValue();
                listEtiquetasCol.add(entry.getKey());
                Columna<?> columnaOrdenada = new Columna<>(new ArrayList<>(Collections.nCopies(columnaSinOrden.cantidadCeldas(), null)));
                for (int index : indices) {
                    //columnaOrdenada.agregarValor(columnaSinOrden.obtenerCelda(index));
                }
                listColumnas.add(columnaOrdenada);
                entry.setValue(columnaOrdenada);
            }

            return new Tabla<K,F>(listEtiquetasCol, listColumnas);
        }
    }
    */

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
    public void agregarFila(String etiqueta, Object[] fila) {
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
    public void agregarColumna(Object etiq, Object[] columna) {
        if (columna.length != getCantidadFilas()) {
            throw new ColumnaInvalida("La columna nueva deber ser de largo " + String.valueOf(getCantidadFilas()));
        }
        try {
            K etiqueta_col = (K) etiq;
            Etiqueta<K> etiqueta = new Etiqueta<>(etiqueta_col);
            Columna<?> columnaCasteada = castearColumna(columna);
            tabla.put((Etiqueta<K>) etiqueta, columnaCasteada);
        } catch (ClassCastException e) {
            throw new EtiquetaInvalida("Etiqueta de columna invalida");
        }
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

    // --------------------------------FILTRADO---------
    @Override
    public Tabla<K, F> filtrar(K etiq, Predicate<Object> criterio) throws FiltroInvalido {
        Etiqueta<K> enueva = new Etiqueta<K>(etiq);
        if (!tabla.keySet().contains(enueva))
            throw new FiltroInvalido("La etiqueta " + etiq + " no se encuentra en el encabezado de la tabla.");

        Columna<?> colFiltrada = tabla.get(enueva);
        List<Etiqueta<F>> eFilas = new ArrayList<Etiqueta<F>>();

        for (int i = 0; i < getCantidadFilas(); i++) {
            if (criterio.test(colFiltrada.valorCelda(i)))
                eFilas.add(etiquetas_fila.get(i));
        }
        if (eFilas.isEmpty())
            throw new FiltroInvalido("El criterio de filtro no arrojó ningún resultado.");
        return subtablaFilas(eFilas);
    }

    @Override
    public Tabla<K, F> filtrar(List<K> etiq, Predicate<List<Object>> criterio) throws FiltroInvalido {
        List<Etiqueta<K>> etiquetas = new ArrayList<Etiqueta<K>>();
        List<Columna<?>> colFiltradas = new ArrayList<Columna<?>>();
        for( K e : etiq){
            Etiqueta<K> enueva = new Etiqueta<K>(e);
            if (!tabla.keySet().contains(enueva))
            throw new FiltroInvalido("La etiqueta " + e + " no se encuentra en el encabezado de la tabla.");
            etiquetas.add(enueva);
            colFiltradas.add(tabla.get(enueva));
        }

        List<Etiqueta<F>> eFilas = new ArrayList<Etiqueta<F>>();

        for (int i = 0; i < getCantidadFilas(); i++) {
            List<Object> fila = new ArrayList<Object>();
            //if (criterio.test(colFiltradas));
                //eFilas.add(etiquetas_fila.get(i));
        }
        if (eFilas.isEmpty())
            throw new FiltroInvalido("El criterio de filtro no arrojó ningún resultado.");
        return subtablaFilas(eFilas);
    }

    
    public Tabla<String,String> groupbyTabla(List<K> nombre_etiquetas , String operacion){
        List<Etiqueta<K>> etiquetas = new ArrayList<Etiqueta<K>>();
        List<HashSet<String>> valores = new ArrayList<HashSet<String>>();
        for(K nombre_etiqueta : nombre_etiquetas){
            Etiqueta<K> enueva = new Etiqueta<K>(nombre_etiqueta);
            if (!tabla.keySet().contains(enueva))
            throw new FiltroInvalido("La etiqueta " + nombre_etiqueta + " no se encuentra en el encabezado de la tabla.");
            etiquetas.add(enueva);
            HashSet<String> set = new HashSet<>();
            for (Celda<?> c : tabla.get(enueva).obtenerValores()){
                set.add(c.obtenerValor().toString());
            }
            valores.add(set);
        }
        List<String> valoresCombinados = generarCombinaciones(valores,0,"");

        //Aplicar filtro para cada uno de los valores de valoresCombinados
        //Obtener las columnasDeseadas (tabla.keySet() - etiquetas - columnas no numericas)
        //Aplicar subtabla para las columnasDeseadas
        //Realizar operaciones en tabla resultante 
        //Formatear resultados
        //Llamar a Constructor: Tabla(columnasDeseadas, valoresCombinados, resultados)

        return null;

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

}