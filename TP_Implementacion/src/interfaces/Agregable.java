package interfaces;

public interface Agregable<T> {
    void agregarFila(String etiqueta, Object[] fila);
    void agregarFila(Object[] fila);
    void agregarColumna(Object etiqueta, Object[] columna);
}
