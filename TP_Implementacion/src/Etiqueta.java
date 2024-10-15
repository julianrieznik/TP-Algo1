import javax.management.RuntimeErrorException;

public class Etiqueta<T> {
    public T nombre;

    public Etiqueta(T nombre) {
        if (nombre instanceof String || nombre instanceof Number){
        this.nombre = nombre;
        }
        //HACER EXCEPCION PROPIA
        else throw new IllegalArgumentException("La etiqueta debe ser string o number");
    }
}
