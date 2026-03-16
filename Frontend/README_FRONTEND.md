# Frontend - LogiTrack Web App

## Descripción

El frontend de LogiTrack es una aplicación web SPA (Single Page Application) desarrollada con tecnologías vanilla (HTML5, CSS3, JavaScript). Proporciona una interfaz intuitiva para interactuar con la API backend, permitiendo gestionar bodegas, productos, inventarios, movimientos y auditorías.

## Características Principales

- **Interfaz Responsive**: Adaptable a desktop, tablet y móvil
- **Autenticación JWT**: Login seguro con tokens
- **Control de Acceso**: UI adaptada por roles (ADMIN, SUPERVISOR, OPERARIO)
- **API Integration**: Comunicación REST con backend
- **Navegación SPA**: Sin recargas de página
- **Feedback Visual**: Toasts, loaders, modales
- **Validaciones**: Formularios con validaciones del lado cliente

## Tecnologías

- **HTML5**: Estructura semántica
- **CSS3**: Diseño moderno con variables CSS
- **JavaScript ES6+**: Lógica de aplicación
- **Fetch API**: Requests HTTP
- **LocalStorage**: Persistencia de sesión
- **Responsive Design**: Flexbox, Grid, Media Queries

## Estructura de Archivos

```
Frontend/
├── index.html              # Página de login
├── dashboard.html          # Dashboard principal
├── inventario.html         # Gestión de inventario
├── movimientos.html        # Registro y consulta de movimientos
├── productos.html          # Catálogo de productos
├── bodegas.html            # Gestión de bodegas
├── usuarios.html           # Administración de usuarios
├── roles.html              # Gestión de roles
├── auditoria.html          # Consulta de auditorías
├── reportes.html           # Reportes del sistema
├── shared.css              # Estilos globales
├── shared.js               # Utilidades y API client
└── README_FRONTEND.md      # Esta documentación
```

## Páginas y Funcionalidades

### 1. Login (index.html)
- **Funcionalidad**: Autenticación de usuarios
- **Campos**: Username, Password
- **Validación**: JWT token almacenado en localStorage
- **Redirección**: Dashboard tras login exitoso

### 2. Dashboard (dashboard.html)
- **Funcionalidad**: Vista general del sistema
- **Widgets**: Estadísticas, alertas de stock, accesos rápidos
- **Navegación**: Sidebar con menú según rol

### 3. Inventario (inventario.html)
- **Funcionalidad**: CRUD de inventarios por bodega
- **Características**:
  - Filtros por producto/bodega
  - Alertas de stock crítico (< mínimo)
  - Alertas de stock bajo (< 10 unidades)
  - Creación/edición con validaciones

### 4. Movimientos (movimientos.html)
- **Funcionalidad**: Registro y consulta de movimientos
- **Tipos**: ENTRADA, SALIDA, TRASLADO, AJUSTE
- **Características**:
  - Formulario dinámico según tipo
  - Filtros por fecha y tipo
  - Restricciones por rol (OPERARIO solo su bodega)
  - Información contextual por tipo

### 5. Productos (productos.html)
- **Funcionalidad**: Gestión del catálogo de productos
- **Campos**: Código, nombre, unidad, categoría
- **Características**: CRUD completo con validaciones

### 6. Bodegas (bodegas.html)
- **Funcionalidad**: Administración de almacenes
- **Campos**: Nombre, dirección, capacidad, ciudad, encargado
- **Características**: CRUD con relaciones geográficas

### 7. Usuarios (usuarios.html)
- **Funcionalidad**: Gestión de usuarios del sistema
- **Campos**: Username, password, estado, rol, persona, bodega
- **Características**:
  - Solo ADMIN puede gestionar
  - Encriptación automática de passwords
  - Asignación de bodegas para OPERARIOS

### 8. Roles (roles.html)
- **Funcionalidad**: Definición de perfiles de usuario
- **Campos**: Nombre del rol
- **Características**: CRUD básico

### 9. Auditoría (auditoria.html)
- **Funcionalidad**: Consulta de registros de cambios
- **Campos**: Usuario, entidad, operación, fecha, cambios
- **Características**: Vista de JSON de cambios

### 10. Reportes (reportes.html)
- **Funcionalidad**: Reportes consolidados
- **Tipos**: Stock por bodega, productos más movidos

## Sistema de Diseño

### Paleta de Colores
```css
--navy: #0d1b2a;        /* Fondo principal */
--gold: #c9a84c;         /* Acentos, botones */
--slate: #8a9bb0;        /* Texto secundario */
--white: #f4f6f9;        /* Fondos claros */
--danger: #e05c5c;       /* Errores */
--success: #4caf7d;      /* Éxitos */
```

### Tipografía
- **Títulos**: Playfair Display (serif)
- **Cuerpo**: DM Sans (sans-serif)
- **Pesos**: 300, 400, 500, 600, 700

### Componentes UI
- **Botones**: Variantes primary, outline, danger
- **Formularios**: Inputs, selects, textareas con validación
- **Tablas**: Datos paginados con filtros
- **Modales**: Overlays para formularios
- **Toasts**: Notificaciones temporales
- **Sidebar**: Navegación lateral
- **Cards**: Contenedores de contenido

## Control de Acceso

### Roles en Frontend
```javascript
// Función getUserRole() obtiene rol de localStorage
const role = getUserRole();

if (role !== 'ADMIN') {
  // Ocultar elementos de administración
  hideAdminElements();
}
```

### Restricciones por Rol
- **ADMIN**: Acceso completo a todas las páginas
- **SUPERVISOR**: Gestión inventario, movimientos, reportes
- **OPERARIO**: Solo movimientos en su bodega asignada

### Navegación Dinámica
- Sidebar se construye dinámicamente según rol
- Elementos ocultos/deshabilitados para roles inferiores

## API Client (shared.js)

### Funciones Principales
```javascript
// API object con métodos HTTP
const API = {
  get: (path) => fetch con GET,
  post: (path, body) => fetch con POST,
  put: (path, body) => fetch con PUT,
  delete: (path) => fetch con DELETE
};

// Todas incluyen:
// - Authorization: Bearer <token>
// - Content-Type: application/json
// - Error handling (401 → logout)
```

### Utilidades
- **requireAuth()**: Verifica token, redirige a login si no
- **getSidebarHTML()**: Genera HTML del sidebar
- **toast()**: Muestra notificaciones
- **openModal/closeModal()**: Control de modales
- **confirmAction()**: Diálogos de confirmación

## Validaciones del Lado Cliente

### Formularios
- **Campos requeridos**: Validación visual
- **Formatos**: Email, números, fechas
- **Longitudes**: Mínimos y máximos
- **Dependencias**: Campos condicionales

### Feedback
- **Estados**: Valid/invalid con colores
- **Mensajes**: Texto descriptivo de errores
- **Prevención**: Submit bloqueado si inválido

## Responsive Design

### Breakpoints
- **Desktop**: > 1024px
- **Tablet**: 768px - 1024px
- **Mobile**: < 768px

### Adaptaciones
- **Sidebar**: Collapsible en móvil
- **Tablas**: Scroll horizontal
- **Modales**: Ancho adaptativo
- **Formularios**: Columnas flexibles

## Desarrollo y Debugging

### Servir Localmente
```bash
# Con extensión Live Server de VS Code
# O servidor HTTP simple
python -m http.server 8000
# Acceder: http://localhost:8000/Frontend/
```

### Consola del Navegador
- **Errores**: Ver en Console tab
- **Network**: Ver requests API
- **Storage**: Ver localStorage (token, role)

### Debugging Común
- **Token expirado**: 401 errors → auto-logout
- **CORS**: Configurar backend para localhost:8000
- **API errors**: Ver response en Network tab

## Integración con Backend

### Endpoints Consumidos
- `/auth/login` - Autenticación
- `/api/*` - Todos los recursos CRUD
- Headers: `Authorization: Bearer ${token}`

### Manejo de Errores
```javascript
try {
  const data = await API.get('/api/resource');
} catch(e) {
  toast(e.message, 'danger');
  if (e.status === 401) logout();
}
```

### Estado de Autenticación
- **Login**: Guarda token y role en localStorage
- **Requests**: Incluye token automáticamente
- **Logout**: Limpia storage y redirige

## Mejoras Futuras

- **Framework**: Migrar a Vue.js/React para mejor mantenibilidad
- **Estado Global**: Implementar store (Pinia/Vuex/Redux)
- **Componentes**: Sistema de componentes reutilizables
- **Testing**: Tests E2E con Cypress
- **PWA**: Service workers para offline
- **Internacionalización**: Soporte multi-idioma

## Equipo de Desarrollo
- **Frontend Developer**: Enrique Corpus
- **UI/UX Design**: Campuslands Team
- **Fecha**: Marzo 2026