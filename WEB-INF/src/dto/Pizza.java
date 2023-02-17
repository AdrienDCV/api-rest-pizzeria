package dto;


import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Pizza {
    
    // attributes
    private int id;
    private String name;
    private TypePate typePate;
    private double prixBase;
    private int[] ingredients; 

    // constructor(s)
    public Pizza() {}

    public Pizza(int id, String name, TypePate typePate, double prixBase, int[] ingredients) {
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

    public int[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(int[] ingredients) {
        this.ingredients = ingredients;
    }


    @Override
    public String toString() {
        return "Pizza [id=" + id + ", name=" + name + ", typePate=" + typePate + ", prixBase=" + prixBase
                + ", Ingredients=" + ingredients + "]";
    }

}
