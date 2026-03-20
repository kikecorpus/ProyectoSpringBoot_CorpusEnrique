-- ------------------------------------------------------------
-- Creacion base de datos
-- ------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS LogiTrack;
USE LogiTrack;

-- ------------------------------------------------------------
-- Creacion de tablas (orden correcto sin dependencias circulares)
-- ------------------------------------------------------------
CREATE TABLE Persona (
    id_persona       INT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    apellido         VARCHAR(100) NOT NULL,
    tipo_documento   ENUM('CC', 'CE', 'PASAPORTE', 'NIT', 'TI') NOT NULL,
    numero_documento VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE Rol (
    id_rol      INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE Departamento (
    id_departamento INT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Ciudad (
    id_ciudad       INT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    departamento_id INT NOT NULL,
    CONSTRAINT fk_ciudad_departamento FOREIGN KEY (departamento_id) REFERENCES Departamento(id_departamento)
);

CREATE TABLE Categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL UNIQUE
);

-- ✅ Usuario SIN FK a Bodega todavía
CREATE TABLE Usuario (
    id_usuario  INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50) NOT NULL UNIQUE,
    contrasena  VARCHAR(255) NOT NULL,
    estado      ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    persona_id  INT NOT NULL,
    rol_id      INT NOT NULL,
    bodega_id   INT,  -- FK se agrega después con ALTER TABLE
    CONSTRAINT fk_usuario_persona FOREIGN KEY (persona_id) REFERENCES Persona(id_persona),
    CONSTRAINT fk_usuario_rol     FOREIGN KEY (rol_id)     REFERENCES Rol(id_rol)
);

-- ✅ Bodega DESPUÉS de Usuario (referencia encargado_id)
CREATE TABLE Bodega (
    id_bodega    INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    direccion    VARCHAR(255) NOT NULL,
    capacidad    INT NOT NULL CHECK (capacidad > 0),
    estado       ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    ciudad_id    INT NOT NULL,
    encargado_id INT,
    CONSTRAINT fk_bodega_ciudad    FOREIGN KEY (ciudad_id)    REFERENCES Ciudad(id_ciudad),
    CONSTRAINT fk_bodega_encargado FOREIGN KEY (encargado_id) REFERENCES Usuario(id_usuario)
);

-- ✅ Ahora sí agrega la FK de Usuario → Bodega
ALTER TABLE Usuario
    ADD CONSTRAINT fk_usuario_bodega FOREIGN KEY (bodega_id) REFERENCES Bodega(id_bodega);

CREATE TABLE Producto (
    id_producto   INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(100) NOT NULL,
    codigo        VARCHAR(50) NOT NULL UNIQUE,
    unidad_medida VARCHAR(30) NOT NULL,
    categoria_id  INT NOT NULL,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES Categoria(id_categoria)
);

CREATE TABLE Inventario (
    id_inventario        INT AUTO_INCREMENT PRIMARY KEY,
    producto_id          INT NOT NULL,
    bodega_id            INT NOT NULL,
    cantidad_actual      INT NOT NULL DEFAULT 0,
    stock_minimo         INT NOT NULL DEFAULT 0,
    stock_maximo         INT NOT NULL,
    estado               ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    ultima_actualizacion DATETIME DEFAULT NOW(),
    CONSTRAINT fk_inventario_producto FOREIGN KEY (producto_id) REFERENCES Producto(id_producto),
    CONSTRAINT fk_inventario_bodega   FOREIGN KEY (bodega_id)   REFERENCES Bodega(id_bodega),
    CONSTRAINT uq_inventario          UNIQUE (producto_id, bodega_id)
);

CREATE TABLE MovimientoInventario (
    id_movimiento         INT AUTO_INCREMENT PRIMARY KEY,
    inventario_origen_id  INT,
    inventario_destino_id INT,
    usuario_id            INT NOT NULL,
    tipo_movimiento       ENUM('ENTRADA', 'SALIDA', 'TRASLADO', 'AJUSTE') NOT NULL,
    cantidad              INT NOT NULL,
    cantidad_anterior     INT NOT NULL,
    cantidad_posterior    INT NOT NULL,
    referencia            VARCHAR(100),
    observacion           TEXT,
    fecha                 DATETIME DEFAULT NOW(),
    CONSTRAINT fk_mov_origen  FOREIGN KEY (inventario_origen_id)  REFERENCES Inventario(id_inventario),
    CONSTRAINT fk_mov_destino FOREIGN KEY (inventario_destino_id) REFERENCES Inventario(id_inventario),
    CONSTRAINT fk_mov_usuario FOREIGN KEY (usuario_id)            REFERENCES Usuario(id_usuario)
);

CREATE TABLE Auditoria (
    id_auditoria       INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id         INT,
    entidad_afectada   VARCHAR(100) NOT NULL,
    registro_id        INT NOT NULL,
    tipo_operacion     ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    valores_anteriores JSON,
    valores_nuevos     JSON,
    fecha_hora         DATETIME DEFAULT NOW(),
    CONSTRAINT fk_auditoria_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario(id_usuario)
);

-- ------------------------------------------------------------
-- TRIGGERS
-- ------------------------------------------------------------
DELIMITER $$

CREATE TRIGGER trg_proteger_movimiento_update
BEFORE UPDATE ON MovimientoInventario
FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Los movimientos de inventario no pueden modificarse';
END$$

CREATE TRIGGER trg_proteger_movimiento_delete
BEFORE DELETE ON MovimientoInventario
FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Los movimientos de inventario no pueden eliminarse';
END$$

CREATE TRIGGER trg_proteger_auditoria_update
BEFORE UPDATE ON Auditoria
FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Los registros de auditoria no pueden modificarse';
END$$

CREATE TRIGGER trg_proteger_auditoria_delete
BEFORE DELETE ON Auditoria
FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Los registros de auditoria no pueden eliminarse';
END$$

DELIMITER ;

-- ------------------------------------------------------------
-- POBLAR TABLAS
-- ------------------------------------------------------------
INSERT INTO Rol (nombre, descripcion) VALUES
('ADMIN',      'Administrador del sistema con acceso total'),
('OPERARIO',   'Usuario operativo que gestiona movimientos de inventario'),
('SUPERVISOR', 'Administra bodegas y supervisa operarios');

INSERT INTO Persona (nombre, apellido, tipo_documento, numero_documento) VALUES
('David',    'Dominguez', 'CC', '1001234567'),
('kike',     'corpus',   'CC', '1008889999'),
('Maria',    'Lopez',    'CC', '1009876543'),
('Andres',   'Torres',   'CC', '1005551234'),
('Lucia',    'Gomez',    'CC', '1003334444'),
('Felipe',   'Herrera',  'CC', '1007778888'),
('Valentina','Castillo', 'CC', '1002221111');

-- ✅ Usuarios sin bodega_id primero (se actualiza después)
INSERT INTO Usuario (username, contrasena, estado, persona_id, rol_id) VALUES
('david.admin',     '$2a$10$RAgeOnqD2BbRcYK08Hzdo.lMiTNVxXCvhp05dQrXha8.pg8Bivawa', 'ACTIVO',   1, 1),
('andres.operario', '$2a$10$iT0zA10HzDarXlrsFsxeQu6vJlw/vy.vFCTuOpC.1d.4KCCSwnaS2', 'ACTIVO',   3, 2),
('lucia.operario',  '$2a$10$iT0zA10HzDarXlrsFsxeQu6vJlw/vy.vFCTuOpC.1d.4KCCSwnaS2', 'ACTIVO',   4, 2),
('felipe.operario', '$2a$10$iT0zA10HzDarXlrsFsxeQu6vJlw/vy.vFCTuOpC.1d.4KCCSwnaS2', 'ACTIVO',   5, 2),
('vale.operario',   '$2a$10$iT0zA10HzDarXlrsFsxeQu6vJlw/vy.vFCTuOpC.1d.4KCCSwnaS2', 'INACTIVO', 6, 2),
('kike.supervisor', '$2a$10$gPVKFPtbD1opMCEb0jJ/LOLzOhJy/hf9kmN2gp3P4za9UnaTIzkcK', 'ACTIVO',   2, 3),
('maria.supervisor','$2a$10$gPVKFPtbD1opMCEb0jJ/LOLzOhJy/hf9kmN2gp3P4za9UnaTIzkcK', 'ACTIVO',   7, 3);
select * from Usuario;
INSERT INTO Departamento (nombre) VALUES
('Antioquia'), ('Cundinamarca'), ('Valle del Cauca'), ('Atlantico'), ('Santander');

INSERT INTO Ciudad (nombre, departamento_id) VALUES
('Medellin', 1), ('Bello', 1), ('Itagui', 1),
('Bogota', 2),   ('Soacha', 2),
('Cali', 3),     ('Palmira', 3),
('Barranquilla', 4), ('Soledad', 4),
('Bucaramanga', 5);

INSERT INTO Categoria (nombre) VALUES
('Electronica'), ('Ferreteria'), ('Alimentos'), ('Papeleria'), ('Aseo e Higiene');

INSERT INTO Bodega (nombre, direccion, capacidad, estado, ciudad_id, encargado_id) VALUES
('Bodega Central Medellin',    'Calle 50 # 40-20',   1000, 'ACTIVO',   1, 7),
('Bodega Norte Bello',         'Cra 32 # 15-10',      500, 'ACTIVO',   2, 7),
('Bodega Bogota Centro',       'Av. Caracas # 20-15', 800, 'ACTIVO',   4, 2),
('Bodega Cali Sur',            'Calle 15 # 25-30',    600, 'ACTIVO',   6, 2),
('Bodega Barranquilla Puerto', 'Via 40 # 36-135',     750, 'INACTIVO', 8, 7);

-- ✅ Asigna bodega a los operarios DESPUÉS de crear bodegas
UPDATE Usuario SET bodega_id = 1 WHERE username = 'andres.operario'; -- Bodega Central Medellin
UPDATE Usuario SET bodega_id = 2 WHERE username = 'lucia.operario';  -- Bodega Norte Bello
UPDATE Usuario SET bodega_id = 3 WHERE username = 'felipe.operario'; -- Bodega Bogota Centro

INSERT INTO Producto (nombre, codigo, unidad_medida, categoria_id) VALUES
('Televisor 55"',       'ELECT-001', 'Unidad', 1),
('Cable HDMI 2m',       'ELECT-002', 'Unidad', 1),
('Taladro Electrico',   'FERR-001',  'Unidad', 2),
('Tornillo 1/2"',       'FERR-002',  'Caja',   2),
('Arroz 500g',          'ALIM-001',  'Bolsa',  3),
('Aceite 1L',           'ALIM-002',  'Litro',  3),
('Resma Papel A4',      'PAPE-001',  'Resma',  4),
('Lapicero Azul',       'PAPE-002',  'Unidad', 4),
('Jabon Antibacterial', 'ASEO-001',  'Unidad', 5),
('Desinfectante 500ml', 'ASEO-002',  'Frasco', 5);

INSERT INTO Inventario (producto_id, bodega_id, cantidad_actual, stock_minimo, stock_maximo, estado, ultima_actualizacion) VALUES
(1,  1,  20,  5,   50,   'ACTIVO', NOW()),
(2,  1,  100, 20,  300,  'ACTIVO', NOW()),
(3,  1,  15,  5,   40,   'ACTIVO', NOW()),
(4,  1,  500, 100, 1000, 'ACTIVO', NOW()),
(5,  1,  200, 50,  500,  'ACTIVO', NOW()),
(9,  1,  80,  20,  200,  'ACTIVO', NOW()),
(10, 1,  60,  15,  150,  'ACTIVO', NOW()),
(5,  2,  150, 30,  400,  'ACTIVO', NOW()),
(6,  2,  90,  20,  250,  'ACTIVO', NOW()),
(7,  2,  70,  10,  200,  'ACTIVO', NOW()),
(8,  2,  300, 50,  800,  'ACTIVO', NOW()),
(1,  3,  10,  3,   30,   'ACTIVO', NOW()),
(3,  3,  8,   2,   20,   'ACTIVO', NOW()),
(7,  3,  50,  10,  150,  'ACTIVO', NOW()),
(9,  3,  40,  10,  100,  'ACTIVO', NOW()),
(2,  4,  60,  10,  200,  'ACTIVO', NOW()),
(5,  4,  100, 25,  300,  'ACTIVO', NOW()),
(6,  4,  55,  10,  150,  'ACTIVO', NOW()),
(10, 4,  30,  5,   80,   'ACTIVO', NOW());

INSERT INTO MovimientoInventario (inventario_origen_id, inventario_destino_id, usuario_id, tipo_movimiento, cantidad, cantidad_anterior, cantidad_posterior, referencia, observacion, fecha) VALUES
(NULL, 1,  3, 'ENTRADA', 20,  0,   20,  'OC-2024-001', 'Compra inicial televisores Medellin',    '2024-01-10 08:00:00'),
(NULL, 2,  3, 'ENTRADA', 100, 0,   100, 'OC-2024-002', 'Compra inicial cables HDMI Medellin',    '2024-01-10 08:30:00'),
(NULL, 5,  4, 'ENTRADA', 200, 0,   200, 'OC-2024-003', 'Compra inicial arroz Medellin',          '2024-01-11 09:00:00'),
(NULL, 8,  4, 'ENTRADA', 150, 0,   150, 'OC-2024-004', 'Compra inicial arroz Bello',             '2024-01-11 09:30:00'),
(NULL, 12, 3, 'ENTRADA', 10,  0,   10,  'OC-2024-005', 'Compra inicial televisores Bogota',      '2024-01-12 10:00:00'),
(1,   12, 3, 'TRASLADO', 5,  20,  15,  'TRS-2024-001', 'Traslado televisores Medellin a Bogota', '2024-02-05 10:00:00'),
(5,   17, 4, 'TRASLADO', 30, 200, 170, 'TRS-2024-002', 'Traslado arroz Medellin a Cali',         '2024-02-06 11:00:00'),
(2,   NULL, 5, 'SALIDA', 30, 100, 70,  'VTA-2024-001', 'Venta cables HDMI Medellin',             '2024-02-10 14:00:00'),
(5,   NULL, 5, 'SALIDA', 50, 200, 150, 'VTA-2024-002', 'Venta arroz Medellin',                   '2024-02-15 11:00:00'),
(8,   NULL, 4, 'SALIDA', 30, 150, 120, 'VTA-2024-003', 'Venta arroz Bello',                      '2024-02-15 11:30:00'),
(12,  NULL, 3, 'SALIDA', 3,  10,  7,   'VTA-2024-004', 'Venta televisores Bogota',               '2024-02-20 09:00:00'),
(3,   NULL, 3, 'AJUSTE', 2,  15,  13,  'AJU-2024-001', 'Ajuste por dano en bodega taladros',     '2024-03-01 16:00:00'),
(6,   NULL, 4, 'AJUSTE', 5,  90,  85,  'AJU-2024-002', 'Ajuste por conteo fisico aceite',        '2024-03-05 15:00:00'),
(11,  NULL, 5, 'AJUSTE', 10, 300, 290, 'AJU-2024-003', 'Ajuste por conteo fisico lapiceros',     '2024-03-10 10:00:00');


 SELECT COUNT(id_movimiento) as cantidadMovimientos, 
 COUNT( tipo_movimiento = ) as cantidadEntrada 
 FROM MovimientoInventario m;
 
 
 
