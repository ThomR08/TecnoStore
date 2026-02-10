package model;

public enum TipoDocumento {

    CC("Cédula de ciudadanía"),
    CE("Cédula de extranjería"),
    TI("Tarjeta de identidad"),
    PASAPORTE("Pasaporte");

    private final String texto;

    TipoDocumento(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto;
    }
}
