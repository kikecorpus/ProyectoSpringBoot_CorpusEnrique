// ── Guard ────────────────────────────────────────────────────
if (!localStorage.getItem('token')) {
  window.location.href = 'index.html';
}

// ── Estado global ────────────────────────────────────────────
const state = {
  currentModule: 'dashboard',
  editingId: null,
  cache: {}
};

// ── Utils ────────────────────────────────────────────────────
function toast(msg, type = 'success') {
  const el = document.createElement('div');
  el.className = `toast ${type}`;
  const icons = { success: '✓', error: '✕', info: 'ℹ' };
  el.innerHTML = `<span>${icons[type]}</span><span>${msg}</span>`;
  document.getElementById('toastContainer').appendChild(el);
  setTimeout(() => el.remove(), 3500);
}

function openModal(title) {
  document.getElementById('modalTitle').textContent = title;
  document.getElementById('modal').classList.add('open');
}

function closeModal() {
  document.getElementById('modal').classList.remove('open');
  document.getElementById('modalBody').innerHTML = '';
  state.editingId = null;
}

function badgeEstado(estado) {
  return estado === 'ACTIVO'
    ? `<span class="badge badge-green">${estado}</span>`
    : `<span class="badge badge-red">${estado}</span>`;
}

function setPageTitle(title) {
  document.getElementById('pageTitle').textContent = title;
}

function showLoading(tbodyId) {
  document.getElementById(tbodyId).innerHTML =
    `<tr class="loading-row"><td colspan="99">Cargando...</td></tr>`;
}

function showEmpty(tbodyId, msg = 'Sin registros') {
  document.getElementById(tbodyId).innerHTML =
    `<tr><td colspan="99"><div class="empty-state">
      <div class="empty-icon">📦</div><p>${msg}</p>
    </div></td></tr>`;
}

function confirmDelete(nombre, fn) {
  if (confirm(`¿Eliminar "${nombre}"? Esta acción no se puede deshacer.`)) fn();
}

// ── Nav ──────────────────────────────────────────────────────
function navigate(module) {
  state.currentModule = module;
  document.querySelectorAll('.nav-item').forEach(el => {
    el.classList.toggle('active', el.dataset.module === module);
  });

  const titles = {
    dashboard:    'Dashboard',
    departamento: 'Departamentos',
    categoria:    'Categorías',
    ciudad:       'Ciudades',
    rol:          'Roles',
    persona:      'Personas',
    usuario:      'Usuarios',
    bodega:       'Bodegas',
    producto:     'Productos',
    inventario:   'Inventario',
    movimiento:   'Movimientos',
    auditoria:    'Auditoría',
  };
  setPageTitle(titles[module] || module);

  const modules = {
    dashboard,
    departamento: modDepartamento,
    categoria: modCategoria,
    ciudad: modCiudad,
    rol: modRol,
    persona: modPersona,
    usuario: modUsuario,
    bodega: modBodega,
    producto: modProducto,
    inventario: modInventario,
    movimiento: modMovimiento,
    auditoria: modAuditoria,
  };

  const content = document.getElementById('content');
  content.innerHTML = '';
  if (modules[module]) modules[module](content);
}

// ── DASHBOARD ────────────────────────────────────────────────
async function dashboard(content) {
  content.innerHTML = `
    <div class="stats-grid" id="statsGrid">
      <div class="stat-card amber" data-icon="📦">
        <div class="stat-label">Productos</div>
        <div class="stat-value" id="statProductos">—</div>
      </div>
      <div class="stat-card green" data-icon="🏭">
        <div class="stat-label">Bodegas</div>
        <div class="stat-value" id="statBodegas">—</div>
      </div>
      <div class="stat-card blue" data-icon="📋">
        <div class="stat-label">Inventarios</div>
        <div class="stat-value" id="statInventarios">—</div>
      </div>
      <div class="stat-card red" data-icon="⚠️">
        <div class="stat-label">Stock Crítico</div>
        <div class="stat-value" id="statCritico">—</div>
      </div>
    </div>

    <div class="table-card">
      <div class="table-header">
        <span class="table-title">⚠ Inventarios con Stock Crítico</span>
      </div>
      <table>
        <thead>
          <tr>
            <th>Producto</th>
            <th>Bodega</th>
            <th>Actual</th>
            <th>Mínimo</th>
            <th>Estado</th>
          </tr>
        </thead>
        <tbody id="tbodyCritico"></tbody>
      </table>
    </div>`;

  try {
    const [productos, bodegas, inventarios, criticos] = await Promise.all([
      API.getProductos(), API.getBodegas(),
      API.getInventarios(), API.getStockCritico()
    ]);

    document.getElementById('statProductos').textContent   = productos.length;
    document.getElementById('statBodegas').textContent     = bodegas.length;
    document.getElementById('statInventarios').textContent = inventarios.length;
    document.getElementById('statCritico').textContent     = criticos.length;

    const tbody = document.getElementById('tbodyCritico');
    if (!criticos.length) {
      tbody.innerHTML = `<tr><td colspan="5"><div class="empty-state">
        <div class="empty-icon">✅</div><p>Sin stock crítico</p>
      </div></td></tr>`;
    } else {
      tbody.innerHTML = criticos.map(i => `
        <tr>
          <td>${i.producto.nombre} <span class="mono">${i.producto.codigo}</span></td>
          <td>${i.bodega.nombre}</td>
          <td><strong style="color:var(--danger)">${i.cantidadActual}</strong></td>
          <td class="mono">${i.stockMinimo}</td>
          <td>${badgeEstado(i.estado)}</td>
        </tr>`).join('');
    }
  } catch(e) { toast('Error cargando dashboard', 'error'); }
}

// ── MÓDULO GENÉRICO CRUD ─────────────────────────────────────
function crudModule(content, { title, columns, fetchAll, renderRow, openCreate, openEdit, doDelete }) {
  content.innerHTML = `
    <div class="table-card">
      <div class="table-header">
        <span class="table-title">${title}</span>
        <div style="display:flex;gap:.6rem;align-items:center">
          <input class="search-input" id="searchInput" placeholder="Buscar..."/>
          <button class="btn-primary" onclick="openCreateCurrent()">+ Nuevo</button>
        </div>
      </div>
      <table>
        <thead><tr>${columns.map(c=>`<th>${c}</th>`).join('')}<th>Acciones</th></tr></thead>
        <tbody id="tbodyMain"></tbody>
      </table>
    </div>`;

  window.openCreateCurrent = openCreate;
  window.openEditCurrent   = openEdit;
  window.doDeleteCurrent   = doDelete;

  let allData = [];

  async function load() {
    showLoading('tbodyMain');
    try {
      allData = await fetchAll();
      render(allData);
    } catch(e) { toast('Error cargando datos', 'error'); }
  }

  function render(data) {
    const tbody = document.getElementById('tbodyMain');
    if (!data.length) { showEmpty('tbodyMain'); return; }
    tbody.innerHTML = data.map(renderRow).join('');
  }

  document.getElementById('searchInput').addEventListener('input', e => {
    const q = e.target.value.toLowerCase();
    render(allData.filter(d => JSON.stringify(d).toLowerCase().includes(q)));
  });

  load();
  window.reloadCurrent = load;
}

// ── DEPARTAMENTOS ────────────────────────────────────────────
function modDepartamento(content) {
  crudModule(content, {
    title: 'Departamentos',
    columns: ['ID', 'Nombre'],
    fetchAll: API.getDepartamentos,
    renderRow: d => `<tr>
      <td class="mono">${d.idDepartamento}</td>
      <td>${d.nombre}</td>
      <td><div class="actions-cell">
        <button class="btn-icon edit" onclick="openEditCurrent(${JSON.stringify(d).replace(/"/g,"&quot;")})">✎</button>
        <button class="btn-icon del" onclick="confirmDelete('${d.nombre}', ()=>doDeleteCurrent(${d.idDepartamento}))">✕</button>
      </div></td>
    </tr>`,
    openCreate: () => {
      openModal('Nuevo Departamento');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group">
            <label class="form-label">Nombre</label>
            <input class="form-control" id="fNombre" placeholder="Antioquia"/>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.createDepartamento({ nombre: document.getElementById('fNombre').value });
          toast('Departamento creado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    openEdit: (d) => {
      state.editingId = d.idDepartamento;
      openModal('Editar Departamento');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group">
            <label class="form-label">Nombre</label>
            <input class="form-control" id="fNombre" value="${d.nombre}"/>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.updateDepartamento(state.editingId, { nombre: document.getElementById('fNombre').value });
          toast('Actualizado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    doDelete: async (id) => {
      try { await API.deleteDepartamento(id); toast('Eliminado'); reloadCurrent(); }
      catch(e) { toast(e.message, 'error'); }
    }
  });
}

// ── CATEGORÍAS ───────────────────────────────────────────────
function modCategoria(content) {
  crudModule(content, {
    title: 'Categorías',
    columns: ['ID', 'Nombre'],
    fetchAll: API.getCategorias,
    renderRow: c => `<tr>
      <td class="mono">${c.idCategoria}</td>
      <td>${c.nombre}</td>
      <td><div class="actions-cell">
        <button class="btn-icon edit" onclick="openEditCurrent(${JSON.stringify(c).replace(/"/g,"&quot;")})">✎</button>
        <button class="btn-icon del" onclick="confirmDelete('${c.nombre}', ()=>doDeleteCurrent(${c.idCategoria}))">✕</button>
      </div></td>
    </tr>`,
    openCreate: () => {
      openModal('Nueva Categoría');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group">
            <label class="form-label">Nombre</label>
            <input class="form-control" id="fNombre" placeholder="Electrónica"/>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.createCategoria({ nombre: document.getElementById('fNombre').value });
          toast('Categoría creada'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    openEdit: (c) => {
      state.editingId = c.idCategoria;
      openModal('Editar Categoría');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group">
            <label class="form-label">Nombre</label>
            <input class="form-control" id="fNombre" value="${c.nombre}"/>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.updateCategoria(state.editingId, { nombre: document.getElementById('fNombre').value });
          toast('Actualizado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    doDelete: async (id) => {
      try { await API.deleteCategoria(id); toast('Eliminado'); reloadCurrent(); }
      catch(e) { toast(e.message, 'error'); }
    }
  });
}

// ── CIUDADES ─────────────────────────────────────────────────
function modCiudad(content) {
  crudModule(content, {
    title: 'Ciudades',
    columns: ['ID', 'Nombre', 'Departamento'],
    fetchAll: API.getCiudades,
    renderRow: c => `<tr>
      <td class="mono">${c.idCiudad}</td>
      <td>${c.nombre}</td>
      <td><span class="badge badge-muted">${c.departamento.nombre}</span></td>
      <td><div class="actions-cell">
        <button class="btn-icon edit" onclick="openEditCurrent(${JSON.stringify(c).replace(/"/g,"&quot;")})">✎</button>
        <button class="btn-icon del" onclick="confirmDelete('${c.nombre}', ()=>doDeleteCurrent(${c.idCiudad}))">✕</button>
      </div></td>
    </tr>`,
    openCreate: async () => {
      const deps = await API.getDepartamentos();
      openModal('Nueva Ciudad');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group">
            <label class="form-label">Nombre</label>
            <input class="form-control" id="fNombre" placeholder="Medellín"/>
          </div>
          <div class="form-group">
            <label class="form-label">Departamento</label>
            <select class="form-control" id="fDep">
              ${deps.map(d=>`<option value="${d.idDepartamento}">${d.nombre}</option>`).join('')}
            </select>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.createCiudad({ nombre: document.getElementById('fNombre').value, departamentoId: +document.getElementById('fDep').value });
          toast('Ciudad creada'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    openEdit: async (c) => {
      state.editingId = c.idCiudad;
      const deps = await API.getDepartamentos();
      openModal('Editar Ciudad');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group">
            <label class="form-label">Nombre</label>
            <input class="form-control" id="fNombre" value="${c.nombre}"/>
          </div>
          <div class="form-group">
            <label class="form-label">Departamento</label>
            <select class="form-control" id="fDep">
              ${deps.map(d=>`<option value="${d.idDepartamento}" ${d.idDepartamento===c.departamento.idDepartamento?'selected':''}>${d.nombre}</option>`).join('')}
            </select>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.updateCiudad(state.editingId, { nombre: document.getElementById('fNombre').value, departamentoId: +document.getElementById('fDep').value });
          toast('Actualizado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    doDelete: async (id) => {
      try { await API.deleteCiudad(id); toast('Eliminado'); reloadCurrent(); }
      catch(e) { toast(e.message, 'error'); }
    }
  });
}

// ── ROLES ────────────────────────────────────────────────────
function modRol(content) {
  crudModule(content, {
    title: 'Roles',
    columns: ['ID', 'Nombre', 'Descripción'],
    fetchAll: API.getRoles,
    renderRow: r => `<tr>
      <td class="mono">${r.idRol}</td>
      <td><span class="badge badge-amber">${r.nombre}</span></td>
      <td>${r.descripcion || '<span style="color:var(--muted)">—</span>'}</td>
      <td><div class="actions-cell">
        <button class="btn-icon edit" onclick="openEditCurrent(${JSON.stringify(r).replace(/"/g,"&quot;")})">✎</button>
        <button class="btn-icon del" onclick="confirmDelete('${r.nombre}', ()=>doDeleteCurrent(${r.idRol}))">✕</button>
      </div></td>
    </tr>`,
    openCreate: () => {
      openModal('Nuevo Rol');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group"><label class="form-label">Nombre</label><input class="form-control" id="fNombre" placeholder="ADMIN"/></div>
          <div class="form-group"><label class="form-label">Descripción</label><input class="form-control" id="fDesc" placeholder="Descripción del rol"/></div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.createRol({ nombre: document.getElementById('fNombre').value, descripcion: document.getElementById('fDesc').value });
          toast('Rol creado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    openEdit: (r) => {
      state.editingId = r.idRol;
      openModal('Editar Rol');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group"><label class="form-label">Nombre</label><input class="form-control" id="fNombre" value="${r.nombre}"/></div>
          <div class="form-group"><label class="form-label">Descripción</label><input class="form-control" id="fDesc" value="${r.descripcion || ''}"/></div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.updateRol(state.editingId, { nombre: document.getElementById('fNombre').value, descripcion: document.getElementById('fDesc').value });
          toast('Actualizado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    doDelete: async (id) => {
      try { await API.deleteRol(id); toast('Eliminado'); reloadCurrent(); }
      catch(e) { toast(e.message, 'error'); }
    }
  });
}

// ── PERSONAS ─────────────────────────────────────────────────
function modPersona(content) {
  const tiposDoc = ['CC','CE','PASAPORTE','NIT','TI'];
  crudModule(content, {
    title: 'Personas',
    columns: ['ID', 'Nombre completo', 'Documento'],
    fetchAll: API.getPersonas,
    renderRow: p => `<tr>
      <td class="mono">${p.idPersona}</td>
      <td>${p.nombre} ${p.apellido}</td>
      <td><span class="badge badge-muted">${p.tipoDocumento}</span> <span class="mono">${p.numeroDocumento}</span></td>
      <td><div class="actions-cell">
        <button class="btn-icon edit" onclick="openEditCurrent(${JSON.stringify(p).replace(/"/g,"&quot;")})">✎</button>
        <button class="btn-icon del" onclick="confirmDelete('${p.nombre}', ()=>doDeleteCurrent(${p.idPersona}))">✕</button>
      </div></td>
    </tr>`,
    openCreate: () => {
      openModal('Nueva Persona');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid" style="grid-template-columns:1fr 1fr">
          <div class="form-group"><label class="form-label">Nombre</label><input class="form-control" id="fNombre" placeholder="Juan"/></div>
          <div class="form-group"><label class="form-label">Apellido</label><input class="form-control" id="fApellido" placeholder="Pérez"/></div>
          <div class="form-group"><label class="form-label">Tipo Doc</label>
            <select class="form-control" id="fTipo">${tiposDoc.map(t=>`<option>${t}</option>`).join('')}</select>
          </div>
          <div class="form-group"><label class="form-label">N° Documento</label><input class="form-control" id="fDoc" placeholder="1234567890"/></div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.createPersona({ nombre: document.getElementById('fNombre').value, apellido: document.getElementById('fApellido').value, tipoDocumento: document.getElementById('fTipo').value, numeroDocumento: document.getElementById('fDoc').value });
          toast('Persona creada'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    openEdit: (p) => {
      state.editingId = p.idPersona;
      openModal('Editar Persona');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid" style="grid-template-columns:1fr 1fr">
          <div class="form-group"><label class="form-label">Nombre</label><input class="form-control" id="fNombre" value="${p.nombre}"/></div>
          <div class="form-group"><label class="form-label">Apellido</label><input class="form-control" id="fApellido" value="${p.apellido}"/></div>
          <div class="form-group"><label class="form-label">Tipo Doc</label>
            <select class="form-control" id="fTipo">${tiposDoc.map(t=>`<option ${t===p.tipoDocumento?'selected':''}>${t}</option>`).join('')}</select>
          </div>
          <div class="form-group"><label class="form-label">N° Documento</label><input class="form-control" id="fDoc" value="${p.numeroDocumento}"/></div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.updatePersona(state.editingId, { nombre: document.getElementById('fNombre').value, apellido: document.getElementById('fApellido').value, tipoDocumento: document.getElementById('fTipo').value, numeroDocumento: document.getElementById('fDoc').value });
          toast('Actualizado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    doDelete: async (id) => {
      try { await API.deletePersona(id); toast('Eliminado'); reloadCurrent(); }
      catch(e) { toast(e.message, 'error'); }
    }
  });
}

// ── USUARIOS ─────────────────────────────────────────────────
function modUsuario(content) {
  crudModule(content, {
    title: 'Usuarios',
    columns: ['ID', 'Username', 'Persona', 'Rol', 'Estado'],
    fetchAll: API.getUsuarios,
    renderRow: u => `<tr>
      <td class="mono">${u.idUsuario}</td>
      <td><strong>${u.username}</strong></td>
      <td>${u.persona.nombre} ${u.persona.apellido}</td>
      <td><span class="badge badge-amber">${u.rol.nombre}</span></td>
      <td>${badgeEstado(u.estado)}</td>
      <td><div class="actions-cell">
        <button class="btn-icon edit" onclick="openEditCurrent(${JSON.stringify(u).replace(/"/g,"&quot;")})">✎</button>
        <button class="btn-icon del" onclick="confirmDelete('${u.username}', ()=>doDeleteCurrent(${u.idUsuario}))">✕</button>
      </div></td>
    </tr>`,
    openCreate: async () => {
      const [personas, roles] = await Promise.all([API.getPersonas(), API.getRoles()]);
      openModal('Nuevo Usuario');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group"><label class="form-label">Username</label><input class="form-control" id="fUser" placeholder="juan.perez"/></div>
          <div class="form-group"><label class="form-label">Contraseña</label><input class="form-control" type="password" id="fPass" placeholder="Mínimo 8 caracteres"/></div>
          <div class="form-group"><label class="form-label">Estado</label>
            <select class="form-control" id="fEstado"><option>ACTIVO</option><option>INACTIVO</option></select>
          </div>
          <div class="form-group"><label class="form-label">Persona</label>
            <select class="form-control" id="fPersona">${personas.map(p=>`<option value="${p.idPersona}">${p.nombre} ${p.apellido}</option>`).join('')}</select>
          </div>
          <div class="form-group"><label class="form-label">Rol</label>
            <select class="form-control" id="fRol">${roles.map(r=>`<option value="${r.idRol}">${r.nombre}</option>`).join('')}</select>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.createUsuario({ username: document.getElementById('fUser').value, contrasena: document.getElementById('fPass').value, estado: document.getElementById('fEstado').value, personaId: +document.getElementById('fPersona').value, rolId: +document.getElementById('fRol').value });
          toast('Usuario creado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    openEdit: async (u) => {
      state.editingId = u.idUsuario;
      const [personas, roles] = await Promise.all([API.getPersonas(), API.getRoles()]);
      openModal('Editar Usuario');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group"><label class="form-label">Username</label><input class="form-control" id="fUser" value="${u.username}"/></div>
          <div class="form-group"><label class="form-label">Nueva Contraseña</label><input class="form-control" type="password" id="fPass" placeholder="Dejar vacío para no cambiar"/></div>
          <div class="form-group"><label class="form-label">Estado</label>
            <select class="form-control" id="fEstado"><option ${u.estado==='ACTIVO'?'selected':''}>ACTIVO</option><option ${u.estado==='INACTIVO'?'selected':''}>INACTIVO</option></select>
          </div>
          <div class="form-group"><label class="form-label">Persona</label>
            <select class="form-control" id="fPersona">${personas.map(p=>`<option value="${p.idPersona}" ${p.idPersona===u.persona.idPersona?'selected':''}>${p.nombre} ${p.apellido}</option>`).join('')}</select>
          </div>
          <div class="form-group"><label class="form-label">Rol</label>
            <select class="form-control" id="fRol">${roles.map(r=>`<option value="${r.idRol}" ${r.idRol===u.rol.idRol?'selected':''}>${r.nombre}</option>`).join('')}</select>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.updateUsuario(state.editingId, { username: document.getElementById('fUser').value, contrasena: document.getElementById('fPass').value || u.contrasena, estado: document.getElementById('fEstado').value, personaId: +document.getElementById('fPersona').value, rolId: +document.getElementById('fRol').value });
          toast('Actualizado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    doDelete: async (id) => {
      try { await API.deleteUsuario(id); toast('Eliminado'); reloadCurrent(); }
      catch(e) { toast(e.message, 'error'); }
    }
  });
}

// ── BODEGAS ──────────────────────────────────────────────────
function modBodega(content) {
  crudModule(content, {
    title: 'Bodegas',
    columns: ['ID', 'Nombre', 'Ciudad', 'Capacidad', 'Estado'],
    fetchAll: API.getBodegas,
    renderRow: b => `<tr>
      <td class="mono">${b.idBodega}</td>
      <td><strong>${b.nombre}</strong><br><small style="color:var(--muted)">${b.direccion}</small></td>
      <td>${b.ciudad.nombre}</td>
      <td class="mono">${b.capacidad}</td>
      <td>${badgeEstado(b.estado)}</td>
      <td><div class="actions-cell">
        <button class="btn-icon edit" onclick="openEditCurrent(${JSON.stringify(b).replace(/"/g,"&quot;")})">✎</button>
        <button class="btn-icon del" onclick="confirmDelete('${b.nombre}', ()=>doDeleteCurrent(${b.idBodega}))">✕</button>
      </div></td>
    </tr>`,
    openCreate: async () => {
      const [ciudades, usuarios] = await Promise.all([API.getCiudades(), API.getUsuarios()]);
      openModal('Nueva Bodega');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group"><label class="form-label">Nombre</label><input class="form-control" id="fNombre" placeholder="Bodega Central"/></div>
          <div class="form-group"><label class="form-label">Dirección</label><input class="form-control" id="fDir" placeholder="Calle 50 # 30-20"/></div>
          <div class="form-group"><label class="form-label">Capacidad</label><input class="form-control" type="number" id="fCap" placeholder="1000"/></div>
          <div class="form-group"><label class="form-label">Estado</label>
            <select class="form-control" id="fEstado"><option>ACTIVO</option><option>INACTIVO</option></select>
          </div>
          <div class="form-group"><label class="form-label">Ciudad</label>
            <select class="form-control" id="fCiudad">${ciudades.map(c=>`<option value="${c.idCiudad}">${c.nombre}</option>`).join('')}</select>
          </div>
          <div class="form-group"><label class="form-label">Encargado</label>
            <select class="form-control" id="fEnc">${usuarios.map(u=>`<option value="${u.idUsuario}">${u.username}</option>`).join('')}</select>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.createBodega({ nombre: document.getElementById('fNombre').value, direccion: document.getElementById('fDir').value, capacidad: +document.getElementById('fCap').value, estado: document.getElementById('fEstado').value, ciudadId: +document.getElementById('fCiudad').value, encargadoId: +document.getElementById('fEnc').value });
          toast('Bodega creada'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    openEdit: async (b) => {
      state.editingId = b.idBodega;
      const [ciudades, usuarios] = await Promise.all([API.getCiudades(), API.getUsuarios()]);
      openModal('Editar Bodega');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group"><label class="form-label">Nombre</label><input class="form-control" id="fNombre" value="${b.nombre}"/></div>
          <div class="form-group"><label class="form-label">Dirección</label><input class="form-control" id="fDir" value="${b.direccion}"/></div>
          <div class="form-group"><label class="form-label">Capacidad</label><input class="form-control" type="number" id="fCap" value="${b.capacidad}"/></div>
          <div class="form-group"><label class="form-label">Estado</label>
            <select class="form-control" id="fEstado"><option ${b.estado==='ACTIVO'?'selected':''}>ACTIVO</option><option ${b.estado==='INACTIVO'?'selected':''}>INACTIVO</option></select>
          </div>
          <div class="form-group"><label class="form-label">Ciudad</label>
            <select class="form-control" id="fCiudad">${ciudades.map(c=>`<option value="${c.idCiudad}" ${c.idCiudad===b.ciudad.idCiudad?'selected':''}>${c.nombre}</option>`).join('')}</select>
          </div>
          <div class="form-group"><label class="form-label">Encargado</label>
            <select class="form-control" id="fEnc">${usuarios.map(u=>`<option value="${u.idUsuario}" ${u.idUsuario===b.encargado?.idUsuario?'selected':''}>${u.username}</option>`).join('')}</select>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.updateBodega(state.editingId, { nombre: document.getElementById('fNombre').value, direccion: document.getElementById('fDir').value, capacidad: +document.getElementById('fCap').value, estado: document.getElementById('fEstado').value, ciudadId: +document.getElementById('fCiudad').value, encargadoId: +document.getElementById('fEnc').value });
          toast('Actualizado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    doDelete: async (id) => {
      try { await API.deleteBodega(id); toast('Eliminado'); reloadCurrent(); }
      catch(e) { toast(e.message, 'error'); }
    }
  });
}

// ── PRODUCTOS ────────────────────────────────────────────────
function modProducto(content) {
  crudModule(content, {
    title: 'Productos',
    columns: ['ID', 'Nombre', 'Código', 'Unidad', 'Categoría'],
    fetchAll: API.getProductos,
    renderRow: p => `<tr>
      <td class="mono">${p.idProducto}</td>
      <td><strong>${p.nombre}</strong></td>
      <td class="mono">${p.codigo}</td>
      <td>${p.unidadMedida}</td>
      <td><span class="badge badge-muted">${p.categoria.nombre}</span></td>
      <td><div class="actions-cell">
        <button class="btn-icon edit" onclick="openEditCurrent(${JSON.stringify(p).replace(/"/g,"&quot;")})">✎</button>
        <button class="btn-icon del" onclick="confirmDelete('${p.nombre}', ()=>doDeleteCurrent(${p.idProducto}))">✕</button>
      </div></td>
    </tr>`,
    openCreate: async () => {
      const cats = await API.getCategorias();
      openModal('Nuevo Producto');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group"><label class="form-label">Nombre</label><input class="form-control" id="fNombre" placeholder="Laptop HP 15"/></div>
          <div class="form-group"><label class="form-label">Código</label><input class="form-control" id="fCod" placeholder="PROD-001"/></div>
          <div class="form-group"><label class="form-label">Unidad de Medida</label><input class="form-control" id="fUnidad" placeholder="UNIDAD"/></div>
          <div class="form-group"><label class="form-label">Categoría</label>
            <select class="form-control" id="fCat">${cats.map(c=>`<option value="${c.idCategoria}">${c.nombre}</option>`).join('')}</select>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.createProducto({ nombre: document.getElementById('fNombre').value, codigo: document.getElementById('fCod').value, unidadMedida: document.getElementById('fUnidad').value, categoriaId: +document.getElementById('fCat').value });
          toast('Producto creado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    openEdit: async (p) => {
      state.editingId = p.idProducto;
      const cats = await API.getCategorias();
      openModal('Editar Producto');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group"><label class="form-label">Nombre</label><input class="form-control" id="fNombre" value="${p.nombre}"/></div>
          <div class="form-group"><label class="form-label">Código</label><input class="form-control" id="fCod" value="${p.codigo}"/></div>
          <div class="form-group"><label class="form-label">Unidad de Medida</label><input class="form-control" id="fUnidad" value="${p.unidadMedida}"/></div>
          <div class="form-group"><label class="form-label">Categoría</label>
            <select class="form-control" id="fCat">${cats.map(c=>`<option value="${c.idCategoria}" ${c.idCategoria===p.categoria.idCategoria?'selected':''}>${c.nombre}</option>`).join('')}</select>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.updateProducto(state.editingId, { nombre: document.getElementById('fNombre').value, codigo: document.getElementById('fCod').value, unidadMedida: document.getElementById('fUnidad').value, categoriaId: +document.getElementById('fCat').value });
          toast('Actualizado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    doDelete: async (id) => {
      try { await API.deleteProducto(id); toast('Eliminado'); reloadCurrent(); }
      catch(e) { toast(e.message, 'error'); }
    }
  });
}

// ── INVENTARIO ───────────────────────────────────────────────
function modInventario(content) {
  crudModule(content, {
    title: 'Inventario',
    columns: ['ID', 'Producto', 'Bodega', 'Actual', 'Mín', 'Máx', 'Estado'],
    fetchAll: API.getInventarios,
    renderRow: i => `<tr>
      <td class="mono">${i.idInventario}</td>
      <td>${i.producto.nombre}<br><small class="mono" style="color:var(--muted)">${i.producto.codigo}</small></td>
      <td>${i.bodega.nombre}</td>
      <td><strong style="color:${i.stockCritico?'var(--danger)':'var(--text)'}">${i.cantidadActual}</strong>${i.stockCritico?'<span class="critico-dot" style="margin-left:5px"></span>':''}</td>
      <td class="mono">${i.stockMinimo}</td>
      <td class="mono">${i.stockMaximo}</td>
      <td>${badgeEstado(i.estado)}</td>
      <td><div class="actions-cell">
        <button class="btn-icon edit" onclick="openEditCurrent(${JSON.stringify(i).replace(/"/g,"&quot;")})">✎</button>
        <button class="btn-icon del" onclick="confirmDelete('inv#${i.idInventario}', ()=>doDeleteCurrent(${i.idInventario}))">✕</button>
      </div></td>
    </tr>`,
    openCreate: async () => {
      const [productos, bodegas] = await Promise.all([API.getProductos(), API.getBodegas()]);
      openModal('Nuevo Inventario');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group"><label class="form-label">Producto</label>
            <select class="form-control" id="fProd">${productos.map(p=>`<option value="${p.idProducto}">${p.nombre}</option>`).join('')}</select>
          </div>
          <div class="form-group"><label class="form-label">Bodega</label>
            <select class="form-control" id="fBod">${bodegas.map(b=>`<option value="${b.idBodega}">${b.nombre}</option>`).join('')}</select>
          </div>
          <div class="form-group"><label class="form-label">Cantidad Actual</label><input class="form-control" type="number" id="fActual" placeholder="50"/></div>
          <div class="form-group"><label class="form-label">Stock Mínimo</label><input class="form-control" type="number" id="fMin" placeholder="10"/></div>
          <div class="form-group"><label class="form-label">Stock Máximo</label><input class="form-control" type="number" id="fMax" placeholder="200"/></div>
          <div class="form-group"><label class="form-label">Estado</label>
            <select class="form-control" id="fEstado"><option>ACTIVO</option><option>INACTIVO</option></select>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.createInventario({ productoId: +document.getElementById('fProd').value, bodegaId: +document.getElementById('fBod').value, cantidadActual: +document.getElementById('fActual').value, stockMinimo: +document.getElementById('fMin').value, stockMaximo: +document.getElementById('fMax').value, estado: document.getElementById('fEstado').value });
          toast('Inventario creado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    openEdit: async (i) => {
      state.editingId = i.idInventario;
      const [productos, bodegas] = await Promise.all([API.getProductos(), API.getBodegas()]);
      openModal('Editar Inventario');
      document.getElementById('modalBody').innerHTML = `
        <div class="form-grid">
          <div class="form-group"><label class="form-label">Producto</label>
            <select class="form-control" id="fProd">${productos.map(p=>`<option value="${p.idProducto}" ${p.idProducto===i.producto.idProducto?'selected':''}>${p.nombre}</option>`).join('')}</select>
          </div>
          <div class="form-group"><label class="form-label">Bodega</label>
            <select class="form-control" id="fBod">${bodegas.map(b=>`<option value="${b.idBodega}" ${b.idBodega===i.bodega.idBodega?'selected':''}>${b.nombre}</option>`).join('')}</select>
          </div>
          <div class="form-group"><label class="form-label">Cantidad Actual</label><input class="form-control" type="number" id="fActual" value="${i.cantidadActual}"/></div>
          <div class="form-group"><label class="form-label">Stock Mínimo</label><input class="form-control" type="number" id="fMin" value="${i.stockMinimo}"/></div>
          <div class="form-group"><label class="form-label">Stock Máximo</label><input class="form-control" type="number" id="fMax" value="${i.stockMaximo}"/></div>
          <div class="form-group"><label class="form-label">Estado</label>
            <select class="form-control" id="fEstado"><option ${i.estado==='ACTIVO'?'selected':''}>ACTIVO</option><option ${i.estado==='INACTIVO'?'selected':''}>INACTIVO</option></select>
          </div>
        </div>`;
      window.saveModal = async () => {
        try {
          await API.updateInventario(state.editingId, { productoId: +document.getElementById('fProd').value, bodegaId: +document.getElementById('fBod').value, cantidadActual: +document.getElementById('fActual').value, stockMinimo: +document.getElementById('fMin').value, stockMaximo: +document.getElementById('fMax').value, estado: document.getElementById('fEstado').value });
          toast('Actualizado'); closeModal(); reloadCurrent();
        } catch(e) { toast(e.message, 'error'); }
      };
    },
    doDelete: async (id) => {
      try { await API.deleteInventario(id); toast('Eliminado'); reloadCurrent(); }
      catch(e) { toast(e.message, 'error'); }
    }
  });
}

// ── MOVIMIENTOS ──────────────────────────────────────────────
async function modMovimiento(content) {
  content.innerHTML = `
    <div class="table-card">
      <div class="table-header">
        <span class="table-title">Movimientos de Inventario</span>
        <button class="btn-primary" onclick="openCreateMovimiento()">+ Registrar</button>
      </div>
      <table>
        <thead><tr><th>ID</th><th>Tipo</th><th>Origen</th><th>Destino</th><th>Cantidad</th><th>Antes</th><th>Después</th><th>Usuario</th><th>Fecha</th></tr></thead>
        <tbody id="tbodyMain"></tbody>
      </table>
    </div>`;

  const badgeTipo = t => {
    const map = { ENTRADA:'badge-green', SALIDA:'badge-red', TRASLADO:'badge-blue', AJUSTE:'badge-amber' };
    return `<span class="badge ${map[t]||'badge-muted'}">${t}</span>`;
  };

  async function load() {
    showLoading('tbodyMain');
    try {
      const movs = await API.getMovimientos();
      const tbody = document.getElementById('tbodyMain');
      if (!movs.length) { showEmpty('tbodyMain', 'Sin movimientos registrados'); return; }
      tbody.innerHTML = movs.map(m => `<tr>
        <td class="mono">${m.idMovimiento}</td>
        <td>${badgeTipo(m.tipoMovimiento)}</td>
        <td>${m.inventarioOrigen ? m.inventarioOrigen.nombreProducto : '<span style="color:var(--muted)">—</span>'}</td>
        <td>${m.inventarioDestino ? m.inventarioDestino.nombreProducto : '<span style="color:var(--muted)">—</span>'}</td>
        <td class="mono"><strong>${m.cantidad}</strong></td>
        <td class="mono">${m.cantidadAnterior}</td>
        <td class="mono">${m.cantidadPosterior}</td>
        <td>${m.usuario.username}</td>
        <td class="mono">${new Date(m.fecha).toLocaleDateString('es-CO')}</td>
      </tr>`).join('');
    } catch(e) { toast('Error cargando movimientos', 'error'); }
  }

  window.openCreateMovimiento = async () => {
    const inventarios = await API.getInventarios();
    openModal('Registrar Movimiento');
    document.getElementById('modalBody').innerHTML = `
      <div class="form-grid">
        <div class="form-group"><label class="form-label">Tipo de Movimiento</label>
          <select class="form-control" id="fTipo" onchange="toggleMovFields()">
            <option>ENTRADA</option><option>SALIDA</option><option>TRASLADO</option><option>AJUSTE</option>
          </select>
        </div>
        <div class="form-group" id="fieldOrigen"><label class="form-label">Inventario Origen</label>
          <select class="form-control" id="fOrigen">
            <option value="">— Ninguno —</option>
            ${inventarios.map(i=>`<option value="${i.idInventario}">${i.producto.nombre} / ${i.bodega.nombre} (${i.cantidadActual})</option>`).join('')}
          </select>
        </div>
        <div class="form-group" id="fieldDestino"><label class="form-label">Inventario Destino</label>
          <select class="form-control" id="fDestino">
            <option value="">— Ninguno —</option>
            ${inventarios.map(i=>`<option value="${i.idInventario}">${i.producto.nombre} / ${i.bodega.nombre} (${i.cantidadActual})</option>`).join('')}
          </select>
        </div>
        <div class="form-group"><label class="form-label">Cantidad</label><input class="form-control" type="number" id="fCantidad" placeholder="10"/></div>
        <div class="form-group"><label class="form-label">Referencia</label><input class="form-control" id="fRef" placeholder="ORD-001"/></div>
        <div class="form-group"><label class="form-label">Observación</label><input class="form-control" id="fObs" placeholder="Descripción opcional"/></div>
      </div>`;
    toggleMovFields();

    window.saveModal = async () => {
      try {
        const usuarioId = 1; // Ajustar según tu implementación de JWT
        await API.createMovimiento({
          tipoMovimiento: document.getElementById('fTipo').value,
          inventarioOrigenId:  +document.getElementById('fOrigen').value  || null,
          inventarioDestinoId: +document.getElementById('fDestino').value || null,
          cantidad: +document.getElementById('fCantidad').value,
          referencia: document.getElementById('fRef').value,
          observacion: document.getElementById('fObs').value,
        }, usuarioId);
        toast('Movimiento registrado'); closeModal(); load();
      } catch(e) { toast(e.message, 'error'); }
    };
  };

  window.toggleMovFields = () => {
    const tipo = document.getElementById('fTipo')?.value;
    const fieldOrigen  = document.getElementById('fieldOrigen');
    const fieldDestino = document.getElementById('fieldDestino');
    if (!fieldOrigen) return;
    fieldOrigen.style.display  = tipo === 'ENTRADA'  ? 'none' : '';
    fieldDestino.style.display = tipo === 'SALIDA' || tipo === 'AJUSTE' ? 'none' : '';
  };

  load();
}

// ── AUDITORÍA ────────────────────────────────────────────────
async function modAuditoria(content) {
  content.innerHTML = `
    <div class="table-card">
      <div class="table-header">
        <span class="table-title">Registro de Auditoría</span>
        <input class="search-input" id="searchAudit" placeholder="Buscar..."/>
      </div>
      <table>
        <thead><tr><th>ID</th><th>Operación</th><th>Entidad</th><th>Registro ID</th><th>Usuario</th><th>Fecha</th></tr></thead>
        <tbody id="tbodyMain"></tbody>
      </table>
    </div>`;

  const badgeOp = op => {
    const map = { INSERT:'badge-green', UPDATE:'badge-blue', DELETE:'badge-red' };
    return `<span class="badge ${map[op]||''}">${op}</span>`;
  };

  let allData = [];

  showLoading('tbodyMain');
  try {
    allData = await API.getAuditorias();
    render(allData);
  } catch(e) { toast('Error cargando auditoría', 'error'); }

  function render(data) {
    const tbody = document.getElementById('tbodyMain');
    if (!data.length) { showEmpty('tbodyMain', 'Sin registros de auditoría'); return; }
    tbody.innerHTML = data.map(a => `<tr>
      <td class="mono">${a.idAuditoria}</td>
      <td>${badgeOp(a.tipoOperacion)}</td>
      <td><span class="badge badge-muted">${a.entidadAfectada}</span></td>
      <td class="mono">${a.registroId}</td>
      <td>${a.usuario?.username || '—'}</td>
      <td class="mono">${new Date(a.fechaHora).toLocaleString('es-CO')}</td>
    </tr>`).join('');
  }

  document.getElementById('searchAudit').addEventListener('input', e => {
    const q = e.target.value.toLowerCase();
    render(allData.filter(d => JSON.stringify(d).toLowerCase().includes(q)));
  });
}

// ── Logout ───────────────────────────────────────────────────
function logout() {
  localStorage.clear();
  window.location.href = 'index.html';
}

// ── Init ─────────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
  const username = localStorage.getItem('username') || 'usuario';
  document.getElementById('currentUser').textContent = username;
  document.getElementById('userAvatar').textContent = username[0].toUpperCase();
  navigate('dashboard');
});
