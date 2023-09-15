package net.junitpractice.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.junitpractice.springboottesting.model.Employee;
import net.junitpractice.springboottesting.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
//we don't have to rerite test cases we just have to integrate testcontainers here
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)     //random port because it will start embedded server on random port
@AutoConfigureMockMvc           ///because we need to mock rest api so auto configure them
public class EmployeeControllerIT extends AbstractContainerBaseTest {
    //mysql container object

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ObjectMapper objectMapper;      //object mapper library is for serialization and deserialization

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();     //in order to keep clean setup for each junit test case we will delete every record from our table
    }

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
        //no mocking is involved
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
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Avishkar").lastName("Singh").email("avishkar@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Rahul").lastName("Dev").email("Kumar@gmail.com").build());
        employeeRepository.saveAll(listOfEmployees);
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
        employeeRepository.save(employee);
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
        employeeRepository.save(employee);
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
        employeeRepository.save(savedEmployee);
        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Jadhav")
                .email("ram@gmail.com")
                .build();
        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}",savedEmployee.getId())
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
        Employee savedEmployee = Employee.builder().firstName("Avishkar")
                .lastName("Singh")
                .email("avishkar@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);
        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Jadhav")
                .email("ram@gmail.com")
                .build();

        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}",2L)
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
        Employee savedEmployee = Employee.builder()
                .firstName("Avishkar")
                .lastName("Singh")
                .email("avishkar@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);
        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}",savedEmployee.getId()));
        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }





}














