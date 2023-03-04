package controlers;

import java.io.*;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.JwtManager;
import dao.PizzaDAO;
import dto.Ingredient;
import dto.Pizza;

@WebServlet("/pizzas/*")
public class PizzasRestApi extends HttpServlet{
   
    // attributes
    PizzaDAO pizzaDAO = new PizzaDAO();
    ObjectMapper objMapper = new ObjectMapper();
    JwtManager jwtManager = new JwtManager();


    // methods
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = res.getWriter();
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Pizza> pizzasList = pizzaDAO.findAll();
            objMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS , false);
            String jsonString = objMapper.writeValueAsString(pizzasList);
            out.print(jsonString);
            return;
        }

        String[] pathInfoSplits = pathInfo.split("/");
        if (pathInfoSplits.length > 3) {
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
            res.sendError(HttpServletResponse.SC_OK);
            return;
        }

        // GET prix final
        if (pathInfoSplits.length == 3 && pathInfo.endsWith("prixfinal")) {
            double finalPrice = pizzaDAO.getFinalPrice(pizza);
            String jsonString = objMapper.writeValueAsString(finalPrice);
            out.print(jsonString);
            res.sendError(HttpServletResponse.SC_OK);
            return;
        }

        return;
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            jwtManager.decodeJWT(req.getHeader("Authorization"));

            res.setContentType("application/json;charset=UTF-8");

            String pathInfo = req.getPathInfo();
            String[] pathInfoSplits = pathInfo.split("/");

            if (pathInfoSplits.length == 2) {
                String idPizza = pathInfoSplits[1];
                String ingredientInfo = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
                Ingredient ingredient = objMapper.readValue(ingredientInfo, Ingredient.class);
                if (pizzaDAO.addIngredient(Integer.parseInt(idPizza), ingredient)) {
                    res.sendError(HttpServletResponse.SC_CREATED);  
                } else {
                    res.sendError(HttpServletResponse.SC_CONFLICT);
                }
                return;
            }
            else {
                String pizzaInfos = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
                Pizza pizza = objMapper.readValue(pizzaInfos, Pizza.class);
                if (pizzaDAO.savePizza(pizza)) {
                    res.sendError(HttpServletResponse.SC_CREATED);  
                } else {
                    res.sendError(HttpServletResponse.SC_CONFLICT);
                }
                return;
            }

        } catch (Exception e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        
    }

    public void doDelete (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            jwtManager.decodeJWT(req.getHeader("Authorization"));

            res.setContentType("application/json;charset=UTF-8");

            String pathInfo = req.getPathInfo();
            String[] pathInfoSplits = pathInfo.split("/");
            if (pathInfoSplits.length > 3) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // DELETE id
            if (pathInfoSplits.length == 2) {
                String id = pathInfoSplits[1];
                if (pizzaDAO.deletePizza(Integer.parseInt(id))) {
                    res.sendError(HttpServletResponse.SC_OK);
                } else {
                    res.sendError(HttpServletResponse.SC_NOT_FOUND);      
                }
            }

            if (pathInfoSplits.length == 3) {
                String idPizza = pathInfoSplits[1];
                String idIngredient = pathInfoSplits[2];
                if (pizzaDAO.deleteIngredient(Integer.parseInt(idPizza), Integer.parseInt(idIngredient))) {
                    res.sendError(HttpServletResponse.SC_OK);
                } else {
                    res.sendError(HttpServletResponse.SC_NOT_FOUND);      
                }
            }
            return;
        }
        catch (Exception e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
    }
    
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            this.doPatch(req, res);
        } else {
            super.service(req, res);
        }
    }

    public void doPatch (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
    	
        try {
            jwtManager.decodeJWT(req.getHeader("Authorization"));

            res.setContentType("application/json;charset=UTF-8");

            String pathInfo = req.getPathInfo();
            String[] pathInfoSplits = pathInfo.split("/");

            String pizzaInfos = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
            Pizza newPizza = objMapper.readValue(pizzaInfos, Pizza.class);

            if (pathInfoSplits.length == 2) {
                String id = pathInfoSplits[1];
                if (pizzaDAO.updatePizza(Integer.parseInt(id), newPizza)) {
                    res.sendError(HttpServletResponse.SC_OK);
                } else {
                    res.sendError(HttpServletResponse.SC_NOT_FOUND);      
                }
            }
            return;
        }
        catch (Exception e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
    
    }
    

}
