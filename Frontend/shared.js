/* ═══════════════════════════════════════════════════════════
   shared.js  —  API helpers, auth guard, toast, sidebar
   ═══════════════════════════════════════════════════════════ */

const API_BASE = 'http://192.168.32.60:8080';

/* ── Auth helpers ── */
function getToken() { return localStorage.getItem('token'); }
function getUsername() { return localStorage.getItem('username') || 'Usuario'; }
function getUserRole() { return localStorage.getItem('userRole') || 'OPERARIO'; }
function requireAuth() {
  if (!getToken()) { window.location.href = 'index.html'; return false; }
  return true;
}
function logout() {
  localStorage.clear();
  window.location.href = 'index.html';
}

/* ── HTTP helpers ── */
async function apiFetch(path, options = {}) {
  const token = getToken();
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...(options.headers || {})
  };
  const res = await fetch(`${API_BASE}${path}`, { ...options, headers });

  if (res.status === 401 || res.status === 403) {
    logout();
    throw new Error('Sesión expirada');
  }
  if (!res.ok) {
    let msg = `Error ${res.status}`;
    try {
      const data = await res.json();
      msg = data.message || data.error || msg;
    } catch {}
    throw new Error(msg);
  }
  if (res.status === 204) return null;
  return res.json();
}

const API = {
  get:    (path)       => apiFetch(path),
  post:   (path, body) => apiFetch(path, { method: 'POST',   body: JSON.stringify(body) }),
  put:    (path, body) => apiFetch(path, { method: 'PUT',    body: JSON.stringify(body) }),
  delete: (path)       => apiFetch(path, { method: 'DELETE' }),
};

/* ── Toast notifications ── */
function toast(message, type = 'info') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }
  const icons = { success: '✅', danger: '❌', warning: '⚠️', info: 'ℹ️' };
  const t = document.createElement('div');
  t.className = `toast ${type}`;
  t.innerHTML = `<span>${icons[type] || icons.info}</span><span>${message}</span>`;
  container.appendChild(t);
  setTimeout(() => t.remove(), 3500);
}

/* ── Modal helpers ── */
function openModal(id) {
  document.getElementById(id).classList.add('open');
}
function closeModal(id) {
  document.getElementById(id).classList.remove('open');
}

/* ── Sidebar active state ── */
function initSidebar() {
  const username = getUsername();
  const uname = document.getElementById('sidebarUsername');
  const uavatar = document.getElementById('sidebarAvatar');
  if (uname) uname.textContent = username;
  if (uavatar) uavatar.textContent = username.charAt(0).toUpperCase();

  const current = window.location.pathname.split('/').pop();
  document.querySelectorAll('.nav-item').forEach(a => {
    a.classList.toggle('active', a.getAttribute('href') === current);
  });
}

/* ── Formatters ── */
function fmtDate(iso) {
  if (!iso) return '—';
  return new Date(iso).toLocaleString('es-CO', {
    day: '2-digit', month: '2-digit', year: 'numeric',
    hour: '2-digit', minute: '2-digit'
  });
}
function fmtNum(n) {
  return Number(n).toLocaleString('es-CO');
}

/* ── Confirm dialog ── */
function confirmAction(msg) {
  return confirm(msg);
}

/* ── Sidebar HTML generator ── */
function getSidebarHTML() {
  const role = getUserRole();
  const isAdmin = role === 'ADMIN';
  const isSupervisor = role === 'SUPERVISOR' || isAdmin;
  const isOperario = role === 'OPERARIO';

  return `
  <aside class="sidebar" id="sidebar">
    <div class="sidebar-brand">
      <div class="brand-hex">S</div>
      <span class="brand-text">Stock<span>Core</span></span>
    </div>
    <nav class="sidebar-nav">
      <div class="nav-section">Principal</div>
      <a class="nav-item" href="dashboard.html">
        <span class="icon">📊</span> Dashboard
      </a>
      <a class="nav-item" href="inventario.html">
        <span class="icon">📦</span> Inventario
      </a>
      <a class="nav-item" href="movimientos.html">
        <span class="icon">🔄</span> Movimientos
      </a>
      ${isSupervisor ? `<a class="nav-item" href="reportes.html">
        <span class="icon">📈</span> Reportes
      </a>` : ''}

      <div class="nav-section">Administración</div>
      <a class="nav-item" href="productos.html">
        <span class="icon">🏷️</span> Productos
      </a>
      <a class="nav-item" href="bodegas.html">
        <span class="icon">🏭</span> Bodegas
      </a>
      ${isAdmin ? `<a class="nav-item" href="usuarios.html">
        <span class="icon">👥</span> Usuarios
      </a>
      <a class="nav-item" href="roles.html">
        <span class="icon">🔑</span> Roles
      </a>
      <a class="nav-item" href="personas.html">
        <span class="icon">👤</span> Personas
      </a>
      <a class="nav-item" href="departamentos.html">
        <span class="icon">🗺️</span> Departamentos
      </a>
      <a class="nav-item" href="ciudades.html">
        <span class="icon">🏙️</span> Ciudades
      </a>` : ''}
      ${isSupervisor ? `<a class="nav-item" href="auditoria.html">
        <span class="icon">🛡️</span> Auditoría
      </a>` : ''}
    </nav>
    <div class="sidebar-footer">
      <div class="user-card">
        <div class="user-avatar" id="sidebarAvatar">U</div>
        <div>
          <div class="user-name" id="sidebarUsername">Usuario</div>
          <div class="user-role">${role}</div>
        </div>
      </div>
      <button class="btn-logout" onclick="logout()">⬅ Cerrar sesión</button>
    </div>
  </aside>`;
}
