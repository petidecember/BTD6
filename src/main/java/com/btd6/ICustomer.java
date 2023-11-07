import java.util.UUID;

public interface ICustomer {

   String getFirstname();

   void setFirstname(String firstName);
   
   UUID getUuid();
   
   void setUuid(UUID id);

   String getLastname();

   void setLastname(String lastName);
}
