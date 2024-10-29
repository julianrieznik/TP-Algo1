package interfaces;
import java.util.List;

public interface Proyectable<T,K,F> {
    T subtabla(List<F> listFilas, List<K> listColumnas);
    T subtabla(List<K> list, boolean FoC);
    T head(int n);
    T tail(int n);
}
