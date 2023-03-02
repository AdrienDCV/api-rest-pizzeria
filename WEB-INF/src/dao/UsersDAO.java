package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Ingredient;
import dto.Pizza;
import dto.TypePate;
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
               usersList.add(this.findById(rs.getInt("id")));
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
    
    public Users findById(int id) {
    	Users user = null;
    	try {
    		PreparedStatement pstmtSelect = con.prepareStatement("SELECT * FROM users WHERE idclient =?");
    		pstmtSelect.setInt(1, id);
    		ResultSet rs = pstmtSelect.executeQuery();
    		
    		if (rs.next()) {
    			user = new Users(id, rs.getString("name"), rs.getString("password"), rs.getString("token"), rs.getString("address"), rs.getString("tel"));
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
    
    public Users findByLogs(String login,String mdp) {
    	Users user = null;
    	try {
    		PreparedStatement pstmtSelect = con.prepareStatement("SELECT * FROM users WHERE name=? mdp=?");
    		pstmtSelect.setString(1, login);
    		pstmtSelect.setString(2, mdp);
    		ResultSet rs = pstmtSelect.executeQuery();
    		
    		if (rs.next()) {
    			user = new Users(rs.getInt("idclient"), rs.getString("name"), rs.getString("password"), rs.getString("token"), rs.getString("address"), rs.getString("tel"));
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
    
    public List<String> getAllToken(){
    	List<String> tokenList=new ArrayList<String>();
		List<Users> usersList=this.findAll();
		for(int i=0;i<usersList.size();i++) {
			if(usersList.get(i).getToken()!=null) {
				tokenList.add(usersList.get(i).getToken());
			}
		}
    	return tokenList;
    }
    
    public String getTokenByUserId(int id) {
    	return this.findById(id).getToken();
    }
    
    public void saveToken(Users user, String token){
    	
    	try {
    		user.setToken(token);
    		PreparedStatement pstmtUpdate=con.prepareStatement("SET token=? FROM users WHERE idclient=?");
    		pstmtUpdate.setString(1, token);
    		pstmtUpdate.setInt(2, user.getIdclient());
    		pstmtUpdate.executeUpdate();
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
            
    }
}
