package se.lu.ics;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import se.lu.ics.data.DaoException;
import se.lu.ics.data.EmployeeDao;
import se.lu.ics.models.Employee;

public class Main {
    public static void main(String[] args) {

        // Test "Level 2" separation of concerns

        try {

            EmployeeDao employeeDao = new EmployeeDao();

            List<Employee> employees = employeeDao.findAll();

            for (Employee employee : employees) {
                System.out.println(employee.getEmpNo() + " " + employee.getEmpName() + " " + employee.getEmpSalary());
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DaoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}