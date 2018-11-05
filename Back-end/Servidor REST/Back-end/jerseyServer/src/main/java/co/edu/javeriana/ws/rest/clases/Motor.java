package co.edu.javeriana.ws.rest.clases;


public class Motor {
	private String referencia;
	private String cilindraje;
	private String configuracion;
	private boolean turbo;
	
	public Motor() {
		
	}
	
	public Motor(String referencia, String cilindraje, String configuracion, boolean turbo) {
		super();
		this.referencia = referencia;
		this.cilindraje = cilindraje;
		this.configuracion = configuracion;
		this.turbo = turbo;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getCilindraje() {
		return cilindraje;
	}

	public void setCilindraje(String cilindraje) {
		this.cilindraje = cilindraje;
	}

	public String getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(String configuracion) {
		this.configuracion = configuracion;
	}

	public boolean isTurbo() {
		return turbo;
	}

	public void setTurbo(boolean turbo) {
		this.turbo = turbo;
	}
	
}
