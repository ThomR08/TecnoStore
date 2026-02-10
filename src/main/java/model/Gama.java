package model;

public enum Gama {

    BAJA("Baja"),
    MEDIA("Media"),
    ALTA("Alta");

    private final String texto;

    Gama(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto;
    }
}
