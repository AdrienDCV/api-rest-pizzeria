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
                pizzasList.add(new Pizza(rsBasePizza.getInt("id"), rsBasePizza.getString("name"), TypePate.get(rsBasePizza.getString("typePate")), rsBasePizza.getFloat("prixBase"), new ArrayList<>()));
            }

            for (Pizza pizza : pizzasList) {
                ResultSet rsIngredients = stmt.executeQuery("SELECT idingredient FROM pizza WHERE idbasepizza=" + pizza.getId());
                List<Integer> ingredientsList = pizza.getIngredients();
                while (rsIngredients.next()) {
                        ingredientsList.add(rsIngredients.getInt("idingredient"));          
                }   
                pizza.setIngredients(ingredientsList);
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

    public Pizza findById(int id) {
        Pizza pizza = null;

        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

            PreparedStatement pstmtSelectBasePizza = con.prepareStatement("SELECT * FROM basepizza WHERE id=?");
            pstmtSelectBasePizza.setInt(1, id);
            ResultSet rsBasePizza = pstmtSelectBasePizza.executeQuery();

            if(rsBasePizza.next()){
                pizza = new Pizza(rsBasePizza.getInt("id"), rsBasePizza.getString("name"), TypePate.get(rsBasePizza.getString("typePate")), rsBasePizza.getFloat("prixBase"), new ArrayList<>());
            }

            Statement stmt = con.createStatement();
            ResultSet rsIngredients = stmt.executeQuery("SELECT idingredient FROM pizza WHERE idbasepizza=" + pizza.getId());
            List<Integer> ingredientsList = pizza.getIngredients();
            while (rsIngredients.next()) {
                System.out.println("passe par là");
                ingredientsList.add(rsIngredients.getInt("idingredient"));          
            }   
            pizza.setIngredients(ingredientsList);
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

    public boolean delete(int id) {
        boolean deleted = false;
        System.out.println(id);

        try {
            if (this.findById(id) != null) {
                System.out.println("passe par là");
                Class.forName("org.postgresql.Driver");
                this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

                PreparedStatement pstmtDelete = con.prepareStatement("DELETE FROM pizza WHERE id=?");
                pstmtDelete.setInt(1, id);
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
            if (this.findById(pizza.getId()) == null) {
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

    public double getFinalPrice(Pizza pizza) {
        double finalPrice = 0;


        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

            PreparedStatement pstmtSelectPriceBasePizza = con.prepareStatement("SELECT prixbase FROM basepizza WHERE id=?");
            pstmtSelectPriceBasePizza.setInt(1, pizza.getId());
            ResultSet rsPriceBasePizza = pstmtSelectPriceBasePizza.executeQuery();

            if(rsPriceBasePizza.next()){
              finalPrice = rsPriceBasePizza.getFloat("prixbase");
            }

            Statement stmtPriceIngredients = con.createStatement();
            for (Integer idingredients : pizza.getIngredients()) {
                ResultSet rsPriceIngredients = stmtPriceIngredients.executeQuery("SELECT price FROM ingredients WHERE id=" + idingredients);
                if (rsPriceIngredients.next()) {
                    finalPrice += rsPriceIngredients.getFloat("price");
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