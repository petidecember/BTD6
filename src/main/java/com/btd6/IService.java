import java.util.List;
import java.util.UUID;

public interface IService<T> {
	
	boolean insert(T o);
	
	T findById(UUID uuid);
	
	List<? extends T> getAll(); 
	
	boolean update(T o);
	
	boolean delete(UUID uuid);
	
}
