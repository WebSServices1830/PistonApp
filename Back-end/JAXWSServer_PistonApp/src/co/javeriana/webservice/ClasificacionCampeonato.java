package co.javeriana.webservice;

import java.time.LocalTime;

import org.bson.types.ObjectId;

public class ClasificacionCampeonato {
	
	private ObjectId id;
	private String puntaje;
	private LocalTime tiempo;
	private int posicion;
	private ObjectId piloto;
	
	
	
	
	public ClasificacionCampeonato() {
		super();
	}
	public ClasificacionCampeonato(String puntaje, LocalTime tiempo, int posicion, ObjectId piloto) {
		super();
		this.id = new ObjectId();
		this.puntaje = puntaje;
		this.tiempo = tiempo;
		this.posicion = posicion;
		this.piloto = piloto;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getPuntaje() {
		return puntaje;
	}
	public void setPuntaje(String puntaje) {
		this.puntaje = puntaje;
	}
	public LocalTime getTiempo() {
		return tiempo;
	}
	public void setTiempo(LocalTime tiempo) {
		this.tiempo = tiempo;
	}
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	public ObjectId getPiloto() {
		return piloto;
	}
	public void setPiloto(ObjectId piloto) {
		this.piloto = piloto;
	}
	
	
	
}
