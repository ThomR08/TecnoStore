package model;

public class Cliente {
    private long id;
    private String nombre;
    private String documento;
    private TipoDocumento tipoDocumento;
    private String correo;
    private String telefono;

    public Cliente() {
    }

    public Cliente(long id, String nombre, String documento, TipoDocumento tipoDocumento, String correo, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.tipoDocumento = tipoDocumento;
        this.correo = correo;
        this.telefono = telefono;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", documento='" + documento + '\'' +
                ", tipoDocumento=" + tipoDocumento +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
