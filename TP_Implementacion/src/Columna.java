import java.util.ArrayList;
import java.util.List;

public class Columna<E> {
    private List<Celda> columna; 
    private String tipo;


    public Columna(List<E> celdas){
        if (celdas.isEmpty()){
            //HACER EXCEPCION PROPIA
            throw new RuntimeException("Debe haber al menos un valor");
        }
        if(this.chequearTipo(celdas)) {
            this.columna = generarColumna(celdas);
        }
        else{
            //HACER EXCEPCION PROPIA
            throw new RuntimeException("Las lista deben ser del mismo tipo");
        }
    }
    
    private boolean chequearTipo(List<E> lista){
        Boolean esValida = true;
        String tipo = lista.get(0).getClass().getSimpleName();
        for (E elemento : lista){
            if (elemento.getClass().getSimpleName() != tipo) {
                esValida = false;
                break;
            }
        }
        return esValida;
    }

    private List<Celda> generarColumna(List<E> lista){
        List<Celda> columna = new ArrayList<>();
        for (E elemento : lista){
            Celda c = new Celda(elemento);
            columna.add(c);
        }
        return columna;
    }

    public String tipo() {
        return tipo;
    }
}
