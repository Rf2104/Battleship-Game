module game.batalhanaval {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens game.batalhanaval to javafx.fxml;
    exports game.batalhanaval;
}