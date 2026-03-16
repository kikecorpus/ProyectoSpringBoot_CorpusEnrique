# LogiTrack - Sistema de Gestión y Auditoría de Bodegas

## Descripción del Proyecto

**LogiTrack** es un sistema académico desarrollado para la empresa LogiTrack S.A., que administra múltiples bodegas distribuidas en distintas ciudades. El sistema permite gestionar productos, controlar movimientos de inventario (entradas, salidas y transferencias) y mantener una auditoría completa de todos los cambios realizados.

Hasta ahora, el control se realizaba manualmente en hojas de cálculo, sin trazabilidad ni control de accesos. Este proyecto implementa un backend centralizado en Spring Boot que resuelve estas necesidades.

## Objetivo General

Desarrollar un sistema de gestión y auditoría de bodegas que permita registrar transacciones de inventario y generar reportes auditables de los cambios realizados por cada usuario.

## Tecnologías Utilizadas

### Backend
- **Spring Boot 3.5.11** - Framework principal
- **Java 17** - Lenguaje de programación
- **Spring Data JPA** - ORM para base de datos
- **Spring Security** - Autenticación y autorización
- **JWT (JJWT)** - Tokens seguros
- **MySQL 8.0+** - Base de datos
- **Lombok** - Reducción de boilerplate
- **SpringDoc OpenAPI** - Documentación API (Swagger)
- **Bean Validation** - Validaciones de entrada

### Frontend
- **HTML5 / CSS3 / JavaScript (Vanilla)** - Interfaz de usuario
- **Responsive Design** - Adaptable a dispositivos
- **API REST** - Comunicación con backend
- **LocalStorage** - Almacenamiento de tokens y datos de usuario

### Base de Datos
- **MySQL** con triggers y constraints para integridad
- **Scripts SQL** incluidos en `SQL/modelos/` y `SQL/script/`

## Requisitos Funcionales

### 1. Gestión de Bodegas
- CRUD completo de bodegas
- Campos: ID, nombre, dirección, capacidad, ciudad, encargado

### 2. Gestión de Productos
- CRUD completo de productos
- Campos: ID, código, nombre, unidad de medida, categoría

### 3. Gestión de Inventario
- Control de stock por producto-bodega
- Alertas de stock crítico y bajo
- Validaciones de capacidad y stock mínimo/máximo

### 4. Movimientos de Inventario
- Tipos: ENTRADA, SALIDA, TRASLADO, AJUSTE
- Registro con usuario responsable
- Validaciones de stock disponible
- Inmutabilidad post-registro

### 5. Auditoría de Cambios
- Registro automático de todas las operaciones
- Entidad Auditoria con JSON de cambios
- Filtros por usuario, entidad, fecha

### 6. Autenticación y Seguridad
- Login con JWT
- Roles: ADMIN, SUPERVISOR, OPERARIO
- Control de acceso basado en roles
- Endpoints seguros

### 7. Reportes y Consultas
- Stock total por bodega
- Productos más movidos
- Movimientos por rango de fechas
- Alertas de stock bajo

### 8. Documentación API
- Swagger/OpenAPI 3 integrado
- Endpoints documentados con ejemplos

### 9. Manejo de Errores
- Excepciones globales con @ControllerAdvice
- Respuestas JSON estructuradas
- Validaciones con anotaciones

## Arquitectura del Sistema

### Patrón de Diseño
- **Arquitectura en Capas (3-Tier)**:
  - Controller (REST API)
  - Service (Lógica de negocio)
  - Repository (Acceso a datos)

### Componentes Transversales
- **Mappers**: Conversión DTO ↔ Entidad
- **DTOs**: Objetos de transferencia
- **Exception Handlers**: Manejo centralizado de errores
- **Security Filters**: JWT validation
- **Auditoría**: Listeners automáticos

### Estructura de Base de Datos
14 entidades principales con relaciones:
- Persona → Usuario (rol, bodega)
- Bodega → Ciudad → Departamento
- Producto → Categoría
- Inventario (producto-bodega)
- MovimientoInventario (transacciones)
- Auditoria (registro de cambios)

## Instalación y Configuración

### Prerrequisitos
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Node.js (opcional, para desarrollo frontend)

### Configuración de Base de Datos
1. Crear base de datos `LogiTrack` en MySQL
2. Ejecutar scripts en `SQL/script/sql_script.sql`
3. Configurar credenciales en `src/main/resources/application.properties`

### Ejecución
```bash
# Backend
mvn clean install
mvn spring-boot:run

# Frontend (servir con servidor local, ej: Live Server en VS Code)
# Abrir index.html en navegador
```

### Acceso
- **Backend**: http://localhost:8080
- **Swagger**: http://localhost:8080/swagger-ui.html
- **Frontend**: Abrir `Frontend/index.html` en navegador

## Equipo de Desarrollo
- **Desarrollador**: Enrique Corpus
- **Institución**: Campuslands
- **Fecha**: Marzo 2026

## Licencia
Proyecto académico - Todos los derechos reservados.