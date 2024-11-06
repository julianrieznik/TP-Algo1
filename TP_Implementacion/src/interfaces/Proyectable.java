package interfaces;
import java.util.List;

public interface Proyectable<T,K,F> {
    T subtabla(List<F> listFilas, List<K> listColumnas);
    T head(int n);
    T tail(int n);
}
