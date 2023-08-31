package net.junitpractice.springboottesting.repository;

import net.junitpractice.springboottesting.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    //junit test for save employee operation
    @DisplayName("junit test for save employee operation")
    @Test
    public void givenEmployeePbject_whenSave_thenReturnSavedEmployee(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Avishkar")
                .lastName("Singh")
                .email("avis@gmail.com")
                .build();

        //when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }


}
