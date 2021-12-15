module com.unina.oobdiproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.unina.oobdiproject to javafx.fxml;
    exports com.unina.oobdiproject;
}