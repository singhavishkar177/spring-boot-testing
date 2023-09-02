package net.junitpractice.springboottesting.repository;

import net.junitpractice.springboottesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository  extends JpaRepository<Employee,Long> {
    Optional<Employee> findByEmail(String email);
    //custom query using jpql to test purpose, use @query to write java persistence query language with index params
    @Query("select e from Employee e where e.firstName =?1 and e.lastName =?2")
    Employee findByJPQL(String firstName,String lastName);

    //define cutom query JPQL with named parameters
    @Query("select e from Employee e where e.firstName =:firstName and e.lastName =:lastName")
    Employee findByJPQLNamedpaams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    //define custom query using Native SQL with index params
    @Query(value = "select * from employees e where e.first_name=?1 and e.last_name=?2",nativeQuery = true)
    Employee findByNativeSQL(String firstName,String lastName);

    //define custom query using Native SQL with named params
    @Query(value = "select * from employees e where e.first_name =:firstName and e.last_name =:lastName",nativeQuery = true)
    Employee findByNativeSQLWithNamedParams(@Param("firstName") String firstName,@Param("lastName") String lastName);
}
