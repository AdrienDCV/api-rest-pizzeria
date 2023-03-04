package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.Ingredient;

public class IngredientDAO {
    
    // attributes 
    private DS ds;
    private Connection con;

    public IngredientDAO() {
        this.ds = new DS();
    }

    // methods
    public List<Ingredient> findAll() {
        List<Ingredient> ingredientsList = new ArrayList<>();

        try {
            this.con = ds.getConnection();

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ingredients");

            while(rs.next()){
               ingredientsList.add(this.findById(rs.getInt("idingredient")));
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

        return ingredientsList;

    } 

    public Ingredient findById(int id) {
        Ingredient ingredient = null;

        try {
            this.con = ds.getConnection();

            PreparedStatement pstmtSelect = con.prepareStatement("SELECT * FROM ingredients WHERE idingredient=?");
            pstmtSelect.setInt(1, id);
            ResultSet rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                ingredient = new Ingredient(id, rs.getString("name"), rs.getDouble("price"));
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
       
        return ingredient;
    }

    public boolean delete(int id) {
        boolean deleted = false;

        try {
            if (this.findById(id) != null) {
                this.con = ds.getConnection();

                PreparedStatement pstmtDelete = con.prepareStatement("DELETE FROM ingredients WHERE idingredient=?");
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

    public boolean save(Ingredient ingredient) {
        boolean saved = false;

        try {
            if (this.findById(ingredient.getId()) == null) {
                this.con = ds.getConnection();

                PreparedStatement pstmtInsert = con.prepareStatement("INSERT INTO ingredients VALUES(?,?,?)");
                pstmtInsert.setInt(1, ingredient.getId());
                pstmtInsert.setString(2, ingredient.getName());
                pstmtInsert.setDouble(3, ingredient.getPrice());
                pstmtInsert.executeUpdate();
                saved = true;
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
