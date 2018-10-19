package clases_negocio;

import org.bson.types.ObjectId;

public class ComentarioPiloto {
	
	private String id_str;
	private ObjectId id;
	private String contenido;
	private int calificacion;
	private String usuario;
	private String piloto;
	
	public ComentarioPiloto(){
		
	}
	
	public ComentarioPiloto(String contenido, int calificacion, String usuario, String piloto) {
		this.id = new ObjectId();
		this.contenido = contenido;
		this.calificacion = calificacion;
		this.usuario= usuario;
		this.piloto= piloto;
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

	public int getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(int calificacion) {
		this.calificacion = calificacion;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPiloto() {
		return piloto;
	}

	public void setPiloto(String piloto) {
		this.piloto = piloto;
	}
	
}
