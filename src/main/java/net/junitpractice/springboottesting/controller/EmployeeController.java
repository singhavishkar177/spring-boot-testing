package net.junitpractice.springboottesting.controller;

import net.junitpractice.springboottesting.model.Employee;
import net.junitpractice.springboottesting.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmplyee(@RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployee(){
        return employeeService.getAllEmployees();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long id){
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)        //this will map if employee object exists or not,if it exists then simply return ok()
                .orElseGet(() -> ResponseEntity.notFound().build());    //this means 404 error
    }
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long employeeId,@RequestBody Employee employee){
        return employeeService.getEmployeeById(employeeId)
                .map(savedEmployee -> {
                   savedEmployee.setFirstName(employee.getFirstName());
                   savedEmployee.setLastName(employee.getLastName());
                   savedEmployee.setEmail(employee.getEmail());
                  Employee updatedEmployee =  employeeService.updateEmployee(savedEmployee);
                  return new ResponseEntity<>(updatedEmployee,HttpStatus.OK);
                })          //if we don't find object of given object then build a object of not found
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmploye(@PathVariable("id") long employeeid){
        employeeService.deleteEmployeeById(employeeid);
        return new ResponseEntity<String>("Employee deleted successfully!.",HttpStatus.OK);
    }
}
