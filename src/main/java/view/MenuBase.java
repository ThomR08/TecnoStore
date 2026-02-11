package view;

import utils.InputReader;

public abstract class MenuBase {

    protected final InputReader input = InputReader.getInstance();

    public abstract void iniciar();
}
