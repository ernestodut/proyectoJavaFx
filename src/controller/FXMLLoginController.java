package controller;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import utils.ConnectionUtil;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;



public class FXMLLoginController implements Initializable{

    @FXML
    private TextField textEmail;

    @FXML
    private PasswordField textPassword;

    @FXML
    private Button loginButton;

    Stage dialogStage = new Stage();
    Scene scene;

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public FXMLLoginController() {
        connection = ConnectionUtil.connectdb();
    }



    public void loginAction(ActionEvent event) throws SQLException {
        String email = textEmail.getText().toString();
        String password = textPassword.getText().toString();

        String sql = "SELECT * FROM usuarios WHERE usuario = ? and password = ?";

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                infoBox("Por favor ingresa correctamente usuario y password", null, "Failed");
            }else{

                connection.close();
                infoBox("Login Exitoso",null,"Success" );
                Node node = (Node)event.getSource();
                dialogStage = (Stage) node.getScene().getWindow();
                dialogStage.close();
                MenuBar menuBar = new MenuBar();
                Menu menu = new Menu("Opciones");
                MenuItem addCarrier = new MenuItem("Agregar Carrier");
                MenuItem quit = new MenuItem("Quit");
                addCarrier  .setOnAction(e -> Platform.exit());
                quit.setOnAction(e -> Platform.exit());
                menu.getItems().add(addCarrier);
                menu.getItems().add(quit);
                menuBar.getMenus().add(menu);
                menuBar.setUseSystemMenuBar(true);

                scene = new Scene(FXMLLoader.load(getClass().getResource("../view/FXMLLista.fxml")));
                ((AnchorPane) scene.getRoot()).getChildren().addAll(menuBar);
                scene.getStylesheets().add("view/form.css");
                dialogStage.setScene(scene);
                dialogStage.show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }


    public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {


    }

}
