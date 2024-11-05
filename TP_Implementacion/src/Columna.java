
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import excepciones.CeldaInvalida;
import excepciones.ColumnaInvalida;
import excepciones.ValorNoAgregable;

public class Columna<E> {
    private List<Celda<E>> columna; 
    private String tipo;


    public Columna(E[] lista){
        List<Celda<E>> columna = generarColumna(lista);
        this.columna = columna;
    }

    public Columna(List<Celda<E>> celdas){
        if (celdas.size() == 0){
            throw new CeldaInvalida("Debe haber al menos un valor en cada celda");
        }
        if(this.chequearTipo(celdas)) {
            this.columna = celdas;
            this.tipo = celdas.get(0).getClass().getSimpleName();
        }
        else{
            throw new ColumnaInvalida("Las celdas de una columna deben ser del mismo tipo");
        }
    }
    
    
    private boolean chequearTipo(E[] lista){
        return chequearTipo(generarColumna(lista));
    }

    private boolean chequearTipo(List<Celda<E>> lista){
        Boolean esValida = true;
        String tipo = lista.get(0).getClass().getSimpleName();
        for (Celda<E> celda : lista){
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

    public Integer cantidadCeldas(){
        return columna.size();
    }

    public List<Celda<E>> obtenerValores(){
        return this.columna;
    }

    public E valorCelda(Integer idx){
        return columna.get(idx).obtenerValor();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Celda<E> celda : obtenerValores()){
            String valor = String.valueOf(celda.obtenerValor());
            sb.append(valor);
            sb.append("\n");
        }
        return sb.toString();
    }

    public void eliminarValor(int indice_valor){
        columna.remove(indice_valor);
    }

    public void agregarValor(Object o) {
        try {
            E valorCasteado = (E) o;
            columna.add(new Celda<>(valorCasteado));
        } catch (ClassCastException e) {
            throw new ValorNoAgregable(
                "No se puede agregar el tipo " + o.getClass().getSimpleName() + " a una columna de tipo " + tipo());
        }
    }

    public Columna<E> subColumna(List<Integer> indices){
        List<Celda<E>> celdasNuevas = new ArrayList<Celda<E>>();

        for(Integer indice : indices){
            celdasNuevas.add(new Celda<E>(valorCelda(indice)));
        }
        return new Columna<>(celdasNuevas);
    }
}


