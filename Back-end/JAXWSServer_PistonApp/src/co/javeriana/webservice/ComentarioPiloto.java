package co.javeriana.webservice;

public class ComentarioPiloto {

	private String contenido;
	private int calificacion;
	private Usuario usuario;
	private Piloto piloto;
	
	
	
	
	public ComentarioPiloto(String contenido, int calificacion, Usuario usuario, Piloto piloto) {
		super();
		this.contenido = contenido;
		this.calificacion = calificacion;
		this.usuario = usuario;
		this.piloto = piloto;
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
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Piloto getPiloto() {
		return piloto;
	}
	public void setPiloto(Piloto piloto) {
		this.piloto = piloto;
	}
	
}
