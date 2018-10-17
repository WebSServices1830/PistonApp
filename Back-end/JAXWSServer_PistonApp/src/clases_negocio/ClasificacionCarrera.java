package clases_negocio;

import java.time.LocalTime;
import java.util.List;

import org.bson.types.ObjectId;

public class ClasificacionCarrera {

	private String id_str;
	private ObjectId id;
	private int puntaje;
	private LocalTime tiempo;
	private int posicion; 
	private ObjectId competidor;
	
	public ClasificacionCarrera(){
		
	}
	
	public ClasificacionCarrera(int puntaje, LocalTime tiempo, int posicion, ObjectId competidor) {
		super();
		this.id = new ObjectId();
		this.puntaje = puntaje;
		this.tiempo = tiempo;
		this.posicion = posicion;
		this.competidor = competidor;
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
	public int getPuntaje() {
		return puntaje;
	}
	public void setPuntaje(int puntaje) {
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
	public ObjectId getCompetidor() {
		return competidor;
	}
	public void setCompetidor(ObjectId competidor) {
		this.competidor = competidor;
	}
	
	
}
