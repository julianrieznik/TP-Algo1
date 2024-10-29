package interfaces;
import java.util.List;
public interface Ordenable<T,E> {
    T ordenar(List<E> lista, boolean asc_desc);
}
