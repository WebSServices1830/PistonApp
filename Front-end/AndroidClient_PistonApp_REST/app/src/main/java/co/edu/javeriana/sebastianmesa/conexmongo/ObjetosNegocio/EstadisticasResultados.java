package co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EstadisticasResultados /* implements Comparable<EstadisticasResultados>*/ {

    private String id;
    private Date fecha;
    private String lugar;
    private String ultimoEnGanar;

    public EstadisticasResultados(String id, Date fecha, String lugar, String ultimoEnGanar) {
        this.id = id;
        this.fecha = fecha;
        this.lugar = lugar;
        this.ultimoEnGanar = ultimoEnGanar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getUltimoEnGanar() {
        return ultimoEnGanar;
    }

    public void setUltimoEnGanar(String ultimoEnGanar) {
        this.ultimoEnGanar = ultimoEnGanar;
    }

    //    @Override
//    public int compareTo(EstadisticasResultados another) {
//        return getListagranPremios().get().getFecha() - another.getListagranPremios();
//    }
}
