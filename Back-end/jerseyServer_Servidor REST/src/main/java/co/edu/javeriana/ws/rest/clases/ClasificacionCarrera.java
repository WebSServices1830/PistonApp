package co.edu.javeriana.ws.rest.clases;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;

@XmlRootElement
public class ClasificacionCarrera {

	private String id_str;
	private ObjectId id;
	private int puntaje;
	private Date tiempo;
	private String competidor;
	
	public ClasificacionCarrera(){
		
	}
	
	public ClasificacionCarrera(int puntaje, Date tiempo, String competidor) {
		super();
		this.id = new ObjectId();
		this.puntaje = puntaje;
		this.tiempo = tiempo;
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
	public Date getTiempo() {
		return tiempo;
	}
	public void setTiempo(Date tiempo) {
		this.tiempo = tiempo;
	}
	public String getCompetidor() {
		return competidor;
	}
	public void setCompetidor(String competidor) {
		this.competidor = competidor;
	}
	
	
}
