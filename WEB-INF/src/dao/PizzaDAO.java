package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.Ingredient;
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

            Statement stmtPizzas = con.createStatement();
            ResultSet rsBasePizza = stmtPizzas.executeQuery("SELECT * FROM basepizza");

            while(rsBasePizza.next()){
                pizzasList.add(new Pizza(rsBasePizza.getInt("id"), rsBasePizza.getString("name"), TypePate.get(rsBasePizza.getString("typePate")), rsBasePizza.getFloat("prixBase"), new ArrayList<>(), new ArrayList<>()));
            }

            PreparedStatement pstmtIdsIngredients = con.prepareStatement("SELECT idingredient FROM pizza WHERE idbasepizza=?");
            PreparedStatement pstmtIngredients = con.prepareStatement("SELECT * FROM ingredients WHERE id=?");
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
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

            PreparedStatement pstmtSelectBasePizza = con.prepareStatement("SELECT * FROM basepizza WHERE id=?");
            pstmtSelectBasePizza.setInt(1, id);
            ResultSet rsBasePizza = pstmtSelectBasePizza.executeQuery();

            if(rsBasePizza.next()){
                pizza = new Pizza(rsBasePizza.getInt("id"), rsBasePizza.getString("name"), TypePate.get(rsBasePizza.getString("typePate")), rsBasePizza.getFloat("prixBase"), new ArrayList<>(), new ArrayList<>());
            
                PreparedStatement pstmtIdsIngredients = con.prepareStatement("SELECT idingredient FROM pizza WHERE idbasepizza=?");
                PreparedStatement pstmtIngredients = con.prepareStatement("SELECT * FROM ingredients WHERE id=?");
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
                ingredientsList.add(new Ingredient(rsIngredients.getInt("id"), rsIngredients.getString("name"), rsIngredients.getFloat("price")));
            }
        }   
        pizza.setIdsIngredientsList(idsIngredientsList);
        pizza.setIngredientsList(ingredientsList);
    }

    public boolean deletePizza(int id) {
        boolean deleted = false;

        try {
            if (this.findById(id) != null) {
                Class.forName("org.postgresql.Driver");
                this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

                
                PreparedStatement pstmtDeletePizza = con.prepareStatement("DELETE FROM pizza WHERE idbasepizza=?");
                pstmtDeletePizza.setInt(1, id);
                pstmtDeletePizza.executeUpdate();
                
                PreparedStatement pstmtDeleteCommande = con.prepareStatement("DELETE FROM commandepizza WHERE idpizza=?");
                pstmtDeleteCommande.setInt(1, id);
                pstmtDeleteCommande.executeUpdate();

                PreparedStatement pstmtDeleteBasePizza = con.prepareStatement("DELETE FROM basepizza WHERE id=?");
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
                Class.forName("org.postgresql.Driver");
                this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

                PreparedStatement pstmtDeleteIngrediStatement = con.prepareStatement("DELETE FROM pizza WHERE idbasepizza=? AND idingredient=?");
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return deleted;
    }


    public boolean savePizza(Pizza pizza) {
        boolean saved = false;
        System.out.println(pizza);

        try {
            if (this.findById(pizza.getId()) == null) {
                System.out.println("passe par là !");
                Class.forName("org.postgresql.Driver");
                this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

                // ajout basepizza
                PreparedStatement pstmtInsertPizza = con.prepareStatement("INSERT INTO basepizza VALUES(?,?,?,?)");
                pstmtInsertPizza.setInt(1, pizza.getId());
                pstmtInsertPizza.setString(2, pizza.getName());
                pstmtInsertPizza.setString(3, pizza.getTypePate().getLabel());
                pstmtInsertPizza.setDouble(4, pizza.getPrixBase());
                if (pstmtInsertPizza.executeUpdate() < 0) {
                    saved = true;
                }

                // ajout ingrédients
                if (pizza.getIdsIngredientsList() != null) {
                    System.out.println("passe aussi par là !");
                    PreparedStatement pstmtInsertIngredients = con.prepareStatement("INSERT INTO pizza VALUES(?,?)");
                    List<Integer> idsIngredientsList =  pizza.getIdsIngredientsList();
                    pstmtInsertIngredients.setInt(1, pizza.getId());
                    for (Integer pizzaId : idsIngredientsList) {
                        pstmtInsertIngredients.setInt(2,pizzaId);
                        pstmtInsertIngredients.addBatch();
                    }
                    if (pstmtInsertIngredients.executeBatch().length < 0) {
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

    public boolean addIngredient(int idPizza, int idIngredient) {
        boolean saved = false;

        try {
            Pizza pizza = this.findById(idPizza);

            if (pizza != null) {
                Class.forName("org.postgresql.Driver");
                this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM pizza WHERE idbasepizza=" + pizza.getId() + " AND idingredient=" + idIngredient);

                if (!rs.next()) {
                    PreparedStatement pstmtInsertIngredients = con.prepareStatement("INSERT INTO pizza VALUES(?, ?)");
                    pstmtInsertIngredients.setInt(1, pizza.getId());
                    pstmtInsertIngredients.setInt(2, idIngredient);
                    pstmtInsertIngredients.executeUpdate();
    
                    pizza.getIdsIngredientsList().add(idIngredient);
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

            finalPrice += pizza.getPrixBase();
            Statement stmtPriceIngredients = con.createStatement();
            for (Integer idingredients : pizza.getIdsIngredientsList()) {
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
    

    public boolean updatePizza(String datasToUpdate) {
        boolean updated = false;

        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

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
