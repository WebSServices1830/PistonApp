package co.javeriana.webservice;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_Auto;
import CRUDs.CRUD_Campeonato;
import CRUDs.CRUD_ClasificacionCampeonato;
import CRUDs.CRUD_Escuderia;
import CRUDs.CRUD_GranPremio;
import CRUDs.CRUD_Piloto;
import CRUDs.CRUD_Pista;
import CRUDs.CRUD_Usuario;
import clases_mongoDB.ManejadorImagenes;
import clases_negocio.Auto;
import clases_negocio.Campeonato;
import clases_negocio.ClasificacionCampeonato;
import clases_negocio.Escuderia;
import clases_negocio.GranPremio;
import clases_negocio.Piloto;
import clases_negocio.Pista;
import clases_negocio.Record;

@WebService(name="infoCampeonato")
public class InformacionCampeonato {
	
	CRUD_Campeonato manejadorCampeonato = new CRUD_Campeonato();
	CRUD_GranPremio manejadorGranPremio = new CRUD_GranPremio();
	CRUD_Pista manejadorPista = new CRUD_Pista();
	CRUD_Piloto manejadorPiloto = new CRUD_Piloto();
	CRUD_Escuderia manejadorEscuderia = new CRUD_Escuderia();
	CRUD_Auto manejadorAuto = new CRUD_Auto();
	CRUD_ClasificacionCampeonato manejadorClasificacion_Campeonato = new CRUD_ClasificacionCampeonato();
	
	@WebMethod
	public void inicializarCampeonato() {
		//CAMPEONATO
		GregorianCalendar fechaInicio = new GregorianCalendar(2018,0,1);
		
		GregorianCalendar fechaFin = new GregorianCalendar(2018,11,31);
		
		Campeonato campeonato = manejadorCampeonato.campeonato_create("Campeonato 2018", fechaInicio.getTime(), fechaFin.getTime());
		
		//GRAN PREMIO 1
		GregorianCalendar fechaGranPremio_1 = new GregorianCalendar(2018,2,25);
		
		GregorianCalendar mejorVuelta_1 = new GregorianCalendar();
		mejorVuelta_1.set(Calendar.HOUR,1);
		mejorVuelta_1.set(Calendar.MINUTE,29);
		mejorVuelta_1.set(Calendar.SECOND,33);
		mejorVuelta_1.set(Calendar.MILLISECOND,283);
		
		//PISTA 1
		GregorianCalendar recordVuelta_1 = new GregorianCalendar();
	
		recordVuelta_1.set(Calendar.MINUTE, 1);
		recordVuelta_1.set(Calendar.SECOND, 24);
		recordVuelta_1.set(Calendar.MILLISECOND, 125);
		Record record_1 = new Record(recordVuelta_1.getTime(),"Michael Schumacher",2004);
		String fotoRef_1 = null;
		try {
			fotoRef_1 = ManejadorImagenes.saveImageIntoMongoDB("fotos/australiaGrandPrix.png", "Melbourne");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(fotoRef_1 != null) {
			Pista pista = manejadorPista.pista_create("Melbourne", fotoRef_1, "Sebastian Vettel", (float)307.57, (float)5.303, record_1);
			
			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_1.getTime(), 58, mejorVuelta_1.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}
		
		//GRAN PREMIO 2
		
				GregorianCalendar fechaGranPremio_2 = new GregorianCalendar(2018,3,8);
				
				GregorianCalendar mejorVuelta_2 = new GregorianCalendar();
				mejorVuelta_2.set(Calendar.HOUR,1);
				mejorVuelta_2.set(Calendar.MINUTE,32);
				mejorVuelta_2.set(Calendar.SECOND,01);
				mejorVuelta_2.set(Calendar.MILLISECOND,940);
				
				//PISTA 2
				GregorianCalendar recordVuelta_2 = new GregorianCalendar();

				recordVuelta_2.set(Calendar.MINUTE, 1);
				recordVuelta_2.set(Calendar.SECOND, 31);
				recordVuelta_2.set(Calendar.MILLISECOND, 447);
				Record record_2 = new Record(recordVuelta_2.getTime(),"Pedro de la Rosa",2005);
				String fotoRef_2 = null;
				try {
					fotoRef_2 = ManejadorImagenes.saveImageIntoMongoDB("fotos/BahrainGrandPrix.png", "Bahrain");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_2 != null) {
					Pista pista = manejadorPista.pista_create("Bahrain", fotoRef_2, "Sebastian Vettel", (float)308.23, (float)5.412, record_2);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_2.getTime(), 57, mejorVuelta_2.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				
				//GRAN PREMIO 3
				
				GregorianCalendar fechaGranPremio_3 = new GregorianCalendar(2018,3,15);
				
				GregorianCalendar mejorVuelta_3 = new GregorianCalendar();
				mejorVuelta_3.set(Calendar.HOUR,1);
				mejorVuelta_3.set(Calendar.MINUTE,35);
				mejorVuelta_3.set(Calendar.SECOND,36);
				mejorVuelta_3.set(Calendar.MILLISECOND,380);
				
				//PISTA 3
				GregorianCalendar recordVuelta_3 = new GregorianCalendar();
			
				recordVuelta_3.set(Calendar.MINUTE, 1);
				recordVuelta_3.set(Calendar.SECOND, 32);
				recordVuelta_3.set(Calendar.MILLISECOND, 238);
				Record record_3 = new Record(recordVuelta_3.getTime(),"Michael Schumacher",2004);
				String fotoRef_3 = null;
				try {
					fotoRef_3 = ManejadorImagenes.saveImageIntoMongoDB("fotos/ShangaiGrandPrix.png", "Shangai");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_3 != null) {
					Pista pista = manejadorPista.pista_create("Shangai", fotoRef_3, "Daniel Ricciardo", (float)305.066, (float)5.451, record_3);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_3.getTime(), 56, mejorVuelta_3.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
				//GRAN PREMIO 4
				
				GregorianCalendar fechaGranPremio_4 = new GregorianCalendar(2018,3,29);
				
				GregorianCalendar mejorVuelta_4 = new GregorianCalendar();
				mejorVuelta_4.set(Calendar.HOUR,1);
				mejorVuelta_4.set(Calendar.MINUTE,43);
				mejorVuelta_4.set(Calendar.SECOND,44);
				mejorVuelta_4.set(Calendar.MILLISECOND,291);
				
				//PISTA 4
				GregorianCalendar recordVuelta_4 = new GregorianCalendar();
		
				recordVuelta_4.set(Calendar.MINUTE, 1);
				recordVuelta_4.set(Calendar.SECOND, 43);
				recordVuelta_4.set(Calendar.MILLISECOND, 441);
				Record record_4 = new Record(recordVuelta_4.getTime(),"Sebastian Vettel",2017);
				String fotoRef_4 = null;
				try {
					fotoRef_4 = ManejadorImagenes.saveImageIntoMongoDB("fotos/AzerbaijanGrandPrix.png", "Azerbaijan");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_4 != null) {
					Pista pista = manejadorPista.pista_create("Azerbaijan", fotoRef_4, "Lewis Hamilton", (float)306.049, (float)6.003, record_4);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_4.getTime(), 51, mejorVuelta_4.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				
				//GRAN PREMIO 5
				
				GregorianCalendar fechaGranPremio_5 = new GregorianCalendar(2018,4,13);
				
				GregorianCalendar mejorVuelta_5 = new GregorianCalendar();
				mejorVuelta_5.set(Calendar.HOUR,1);
				mejorVuelta_5.set(Calendar.MINUTE,35);
				mejorVuelta_5.set(Calendar.SECOND,29);
				mejorVuelta_5.set(Calendar.MILLISECOND,972);
				
				//PISTA 5
				GregorianCalendar recordVuelta_5 = new GregorianCalendar();

				recordVuelta_5.set(Calendar.MINUTE, 1);
				recordVuelta_5.set(Calendar.SECOND, 18);
				recordVuelta_5.set(Calendar.MILLISECOND, 441);
				Record record_5 = new Record(recordVuelta_5.getTime(),"Daniel Ricciardo",2018);
				String fotoRef_5 = null;
				try {
					fotoRef_5 = ManejadorImagenes.saveImageIntoMongoDB("fotos/EspanaGrandPremio.png", "Barcelona");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_5 != null) {
					Pista pista = manejadorPista.pista_create("Barcelona", fotoRef_5, "Lewis Hamilton", (float)307.104, (float)4.655, record_5);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_5.getTime(), 66, mejorVuelta_5.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
				//GRAN PREMIO 6
				
				GregorianCalendar fechaGranPremio_6 = new GregorianCalendar(2018,4,27);
				
				GregorianCalendar mejorVuelta_6 = new GregorianCalendar();
				mejorVuelta_6.set(Calendar.HOUR,1);
				mejorVuelta_6.set(Calendar.MINUTE,42);
				mejorVuelta_6.set(Calendar.SECOND,54);
				mejorVuelta_6.set(Calendar.MILLISECOND,807);
				
				//PISTA 6
				GregorianCalendar recordVuelta_6 = new GregorianCalendar();
	
				recordVuelta_6.set(Calendar.MINUTE, 1);
				recordVuelta_6.set(Calendar.SECOND, 14);
				recordVuelta_6.set(Calendar.MILLISECOND, 260);
				Record record_6 = new Record(recordVuelta_6.getTime(),"Max Verstappen",2018);
				String fotoRef_6 = null;
				try {
					fotoRef_6 = ManejadorImagenes.saveImageIntoMongoDB("fotos/MontecarloGrandPrix.png", "Monaco");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_6 != null) {
					Pista pista = manejadorPista.pista_create("Principado de Monaco", fotoRef_6, "Daniel Ricciardo", (float)260.286, (float)3.337, record_6);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_6.getTime(), 78, mejorVuelta_6.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				
				//GRAN PREMIO 7
				
				GregorianCalendar fechaGranPremio_7 = new GregorianCalendar(2018,5,10);
				
				GregorianCalendar mejorVuelta_7 = new GregorianCalendar();
				mejorVuelta_7.set(Calendar.HOUR,1);
				mejorVuelta_7.set(Calendar.MINUTE,28);
				mejorVuelta_7.set(Calendar.SECOND,31);
				mejorVuelta_7.set(Calendar.MILLISECOND,377);
				
				//PISTA 7
				GregorianCalendar recordVuelta_7 = new GregorianCalendar();
		
				recordVuelta_7.set(Calendar.MINUTE, 1);
				recordVuelta_7.set(Calendar.SECOND, 13);
				recordVuelta_7.set(Calendar.MILLISECOND, 622);
				Record record_7 = new Record(recordVuelta_7.getTime(),"Rubens Barrichello",2004);
				String fotoRef_7 = null;
				try {
					fotoRef_7 = ManejadorImagenes.saveImageIntoMongoDB("fotos/CanadaGrandPrix.png", "Canada");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_7 != null) {
					Pista pista = manejadorPista.pista_create("Canada", fotoRef_7, "Sebastian Vettel", (float)305.27, (float)4.361, record_7);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_7.getTime(), 70, mejorVuelta_7.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				
				//GRAN PREMIO 8
				
				GregorianCalendar fechaGranPremio_8 = new GregorianCalendar(2018,5,24);
				
				GregorianCalendar mejorVuelta_8 = new GregorianCalendar();
				mejorVuelta_8.set(Calendar.HOUR,1);
				mejorVuelta_8.set(Calendar.MINUTE,30);
				mejorVuelta_8.set(Calendar.SECOND,11);
				mejorVuelta_8.set(Calendar.MILLISECOND,385);
				
				//PISTA 8
				GregorianCalendar recordVuelta_8 = new GregorianCalendar();
		
				recordVuelta_8.set(Calendar.MINUTE, 1);
				recordVuelta_8.set(Calendar.SECOND, 334);
				recordVuelta_8.set(Calendar.MILLISECOND, 225);
				Record record_8 = new Record(recordVuelta_8.getTime(),"Valtteri Bottas",2018);
				String fotoRef_8 = null;
				try {
					fotoRef_8 = ManejadorImagenes.saveImageIntoMongoDB("fotos/FranceGrandPrix.png", "France");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_8 != null) {
					Pista pista = manejadorPista.pista_create("France", fotoRef_8, "Lewis Hamilton", (float)309.69, (float)5.842, record_8);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_8.getTime(), 53, mejorVuelta_8.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
				//GRAN PREMIO 9
				
				GregorianCalendar fechaGranPremio_9 = new GregorianCalendar(2018,6,1);
				
				GregorianCalendar mejorVuelta_9 = new GregorianCalendar();
				mejorVuelta_4.set(Calendar.HOUR,1);
				mejorVuelta_4.set(Calendar.MINUTE,21);
				mejorVuelta_4.set(Calendar.SECOND,56);
				mejorVuelta_4.set(Calendar.MILLISECOND,024);
				
				//PISTA 9
				GregorianCalendar recordVuelta_9 = new GregorianCalendar();

				recordVuelta_9.set(Calendar.MINUTE, 1);
				recordVuelta_9.set(Calendar.SECOND, 06);
				recordVuelta_9.set(Calendar.MILLISECOND, 957);
				Record record_9 = new Record(recordVuelta_9.getTime(),"Kimi Räikkönen",2018);
				String fotoRef_9 = null;
				try {
					fotoRef_9 = ManejadorImagenes.saveImageIntoMongoDB("fotos/AustriaGrandPrix.png", "Austria");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_9!= null) {
					Pista pista = manejadorPista.pista_create("Austria", fotoRef_9, "Max Verstappen", (float)306.452, (float)4.318, record_9);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_9.getTime(), 71, mejorVuelta_9.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				
				//GRAN PREMIO 10
				
				GregorianCalendar fechaGranPremio_10 = new GregorianCalendar(2018,6,8);
				
				GregorianCalendar mejorVuelta_10 = new GregorianCalendar();
				mejorVuelta_10.set(Calendar.HOUR,1);
				mejorVuelta_10.set(Calendar.MINUTE,27);
				mejorVuelta_10.set(Calendar.SECOND,29);
				mejorVuelta_10.set(Calendar.MILLISECOND,784);
				
				//PISTA 10
				GregorianCalendar recordVuelta_10 = new GregorianCalendar();
		
				recordVuelta_10.set(Calendar.MINUTE, 1);
				recordVuelta_10.set(Calendar.SECOND, 30);
				recordVuelta_10.set(Calendar.MILLISECOND, 621);
				Record record_10 = new Record(recordVuelta_10.getTime(),"Lewis Hamilton",2017);
				String fotoRef_10 = null;
				try {
					fotoRef_10 = ManejadorImagenes.saveImageIntoMongoDB("fotos/GranBretaniaGrandPrix.png", "Gran Bretania");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_10 != null) {
					Pista pista = manejadorPista.pista_create("Gran Bretania", fotoRef_10, "Sebastian Vettel", (float)306.198, (float)5.891, record_10);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_10.getTime(), 52, mejorVuelta_10.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
				//GRAN PREMIO 11
				
				GregorianCalendar fechaGranPremio_11 = new GregorianCalendar(2018,6,22);
				
				GregorianCalendar mejorVuelta_11 = new GregorianCalendar();
				mejorVuelta_11.set(Calendar.HOUR,1);
				mejorVuelta_11.set(Calendar.MINUTE,32);
				mejorVuelta_11.set(Calendar.SECOND,29);
				mejorVuelta_11.set(Calendar.MILLISECOND,845);
				
				//PISTA 11
				GregorianCalendar recordVuelta_11 = new GregorianCalendar();
			
				recordVuelta_11.set(Calendar.MINUTE, 1);
				recordVuelta_11.set(Calendar.SECOND, 13);
				recordVuelta_11.set(Calendar.MILLISECOND, 780);
				Record record_11 = new Record(recordVuelta_11.getTime(),"Kimi Räikkönen",2004);
				String fotoRef_11 = null;
				try {
					fotoRef_11 = ManejadorImagenes.saveImageIntoMongoDB("fotos/GermanyGrandPrix.png", "Germany");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_11 != null) {
					Pista pista = manejadorPista.pista_create("Hockenheim", fotoRef_11, "Lewis Hamilton", (float)306.458, (float)4.574, record_11);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_11.getTime(), 67, mejorVuelta_11.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				
				//GRAN PREMIO 12
				
				GregorianCalendar fechaGranPremio_12 = new GregorianCalendar(2018,6,29);
				
				GregorianCalendar mejorVuelta_12 = new GregorianCalendar();
				mejorVuelta_12.set(Calendar.HOUR,1);
				mejorVuelta_12.set(Calendar.MINUTE,37);
				mejorVuelta_12.set(Calendar.SECOND,16);
				mejorVuelta_12.set(Calendar.MILLISECOND,427);
				
				//PISTA 12
				GregorianCalendar recordVuelta_12 = new GregorianCalendar();
		
				recordVuelta_12.set(Calendar.MINUTE, 1);
				recordVuelta_12.set(Calendar.SECOND, 19);
				recordVuelta_12.set(Calendar.MILLISECOND, 071);
				Record record_12 = new Record(recordVuelta_12.getTime(),"Michael Schumacher",2004);
				String fotoRef_12 = null;
				try {
					fotoRef_12 = ManejadorImagenes.saveImageIntoMongoDB("fotos/HungaryGrandPrix.png", "Hungary");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_12 != null) {
					Pista pista = manejadorPista.pista_create("Budapest", fotoRef_12, "Lewis Hamilton", (float)306.63, (float)4.381, record_12);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_12.getTime(), 70, mejorVuelta_12.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				//GRAN PREMIO 13
				
				GregorianCalendar fechaGranPremio_13 = new GregorianCalendar(2018,7,26);
				
				GregorianCalendar mejorVuelta_13 = new GregorianCalendar();
				mejorVuelta_13.set(Calendar.HOUR,1);
				mejorVuelta_13.set(Calendar.MINUTE,23);
				mejorVuelta_13.set(Calendar.SECOND,34);
				mejorVuelta_13.set(Calendar.MILLISECOND,476);
				
				//PISTA 13
				GregorianCalendar recordVuelta_13 = new GregorianCalendar();

				recordVuelta_13.set(Calendar.MINUTE, 1);
				recordVuelta_13.set(Calendar.SECOND, 46);
				recordVuelta_13.set(Calendar.MILLISECOND, 286);
				Record record_13 = new Record(recordVuelta_13.getTime(),"Valtteri Bottas",2018);
				String fotoRef_13 = null;
				try {
					fotoRef_13 = ManejadorImagenes.saveImageIntoMongoDB("fotos/BelgianGrandPrix.png", "Belgian");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_13 != null) {
					Pista pista = manejadorPista.pista_create("Lieja", fotoRef_13, "Sebastian Vettel", (float)308.052, (float)7.004, record_13);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_13.getTime(), 44, mejorVuelta_13.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}

//GRAN PREMIO 14
				
				GregorianCalendar fechaGranPremio_14 = new GregorianCalendar(2018,8,02);
				
				GregorianCalendar mejorVuelta_14 = new GregorianCalendar();
				mejorVuelta_14.set(Calendar.HOUR,1);
				mejorVuelta_14.set(Calendar.MINUTE,16);
				mejorVuelta_14.set(Calendar.SECOND,54);
				mejorVuelta_14.set(Calendar.MILLISECOND,484);
				
				//PISTA 14
				GregorianCalendar recordVuelta_14 = new GregorianCalendar();
			
				recordVuelta_14.set(Calendar.MINUTE, 1);
				recordVuelta_14.set(Calendar.SECOND, 21);
				recordVuelta_14.set(Calendar.MILLISECOND, 046);
				Record record_14 = new Record(recordVuelta_14.getTime(),"Rubens Barrichello",2004);
				String fotoRef_14 = null;
				try {
					fotoRef_14 = ManejadorImagenes.saveImageIntoMongoDB("fotos/ItalyGrandPrix.png", "Italy");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_14 != null) {
					Pista pista = manejadorPista.pista_create("Monza", fotoRef_14, "Lewis Hamilton", (float)306.72, (float)5.793, record_14);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_14.getTime(), 53, mejorVuelta_14.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
//GRAN PREMIO 15
				
				GregorianCalendar fechaGranPremio_15 = new GregorianCalendar(2018,8,16);
				
				GregorianCalendar mejorVuelta_15 = new GregorianCalendar();
				mejorVuelta_15.set(Calendar.HOUR,1);
				mejorVuelta_15.set(Calendar.MINUTE,51);
				mejorVuelta_15.set(Calendar.SECOND,11);
				mejorVuelta_15.set(Calendar.MILLISECOND,611);
				
				//PISTA 15
				GregorianCalendar recordVuelta_15 = new GregorianCalendar();
	
				recordVuelta_15.set(Calendar.MINUTE, 1);
				recordVuelta_15.set(Calendar.SECOND, 41);
				recordVuelta_15.set(Calendar.MILLISECOND, 905);
				Record record_15 = new Record(recordVuelta_15.getTime(),"Kevin Magnussen",2018);
				String fotoRef_15 = null;
				try {
					fotoRef_15 = ManejadorImagenes.saveImageIntoMongoDB("fotos/SingaporeGrandPrix.png", "Singapore");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_15 != null) {
					Pista pista = manejadorPista.pista_create("Ciudad de Singapur", fotoRef_15, "Lewis Hamilton", (float)308.706, (float)5.063, record_15);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_15.getTime(), 61, mejorVuelta_15.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
					//GRAN PREMIO 16
				
				GregorianCalendar fechaGranPremio_16 = new GregorianCalendar(2018,8,30);
				
				GregorianCalendar mejorVuelta_16 = new GregorianCalendar();
				mejorVuelta_16.set(Calendar.HOUR,1);
				mejorVuelta_16.set(Calendar.MINUTE,27);
				mejorVuelta_16.set(Calendar.SECOND,25);
				mejorVuelta_16.set(Calendar.MILLISECOND,181);
				
				//PISTA 16
				GregorianCalendar recordVuelta_16 = new GregorianCalendar();
	
				recordVuelta_16.set(Calendar.MINUTE, 1);
				recordVuelta_16.set(Calendar.SECOND, 35);
				recordVuelta_16.set(Calendar.MILLISECOND, 861);
				Record record_16 = new Record(recordVuelta_16.getTime(),"Valtteri Bottas",2018);
				String fotoRef_16 = null;
				try {
					fotoRef_16 = ManejadorImagenes.saveImageIntoMongoDB("fotos/RussiaGrandPrix.png", "Russia");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_16 != null) {
					Pista pista = manejadorPista.pista_create("San Petersburgo", fotoRef_16, "Lewis Hamilton", (float)309.745, (float)5.848, record_16);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_16.getTime(), 53, mejorVuelta_16.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}

				//GRAN PREMIO 17
				
				GregorianCalendar fechaGranPremio_17 = new GregorianCalendar(2018,9,07);
				
				GregorianCalendar mejorVuelta_17 = new GregorianCalendar();
				mejorVuelta_17.set(Calendar.HOUR,1);
				mejorVuelta_17.set(Calendar.MINUTE,27);
				mejorVuelta_17.set(Calendar.SECOND,17);
				mejorVuelta_17.set(Calendar.MILLISECOND,062);
				
				//PISTA 17
				GregorianCalendar recordVuelta_17 = new GregorianCalendar();
	
				recordVuelta_17.set(Calendar.MINUTE,1);
				recordVuelta_17.set(Calendar.SECOND, 31);
				recordVuelta_17.set(Calendar.MILLISECOND, 540);
				Record record_17 = new Record(recordVuelta_17.getTime(),"Kimi Räikkönen",2005);
				String fotoRef_17 = null;
				try {
					fotoRef_17 = ManejadorImagenes.saveImageIntoMongoDB("fotos/JapanGrandPrix.png", "Japan");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_17 != null) {
					Pista pista = manejadorPista.pista_create("Suzuka", fotoRef_17, "Lewis Hamilton", (float)307.471, (float)5.807, record_17);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_17.getTime(), 53, mejorVuelta_17.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				//GRAN PREMIO 18
				
				GregorianCalendar fechaGranPremio_18 = new GregorianCalendar(2018,9,21);
				
				GregorianCalendar mejorVuelta_18 = new GregorianCalendar();
				mejorVuelta_18.set(Calendar.HOUR,0);
				mejorVuelta_18.set(Calendar.MINUTE,0);
				mejorVuelta_18.set(Calendar.SECOND,0);
				mejorVuelta_18.set(Calendar.MILLISECOND,0);
				
				//PISTA 18
				GregorianCalendar recordVuelta_18 = new GregorianCalendar();
				recordVuelta_18.set(Calendar.MINUTE,1);
				recordVuelta_18.set(Calendar.SECOND, 37);
				recordVuelta_18.set(Calendar.MILLISECOND, 766);
				Record record_18 = new Record(recordVuelta_18.getTime(),"Sebastian Vettel",2017);
				String fotoRef_18 = null;
				try {
					fotoRef_18 = ManejadorImagenes.saveImageIntoMongoDB("fotos/UnitedStatesGrandPrix.png", "United States");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_18 != null) {
					Pista pista = manejadorPista.pista_create("Austin", fotoRef_18, "Lewis Hamilton", (float)308.405, (float)5.513, record_18);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_18.getTime(), 56, mejorVuelta_18.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		

//GRAN PREMIO 19
				
				GregorianCalendar fechaGranPremio_19 = new GregorianCalendar(2018,9,28);
				
				GregorianCalendar mejorVuelta_19 = new GregorianCalendar();
				mejorVuelta_19.set(Calendar.HOUR,0);
				mejorVuelta_19.set(Calendar.MINUTE,0);
				mejorVuelta_19.set(Calendar.SECOND,0);
				mejorVuelta_19.set(Calendar.MILLISECOND,0);
				
				//PISTA 19
				GregorianCalendar recordVuelta_19 = new GregorianCalendar();
				recordVuelta_19.set(Calendar.MINUTE,1);
				recordVuelta_19.set(Calendar.SECOND, 18);
				recordVuelta_19.set(Calendar.MILLISECOND, 785);
				Record record_19 = new Record(recordVuelta_19.getTime(),"Sebastian Vettel",2017);
				String fotoRef_19 = null;
				try {
					fotoRef_19 = ManejadorImagenes.saveImageIntoMongoDB("fotos/MexicoGrandPrix.png", "Mexico");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_19 != null) {
					Pista pista = manejadorPista.pista_create("Ciudad de México", fotoRef_19, "Max Verstappen", (float)305.354, (float)4.304, record_19);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_19.getTime(), 71, mejorVuelta_19.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
//GRAN PREMIO 20
				
				GregorianCalendar fechaGranPremio_20 = new GregorianCalendar(2018,10,11);
				
				GregorianCalendar mejorVuelta_20 = new GregorianCalendar();
				mejorVuelta_20.set(Calendar.HOUR,0);
				mejorVuelta_20.set(Calendar.MINUTE,0);
				mejorVuelta_20.set(Calendar.SECOND,0);
				mejorVuelta_20.set(Calendar.MILLISECOND,0);
				
				//PISTA 20
				GregorianCalendar recordVuelta_20 = new GregorianCalendar();
				recordVuelta_20.set(Calendar.MINUTE,1);
				recordVuelta_20.set(Calendar.SECOND, 11);
				recordVuelta_20.set(Calendar.MILLISECOND, 044);
				Record record_20 = new Record(recordVuelta_20.getTime(),"Max Verstappen",2017);
				String fotoRef_20 = null;
				try {
					fotoRef_20 = ManejadorImagenes.saveImageIntoMongoDB("fotos/BrasilGrandPrix.png", "Brasil");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_20 != null) {
					Pista pista = manejadorPista.pista_create("Sao Paulo", fotoRef_20, "Max Verstappen", (float)305.909, (float)4.309, record_20);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_20.getTime(), 71, mejorVuelta_20.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
//GRAN PREMIO 21
				
				GregorianCalendar fechaGranPremio_21 = new GregorianCalendar(2018,10,25);
				
				GregorianCalendar mejorVuelta_21 = new GregorianCalendar();
				mejorVuelta_21.set(Calendar.HOUR,0);
				mejorVuelta_21.set(Calendar.MINUTE,0);
				mejorVuelta_21.set(Calendar.SECOND,0);
				mejorVuelta_21.set(Calendar.MILLISECOND,0);
				
				//PISTA 21
				GregorianCalendar recordVuelta_21 = new GregorianCalendar();
				recordVuelta_21.set(Calendar.MINUTE,1);
				recordVuelta_21.set(Calendar.SECOND, 40);
				recordVuelta_21.set(Calendar.MILLISECOND, 279);
				Record record_21 = new Record(recordVuelta_21.getTime(),"Sebastian Vettel",2009);
				String fotoRef_21 = null;
				try {
					fotoRef_21 = ManejadorImagenes.saveImageIntoMongoDB("fotos/ArabGrandPrix.png", "UNITED ARAB EMIRATES");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_21 != null) {
					Pista pista = manejadorPista.pista_create("Abu Dabi", fotoRef_21, "Valtteri Bottas", (float)305.355, (float)5.554, record_21);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_21.getTime(), 55, mejorVuelta_21.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
				//PILOTO 1
				GregorianCalendar fechaNacimiento_1 = new GregorianCalendar(1985,0,07);
				String fotoRefP1 = null;
				try {
					fotoRefP1 = ManejadorImagenes.saveImageIntoMongoDB("fotos/LewisHamilton.jpg", "Lewis Hamilton");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Piloto piloto1= new Piloto("Lewis Hamilton",fechaNacimiento_1.getTime(),"Stevenage, England",fotoRefP1,131,2941,225);
	
				//PILOTO 2
				GregorianCalendar fechaNacimiento_2 = new GregorianCalendar(1987,06,03);
				String fotoRefP2 = null;
				try {
					fotoRefP2 = ManejadorImagenes.saveImageIntoMongoDB("fotos/sebastian.jpg", "Sebatian Vettel");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Piloto piloto2= new Piloto("Sebastian Vettel",fechaNacimiento_2.getTime(),"Heppenheim, Germany",fotoRefP2,109,2689,216);
	
				//PILOTO 3
				GregorianCalendar fechaNacimiento_3 = new GregorianCalendar(1989,07,28);
				String fotoRefP3 = null;
				try {
					fotoRefP3 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Valtteri Bottas.jpg", "Valtteri Bottas");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Piloto piloto3= new Piloto("Valtteri Bottas",fechaNacimiento_3.getTime(),"Nastola, Finland",fotoRefP3,30,923,115);
	
				//PILOTO 4
				GregorianCalendar fechaNacimiento_4 = new GregorianCalendar(1979,9,17);
				String fotoRefP4 = null;
				try {
					fotoRefP4 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Kimi Räikkönen.jpg", "Kimi Räikkönen");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Piloto piloto4= new Piloto("Kimi Räikkönen",fechaNacimiento_4.getTime(),"Espoo, Finland",fotoRefP4,100,1761,290);
	
				//PILOTO 5
				GregorianCalendar fechaNacimiento_5 = new GregorianCalendar(1997,8,30);
				String fotoRefP5 = null;
				try {
					fotoRefP5 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Max Verstappen.jpg", "Max Verstappen");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Piloto piloto5= new Piloto("Max Verstappen",fechaNacimiento_5.getTime(),"Hasselt, Belgium",fotoRefP5,18,594,77);
	
	
				//PILOTO 6
				GregorianCalendar fechaNacimiento_6 = new GregorianCalendar(1989,6,01);
				String fotoRefP6 = null;
				try {
					fotoRefP6 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Daniel Ricciardo.jpg", "Daniel Ricciardo");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Piloto piloto6= new Piloto("Daniel Ricciardo",fechaNacimiento_6.getTime(),"Perth, Australia",fotoRefP6,29,962,146);
	
				//PILOTO 7
				GregorianCalendar fechaNacimiento_7 = new GregorianCalendar(1990,0,26);
				String fotoRefP7 = null;
				try {
					fotoRefP7 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Sergio Perez.jpg", "Sergio Perez");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Piloto piloto7= new Piloto("Sergio Perez",fechaNacimiento_7.getTime(),"Guadalajara, Mexico",fotoRefP7,8,520,153);
	
	
				//PILOTO 8
				GregorianCalendar fechaNacimiento_8 = new GregorianCalendar(1992,9,5);
				String fotoRefP8 = null;
				try {
					fotoRefP8 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Kevin Magnussen.jpg", "Kevin Magnussen");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Piloto piloto8= new Piloto("Kevin Magnussen",fechaNacimiento_8.getTime(),"Roskilde, Denmark",fotoRefP8,1,134,78);
	
	
				//PILOTO 9
				GregorianCalendar fechaNacimiento_9 = new GregorianCalendar(1987,7,19);
				String fotoRefP9 = null;
				try {
					fotoRefP9 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Nico Hulkenberg.jpg", "Nico Hulkenberg");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Piloto piloto9= new Piloto("Nico Hulkenberg",fechaNacimiento_9.getTime(),"Emmerich am Rhein, Germany",fotoRefP9,0,458,154);
	
	
				//PILOTO 10
				GregorianCalendar fechaNacimiento_10 = new GregorianCalendar(1981,6,29);
				String fotoRefP10 = null;
				try {
					fotoRefP10 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Fernando Alonso.jpg", "Fernando Alonso");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Piloto piloto10= new Piloto("Fernando Alonso",fechaNacimiento_10.getTime(),"Oviedo, Spain",fotoRefP10,97,1899,310);
	
	




	
	
	}
	
	@WebMethod
	public List<ClasificacionCampeonato> verClasificacionesCampeonato() {
		return manejadorClasificacion_Campeonato.clasificacionCampeonato_getAll();
	}
	
	@WebMethod
	public List<GranPremio> carrerasOrdenadoPorFecha(){
		return manejadorGranPremio.grandesPremios_X_Fecha();
	}
	
	@WebMethod
	public Escuderia verEscuderia(String id){
		return manejadorEscuderia.escuderia_get(id);
	}
	
	@WebMethod
	public Piloto verPiloto(String id){
		return manejadorPiloto.piloto_get(id);
	}
	
	@WebMethod
	public Pista verPista(String id){
		return manejadorPista.pista_get(id);
	}
	
	@WebMethod
	public Auto verAuto(String id){
		return manejadorAuto.auto_get(id);
	}
	
}
