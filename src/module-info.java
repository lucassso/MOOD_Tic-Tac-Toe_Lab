module MOOD.tictactoe {
    requires javafx.controls;

    // Only if FXML is needed
    requires javafx.fxml;
    opens org.headroyce.lross.tictactoe to javafx.fxml;

    exports org.headroyce.lross.tictactoe;
}