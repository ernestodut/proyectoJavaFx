package model;

import javafx.beans.property.*;

public class Servicio {

    private IntegerProperty id;
    private StringProperty nombre;
    private StringProperty segmento;
    private StringProperty duracion;
    private StringProperty shortcode;
    private DoubleProperty precio;
    private IntegerProperty carrier_id;
    private IntegerProperty tipo_cobro_id;
    private StringProperty carrierName;

    public String getCarrierName() {
        return carrierName.get();
    }

    public StringProperty carrierNameProperty() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName.set(carrierName);
    }



    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getSegmento() {
        return segmento.get();
    }

    public StringProperty segmentoProperty() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento.set(segmento);
    }

    public String getDuracion() {
        return duracion.get();
    }

    public StringProperty duracionProperty() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion.set(duracion);
    }

    public String getShortcode() {
        return shortcode.get();
    }

    public StringProperty shortcodeProperty() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode.set(shortcode);
    }

    public double getPrecio() {
        return precio.get();
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public int getCarrier_id() {
        return carrier_id.get();
    }

    public IntegerProperty carrier_idProperty() {
        return carrier_id;
    }

    public void setCarrier_id(int carrier_id) {
        this.carrier_id.set(carrier_id);
    }

    public int getTipo_cobro_id() {
        return tipo_cobro_id.get();
    }

    public IntegerProperty tipo_cobro_idProperty() {
        return tipo_cobro_id;
    }

    public void setTipo_cobro_id(int tipo_cobro_id) {
        this.tipo_cobro_id.set(tipo_cobro_id);
    }

    public Servicio(){
        this.id = new SimpleIntegerProperty();
        this.carrier_id = new SimpleIntegerProperty();
        this.tipo_cobro_id = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.segmento = new SimpleStringProperty();
        this.shortcode = new SimpleStringProperty();
        this.duracion = new SimpleStringProperty();
        this.precio = new SimpleDoubleProperty();
        this.carrierName = new SimpleStringProperty();
    }


}
