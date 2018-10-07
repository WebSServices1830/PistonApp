package co.javeriana.webservice;

public class Usuario {
	
	private String nombreUsuario;
	private String contra;
	private int edad;
	private String descripcion;
	private String foto;
	private boolean admin;
	private long bolsillo;
	
	
	
	
	public Usuario(String nombreUsuario, String contra, int edad, String descripcion, String foto, boolean admin) {
		super();
		this.nombreUsuario = nombreUsuario;
		this.contra = contra;
		this.edad = edad;
		this.descripcion = descripcion;
		this.foto = foto;
		this.admin = admin;
		this.bolsillo = 0;
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
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
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

}
