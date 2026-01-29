package model;

import java.time.Instant;

public class StockMovement {
    private Integer id;
    private Ingredient ingredient;
    private Double quantity;
    private Instant movementDatetime;
    private MovementTypeEnum type; // Entrée (IN) ou Sortie (OUT)

    public StockMovement(Integer id, Ingredient ingredient, Double quantity, MovementTypeEnum type) {
        this.id = id;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.type = type;
        this.movementDatetime = Instant.now();
    }

    // Getters
    public Integer getId() { return id; }
    public Ingredient getIngredient() { return ingredient; }
    public Double getQuantity() { return quantity; }
    public Instant getMovementDatetime() { return movementDatetime; }
    public MovementTypeEnum getType() { return type; }
}