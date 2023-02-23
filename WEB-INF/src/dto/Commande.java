package dto;

import java.util.List;

public class Commande {
    
    // attributes
    private int idCommande;
    private int idUser;
    private String date;
    private List<Pizza> pizzas;

    // constructor(s)
    public Commande() {}

    public Commande(int idCommande, int idUser, String date, List<Pizza> pizzas) {
        this.idCommande = idCommande;
        this.idUser = idUser;
        this.date = date;
        this.pizzas = pizzas;
    }

    // methods
    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    } 

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

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    @Override
    public String toString() {
        return "Commande [idCommande=" + idCommande + ", idUser=" + idUser + ", date=" + date + ", pizzas=" + pizzas
                + "]";
    }
    
}
