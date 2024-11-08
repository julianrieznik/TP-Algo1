package interfaces;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;


public interface Filtrable<T,K,V,O> {
    
    //T filtrar(BiFunction<K,V,Boolean> pred);
    T filtrar(K etiq, Predicate<V> pred);
    T filtrar(List<K> etiq, List<Predicate<V>> preds, O operador);
}