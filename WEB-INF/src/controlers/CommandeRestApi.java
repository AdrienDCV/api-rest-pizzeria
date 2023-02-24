package controlers;

import java.io.IOException;
import java.io.*;
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
        
        String[] pathInfoSplits = pathInfo.split("/");
        if (pathInfoSplits.length < 0 && pathInfoSplits.length < 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String id = pathInfoSplits[1];
        Commande commande = commandeDAO.findById(Integer.parseInt(id));
        if (commande == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // GET id
        if (pathInfoSplits.length == 2) {
            String jsonString = objMapper.writeValueAsString(commande);
            out.print(jsonString);
            return;
        }

        // GET prix final
        if (pathInfoSplits.length == 3 && pathInfoSplits[2].equals("prixfinal")) {
            double finalPrice = commandeDAO.getFinalPrice(commande);
            String jsonString = objMapper.writeValueAsString(finalPrice);
            out.print(jsonString);
            return;
        }

        return;
    }

    public void doPost (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("application/json;charset=UTF-8");
        ObjectMapper objMapper = new ObjectMapper();
        CommandeDAO commandeDAO = new CommandeDAO();

        String commandeInfos = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
        Commande commande = objMapper.readValue(commandeInfos, Commande.class);
        if (commandeDAO.save(commande)) {
            res.sendError(HttpServletResponse.SC_CREATED);  
        } else {
            res.sendError(HttpServletResponse.SC_CONFLICT);
        }
        return;
    }

}
