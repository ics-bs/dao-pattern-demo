package se.lu.ics.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.EmployeeDao;
import se.lu.ics.models.Employee;

import java.io.IOException;
import java.util.List;

public class EmployeeController {

    @FXML
    private TableView<Employee> tableViewEmployee;

    @FXML
    private TableColumn<Employee, String> tableColumnEmployeeNo; 

    @FXML
    private TableColumn<Employee, String> tableColumnEmployeeName;

    @FXML
    private TableColumn<Employee, Double> tableColumnEmployeeSalary;

    @FXML
    private TextField textFieldEmployeeNo; 

    @FXML
    private TextField textFieldEmployeeName;

    @FXML
    private TextField textFieldEmployeeSalary;

    @FXML
    private Button btnEmployeeAdd;

    @FXML
    private Label labelErrorMessage;

    private EmployeeDao employeeDao;

        public EmployeeController() {
        try {
            employeeDao = new EmployeeDao();
        } catch (IOException e) {
            displayErrorMessage("Error initializing database connection: " + e.getMessage());
        }
    }


    @FXML
    public void initialize() {
        // Set up table columns
        tableColumnEmployeeNo.setCellValueFactory(new PropertyValueFactory<>("empNo"));
        tableColumnEmployeeName.setCellValueFactory(new PropertyValueFactory<>("empName"));
        tableColumnEmployeeSalary.setCellValueFactory(new PropertyValueFactory<>("empSalary"));

        loadEmployees();
    }

    @FXML
    private void buttonEmployeeAdd_OnClick(MouseEvent event) {
        clearErrorMessage();

        try {
            String empNo = textFieldEmployeeNo.getText();
            String empName = textFieldEmployeeName.getText();
            double empSalary = Double.parseDouble(textFieldEmployeeSalary.getText());

            Employee newEmployee = new Employee(empNo, empName, empSalary);

            // Save the employee using the DAO
            employeeDao.save(newEmployee);

            // Refresh the TableView after saving
            loadEmployees();

            // Clear input fields after adding
            textFieldEmployeeNo.clear(); 
            textFieldEmployeeName.clear();
            textFieldEmployeeSalary.clear();
        } catch (DaoException e) {
            displayErrorMessage(e.getMessage());
        } catch (NumberFormatException e) {
            displayErrorMessage("Invalid salary. Please enter a valid number.");
        }
    }

    private void loadEmployees() {
        clearErrorMessage();
        try {
            List<Employee> employeeList = employeeDao.findAll();
            ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList(employeeList);
            tableViewEmployee.setItems(employeeObservableList);
        } catch (DaoException e) {
            displayErrorMessage("Error loading employees: " + e.getMessage());
        }
    }

    private void displayErrorMessage(String message) {
        labelErrorMessage.setText(message);
        labelErrorMessage.setStyle("-fx-text-fill: red;");
    }

    private void clearErrorMessage() {
        labelErrorMessage.setText("");
    }
}