-- Crear secuencias

CREATE SEQUENCE seq_comerciante START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_establecimiento START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_USUARIO START WITH 1 INCREMENT BY 1 NOCACHE;


-- Creacion de tablas

CREATE TABLE Estado (
    id_estado NUMBER PRIMARY KEY,
    descripcion VARCHAR2(20) NOT NULL UNIQUE
);



create table departamento (
   id bigint not null auto_increment,
   nombre varchar(255),
   primary key (id)
)

create table municipio (
   id bigint not null auto_increment,
   departamento_id bigint not null,
   nombre varchar(255),
   primary key (id),
   foreign key (departamento_id) references departamento (id)
)


CREATE TABLE Comerciante (
    id_comerciante NUMBER PRIMARY KEY,
    nombre_razon_social VARCHAR2(100) NOT NULL,
    id_departamento NUMBER NOT NULL REFERENCES Departamento(id_departamento),
    id_municipio NUMBER NOT NULL REFERENCES Municipio(id_municipio),
    telefono VARCHAR2(15),
    correo_electronico VARCHAR2(100),
    fecha_registro DATE DEFAULT SYSDATE,
    id_estado NUMBER NOT NULL REFERENCES Estado(id_estado),
    fecha_actualizacion DATE,
    usuario_actualizacion VARCHAR2(50)
);

CREATE TABLE Establecimiento (
    id_establecimiento NUMBER PRIMARY KEY,
    id_comerciante NUMBER REFERENCES Comerciante(id_comerciante),
    nombre_establecimiento VARCHAR2(100) NOT NULL,
    ingresos NUMBER(10, 2) NOT NULL,
    numero_empleados NUMBER NOT NULL,
    fecha_actualizacion DATE,
    usuario_actualizacion VARCHAR2(50)
);

--tabla para login

CREATE TABLE Usuario (
    id_usuario NUMBER PRIMARY KEY,
    correo_electronico VARCHAR2(100) UNIQUE NOT NULL,
    password VARCHAR2(255) NOT NULL, -- Encriptada con BCrypt
    rol VARCHAR2(50) NOT NULL, -- Ejemplo: 'ADMINISTRADOR', 'AUXILIAR'
    fecha_creacion DATE DEFAULT SYSDATE,
    fecha_actualizacion DATE,
    usuario_actualizacion VARCHAR2(50)
);


INSERT INTO Usuario (id_usuario, correo_electronico, password, rol) VALUES (
    SEQ_USUARIO.NEXTVAL, 
    'admin@example.com', 
    '$2a$10$2G8m8vPqExCeQ7DWOVOMPe1SUDrXgyD/2w97FD5.N1X.BcsCb4wC2', -- Password: admin123 (encriptado con BCrypt)
    'ADMINISTRADOR'
);

INSERT INTO Usuario (id_usuario, correo_electronico, password, rol) VALUES (
    SEQ_USUARIO.NEXTVAL, 
    'aux@example.com', 
    '$2a$10$EXZ2bC3sJqD5kCePvqM7AeJPEkx2VrxGbClstN87kkkTbU3BrY9Qm', -- Password: aux123 (encriptado con BCrypt)
    'AUXILIAR'
);

--triggers

-- Trigger para insertar ID en Comerciante
CREATE OR REPLACE TRIGGER trg_comerciante_id
BEFORE INSERT ON comerciante
FOR EACH ROW
BEGIN
    :NEW.id_comerciante := seq_comerciante.NEXTVAL;
END;

-- Trigger para insertar ID en Establecimiento
CREATE OR REPLACE TRIGGER trg_establecimiento_id
BEFORE INSERT ON establecimiento
FOR EACH ROW
BEGIN
    :NEW.id_establecimiento := seq_establecimiento.NEXTVAL;
END;


CREATE OR REPLACE TRIGGER trg_auditoria_comerciante
BEFORE INSERT OR UPDATE ON Comerciante
FOR EACH ROW
BEGIN
    :NEW.fecha_actualizacion := SYSDATE;
    :NEW.usuario_actualizacion := NVL(USER, 'SYSTEM'); -- Asigna el usuario actual
END;

CREATE OR REPLACE TRIGGER trg_auditoria_establecimiento
BEFORE INSERT OR UPDATE ON Establecimiento
FOR EACH ROW
BEGIN
    :NEW.fecha_actualizacion := SYSDATE;
    :NEW.usuario_actualizacion := NVL(USER, 'SYSTEM'); -- Asigna el usuario actual
END;


-- Inserts ------------------------

INSERT INTO Departamento (id, nombre) VALUES (1, 'Antioquia');
INSERT INTO Departamento (id, nombre) VALUES (2, 'Cundinamarca');
INSERT INTO Departamento (id, nombre) VALUES (3, 'Valle del Cauca');
INSERT INTO Departamento (id, nombre) VALUES (4, 'Atlántico');
INSERT INTO Departamento (id, nombre) VALUES (5, 'Santander');
INSERT INTO Departamento (id, nombre) VALUES (6, 'Bolívar');
INSERT INTO Departamento (id, nombre) VALUES (7, 'Cauca');
INSERT INTO Departamento (id, nombre) VALUES (8, 'Nariño');
INSERT INTO Departamento (id, nombre) VALUES (9, 'Risaralda');
INSERT INTO Departamento (id, nombre) VALUES (10, 'Quindío');



INSERT INTO Municipio (id, DEPARTAMENTO_ID , NOMBRE) VALUES (1, 1, 'Medellín');
INSERT INTO Municipio (id, DEPARTAMENTO_ID , NOMBRE) VALUES (2, 1, 'Envigado');
INSERT INTO Municipio (id, DEPARTAMENTO_ID , NOMBRE) VALUES (3, 1, 'Bello');
INSERT INTO Municipio (id, DEPARTAMENTO_ID , NOMBRE) VALUES (4, 2, 'Bogotá');
INSERT INTO Municipio (id, DEPARTAMENTO_ID , NOMBRE) VALUES (5, 2, 'Chía');
INSERT INTO Municipio (id, DEPARTAMENTO_ID , NOMBRE) VALUES (6, 3, 'Cali');
INSERT INTO Municipio (id, DEPARTAMENTO_ID , NOMBRE) VALUES (7, 3, 'Palmira');
INSERT INTO Municipio (id, DEPARTAMENTO_ID , NOMBRE) VALUES (8, 4, 'Barranquilla');
INSERT INTO Municipio (id, DEPARTAMENTO_ID , NOMBRE) VALUES (9, 4, 'Soledad');
INSERT INTO Municipio (id, DEPARTAMENTO_ID , NOMBRE) VALUES (10, 5, 'Bucaramanga');

INSERT INTO Comerciante ( nombre_razon_social, ID_DEPARTAMENTO , ID_MUNICIPIO , telefono, correo_electronico, id_estado)
VALUES ( 'Postobon S.A', 1, 1, '98541789', 'postobon@mail.com', 1);

INSERT INTO Comerciante ( nombre_razon_social, ID_DEPARTAMENTO , ID_MUNICIPIO, telefono, correo_electronico, id_estado)
VALUES ( 'Alpina S.A', 2, 2, '3109876543', 'alpina@mail.com', 2);

INSERT INTO Comerciante ( nombre_razon_social, ID_DEPARTAMENTO , ID_MUNICIPIO, telefono, correo_electronico, id_estado)
VALUES ( 'Grupo Éxito', 3, 3, '3176549870', 'grupo_exito@mail.com', 3);

INSERT INTO Comerciante ( nombre_razon_social, ID_DEPARTAMENTO , ID_MUNICIPIO, telefono, correo_electronico, id_estado)
VALUES ( 'Droguerías Colsubsidio', 4, 4, '3125671234', 'colsubsidio@mail.com', 4);

INSERT INTO Comerciante ( nombre_razon_social, ID_DEPARTAMENTO , ID_MUNICIPIO, telefono, correo_electronico, id_estado)
VALUES ( 'Carvajal S.A', 5, 5, '3147895632', 'carvajal@mail.com', 5);

INSERT INTO Comerciante ( nombre_razon_social, ID_DEPARTAMENTO , ID_MUNICIPIO, telefono, correo_electronico, id_estado)
VALUES ( 'Bavaria', 6, 6, '3219876540', 'bavaria@mail.com', 2);

INSERT INTO Comerciante (id_comerciante, nombre_razon_social, ID_DEPARTAMENTO , ID_MUNICIPIO, telefono, correo_electronico, id_estado)
VALUES (seq_comerciante.NEXTVAL, 'Tecnoglass', 7, 7, '3118765432', 'tecnoglass@mail.com', 1);

INSERT INTO Comerciante (id_comerciante, nombre_razon_social, ID_DEPARTAMENTO , ID_MUNICIPIO, telefono, correo_electronico, id_estado)
VALUES (seq_comerciante.NEXTVAL, 'Ramo S.A', 7, 7, '3148765432', 'ramo@mail.com', 3);

INSERT INTO Comerciante (id_comerciante, nombre_razon_social, ID_DEPARTAMENTO , ID_MUNICIPIO, telefono, correo_electronico, id_estado)
VALUES (seq_comerciante.NEXTVAL, 'Familia S.A', 8, 8, '3138765432', 'familia@mail.com', 4);

INSERT INTO Comerciante (id_comerciante, nombre_razon_social, ID_DEPARTAMENTO , ID_MUNICIPIO, telefono, correo_electronico, id_estado)
VALUES (seq_comerciante.NEXTVAL, 'Colanta', 9, 9, '3109871234', 'colanta@mail.com', 1);


-- Insertar Establecimientos
INSERT INTO Establecimiento ( id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES ( 21, 'Sucursal Cali', 15000.50, 25);

INSERT INTO Establecimiento ( id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES ( 22, 'Sucursal Medellín', 20000.75, 30);

INSERT INTO Establecimiento ( id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES ( 23, 'Planta Chía', 50000.00, 50);

INSERT INTO Establecimiento ( id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (23, 'Sucursal Bogotá', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,25, 'Sucursal Peru', 60000.30, 50);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,24, 'Sucursal Choco', 70000.30, 20);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,27, 'Sucursal Huila', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,22, 'Sucursal Cancun', 100000.30, 20);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,21, 'Sucursal Meta', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,22, 'Sucursal Patio bonito', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,22, 'Sucursal San Antonio', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,21, 'Planta Bogotá', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,21, 'Sucursal Andes', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,22, 'Sucursal Ricaurte', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,21, 'Sucursal Silvia', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,22, 'Sucursal San Jose', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,22, 'Sucursal Colombia', 30000.30, 25);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,21, 'Sucursal Puerto', 30000.30, 10);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,22, 'Sucursal Cedi', 30000.30, 15);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,23, 'Sucursal Belgica', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,25, 'Sucursal Inglaterra', 30000.30, 15);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,25, 'Sucursal BESA', 60000.30, 50);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,25, 'Sucursal Bosa', 30000.30, 55);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,26, 'Sucursal 2345', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,26, 'Sucursal 456', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,26, 'Sucursal 789', 30000.30, 35);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,26, 'Sucursal 101112', 10000.30, 10);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,26, 'Sucursal 131415', 10000.30, 9);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,26, 'Sucursal 161718', 10000.30, 7);

INSERT INTO Establecimiento (id_establecimiento, id_comerciante, nombre_establecimiento, ingresos, numero_empleados)
VALUES (seq_establecimiento.NEXTVAL,26, 'Sucursal 192021', 10000.30, 8);


--Paquete creacion 

CREATE OR REPLACE PACKAGE pkg_comerciante AS
    -- Funciones
    FUNCTION consultar_por_id_comerciante(p_id IN NUMBER) RETURN VARCHAR2;
    FUNCTION consultar(
        p_nombre IN VARCHAR2 DEFAULT NULL,
        p_municipio IN VARCHAR2 DEFAULT NULL,
        p_fecha_registro IN DATE DEFAULT NULL,
        p_estado IN NUMBER DEFAULT NULL,
        p_pagina IN NUMBER DEFAULT 1,
        p_tamano_pagina IN NUMBER DEFAULT 10
    ) RETURN SYS_REFCURSOR;

    -- Procedimientos
    PROCEDURE crear(
        p_nombre IN VARCHAR2,
        p_departamento IN VARCHAR2,
        p_municipio IN VARCHAR2,
        p_telefono IN VARCHAR2 DEFAULT NULL,
        p_correo IN VARCHAR2 DEFAULT NULL,
        p_fecha_registro IN DATE,
        p_estado IN NUMBER,
        v_codigo_error OUT NUMBER,
        v_mensaje_error OUT VARCHAR2
    );

    PROCEDURE actualizar(
        p_id IN NUMBER,
        p_nombre IN VARCHAR2,
        p_departamento IN VARCHAR2,
        p_municipio IN VARCHAR2,
        p_telefono IN VARCHAR2 DEFAULT NULL,
        p_correo IN VARCHAR2 DEFAULT NULL,
        p_fecha_registro IN DATE,
        p_estado IN NUMBER,
        v_codigo_error OUT NUMBER,
        v_mensaje_error OUT VARCHAR2
    );

    PROCEDURE eliminar(
        p_id IN NUMBER,
        v_codigo_error OUT NUMBER,
        v_mensaje_error OUT VARCHAR2
    );
END pkg_comerciante;



--PROCEDIMIENTO ACTUALIZAR
CREATE OR REPLACE PROCEDURE COMERCIO.actualizar(
        p_id IN NUMBER,
        p_nombre IN VARCHAR2,
        p_departamento IN NUMBER ,
        p_municipio IN NUMBER ,
        p_telefono IN VARCHAR2 DEFAULT NULL,
        p_correo IN VARCHAR2 DEFAULT NULL,
        p_estado IN NUMBER,
        v_codigo_error OUT NUMBER,
        v_mensaje_error OUT VARCHAR2
    ) IS
    BEGIN
        UPDATE Comerciante
        SET nombre_razon_social = p_nombre,
            ID_DEPARTAMENTO = p_departamento,
            ID_MUNICIPIO  = p_municipio,
            telefono = p_telefono,
            correo_electronico = p_correo,
            id_estado = p_estado
        WHERE id_comerciante = p_id;

        v_codigo_error := 0;
        v_mensaje_error := NULL;

    EXCEPTION
        WHEN OTHERS THEN
            v_codigo_error := SQLCODE;
            v_mensaje_error := SQLERRM;
    END actualizar;

	
	

--PROCEDIMIENTO CREAR
CREATE OR REPLACE PROCEDURE COMERCIO.crear(
        p_nombre IN VARCHAR2,
        p_departamento IN VARCHAR2,
        p_municipio IN VARCHAR2,
        p_telefono IN VARCHAR2 DEFAULT NULL,
        p_correo IN VARCHAR2 DEFAULT NULL,
        p_fecha_registro IN DATE,
        p_estado IN NUMBER,
        v_codigo_error OUT NUMBER,
        v_mensaje_error OUT VARCHAR2
    ) IS
    BEGIN
        INSERT INTO Comerciante (ID_COMERCIANTE ,nombre_razon_social, id_departamento, id_municipio, telefono, correo_electronico, fecha_registro, id_estado)
        VALUES (seq_comerciante.NEXTVAL , p_nombre, p_departamento, p_municipio, p_telefono, p_correo, p_fecha_registro, p_estado);

        v_codigo_error := 0;
        v_mensaje_error := NULL;

    EXCEPTION
        WHEN OTHERS THEN
            v_codigo_error := SQLCODE;
            v_mensaje_error := SQLERRM;
    END crear;
	
	
--PROCEDIMIENTO ELIMINAR
CREATE OR REPLACE PROCEDURE COMERCIO.eliminar(
        p_id IN NUMBER,
        v_codigo_error OUT NUMBER,
        v_mensaje_error OUT VARCHAR2
    ) IS
    BEGIN
        DELETE FROM Comerciante WHERE id_comerciante = p_id;

        v_codigo_error := 0;
        v_mensaje_error := NULL;

    EXCEPTION
        WHEN OTHERS THEN
            v_codigo_error := SQLCODE;
            v_mensaje_error := SQLERRM;
    END eliminar;
	
	

--FUNCION CONSULTAR 

CREATE OR REPLACE FUNCTION COMERCIO.consultar(
    p_nombre IN VARCHAR2 DEFAULT NULL,
    p_municipio IN NUMBER DEFAULT NULL,
    p_fecha_registro IN DATE DEFAULT NULL,
    p_estado IN NUMBER DEFAULT NULL,
    p_pagina IN NUMBER DEFAULT 1,
    p_tamano_pagina IN NUMBER DEFAULT 10
) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
    v_offset NUMBER := (p_pagina - 1) * p_tamano_pagina;
BEGIN
    OPEN v_cursor FOR
    SELECT 
        c.ID_COMERCIANTE,
        c.NOMBRE_RAZON_SOCIAL,
        d.NOMBRE AS DEPARTAMENTO,
        m.NOMBRE AS MUNICIPIO,
        c.TELEFONO,
        c.CORREO_ELECTRONICO,
        TO_CHAR(c.FECHA_REGISTRO, 'YYYY-MM-DD') AS FECHA_REGISTRO,
        c.ID_ESTADO
    FROM Comerciante c
    JOIN Departamento d ON c.ID_DEPARTAMENTO = d.ID
    JOIN Municipio m ON c.ID_MUNICIPIO = m.ID
    LEFT JOIN Estado e ON c.ID_ESTADO = e.ID_ESTADO
    WHERE (p_nombre IS NULL OR c.NOMBRE_RAZON_SOCIAL LIKE '%' || p_nombre || '%')
      AND (p_municipio IS NULL OR c.ID_MUNICIPIO = p_municipio)
      AND (p_fecha_registro IS NULL OR c.FECHA_REGISTRO = p_fecha_registro)
      AND (p_estado IS NULL OR c.ID_ESTADO = p_estado)
    ORDER BY c.FECHA_REGISTRO DESC
    OFFSET v_offset ROWS FETCH NEXT p_tamano_pagina ROWS ONLY;

    RETURN v_cursor;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error en la función COMERCIO.consultar: ' || SQLERRM);
        RETURN NULL;
END consultar;
	
	
	
--FUNCION CONSULTAR POR ID

CREATE OR REPLACE FUNCTION COMERCIO.consultar_por_id_comerciante(
    p_id IN NUMBER
) RETURN VARCHAR2 IS
    v_respuesta VARCHAR2(4000);
    v_total_activos NUMBER := 0;
    v_cantidad_empleados NUMBER := 0;
BEGIN

    SELECT NVL(SUM(e.ingresos), 0), NVL(SUM(e.numero_empleados), 0)
    INTO v_total_activos, v_cantidad_empleados
    FROM COMERCIO.Establecimiento e
    WHERE e.id_comerciante = p_id;


    SELECT c.nombre_razon_social || ', ' || c.id_departamento || ', ' || c.id_municipio || ', ' ||
           c.telefono || ', ' || c.correo_electronico || ', ' || TO_CHAR(c.fecha_registro, 'YYYY-MM-DD') || ', ' ||
           e.descripcion || ', Total Activos: ' || TO_CHAR(v_total_activos) || ', Empleados: ' || TO_CHAR(v_cantidad_empleados)
    INTO v_respuesta
    FROM COMERCIO.Comerciante c
    LEFT JOIN COMERCIO.Estado e ON c.id_estado = e.id_estado
    WHERE c.id_comerciante = p_id;

    RETURN v_respuesta;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN NULL;
    WHEN OTHERS THEN
        RETURN 'Error: ' || SQLERRM;
END consultar_por_id_comerciante;


--FUNCION REPORTE COMERCIANTES ACTIVOS
CREATE OR REPLACE FUNCTION COMERCIO.obtener_comerciantes_activos 
RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT 
        c.nombre_razon_social AS nombre_razon_social,
        d.nombre AS departamento,
        m.nombre AS municipio,
        c.telefono AS telefono,
        c.correo_electronico AS correo_electronico,
        c.fecha_registro AS fecha_registro,
        e.descripcion AS estado,
        COUNT(est.id_establecimiento) AS cantidad_establecimientos,
        NVL(SUM(est.ingresos), 0) AS total_activos,
        NVL(SUM(est.numero_empleados), 0) AS cantidad_empleados
    FROM 
        Comerciante c
    JOIN Departamento d ON c.id_departamento = d.ID
    JOIN Municipio m ON c.id_municipio = m.ID
    JOIN Estado e ON c.id_estado = e.id_estado
    LEFT JOIN Establecimiento est ON est.id_comerciante = c.id_comerciante
    WHERE 
        e.descripcion = 'Activo' -- Consideramos solo comerciantes activos
    GROUP BY 
        c.nombre_razon_social, d.nombre, m.nombre, 
        c.telefono, c.correo_electronico, c.fecha_registro, 
        e.descripcion;
    
    RETURN v_cursor;
END obtener_comerciantes_activos;


