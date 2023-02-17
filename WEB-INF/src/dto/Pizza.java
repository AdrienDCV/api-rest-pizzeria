package dto;

import java.util.List;

public class Pizza {
    
    // attributes
    private int id;
    private String name;
    private TypePate typePate;
    private double prixBase;
    private List<Integer> ingredients; 

    // constructor(s)
    public Pizza() {}

    public Pizza(int id, String name, TypePate typePate, double prixBase, List<Integer> ingredients) {
        this.id = id;
        this.name = name;
        this.typePate = typePate;
        this.prixBase = prixBase;
        this.ingredients = ingredients; 
    }

    // methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypePate getTypePate() {
        return typePate;
    }

    public void setTypePate(String typePateString) {
        this.typePate = TypePate.get(typePateString);
    }

    public double getPrixBase() {
        return prixBase;
    }

    public void setPrixBase(double prixBase) {
        this.prixBase = prixBase;
    }

    public List<Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Integer> ingredients) {
        this.ingredients = ingredients;
    }


    @Override
    public String toString() {
        return "Pizza [id=" + id + ", name=" + name + ", typePate=" + typePate + ", prixBase=" + prixBase
                + ", Ingredients=" + ingredients + "]";
    }

}
