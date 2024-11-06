package excepciones;


public class ValorNoAgregable extends RuntimeException {

    public ValorNoAgregable(String mensaje) {
        super(mensaje);
    }
}