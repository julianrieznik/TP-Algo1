public class Celda<T> {
    private T valor;
    private String tipo;

    public Celda(T valor) {
        if (valor instanceof String || valor instanceof Number || valor instanceof Boolean) {
            this.valor = valor;
            this.tipo = valor.getClass().getSimpleName();  
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

    public String tipo() {
        return tipo;
    }

    public static void main(String[] args) {
        Celda celda = new Celda<Integer>(3);
        System.out.println(celda.tipo());
    }
}
