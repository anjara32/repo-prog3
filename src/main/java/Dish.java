package model;

import java.util.ArrayList;
import java.util.List;

public class Dish {

    private Integer id;
    private String name;
    private DishType dishType; // Assure-toi que DishType.java existe ou utilise DishTypeEnum
    private Double sellingPrice;
    private List<DishIngredient> ingredients = new ArrayList<>();

    public Dish(Integer id, String name, DishType dishType, Double sellingPrice) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.sellingPrice = sellingPrice;
    }

    public void addIngredient(DishIngredient di) {
        ingredients.add(di);
    }

    // Getters
    public Integer getId() { return id; }
    public String getName() { return name; }
    public DishType getDishType() { return dishType; }
    public Double getSellingPrice() { return sellingPrice; }
    public List<DishIngredient> getIngredients() { return ingredients; }


    public Double getDishCost() {
        double total = 0.0;
        for (DishIngredient di : ingredients) {
            if (di.getQuantityRequired() == null) {
                throw new IllegalStateException(
                        "Quantité requise non définie pour l'ingrédient : "
                                + di.getIngredient().getName()
                );
            }
            // Utilise getUnitPrice() de Ingredient
            total += di.getIngredient().getUnitPrice() * di.getQuantityRequired();
        }
        return total;
    }


    public Double getGrossMargin() {
        if (sellingPrice == null) {
            throw new IllegalStateException(
                    "Prix de vente non défini pour le plat : " + name
            );
        }
        return sellingPrice - getDishCost();
    }
}