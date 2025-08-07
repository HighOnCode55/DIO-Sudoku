module br.com.sudoku {
    requires javafx.controls;
    requires javafx.fxml;

    opens br.com.sudoku.ui to javafx.fxml;
    exports br.com.sudoku.ui;
}
