import java.util.List;
import java.util.UUID;

public interface IDAO<T> {
	
   void createTable();
	
   void removeTable();

   // CREATE
   boolean insert(T o);

   // READ
   T findById(UUID id);
   
   // READ
   List<? extends T> getAll();
   
   // UPDATE
   boolean update(T o);
   
  
   // DELETE
   boolean delete(UUID uuid);
   
   // DELETE ALL
   void truncate();
}
