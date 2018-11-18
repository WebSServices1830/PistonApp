package co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio;

import java.util.Date;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

public class Usuario {
	
	private String id_str;
	private ObjectId id;
	private String nombreUsuario;
	private String contra;
	private Date fechaNacimiento;
	private String urlFoto;
	private boolean admin;
	private double bolsillo;
	
	public Usuario(){
		
	}
	public Usuario(String nombreUsuario, String contra, Date fechaNacimiento, String urlFoto, boolean admin) {
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
	public double getBolsillo() {
		return bolsillo;
	}
	public void setBolsillo(double bolsillo) {
		this.bolsillo = bolsillo;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	

}
