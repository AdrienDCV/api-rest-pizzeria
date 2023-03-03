package controlers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.JwtManager;
import dao.UsersDAO;

@WebServlet("/users/token")
public class UsersRestApi extends HttpServlet{

	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = res.getWriter();
        UsersDAO usersDAO = new UsersDAO();

        String login = req.getParameter("login");
        String password = req.getParameter("pwd");
        
        System.out.println(password);
   
        String token = null;
        if (usersDAO.findByLogs(login, password) != null) {
            JwtManager jwtManager = new JwtManager();
            token = jwtManager.createJWT();
            out.println(token);
            return;
        } else {
            out.println("Utilisateur inconnu : accès interdit.");
        }

        return;
    }

    
	
}
