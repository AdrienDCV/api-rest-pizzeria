package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import dto.Commande;

public class CommandeDAO {
    
    // attributes
    private Connection con;

    // constructor(s)
    public CommandeDAO() {
        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // methods

    public List<Commande> findAll() {
        List<Commande> commandesList = new ArrayList<>();
        // TO DO
        return commandesList;
    }

    public Commande findById(int id) {
        Commande commande = null;
        // TO DO
        return commande;
    }

    public boolean delete(int id) {
        boolean deleted = false;
        // TO DO
        return deleted;
    }

    public boolean save(Commande commande) {
        boolean saved = false; 
        // TO DO
        return saved;
    }


}
