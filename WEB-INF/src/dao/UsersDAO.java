package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Users;

public class UsersDAO {
	private Connection con;

    public UsersDAO() {
        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/devweb","adri","adriPostgresql");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Users> findAll() {
        List<Users> usersList = new ArrayList<>();

        try {

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while(rs.next()){
               usersList.add(this.findByLogs(rs.getString("login"), rs.getString("password")));
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
		return usersList;
        
    }
    
    
    public Users findByLogs(String login,String password) {
    	Users user = null;
    	try {
    		PreparedStatement pstmtSelect = con.prepareStatement("SELECT * FROM clients WHERE login=? AND password=?");
    		pstmtSelect.setString(1, login);
    		pstmtSelect.setString(2, password);
			System.out.println(pstmtSelect.toString());
    		ResultSet rs = pstmtSelect.executeQuery();
    		
    		if (rs.next()) {
    				user = new Users(rs.getInt("idclient"), login, password, rs.getString("email"), rs.getString("address"), rs.getString("tel"));
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
            
    	return user;
    }

}
