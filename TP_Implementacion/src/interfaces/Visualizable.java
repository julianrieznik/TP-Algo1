package interfaces;

public interface Visualizable<T> {

    void ver(Integer cantidad_caracteres);
    void verFila(String etiqueta_fila, Integer cantidad_caracteres);
    void verFila(Integer indice_fila, Integer cantidad_caracteres);
    void verColumna(String etiqueta_columna);
    void verColumna(Integer indice_columna);

}
 