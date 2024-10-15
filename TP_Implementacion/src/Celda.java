public class Celda<T> {
    private T valor;

    public Celda(T valor) {
        if (valor instanceof String || valor instanceof Number || valor instanceof Boolean) {
            this.valor = valor;  
        } else {
            //HACER EXEPCION PROPIA
            throw new IllegalArgumentException("El valor debe ser de tipo String, Number o Boolean");
        }
    }

    public T obtenerValor() {
        return valor;
    }

    public void asignarValor(T valor) {
        this.valor = valor;
    }
    
}
