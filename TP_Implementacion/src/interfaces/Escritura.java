package interfaces;

import excepciones.FormatoTablaInvalido;

public interface Escritura<T>{
    void escribirCSV(T contenido, String ruta);
}
