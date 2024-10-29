package interfaces;
import java.util.List;

public interface Proyectable<T,E> {
    T subtabla(List<E> listFilas, List<E> listColumnas);
    T subtabla(List<E> list, boolean FoC);
    T head(int n);
    T tail(int n);
}
