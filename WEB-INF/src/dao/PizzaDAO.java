package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.Ingredient;
import dto.Pizza;
import dto.TypePate;

public class PizzaDAO {
    
    // attributes 
    private DS ds;
    private Connection con;

    public PizzaDAO() {
        this.ds = new DS();
    }
    

    // methods
    public List<Pizza> findAll() {
        List<Pizza> pizzasList = new ArrayList<>();

        
        try {
            this.con = ds.getConnection();

            Statement stmtPizzas = con.createStatement();
            ResultSet rsBasePizza = stmtPizzas.executeQuery("SELECT * FROM basespizzas");

            while(rsBasePizza.next()){
                pizzasList.add(new Pizza(rsBasePizza.getInt("idbasepizza"), rsBasePizza.getString("name"), TypePate.get(rsBasePizza.getString("typePate")), rsBasePizza.getFloat("prixBase"), new ArrayList<>(), new ArrayList<>()));
            }

            PreparedStatement pstmtIdsIngredients = con.prepareStatement("SELECT idingredient FROM pizzas WHERE idbasepizza=?");
            PreparedStatement pstmtIngredients = con.prepareStatement("SELECT * FROM ingredients WHERE idingredient=?");
            for (Pizza pizza : pizzasList) {
                pstmtIdsIngredients.setInt(1, pizza.getId());
                retrieveIngredients(pizza, pstmtIdsIngredients, pstmtIngredients);
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
            this.con = ds.getConnection();

            PreparedStatement pstmtSelectBasePizza = con.prepareStatement("SELECT * FROM basespizzas WHERE idbasepizza=?");
            pstmtSelectBasePizza.setInt(1, id);
            ResultSet rsBasePizza = pstmtSelectBasePizza.executeQuery();

            if(rsBasePizza.next()){
                pizza = new Pizza(rsBasePizza.getInt("idbasepizza"), rsBasePizza.getString("name"), TypePate.get(rsBasePizza.getString("typePate")), rsBasePizza.getFloat("prixBase"), new ArrayList<>(), new ArrayList<>());
            
                PreparedStatement pstmtIdsIngredients = con.prepareStatement("SELECT idingredient FROM pizzas WHERE idbasepizza=?");
                PreparedStatement pstmtIngredients = con.prepareStatement("SELECT * FROM ingredients WHERE idingredient=?");
                pstmtIdsIngredients.setInt(1, id);
                retrieveIngredients(pizza, pstmtIdsIngredients, pstmtIngredients);
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

    private void retrieveIngredients(Pizza pizza, PreparedStatement pstmtIdsIngredients, PreparedStatement pstmtIngredients)
            throws SQLException {
        ResultSet rsIdsIngredients = pstmtIdsIngredients.executeQuery();
        ArrayList<Integer> idsIngredientsList = new ArrayList<>();
        ArrayList<Ingredient> ingredientsList = new ArrayList<>();
        while (rsIdsIngredients.next()) {
            idsIngredientsList.add(rsIdsIngredients.getInt("idingredient"));
            pstmtIngredients.setInt(1, rsIdsIngredients.getInt("idingredient"));
            ResultSet rsIngredients = pstmtIngredients.executeQuery();
            if (rsIngredients.next()) {
                ingredientsList.add(new Ingredient(rsIngredients.getInt("idingredient"), rsIngredients.getString("name"), rsIngredients.getFloat("price")));
            }
        }   
        pizza.setIdsIngredientsList(idsIngredientsList);
        pizza.setIngredientsList(ingredientsList);
    }

    public boolean deletePizza(int id) {
        boolean deleted = false;

        try {
            if (this.findById(id) != null) {
                this.con = ds.getConnection();

                
                PreparedStatement pstmtDeletePizza = con.prepareStatement("DELETE FROM pizzas WHERE idbasepizza=?");
                pstmtDeletePizza.setInt(1, id);
                pstmtDeletePizza.executeUpdate();
                
                PreparedStatement pstmtDeleteCommande = con.prepareStatement("DELETE FROM commandespizzas WHERE idpizza=?");
                pstmtDeleteCommande.setInt(1, id);
                pstmtDeleteCommande.executeUpdate();

                PreparedStatement pstmtDeleteBasePizza = con.prepareStatement("DELETE FROM basespizzas WHERE idbasepizza=?");
                pstmtDeleteBasePizza.setInt(1, id);
                pstmtDeleteBasePizza.executeUpdate();

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

    public boolean deleteIngredient(int idPizza, int idIngredient) {
        boolean deleted = false;

        try {
            Pizza pizza = this.findById(idPizza);
            if (pizza != null) {
                this.con = ds.getConnection();

                PreparedStatement pstmtDeleteIngrediStatement = con.prepareStatement("DELETE FROM pizzas WHERE idbasepizza=? AND idingredient=?");
                pstmtDeleteIngrediStatement.setInt(1, pizza.getId());
                pstmtDeleteIngrediStatement.setInt(2, idIngredient);
                pstmtDeleteIngrediStatement.executeUpdate();

                if (pizza.getIdsIngredientsList().remove((Integer) idIngredient)) {
                    deleted = true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return deleted;
    }


    public boolean savePizza(Pizza pizza) {
        boolean saved = false;

        try {
            if (this.findById(pizza.getId()) == null) {
                this.con = ds.getConnection();

                // ajout basepizza
                PreparedStatement pstmtInsertPizza = con.prepareStatement("INSERT INTO basespizzas VALUES(?,?,?,?)");
                pstmtInsertPizza.setInt(1, pizza.getId());
                pstmtInsertPizza.setString(2, pizza.getName());
                pstmtInsertPizza.setString(3, pizza.getTypePate().getLabel());
                pstmtInsertPizza.setDouble(4, pizza.getPrixBase());
                if (pstmtInsertPizza.executeUpdate() < 0) {
                    saved = true;
                }

                // ajout ingrédients
                if (pizza.getIngredientsList() != null) {
                    PreparedStatement pstmtInsertIngredients = con.prepareStatement("INSERT INTO pizzas VALUES(?,?)");
                    List<Ingredient> ingredientsList =  pizza.getIngredientsList();
                    pstmtInsertIngredients.setInt(1, pizza.getId());
                    for (Ingredient ingredient : ingredientsList) {
                        pstmtInsertIngredients.setInt(2,ingredient.getId());
                        pstmtInsertIngredients.addBatch();
                    }
                    if (pstmtInsertIngredients.executeBatch().length > 0) {
                        saved = true;
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
        
        return saved;
    }

    public boolean addIngredient(int idPizza, Ingredient ingredient) {
        boolean saved = false;

        try {
            Pizza pizza = this.findById(idPizza);

            if (pizza != null) {
                this.con = ds.getConnection();

                if (!pizza.getIngredientsList().contains(ingredient)) {
                    PreparedStatement pstmtInsertIngredients = con.prepareStatement("INSERT INTO pizzas VALUES(?, ?)");
                    pstmtInsertIngredients.setInt(1, pizza.getId());
                    pstmtInsertIngredients.setInt(2, ingredient.getId());
                    pstmtInsertIngredients.executeUpdate();
    
                    pizza.getIngredientsList().add(ingredient);
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
            this.con = ds.getConnection();

            finalPrice += pizza.getPrixBase();
            Statement stmtPriceIngredients = con.createStatement();
            for (Integer idingredient : pizza.getIdsIngredientsList()) {
                ResultSet rsPriceIngredients = stmtPriceIngredients.executeQuery("SELECT price FROM ingredients WHERE idingredient=" + idingredient);
                if (rsPriceIngredients.next()) {
                    finalPrice += (double) rsPriceIngredients.getFloat("price");
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
    

    public boolean updatePizza(int idPizza, Pizza newPizza) {
        boolean updated = false;

        try {
            Pizza oldPizza = this.findById(idPizza);
            this.con = ds.getConnection();

            PreparedStatement pstmtUpdateBasePizza = con.prepareStatement("UPDATE basespizzas SET name=?, typepate=?, prixbase=? WHERE idbasepizza=?");
            pstmtUpdateBasePizza.setInt(4, idPizza); 

            PreparedStatement pstmtDeleteIngredients = con.prepareStatement("DELETE FROM pizzas WHERE idbasepizza=?");
            pstmtDeleteIngredients.setInt(1, idPizza);  
            pstmtDeleteIngredients.executeUpdate();

            PreparedStatement pstmtInsertIngredients = con.prepareStatement("INSERT INTO pizzas VALUES (?,?)");
            pstmtInsertIngredients.setInt(1, idPizza); 

            if (oldPizza != null && newPizza != null) {
                if (!oldPizza.getName().equals(newPizza.getName()) && newPizza.getName() != null) {
                    pstmtUpdateBasePizza.setString(1, newPizza.getName());
                } else {
                    pstmtUpdateBasePizza.setString(1, oldPizza.getName());
                }

                if (newPizza.getTypePate() != null) {
                    if (!oldPizza.getTypePate().getLabel().equals(newPizza.getTypePate().getLabel())) {
                        pstmtUpdateBasePizza.setString(2, newPizza.getTypePate().getLabel());
                    } 
                } else {
                    pstmtUpdateBasePizza.setString(2, oldPizza.getTypePate().getLabel());
                }
                

                if (oldPizza.getPrixBase() != newPizza.getPrixBase()) {
                    pstmtUpdateBasePizza.setDouble(3, oldPizza.getPrixBase());
                } else {
                    pstmtUpdateBasePizza.setDouble(3, oldPizza.getPrixBase());
                }
                

                if (oldPizza.getIngredientsList().equals(newPizza.getIngredientsList())) {
                    for (Ingredient ingredient : newPizza.getIngredientsList()) {
                        pstmtInsertIngredients.setInt(2, ingredient.getId());
                        pstmtInsertIngredients.addBatch();
                    }
                } else {
                    for (Ingredient ingredient : oldPizza.getIngredientsList()) {
                        pstmtInsertIngredients.setInt(2, ingredient.getId());
                        pstmtInsertIngredients.addBatch();
                    }
                }
            }
            pstmtUpdateBasePizza.executeUpdate();
            pstmtInsertIngredients.executeBatch();
            updated = true;
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

        return updated;
    }

}
