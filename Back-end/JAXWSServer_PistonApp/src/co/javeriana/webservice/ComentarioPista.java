package co.javeriana.webservice;

import org.bson.types.ObjectId;

public class ComentarioPista {

	private ObjectId id;
	private String contenido;
	private int calificacion;
	private ObjectId pista;
	private ObjectId usuario;
	
	
	
	public ComentarioPista() {
		super();
	}
	public ComentarioPista(String contenido, int calificacion, ObjectId pista, ObjectId usuario) {
		super();
		this.id = new ObjectId();
		this.contenido = contenido;
		this.calificacion = calificacion;
		this.pista = pista;
		this.usuario = usuario;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	public int getCalificacion() {
		return calificacion;
	}
	public void setCalificacion(int calificacion) {
		this.calificacion = calificacion;
	}
	public ObjectId getPista() {
		return pista;
	}
	public void setPista(ObjectId pista) {
		this.pista = pista;
	}
	public ObjectId getUsuario() {
		return usuario;
	}
	public void setUsuario(ObjectId usuario) {
		this.usuario = usuario;
	}
	
	
	
	
	
}
