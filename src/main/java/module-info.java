module dao.pattern.demo {
    exports se.lu.ics;

    opens se.lu.ics.controllers to javafx.fxml;
    opens se.lu.ics.models to javafx.base;

    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
}
