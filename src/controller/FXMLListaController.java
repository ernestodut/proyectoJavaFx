package controller;

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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperCompileManager;
import utils.ConnectionUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import static controller.FXMLLoginController.infoBox;

public class FXMLListaController  implements Initializable {

    public String getNombreServicio() {
        return nombreServicio;
    }

    public static String valorCombo = "TODOS";

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    private String nombreServicio;

    @FXML
    private TableView tablaServicios;
    @FXML
    private TableColumn<Servicio, Integer>  svcIdColumn;
    @FXML
    private TableColumn<Servicio, String>  svcNombreColumn;
    @FXML
    private TableColumn<Servicio, String> svcCarrierColumn;
    @FXML
    private TableColumn<Servicio, String> svcShortcode;
    @FXML
    private TableColumn<Servicio, Double> svcPrecio;

    Stage dialogStage = new Stage();
    Scene scene;

    @FXML
    private ComboBox<String> comboCarrier;
    private ObservableList<String> dbCarrier = FXCollections.observableArrayList();

    private Stage stage;



    private FXMLHomeController fxmlHomeController ;

    public void setFXMLHomeController(FXMLHomeController fxmlHomeController) {
        this.fxmlHomeController = fxmlHomeController ;
    }


    /**
     * Boton NUEVO
     * @param event
     */
    public void formNuevo(ActionEvent event){
        Node node = (Node)event.getSource();
        dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();
        try {
            MenuBar menuBar = new MenuBar();
            Menu menu = new Menu("Opciones");
            MenuItem quit = new MenuItem("Salir");
            quit.setOnAction(e -> Platform.exit());
            menu.getItems().add(quit);
            menuBar.getMenus().add(menu);
            menuBar.setUseSystemMenuBar(true);
            scene = new Scene(FXMLLoader.load(getClass().getResource("../view/FXMLHome.fxml")));
            dialogStage.setScene(scene);
            ((AnchorPane) scene.getRoot()).getChildren().addAll(menuBar);
            scene.getStylesheets().add("view/form.css");
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Boton BAJA
     * @param event
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void bajaSvc(ActionEvent event) throws SQLException, ClassNotFoundException {
        try{
            TablePosition pos = (TablePosition) tablaServicios.getSelectionModel().getSelectedCells().get(0);

            int row = pos.getRow();
            Servicio servicio = (Servicio) tablaServicios.getItems().get(row);
            TableColumn col = pos.getTableColumn();
            System.out.println(svcIdColumn.getCellObservableValue(servicio).getValue());
            int idSvc = svcIdColumn.getCellObservableValue(servicio).getValue();
            ServicioDAO.eliminarServicio(servicio);

            ObservableList<Servicio> svcDatos = ServicioDAO.buscarServicios();
            svcIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
            svcNombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
            svcCarrierColumn.setCellValueFactory(cellData -> cellData.getValue().carrierNameProperty());
            svcShortcode.setCellValueFactory(cellData -> cellData.getValue().shortcodeProperty());
            svcPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());
            tablaServicios.setItems(svcDatos);
            tablaServicios.refresh();
            infoBox("Servicio eliminado", null, "Success");
        }catch (IndexOutOfBoundsException e){
            infoBox("Selecciona un registro para eliminar", null, "Failed");
        }
    }


    /**
     * Boton Modificar
     * @param event
     * @throws SQLException
     */
    public void formModificar(ActionEvent event) throws SQLException {

        try {
            TablePosition pos = (TablePosition) tablaServicios.getSelectionModel().getSelectedCells().get(0);
            System.out.println(pos);
            int row = pos.getRow();
            Servicio servicio = (Servicio) tablaServicios.getItems().get(row);
            TableColumn col = pos.getTableColumn();
            //System.out.println(svcIdColumn.getCellObservableValue(servicio).getValue());
            int idSvc = svcIdColumn.getCellObservableValue(servicio).getValue();
            Servicio servicioSeleccionado = ServicioDAO.seleccionarServicio(idSvc);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/FXMLHome.fxml"));


            loader.load();

            FXMLHomeController home = loader.getController();
            home.setServicio(servicioSeleccionado);
            Parent p = loader.getRoot();
            Node node = (Node)event.getSource();
            dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
            MenuBar menuBar = new MenuBar();
            Menu menu = new Menu("Opciones");
            MenuItem quit = new MenuItem("Salir");
            quit.setOnAction(e -> Platform.exit());
            menu.getItems().add(quit);
            menuBar.getMenus().add(menu);
            menuBar.setUseSystemMenuBar(true);
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            ((AnchorPane) scene.getRoot()).getChildren().addAll(menuBar);
            scene.getStylesheets().add("view/form.css");
            stage.setScene(scene);
            stage.showAndWait();
            tablaServicios.refresh();

        } catch (IndexOutOfBoundsException e){
            infoBox("Selecciona un registro para modificar", null, "Failed");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            ObservableList<String> dbCarrier = ServicioDAO.buscarCarriers();


            ObservableList<Servicio> svcDatos = ServicioDAO.buscarServicios();
            svcIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
            svcNombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
            svcCarrierColumn.setCellValueFactory(cellData -> cellData.getValue().carrierNameProperty());
            svcShortcode.setCellValueFactory(cellData -> cellData.getValue().shortcodeProperty());
            svcPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());

            comboCarrier.setItems(dbCarrier);

            tablaServicios.setItems(svcDatos);


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**Boton Buscar**/
    public void search(ActionEvent event) throws SQLException, ClassNotFoundException {

        ObservableList<Servicio> listaServicios = FXCollections.observableArrayList();
        valorCombo = comboCarrier.getValue();
        if(valorCombo.equalsIgnoreCase("TODOS")){
            listaServicios = ServicioDAO.buscarServicios();
        }else{
            listaServicios = ServicioDAO.buscarServiciosPorCarrier(comboCarrier.getValue());
        }

        tablaServicios.setItems(listaServicios);
        tablaServicios.refresh();

    }

    /***
     * Reporte de todos los servicios
     */
    public void reporte(ActionEvent event) throws SQLException {
        //System.out.println("Valor de Combo " + valorCombo);
        HashMap hm = null;
        String reportSrcFile = "";
        Connection connection = ConnectionUtil.connectdb();
        if(valorCombo.equalsIgnoreCase("TODOS")) {
            reportSrcFile = "/home/ernesto/IdeaProjects/servicios/src/reports/report1.jrxml";
        }else{
            reportSrcFile = "/home/ernesto/IdeaProjects/servicios/src/reports/report2.jrxml";
            int carrierId = ServicioDAO.getCarrierId(valorCombo);

            hm = new HashMap();
            hm.put("parameter1", carrierId);
            hm.put("parameter2", valorCombo.toUpperCase());

        }
        try {

            JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, hm, connection);
            JasperExportManager.exportReportToPdfFile(print, "/home/ernesto/servicios.pdf");
            JasperViewer.viewReport(print, false);

        } catch (JRException e) {
            e.printStackTrace();
        }finally {
            connection.close();
        }


    }


}
