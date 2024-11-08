package interfaces;

public interface Agregable<T,F> {
    void agregarFila(F etiqueta, Object[] fila);
    void agregarFila(Object[] fila);
    void agregarColumna(String etiqueta, Object[] columna);
}
