package excepciones;



public class FilaInvalida extends RuntimeException {

    public FilaInvalida(String mensaje) {
        super(mensaje);
    }
}