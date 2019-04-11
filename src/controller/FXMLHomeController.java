    package controller;

    import java.io.IOException;
    import java.net.URL;
    import java.sql.*;
    import java.sql.Connection;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ResourceBundle;

    import javafx.application.Platform;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.scene.Node;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.layout.AnchorPane;
    import javafx.stage.Stage;
    import model.Servicio;
    import model.ServicioDAO;
    import utils.ConnectionUtil;
    import utils.Validaciones;

    import static controller.FXMLLoginController.infoBox;

    public class FXMLHomeController implements Initializable{

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Stage dialogStage = new Stage();
        Scene scene;


        private String nombre;


        public  void setServicio(Servicio svc) throws SQLException {

            this.textId.setText(String.valueOf(svc.getId()));
            //System.out.println(svc.getId());
            this.textId.setVisible(false);
            this.textNombre.setText(svc.getNombre());
            this.textDuracion.setText(svc.getDuracion());
            this.textSegmento.setText(svc.getSegmento());
            this.textShortcode.setText(svc.getShortcode());
            String strPrecio = String.valueOf(svc.getPrecio());
            this.textPrecio.setText(strPrecio);
            //System.out.println(svc.getTipo_cobro_id());
            String tipoCobro = ServicioDAO.getTipoCobro(svc.getTipo_cobro_id());
            this.comboTipoCobro.setValue(tipoCobro);
            //System.out.println(svc.getCarrier_id());
            String carrier = ServicioDAO.getCarrier(svc.getCarrier_id());
            this.comboCarrier.setValue(carrier);
        }



        public FXMLHomeController() {
            conn = ConnectionUtil.connectdb();
        }

        @FXML
        void actionListenerFxmlList(ActionEvent event) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/FXMLLista.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();

                FXMLListaController fxmlListaController = fxmlLoader.getController();
                fxmlListaController.setFXMLHomeController(this);

                textNombre.setText(fxmlListaController.getNombreServicio());
            } catch(Exception e) {
                e.printStackTrace();
            }
        }



        @FXML
        private TextField textId;
        @FXML
        private TextField textNombre;
        @FXML
        private TextField textSegmento;
        @FXML
        private TextField textPrecio;
        @FXML
        private TextField textDuracion;
        @FXML
        private TextField textShortcode;

        @FXML
        private ComboBox<String> comboCarrier;
        private ObservableList<String> dbCarrier = FXCollections.observableArrayList();
        @FXML
        private ComboBox<String> comboTipoCobro;
        private ObservableList<String> dbTipoCobro = FXCollections.observableArrayList();


        @Override
        public void initialize(URL url, ResourceBundle rb) {



            textNombre.setText(nombre);

            try {
                dbCarrier = ServicioDAO.buscarCarriers();
                comboCarrier.setItems(dbCarrier);
                dbTipoCobro = ServicioDAO.buscarTipoCobros();
                comboTipoCobro.setItems(dbTipoCobro);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }

        public void showLista(ActionEvent event) throws SQLException {
            Node node = (Node)event.getSource();
            dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
            try {
                scene = new Scene(FXMLLoader.load(getClass().getResource("../view/FXMLLista.fxml")));
                MenuBar menuBar = new MenuBar();
                Menu menu = new Menu("Opciones");
                MenuItem quit = new MenuItem("Salir");
                quit.setOnAction(e -> Platform.exit());
                menu.getItems().add(quit);
                menuBar.getMenus().add(menu);
                menuBar.setUseSystemMenuBar(true);
                ((AnchorPane) scene.getRoot()).getChildren().addAll(menuBar);
                scene.getStylesheets().add("view/form.css");
                dialogStage.setScene(scene);
                dialogStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void guardadSvc(ActionEvent event) throws SQLException {

            /****
             * Validaciones
             */
            if (!validacionesFormularioString(textNombre)){return;}
            if (!validacionesFormularioString(textSegmento)){return;}
            if (!validacionesFormularioNumber(textShortcode)){return;}
            if (!validacionesFormularioNumber(textPrecio)){return;}
            if (!validacionesFormularioNumber(textDuracion)){return;}

            if(comboCarrier.getValue()==null){
                infoBox("Debe estar seleccionado un Carrier", null, "Failed");
                comboCarrier.requestFocus();
                return;
            }

            if(comboTipoCobro.getValue()==null){
                infoBox("Debe estar seleccionado un tipo de cobro", null, "Failed");
                comboCarrier.requestFocus();
                return;
            }

            if(textId.getText().equalsIgnoreCase("")){
                Servicio svc = new Servicio();
                svc.setNombre(textNombre.getText());
                svc.setDuracion(textDuracion.getText());
                svc.setPrecio(Double.parseDouble(textPrecio.getText()));
                svc.setShortcode(textShortcode.getText());
                svc.setSegmento(textSegmento.getText());
                int carrier_id = ServicioDAO.getCarrierId(comboCarrier.getValue());
                svc.setCarrier_id(carrier_id);
                int tipoCobroId = ServicioDAO.getTipoCobroId(comboTipoCobro.getId());
                svc.setTipo_cobro_id(tipoCobroId);
                ServicioDAO.insertarServicio(svc);
                infoBox("Alta exitosa",null,"Success" );
                Node node = (Node)event.getSource();
                dialogStage = (Stage) node.getScene().getWindow();
                dialogStage.close();
                try {
                    scene = new Scene(FXMLLoader.load(getClass().getResource("../view/FXMLLista.fxml")));
                    scene.getStylesheets().add("view/form.css");
                    dialogStage.setScene(scene);
                    dialogStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Servicio svc = new Servicio();
                svc.setId(Integer.parseInt(textId.getText()));
                svc.setNombre(textNombre.getText());
                svc.setDuracion(textDuracion.getText());
                svc.setPrecio(Double.parseDouble(textPrecio.getText()));
                svc.setShortcode(textShortcode.getText());
                svc.setSegmento(textSegmento.getText());
                int carrier_id = ServicioDAO.getCarrierId(comboCarrier.getValue());
                svc.setCarrier_id(carrier_id);
                int tipoCobroId = ServicioDAO.getTipoCobroId(comboTipoCobro.getValue());
                svc.setTipo_cobro_id(tipoCobroId);
                ServicioDAO.actualizarServicio(svc);
                infoBox("Modificación exitosa",null,"Success" );
                Node node = (Node)event.getSource();
                dialogStage = (Stage) node.getScene().getWindow();
                dialogStage.close();
                try {
                    scene = new Scene(FXMLLoader.load(getClass().getResource("../view/FXMLLista.fxml")));
                    scene.getStylesheets().add("view/form.css");
                    dialogStage.setScene(scene);
                    dialogStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }




        }


        public static  boolean validacionesFormularioString(TextField textField){
            if (!Validaciones.validarLlenado(textField))
            {
                infoBox("El campo nombre debe ser llenado", null, "Failed");
                textField.requestFocus();
                return false;
            }

            if( !Validaciones.isValidName( textField.getText().toString() ))
            {
                infoBox("El campo nombre debe tener puras letras", null, "Failed");
                textField.requestFocus();
                return false;
            }
            return true;
        }

        public static  boolean validacionesFormularioNumber(TextField textField){
            if (!Validaciones.validarLlenado(textField))
            {
                infoBox("El campo  debe ser llenado", null, "Failed");
                textField.requestFocus();
                return false;
            }

            if( !Validaciones.isValidNumber( textField.getText().toString() ))
            {
                infoBox("El campo  debe tener puras números", null, "Failed");
                textField.requestFocus();
                return false;
            }
            return true;
        }
    }
