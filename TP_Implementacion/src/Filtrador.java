import java.util.function.Predicate;

public class Filtrador {

    public static Predicate<Celda> generadorPredicate(Operador operador , Object valor ){
        if(operador == Operador.IGUAL) return a -> a.igual(valor);
        else if (operador == Operador.MAYOR) return a -> a.comparador(valor) < 0;
        else return a -> a.comparador(valor) > 0;
    }
}
