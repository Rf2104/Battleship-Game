package game.batalhanaval;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class BatalhaNavalAPP extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Load the icon image
        Image icon = new Image(getClass().getResourceAsStream("/game/batalhanaval/icon.png"));


        FXMLLoader fxmlLoader3 = new FXMLLoader(BatalhaNavalAPP.class.getResource("Menu.fxml"));
        MenuController controller3 = new MenuController();
        fxmlLoader3.setController(controller3);
        Scene scene3 = new Scene(fxmlLoader3.load(), 600, 400);
        stage.setTitle("Menu");
        stage.setScene(scene3);
        stage.setResizable(false); // Disable maximize button
        stage.getIcons().add(icon);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}