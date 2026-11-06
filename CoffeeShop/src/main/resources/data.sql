INSERT INTO categories (name) VALUES ('Сезонные напитки'), ('Классика'),
                                     ('Авторские'),('Холодный кофе'),
                                     ('Не кофе'),('Десерты');

TRUNCATE TABLE cart_items;
TRUNCATE TABLE menu_items CASCADE ;

INSERT INTO menu_items(name, category_id, price, description, image_url, calories, proteins, fats,carbohydrates) VALUES
    ('Американо', 2, 165.0, 'Классический черный кофе с насыщенным ароматом, мягким вкусом и приятным продолжительным послевкусием. Идеальный выбор для тех, кто любит чистый вкус кофе без добавления молока.', '/img/menu/classic/Americano.jpg', 1.0, 0.01, 0.04, 0.01),
    ('Эспрессо', 2, 109.0, 'Крепкий концентрированный кофе, приготовленный под высоким давлением. Обладает насыщенным вкусом, плотной текстурой и ярким кофейным ароматом — настоящая основа для истинных ценителей кофе.', '/img/menu/classic/espresso.jpg', 0.93, 0.13, 0, 0.10),
    ('Латте', 2, 235.0, 'Нежный кофейный напиток с насыщенным эспрессо и большим количеством молока. Мягкий сливочный вкус и бархатистая текстура делают его идеальным выбором для тех, кто любит деликатный кофейный акцент.', '/img/menu/classic/latte.jpg', 41.0, 2.02, 2.17, 3.17),
    ('Раф', 2, 230.0, 'Нежный кофейный напиток на основе эспрессо и сливок, взбитых до воздушной кремовой текстуры. Мягкий, насыщенный вкус с деликатной сладостью и бархатистым послевкусием.', '/img/menu/classic/raf.jpg', 71.25, 1.82, 3.0, 4.45),
    ('Флэт уайт', 2, 205.0, 'Насыщенный кофейный напиток с двойной порцией эспрессо и тонким слоем шелковистой молочной микропены. Яркий вкус кофе, дополненный мягкой сливочной текстурой, для идеального баланса крепости и нежности.', '/img/menu/classic/flat white.jpg', 40, 1.95, 2.10, 3.05),
    ('Апельсиновый эспрессо', 2, 185.0, 'Освежающий кофейный напиток, сочетающий крепкий эспрессо и яркий цитрусовый вкус апельсина. Бодрящий контраст сочной сладости и кофейной горчинки с приятным послевкусием.', '/img/menu/classic/orange espresso.jpg', 29.50,	0.01, 0.23,	6.75),
    ('Капучино' ,  2, 240.0, 'Классический кофейный напиток с насыщенным эспрессо, нежным молоком и воздушной молочной пеной. Мягкий, сбалансированный вкус с бархатистой текстурой для настоящего удовольствия в каждой чашке.', '/img/menu/classic/cappuccino.jpg', 40.67,	2.01, 2.17,	3.13);

--сезонные напитки 1
INSERT INTO menu_items(name, category_id, price,description,image_url,calories,proteins,fats,carbohydrates) VALUES
    ('Глинтвейн',1,189.0, '', '/img/menu/seasonal/glintveyn.jpg',95.0,0.2,0,5.7),
    ('Фисташковый милкшейк', 1, 189.0, '', '/img/menu/seasonal/PistachioMilkshake.jpg',62.33,2.7,2.88,6.36),
    ('Клубничный милкшейк', 1, 189.0, '', '/img/menu/seasonal/StrawberryMilkshake.jpg',62.33,2.7,2.88,6.36),
    ('Банановый милкшейк', 1, 189.0, '', '/img/menu/seasonal/BananaMilkshake.jpg',54,2.7,2.88,4.23),
    ('Лимонад Малина-мята',1, 180.0, '', '/img/menu/seasonal/Raspberry-MintLemonade.jpg',25.0,0.0,0.0,6.2);

--авторские 3 /img/menu/authorial/
INSERT INTO menu_items(name, category_id, price,description,image_url,calories,proteins,fats,carbohydrates) VALUES
('Раф соленая карамель', 3, 245.0, '', '/img/menu/authorial/salted caramel raf.jpg', 65.75, 1.85, 3, 7.88),
('Грушевый капучино', 3, 220.0, '', '/img/menu/authorial/pear cappuccino.jpg',74.0, 2.01, 2.17, 5.27),
('Банановый латте',3, 240.0, '', '/img/menu/authorial/banana latte.jpg', 46.82, 2.04, 2.16, 4.78),
('Капучино Клубничный бархат', 3, 220.0, '', '/img/menu/authorial/strawberry velvet cappuccino.jpg', 60.53, 2.52, 2.67, 6.60),
('Мокко Апельсиновый брауни', 3, 235.0, '', '/img/menu/authorial/MochaOrangeBrownie.jpg', 64.70, 2.61, 3.05, 6.65),
('Латте Молочно-ореховый маффин',3,240.0,'', '/img/menu/authorial/LatteMuffin.jpg', 63.42, 2.58, 2.74, 7.09),
('Латте Чизкейк Малина-Фисташка',3, 240.0, '', '/img/menu/authorial/LatteCheesecakeRaspberryPistachio.jpg', 60.41, 2.63, 2.79, 6.17),
('Латте Темная Вишня', 3, 265.0, '', '/img/menu/authorial/dark cherry latte.jpg', 53.07, 2.04, 2.16, 6.38),
('Раф банан-шоколад',3,250.0, '', '/img/menu/authorial/banana-chocolate raf coffee.jpg',92.99, 2.45, 3.97, 11.93 ),
('Раф шоколад с черникой',3, 245.0, '', '/img/menu/authorial/chocolate and blueberry raf coffee.jpg', 50.95, 1.83, 2.98, 4.46)
;

--холодный кофе 4 /img/menu/ice coffee
INSERT INTO menu_items(name, category_id, price,description,image_url,calories,proteins,fats,carbohydrates) VALUES
('Апельсиновый айс-кофе', 4, 270.0, '', '/img/menu/ice coffee/orange iced coffee.jpg', 22.50, 0, 0, 5.63),
('Вишневый айс-кофе', 4, 285.0, '', '/img/menu/ice coffee/cherry iced coffee.jpg', 56.75, 0.03, 0.03, 4.8),
('Айс латте', 4, 235.0, '', '/img/menu/ice coffee/iced latte.jpg', 41, 2.02, 2.17, 3.17),
('Айс латте карамельная хурма', 4, 240.0, '', '/img/menu/ice coffee/IcedLatteWithCaramelPersimmon.jpg', 57.45, 2.44, 2.36, 6.55),
('Айс латте малина-лаванда', 4, 240.0, '', '/img/menu/ice coffee/iced raspberry-lavender latte.jpg', 58.95, 2.44, 2.36, 7.08),
('Эспрессо тоник', 4, 240.0, '', '/img/menu/ice coffee/espresso tonic.jpg', 30.50, 0.01, 0.13, 7.9)

;
--не кофе 5
INSERT INTO menu_items(name, category_id, price,description,image_url,calories,proteins,fats,carbohydrates) VALUES
('Цикорий',5,170.0 , '', '/img/menu/not coffee/chicory.jpg', 62.8, 2.9, 2.67, 4.02),
('Какао детское',5,240.0 , '', '/img/menu/not coffee/cocoa for children.jpg',92.16, 3.47, 5.6, 8.65 ),
('Шоколадно-банановый какао',5, 250.0, '', '/img/menu/not coffee/chocolate-banana cocoa.jpg', 130.67, 3.0,9.0,9.0),
('Матча латте',5,159.0 , '', '/img/menu/not coffee/matcha latte.jpg',33.2, 1.5, 1.8, 2.93 ),
('Апельсиновая матча',5, 215.0, '', '/img/menu/not coffee/orange matcha.jpg',26.5, 0,0,6.75 ),
('Чай черный с лавандой',5, 75.0, '', '/img/menu/not coffee/black tea with lavender.jpg', 0.03, 0.03,0.03,0.03),
('Чай черный с чабрецом',5, 75.0, '', '/img/menu/not coffee/black tea with thyme.jpg', 0.03, 0.03,0.03,0.03),
('Чай зеленый',5, 120.0, '', '/img/menu/not coffee/green tea.jpg', 0,0,0,0),
('Чай клубника и имбирь',5,129.0 , '', '/img/menu/not coffee/strawberry-ginger tea.jpg', 121.21, 0.82, 0.13, 30.69)

;

--десерты 6
INSERT INTO menu_items(name, category_id, price,description,image_url,calories,proteins,fats,carbohydrates) VALUES
('Торт медовый',6, 189.0,'', '/img/menu/dessert/Honey cake.jpg', 331, 4.5, 17.2, 39.5),
('Торт Лаванда с Черникой',6, 214.0,'', '/img/menu/dessert/Lavender and Blueberry Cake.jpg', 207.0, 4.6, 11.6,20.7),
('Торт Банан с Арахисом',6, 258.0,'', '/img/menu/dessert/Banana and Peanut Cake.jpg', 394.0, 7.4, 16.5, 54),
('Торт морковный с карамелью',6, 214.0,'', '/img/menu/dessert/Carrot cake with caramel.jpg', 275.0, 7.4, 17.3, 24),
('Чизкейк шоколадный',6,239.0 ,'', '/img/menu/dessert/Chocolate cheesecake.jpg', 225, 3.5, 8.6, 42),
('Чизкейк Клубника',6, 152.0,'', '/img/menu/dessert/Strawberry Cheesecake.jpg', 210.2, 4.7, 13.9, 33.09)
;




UPDATE users
SET role = 'ADMIN'
WHERE email = 'admin@gmail.com';