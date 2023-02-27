package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.Commande;
import dto.Ingredient;
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

            PizzaDAO pizzaDAO = new PizzaDAO();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM commandes");

            while(rs.next()){
                commandesList.add(new Commande(rs.getInt("idcommande"), rs.getInt("idclient"), rs.getDate("datecommande"), new ArrayList<>()));
            }

            // réucpérer la liste de pizzas
            for (Commande  commande : commandesList) {
                ResultSet rsPizzas = stmt.executeQuery("SELECT idpizza FROM commandepizza WHERE idcommande=" + commande.getIdCommande());
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
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

            PizzaDAO pizzaDAO = new PizzaDAO();
            Statement stmtSelectCommande = con.createStatement();
            ResultSet rsCommande = stmtSelectCommande.executeQuery("SELECT * FROM commandes WHERE idcommande="+id);

            if (rsCommande.next()){
                commande = new Commande(rsCommande.getInt("idcommande"), rsCommande.getInt("idclient"), rsCommande.getDate("datecommande"), new ArrayList<>());
            
                PreparedStatement pstmtSelectPizza = con.prepareStatement("SELECT idpizza FROM commandepizza WHERE idcommande=?");
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
                Class.forName("org.postgresql.Driver");
                this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");
                PizzaDAO pizzaDAO = new PizzaDAO();

                PreparedStatement pstmtInsertCommande = con.prepareStatement("INSERT INTO commandes VALUES(?,?,?)");
                pstmtInsertCommande.setInt(1, commande.getIdCommande());
                pstmtInsertCommande.setInt(2, commande.getIdClient());
                pstmtInsertCommande.setDate(3, commande.getDate());
                pstmtInsertCommande.executeUpdate();

                PreparedStatement pstmtInsertPizzas = con.prepareStatement("INSERT INTO commandepizza VALUES(?,?)");
                List<Pizza> pizzasList =  commande.getPizzasList();
                pstmtInsertPizzas.setInt(1, commande.getIdCommande());
                for (Pizza pizza : pizzasList) {
                    pstmtInsertPizzas.setInt(2,pizza.getId());
                    for (Integer idIngredient : pizza.getIdsIngredientsList()) {
                        pizzaDAO.addIngredient(pizza.getId(), idIngredient);
                    }
                    pstmtInsertPizzas.addBatch();
                }
                int[] inserts = pstmtInsertPizzas.executeBatch();
                if (inserts != null) {
                    saved = true;
                }         

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
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

            Statement stmtPrice = con.createStatement(); 
            for (Pizza pizza : commande.getPizzasList()) {  
                ResultSet rsPizzaPrice = stmtPrice.executeQuery("SELECT prixBase FROM basepizza WHERE id=" + pizza.getId());
                if (rsPizzaPrice.next()) {
                    finalPrice += rsPizzaPrice.getFloat("prixBase");
                    ResultSet rsPizzaIngredientsPrice = stmtPrice.executeQuery("SELECT SUM(price) AS price FROM pizza AS p JOIN ingredients AS i ON (p.idingredient = i.id) WHERE p.idbasepizza =" + pizza.getId());
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
