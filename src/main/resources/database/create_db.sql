CREATE TABLE Department (
    DepartmentID    INTEGER IDENTITY(1,1), -- Surrogate key
    DeptName        VARCHAR(100) NOT NULL,
    DeptBudget      DECIMAL(19,2),

    CONSTRAINT PK_Department_DepartmentID PRIMARY KEY (DepartmentID),
    CONSTRAINT UQ_Department_DeptName UNIQUE (DeptName)
);

CREATE TABLE Employee (
    EmployeeID  INTEGER IDENTITY(1,1), -- Surrogate key
    EmpNo       VARCHAR(10) NOT NULL,
    EmpName     VARCHAR(50),
    EmpSalary   DECIMAL(19,2),

    CONSTRAINT PK_Employee_ID PRIMARY KEY (EmployeeID),
    CONSTRAINT UQ_Employee_EmpNo UNIQUE (EmpNo),
);

CREATE TABLE Work (
    EmployeeID      INTEGER,
    DepartmentID    INTEGER,
    StartDate       DATE,
    CONSTRAINT PK_Work_EmployeeID_DepartmentID PRIMARY KEY (EmployeeID, DepartmentID),
    CONSTRAINT FK_Work_Employee_EmployeeID FOREIGN KEY (EmployeeID) 
        REFERENCES Employee(EmployeeID),
    CONSTRAINT FK_Work_Department_DepartmentID FOREIGN KEY (DepartmentID) 
        REFERENCES Department(DepartmentID)
);

-- Insert departments
INSERT INTO Department (DeptName, DeptBudget)
VALUES 
    ('HR', 150000.00), 
    ('IT', 250000.00), 
    ('Sales', 200000.00);

-- Insert employees
INSERT INTO Employee (EmpNo, EmpName, EmpSalary)
VALUES 
    ('E1', 'Bob', '55000'),
    ('E2', 'Dan', '60000'),
    ('E3', 'Amy', '62000'),
    ('E4', 'Ham', '70000'),
    ('E5', 'Jim', '48000'),
    ('E6', 'Sue', '52000');

-- Insert employees into departments in the Work table using subqueries to retrieve IDs

-- Insert employee-department relationships (Work table)
INSERT INTO Work (EmployeeID, DepartmentID, StartDate)
VALUES
    (
        (SELECT EmployeeID FROM Employee WHERE EmpNo = 'E1'),
        (SELECT DepartmentID FROM Department WHERE DeptName = 'HR'),
        '2023-05-01'
    ),
    (
        (SELECT EmployeeID FROM Employee WHERE EmpNo = 'E1'),
        (SELECT DepartmentID FROM Department WHERE DeptName = 'IT'),
        '2023-06-01'
    ), 
    (
        (SELECT EmployeeID FROM Employee WHERE EmpNo = 'E2'),
        (SELECT DepartmentID FROM Department WHERE DeptName = 'HR'),
        '2022-08-15'
    ),
    (
        (SELECT EmployeeID FROM Employee WHERE EmpNo = 'E3'),
        (SELECT DepartmentID FROM Department WHERE DeptName = 'IT'),
        '2021-06-10'
    ),
    (
        (SELECT EmployeeID FROM Employee WHERE EmpNo = 'E4'),
        (SELECT DepartmentID FROM Department WHERE DeptName = 'IT'),
        '2023-01-20'
    ),
    (
        (SELECT EmployeeID FROM Employee WHERE EmpNo = 'E4'),
        (SELECT DepartmentID FROM Department WHERE DeptName = 'Sales'),
        '2023-04-25'
    ),
    (
        (SELECT EmployeeID FROM Employee WHERE EmpNo = 'E5'),
        (SELECT DepartmentID FROM Department WHERE DeptName = 'Sales'),
        '2022-11-05'
    ),
    (
        (SELECT EmployeeID FROM Employee WHERE EmpNo = 'E6'),
        (SELECT DepartmentID FROM Department WHERE DeptName = 'Sales'),
        '2023-03-12'
    );