import './style.css';

const API_URL = 'http://localhost:4567/produto';

document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('product-form');
  const productList = document.getElementById('product-list');
  const jsonView = document.getElementById('json-view');
  
  const editModal = document.getElementById('edit-modal');
  const editForm = document.getElementById('edit-form');
  const btnCancelEdit = document.getElementById('btn-cancel-edit');

  // Carrega a lista de produtos assim que a página é aberta
  fetchProducts();

  // Evento disparado ao enviar o formulário de cadastro (Novo Produto)
  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const descricao = document.getElementById('descricao').value;
    const preco = parseFloat(document.getElementById('preco').value);
    const quantidade = parseInt(document.getElementById('quantidade').value, 10);
    const dataFabricacao = document.getElementById('data-fabricacao').value;

    const newProduct = { descricao, preco, quantidade, dataFabricacao };

    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newProduct)
      });
      
      if (response.ok) {
        form.reset();
        fetchProducts();
      } else {
        alert('Erro ao cadastrar produto!');
      }
    } catch (err) {
      console.error(err);
      alert('Falha na comunicação com o servidor.');
    }
  });

  // Evento disparado ao enviar o formulário de EDIÇÃO (Salvar alterações)
  editForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('edit-id').value;
    const descricao = document.getElementById('edit-descricao').value;
    const preco = parseFloat(document.getElementById('edit-preco').value);
    const quantidade = parseInt(document.getElementById('edit-quantidade').value, 10);
    const dataFabricacao = document.getElementById('edit-data-fabricacao').value;

    const updatedProduct = { id: parseInt(id), descricao, preco, quantidade, dataFabricacao };

    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedProduct)
      });
      
      if (response.ok) {
        closeModal();
        fetchProducts();
      } else {
        alert('Erro ao editar produto!');
      }
    } catch (err) {
      console.error(err);
      alert('Falha na comunicação.');
    }
  });

  btnCancelEdit.addEventListener('click', closeModal);

  // Função global para abrir o modal de edição e preencher os dados atuais do produto
  window.openEditModal = function(id, descricao, preco, quantidade, dataFabricacao) {
    document.getElementById('edit-id').value = id;
    document.getElementById('edit-descricao').value = descricao;
    document.getElementById('edit-preco').value = preco;
    document.getElementById('edit-quantidade').value = quantidade;
    
    document.getElementById('edit-data-fabricacao').value = dataFabricacao;
    
    editModal.style.display = 'flex';
  };

  // Função global para excluir um produto chamando a API DELETE
  window.deleteProduct = async function(id) {
    if (!confirm('Tem certeza que deseja excluir este produto?')) return;
    
    try {
      const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
      if (response.ok) {
        fetchProducts();
      } else {
        alert('Erro ao excluir produto.');
      }
    } catch (err) {
      console.error(err);
      alert('Falha ao excluir.');
    }
  };

  function closeModal() {
    editModal.style.display = 'none';
  }

  // Busca os produtos no back-end (GET)
  async function fetchProducts() {
    try {
      const response = await fetch(API_URL);
      if (response.ok) {
        const products = await response.json();
        renderProducts(products);
      }
    } catch (err) {
      console.error(err);
      productList.innerHTML = '<p class="empty-state">Erro ao carregar produtos do servidor.</p>';
    }
  }

  // Renderiza os produtos na tela construindo blocos HTML (Cards)
  function renderProducts(products) {
    if (!products || products.length === 0) {
      productList.innerHTML = '<p class="empty-state">Nenhum produto cadastrado ainda.</p>';
      jsonView.innerHTML = '';
      return;
    }

    productList.innerHTML = '';
    products.forEach(product => {
      const item = document.createElement('div');
      item.className = 'product-item';
      
      item.innerHTML = `
        <div class="product-info">
          <h3>${product.descricao} <span style="font-size: 0.8rem; color:#94a3b8;">(#${product.id})</span></h3>
          <p>Qtd: ${product.quantidade} | Fab: ${product.dataFabricacao}</p>
        </div>
        <div class="product-actions" style="display:flex; gap:0.5rem; align-items:center;">
          <div class="product-price">${new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(product.preco)}</div>
          <button class="btn-edit" onclick="openEditModal(${product.id}, '${product.descricao}', ${product.preco}, ${product.quantidade}, '${product.dataFabricacao}')">Editar</button>
          <button class="btn-delete" onclick="deleteProduct(${product.id})">Excluir</button>
        </div>
      `;
      productList.appendChild(item);
    });

    const pre = document.createElement('pre');
    pre.style.background = 'rgba(15, 23, 42, 0.6)';
    pre.style.padding = '1rem';
    pre.style.borderRadius = '0.5rem';
    pre.style.overflowX = 'auto';
    pre.style.color = '#34d399';
    pre.style.fontFamily = 'monospace';
    pre.textContent = JSON.stringify(products, null, 2);
    
    jsonView.innerHTML = '';
    jsonView.appendChild(pre);
  }
});
