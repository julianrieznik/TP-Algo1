import excepciones.EtiquetaInvalida;

public class Etiqueta<T> {
    public T nombre;

    public Etiqueta(T nombre) {
        if (nombre instanceof String || nombre instanceof Number){
        this.nombre = nombre;
        }
        //HACER EXCEPCION PROPIA
        else throw new EtiquetaInvalida("La etiqueta debe ser string o number");
    }

    public T getNombre() {
        return nombre;
    }
    
}
