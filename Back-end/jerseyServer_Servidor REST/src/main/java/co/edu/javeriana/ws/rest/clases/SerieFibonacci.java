package co.edu.javeriana.ws.rest.clases;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SerieFibonacci {
	
	private int posicion;
	private int valor;
	
	public SerieFibonacci() {
		
	}
	
	public SerieFibonacci(int posicion, int valor) {
		super();
		this.posicion = posicion;
		this.valor = valor;
	}
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	public int getValor() {
		return valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
	@Override
	public String toString() {
		return "SerieFibonacci [posicion=" + posicion + ", valor=" + valor + "]";
	}
}
