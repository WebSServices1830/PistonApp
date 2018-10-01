
package javeriana.edu.co;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebService(name = "CRUD_Piloto", targetNamespace = "http://co.edu.javeriana/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface CRUDPiloto {


    /**
     * 
     * @param foto
     * @param fechaNacimiento
     * @param id
     * @param nombre
     */
    @WebMethod
    @RequestWrapper(localName = "update", targetNamespace = "http://co.edu.javeriana/", className = "javeriana.edu.co.Update")
    @ResponseWrapper(localName = "updateResponse", targetNamespace = "http://co.edu.javeriana/", className = "javeriana.edu.co.UpdateResponse")
    @Action(input = "http://co.edu.javeriana/CRUD_Piloto/updateRequest", output = "http://co.edu.javeriana/CRUD_Piloto/updateResponse")
    public void update(
        @WebParam(name = "nombre", targetNamespace = "")
        String nombre,
        @WebParam(name = "fecha_Nacimiento", targetNamespace = "")
        XMLGregorianCalendar fechaNacimiento,
        @WebParam(name = "foto", targetNamespace = "")
        String foto,
        @WebParam(name = "id", targetNamespace = "")
        int id);

    /**
     * 
     * @param foto
     * @param fechaNacimiento
     * @param nombre
     */
    @WebMethod
    @RequestWrapper(localName = "create", targetNamespace = "http://co.edu.javeriana/", className = "javeriana.edu.co.Create")
    @ResponseWrapper(localName = "createResponse", targetNamespace = "http://co.edu.javeriana/", className = "javeriana.edu.co.CreateResponse")
    @Action(input = "http://co.edu.javeriana/CRUD_Piloto/createRequest", output = "http://co.edu.javeriana/CRUD_Piloto/createResponse")
    public void create(
        @WebParam(name = "nombre", targetNamespace = "")
        String nombre,
        @WebParam(name = "fecha_Nacimiento", targetNamespace = "")
        XMLGregorianCalendar fechaNacimiento,
        @WebParam(name = "foto", targetNamespace = "")
        String foto);

    /**
     * 
     * @param id
     */
    @WebMethod
    @RequestWrapper(localName = "erase", targetNamespace = "http://co.edu.javeriana/", className = "javeriana.edu.co.Erase")
    @ResponseWrapper(localName = "eraseResponse", targetNamespace = "http://co.edu.javeriana/", className = "javeriana.edu.co.EraseResponse")
    @Action(input = "http://co.edu.javeriana/CRUD_Piloto/eraseRequest", output = "http://co.edu.javeriana/CRUD_Piloto/eraseResponse")
    public void erase(
        @WebParam(name = "id", targetNamespace = "")
        int id);

    /**
     * 
     * @param id
     * @return
     *     returns javeriana.edu.co.Piloto
     */
    @WebMethod(operationName = "read_piloto")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "read_piloto", targetNamespace = "http://co.edu.javeriana/", className = "javeriana.edu.co.ReadPiloto")
    @ResponseWrapper(localName = "read_pilotoResponse", targetNamespace = "http://co.edu.javeriana/", className = "javeriana.edu.co.ReadPilotoResponse")
    @Action(input = "http://co.edu.javeriana/CRUD_Piloto/read_pilotoRequest", output = "http://co.edu.javeriana/CRUD_Piloto/read_pilotoResponse")
    public Piloto readPiloto(
        @WebParam(name = "id", targetNamespace = "")
        int id);

    /**
     * 
     * @return
     *     returns java.util.List<javeriana.edu.co.Piloto>
     */
    @WebMethod(operationName = "read_lista")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "read_lista", targetNamespace = "http://co.edu.javeriana/", className = "javeriana.edu.co.ReadLista")
    @ResponseWrapper(localName = "read_listaResponse", targetNamespace = "http://co.edu.javeriana/", className = "javeriana.edu.co.ReadListaResponse")
    @Action(input = "http://co.edu.javeriana/CRUD_Piloto/read_listaRequest", output = "http://co.edu.javeriana/CRUD_Piloto/read_listaResponse")
    public List<Piloto> readLista();

}
