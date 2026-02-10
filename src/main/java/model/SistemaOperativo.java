package model;

public enum SistemaOperativo {

    ANDROID("Android"),
    IOS("iOS"),
    HARMONY_OS("HarmonyOS");

    private final String text;

    SistemaOperativo(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}