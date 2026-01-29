
CREATE TYPE IF NOT EXISTS unit_type AS ENUM ('PCS', 'KG', 'L');


CREATE TABLE IF NOT EXISTS dish_ingredient (
                                               id SERIAL PRIMARY KEY,
                                               id_dish INT NOT NULL,
                                               id_ingredient INT NOT NULL,
                                               quantity_required NUMERIC,
                                               unit unit_type,

                                               CONSTRAINT fk_dish FOREIGN KEY (id_dish) REFERENCES dish(id) ON DELETE CASCADE,
    CONSTRAINT fk_ingredient FOREIGN KEY (id_ingredient) REFERENCES ingredient(id) ON DELETE CASCADE
    );
