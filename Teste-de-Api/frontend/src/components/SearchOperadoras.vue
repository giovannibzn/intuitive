<template>
    <div class="operadoras-search">
      <h1>Busca de Operadoras de Saúde</h1>
      
      <div class="search-box">
        <input 
          v-model="searchQuery"
          @keyup.enter="performSearch"
          placeholder="Digite razão social, nome fantasia ou CNPJ"
        />
        <button @click="performSearch">Buscar</button>
        
        <div class="filters">
          <label>
            Limite de resultados:
            <input type="number" v-model.number="resultLimit" min="1" max="50">
          </label>
        </div>
      </div>
      
      <div v-if="loading" class="loading">Carregando...</div>
      
      <div v-if="error" class="error">{{ error }}</div>
      
      <div v-if="results.length > 0" class="results">
        <div class="summary">
          Exibindo {{ results.length }} de {{ totalResults }} resultados para "{{ lastQuery }}"
        </div>
        
        <table>
          <thead>
            <tr>
              <th>Razão Social</th>
              <th>Nome Fantasia</th>
              <th>CNPJ</th>
              <th>Relevância</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in results" :key="item.CNPJ">
              <td>{{ item['Razão Social'] }}</td>
              <td>{{ item['Nome Fantasia'] }}</td>
              <td>{{ formatCNPJ(item.CNPJ) }}</td>
              <td>{{ item.relevance_score }}%</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        searchQuery: '',
        results: [],
        lastQuery: '',
        totalResults: 0,
        loading: false,
        error: '',
        resultLimit: 10
      }
    },
    methods: {
      async performSearch() {
        if (!this.searchQuery.trim()) {
          this.error = 'Por favor, digite um termo para busca';
          return;
        }
        
        this.loading = true;
        this.error = '';
        
        try {
          const response = await fetch(
            `http://localhost:5000/api/search?q=${encodeURIComponent(this.searchQuery)}&limit=${this.resultLimit}`
          );
          
          if (!response.ok) throw new Error('Erro na busca');
          
          const data = await response.json();
          this.results = data.results;
          this.totalResults = data.count;
          this.lastQuery = this.searchQuery;
        } catch (err) {
          this.error = 'Falha ao buscar operadoras. Tente novamente.';
          console.error(err);
        } finally {
          this.loading = false;
        }
      },
      formatCNPJ(cnpj) {
        if (!cnpj || !/^\d{14}$/.test(cnpj)) return 'CNPJ inválido';
        return cnpj.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, '$1.$2.$3/$4-$5');
      }
    }
  }
  </script>
  
  <style scoped>
  .operadoras-search {
    max-width: 1000px;
    margin: 0 auto;
    padding: 20px;
  }
  
  .search-box {
    margin: 20px 0;
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
    align-items: center;
  }
  
  input[type="text"] {
    flex: 1;
    padding: 10px;
    min-width: 300px;
  }
  
  button {
    padding: 10px 20px;
    background: #42b983;
    color: white;
    border: none;
    cursor: pointer;
  }
  
  button:hover {
    background: #369f6e;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
  }
  
  th, td {
    padding: 12px 15px;
    text-align: left;
    border-bottom: 1px solid #ddd;
  }
  
  th {
    background-color: #f5f5f5;
  }
  
  tr:hover {
    background-color: #f9f9f9;
  }
  
  .loading, .error {
    margin: 20px 0;
    padding: 15px;
    text-align: center;
  }
  
  .loading {
    color: #666;
  }
  
  .error {
    color: #ff4444;
    background-color: #ffeeee;
  }
  
  .filters {
    margin-left: auto;
    display: flex;
    gap: 15px;
  }
  
  .filters label {
    display: flex;
    align-items: center;
    gap: 5px;
  }
  
  .filters input[type="number"] {
    width: 60px;
    padding: 5px;
  }
  </style>
