
import java.util.ArrayList;
import java.util.Arrays;
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

    /*
     * public Columna(List<Celda<E>> celdas){
     * if (celdas.size() == 0){
     * throw new CeldaInvalida("Debe haber al menos un valor en cada celda");
     * }
     * if(this.chequearTipo(celdas)) {
     * this.columna = celdas;
     * this.tipo = celdas.get(0).tipo();
     * }
     * else{
     * throw new
     * ColumnaInvalida("Las celdas de una columna deben ser del mismo tipo");
     * }
     * }
     */

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

    private boolean chequearTipo(E[] lista) {
        return chequearTipo(generarColumna(lista));
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
            Boolean aux = true;
            if (this.tipo != aux.getClass().getSimpleName()) distintoTipo = true;
        }
        else if (valor instanceof Double) {
            Double aux = 1.5;
            if (this.tipo != aux.getClass().getSimpleName()) distintoTipo = true;
        }
        else if (valor instanceof Integer) {
            Integer aux = 1;
            if (this.tipo != aux.getClass().getSimpleName()) distintoTipo = true;
        }
        else if (valor instanceof String) {
            String aux = "aux";
            if (this.tipo != aux.getClass().getSimpleName()) distintoTipo = true;
        }

        if(distintoTipo) throw new CeldaInvalida("El valor debe ser de tipo " + this.tipo + " para ser agregado a esta columna o usar cambiarTipoColumna()");

        for (int i = 0; i < columna.size(); i++)
            if (columna.get(i).obtenerValor() == null)
                modificarValorCelda(i, valor);
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
