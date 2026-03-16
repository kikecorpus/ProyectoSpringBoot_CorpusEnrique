# Backend - LogiTrack API

## Descripción

El backend de LogiTrack es una API REST desarrollada con Spring Boot que proporciona todas las funcionalidades del sistema de gestión de bodegas. Implementa arquitectura en capas, seguridad JWT, auditoría automática y documentación completa con Swagger.

## Arquitectura

### Patrón de Diseño
- **Controller Layer**: Maneja requests HTTP y responses
- **Service Layer**: Contiene lógica de negocio y validaciones
- **Repository Layer**: Acceso a datos con JPA
- **Model Layer**: Entidades JPA con relaciones
- **Config Layer**: Configuraciones de seguridad, JWT, OpenAPI
- **Exception Layer**: Manejo global de errores

### Tecnologías Clave
- **Spring Boot 3.5.11**: Framework principal
- **Spring Data JPA**: ORM con Hibernate
- **Spring Security**: Autenticación y autorización
- **JWT**: Tokens stateless para sesiones
- **MySQL**: Base de datos relacional
- **Lombok**: Anotaciones para reducir código
- **Validation API**: Validaciones de entrada
- **OpenAPI 3**: Documentación automática

## Estructura de Paquetes

```
src/main/java/com/Campusland/ProyectoSpringBoot_CorpusEnrique/
├── ProyectoSpringBootCorpusEnriqueApplication.java    # Clase principal
├── auth/                                              # Autenticación
│   ├── AuthController.java                           # Endpoints de login
│   ├── LoginRequest.java                             # DTO login
│   ├── LoginResponse.java                            # DTO respuesta login
├── config/                                            # Configuraciones
│   ├── JwtFilter.java                                # Filtro JWT
│   ├── JwtService.java                               # Servicio JWT
│   ├── openAPIConfig.java                            # Config Swagger
│   └── SecurityConfig.java                           # Config seguridad
├── controller/                                        # Controladores REST
│   ├── AuditoriaController.java
│   ├── BodegaController.java
│   ├── CategoriaController.java
│   ├── CiudadController.java
│   ├── DepartamentoController.java
│   ├── InventarioController.java
│   ├── MovimientoInventarioController.java
│   ├── PersonaController.java
│   ├── ProductoController.java
│   ├── ReporteController.java
│   ├── RolController.java
│   └── UsuarioController.java
├── dto/                                              # Data Transfer Objects
│   ├── request/                                      # DTOs de entrada
│   └── response/                                     # DTOs de salida
├── exception/                                         # Manejo de excepciones
│   ├── BusinessRuleException.java                    # Excepciones negocio
│   ├── ErrorResponse.java                            # DTO error
│   └── GlobalExceptionHandler.java                   # Handler global
├── mappers/                                           # Mappers DTO-Entidad
│   ├── AuditoriaMapper.java
│   ├── BodegaMapper.java
│   └── ... (uno por entidad)
├── model/                                             # Entidades JPA
│   ├── Auditoria.java
│   ├── Bodega.java
│   └── ... (14 entidades)
├── repository/                                        # Repositorios JPA
│   ├── AuditoriaRepository.java
│   └── ... (uno por entidad)
└── service/                                           # Servicios de negocio
    ├── impl/                                          # Implementaciones
    └── interfaces                                     # Interfaces
```

## Endpoints API

### Autenticación
- `POST /auth/login` - Login con username/password, retorna JWT

### Gestión de Usuarios
- `GET /api/usuario` - Listar usuarios (ADMIN)
- `POST /api/usuario` - Crear usuario (ADMIN)
- `PUT /api/usuario/{id}` - Actualizar usuario (ADMIN)
- `DELETE /api/usuario/{id}` - Eliminar usuario (ADMIN)
- `GET /api/usuario/me` - Perfil del usuario autenticado

### Gestión de Inventario
- `GET /api/inventario` - Listar inventarios
- `POST /api/inventario` - Crear inventario
- `PUT /api/inventario/{id}` - Actualizar inventario
- `DELETE /api/inventario/{id}` - Eliminar inventario
- `GET /api/inventario/stock-critico` - Stock por debajo del mínimo
- `GET /api/inventario/stock-bajo` - Cantidad < 10

### Movimientos de Inventario
- `GET /api/movimiento` - Listar movimientos
- `POST /api/movimiento` - Registrar movimiento
- `GET /api/movimiento/{id}` - Obtener movimiento
- `GET /api/movimiento/inventario/{id}` - Movimientos por inventario
- `GET /api/movimiento/rango?desde=...&hasta=...` - Por rango de fechas

### Reportes
- `GET /api/reporte/general` - Resumen: stock por bodega + top productos

### Auditoría
- `GET /api/auditoria` - Listar registros de auditoría

### Otros CRUD
- `/api/bodega`, `/api/producto`, `/api/categoria`, `/api/rol`, `/api/persona`, `/api/ciudad`, `/api/departamento`

## Seguridad

### Autenticación JWT
- **Login**: POST /auth/login con username/password
- **Token**: HS256, expiración 30 min
- **Header**: Authorization: Bearer <token>

### Roles y Permisos
| Rol | Permisos |
|-----|----------|
| **ADMIN** | Acceso completo a todos los recursos |
| **SUPERVISOR** | Gestión inventario, reportes, lectura usuarios |
| **OPERARIO** | Registrar movimientos en su bodega asignada |

### Endpoints Públicos
- `/auth/login`
- `/swagger-ui/**`
- `/v3/api-docs/**`

## Auditoría Automática

### Implementación
- **@Auditable**: Anotación personalizada en entidades
- **@EntityListeners**: AuditoriaListener registra cambios
- **Campos**: usuario, entidad, operación, valores_anteriores/nuevos

### Entidades Auditadas
- Usuario, Bodega, Producto, Inventario, MovimientoInventario, etc.

## Validaciones y Errores

### Validaciones
- **@NotNull, @NotBlank**: Campos requeridos
- **@Size**: Longitud de strings
- **@Positive, @PositiveOrZero**: Números positivos
- **@Valid**: Validación en DTOs

### Manejo de Errores
- **GlobalExceptionHandler**: Captura excepciones
- **Códigos HTTP**: 400 (validación), 401 (no autorizado), 404 (no encontrado), 500 (error interno)
- **Respuesta JSON**: timestamp, status, message, errorCode

## Configuración

### application.properties
```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/LogiTrack
spring.datasource.username=root
spring.datasource.password=password

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# JWT
jwt.secret=clave_super_secreta_para_clase_2026
jwt.expiration=1800000

# OpenAPI
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

## Ejecución

```bash
# Compilar
mvn clean compile

# Ejecutar
mvn spring-boot:run

# Acceder
# API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
```

## Testing

### Pruebas Unitarias
- **JUnit 5**: Tests en `src/test/java/`
- **Mockito**: Mocks para servicios
- **TestContainers**: Base de datos en memoria (opcional)

### Ejecución de Tests
```bash
mvn test
```

## Documentación API

Accede a la documentación completa en:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

Incluye ejemplos de requests/responses, parámetros y autenticación.