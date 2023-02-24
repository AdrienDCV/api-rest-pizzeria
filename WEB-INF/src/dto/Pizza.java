package dto;

import java.util.List;

public class Pizza {
    
    // attributes
    private int id;
    private String name;
    private TypePate typePate;
    private double prixBase;
    private List<Integer> idsIngredientsList; 
    private List<Ingredient> ingredientsList;

    // constructor(s)
    public Pizza() {}

    public Pizza(int id, String name, TypePate typePate, double prixBase, List<Integer> idsIngredientsList, List<Ingredient> ingredientsList) {
        this.id = id;
        this.name = name;
        this.typePate = typePate;
        this.prixBase = prixBase;
        this.idsIngredientsList = idsIngredientsList; 
        this.ingredientsList = ingredientsList;
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

    public List<Integer> getIdsIngredientsList() {
        return idsIngredientsList;
    }

    public void setIdsIngredientsList(List<Integer> idsIngredientsList) {
        this.idsIngredientsList = idsIngredientsList;
    }

    public List<Ingredient> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    @Override
    public String toString() {
        return "Pizza [id=" + id + ", name=" + name + ", typePate=" + typePate + ", prixBase=" + prixBase
                + ", idsIngredientsList=" + idsIngredientsList + ", ingredientsList=" + ingredientsList + "]";
    }

    

}
