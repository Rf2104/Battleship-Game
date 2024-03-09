package game.batalhanaval;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class BatalhaNavalController2 implements Initializable {

    private BatalhaNavalController controller1;
    @FXML
    public GridPane tabBarcosP2 = new GridPane();
    @FXML
    public GridPane tabInimigoP2 = new GridPane();
    @FXML
    public Label naviosRestantesP2;
    @FXML
    public Button newGameButton2;
    @FXML
    public Label faseJogoP2;
    @FXML
    public Label j2name;
    @FXML
    public Button goMenuButton2;

    AtomicInteger index2 = new AtomicInteger();
    AtomicInteger ataques2 = new AtomicInteger();

    public int barcosP1 = 5;

    public void setController1(BatalhaNavalController controller1) {
        this.controller1 = controller1;
    }

    public GridPane getTabBarcosP2() {
        return tabBarcosP2;
    }

    public GridPane getTabInimigoP2() {
        return tabInimigoP2;
    }

    public String getCor(int tamBarco) {
        switch (tamBarco) {
            case 5:
                return "-fx-background-color: #FFD93D;";
            case 4:
                return "-fx-background-color: #0096FF;";
            case 3:
                return "-fx-background-color: #4C9E35;";
            case 2:
                return "-fx-background-color: #FF6000;";
            default:
                return "";
        }
    }

    public boolean colocarBarcoP2(Barco barco, int x, int y){
        if (podeColocarP2(barco, x, y)) {
            for (int i = 0; i < barco.tamBarco; i++) {
                Node node;
                if (barco.vertical) {
                    node = tabBarcosP2.getChildren().get((y + i) * 10 + x);
                } else {
                    node = tabBarcosP2.getChildren().get(y * 10 + x + i);
                }
                node.setStyle(getCor(barco.tamBarco));
                node.setOpacity(1);
                node.setUserData(barco);
            }
            return true;
        }
        return false;
    }

    public boolean podeColocarP2(Barco barco, int x, int y){
        // Verifica se o barco não ultrapassa as bordas do tabuleiro
        if((barco.vertical && y+barco.tamBarco > 10) || (!barco.vertical && x+barco.tamBarco > 10)){
            return false;
        }
        // Verifica se não há outro barco na posição
        for(int i=0; i<barco.tamBarco; i++){
            Node node;
            if(barco.vertical){
                node = tabBarcosP2.getChildren().get((y + i) * 10 + x);
            } else {
                node = tabBarcosP2.getChildren().get(y * 10 + x + i);
            }
            if (node != null && GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null){
                if(!node.getStyle().equals("-fx-background-color: lightblue;")){
                    return false; // Já existe um barco nesta posição
                }
            }
        }
        return true;
    }

    public boolean atacarP2(int x, int y) { //Ataque do player 2
        if(barcosP1 == 0){
            return false;
        }
        Barco barco;
        Node nodeP2 = tabInimigoP2.getChildren().get(y * 10 + x);
        Node nodeP1 = controller1.getTabBarcosP1().getChildren().get(y * 10 + x);
        if (nodeP2 != null && GridPane.getRowIndex(nodeP2) != null && GridPane.getColumnIndex(nodeP2) != null) {
            if (nodeP2.getStyle().equals("-fx-background-color: #ED2B2A;")) {
                return false;
            }
            if (nodeP2.getStyle().equals("-fx-background-color: lightgrey;")){
                return false;
            }
            if (nodeP2.getStyle().equals("-fx-background-color: grey;")){//ataque não contabilizado, atacou onde ja tinha falhado
                return false;
            }
            if(nodeP1.getStyle().equals("-fx-background-color: lightblue;")){
                barco = null;
                nodeP1.setStyle("-fx-background-color: lightgrey;");
                nodeP1.setOpacity(1);
                nodeP2.setStyle("-fx-background-color: lightgrey;");
                nodeP2.setOpacity(1);
            }else{
                barco = (Barco) nodeP1.getUserData();
                nodeP1.setStyle("-fx-background-color: #ED2B2A;");
                nodeP1.setOpacity(1);
                nodeP2.setStyle("-fx-background-color: #ED2B2A;");
                nodeP2.setOpacity(1);
            }
            if (barco != null) {
                // Diminuir a vida do barco
                barco.hit();
                // Verificar se o barco foi destruído
                if (!barco.isAlive()) {
                    barcosP1--;
                    naviosRestantesP2.setText("Navios por destruir: " + barcosP1);
                    // Alterar a cor dos Nodes do barco destruído para cinza
                    List<Node> nodesDoBarco = controller1.tabBarcosP1.getChildren().filtered(node -> node.getUserData() == barco);
                    for (Node node : nodesDoBarco) {
                        int cy = GridPane.getRowIndex(node);
                        int cx = GridPane.getColumnIndex(node);
                        nodeP2 = tabInimigoP2.getChildren().get(cy * 10 + cx);
                        nodeP2.setStyle("-fx-background-color: grey;");
                        nodeP2.setOpacity(1);
                        node.setStyle("-fx-background-color: grey;");
                        node.setOpacity(1);
                    }
                    if(barcosP1 == 0){
                        faseJogoP2.setText(j2name.getText() + " VENCEU!");
                        controller1.faseJogoP1.setText(j2name.getText() + " VENCEU!");
                        return false;
                    }

                }
            }
            return true;
        }
        return false;
    }

    public void initialize(URL url, ResourceBundle rb){

        newGameButton2.setOnAction(event -> controller1.reiniciarJogo());

        int[] tamP2 = {5, 4, 3, 3, 2};

        goMenuButton2.setOnAction(event -> {
            try {
                MenuController.fechar();

                Image icon = new Image(getClass().getResourceAsStream("/game/batalhanaval/icon.png"));

                Stage stage = new Stage();

                FXMLLoader fxmlLoader3 = new FXMLLoader(BatalhaNavalAPP.class.getResource("Menu.fxml"));
                MenuController controller3 = new MenuController();
                fxmlLoader3.setController(controller3);
                Scene scene3 = null;

                scene3 = new Scene(fxmlLoader3.load(), 600, 400);

                stage.setTitle("Menu");
                stage.setScene(scene3);
                stage.setResizable(false); // Disable maximize button
                stage.getIcons().add(icon);
                stage.show();
            }catch (IOException e) {
                e.printStackTrace();
            }
        });


        //Atacar
        tabInimigoP2.setOnMouseClicked(event -> {
            if(index2.get() > 4 && controller1.index1.get() > 4) {
                if (controller1.getQuem() == 1) {
                    if (ataques2.get() < 3) {
                        Node clickedNode = event.getPickResult().getIntersectedNode();
                        if (clickedNode != tabInimigoP2 && GridPane.getRowIndex(clickedNode) != null && GridPane.getColumnIndex(clickedNode) != null) {
                            // obter coordenadas da célula clicada
                            int x = GridPane.getColumnIndex(clickedNode);
                            int y = GridPane.getRowIndex(clickedNode);
                            if (atacarP2(x, y) == true) {
                                ataques2.getAndIncrement();
                                if(ataques2.get() == 3) {
                                    controller1.setQuem(0);
                                    ataques2.set(0);
                                    faseJogoP2.setText("Fase do Jogo: Ataque de " + controller1.j1name.getText());
                                    controller1.faseJogoP1.setText("Fase do Jogo: "+controller1.j1name.getText()+" Ataca");
                                }
                            }
                        }
                    }
                }
            }
        });

        //Posicionar
        tabBarcosP2.setOnMouseClicked(event -> {
            if(index2.get() <= 4) {
                Node clickedNode = event.getPickResult().getIntersectedNode();
                if (clickedNode != tabBarcosP2 && GridPane.getRowIndex(clickedNode) != null && GridPane.getColumnIndex(clickedNode) != null) {
                    // obter coordenadas da célula clicada
                    int x = GridPane.getColumnIndex(clickedNode);
                    int y = GridPane.getRowIndex(clickedNode);
                    // criar e adicionar barcos
                    Barco barco;
                    if (event.getButton() == MouseButton.PRIMARY) {
                        barco = new Barco(tamP2[index2.get()], false); // horizontal
                    } else {
                        barco = new Barco(tamP2[index2.get()], true); // vertical
                    }
                    if (colocarBarcoP2(barco, x, y)) {
                        index2.getAndIncrement();
                        if(controller1.index1.get() > 4 && index2.get() > 4) {
                            faseJogoP2.setText("Fase do Jogo: "+controller1.j1name.getText()+" Ataca");
                            controller1.faseJogoP1.setText("Fase do Jogo: Ataque de " + controller1.j1name.getText());
                        }
                    }
                }
            }
        });
    }

}
