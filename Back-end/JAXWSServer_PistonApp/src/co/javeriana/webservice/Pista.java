package co.javeriana.webservice;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

public class Pista {
	
	private ObjectId id;
	private String ciudad;
	private String foto_ref;
	private String nombreUltimoGanador;
	private int numeroVueltas;
	private float distanciaCarreara_km;
	private float longitudCircuito_km;
	private ObjectId record;
	private ObjectId granpremio;
	private List<ObjectId> comentarios;
	
	
	
	
	public Pista() {
		super();
	}
	public Pista(String ciudad, String foto_ref, String nombreUltimoGanador, int numeroVueltas,
			float distanciaCarreara_km, float longitudCircuito_km, ObjectId record, ObjectId granpremio) {
		super();
		this.id = new ObjectId();
		this.ciudad = ciudad;
		this.foto_ref = foto_ref;
		this.nombreUltimoGanador = nombreUltimoGanador;
		this.numeroVueltas = numeroVueltas;
		this.distanciaCarreara_km = distanciaCarreara_km;
		this.longitudCircuito_km = longitudCircuito_km;
		this.record = record;
		this.granpremio = granpremio;
		this.comentarios = new ArrayList<ObjectId>();
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
	public int getNumeroVueltas() {
		return numeroVueltas;
	}
	public void setNumeroVueltas(int numeroVueltas) {
		this.numeroVueltas = numeroVueltas;
	}
	public float getDistanciaCarreara_km() {
		return distanciaCarreara_km;
	}
	public void setDistanciaCarreara_km(float distanciaCarreara_km) {
		this.distanciaCarreara_km = distanciaCarreara_km;
	}
	public float getLongitudCircuito_km() {
		return longitudCircuito_km;
	}
	public void setLongitudCircuito_km(float longitudCircuito_km) {
		this.longitudCircuito_km = longitudCircuito_km;
	}
	public ObjectId getRecord() {
		return record;
	}
	public void setRecord(ObjectId record) {
		this.record = record;
	}
	public ObjectId getGranpremio() {
		return granpremio;
	}
	public void setGranpremio(ObjectId granpremio) {
		this.granpremio = granpremio;
	}
	public List<ObjectId> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<ObjectId> comentarios) {
		this.comentarios = comentarios;
	}
	
	
	
}
