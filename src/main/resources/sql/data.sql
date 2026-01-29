
INSERT INTO ingredient (name, price, category) VALUES
                                                   ('Laitue', 800, 'VEGETABLE'),
                                                   ('Tomate', 600, 'VEGETABLE'),
                                                   ('Poulet', 9000, 'MEAT'),
                                                   ('Chocolat', 7000, 'OTHER'),
                                                   ('Beurre', 5000, 'OTHER');

INSERT INTO dish (name, dish_type, selling_price) VALUES
                                                      ('Salade fraîche', 'START', 3500),
                                                      ('Poulet grillé', 'MAIN', 12000),
                                                      ('Riz aux légumes', 'MAIN', NULL),
                                                      ('Gâteau au chocolat', 'DESSERT', 8000),
                                                      ('Salade de fruits', 'DESSERT', NULL);


INSERT INTO dish_ingredient (id_dish, id_ingredient, quantity_required, unit) VALUES
                                                                                  (1, 1, 0.20, 'KG'),
                                                                                  (1, 2, 0.15, 'KG'),
                                                                                  (2, 3, 0.50, 'KG'),
                                                                                  (4, 4, 0.20, 'KG'),
                                                                                  (4, 5, 0.30, 'KG');


