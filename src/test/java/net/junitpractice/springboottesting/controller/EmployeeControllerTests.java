package net.junitpractice.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.junitpractice.springboottesting.model.Employee;
import net.junitpractice.springboottesting.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean       //mock bean repository tells spring to create a mock instance of EmployeeService to create a mock instance of EmployeeService and add it to the application context so that its injected into EmployeeController
    private EmployeeService employeeService;

    //next we will use object mapper jackson class to serialize and deserialize ava objects
    @Autowired
    private ObjectMapper objectMapper;

    //junit test case for create employee method
    @DisplayName("junit test case for create employee method")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnEmployee() throws Exception{
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Avishkar")
                .lastName("Singh")
                .email("avishkar@gmail.com")
                .build();
        //stubbing saveEmployee method
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when - action or behaviour that we are going to test
        //making rest api with mock object
        ResultActions response =  mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)        //content type is json
                .content(objectMapper.writeValueAsString(employee)));       //content is json in body , using object mapper to convert object into json
        //then - verify the output
        //we are going to check/verify if response of the rest api is created or not, and we will check json values with actual employee object values
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))    //$ is root object of json response
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }
    //junit test case for get all employees method
    @DisplayName("junit test case for get all employees method")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception{
        //given - precondition or setup
        //studbing getAllEmployee() method
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Avishkar").lastName("Singh").email("avishkar@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Rahul").lastName("Dev").email("Kumar@gmail.com").build());
        //below line means that if we make getAllEmployee() method cll then we will get listOfEmployees as response
        BDDMockito.given(employeeService.getAllEmployees()).willReturn(listOfEmployees);
        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(listOfEmployees.size())));
    }
    //Positive scenario - valid employee id
    //junit test case for get employee by id method
    @DisplayName("junit test case for get employee by id method,Positive scenario - valid employee id")
    @Test
    public void givenEmployeeId_whenGetEmployeebyId_thenReturnEmployeeObject() throws Exception{
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Avishkar")
                .lastName("Singh")
                .email("avishkar@gmail.com")
                .build();
        Optional<Employee> emp1 = Optional.of(employee);
        //studbing getEmployeeById() method
        BDDMockito.given(employeeService.getEmployeeById(employee.getId())).willReturn(emp1);
        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",employee.getId()));   //second argument is value for id in url
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())));
    }

    //Negative scenario - not a valid employee id
    //junit test case for get employee by id method
    @DisplayName("junit test case for get employee by id method,Negative scenario - not a valid employee id")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeebyId_thenReturnErrorCode() throws Exception{
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Avishkar")
                .lastName("Singh")
                .email("avishkar@gmail.com")
                .build();
        //studbing getEmployeeById() method
        BDDMockito.given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.empty());
        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",22l));   //second argument is value for id in url
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    //Positive scenario - valid employee id
    //junit test case for update employee method
    @DisplayName("junit test case for update employee method,Positive scenario - valid employee id")
    @Test
    public void givenEmployeeIdAndEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception{
        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Avishkar")
                .lastName("Singh")
                .email("avishkar@gmail.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Jadhav")
                .email("ram@gmail.com")
                .build();
        BDDMockito.given(employeeService.getEmployeeById(1L)).willReturn(Optional.of(savedEmployee));

        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(updatedEmployee.getFirstName())))    //$ is root object of json response
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(updatedEmployee.getEmail())));
    }

    //Negaive scenario - not a valid employee id
    //junit test case for update employee method
    @DisplayName("junit test case for update employee method,Negative scenario - not a valid employee id")
    @Test
    public void givenInvalidEmployeeId_whenUpdateEmployee_thenReturnNotfoundError() throws Exception{
        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Avishkar")
                .lastName("Singh")
                .email("avishkar@gmail.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Jadhav")
                .email("ram@gmail.com")
                .build();
        BDDMockito.given(employeeService.getEmployeeById(1L)).willReturn(Optional.empty());

        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }
    //junit test case for delete employee method
    @DisplayName("junit test case for delete employee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn() throws Exception{
        //given - precondition or setup
        //stub delete method calls which return void
        BDDMockito.willDoNothing().given(employeeService).deleteEmployeeById(1L);
        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}",1L));
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }


}















