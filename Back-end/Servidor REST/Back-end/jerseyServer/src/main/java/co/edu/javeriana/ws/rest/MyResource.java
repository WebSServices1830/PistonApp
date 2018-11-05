package co.edu.javeriana.ws.rest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import DataManagers.CRUD_Piloto;
import clases_mongoDB.ManejadorImagenes;
import co.edu.javeriana.ws.rest.clases.Piloto;
import co.edu.javeriana.ws.rest.clases.SerieFibonacci;

@Path("/PistonApp")
public class MyResource {
	
	CRUD_Piloto manejadorPiloto = new CRUD_Piloto();

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
    
    //http://localhost:8080/myapp/PistonApp/fibonacci/7
    @GET
    @Produces({"application/xml", "application/json"})
    @Path("/fibonacci/{numero}")
    public List<SerieFibonacci> getString(@PathParam("numero") String numero) {
    	
    	List<SerieFibonacci> retorno = new ArrayList<SerieFibonacci>();
    	
    	SerieFibonacci sf1= new SerieFibonacci(1, 1);
        retorno.add(sf1);
        SerieFibonacci sf2= new SerieFibonacci(2, 1);
        retorno.add(sf2);

        for (int i = 2; i < Integer.parseInt(numero); i++) {
        	SerieFibonacci sf= new SerieFibonacci(i, retorno.get(i-1).getValor()+retorno.get(i-2).getValor());
            retorno.add(sf);
        }
    	
    	return retorno;
    }
    
    
    //PILOTOS---------------------------------------------------------------------
    //http://localhost:8080/myapp/PistonApp/pilotos
    @GET
    @Produces({"application/xml", "application/json"})
    @Path("/pilotos")
    public List<Piloto> consultarPilotos() {
    	return manejadorPiloto.piloto_getAll();
    }
    
    //http://localhost:8080/myapp/PistonApp/pilotos/5bd7288b7cb2bc1e3c10d43e
    @DELETE
    @Path("/pilotos/{idPiloto}")
    public ResponseBuilder eliminarPiloto(@PathParam("idPiloto") String idPiloto) {
	    try {
	    	manejadorPiloto.piloto_delete(idPiloto);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
	    return Response.status(200);
    }
    
    //http://localhost:8080/myapp/PistonApp/pilotos/5bd729797cb2bc1e3c10d49f?nuevoNombre=TheGrefg
    @PUT
    @Path("/pilotos/{idPiloto}")
    public ResponseBuilder cambiarNombrePiloto(@PathParam("idPiloto") String idPiloto, @QueryParam("nuevoNombre") String nuevoNombre) {
    	Piloto piloto= manejadorPiloto.piloto_get(idPiloto);
    	if(piloto != null){
    		piloto.setNombreCompleto(nuevoNombre);
    		manejadorPiloto.piloto_update(piloto);
    		return Response.status(200);
    	}
    	return Response.status(404);
    }
    
    //http://localhost:8080/myapp/PistonApp/pilotos?nombreCompleto=PostMalone&fechaNacimiento=04/07/1995&idEscuderia=1
    @POST
    @Path("/pilotos")
    public ResponseBuilder crearPiloto(@QueryParam("nombreCompleto") String nombreCompleto, @QueryParam("fechaNacimiento") String fechaNacimiento, @QueryParam("idEscuderia") String idEscuderia) {
    	Piloto piloto = new Piloto();
    	piloto.setNombreCompleto(nombreCompleto);
    	
    	Date fechaNacimiento_date;
		try {
			fechaNacimiento_date = new SimpleDateFormat("dd/MM/yyyy").parse(fechaNacimiento);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(500);
		}
    	piloto.setFecha_Nacimiento(fechaNacimiento_date);
    	
    	piloto.setId_escuderia(idEscuderia);
    	
    	manejadorPiloto.piloto_create(piloto);
    	
    	return Response.status(200);
    	
    }
    
    
    //PISTON APP------------------------------------------------------------------
    //http://localhost:8080/myapp/PistonApp
    @POST
    public ResponseBuilder crearPilotos(){
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
		manejadorPiloto.piloto_create(piloto1);
		
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
		manejadorPiloto.piloto_create(piloto2);
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
		manejadorPiloto.piloto_create(piloto3);
		//PILOTO 4
		GregorianCalendar fechaNacimiento_4 = new GregorianCalendar(1979,9,17);
		String fotoRefP4 = null;
		try {
			fotoRefP4 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Kimi Raikkonen.jpg", "Kimi Raikkonen");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto4= new Piloto("Kimi Rï¿½ikkï¿½nen",fechaNacimiento_4.getTime(),"Espoo, Finland",fotoRefP4,100,1761,290);
		manejadorPiloto.piloto_create(piloto4);
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
		manejadorPiloto.piloto_create(piloto5);

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
		manejadorPiloto.piloto_create(piloto6);
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

		manejadorPiloto.piloto_create(piloto7);
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
		manejadorPiloto.piloto_create(piloto8);
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
		manejadorPiloto.piloto_create(piloto9);

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
		manejadorPiloto.piloto_create(piloto10);
		
		//PILOTO 11
		GregorianCalendar fechaNacimiento_11 = new GregorianCalendar(1996,8,17);
		String fotoRefP11 = null;
		try {
			fotoRefP11 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Esteban Ocon.jpg", "Esteban Ocon");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto11= new Piloto("Esteban Ocon",fechaNacimiento_11.getTime(),"ï¿½vreux, Normandy",fotoRefP11,0,136,47);
		manejadorPiloto.piloto_create(piloto11);

		//PILOTO 12
		GregorianCalendar fechaNacimiento_12 = new GregorianCalendar(1994,8,1);
		String fotoRefP12 = null;
		try {
			fotoRefP12 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Carlos Sainz.jpg", "Carlos Sainz");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto12= new Piloto("Carlos Sainz",fechaNacimiento_12.getTime(),"Madrid, Spain",fotoRefP12,0,157,78);
		manejadorPiloto.piloto_create(piloto12);

		//PILOTO 13
		GregorianCalendar fechaNacimiento_13 = new GregorianCalendar(1986,3,17);
		String fotoRefP13 = null;
		try {
			fotoRefP13 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Romain Grosjean.jpg", "Romain Grosjean");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto13= new Piloto("Romain Grosjean",fechaNacimiento_13.getTime(),"Geneva, Switzerland",fotoRefP13,10,375,142);
		manejadorPiloto.piloto_create(piloto13);
		
		//PILOTO 14
		GregorianCalendar fechaNacimiento_14 = new GregorianCalendar(1996,1,7);
		String fotoRefP14 = null;
		try {
			fotoRefP14 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Pierre Gasly.jpg", "Pierre Gasly");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto14= new Piloto("Pierre Gasly",fechaNacimiento_14.getTime(),"Rouen, France",fotoRefP14,0,28,23);
		manejadorPiloto.piloto_create(piloto14);

		//PILOTO 15
		GregorianCalendar fechaNacimiento_15 = new GregorianCalendar(1997,9,16);
		String fotoRefP15 = null;
		try {
			fotoRefP15 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Charles Leclerc.jpg", "Charles Leclerc");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto15= new Piloto("Charles Leclerc",fechaNacimiento_15.getTime(),"Monte Carlo, Monaco",fotoRefP15,0,21,18);
		manejadorPiloto.piloto_create(piloto15);

		//PILOTO 16
		GregorianCalendar fechaNacimiento_16 = new GregorianCalendar(1992,2,26);
		String fotoRefP16 = null;
		try {
			fotoRefP16 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Stoffel Vandoorne.jpg", "Stoffel Vandoorne");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto16= new Piloto("Stoffel Vandoorne",fechaNacimiento_16.getTime(),"Kortrijk, Belgium",fotoRefP16,0,22,39);
		manejadorPiloto.piloto_create(piloto16);

		//PILOTO 17
		GregorianCalendar fechaNacimiento_17 = new GregorianCalendar(1998,9,29);
		String fotoRefP17 = null;
		try {
			fotoRefP17 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Lance Stroll.jpg", "Lance Stroll");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto17= new Piloto("Lance Stroll",fechaNacimiento_17.getTime(),"Montreal, Canada",fotoRefP17,1,46,38);
		manejadorPiloto.piloto_create(piloto17);

		//PILOTO 18
		GregorianCalendar fechaNacimiento_18 = new GregorianCalendar(1990,8,2);
		String fotoRefP18 = null;
		try {
			fotoRefP18 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Marcus Ericsson.jpg", "Marcus Ericsson");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto18= new Piloto("Marcus Ericsson",fechaNacimiento_18.getTime(),"Kumla, Sweden",fotoRefP18,0,15,94);
		manejadorPiloto.piloto_create(piloto18);

		//PILOTO 19
		GregorianCalendar fechaNacimiento_19 = new GregorianCalendar(1989,10,10);
		String fotoRefP19 = null;
		try {
			fotoRefP19 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Brendon Hartley.jpg", "Brendon Hartley");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto19= new Piloto("Brendon Hartley",fechaNacimiento_19.getTime(),"Palmerston North, New Zealand",fotoRefP19,0,2,22);
		manejadorPiloto.piloto_create(piloto19);

		//PILOTO 20
		GregorianCalendar fechaNacimiento_20 = new GregorianCalendar(1995,07,25);
		String fotoRefP20 = null;
		try {
			fotoRefP20 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Sergey Sirotkin.jpg", "Sergey Sirotkin");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto20= new Piloto("Sergey Sirotkin",fechaNacimiento_20.getTime(),"Moscow, Russia",fotoRefP20,0,1,18);
		manejadorPiloto.piloto_create(piloto20);
		
		return Response.status(200);

    }


}
