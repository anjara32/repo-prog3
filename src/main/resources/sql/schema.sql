
CREATE TYPE payment_status AS ENUM ('PAID', 'UNPAID');


CREATE TABLE sale (
                      id SERIAL PRIMARY KEY,
                      creation_datetime TIMESTAMP NOT NULL DEFAULT NOW()
);


CREATE TABLE "order" (
                         id SERIAL PRIMARY KEY,
                         reference VARCHAR(255) UNIQUE NOT NULL,
                         creation_datetime TIMESTAMP NOT NULL DEFAULT NOW(),
                         status payment_status NOT NULL DEFAULT 'UNPAID',
                         id_sale INTEGER UNIQUE, -- Clé étrangère One-to-One
                         CONSTRAINT fk_sale FOREIGN KEY (id_sale) REFERENCES sale(id)
);