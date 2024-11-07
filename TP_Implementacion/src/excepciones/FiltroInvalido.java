package excepciones;



public class FiltroInvalido extends RuntimeException {

    public FiltroInvalido(String mensaje) {
        super(mensaje);
    }
}