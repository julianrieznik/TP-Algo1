
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import excepciones.CeldaInvalida;
import excepciones.ColumnaInvalida;
import excepciones.ValorNoAgregable;

public class Columna<E> {
    private List<Celda<E>> columna;
    private String tipo;

    public Columna(E[] lista) {
        List<Celda<E>> columna = generarColumna(lista);
        this.columna = columna;
        this.tipo = "";
        if (columna.size() != 0) {
            if (this.chequearTipo(columna)) {
                for (int i = 0; i < columna.size(); i++) {
                    if (columna.get(i).obtenerValor() != null) {
                        this.tipo = columna.get(i).tipo();
                        break;
                    }
                }
                if (this.tipo == "")
                    this.tipo = columna.get(0).tipo();

            } else {
                throw new ColumnaInvalida("Las celdas de una columna deben ser del mismo tipo");
            }
        }
    }


    public Columna(List<Celda<E>> celdas) {
        this.columna = celdas;
        this.tipo = "";

        if (celdas.size() != 0) {
            if (this.chequearTipo(celdas)) {
                this.tipo = celdas.get(0).tipo();
            } else {
                throw new ColumnaInvalida("Las celdas de una columna deben ser del mismo tipo");
            }
        }
    }

    public Columna<E> copiaProfunda() {
        List<Celda<E>> copiaColumna = new ArrayList<>();
        
        // Iteramos sobre cada celda en la columna actual y agregamos su copia profunda
        for (Celda<E> celda : this.columna) {
            copiaColumna.add(celda.copiaProfunda());
        }

        // Creamos una nueva instancia de Columna usando el constructor de lista
        Columna<E> copia = new Columna<>(copiaColumna);
        copia.tipo = this.tipo;  // Copiar el tipo
        return copia;
    }

    public boolean todosSonNull() {
        for (Celda<E> celda : columna) {
            E valor = celda.obtenerValor();
            // Verificamos si el valor es null o la cadena "null"
            if (valor != null) {
                return false;  // Si encontramos un valor que no es null ni "null", retornamos false
            }
        }
        return true;  // Si todos los valores son null o "null", retornamos true
    }
    private boolean chequearTipo(List<Celda<E>> lista) {
        Boolean esValida = true;
        String tipo = lista.get(0).getClass().getSimpleName();
        for (Celda<E> celda : lista) {
            if (!celda.getClass().getSimpleName().equals(tipo)) {
                esValida = false;
                break;
            }
        }
        return esValida;
    }


    public List<Celda<E>> generarColumna(E[] lista) {
        List<Celda<E>> columna = new ArrayList<>();
        for (E elemento : lista) {
            Celda<E> c = new Celda<>(elemento);
            columna.add(c);
        }
        return columna;
    }

    public String tipo() {
        return tipo;
    }

    public Integer cantidadCeldas() {
        return columna.size();
    }

    public List<Celda<E>> obtenerValores() {
        return this.columna;
    }

    public Celda<E> obtenerCelda(int idx) {
        return columna.get(idx);
    }

    public E obtenerValorNoNulo() {
        for (Celda<E> celda : columna) {
            if (celda.obtenerValor() != null && celda.obtenerValor() != "null") {
                return celda.obtenerValor();
            }
        }
        return null; // Retorna null si todos los valores son nulos
    }

    public String tipoCelda(Integer idx) {
        return columna.get(idx).tipo();
    }

    public E valorCelda(Integer idx) {
        return columna.get(idx).obtenerValor();
    }

    public Celda<E> getCelda(Integer indice) {
        return columna.get(indice);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Celda<E> celda : obtenerValores()) {
            String valor = String.valueOf(celda.obtenerValor());
            sb.append(valor);
            sb.append("\n");
        }
        
        sb.append("\n" + tipo());
        return sb.toString();
    }

    public void eliminarValor(int indice_valor) {
        columna.remove(indice_valor);
    }

    public void agregarValor(Object o) {
        if (o != null && !columna.isEmpty() && !columna.get(0).obtenerValor().getClass().isInstance(o)) {
            throw new ValorNoAgregable("No se puede agregar el tipo " + o.getClass().getSimpleName()
                    + " a una columna de tipo " + tipo() + " usar cambiarTipoColumna()");
        }
        try {
            E valorCasteado = (E) o;
            columna.add(new Celda<>(valorCasteado));

            if (tipo == "") {
                this.tipo = o.getClass().getSimpleName();
            }
        } catch (ClassCastException e) {
            throw new ValorNoAgregable("No se puede agregar el tipo " + o.getClass().getSimpleName()
                    + " a una columna de tipo " + tipo() + " usar cambiarTipoColumna()");
        }
    }

    public Columna<E> subColumna(List<Integer> indices) {
        List<Celda<E>> celdasNuevas = new ArrayList<Celda<E>>();

        for (Integer indice : indices) {
            celdasNuevas.add(new Celda<E>(valorCelda(indice)));
        }
        return new Columna<>(celdasNuevas);
    }

    public void modificarValorCelda(int idx, Object valor) {
        try {
            E valorCasteado = (E) valor;
            columna.get(idx).modificar(valorCasteado);
        } catch (ClassCastException e) {
            throw new ValorNoAgregable("El nuevo valor debe ser de tipo " + tipo() + " o usar cambiarTipoColumna()");
        }

    }

    protected Object[] aListaGenerica() {
        Object[] arreglo = new Object[this.columna.size()];
        int contador = 0;
        for (Celda<E> celda : this.columna) {
            Object valor = (Object) celda.obtenerValor();
            arreglo[contador] = valor;
            contador++;
        }
        return arreglo;
    }

    public List<E> aArrayList() {
        List<E> array = new ArrayList<E>();
        for (Celda<E> celda : this.columna) {
            E valor = celda.obtenerValor();
            array.add(valor);
        }

        return array;
    }

    public boolean tieneNA() {
        for (Celda<E> c : columna)
            if (c.obtenerValor() == null || c.obtenerValor() == "null")
                return true;
        return false;
    }

    public void rellenarNA(Object valor) throws CeldaInvalida{
        Boolean distintoTipo = false;

        if (this.tipo == "Object") this.tipo = valor.getClass().getSimpleName();

        else if (valor instanceof Boolean) {
            if (! this.tipo.equals("Boolean")) distintoTipo = true;
        }
        else if (valor instanceof Double) {
            if (! this.tipo.equals("Double")) distintoTipo = true;
        }
        else if (valor instanceof Integer) {
            if (! this.tipo.equals("Integer")) distintoTipo = true;
        }
        else if (valor instanceof String) {
            if (! this.tipo.equals("String")) distintoTipo = true;
        }

        if(distintoTipo) throw new CeldaInvalida("El valor debe ser de tipo " + this.tipo + " para ser agregado a esta columna o usar cambiarTipoColumna()");

        for (int i = 0; i < columna.size(); i++)
            if (columna.get(i).obtenerValor() == null || columna.get(i).obtenerValor() == "null")
                modificarValorCelda(i, valor);
    }


    private Class<?> getValorClase() {
        return !columna.isEmpty() && columna.get(0).obtenerValor() != null
            ? columna.get(0).obtenerValor().getClass()
            : Object.class;
    }


    public Double maximoValorDouble() {
        if (!(getValorClase() == Double.class)) {
            throw new UnsupportedOperationException("La columna no es de tipo Double.");
        }

        return columna.stream()
            .filter(c -> c.obtenerValor() != null)  // Filtra las celdas nulas
            .map(c -> (Double) c.obtenerValor())    // Mapea los valores a Double
            .max(Comparator.naturalOrder())          // Obtiene el valor máximo
            .orElse(null);                          // Devuelve null si no hay valores
    }

    public Double minimoValorDouble() {
        if (!(getValorClase() == Double.class)) {
            throw new UnsupportedOperationException("La columna no es de tipo Double.");
        }

        return columna.stream()
            .filter(c -> c.obtenerValor() != null)  // Filtra las celdas nulas
            .map(c -> (Double) c.obtenerValor())    // Mapea los valores a Double
            .min(Comparator.naturalOrder())          // Obtiene el valor mínimo
            .orElse(null);                          // Devuelve null si no hay valores
    }

    public Integer maximoValorInteger() {
        if (!(getValorClase() == Integer.class)) {
            throw new UnsupportedOperationException("La columna no es de tipo Integer.");
        }

        return columna.stream()
            .filter(c -> c.obtenerValor() != null)  // Filtra las celdas nulas
            .map(c -> (Integer) c.obtenerValor())    // Mapea los valores a Integer
            .max(Comparator.naturalOrder())          // Obtiene el valor máximo
            .orElse(null);                          // Devuelve null si no hay valores
    }
    public Integer minimoValorInteger() {
        if (!(getValorClase() == Integer.class)) {
            throw new UnsupportedOperationException("La columna no es de tipo Integer.");
        }

        return columna.stream()
            .filter(c -> c.obtenerValor() != null)  // Filtra las celdas nulas
            .map(c -> (Integer) c.obtenerValor())    // Mapea los valores a Integer
            .min(Comparator.naturalOrder())          // Obtiene el valor mínimo
            .orElse(null);                          // Devuelve null si no hay valores
    }
    public <T> Number max(Boolean omitirNulos) {

        if (!omitirNulos && tieneNA()) {
            return null;
        }
    
        List<E> array = aArrayList();
        Double max = null;
    
        if (tipo().equals("String") || tipo().equals("Boolean")) {
            throw new ColumnaInvalida("La columna seleccionada no es numerica");
        }
    
        for (E valor : array) {
            if (valor != null && valor instanceof Number) {
                double valorDouble = ((Number) valor).doubleValue();
                if (max == null || valorDouble > max) {
                    max = valorDouble;
                }
            }
        }
    
        return max;
    }
    

    public <T> Number min(Boolean omitirNulos) {

        if (!omitirNulos && tieneNA()) {
            return null;
        }
    
        List<E> array = aArrayList();
        Double min = null;
    
        if (tipo().equals("String") || tipo().equals("Boolean")) {
            throw new ColumnaInvalida("La columna seleccionada no es numerica");
        }
    
        for (E valor : array) {
            if (valor != null && valor instanceof Number) {
                double valorDouble = ((Number) valor).doubleValue();
                if (min == null || valorDouble < min) {
                    min = valorDouble;
                }
            }
        }
    
        return min;
    }
    


    public <T> Double sum(Boolean omitirNulos) {

        if (!omitirNulos && tieneNA()) {
            return null;
        }
    
        Double suma = 0.0;
    
        if (tipo().equals("String") || tipo().equals("Boolean")) {
            throw new ColumnaInvalida("La columna seleccionada no es numerica");
        }
    
        List<E> array = aArrayList();
    
        for (E valor : array) {
            if (valor != null) {
                if (valor instanceof Number) {  
                    suma += ((Number) valor).doubleValue();  // Convierte a double de manera segura
                }
            }
        }
    
        return suma;
    }

    public Double promedio(Boolean omitirNulos){

        if(!omitirNulos && tieneNA()){
            return null;
        }

        if (tipo().equals("String") || tipo().equals("Boolean")){
            throw new ColumnaInvalida("La columna seleccionada no es numerica");
        }
        List<E> array = aArrayList();
        Double suma = sum(omitirNulos);
        Integer cantidad = null;
        if(omitirNulos && tieneNA()){
            cantidad = 0;
            for (E valor : array){
                if(valor != null){
                    cantidad++;
                }
            }
        }
        else {
            cantidad = array.size();
        }

        if (cantidad == 0){
            return 0.0;
        }
        Double promedio = suma / cantidad;
        return promedio;

    }

    public Integer count(){
        List<Celda<E>> lista = obtenerValores();
        Integer cantidad = 0;

        for (Celda<E> celda : lista){
            if(celda.obtenerValor() != null && celda.obtenerValor() != "null"){
                cantidad++;
            }
        }
        return cantidad;
    }

    public static void main(String[] args) {
        String[] s = { "Pepe", "Luis", "aa", " " };
        Columna<String> col = new Columna<>(s);

        Double[] f = { 1.2, 3.3, 4.4 };
        Columna<Double> col1 = new Columna<>(f);

        System.out.println(col.tipo());
        System.out.println(col1.tipo());

        col1.agregarValor(1);
    }
}
