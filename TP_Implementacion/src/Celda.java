import excepciones.CeldaInvalida;

public class Celda<T> implements Comparable<Celda<T>>{
    private T valor;
    private String tipo;

    public Celda(T valor) {
        if (valor instanceof String || valor instanceof Number || valor instanceof Boolean) {
            this.valor = valor;
            this.tipo = valor.getClass().getSimpleName();  
        }else if (valor == null){
            this.valor = valor;
            this.tipo = "Object";
        } 
        else {
            throw new CeldaInvalida("El valor debe ser de tipo String, Number o Boolean");
        }
    }

    public T obtenerValor() {
        return valor;
    }

    private boolean chequeartipo(Object valor) {
        return valor.getClass().getSimpleName().equals(this.tipo);
    }
    
    public void modificar(Object valor) {
        if (chequeartipo(valor)){
            this.valor = (T) valor;
        }
        else if (this.valor == null){
            this.valor = (T) valor;
            this.tipo = valor.getClass().getSimpleName();
        }
        else{
            //HACER EXEPCION PROPIA
            throw new CeldaInvalida("El valor debe ser de tipo " + tipo() + " o usar metodo cambiarTipoColumna()");
        }
        
    }

    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((valor == null) ? 0 : valor.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Celda other = (Celda) obj;
        if (valor == null) {
            if (other.valor != null)
                return false;
        } else if (!valor.equals(other.valor))
            return false;
        return true;
    }

    @Override
    public int compareTo(Celda<T> o) {
        if(this.getClass() != o.getClass()) throw new CeldaInvalida("Las celdas que se quieren comparar son de distinto tipo.");
        
        if(this.valor instanceof Boolean) return Boolean.compare((Boolean) this.valor, (Boolean)  o.valor);
        else if(this.valor instanceof Double) return Double.compare((Double) this.valor, (Double) o.valor);
        else if(this.valor instanceof Float) return Float.compare((Float) this.valor, (Float) o.valor);
        else if(this.valor instanceof Integer) return Integer.compare((Integer) this.valor, (Integer) o.valor);
        else return ((String) this.valor).compareTo((String) o.valor);
    }

    public int comparador(T v){
        if (v == null || this.valor == null) return 0;
        return this.compareTo(new Celda<T>(v));
    }

    public boolean igual(T v){
        return this.equals(new Celda<T>(v));
    }
    

    public String tipo() {
        return tipo;
    }

    public static void main(String[] args) {
        Celda<Integer> celda = new Celda<Integer>(3);
        System.out.println(celda.tipo());
    }

    


}
