package excepciones;

public class OperacionInvalida extends RuntimeException{
    
    public OperacionInvalida(String mensaje) {
        super(mensaje);
    }
}
