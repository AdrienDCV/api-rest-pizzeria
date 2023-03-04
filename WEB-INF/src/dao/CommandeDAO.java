package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.Commande;
import dto.Ingredient;
import dto.Pizza;

public class CommandeDAO {
    
    // attributes
    private DS ds;
    private Connection con;

    // constructor(s)
    public CommandeDAO() {
        this.ds = new DS();
    }

    // methods

    public List<Commande> findAll() {
        List<Commande> commandesList = new ArrayList<>();

        try {
            this.con = ds.getConnection();

            PizzaDAO pizzaDAO = new PizzaDAO();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM commandes");

            while(rs.next()){
                commandesList.add(new Commande(rs.getInt("idcommande"), rs.getInt("idclient"), rs.getDate("datecommande"), new ArrayList<>()));
            }

            // réucpérer la liste de pizzas
            for (Commande  commande : commandesList) {
                ResultSet rsPizzas = stmt.executeQuery("SELECT idpizza FROM commandespizzas WHERE idcommande=" + commande.getIdCommande());
                List<Pizza> pizzasList = commande.getPizzasList();
                while (rsPizzas.next()) {
                    pizzasList.add(pizzaDAO.findById(rsPizzas.getInt("idpizza")));          
                }   
                commande.setPizzasList(pizzasList);
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
            this.con = ds.getConnection();

            PizzaDAO pizzaDAO = new PizzaDAO();
            Statement stmtSelectCommande = con.createStatement();
            ResultSet rsCommande = stmtSelectCommande.executeQuery("SELECT * FROM commandes WHERE idcommande="+id);

            if (rsCommande.next()){
                commande = new Commande(rsCommande.getInt("idcommande"), rsCommande.getInt("idclient"), rsCommande.getDate("datecommande"), new ArrayList<>());
            
                PreparedStatement pstmtSelectPizza = con.prepareStatement("SELECT idpizza FROM commandespizzas WHERE idcommande=?");
                pstmtSelectPizza.setInt(1, id);
                ResultSet rsPizzas = pstmtSelectPizza.executeQuery();
                List<Pizza> pizzasList = commande.getPizzasList();
                while (rsPizzas.next()) {
                    pizzasList.add(pizzaDAO.findById(rsPizzas.getInt("idpizza")));          
                }   
                commande.setPizzasList(pizzasList);   
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

        return commande;
    }

    public boolean save(Commande commande) {
        boolean saved = false; 
        
        try {
            if (this.findById(commande.getIdCommande()) == null) {
                this.con = ds.getConnection();
                PizzaDAO pizzaDAO = new PizzaDAO();

                PreparedStatement pstmtInsertCommande = con.prepareStatement("INSERT INTO commandes VALUES(?,?,?)");
                pstmtInsertCommande.setInt(1, commande.getIdCommande());
                pstmtInsertCommande.setInt(2, commande.getIdClient());
                pstmtInsertCommande.setDate(3, commande.getDate());
                pstmtInsertCommande.executeUpdate();

                List<Pizza> pizzasList =  commande.getPizzasList();
                for (Pizza pizza : pizzasList) {
                    for (Ingredient ingredient : pizza.getIngredientsList()) {
                        pizzaDAO.addIngredient(pizza.getId(), ingredient);
                    }
                }    
                
                saved = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return saved;
    }

    public double getFinalPrice(Commande commande) {
        double finalPrice = 0;

        try {
            this.con = ds.getConnection();

            Statement stmtPrice = con.createStatement(); 
            for (Pizza pizza : commande.getPizzasList()) {  
                ResultSet rsPizzaPrice = stmtPrice.executeQuery("SELECT prixBase FROM basespizzas WHERE id=" + pizza.getId());
                if (rsPizzaPrice.next()) {
                    finalPrice += rsPizzaPrice.getFloat("prixBase");
                    ResultSet rsPizzaIngredientsPrice = stmtPrice.executeQuery("SELECT SUM(price) AS price FROM pizzas AS p JOIN ingredients AS i ON (p.idingredient = i.id) WHERE p.idbasepizza =" + pizza.getId());
                    if (rsPizzaIngredientsPrice.next()) {
                        finalPrice += rsPizzaIngredientsPrice.getFloat("price");
                    } 
                }
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
