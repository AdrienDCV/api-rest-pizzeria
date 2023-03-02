package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.Ingredient;

public class IngredientDAO {
    
    // attributes 
    private Connection con;

    public IngredientDAO() {
        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // methods
    public List<Ingredient> findAll() {
        List<Ingredient> ingredientsList = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ingredients");

            while(rs.next()){
               ingredientsList.add(this.findById(rs.getInt("id")));
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
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

            PreparedStatement pstmtSelect = con.prepareStatement("SELECT * FROM ingredients WHERE id =?");
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
                Class.forName("org.postgresql.Driver");
                this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

                PreparedStatement pstmtDelete = con.prepareStatement("DELETE FROM ingredients WHERE id=?");
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
                Class.forName("org.postgresql.Driver");
                this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

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
