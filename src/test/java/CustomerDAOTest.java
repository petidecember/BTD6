import com.btd6.Customer;
import com.btd6.ICustomerDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class CustomerDAOTest {

    final static Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test", "", "");
    ICustomerDAO customer;
    Handle handle;
   @BeforeAll
   public static void init() {

       jdbi.installPlugin(new SqlObjectPlugin());
   }
    @BeforeEach
    public void setup() {
        handle = jdbi.open();
        customer = handle.attach(ICustomerDAO.class);
        customer.createTable();
        customer.insert(new Customer("Josef", "ettl"));
        customer.insert(new Customer("Josef", "attl"));
        customer.insert(new Customer("Josef", "uttl"));
    }
    @AfterEach
    public void undo() {
       handle.close();
    }
    public void remove() {
       customer.truncate();
    }
    @Test
    void name() {
        Customer customer1 = customer.getAll().get(0);
        Customer josef = new Customer("Josef", "uttl");
        assertEquals(customer1, josef.getFirstname());
    }

    @Test
    public void insert() {



    }

}
