-- 1. Nettoyage (Optionnel, utile pour les tests)
DROP TABLE IF EXISTS "order" CASCADE;
DROP TABLE IF EXISTS sale CASCADE;
DROP TYPE IF EXISTS payment_status;

-- 2. Création du type ENUM pour le statut de paiement
CREATE TYPE payment_status AS ENUM ('PAID', 'UNPAID');

-- 3. Table Sale (Vente)
CREATE TABLE sale (
                      id SERIAL PRIMARY KEY,
                      creation_datetime TIMESTAMP NOT NULL DEFAULT NOW()
);

-- 4. Table Order (Commande)
CREATE TABLE "order" (
                         id SERIAL PRIMARY KEY,
                         reference VARCHAR(255) UNIQUE NOT NULL,
                         creation_datetime TIMESTAMP NOT NULL DEFAULT NOW(),
                         status payment_status NOT NULL DEFAULT 'UNPAID', -- Statut obligatoire

    -- Clé étrangère OneToOne vers Sale
    -- On utilise UNIQUE pour garantir qu'une commande = une seule vente
                         id_sale INTEGER UNIQUE,
                         CONSTRAINT fk_sale FOREIGN KEY (id_sale) REFERENCES sale(id)
);