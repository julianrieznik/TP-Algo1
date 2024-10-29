package excepciones;

public class EtiquetaInvalida extends RuntimeException {

    public EtiquetaInvalida(String mensaje) {
        super(mensaje);
    }
}