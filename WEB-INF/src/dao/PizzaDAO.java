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
            ResultSet rs = stmt.executeQuery("SELECT * FROM pizza");

            while(rs.next()){
                pizzasList.add(new Pizza(rs.getInt("id"), rs.getString("name"), TypePate.get(rs.getString("typePate")), rs.getFloat("prixBase"), null));
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
                PreparedStatement pstmtInsertPizza = con.prepareStatement("INSERT INTO basepizza VALUES(?,?,?)");
                pstmtInsertPizza.setInt(1, pizza.getId());
                pstmtInsertPizza.setString(2, pizza.getName());
                pstmtInsertPizza.setDouble(3, pizza.getPrixBase());
                pstmtInsertPizza.executeUpdate();

                // ajout type pâte + ingrédients 
                PreparedStatement pstmtInsertIngredients = con.prepareStatement("INSERT INTO pizza VALUES(?,?,?)");
                int[] ingredientsIDsTab =  pizza.getIngredients();
                pstmtInsertIngredients.setInt(1, pizza.getId());
                pstmtInsertIngredients.setString(3, pizza.getTypePate().getLabel());
                for (int i = 0; i < ingredientsIDsTab.length; i++) {
                    pstmtInsertIngredients.setInt(2,ingredientsIDsTab[i]);
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
