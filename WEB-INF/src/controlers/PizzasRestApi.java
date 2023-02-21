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
        PizzaDAO pizzaDAO = new PizzaDAO();
        ObjectMapper objMapper = new ObjectMapper();
        
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Pizza> pizzasList = pizzaDAO.findAll();
            String jsonString = objMapper.writeValueAsString(pizzasList);
            out.print(jsonString);
            return;
        }

        String[] pathInfoSplits = pathInfo.split("/");
        if (pathInfoSplits.length < 0 && pathInfoSplits.length < 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String id = pathInfoSplits[1];
        Pizza pizza = pizzaDAO.findById(Integer.parseInt(id));
        if (pizza == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // GET id
        if (pathInfoSplits.length == 2) {
            String jsonString = objMapper.writeValueAsString(pizza);
            out.print(jsonString);
            return;
        }

        // GET prix final
        if (pathInfoSplits.length == 3) {
            double finalPrice = pizzaDAO.getFinalPrice(pizza);
            String jsonString = objMapper.writeValueAsString(finalPrice);
            out.print(jsonString);
            return;
        }

        return;
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

    public void doDelete (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("application/json;charset=UTF-8");
        PizzaDAO pizzaDAO = new PizzaDAO();

        String pathInfo = req.getPathInfo();
        String[] pathInfoSplits = pathInfo.split("/");
        if (pathInfoSplits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // DELETE id
        String id = pathInfoSplits[1];
        if (pizzaDAO.delete(Integer.parseInt(id))) {
            res.sendError(HttpServletResponse.SC_OK);
        } else {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);      
        }


    }

}
