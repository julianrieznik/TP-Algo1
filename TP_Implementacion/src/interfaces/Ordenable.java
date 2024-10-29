package interfaces;
import java.util.List;
public interface Ordenable<T,K> {
    T ordenar(List<K> lista, boolean asc_desc); //True -> Ascendente
}
