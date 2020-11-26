package com.example.servicenovigrad;

import com.example.servicenovigrad.services.DocumentTemplate;
import com.example.servicenovigrad.services.FieldTemplate;
import com.example.servicenovigrad.services.Service;
import com.example.servicenovigrad.userManagement.Admin;
import com.example.servicenovigrad.userManagement.Customer;
import com.example.servicenovigrad.userManagement.Employee;
import com.example.servicenovigrad.userManagement.User;
import com.example.servicenovigrad.userManagement.UserAccount;
import com.example.servicenovigrad.userManagement.Workday;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ObjectManipulationTests {
    @Test
    public void checkAdminFirstName() {
        Admin admin=new Admin(new User("Wow",null,null,null));
        assertEquals("Wow", admin.getFirstName());
    }

    @Test
    public void checkGetRoleCustomer() {
        Customer customer=new Customer(new User("Wow",null,null,null));
        assertEquals("Customer", customer.getRole());
    }

    @Test
    public void checkGetRoleEmployee() {
        Employee employee=new Employee(new User("Wow",null,null,null));
        assertEquals("Employee", employee.getRole());
    }

    @Test
    public void checkServiceName() {
       Service service = new Service(1,"wow",new ArrayList<DocumentTemplate>(), new ArrayList<FieldTemplate>());
        assertEquals("wow", service.getName());
    }

    @Test
    public void checkUserAccountCreateAccount() {
        UserAccount yolo=UserAccount.createAccount("Employee",
                new User(null,null,null,"woohoo"));
        assertEquals("woohoo",yolo.getPassword());
    }

    @Test
    public void checkOfferingToString() {
        Employee.Offering offering = new Employee.Offering(new Service(1,"wow",
                new ArrayList<DocumentTemplate>(), new ArrayList<FieldTemplate>()),true);
        assertEquals("wow (Offered)", offering.toString());
    }

    @Test
    public void checkWorkDayToCompressedString() {
        ArrayList<Workday> alw = new ArrayList<Workday>();
        String values="1010101";
        for(int dayInt=0 ; dayInt<=6; dayInt++){
            alw.add(new Workday(dayInt,values.charAt(dayInt)=='1'));
        }
        assertEquals(values, Workday.toCompressedString(alw));
    }
}
