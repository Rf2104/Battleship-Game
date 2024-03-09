package game.batalhanaval;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private Label titleLabel;
    @FXML
    private Button playButton;
    @FXML
    private TextField nome1;
    @FXML
    private TextField nome2;

    static Stage stage = new Stage();
    static Stage stage2 = new Stage();


    BatalhaNavalController controller1 = new BatalhaNavalController();

    BatalhaNavalController2 controller2 = new BatalhaNavalController2();

    @FXML
    private void handleJogarButtonAction(ActionEvent event) {
        try {
            Image icon = new Image(getClass().getResourceAsStream("/game/batalhanaval/icon.png"));
            FXMLLoader fxmlLoader = new FXMLLoader(BatalhaNavalAPP.class.getResource("BatalhaNaval.fxml"));
            fxmlLoader.setController(controller1);
            Scene scene = new Scene(fxmlLoader.load(), 700, 800);
            stage.setTitle("Player1");
            stage.setScene(scene);
            stage.setResizable(false); // Disable maximize button
            stage.getIcons().add(icon);
            stage.show();

            FXMLLoader fxmlLoader2 = new FXMLLoader(BatalhaNavalAPP.class.getResource("BatalhaNaval2.fxml"));
            fxmlLoader2.setController(controller2);
            Scene scene2 = new Scene(fxmlLoader2.load(), 700, 800);
            stage2.setTitle("Player2");
            stage2.setScene(scene2);
            stage2.setResizable(false); // Disable maximize button
            stage2.getIcons().add(icon);
            stage2.show();

            controller1.setController2(controller2);
            controller2.setController1(controller1);

            if(nome1.getText().equals("")){
                controller1.j1name.setText("Jogador 1");
            }else controller1.j1name.setText(nome1.getText());
            if(nome2.getText().equals("")){
                controller2.j2name.setText("Jogador 2");
            }else controller2.j2name.setText(nome2.getText());

            // Feche a janela do menu atual
            Stage currentStage = (Stage) playButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fechar(){
        stage.close();
        stage2.close();
    }
    public void initialize() {
        Font font3D = Font.loadFont(getClass().getResourceAsStream("3Dumb.ttf"), 65);
        titleLabel.setFont(font3D);

        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        stage2.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }
}