from flask import Flask, request, jsonify
import pandas as pd
from fuzzywuzzy import fuzz

app = Flask(__name__)

# Carrega os dados do CSV
df = pd.read_csv('operadoras.csv', encoding='utf-8')

@app.route('/api/search', methods=['GET'])
def search_operadoras():
    query = request.args.get('q', '')
    threshold = int(request.args.get('threshold', 70))
    limit = int(request.args.get('limit', 10))
    
    if not query:
        return jsonify({"error": "Parâmetro de busca 'q' é obrigatório"}), 400
    
    results = []
    for _, row in df.iterrows():
        # Combina busca em múltiplos campos
        score = max(
            fuzz.token_set_ratio(query, str(row['Razão Social'])),
            fuzz.token_set_ratio(query, str(row['Nome Fantasia'])),
            fuzz.token_set_ratio(query, str(row['CNPJ']))
        )
        if score >= threshold:
            results.append({
                **row.to_dict(),
                "relevance_score": score
            })
    
    # Ordena por relevância e limita resultados
    results = sorted(results, key=lambda x: x['relevance_score'], reverse=True)[:limit]
    
    return jsonify({
        "query": query,
        "count": len(results),
        "results": results
    })

if __name__ == '__main__':
    app.run(debug=True, port=5000)