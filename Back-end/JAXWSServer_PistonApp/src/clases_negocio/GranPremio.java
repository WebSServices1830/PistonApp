package clases_negocio;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.bson.types.ObjectId;

public class GranPremio {
	
	private ObjectId id;
	private Date fecha;
	private int cantVueltas;
	private LocalTime mejorVuelta;
	private ObjectId pista;
	private ObjectId campeonato;
	private List<ObjectId> clasificaciones;
	
	public GranPremio(){
		
	}
	
	public GranPremio( Date fecha, int cantVueltas, LocalTime mejorVuelta, ObjectId pista,
			ObjectId campeonato) {
		super();
		this.id = new ObjectId();
		this.fecha = fecha;
		this.cantVueltas = cantVueltas;
		this.mejorVuelta = mejorVuelta;
		this.pista = pista;
		this.campeonato = campeonato;
		this.clasificaciones = new ArrayList<ObjectId>();
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
	public ObjectId getPista() {
		return pista;
	}
	public void setPista(ObjectId pista) {
		this.pista = pista;
	}
	public ObjectId getCampeonato() {
		return campeonato;
	}
	public void setCampeonato(ObjectId campeonato) {
		this.campeonato = campeonato;
	}
	public List<ObjectId> getClasificaciones() {
		return clasificaciones;
	}
	public void setClasificaciones(List<ObjectId> clasificaciones) {
		this.clasificaciones = clasificaciones;
	}
	
	
}
