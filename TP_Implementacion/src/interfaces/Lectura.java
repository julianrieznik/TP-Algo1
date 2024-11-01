package interfaces;

import java.io.IOException;

import excepciones.FormatoTablaInvalido;

public interface Lectura<T> {
    T leer(String ruta) throws IOException, FormatoTablaInvalido;
}
