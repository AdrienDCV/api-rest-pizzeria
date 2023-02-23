package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Commande;
import dto.Pizza;

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

        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM commandes");

            while(rs.next()){
                commandesList.add(new Commande(rs.getInt("idcommande"), rs.getInt("idclient"), rs.getString("datecommande"), new ArrayList<>()));
            }

            // réucpérer la liste de pizzas
            for (Commande  commande : commandesList) {
                ResultSet rsPizzas = stmt.executeQuery("SELECT idpizza FROM commandepizza WHERE idcommande=" + commande.getIdCommande());
                List<Pizza> pizzasList = commande.getPizzas();
                while (rsPizzas.next()) {
                    PizzaDAO pizzaDAO = new PizzaDAO();
                    Pizza pizza = pizzaDAO.findById(rsPizzas.getInt("idpizza"));
                    pizzasList.add(pizza);          
                }   
                commande.setPizzas(pizzasList);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                con.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return commandesList;
    }

    public Commande findById(int id) {
        Commande commande = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

            PreparedStatement pstmtSelectCommande = con.prepareStatement("SELECT * FROM commandes WHERE idcommande=?");
            pstmtSelectCommande.setInt(1, id);
            ResultSet rs = pstmtSelectCommande.executeQuery();

            if (rs.next()){
                commande = new Commande(rs.getInt("idcommande"), rs.getInt("idclient"), rs.getString("datecommande"), new ArrayList<>());
            }

            PreparedStatement pstmtSelectPizza = con.prepareStatement("SELECT idpizza FROM commandepizza WHERE idcommande=?");
            pstmtSelectPizza.setInt(1, id);
            ResultSet rsPizzas = pstmtSelectPizza.executeQuery();
            List<Pizza> pizzasList = commande.getPizzas();
            while (rsPizzas.next()) {
                PizzaDAO pizzaDAO = new PizzaDAO();
                Pizza pizza = pizzaDAO.findById(rsPizzas.getInt("idpizza"));
                pizzasList.add(pizza);          
            }   
            commande.setPizzas(pizzasList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                con.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }


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

    public double getFinalPrice(Commande commande) {
        double finalPrice = 0;

        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

            PizzaDAO pizzaDAO = new PizzaDAO();

            for (Pizza pizza : commande.getPizzas()) {
                finalPrice += pizzaDAO.getFinalPrice(pizza);               
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return finalPrice;
    }


}
