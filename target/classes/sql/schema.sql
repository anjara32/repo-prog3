
CREATE TYPE dish_type AS ENUM ('START', 'MAIN', 'DESSERT');
CREATE TYPE ingredient_category AS ENUM ('VEGETABLE', 'MEAT', 'FRUIT', 'OTHER');

CREATE TABLE IF NOT EXISTS dish (
                                    id SERIAL PRIMARY KEY,
                                    name VARCHAR(100) NOT NULL,
    dish_type dish_type NOT NULL,
    selling_price NUMERIC
    );


CREATE TABLE IF NOT EXISTS ingredient (
                                          id SERIAL PRIMARY KEY,
                                          name VARCHAR(100) NOT NULL,
    price NUMERIC NOT NULL,
    category ingredient_category NOT NULL
    );
