package excepciones;


public class IndiceInexistente extends RuntimeException {

    public IndiceInexistente(String mensaje) {
        super(mensaje);
    }
}