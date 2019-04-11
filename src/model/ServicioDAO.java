package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicioDAO {

    private static Connection connection = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;



    //*******************************
    //SELECT * FROM carriers
    //*******************************

    public static ObservableList<String> buscarCarriers()throws SQLException, ClassNotFoundException {
        ObservableList<String> svcCarriers = FXCollections.observableArrayList();;
        /**Combo de Carriers*/
        String sqlCarrierName = " SELECT * FROM carriers ";
        try {
            connection = ConnectionUtil.connectdb();
            preparedStatement = connection.prepareStatement(sqlCarrierName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                svcCarriers.add(resultSet.getString("carrier"));
            }

        } catch (SQLException ex) {
            System.err.println("ERR" + ex);
        }finally {
            connection.close();
        }
        svcCarriers.add("TODOS");
        return svcCarriers;
    }

    //******************************
    //SELECT tipo_cobro
    //********************************
    public static ObservableList<String> buscarTipoCobros()throws SQLException, ClassNotFoundException {
        ObservableList<String> svcCarriers = FXCollections.observableArrayList();;
        /**Combo de Carriers*/
        String sqlTipoCobro = " SELECT * FROM tipo_cobro";
        try {
            connection = ConnectionUtil.connectdb();
            preparedStatement = connection.prepareStatement(sqlTipoCobro);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                svcCarriers.add(resultSet.getString("tipo"));

            }
        } catch (SQLException ex) {
            System.err.println("ERR" + ex);
        }finally {
            connection.close();
        }

        return svcCarriers;
    }

    //*******************************
    //SELECT * FROM servicios By Carrier
    //*******************************
    public static ObservableList<Servicio> buscarServiciosPorCarrier (String carrier) throws SQLException, ClassNotFoundException {
        /**Combo de Carriers*/
        String sqlCarrierId = " SELECT id FROM carriers WHERE carrier= '" + carrier + "'";
        ObservableList<Servicio> listaServicios = FXCollections.observableArrayList();
        try {
            connection = ConnectionUtil.connectdb();
            preparedStatement = connection.prepareStatement(sqlCarrierId);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int carrier_id = resultSet.getInt("id");
                String sqlServiciosPorCarrier = " SELECT * FROM servicios WHERE carrier_id='" + carrier_id + "'";
                preparedStatement = connection.prepareStatement(sqlServiciosPorCarrier);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Servicio svc = new Servicio();
                    svc.setCarrier_id(resultSet.getInt("carrier_id"));
                    svc.setCarrierName(carrier);
                    svc.setId(resultSet.getInt("id"));
                    svc.setTipo_cobro_id(resultSet.getInt("tipo_cobro_id"));
                    svc.setDuracion(resultSet.getString("duracion"));
                    svc.setNombre(resultSet.getString("nombre"));
                    svc.setSegmento(resultSet.getString("segmento"));
                    svc.setShortcode(resultSet.getString("shortcode"));
                    svc.setPrecio(resultSet.getDouble("precio"));
                    listaServicios.add(svc);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return listaServicios;
    }


    //*******************************
    //SELECT * FROM servicios
    //*******************************
    public static ObservableList<Servicio> buscarServicios () throws SQLException, ClassNotFoundException {
        String selectServicios = "SELECT carrier_id,carrier,servicios.id,tipo_cobro_id,duracion,nombre,segmento,shortcode,precio FROM servicios INNER JOIN carriers ON carrier_id = carriers.id";

        try {
            //Get ResultSet from dbExecuteQuery method
            connection = ConnectionUtil.connectdb();
            preparedStatement = connection.prepareStatement(selectServicios);
            resultSet = preparedStatement.executeQuery();

            ObservableList<Servicio> listaServicios = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Servicio svc = new Servicio();
                svc.setCarrier_id(resultSet.getInt("carrier_id"));
                svc.setCarrierName(resultSet.getString("carrier"));
                svc.setId(resultSet.getInt("id"));
                svc.setTipo_cobro_id(resultSet.getInt("tipo_cobro_id"));
                svc.setDuracion(resultSet.getString("duracion"));
                svc.setNombre(resultSet.getString("nombre"));
                svc.setSegmento(resultSet.getString("segmento"));
                svc.setShortcode(resultSet.getString("shortcode"));
                svc.setPrecio(resultSet.getDouble("precio"));
                listaServicios.add(svc);
            }
            connection.close();
            return listaServicios;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            connection.close();
        }
    }

    //*******************************
    //Seleccionar un servicio
    //*******************************
    public static Servicio seleccionarServicio(int idSvc) throws SQLException {
        String selectServicio = "SELECT * FROM servicios WHERE id = " + idSvc;
        try{
            connection = ConnectionUtil.connectdb();
            preparedStatement = connection.prepareStatement(selectServicio);
            resultSet = preparedStatement.executeQuery();
            Servicio svc = new Servicio();
            while (resultSet.next()) {
                System.out.println(idSvc);
                System.out.println(resultSet.getInt("tipo_cobro_id"));
                System.out.println(resultSet.getInt("carrier_id"));
                svc.setCarrier_id(resultSet.getInt("carrier_id"));
                svc.setId(resultSet.getInt("id"));
                svc.setTipo_cobro_id(resultSet.getInt("tipo_cobro_id"));
                svc.setDuracion(resultSet.getString("duracion"));
                svc.setNombre(resultSet.getString("nombre"));
                svc.setSegmento(resultSet.getString("segmento"));
                svc.setShortcode(resultSet.getString("shortcode"));
                svc.setPrecio(resultSet.getDouble("precio"));
            }
            return svc;
        }catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            connection.close();
        }

    }


    //*******************************
    //Agregar un servicio
    //*******************************
    public static void insertarServicio(Servicio svc) throws SQLException {
        connection = ConnectionUtil.connectdb();
        String sql = "INSERT INTO servicios(nombre,duracion,segmento,shortcode,precio,carrier_id,tipo_cobro_id) VALUES(?,?,?,?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, svc.getNombre());
            preparedStatement.setString(2, svc.getDuracion());
            preparedStatement.setString(3, svc.getSegmento());
            preparedStatement.setString(4, svc.getShortcode());
            preparedStatement.setDouble(5, svc.getPrecio());
            preparedStatement.setInt(6, svc.getCarrier_id());
            preparedStatement.setInt(7, svc.getTipo_cobro_id());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            connection.close();
        }
    }

    //*******************************
    //Actualizar un servicio
    //*******************************
    public static void actualizarServicio(Servicio svc) throws SQLException {
        connection = ConnectionUtil.connectdb();
        String sql = "UPDATE servicios SET nombre = ?,duracion = ?,segmento = ?,shortcode = ?,precio = ? ,carrier_id = ?,tipo_cobro_id = ? WHERE id = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, svc.getNombre());
            preparedStatement.setString(2, svc.getDuracion());
            preparedStatement.setString(3, svc.getSegmento());
            preparedStatement.setString(4, svc.getShortcode());
            preparedStatement.setDouble(5, svc.getPrecio());
            preparedStatement.setInt(6, svc.getCarrier_id());
            preparedStatement.setInt(7, svc.getTipo_cobro_id());
            preparedStatement.setInt(8,svc.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            connection.close();
        }
    }

    //*******************************
    //Baja de  un servicio
    //*******************************
    public static void eliminarServicio(Servicio svc) throws SQLException {
        connection = ConnectionUtil.connectdb();
        String sql = "DELETE FROM servicios WHERE id = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,svc.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }finally {
            connection.close();
        }
    }

    public static int getCarrierId(String carrier) throws SQLException {
        int carrier_id = 0;
        connection = ConnectionUtil.connectdb();
        String sqlCarrierId = " SELECT id FROM carriers WHERE carrier= '" + carrier + "'";
        //infoBox(sqlCarrierId, null, "Valor de Combo");
        //System.out.println(sqlCarrierId);
        try {
            connection = ConnectionUtil.connectdb();
            preparedStatement = connection.prepareStatement(sqlCarrierId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                carrier_id = resultSet.getInt("id");

            }
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        } finally {
            connection.close();
        }
        return carrier_id;
    }

    public static int getTipoCobroId(String tipoCobro) throws SQLException {
        int tipo_cobro_id = 0;
        connection = ConnectionUtil.connectdb();
        String sqlCarrierId = " SELECT id FROM tipo_cobro WHERE tipo= '" + tipoCobro + "'";
        //infoBox(sqlCarrierId, null, "Valor de Combo");
        System.out.println(sqlCarrierId);
        try {
            connection = ConnectionUtil.connectdb();
            preparedStatement = connection.prepareStatement(sqlCarrierId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                tipo_cobro_id = resultSet.getInt("id");

            }
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        } finally {
            connection.close();
        }
        return tipo_cobro_id;
    }


    public static String getTipoCobro(int id) throws SQLException {
        String tipo_cobro = "";
        connection = ConnectionUtil.connectdb();
        String sqlCarrierId = " SELECT tipo FROM tipo_cobro WHERE id= '" + id + "'";
        //infoBox(sqlCarrierId, null, "Valor de Combo");
        System.out.println(sqlCarrierId);
        try {
            connection = ConnectionUtil.connectdb();
            preparedStatement = connection.prepareStatement(sqlCarrierId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                tipo_cobro = resultSet.getString("tipo");

            }
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        } finally {
            connection.close();
        }
        return tipo_cobro;
    }

    public static String getCarrier(int id) throws SQLException {
        String carrier = "";
        connection = ConnectionUtil.connectdb();
        String sqlCarrierId = " SELECT carrier FROM carriers WHERE id= '" + id + "'";
        //infoBox(sqlCarrierId, null, "Valor de Combo");
        //System.out.println(sqlCarrierId);
        try {
            connection = ConnectionUtil.connectdb();
            preparedStatement = connection.prepareStatement(sqlCarrierId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                carrier = resultSet.getString("carrier");

            }
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        } finally {
            connection.close();
        }
        return carrier;
    }

}
