package co.edu.javeriana.ws.rest.clases;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

public class Pista {
	
	private String id_str;
	private ObjectId id;
	private String ciudad;
	private String foto_ref;
	private String nombreUltimoGanador;
	private float distanciaCarrera_km;
	private float longitudCircuito_km;
	private Record record;
	private List<String> comentarios;
	private float calificacion;
	
	public Pista() {
		super();
	}
	public Pista(String ciudad, String foto_ref, String nombreUltimoGanador,
			float distanciaCarrera_km, float longitudCircuito_km, Record record) {
		super();
		this.id = new ObjectId();
		this.ciudad = ciudad;
		this.foto_ref = foto_ref;
		this.nombreUltimoGanador = nombreUltimoGanador;
		this.distanciaCarrera_km = distanciaCarrera_km;
		this.longitudCircuito_km = longitudCircuito_km;
		this.record = record;
		this.comentarios = new ArrayList<String>();
		this.id_str= this.id.toString();
		this.calificacion=0;
	}
	
	public float getCalificacion() {
		return calificacion;
	}
	public void setCalificacion(float calificacion) {
		this.calificacion = calificacion;
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
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getFoto_ref() {
		return foto_ref;
	}
	public void setFoto_ref(String foto_ref) {
		this.foto_ref = foto_ref;
	}
	public String getNombreUltimoGanador() {
		return nombreUltimoGanador;
	}
	public void setNombreUltimoGanador(String nombreUltimoGanador) {
		this.nombreUltimoGanador = nombreUltimoGanador;
	}
	public float getDistanciaCarrera_km() {
		return distanciaCarrera_km;
	}
	public void setDistanciaCarrera_km(float distanciaCarreara_km) {
		this.distanciaCarrera_km = distanciaCarreara_km;
	}
	public float getLongitudCircuito_km() {
		return longitudCircuito_km;
	}
	public void setLongitudCircuito_km(float longitudCircuito_km) {
		this.longitudCircuito_km = longitudCircuito_km;
	}
	public Record getRecord() {
		return record;
	}
	public void setRecord(Record record) {
		this.record = record;
	}
	public List<String> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<String> comentarios) {
		this.comentarios = comentarios;
	}
	
}
