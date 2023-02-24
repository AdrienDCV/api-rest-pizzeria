package dto;

import java.sql.Date;
import java.util.List;

public class Commande {
    
    // attributes
    private int idCommande;
    private int idClient;
    private Date date;
    private List<Pizza> pizzasList;

    // constructor(s)
    public Commande() {}

    public Commande(int idCommande, int idClient, Date date, List<Pizza> pizzasList) {
        this.idCommande = idCommande;
        this.idClient = idClient;
        this.date = date;
        this.pizzasList = pizzasList;
    }

    // methods
    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    } 

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Pizza> getPizzasList() {
        return pizzasList;
    }

    public void setPizzasList(List<Pizza> pizzasList) {
        this.pizzasList = pizzasList;
    }

    @Override
    public String toString() {
        return "Commande [idCommande=" + idCommande + ", idClient=" + idClient + ", date=" + date + ", pizzasList=" + pizzasList + "]";
    }
    
    
    
}
