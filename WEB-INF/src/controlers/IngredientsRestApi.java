package controlers;

import java.io.*;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.IngredientDAO;
import dao.JwtManager;
import dto.Ingredient;


@WebServlet("/ingredients/*")
public class IngredientsRestApi extends HttpServlet {

    // attributes
    IngredientDAO ingDAO = new IngredientDAO();
    ObjectMapper objMapper = new ObjectMapper();
    JwtManager jwtManager = new JwtManager();

    // methods
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = res.getWriter();
        
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Ingredient> ingredientsList = ingDAO.findAll();
            String jsonString = objMapper.writeValueAsString(ingredientsList);
            out.print(jsonString);
            return;
        }

        String[] pathInfoSplits = pathInfo.split("/");
        if (pathInfoSplits.length > 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String id = pathInfoSplits[1];
        Ingredient ingredient = ingDAO.findById(Integer.parseInt(id));
        if (ingredient == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // GET id
        if (pathInfoSplits.length == 2) {
            String jsonString = objMapper.writeValueAsString(ingredient);
            out.print(jsonString);
            return;
        }

        // GET name
        if (pathInfoSplits.length == 3 && pathInfo.endsWith("name")) {
            String jsonString = objMapper.writeValueAsString(ingredient.getName());
            out.print(jsonString);
            return;
        } else {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        return;
    }

    public void doPost (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            jwtManager.decodeJWT(req.getHeader("Authorization"));

            res.setContentType("application/json;charset=UTF-8");

            String ingredientInfos = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
            Ingredient ingredient = objMapper.readValue(ingredientInfos, Ingredient.class);
            if (ingDAO.save(ingredient)) {
                res.sendError(HttpServletResponse.SC_CREATED);  
            } else {
                res.sendError(HttpServletResponse.SC_CONFLICT);
            }
        }
        catch (Exception e) {
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
            if (pathInfoSplits.length != 2) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
    
            String id = pathInfoSplits[1];
            if (ingDAO.delete(Integer.parseInt(id))) {
                res.sendError(HttpServletResponse.SC_OK);
                return;
            } else {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);      
                return;
            }
        }
        catch (Exception e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

    }
    

}