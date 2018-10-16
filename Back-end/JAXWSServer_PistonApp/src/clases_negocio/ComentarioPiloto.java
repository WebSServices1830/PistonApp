package clases_negocio;

import org.bson.types.ObjectId;

public class ComentarioPiloto {
	
	private ObjectId id;
	private String contenido;
	private int calificacion;
	private ObjectId usuario;
	private ObjectId piloto;
	
	public ComentarioPiloto(){
		
	}
	
	public ComentarioPiloto(String contenido, int calificacion, ObjectId usuario, ObjectId piloto) {
		this.id = new ObjectId();
		this.contenido = contenido;
		this.calificacion = calificacion;
		this.usuario= usuario;
		this.piloto= piloto;
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

	public ObjectId getUsuario() {
		return usuario;
	}

	public void setUsuario(ObjectId usuario) {
		this.usuario = usuario;
	}

	public ObjectId getPiloto() {
		return piloto;
	}

	public void setPiloto(ObjectId piloto) {
		this.piloto = piloto;
	}
	
}
