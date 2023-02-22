package dto;

import java.util.List;

public class Commande {
    
    // attributes
    private int idUser;
    private String date;
    private List<Integer> pizzas;

    // constructor(s)
    public Commande() {}

    public Commande(int idUser, String date, List<Integer> pizzas) {
        this.idUser = idUser;
        this.date = date;
        this.pizzas = pizzas;
    }

    // methods

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Integer> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<Integer> pizzas) {
        this.pizzas = pizzas;
    }
}
