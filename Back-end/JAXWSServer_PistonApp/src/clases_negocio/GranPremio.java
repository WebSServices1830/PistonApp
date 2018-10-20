package clases_negocio;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.bson.types.ObjectId;

public class GranPremio {
	
	private String id_str;
	private ObjectId id;
	private Date fecha;
	private int cantVueltas;
	private Date mejorVuelta;
	private String pista;
	private String id_campeonato;
	private List<String> id_clasificaciones;
	
	public GranPremio(){
		
	}
	
	public GranPremio( Date fecha, int cantVueltas, Date mejorVuelta, String pista,
			String id_campeonato) {
		super();
		this.id = new ObjectId();
		this.fecha = fecha;
		this.cantVueltas = cantVueltas;
		this.mejorVuelta = mejorVuelta;
		this.pista = pista;
		this.id_campeonato = id_campeonato;
		this.id_clasificaciones = new ArrayList<String>();
		this.id_str= this.id.toString();
	}
	public String getId_str() {
		return id_str;
	}
	public void setId_str(String id_str) {
		this.id_str = id_str;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getCantVueltas() {
		return cantVueltas;
	}
	public void setCantVueltas(int cantVueltas) {
		this.cantVueltas = cantVueltas;
	}
	public Date getMejorVuelta() {
		return mejorVuelta;
	}
	public void setMejorVuelta(Date mejorVuelta) {
		this.mejorVuelta = mejorVuelta;
	}
	public String getPista() {
		return pista;
	}
	public void setPista(String pista) {
		this.pista = pista;
	}

	public String getId_campeonato() {
		return id_campeonato;
	}

	public void setId_campeonato(String id_campeonato) {
		this.id_campeonato = id_campeonato;
	}

	public List<String> getId_clasificaciones() {
		return id_clasificaciones;
	}

	public void setId_clasificaciones(List<String> id_clasificaciones) {
		this.id_clasificaciones = id_clasificaciones;
	}
	
}
