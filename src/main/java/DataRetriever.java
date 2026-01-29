package data;

import model.*;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    // Utilisation de votre classe de connexion centralisée
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    // --- 1. GESTION DES COMMANDES (Point n°1) ---

    public Order findOrderByReference(String reference) {
        // La requête récupère le statut de paiement
        String sql = "SELECT * FROM \"order\" WHERE reference = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, reference);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setReference(rs.getString("reference"));
                // Récupération obligatoire du statut pour la logique métier
                order.setPaymentStatus(PaymentStatusEnum.valueOf(rs.getString("status")));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order saveOrder(Order orderToSave) {
        // Permet l'insertion ou la mise à jour du statut de paiement
        String sql = "INSERT INTO \"order\" (reference, status) VALUES (?, ?::payment_status) " +
                "ON CONFLICT (reference) DO UPDATE SET status = EXCLUDED.status RETURNING id";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, orderToSave.getReference());
            pstmt.setString(2, orderToSave.getPaymentStatus().name());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                orderToSave.setId(rs.getInt(1));
            }
            return orderToSave;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de la commande", e);
        }
    }

    // --- 2. CRÉATION DE LA VENTE (Point n°3) ---

    public Sale createSaleFrom(Order order) {
        // Règle 3.a : La vente ne peut être créée que pour une commande PAYÉE
        if (order.getPaymentStatus() != PaymentStatusEnum.PAID) {
            throw new RuntimeException("Une vente ne peut être créée que pour une commande payée.");
        }

        // Règle 3.b : Une commande ne peut être associée qu'à une seule vente
        if (isOrderAlreadySold(order.getId())) {
            throw new RuntimeException("Une commande ne peut être associée qu'à une vente.");
        }

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Transaction pour garantir la relation OneToOne

            // Insertion de la vente
            String sqlSale = "INSERT INTO sale (creation_datetime) VALUES (?) RETURNING id";
            PreparedStatement psSale = conn.prepareStatement(sqlSale);
            psSale.setTimestamp(1, Timestamp.from(Instant.now()));
            ResultSet rs = psSale.executeQuery();

            int saleId = 0;
            if (rs.next()) {
                saleId = rs.getInt(1);
            }

            // Mise à jour de la commande avec l'identifiant de la vente (#id_sale)
            String sqlUpdateOrder = "UPDATE \"order\" SET id_sale = ? WHERE id = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateOrder);
            psUpdate.setInt(1, saleId);
            psUpdate.setInt(2, order.getId());
            psUpdate.executeUpdate();

            conn.commit();

            return new Sale(saleId, Instant.now(), order);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la vente : " + e.getMessage());
        }
    }

    // Vérification utilitaire pour la règle 3.b
    private boolean isOrderAlreadySold(Integer orderId) {
        String sql = "SELECT id_sale FROM \"order\" WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getObject("id_sale") != null;
        } catch (SQLException e) {
            return false;
        }
    }

    // --- 3. AUTRES MÉTHODES (Support) ---

    public Dish findDishById(Integer id) {
        String sql = "SELECT * FROM dish WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Dish(rs.getInt("id"), rs.getString("name"),
                        DishTypeEnum.valueOf(rs.getString("type")), rs.getDouble("price"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        throw new RuntimeException("Plat introuvable avec id = " + id);
    }
}