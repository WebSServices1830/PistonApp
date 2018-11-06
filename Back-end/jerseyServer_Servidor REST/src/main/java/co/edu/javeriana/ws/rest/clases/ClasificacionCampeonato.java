package co.edu.javeriana.ws.rest.clases;

import java.time.LocalTime;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;

@XmlRootElement
public class ClasificacionCampeonato {
	
	private String id_str;
	private ObjectId id;
	private int puntaje;
	private int posicion;
	private String piloto;
	
	
	public ClasificacionCampeonato() {
		super();
	}
	public ClasificacionCampeonato(int puntaje, int posicion, String piloto) {
		super();
		this.id = new ObjectId();
		this.puntaje = puntaje;
		this.posicion = posicion;
		this.piloto = piloto;
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
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	public String getPiloto() {
		return piloto;
	}
	public void setPiloto(String piloto) {
		this.piloto = piloto;
	}
	
	
	
}
