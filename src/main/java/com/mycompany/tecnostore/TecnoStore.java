package com.mycompany.tecnostore;

import dao.DBConnection;
import utils.InputReader;
import view.MenuPrincipal;
import java.sql.Connection;

public class TecnoStore {

    public static void main(String[] args) {

        try {
            
            Connection con = DBConnection.getConnection();
            InputReader input = InputReader.getInstance();
            
            MenuPrincipal menu = new MenuPrincipal();
            menu.iniciar();

        } catch (Exception e) {

            System.out.println("    Error general: " + e.getMessage());

        } finally {

            DBConnection.closeConnection();

        }
    }
}
