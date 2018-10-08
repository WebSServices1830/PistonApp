package co.javeriana.webservice;

import java.time.LocalTime;

import org.bson.types.ObjectId;

public class Record {

	private ObjectId id;
	private LocalTime recordVuleta_tiempo;
	private String recordVuelta_piloto;
	private int recordVuelta_anio;
	private ObjectId record;
	
	
	
	
	public Record() {
		super();
	}
	public Record( LocalTime recordVuleta_tiempo, String recordVuelta_piloto, int recordVuelta_anio,
			ObjectId record) {
		super();
		this.id = new ObjectId();
		this.recordVuleta_tiempo = recordVuleta_tiempo;
		this.recordVuelta_piloto = recordVuelta_piloto;
		this.recordVuelta_anio = recordVuelta_anio;
		this.record = record;
	}
	public LocalTime getRecordVuleta_tiempo() {
		return recordVuleta_tiempo;
	}
	public void setRecordVuleta_tiempo(LocalTime recordVuleta_tiempo) {
		this.recordVuleta_tiempo = recordVuleta_tiempo;
	}
	public String getRecordVuelta_piloto() {
		return recordVuelta_piloto;
	}
	public void setRecordVuelta_piloto(String recordVuelta_piloto) {
		this.recordVuelta_piloto = recordVuelta_piloto;
	}
	public int getRecordVuelta_anio() {
		return recordVuelta_anio;
	}
	public void setRecordVuelta_anio(int recordVuelta_anio) {
		this.recordVuelta_anio = recordVuelta_anio;
	}
	public ObjectId getRecord() {
		return record;
	}
	public void setRecord(ObjectId record) {
		this.record = record;
	}
	
	
}
