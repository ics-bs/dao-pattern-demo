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

    // Retrieve all employees
    public List<Employee> findAll() {
        String query = "SELECT EmpNo, EmpName, EmpSalary FROM Employee";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

       while (resultSet.next()) {
           employees.add(mapToEmployee(resultSet));
       }

        } catch (SQLException e) {
            throw new DaoException("Error fetching all employees.", e);
        }

        return employees;
    }

    // Save a new employee
    public void save(Employee employee) {
        String query = "INSERT INTO Employee (EmpNo, EmpName, EmpSalary) VALUES (?, ?, ?)";

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, employee.getEmpNo());
            statement.setString(2, employee.getEmpName());
            statement.setDouble(3, employee.getEmpSalary());
            statement.executeUpdate();
        } catch (SQLException e) {
            // SQL Server-specific unique constraint violation error code
            if (e.getErrorCode() == 2627) { 
                throw new DaoException("An employee with this Employee No already exists.", e);
            } else {
                throw new DaoException("Error saving employee: " + employee.getEmpNo(), e);
            }
        }
    }

    // Update an employee's details
    public void update(Employee employee) {
        String query = "UPDATE Employee SET EmpNo = ?, EmpName = ?, EmpSalary = ? WHERE EmpNo = ?";

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, employee.getEmpNo());
            statement.setString(2, employee.getEmpName());
            statement.setDouble(3, employee.getEmpSalary());
            statement.setString(4, employee.getEmpNo());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error updating employee: " + employee.getEmpNo(), e);
        }
    }

    // Delete an employee by EmpNo
    public void deleteByEmpNo(String empNo) {
        String query = "DELETE FROM Employee WHERE EmpNo = ?";

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, empNo);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error deleting employee with Employee No: " + empNo, e);
        }
    }

    // Helper method to map the result set to an Employee object
    private Employee mapToEmployee(ResultSet resultSet) throws SQLException {
        return new Employee(
                resultSet.getString("EmpNo"),
                resultSet.getString("EmpName"),
                resultSet.getDouble("EmpSalary")
        );
    }
}