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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Etiqueta<?> etiqueta = (Etiqueta<?>) obj;
        return nombre.equals(etiqueta.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }
}
