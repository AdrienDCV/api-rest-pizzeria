package controlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.IngredientDAO;
import dao.JwtManager;
import dao.UsersDAO;
import dto.Ingredient;
import dto.Users;
import io.jsonwebtoken.Claims;

public class UsersRestApi extends HttpServlet{

	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = res.getWriter();
        UsersDAO usersDAO = new UsersDAO();
        ObjectMapper objMapper = new ObjectMapper();
        
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Users> usersList = usersDAO.findAll();
            String jsonString = objMapper.writeValueAsString(usersList);
            out.print(jsonString);
            return;
        }

        String[] pathInfoSplits = pathInfo.split("/");
        if (pathInfoSplits.length < 0 && pathInfoSplits.length < 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String id = pathInfoSplits[1];
        Users user= usersDAO.findById(Integer.parseInt(id));
        if (user == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // GET id
        if (pathInfoSplits.length == 2) {
            String jsonString = objMapper.writeValueAsString(user);
            out.print(jsonString);
            return;
        }
        	

        return;
    }
	
	public boolean verifToken(HttpServletRequest req, HttpServletResponse res) throws IOException {
		if(req.getParameter("login")!=null && req.getParameter("mdp")!=null) {
        	JwtManager jwt=new JwtManager(""+req.getParameter("mdp"));
        	String token=jwt.createJWT();
        	UsersDAO userDAO =new UsersDAO();
        	Users user1=userDAO.findByLogs(req.getParameter("login"), req.getParameter("mdp"));
        	if(user1!=null) {
        		ObjectMapper objMapper =new ObjectMapper();
				PrintWriter out=res.getWriter();
				if(user1.getToken()!=null) {
        			Claims claim;
					try {
						claim = jwt.decodeJWT(token);
						if(user1.getToken()!=claim.toString()) {
	        				return false;
	        			}
						else {
							String jsonString = objMapper.writeValueAsString(token);
							out.print(jsonString);
							return true;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			
        			
        		}
        		else {
        			return false;
        		}
        	}
        }
		return false;
	}
}
