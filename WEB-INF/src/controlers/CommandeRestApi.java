package controlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.CommandeDAO;
import dto.Commande;

@WebServlet("/commandes/*")
public class CommandeRestApi extends HttpServlet{
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = res.getWriter();
        CommandeDAO commandeDAO = new CommandeDAO();
        ObjectMapper objMapper = new ObjectMapper();
        
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Commande> commandesList = commandeDAO.findAll();
            String jsonString = objMapper.writeValueAsString(commandesList);
            out.print(jsonString);
            return;
        }
        
        return;
    }

    public void doPost (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TO DO
    }

}
