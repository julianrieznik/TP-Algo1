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

    private boolean chequeartipo(T valor) {
        return valor.getClass().getSimpleName().equals(this.tipo);
    }
    
    public void asignarValor(T valor) {
        if (chequeartipo(valor)){
            this.valor = valor;
        }
        else{
            //HACER EXEPCION PROPIA
            throw new IllegalArgumentException("El valor debe ser de tipo " + tipo());
        }
        
    }

    public String tipo() {
        return tipo;
    }

    public static void main(String[] args) {
        Celda<Integer> celda = new Celda<Integer>(3);
        System.out.println(celda.tipo());
    }


}
