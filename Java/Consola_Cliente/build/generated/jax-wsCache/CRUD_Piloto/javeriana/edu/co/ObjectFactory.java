
package javeriana.edu.co;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the javeriana.edu.co package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ReadPilotoResponse_QNAME = new QName("http://co.edu.javeriana/", "read_pilotoResponse");
    private final static QName _ReadListaResponse_QNAME = new QName("http://co.edu.javeriana/", "read_listaResponse");
    private final static QName _Erase_QNAME = new QName("http://co.edu.javeriana/", "erase");
    private final static QName _EraseResponse_QNAME = new QName("http://co.edu.javeriana/", "eraseResponse");
    private final static QName _ReadPiloto_QNAME = new QName("http://co.edu.javeriana/", "read_piloto");
    private final static QName _UpdateResponse_QNAME = new QName("http://co.edu.javeriana/", "updateResponse");
    private final static QName _ReadLista_QNAME = new QName("http://co.edu.javeriana/", "read_lista");
    private final static QName _Create_QNAME = new QName("http://co.edu.javeriana/", "create");
    private final static QName _CreateResponse_QNAME = new QName("http://co.edu.javeriana/", "createResponse");
    private final static QName _Update_QNAME = new QName("http://co.edu.javeriana/", "update");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: javeriana.edu.co
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReadListaResponse }
     * 
     */
    public ReadListaResponse createReadListaResponse() {
        return new ReadListaResponse();
    }

    /**
     * Create an instance of {@link Erase }
     * 
     */
    public Erase createErase() {
        return new Erase();
    }

    /**
     * Create an instance of {@link EraseResponse }
     * 
     */
    public EraseResponse createEraseResponse() {
        return new EraseResponse();
    }

    /**
     * Create an instance of {@link ReadPilotoResponse }
     * 
     */
    public ReadPilotoResponse createReadPilotoResponse() {
        return new ReadPilotoResponse();
    }

    /**
     * Create an instance of {@link Create }
     * 
     */
    public Create createCreate() {
        return new Create();
    }

    /**
     * Create an instance of {@link CreateResponse }
     * 
     */
    public CreateResponse createCreateResponse() {
        return new CreateResponse();
    }

    /**
     * Create an instance of {@link Update }
     * 
     */
    public Update createUpdate() {
        return new Update();
    }

    /**
     * Create an instance of {@link ReadPiloto }
     * 
     */
    public ReadPiloto createReadPiloto() {
        return new ReadPiloto();
    }

    /**
     * Create an instance of {@link UpdateResponse }
     * 
     */
    public UpdateResponse createUpdateResponse() {
        return new UpdateResponse();
    }

    /**
     * Create an instance of {@link ReadLista }
     * 
     */
    public ReadLista createReadLista() {
        return new ReadLista();
    }

    /**
     * Create an instance of {@link Piloto }
     * 
     */
    public Piloto createPiloto() {
        return new Piloto();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadPilotoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.edu.javeriana/", name = "read_pilotoResponse")
    public JAXBElement<ReadPilotoResponse> createReadPilotoResponse(ReadPilotoResponse value) {
        return new JAXBElement<ReadPilotoResponse>(_ReadPilotoResponse_QNAME, ReadPilotoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadListaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.edu.javeriana/", name = "read_listaResponse")
    public JAXBElement<ReadListaResponse> createReadListaResponse(ReadListaResponse value) {
        return new JAXBElement<ReadListaResponse>(_ReadListaResponse_QNAME, ReadListaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Erase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.edu.javeriana/", name = "erase")
    public JAXBElement<Erase> createErase(Erase value) {
        return new JAXBElement<Erase>(_Erase_QNAME, Erase.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EraseResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.edu.javeriana/", name = "eraseResponse")
    public JAXBElement<EraseResponse> createEraseResponse(EraseResponse value) {
        return new JAXBElement<EraseResponse>(_EraseResponse_QNAME, EraseResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadPiloto }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.edu.javeriana/", name = "read_piloto")
    public JAXBElement<ReadPiloto> createReadPiloto(ReadPiloto value) {
        return new JAXBElement<ReadPiloto>(_ReadPiloto_QNAME, ReadPiloto.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.edu.javeriana/", name = "updateResponse")
    public JAXBElement<UpdateResponse> createUpdateResponse(UpdateResponse value) {
        return new JAXBElement<UpdateResponse>(_UpdateResponse_QNAME, UpdateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadLista }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.edu.javeriana/", name = "read_lista")
    public JAXBElement<ReadLista> createReadLista(ReadLista value) {
        return new JAXBElement<ReadLista>(_ReadLista_QNAME, ReadLista.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Create }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.edu.javeriana/", name = "create")
    public JAXBElement<Create> createCreate(Create value) {
        return new JAXBElement<Create>(_Create_QNAME, Create.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.edu.javeriana/", name = "createResponse")
    public JAXBElement<CreateResponse> createCreateResponse(CreateResponse value) {
        return new JAXBElement<CreateResponse>(_CreateResponse_QNAME, CreateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Update }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.edu.javeriana/", name = "update")
    public JAXBElement<Update> createUpdate(Update value) {
        return new JAXBElement<Update>(_Update_QNAME, Update.class, null, value);
    }

}
