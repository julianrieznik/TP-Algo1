import java.util.ArrayList;
import java.util.List;

import excepciones.CeldaInvalida;
import excepciones.ColumnaInvalida;

public class Columna<E> {
    private List<Celda<E>> columna; 
    private String tipo;


    public Columna(E[] celdas){
        if (celdas.length == 0){

            throw new CeldaInvalida("Debe haber al menos un valor en cada celda");
        }
        if(this.chequearTipo(celdas)) {
            this.columna = generarColumna(celdas);
            this.tipo = celdas[0].getClass().getSimpleName();
        }
        else{

            throw new ColumnaInvalida("Las celdas de una columna deben ser del mismo tipo");
        }
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
        Boolean esValida = true;
        String tipo = lista[0].getClass().getSimpleName();
        for (E elemento : lista){
            if (!elemento.getClass().getSimpleName().equals(tipo)) {
                esValida = false;
                break;
            }
        }
        return esValida;
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
}


