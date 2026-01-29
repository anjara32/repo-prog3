package model;

import java.time.Instant;

/**
 * Représente une vente finalisée associée à une commande payée.
 */
public class Sale {
    private Integer id;
    private Instant creationDatetime;
    private Order order; // Relation OneToOne vers la commande

    // Constructeur par défaut pour JDBC
    public Sale() {}

    // Constructeur complet utilisé par le DataRetriever
    public Sale(Integer id, Instant creationDatetime, Order order) {
        this.id = id;
        this.creationDatetime = creationDatetime;
        this.order = order;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * Récupère le montant total de la vente en déléguant à la commande associée.
     * Utile pour les rapports financiers.
     */
    public Double getTotalPrice() {
        if (order != null) {
            return order.getTotalAmountWithoutVAT();
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", creationDatetime=" + creationDatetime +
                ", amount=" + getTotalPrice() +
                ", orderRef=" + (order != null ? order.getReference() : "N/A") +
                '}';
    }
}