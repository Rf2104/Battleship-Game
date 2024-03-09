package game.batalhanaval;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class BatalhaNavalController implements Initializable {

    private BatalhaNavalController2 controller2;
    @FXML
    public GridPane tabBarcosP1 = new GridPane();
    @FXML
    public GridPane tabInimigoP1 = new GridPane();
    @FXML
    public Label naviosRestantesP1;
    @FXML
    public Button newGameButton1;
    @FXML
    public Label faseJogoP1;
    @FXML
    public Label j1name;
    @FXML
    public Button goMenuButton1;

    AtomicInteger index1 = new AtomicInteger();
    AtomicInteger ataques1 = new AtomicInteger();
    public int quem = 0;

    public int barcosP2 = 5;

    public void setController2(BatalhaNavalController2 controller2) {
        this.controller2 = controller2;
    }

    public GridPane getTabBarcosP1() {
        return tabBarcosP1;
    }

    public GridPane getTabInimigoP1() {
        return tabInimigoP1;
    }

    public int getQuem(){
        return quem;
    }

    public void setQuem(int quem){
        this.quem = quem;
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

    public boolean colocarBarcoP1(Barco barco, int x, int y){
        if (podeColocarP1(barco, x, y)) {
            for (int i = 0; i < barco.tamBarco; i++) {
                Node node;
                if (barco.vertical) {
                    node = tabBarcosP1.getChildren().get((y + i) * 10 + x);
                } else {
                    node = tabBarcosP1.getChildren().get(y * 10 + x + i);
                }
                node.setStyle(getCor(barco.tamBarco));
                node.setOpacity(1);
                node.setUserData(barco);
            }
            return true;
        }
        return false;
    }

    public boolean podeColocarP1(Barco barco, int x, int y){
        // Verifica se o barco não ultrapassa as bordas do tabuleiro
        if((barco.vertical && y+barco.tamBarco > 10) || (!barco.vertical && x+barco.tamBarco > 10)){
            return false;
        }
        // Verifica se não há outro barco na posição
        for(int i=0; i<barco.tamBarco; i++){
            Node node;
            if(barco.vertical){
                node = tabBarcosP1.getChildren().get((y + i) * 10 + x);
            } else {
                node = tabBarcosP1.getChildren().get(y * 10 + x + i);
            }
            if (node != null && GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null){
                if(!node.getStyle().equals("-fx-background-color: lightblue;")){
                    return false; // Já existe um barco nesta posição
                }
            }
        }
        return true;
    }

    public boolean atacarP1(int x, int y) { //Ataque do player 1
        if(barcosP2 == 0){
            return false;
        }
        Barco barco;
        Node nodeP1 = tabInimigoP1.getChildren().get(y * 10 + x);
        Node nodeP2 = controller2.getTabBarcosP2().getChildren().get(y * 10 + x);
        if (nodeP1 != null && GridPane.getRowIndex(nodeP1) != null && GridPane.getColumnIndex(nodeP1) != null) {
            if (nodeP1.getStyle().equals("-fx-background-color: #ED2B2A;")) {//ataque não contabilizado, atacou onde ja tinha acertado
                return false;
            }
            if (nodeP1.getStyle().equals("-fx-background-color: lightgrey;")){//ataque não contabilizado, atacou onde ja tinha falhado
                return false;
            }
            if (nodeP1.getStyle().equals("-fx-background-color: grey;")){//ataque não contabilizado, atacou onde ja tinha falhado
                return false;
            }
            if(nodeP2.getStyle().equals("-fx-background-color: lightblue;")){
                barco = null;//falhou ataque
                nodeP2.setOpacity(1);
                nodeP2.setStyle("-fx-background-color: lightgrey;");
                nodeP1.setOpacity(1);
                nodeP1.setStyle("-fx-background-color: lightgrey;");
            }else{
                barco = (Barco) nodeP2.getUserData();
                nodeP2.setOpacity(1);
                nodeP2.setStyle("-fx-background-color: #ED2B2A;");
                nodeP1.setOpacity(1);
                nodeP1.setStyle("-fx-background-color: #ED2B2A;");
            }
            if (barco != null) {
                // Diminuir a vida do barco
                barco.hit();
                // Verificar se o barco foi destruído
                if (!barco.isAlive()) {
                    barcosP2--;
                    naviosRestantesP1.setText("Navios por destruir: " + barcosP2);
                    // Alterar a cor dos Nodes do barco destruído para cinza
                    List<Node> nodesDoBarco = controller2.tabBarcosP2.getChildren().filtered(node -> node.getUserData() == barco);
                    for (Node node : nodesDoBarco) {
                        int cy = GridPane.getRowIndex(node);
                        int cx = GridPane.getColumnIndex(node);
                        nodeP1 = tabInimigoP1.getChildren().get(cy * 10 + cx);
                        nodeP1.setStyle("-fx-background-color: grey;");
                        nodeP1.setOpacity(1);
                        node.setStyle("-fx-background-color: grey;");
                        node.setOpacity(1);
                    }
                    if(barcosP2 == 0){
                        faseJogoP1.setText(j1name.getText() + " VENCEU!");
                        controller2.faseJogoP2.setText(j1name.getText() + " VENCEU!");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    void reiniciarJogo() {
        // Restaurar configurações iniciais do jogo

        faseJogoP1.setText("Fase do Jogo: Posicionar");
        controller2.faseJogoP2.setText("Fase do Jogo: Posicionar");
        // Limpar tabuleiros
        tabBarcosP1.getChildren().clear();
        tabInimigoP1.getChildren().clear();
        controller2.getTabBarcosP2().getChildren().clear();
        controller2.getTabInimigoP2().getChildren().clear();

        // Reiniciar variáveis de controle do jogo
        index1.set(0);
        controller2.index2.set(0);
        ataques1.set(0);
        controller2.ataques2.set(0);
        quem = 0;
        barcosP2 = 5;
        controller2.barcosP1 = 5;

        // Reiniciar rótulo de navios restantes
        naviosRestantesP1.setText("Navios por destruir: " + barcosP2);
        controller2.naviosRestantesP2.setText("Navios por destruir: " + controller2.barcosP1);

        // Reiniciar tabuleiros com células vazias
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Pane cellP1 = new Pane();
                Pane cellP2 = new Pane();
                Pane cellP3 = new Pane();
                Pane cellP4 = new Pane();
                tabBarcosP1.add(cellP1, j, i);
                tabInimigoP1.add(cellP2, j, i);
                controller2.tabBarcosP2.add(cellP3, j, i);
                controller2.tabInimigoP2.add(cellP4, j, i);
                cellP1.setStyle("-fx-background-color: lightblue;");
                cellP1.setOpacity(0.3);
                cellP2.setStyle("-fx-background-color: lightblue;");
                cellP2.setOpacity(0.3);
                cellP3.setStyle("-fx-background-color: lightblue;");
                cellP3.setOpacity(0.3);
                cellP4.setStyle("-fx-background-color: lightblue;");
                cellP4.setOpacity(0.3);
            }
        }
    }

    public void initialize(URL url, ResourceBundle rb){

        newGameButton1.setOnAction(event -> reiniciarJogo());

        goMenuButton1.setOnAction(event -> {
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

        int[] tamP1 = {5, 4, 3, 3, 2};

        //Atacar
        tabInimigoP1.setOnMouseClicked(event -> {
            if(index1.get() > 4 && controller2.index2.get() > 4) {
                if (getQuem() == 0) {
                    if (ataques1.get() < 3) {
                        Node clickedNode = event.getPickResult().getIntersectedNode();
                        if (clickedNode != tabInimigoP1 && GridPane.getRowIndex(clickedNode) != null && GridPane.getColumnIndex(clickedNode) != null) {
                            // obter coordenadas da célula clicada
                            int x = GridPane.getColumnIndex(clickedNode);
                            int y = GridPane.getRowIndex(clickedNode);
                            if (atacarP1(x, y) == true) {
                                ataques1.getAndIncrement();
                                if(ataques1.get() == 3) {
                                    setQuem(1);
                                    ataques1.set(0);
                                    faseJogoP1.setText("Fase do Jogo: Ataque de " + controller2.j2name.getText());
                                    controller2.faseJogoP2.setText("Fase do Jogo: "+controller2.j2name.getText()+" Ataca");
                                }
                            }
                        }
                    }
                }
            }
        });

        //Posicionar
        tabBarcosP1.setOnMouseClicked(event -> {
            if(index1.get() <= 4) {
                Node clickedNode = event.getPickResult().getIntersectedNode();
                if (clickedNode != tabBarcosP1 && GridPane.getRowIndex(clickedNode) != null && GridPane.getColumnIndex(clickedNode) != null) {
                    // obter coordenadas da célula clicada
                    int x = GridPane.getColumnIndex(clickedNode);
                    int y = GridPane.getRowIndex(clickedNode);
                    // criar e adicionar barcos
                    Barco barco;
                    if (event.getButton() == MouseButton.PRIMARY) {
                        barco = new Barco(tamP1[index1.get()], false); // horizontal
                    } else {
                        barco = new Barco(tamP1[index1.get()], true); // vertical
                    }
                    if (colocarBarcoP1(barco, x, y)) {
                        index1.getAndIncrement();
                        if(index1.get() > 4 && controller2.index2.get() > 4) {
                            faseJogoP1.setText("Fase do Jogo: "+j1name.getText()+" Ataca");
                            controller2.faseJogoP2.setText("Fase do Jogo: Ataque de " + j1name.getText());
                        }
                    }
                }
            }
        });

    }

}