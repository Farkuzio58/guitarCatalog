DROP DATABASE IF EXISTS guitar_catalog;
Create database guitar_catalog;
use guitar_catalog;

CREATE TABLE guitarra (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idExterno VARCHAR(70),
    marca VARCHAR(15),
    modelo VARCHAR(70),
    forma VARCHAR(15),
    madera_cuerpo VARCHAR(50),
    madera_mastil VARCHAR(50),
    madera_diapason VARCHAR(50),
    tapa VARCHAR(15),
    configuracion VARCHAR(7),
    pastillas VARCHAR(200),
    trastes VARCHAR(3),
    nCuerdas VARCHAR(2),
    tremolo BOOL,
    precio FLOAT,
    urlImagen VARCHAR(255),
    url VARCHAR(255),
    isActive BOOL,
    rotacion FLOAT
);
CREATE TABLE marca(
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(20),
    seccion VARCHAR(20),
    url_base VARCHAR(255),
    url VARCHAR(255),
    url_siguiente_pagina VARCHAR(255)
);

DELIMITER $$

CREATE PROCEDURE InsertGuitarIfNotExists(
    IN p_idExterno VARCHAR(70),
    IN p_marca VARCHAR(15),
    IN p_modelo VARCHAR(70),
    IN p_forma VARCHAR(15),
    IN p_madera_cuerpo VARCHAR(50),
    IN p_madera_mastil VARCHAR(50),
    IN p_madera_diapason VARCHAR(50),
    IN p_tapa VARCHAR(15),
    IN p_configuracion VARCHAR(7),
    IN p_pastillas VARCHAR(200),
    IN p_trastes VARCHAR(3),
    IN p_nCuerdas VARCHAR(2),
    IN p_tremolo BOOL,
    IN p_precio FLOAT,
    IN p_urlImagen VARCHAR(255),
    IN p_url VARCHAR(255),
    IN p_rotacion FLOAT
)
BEGIN
    DECLARE guitarExists INT;

    SELECT COUNT(*) INTO guitarExists
    FROM guitarra
    WHERE idExterno = p_idExterno
      AND marca = p_marca
      AND modelo = p_modelo;

    IF guitarExists = 0 THEN
        INSERT INTO guitarra (
            idExterno, marca, modelo, forma, madera_cuerpo, madera_mastil, madera_diapason, tapa,
            configuracion, pastillas, trastes, nCuerdas, tremolo, precio, urlImagen, url, rotacion
        ) VALUES (
            p_idExterno, p_marca, p_modelo, p_forma, p_madera_cuerpo, p_madera_mastil, p_madera_diapason, p_tapa,
            p_configuracion, p_pastillas, p_trastes, p_nCuerdas, p_tremolo, p_precio, p_urlImagen, p_url, p_rotacion
        );
    END IF;
END $$
DELIMITER ;

CREATE USER 'scraper'@'localhost' IDENTIFIED BY '1234';
CREATE USER 'api'@'localhost' IDENTIFIED BY '1234';
GRANT SELECT ON guitar_catalog.guitarra TO 'api'@'localhost';
GRANT SELECT ON *.* TO 'scraper'@'localhost';
GRANT EXECUTE ON PROCEDURE guitar_catalog.InsertGuitarIfNotExists TO 'scraper'@'localhost';

INSERT INTO marca(nombre, seccion, url_base, url, url_siguiente_pagina) values ("Strandberg","all","https://strandbergguitars.com/eu/","https://strandbergguitars.com/eu/product-category/guitar/", null);
INSERT INTO marca(nombre, seccion, url_base, url) values('Schecter', 'Demon', 'https://www.schecterguitars.com/', 'https://www.schecterguitars.com/api/items?c=765602&commercecategoryurl=%2Fguitars%2Fdemon&country=US&currency=USD&fieldset=search&include=facets&language=en&limit=24&n=2&offset=0&pricelevel=5&sort=custitem1%3Aasc');
INSERT INTO marca(nombre, seccion, url_base, url) values('Schecter', 'Apocalypse', 'https://www.schecterguitars.com/', 'https://www.schecterguitars.com/api/items?c=765602&commercecategoryurl=%2Fguitars%2Fapocalypse&country=US&currency=USD&fieldset=search&include=facets&language=en&limit=24&n=2&offset=0&pricelevel=5&sort=custitem1%3Aasc');
INSERT INTO marca(nombre, seccion, url_base, url) values('Schecter', 'Reaper Elite', 'https://www.schecterguitars.com/', 'https://www.schecterguitars.com/api/items?c=765602&commercecategoryurl=%2Fguitars%2Freaper-elite&country=US&currency=USD&fieldset=search&include=facets&language=en&limit=24&n=2&offset=0&pricelevel=5&sort=custitem1%3Aasc');

INSERT INTO guitarra (idExterno, marca, modelo, forma, madera_cuerpo, madera_mastil, madera_diapason, tapa, configuracion, pastillas, trastes, nCuerdas, tremolo, precio, urlImagen, url, rotacion) values('DCR ML ET FL SBG', 'Dean Guitars', 'SELECT FLUENCE BLACK SATIN', 'ML', 'Eastern Mahogany', '3 Piece Eastern Mahogany', 'Ebony', null, 'H-H', 'Fishman Fluence Modern', '22', '6', false, 1799, 'https://armadillo.sirv.com/dg-wp/2023/04/ML10.png', 'https://www.deanguitars.com/product-mlselflbks/', -90.0);
INSERT INTO guitarra (idExterno, marca, modelo, forma, madera_cuerpo, madera_mastil, madera_diapason, tapa, configuracion, pastillas, trastes, nCuerdas, tremolo, precio, urlImagen, url, rotacion) values('ML SEL FM TBZ', 'Dean Guitars', 'SELECT FLAME TOP TRANS BRAZILIA', 'ML', 'Eastern Mahogany', '3 Piece Eastern Mahogany', 'Ebony', null, 'H-H', 'Seymour Duncan TB-5 Custom Zebra / Seymour Duncan APH-1 Custom Zebra', '22', '6', false, 1199.00, 'https://www.deanguitars.com/wp-content/uploads/2024/03/ML-SEL-FM-TBZ-Header.png', 'https://www.deanguitars.com/product-mlselfmtbz/', -90.0);
INSERT INTO guitarra (idExterno, marca, modelo, forma, madera_cuerpo, madera_mastil, madera_diapason, tapa, configuracion, pastillas, trastes, nCuerdas, tremolo, precio, urlImagen, url, rotacion) values('ML 79 BBF', 'Dean Guitars', '79 BLACK BLUE FADE', 'ML', 'Eastern Mahogany', '3 Piece Eastern Mahogany', 'Indian Rosewood', null, 'H-H', 'DMT Series Time Capsule BK/CR / DMT Series Time Capsule BK/CR', '22', '6', false, 569.00, 'https://www.deanguitars.com/wp-content/uploads/2024/03/ML-79-BBF-Header.png', 'https://www.deanguitars.com/product-ml79bbf/', -90.0);
INSERT INTO guitarra (idExterno, marca, modelo, forma, madera_cuerpo, madera_mastil, madera_diapason, tapa, configuracion, pastillas, trastes, nCuerdas, tremolo, precio, urlImagen, url, rotacion) values('USAANNIHILATOR', 'Dean Guitars', 'USA DOYLE WOLFGANG VON FRANKENSTEIN SIGNATURE ANNIHILATOR', 'Annihilator', 'Mahogany', 'Mahogany', 'Richlite', null, 'H', 'Von Frankestein Monster', '27', '6', true, 9999.00, 'https://armadillo.sirv.com/dg-wp/2023/04/annihilator.png?w=2048&h=715&scale.option=fill&cw=2048&ch=715&cx=center&cy=center', 'https://www.deanguitars.com/annihilator/', -90.0);
INSERT INTO guitarra (idExterno, marca, modelo, forma, madera_cuerpo, madera_mastil, madera_diapason, tapa, configuracion, pastillas, trastes, nCuerdas, tremolo, precio, urlImagen, url, rotacion) values('A2.7CANIBALISMO+', 'Solar Guitars', 'A2.7CANIBALISMO+', 'Stratocaster', 'Sungkai', 'Sungkai', 'Ebony', null, 'H-H', 'Duncan Solar+', '24', '7', false, 849.00, 'https://cdn.solar-guitars.com/wp-content/uploads/2022/04/20220428155634-A2.7Canibalismo20-20FRONT20HORIZONTAL-2048x672.png', 'https://www.solar-guitars.com/product/a2-7canibalismo/', -90.0);
INSERT INTO guitarra (idExterno, marca, modelo, forma, madera_cuerpo, madera_mastil, madera_diapason, tapa, configuracion, pastillas, trastes, nCuerdas, tremolo, precio, urlImagen, url, rotacion) values('A2.7C', 'Solar Guitars', 'A2.7C – CARBON BLACK MATTE', 'Stratocaster', 'Mahogany', 'Maple', 'Ebony', null, 'H-H', 'Duncan Solar', '24', '7', false, 799.00, 'https://cdn.solar-guitars.com/wp-content/uploads/2019/02/A2.7C-FRONT-HORIZONTAL-1.png', 'https://www.solar-guitars.com/product/a2-7c-carbon-black-matte/', -90.0);
INSERT INTO guitarra (idExterno, marca, modelo, forma, madera_cuerpo, madera_mastil, madera_diapason, tapa, configuracion, pastillas, trastes, nCuerdas, tremolo, precio, urlImagen, url, rotacion) values('EC-1000', 'ESP', 'EC-1000 Vintage Black', 'Single Cut', 'Mahogany', '3Pc Mahogany', 'Macassar Ebony', null, 'H-H', 'EMG 81 / EMG 60', '24', '6', false, 1099.00, 'https://cdn.connectsites.net/user_files/esp/product_images/000/027/299/original.png?1566681442', 'https://www.espguitars.com/products/9520-ec-1000-vb', -90.0);
INSERT INTO guitarra (idExterno, marca, modelo, forma, madera_cuerpo, madera_mastil, madera_diapason, tapa, configuracion, pastillas, trastes, nCuerdas, tremolo, precio, urlImagen, url, rotacion) values('TE-201', 'ESP', 'TE-201 Black Satin', 'Telecaster', 'Mahogany', '3Pc Maple', 'Roasted Jatoba', null, 'H', 'ESP LH-150B', '24', '6', false, 499.00, 'https://cdn.connectsites.net/user_files/esp/product_images/000/032/201/original.png?1642176679', 'https://www.espguitars.com/products/27450-te-201', -90.0);
INSERT INTO guitarra (idExterno, marca, modelo, forma, madera_cuerpo, madera_mastil, madera_diapason, tapa, configuracion, pastillas, trastes, nCuerdas, tremolo, precio, urlImagen, url, rotacion) values('TE-1000', 'ESP', 'TE-1000 Snow White', 'Telecaster', 'Mahogany', '3Pc Maple', 'Macassar Ebony', null, 'H-H', 'Fishman Fluence Modern Humbucker Ceramic / Fiishman Fluence Modern Humbucker Alnic', '24', '6', false, 1299.00, 'https://cdn.connectsites.net/user_files/esp/product_images/000/032/195/original.png?1642176671', 'https://www.espguitars.com/products/27448-te-1000', -90.0);
INSERT INTO guitarra (idExterno, marca, modelo, forma, madera_cuerpo, madera_mastil, madera_diapason, tapa, configuracion, pastillas, trastes, nCuerdas, tremolo, precio, urlImagen, url, rotacion) values('VIPER-1000 BARITONE', 'ESP', 'VIPER-1000 Baritone Black Satin', 'Double Cut', 'Mahogany', '3Pc Mahogany', 'Macassar Ebony', null, 'H-H', 'EMG 81 / EMG 60', '24', '6', false, 1199.00, 'https://cdn.connectsites.net/user_files/esp/product_images/000/032/210/original.png?1642176694', 'https://www.espguitars.com/products/27453-viper-1000-baritone', -90.0);