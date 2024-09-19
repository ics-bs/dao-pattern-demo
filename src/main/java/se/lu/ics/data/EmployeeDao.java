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

    /**
     * Retrieves all employees from the database.
     * This method executes a SQL SELECT statement to fetch employee details
     * (Employee No, Name, Salary) and returns them as a list of Employee objects.
     *
     * @return A list of Employee objects.
     * @throws DaoException If there is an error accessing the database.
     */
    public List<Employee> findAll() {
        String query = "SELECT EmpNo, EmpName, EmpSalary FROM Employee";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and map each row to an Employee object
            while (resultSet.next()) {
                employees.add(mapToEmployee(resultSet));
            }
        } catch (SQLException e) {
            // Throw a custom DaoException if there's an issue with database access
            throw new DaoException("Error fetching all employees.", e);
        }

        return employees;
    }

    /**
     * Saves a new employee to the database.
     * This method inserts a new employee record (Employee No, Name, Salary) into
     * the database.
     *
     * @param employee The Employee object containing the data to be saved.
     * @throws DaoException If there is an error saving the employee (e.g., if the
     *                      Employee No already exists).
     */
    public void save(Employee employee) {
        String query = "INSERT INTO Employee (EmpNo, EmpName, EmpSalary) VALUES (?, ?, ?)";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set employee data into the prepared statement
            statement.setString(1, employee.getEmployeeNumber());
            statement.setString(2, employee.getName());
            statement.setDouble(3, employee.getSalary());

            // Execute the insert operation
            statement.executeUpdate();
        } catch (SQLException e) {
            // Check for unique constraint violation (SQL Server error code 2627)
            if (e.getErrorCode() == 2627) {
                throw new DaoException("An employee with this Employee No already exists.", e);
            } else {
                throw new DaoException("Error saving employee: " + employee.getEmployeeNumber(), e);
            }
        }
    }

    /**
     * Updates an existing employee's details in the database.
     * This method updates the employee's information based on the provided Employee
     * object.
     *
     * @param employee The Employee object containing the updated data.
     * @throws DaoException If there is an error updating the employee's data.
     */
    public void update(Employee employee) {
        String query = "UPDATE Employee SET EmpNo = ?, EmpName = ?, EmpSalary = ? WHERE EmpNo = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set updated employee data into the prepared statement
            statement.setString(1, employee.getEmployeeNumber());
            statement.setString(2, employee.getName());
            statement.setDouble(3, employee.getSalary());
            statement.setString(4, employee.getEmployeeNumber());

            // Execute the update operation
            statement.executeUpdate();
        } catch (SQLException e) {
            // Throw a DaoException for any SQL errors
            throw new DaoException("Error updating employee: " + employee.getEmployeeNumber(), e);
        }
    }

    /**
     * Deletes an employee from the database by Employee No.
     * This method deletes the record of the employee matching the given Employee
     * No.
     *
     * @param empNo The Employee No of the employee to be deleted.
     * @throws DaoException If there is an error deleting the employee.
     */
    public void deleteByEmployeeNumber(String employeeNumber) {
        String query = "DELETE FROM Employee WHERE EmpNo = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set Employee No in the prepared statement
            statement.setString(1, employeeNumber);

            // Execute the delete operation
            statement.executeUpdate();
        } catch (SQLException e) {
            // Throw a DaoException for SQL errors during deletion
            throw new DaoException("Error deleting employee with Employee No: " + employeeNumber, e);
        }
    }

    /**
     * Maps a row in the ResultSet to an Employee object.
     * This method is a helper function used to convert the result of a SQL query
     * into an Employee object.
     *
     * @param resultSet The ResultSet containing the employee data.
     * @return An Employee object with the data from the ResultSet.
     * @throws SQLException If there is an error accessing the data in the
     *                      ResultSet.
     */
    private Employee mapToEmployee(ResultSet resultSet) throws SQLException {
        return new Employee(
                resultSet.getString("EmpNo"),
                resultSet.getString("EmpName"),
                resultSet.getDouble("EmpSalary"));
    }

}