package net.junitpractice.springboottesting.repository;

import net.junitpractice.springboottesting.integration.AbstractContainerBaseTest;
import net.junitpractice.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)    //this will disable in-memory database
public class EmployeeRepositoryIT extends AbstractContainerBaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach             //we can keep a common code here so that the method annotated with @befopreEach will run before all test cases
    public void setup(){
        employee = Employee.builder()
                .firstName("Kartik")
                .lastName("Singh")
                .email("avis@gmail.com")
                .build();
    }
    //junit test for save employee operation
    @DisplayName("junit test for save employee operation")
    @Test
    public void givenEmployeePbject_whenSave_thenReturnSavedEmployee(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Avishkar")
//                .lastName("Singh")
//                .email("avis@gmail.com")
//                .build();

        //when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //junit test case for get all employees operation
    @DisplayName("junit test case for get all employees operation")
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList(){
        //given - precondition or setup
//        Employee employee1 = Employee.builder()
//                .firstName("Avishkar")
//                .lastName("Singh")
//                .email("avis@gmail.com")
//                .build();
        Employee employee2 = Employee.builder()
                .firstName("Rakesh")
                .lastName("Mall")
                .email("rakesh@gmail.com")
                .build();

        //when - action or behaviour that we are going to test
         employeeRepository.save(employee);
        employeeRepository.save(employee2);
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

//Junit test case for get employee by id operation
@DisplayName("Junit test case for get employee by id operation")
@Test
public void givenEmployeeId_whenFindById_thenReturnEmployee(){
    //given - precondition or setup
//    Employee employee = Employee.builder()
//            .firstName("Avishkar")
//            .lastName("Singh")
//            .email("avis@gmail.com")
//            .build();
    employeeRepository.save(employee);
    //when - action or behaviour that we are going to test
    Employee findEmployee = employeeRepository.findById(employee.getId()).get();
    //then - verify the output
    assertThat(findEmployee).isNotNull();
}
    //Junit test case for get emolpoyee by email
    @DisplayName("Junit test case for get emolpoyee by email")
    @Test
    public void givenEmail_whenFindByEmail_thenReturnEmployee(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Avishkar")
//                .lastName("Singh")
//                .email("avis@gmail.com")
//                .build();
        employeeRepository.save(employee);
        //when - action or behaviour that we are going to test
        Employee findEmployee = employeeRepository.findByEmail(employee.getEmail()).get();
        //then - verify the output
        assertThat(findEmployee).isNotNull();
    }

    //Junit test case for update employee operation
    @DisplayName("Junit test case for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Avishkar")
//                .lastName("Singh")
//                .email("avis@gmail.com")
//                .build();
        employeeRepository.save(employee);
        //when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("sadhu@gmail.com");
        savedEmployee.setFirstName("sadhu");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);
        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("sadhu@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("sadhu");
    }
    //junit test case for delete employee operation
    @DisplayName("junit test case for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenRemoveEmployee(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Avishkar")
//                .lastName("Singh")
//                .email("avis@gmail.com")
//                .build();
        employeeRepository.save(employee);
        //when - action or behaviour that we are going to test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
        //then - verify the output
        assertThat(employeeOptional).isEmpty();
    }

    //junit test case for custom query using jpql with index parameters findByJPQL()
    @DisplayName("junit test case for custom query using jpql with index parameters findByJPQL()")
    @Test
    public void givenFirstNameAndlastnamey_whenfindByJPQL_thenReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Avishkar")
//                .lastName("Singh")
//                .email("avis@gmail.com")
//                .build();
        employeeRepository.save(employee);
        //when - action or behaviour that we are going to test
        Employee resultEmployee = employeeRepository.findByJPQL(employee.getFirstName(), employee.getLastName());
        //then - verify the output
        assertThat(resultEmployee).isNotNull();
    }
    //junit test case for custom query using jpql with NamedParameters  findByJPQLWithnamedParameter()
    @DisplayName("junit test case for custom query using jpql with index parameters findByJPQL()")
    @Test
    public void givenFirstNameAndlastnamey_whenfindByJPQLNamedparams_thenReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Avishkar")
//                .lastName("Singh")
//                .email("avis@gmail.com")
//                .build();
        employeeRepository.save(employee);
        //when - action or behaviour that we are going to test
        Employee resultEmployee = employeeRepository.findByJPQLNamedpaams(employee.getFirstName(), employee.getLastName());
        //then - verify the output
        assertThat(resultEmployee).isNotNull();
    }

    //junit test case for custom query using native SQL with index
    @DisplayName("junit test case for custom query using native SQL with index")
    @Test
    public void givenFirstNameAndlastnamey_whenfindByNativeSQL_thenReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Avishkar")
//                .lastName("Singh")
//                .email("avis@gmail.com")
//                .build();
        employeeRepository.save(employee);
        //when - action or behaviour that we are going to test
        Employee resultEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());
        //then - verify the output
        assertThat(resultEmployee).isNotNull();
    }
    //junit test case for custom query using native SQL with named params
    @DisplayName("junit test case for custom query using native SQL with named params")
    @Test
    public void givenFirstNameAndlastnamey_whenfindByNativeSQLNamedParams_thenReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Avishkar")
//                .lastName("Singh")
//                .email("avis@gmail.com")
//                .build();
        employeeRepository.save(employee);
        //when - action or behaviour that we are going to test
        Employee resultEmployee = employeeRepository.findByNativeSQLWithNamedParams(employee.getFirstName(), employee.getLastName());
        //then - verify the output
        assertThat(resultEmployee).isNotNull();
    }

}
