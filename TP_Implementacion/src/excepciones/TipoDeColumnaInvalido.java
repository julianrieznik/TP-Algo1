package excepciones;

public class TipoDeColumnaInvalido extends RuntimeException {

    public TipoDeColumnaInvalido(String mensaje) {
        super(mensaje);
    }
}
