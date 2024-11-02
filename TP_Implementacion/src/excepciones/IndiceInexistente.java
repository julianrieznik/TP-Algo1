package excepciones;

import javax.management.RuntimeErrorException;

public class IndiceInexistente extends RuntimeException {

    public IndiceInexistente(String mensaje) {
        super(mensaje);
    }
}