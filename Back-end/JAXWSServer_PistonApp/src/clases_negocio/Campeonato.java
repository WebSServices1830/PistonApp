package clases_negocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

public class Campeonato {
	
	private String id_str;
	private ObjectId id;
	private String nombre;
	private Date fecha_inicio;
	private Date fecha_final;
	private List<ObjectId> gran_premios;
	private List<ObjectId> clasificaciones;
	
	
	public Campeonato() {
		super();
	}
	public Campeonato(String nombre, Date fecha_inicio, Date fecha_final) {
		super();
		this.id= new ObjectId();
		this.nombre = nombre;
		this.fecha_inicio = fecha_inicio;
		this.fecha_final = fecha_final;
		this.gran_premios = new ArrayList<ObjectId>();
		this.clasificaciones = new ArrayList<ObjectId>();
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
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFecha_inicio() {
		return fecha_inicio;
	}
	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	public Date getFecha_final() {
		return fecha_final;
	}
	public void setFecha_final(Date fecha_final) {
		this.fecha_final = fecha_final;
	}
	public List<ObjectId> getGran_premios() {
		return gran_premios;
	}
	public void setGran_premios(List<ObjectId> gran_premios) {
		this.gran_premios = gran_premios;
	}
	public List<ObjectId> getClasificaciones() {
		return clasificaciones;
	}
	public void setClasificaciones(List<ObjectId> clasificaciones) {
		this.clasificaciones = clasificaciones;
	}
	
	
	
}
