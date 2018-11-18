package co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio;

public class EstadisticasGeneral implements Comparable<EstadisticasGeneral> {

    private String idComun;
    private int pos;
    private int pun;
    private String name;

    public EstadisticasGeneral(String idComun, int pos, int pun, String name) {
        this.idComun = idComun;
        this.pos = pos;
        this.pun = pun;
        this.name = name;
    }

    public String getIdComun() {
        return idComun;
    }

    public void setIdComun(String idComun) {
        this.idComun = idComun;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPun() {
        return pun;
    }

    public void setPun(int pun) {
        this.pun = pun;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(EstadisticasGeneral another) {
        return getPos() - another.getPos();
    }
}
