package excepciones;

public class OrdenInvalido extends RuntimeException{
    public OrdenInvalido(String mensaje) {
        super(mensaje);
    }
}
