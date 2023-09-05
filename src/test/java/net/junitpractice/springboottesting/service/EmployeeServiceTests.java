package net.junitpractice.springboottesting.service;

import net.junitpractice.springboottesting.exception.ResourceNotFoundException;
import net.junitpractice.springboottesting.model.Employee;
import net.junitpractice.springboottesting.repository.EmployeeRepository;
import net.junitpractice.springboottesting.service.impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

//basically we are going to extend our behaviour for our mockito extension
@ExtendWith(MockitoExtension.class)             //we need to tell test cases that we are using mock annotations otherwise, test will give error
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks                                                    //injecting one mock dependency in this
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .id(1L)
                .firstName("Rakesh")
                .lastName("Mall")
                .email("rakesh@gmail.com")
                .build();
    }

    //Junit test case for saveEmployeeMethod
    @DisplayName("Junit test case for saveEmployeeMethod")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployee(){
        //given - precondition or setup
        //in our EmployeeSericeImpl class saveEmployee method uses findByEmail() and save() methods so we have to provide stubbing for these 2 methods first

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());//given method takes methodCall as argument, here in willReturn() there is empty object because if we return an employee object then there already exists an object with taht email and it will throw ResourceNotfoundexception
        given(employeeRepository.save(employee)).willReturn(employee);   //here in willReturn() parameter is employee becoz save() method return object so.
        //when - action or behaviour that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit test case for saveEmployeeMethod failed one
    @DisplayName("Junit test case for saveEmployeeMethod failed one")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        //given - precondition or setup
        //in our EmployeeSericeImpl class saveEmployee method uses findByEmail() and save() methods so we have to provide stubbing for these 2 methods first

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));//given method takes methodCall as argument
//        given(employeeRepository.save(employee)).willReturn(employee);   //we can't do stubbing for this method now because we want the control to eit after getting exception from findByEmail method
        //when - action or behaviour that we are going to test
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            employeeService.saveEmployee(employee);
        });   //this is Assertion class from junit package, not from assertj
        //then - verify the output
      verify(employeeRepository,never()).save(any(Employee.class));//now after assertThrows gives exception the control should not go to next statement for save method
    }

    //Junit test case for getALlEmployees method
    @DisplayName("Junit test case for getALlEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEMployees_thenReturnEmployeeList(){
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Avishkar")
                .lastName("Singh")
                .email("avishkar@gmail.com")
                .build();
        given(employeeRepository.findAll())
                .willReturn(List.of(employee,employee1));//given method takes methodCall as argument
        //when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();
        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }
    //Junit test case for getALlEmployees method (negative scenario)
    @DisplayName("Junit test case for getALlEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeeList(){
        //given - precondition or setup
        given(employeeRepository.findAll())
                .willReturn(Collections.emptyList());//given method takes methodCall as argument
        //when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();
        //then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }
    //Junit test case for getEmployeeById method
    @DisplayName("Junit test case for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        //given - precondition or setup
        given(employeeRepository.findById(1L))
                .willReturn(Optional.of(employee));//given method takes methodCall as argument
        //when - action or behaviour that we are going to test
        Optional<Employee> employee1 = employeeService.getEmployeeById(employee.getId());
        //then - verify the output
        assertThat(employee1).isNotNull();
    }
    //Junit test case for updateEmployee method
    @DisplayName("Junit test case for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdateEmployee(){
        //given - precondition or setup
        given(employeeRepository.save(employee))
                .willReturn(employee);//given method takes methodCall as argument
        employee.setFirstName("Keshav");
        employee.setEmail("Dhami@outlook.com");
        //when - action or behaviour that we are going to test
        Employee updateEmployee = employeeService.updateEmployee(employee);
        //then - verify the output
        assertThat(updateEmployee.getEmail()).isEqualTo("Dhami@outlook.com");
        assertThat(updateEmployee.getFirstName()).isEqualTo("Keshav");
    }

    //Junit test case for deleteEmployeeById method
    @DisplayName("Junit test case for deleteEmployeeById method")
    @Test
    public void givenEmployeeId_whenDeleteEmployeeById_thenDoNothing(){
        //given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(1L);
        //when - action or behaviour that we are going to test
        employeeService.deleteEmployeeById(1L);
        //then - verify the output
        verify(employeeRepository,times(1)).deleteById(employee.getId());
    }

}
