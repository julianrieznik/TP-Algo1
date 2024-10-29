package excepciones;

public class ColumnaInvalida extends RuntimeException {

    public ColumnaInvalida(String mensaje) {
        super(mensaje);
    }
}
