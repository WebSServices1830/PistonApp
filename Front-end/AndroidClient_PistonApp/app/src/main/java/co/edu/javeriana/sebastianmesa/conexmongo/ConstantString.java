package co.edu.javeriana.sebastianmesa.conexmongo;

public class ConstantString {

    public final static String SOAP_ACTION = "http://localhost:8080/WS/piloto/";
    public final static String NAME_SPACE = "http://localhost:8080/WS/piloto/";
    public final static String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";

    public final static String NAMESPACE2 = "http://webservice.javeriana.co/";
    public final static String URL2 ="http://localhost:8080/WS/piloto.asmx";
    public final static String METHOD_NAME2 = "mostrarMensaje";
    public final static String SOAP_ACTION2 = "http://webservice.javeriana.co/piloto/mostrarMensajeRequest";

    public final static String F_TO_C_METHOD_NAME = "FahrenheitToCelsius";
    public final static String C_TO_F_METHOD_NAME = "CelsiusToFahrenheit";

    public final static String F_TO_C_SOAP_ACTION = SOAP_ACTION + F_TO_C_METHOD_NAME;
    public final static String C_TO_F_SOAP_ACTION = SOAP_ACTION + C_TO_F_METHOD_NAME;

}