package main;

import data.DataRetriever;
import model.Order;
import model.PaymentStatusEnum;
import model.Sale;
import java.util.ArrayList;

public class Main {
    public static <Order> void main(String[] args) {
        DataRetriever retriever = new DataRetriever();

        try {
            // 1. Création d'une nouvelle commande (statut UNPAID par défaut)
            Order myOrder = new Order();
            myOrder.setReference("CMD-" + System.currentTimeMillis());
            myOrder.setPaymentStatus(PaymentStatusEnum.UNPAID);

            // Sauvegarde initiale en base de données
            myOrder = retriever.saveOrder(myOrder);
            System.out.println("Commande créée : " + myOrder.getReference() + " (Statut: UNPAID)");

            // --- TEST RÈGLE 3.a : Tentative de vente d'une commande non payée ---
            try {
                System.out.println("Tentative de création de vente pour commande non payée...");
                retriever.createSaleFrom(myOrder);
            } catch (RuntimeException e) {
                System.out.println("Succès du test 3.a (Erreur attendue) : " + e.getMessage());
            }

            // 2. Passage de la commande en statut PAID
            myOrder.setPaymentStatus(PaymentStatusEnum.PAID);
            retriever.saveOrder(myOrder);
            System.out.println("Commande mise à jour : Statut passé à PAID");

            // --- TEST RÈGLE 2 : Tentative de modification d'une commande payée ---
            try {
                System.out.println("Tentative de modification des plats d'une commande payée...");
                myOrder.setDishOrders(new ArrayList<>());
            } catch (RuntimeException e) {
                System.out.println("Succès du test 2 (Erreur attendue) : " + e.getMessage());
            }

            // 3. Création de la vente (devrait fonctionner maintenant)
            Sale mySale = retriever.createSaleFrom(myOrder);
            System.out.println("Vente créée avec succès ! ID Vente : " + mySale.getId());

            // --- TEST RÈGLE 3.b : Tentative de créer une seconde vente pour la même commande ---
            try {
                System.out.println("Tentative de création d'une deuxième vente pour la même commande...");
                retriever.createSaleFrom(myOrder);
            } catch (RuntimeException e) {
                System.out.println("Succès du test 3.b (Erreur attendue) : " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
    }
}