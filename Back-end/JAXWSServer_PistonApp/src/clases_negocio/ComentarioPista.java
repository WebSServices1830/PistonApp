package clases_negocio;

import org.bson.types.ObjectId;

public class ComentarioPista {

	private String id_str;
	private ObjectId id;
	private String contenido;
	private float calificacion;
	private ObjectId pista;
	private ObjectId usuario;
	
	
	
	public ComentarioPista() {
		super();
	}
	public ComentarioPista(String contenido, float calificacion, ObjectId pista, ObjectId usuario) {
		super();
		this.id = new ObjectId();
		this.contenido = contenido;
		this.calificacion = calificacion;
		this.pista = pista;
		this.usuario = usuario;
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
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	public float getCalificacion() {
		return calificacion;
	}
	public void setCalificacion(float calificacion) {
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
