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
import dao.UsersDAO;
import dto.Ingredient;
import dto.Users;

public class UsersRestApi extends HttpServlet{

	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = res.getWriter();
        UsersDAO usersDAO = new UsersDAO();
        ObjectMapper objMapper = new ObjectMapper();
        
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Users> ingredientsList = usersDAO.findAll();
            String jsonString = objMapper.writeValueAsString(ingredientsList);
            out.print(jsonString);
            return;
        }

        String[] pathInfoSplits = pathInfo.split("/");
        if (pathInfoSplits.length < 0 && pathInfoSplits.length < 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String id = pathInfoSplits[1];
        Users user= UsersDAO.findById(Integer.parseInt(id));
        if (user == null) {
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
        if (pathInfoSplits.length == 3) {
            String jsonString = objMapper.writeValueAsString(ingredient.getName());
            out.print(jsonString);
            return;
        }

        return;
    }
}
