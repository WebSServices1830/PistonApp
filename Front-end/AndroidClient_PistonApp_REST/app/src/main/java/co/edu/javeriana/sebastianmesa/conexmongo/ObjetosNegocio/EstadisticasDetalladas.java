package co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio;

public class EstadisticasDetalladas {

    String code;
    private int pos;
    private String nombre;
    private int puntaje;

    public EstadisticasDetalladas(String code, int pos, String nombre, int puntaje) {
        this.code = code;
        this.pos = pos;
        this.nombre = nombre;
        this.puntaje = puntaje;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
