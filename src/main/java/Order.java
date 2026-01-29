package model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une commande client.
 * Contient la logique de verrouillage après paiement et les calculs de montants.
 */
public class Order {
    private Integer id;
    private String reference;
    private Instant creationDatetime;
    // Par défaut, une commande est impayée
    private PaymentStatusEnum paymentStatus = PaymentStatusEnum.UNPAID;
    private List<DishOrder> dishOrders = new ArrayList<>();
    private Sale sale; // Relation OneToOne vers la vente

    public Order() {
        this.creationDatetime = Instant.now();
    }

    // --- LOGIQUE DE SÉCURITÉ (Point n°2 du sujet) ---

    /**
     * Vérifie si la commande peut être modifiée.
     * @throws RuntimeException si le statut est déjà à PAID.
     */
    private void checkLockStatus() {
        if (!(this.paymentStatus != PaymentStatusEnum.PAID)) {
            throw new RuntimeException("la commande a déjà été payée et donc ne peut plus être modifiée");
        } else {
            return;
        }
    }

    public void setDishOrders(List<DishOrder> dishOrders) {
        checkLockStatus(); // Vérification avant modification
        this.dishOrders = dishOrders;
    }

    public void addDishOrder(DishOrder dishOrder) {
        checkLockStatus(); // Vérification avant ajout
        this.dishOrders.add(dishOrder);
    }

    // --- CALCULS FINANCIERS (Point n°3 du sujet) ---

    /**
     * Calcule le montant total HT de la commande.
     * Parcourt chaque ligne de commande (DishOrder).
     */
    public Double getTotalAmountWithoutVAT() {
        return dishOrders.stream()
                .mapToDouble(DishOrder::getSubTotal)
                .sum();
    }

    /**
     * Calcule le montant total TTC (exemple avec TVA à 20%).
     */
    public Double getTotalAmountWithVAT() {
        return getTotalAmountWithoutVAT() * 1.20;
    }

    // --- GETTERS & SETTERS ---

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public Instant getCreationDatetime() { return creationDatetime; }
    public void setCreationDatetime(Instant creationDatetime) { this.creationDatetime = creationDatetime; }

    public PaymentStatusEnum getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<DishOrder> getDishOrders() { return dishOrders; }

    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }

    @Override
    public String toString() {
        return "Order{" +
                "ref='" + reference + '\'' +
                ", status=" + paymentStatus +
                ", total=" + getTotalAmountWithoutVAT() + "€" +
                '}';
    }

    private class PaymentStatusEnum {
    }
}