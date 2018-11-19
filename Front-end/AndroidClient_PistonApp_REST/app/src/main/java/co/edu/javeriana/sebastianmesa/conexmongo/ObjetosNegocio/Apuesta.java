package co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio;

import org.bson.types.ObjectId;

public class Apuesta {
    private String str_id;
    private ObjectId id;
    private String usuario;
    private String piloto;
    private String granpremio;
    private double monto;




    public Apuesta(String usuario, String piloto, String granpremio, double monto2) {
        super();
        this.id= new ObjectId();
        this.str_id = id.toString();
        this.usuario = usuario;
        this.piloto = piloto;
        this.granpremio = granpremio;
        this.monto = monto2;
    }
    public String getStr_id() {
        return str_id;
    }
    public void setStr_id(String str_id) {
        this.str_id = str_id;
    }
    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getPiloto() {
        return piloto;
    }
    public void setPiloto(String piloto) {
        this.piloto = piloto;
    }
    public String getGranpremio() {
        return granpremio;
    }
    public void setGranpremio(String granpremio) {
        this.granpremio = granpremio;
    }
    public double getMonto() {
        return monto;
    }
    public void setMonto(double monto) {
        this.monto = monto;
    }
}
