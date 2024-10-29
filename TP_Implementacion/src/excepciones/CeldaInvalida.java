package excepciones;

public class CeldaInvalida extends RuntimeException{


    public CeldaInvalida(String mensaje) {
        super(mensaje);
    }
}
