package se.lu.ics.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import se.lu.ics.models.Employee;

public class EmployeeDao {

    private ConnectionHandler connectionHandler;

    public EmployeeDao() throws IOException {
        this.connectionHandler = new ConnectionHandler();
    }

    // Method to retrieve all employees from the database (without EmployeeID)
    public List<Employee> findAll() throws SQLException {
        String query = "SELECT EmpNo, EmpName, EmpSalary FROM Employee";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                employees.add(mapToEmployee(resultSet));
            }
        }

        return employees;
    }

    // Method to get an employee by EmpNo
    public Employee findByEmpNo(String empNo) throws SQLException {
        String query = "SELECT EmpNo, EmpName, EmpSalary"
                + " FROM Employee WHERE EmpNo = ?";
        Employee employee = null;

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, empNo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                employee = mapToEmployee(resultSet);
            }
        }

        return employee;
    }

    // Method to add a new employee to the database
    public void save(Employee employee) throws SQLException {
        String query = "INSERT INTO Employee (EmpNo, EmpName, EmpSalary)"
                + " VALUES (?, ?, ?)";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, employee.getEmpNo());
            statement.setString(2, employee.getEmpName());
            statement.setDouble(3, employee.getEmpSalary());

            statement.executeUpdate();
        }
    }

    // Helper method to map the result set to an Employee object
    private Employee mapToEmployee(ResultSet resultSet) throws SQLException {
        return new Employee(
                resultSet.getString("EmpNo"),
                resultSet.getString("EmpName"),
                resultSet.getDouble("EmpSalary"));
    }
}