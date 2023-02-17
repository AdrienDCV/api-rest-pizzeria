package controlers;

import java.io.*;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.PizzaDAO;
import dto.Pizza;

@WebServlet("/pizzas/*")
public class PizzasRestApi extends HttpServlet{

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = res.getWriter();
        PizzaDAO ingDAO = new PizzaDAO();
        ObjectMapper objMapper = new ObjectMapper();
        
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Pizza> pizzasList = ingDAO.findAll();
            String jsonString = objMapper.writeValueAsString(pizzasList);
            out.print(jsonString);
            return;
        }
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("application/json;charset=UTF-8");
        ObjectMapper objMapper = new ObjectMapper();
        PizzaDAO pizzaDAO = new PizzaDAO();

        String pizzaInfos = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
        Pizza pizza = objMapper.readValue(pizzaInfos, Pizza.class);
        if (pizzaDAO.save(pizza)) {
            res.sendError(HttpServletResponse.SC_CREATED);  
        } else {
            res.sendError(HttpServletResponse.SC_CONFLICT);
        }

    }

}
