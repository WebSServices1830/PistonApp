/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javeriana.edu.co;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javariana.edu.co.Piloto;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Alvaro
 */
@WebService(serviceName = "CRUD_Piloto")
public class CRUD_Piloto {
    
    private List<Piloto> listaPilotos= new ArrayList<Piloto>();

    
    @WebMethod(operationName = "create")
    public void create(@WebParam(name = "nombre") String nombre, @WebParam(name = "fecha_Nacimiento") Date fecha_Nacimiento, @WebParam(name = "foto") String foto) {
        Piloto piloto= new Piloto(nombre, fecha_Nacimiento, foto);
        listaPilotos.add(piloto);
    }
    
    @WebMethod(operationName = "read_lista")
    public List<Piloto> read_lista() {
        return listaPilotos;
    }
    
    @WebMethod(operationName = "read_piloto")
    public Piloto read_piloto(@WebParam(name = "id") int ID) {
        for (Piloto listaPiloto : listaPilotos) {
            if(listaPiloto.getID() == ID)
                return listaPiloto;
        }
        return null;
    }
    
    @WebMethod(operationName = "update")
    public void update(@WebParam(name = "nombre") String nombre, @WebParam(name = "fecha_Nacimiento") Date fecha_Nacimiento, @WebParam(name = "foto") String foto, @WebParam(name = "id") int ID) {
        Piloto piloto= new Piloto(nombre, fecha_Nacimiento, foto, ID);
        erase(ID);
        listaPilotos.add(piloto);
    }
    
    @WebMethod(operationName = "erase")
    public void erase(@WebParam(name = "id") int ID) {
        int cont= 0;
        for (Piloto listaPiloto : listaPilotos) {
            if(listaPiloto.getID() == ID)
                break;
            ++cont;
        }
        listaPilotos.remove(cont);
    }
    
}
