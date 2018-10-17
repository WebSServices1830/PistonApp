


package clases_negocio;

import java.util.GregorianCalendar;

import org.bson.types.ObjectId;

public class Usuario {
	
	private String id_str;
	private ObjectId id;
	private String nombreUsuario;
	private String contra;
	private GregorianCalendar fechaNacimiento;
	private String urlFoto;
	private boolean admin;
	private long bolsillo;
	
	public Usuario(){
		
	}
	public Usuario(String nombreUsuario, String contra, GregorianCalendar fechaNacimiento, String urlFoto, boolean admin) {
		this.id = new ObjectId();
		this.nombreUsuario = nombreUsuario;
		this.contra = contra;
		this.fechaNacimiento = fechaNacimiento;
		this.urlFoto = urlFoto;
		this.admin = admin;
		this.bolsillo = 0;
		this.id_str= this.id.toString();
	}
	
	
	public String getId_str() {
		return id_str;
	}
	public void setId_str(String id_str) {
		this.id_str = id_str;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getContra() {
		return contra;
	}
	public void setContra(String contra) {
		this.contra = contra;
	}
	public String getUrlFoto() {
		return urlFoto;
	}

	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}

	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public long getBolsillo() {
		return bolsillo;
	}
	public void setBolsillo(long bolsillo) {
		this.bolsillo = bolsillo;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public GregorianCalendar getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(GregorianCalendar fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	

}