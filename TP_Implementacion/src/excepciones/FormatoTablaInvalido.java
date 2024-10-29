package excepciones;

public class FormatoTablaInvalido extends RuntimeException {

    public FormatoTablaInvalido(String mensaje) {
        super(mensaje);
    }
}