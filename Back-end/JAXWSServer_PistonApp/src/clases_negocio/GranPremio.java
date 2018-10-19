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
	private LocalTime mejorVuelta;
	private String pista;
	private String campeonato;
	private List<String> clasificaciones;
	
	public GranPremio(){
		
	}
	
	public GranPremio( Date fecha, int cantVueltas, LocalTime mejorVuelta, String pista,
			String campeonato) {
		super();
		this.id = new ObjectId();
		this.fecha = fecha;
		this.cantVueltas = cantVueltas;
		this.mejorVuelta = mejorVuelta;
		this.pista = pista;
		this.campeonato = campeonato;
		this.clasificaciones = new ArrayList<String>();
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
	public LocalTime getMejorVuelta() {
		return mejorVuelta;
	}
	public void setMejorVuelta(LocalTime mejorVuelta) {
		this.mejorVuelta = mejorVuelta;
	}

	public String getPista() {
		return pista;
	}

	public void setPista(String pista) {
		this.pista = pista;
	}

	public String getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(String campeonato) {
		this.campeonato = campeonato;
	}

	public List<String> getClasificaciones() {
		return clasificaciones;
	}

	public void setClasificaciones(List<String> clasificaciones) {
		this.clasificaciones = clasificaciones;
	}
	
}
