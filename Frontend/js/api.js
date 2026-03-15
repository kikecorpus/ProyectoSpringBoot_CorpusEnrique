const API_BASE = 'http://localhost:8080';

function getToken() {
  return localStorage.getItem('token');
}

function authHeaders() {
  return {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${getToken()}`
  };
}

async function request(method, path, body = null) {
  const opts = { method, headers: authHeaders() };
  if (body) opts.body = JSON.stringify(body);

  const res = await fetch(`${API_BASE}${path}`, opts);

  if (res.status === 401) {
    localStorage.clear();
    window.location.href = 'index.html';
    return;
  }

  if (res.status === 204) return null;

  const data = await res.json();

  if (!res.ok) {
    const msg = data.message || data.errors
      ? (data.message || Object.values(data.errors).join(', '))
      : 'Error en la solicitud';
    throw new Error(msg);
  }

  return data;
}

const API = {
  // Departamentos
  getDepartamentos:     () => request('GET',    '/api/departamento'),
  getDepartamento:     id => request('GET',    `/api/departamento/${id}`),
  createDepartamento: dto => request('POST',   '/api/departamento', dto),
  updateDepartamento: (id, dto) => request('PUT', `/api/departamento/${id}`, dto),
  deleteDepartamento:  id => request('DELETE', `/api/departamento/${id}`),

  // Categorias
  getCategorias:        () => request('GET',    '/api/categoria'),
  createCategoria:     dto => request('POST',   '/api/categoria', dto),
  updateCategoria: (id, dto) => request('PUT',  `/api/categoria/${id}`, dto),
  deleteCategoria:     id => request('DELETE',  `/api/categoria/${id}`),

  // Roles
  getRoles:             () => request('GET',    '/api/rol'),
  createRol:           dto => request('POST',   '/api/rol', dto),
  updateRol:     (id, dto) => request('PUT',    `/api/rol/${id}`, dto),
  deleteRol:           id => request('DELETE',  `/api/rol/${id}`),

  // Personas
  getPersonas:          () => request('GET',    '/api/persona'),
  createPersona:       dto => request('POST',   '/api/persona', dto),
  updatePersona: (id, dto) => request('PUT',    `/api/persona/${id}`, dto),
  deletePersona:       id => request('DELETE',  `/api/persona/${id}`),

  // Usuarios
  getUsuarios:          () => request('GET',    '/api/usuario'),
  createUsuario:       dto => request('POST',   '/api/usuario', dto),
  updateUsuario: (id, dto) => request('PUT',    `/api/usuario/${id}`, dto),
  deleteUsuario:       id => request('DELETE',  `/api/usuario/${id}`),

  // Ciudades
  getCiudades:          () => request('GET',    '/api/ciudad'),
  createCiudad:        dto => request('POST',   '/api/ciudad', dto),
  updateCiudad:  (id, dto) => request('PUT',    `/api/ciudad/${id}`, dto),
  deleteCiudad:        id => request('DELETE',  `/api/ciudad/${id}`),

  // Bodegas
  getBodegas:           () => request('GET',    '/api/bodega'),
  createBodega:        dto => request('POST',   '/api/bodega', dto),
  updateBodega:  (id, dto) => request('PUT',    `/api/bodega/${id}`, dto),
  deleteBodega:        id => request('DELETE',  `/api/bodega/${id}`),

  // Productos
  getProductos:         () => request('GET',    '/api/producto'),
  createProducto:      dto => request('POST',   '/api/producto', dto),
  updateProducto:(id, dto) => request('PUT',    `/api/producto/${id}`, dto),
  deleteProducto:      id => request('DELETE',  `/api/producto/${id}`),

  // Inventario
  getInventarios:       () => request('GET',    '/api/inventario'),
  createInventario:    dto => request('POST',   '/api/inventario', dto),
  updateInventario:(id,dto) => request('PUT',   `/api/inventario/${id}`, dto),
  deleteInventario:    id => request('DELETE',  `/api/inventario/${id}`),
  getStockCritico:      () => request('GET',    '/api/inventario/stock-critico'),

  // Movimientos
  getMovimientos:       () => request('GET',    '/api/movimiento'),
  createMovimiento: (dto, usuarioId) => request('POST', `/api/movimiento?usuarioId=${usuarioId}`, dto),
  getMovimientosPorInventario: id => request('GET', `/api/movimiento/inventario/${id}`),

  // Auditoria
  getAuditorias:        () => request('GET',    '/api/auditoria'),
};
