package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.Pizza;
import dto.TypePate;

public class PizzaDAO {
    
    // attributes 
    private Connection con;

    public PizzaDAO() {
        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // methods
    public List<Pizza> findAll() {
        List<Pizza> pizzasList = new ArrayList<>();

        
        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

            Statement stmt = con.createStatement();
            ResultSet rsBasePizza = stmt.executeQuery("SELECT * FROM basepizza");

            while(rsBasePizza.next()){
                pizzasList.add(new Pizza(rsBasePizza.getInt("id"), rsBasePizza.getString("name"), TypePate.get(rsBasePizza.getString("typePate")), rsBasePizza.getFloat("prixBase"), null));
            }

            for (Pizza pizza : pizzasList) {
                ResultSet rsIngredients = stmt.executeQuery("SELECT idingredient FROM pizza WHERE id=" + pizza.getId());
                if (rsIngredients.next()) {
                    pizza.getIngredients().add(rsIngredients.getInt("idingredient"));
                }   
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

        return pizzasList;

    } 

    public Pizza findById(String id) {
        Pizza pizza = null;

        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

            PreparedStatement pstmtSelect = con.prepareStatement("SELECT * FROM pizza WHERE idbasepizza =?");
            pstmtSelect.setInt(1, Integer.parseInt(id));
            ResultSet rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                pizza = new Pizza();
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
       
        return pizza;
    }

    public boolean delete(String id) {
        boolean deleted = false;
        System.out.println(id);

        try {
            if (this.findById(id) != null) {
                System.out.println("passe par là");
                Class.forName("org.postgresql.Driver");
                this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

                PreparedStatement pstmtDelete = con.prepareStatement("DELETE FROM pizza WHERE id=?");
                pstmtDelete.setInt(1, Integer.parseInt(id));
                pstmtDelete.executeUpdate();
                deleted = true;
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

        return deleted;
    }

    public boolean save(Pizza pizza) {
        boolean saved = false;

        try {
            if (this.findById(Integer.toString(pizza.getId())) == null) {
                Class.forName("org.postgresql.Driver");
                this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

                // ajoute basepizza
                PreparedStatement pstmtInsertPizza = con.prepareStatement("INSERT INTO basepizza VALUES(?,?,?,?)");
                pstmtInsertPizza.setInt(1, pizza.getId());
                pstmtInsertPizza.setString(2, pizza.getName());
                pstmtInsertPizza.setString(3, pizza.getTypePate().getLabel());
                pstmtInsertPizza.setDouble(4, pizza.getPrixBase());
                pstmtInsertPizza.executeUpdate();

                // ajout type pâte + ingrédients 
                PreparedStatement pstmtInsertIngredients = con.prepareStatement("INSERT INTO pizza VALUES(?,?)");
                List<Integer> ingredientsIDsTab =  pizza.getIngredients();
                pstmtInsertIngredients.setInt(1, pizza.getId());
                for (int i = 0; i < ingredientsIDsTab.size(); i++) {
                    pstmtInsertIngredients.setInt(2,ingredientsIDsTab.get(i));
                    pstmtInsertIngredients.addBatch();
                }
                int[] inserts = pstmtInsertIngredients.executeBatch();
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
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return saved;
    }

}
