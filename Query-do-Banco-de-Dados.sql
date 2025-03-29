CREATE TABLE operadoras(
	id INT PRIMARY KEY AUTO_INCREMENT,
    registro_ans VARCHAR(20),
    cnpj VARCHAR(20),
    razao_social VARCHAR(255),
    nome_fantasia VARCHAR(255),
    modalidade VARCHAR(100),
    logradouro VARCHAR(255),
    numero VARCHAR(20),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    uf VARCHAR(2),
    cep VARCHAR(10),
    ddd VARCHAR(3),
    telefone VARCHAR(20),
    fax VARCHAR(20),
    email VARCHAR(100),
    representante VARCHAR(100),
    cargo_representante VARCHAR(100),
    regiao_comercializacao VARCHAR(10),
    data_registro_ans DATE
);

ALTER TABLE operadoras ADD UNIQUE (registro_ans);

Select * from operadoras;

CREATE TABLE demonstracoes_contabeis (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data DATE,
    reg_ans VARCHAR(20),
    cd_conta_contabil VARCHAR(20),
    descricao VARCHAR(255),
    vl_saldo_inicial DECIMAL(15, 2),
    vl_saldo_final DECIMAL(15, 2)
);

Select * from demonstracoes_contabeis;

LOAD DATA INFILE 'D:\Giovanni\\Banco de Dados\\Relatorio_cadop.csv'
INTO TABLE Operadoras
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(registro_ans, cnpj, razao_social, nome_fantasia, modalidade,
logradouro, numero, complemento, bairro, cidade, uf, cep,
ddd, telefone, fax, email, representante, cargo_representante,
regiao_comercializacao, data_registro_ans);

Select * from operadoras;

LOAD DATA INFILE 'D:\Giovanni\\Banco de Dados\\4T2024.csv'
INTO TABLE demonstracoes_contabeis
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(data, reg_ans, cd_conta_contabil, descricao, @vl_inicial, @vl_final)
SET
    vl_saldo_inicial = NULLIF(REPLACE(@vl_inicial, ',', '.'), ''),
    vl_saldo_final = NULLIF(REPLACE(@vl_final, ',', '.'), '');
    
LOAD DATA INFILE 'D:\Giovanni\\Banco de Dados\\4T2024.csv'
INTO TABLE demonstracoes_contabeis
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(data, reg_ans, cd_conta_contabil, descricao, @vl_inicial, @vl_final)
SET
    vl_saldo_inicial = NULLIF(REPLACE(@vl_inicial, ',', '.'), ''),
    vl_saldo_final = NULLIF(REPLACE(@vl_final, ',', '.'), '');
    
LOAD DATA INFILE 'D:\Giovanni\\Banco de Dados\\3T2024.csv'
INTO TABLE demonstracoes_contabeis
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(data, reg_ans, cd_conta_contabil, descricao, @vl_inicial, @vl_final)
SET
    vl_saldo_inicial = NULLIF(REPLACE(@vl_inicial, ',', '.'), ''),
    vl_saldo_final = NULLIF(REPLACE(@vl_final, ',', '.'), '');
    
LOAD DATA INFILE 'D:\Giovanni\\Banco de Dados\\2T2024.csv'
INTO TABLE demonstracoes_contabeis
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(data, reg_ans, cd_conta_contabil, descricao, @vl_inicial, @vl_final)
SET
    vl_saldo_inicial = NULLIF(REPLACE(@vl_inicial, ',', '.'), ''),
    vl_saldo_final = NULLIF(REPLACE(@vl_final, ',', '.'), '');

LOAD DATA INFILE 'D:\Giovanni\\Banco de Dados\\1T2024.csv'
INTO TABLE demonstracoes_contabeis
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(data, reg_ans, cd_conta_contabil, descricao, @vl_inicial, @vl_final)
SET
    vl_saldo_inicial = NULLIF(REPLACE(@vl_inicial, ',', '.'), ''),
    vl_saldo_final = NULLIF(REPLACE(@vl_final, ',', '.'), '');
    
LOAD DATA INFILE 'D:\Giovanni\\Banco de Dados\\3T2023.csv'
INTO TABLE demonstracoes_contabeis
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(data, reg_ans, cd_conta_contabil, descricao, @vl_inicial, @vl_final)
SET
    vl_saldo_inicial = NULLIF(REPLACE(@vl_inicial, ',', '.'), ''),
    vl_saldo_final = NULLIF(REPLACE(@vl_final, ',', '.'), '');
    
LOAD DATA INFILE 'D:\Giovanni\\Banco de Dados\\2T2023.csv'
INTO TABLE demonstracoes_contabeis
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(data, reg_ans, cd_conta_contabil, descricao, @vl_inicial, @vl_final)
SET
    vl_saldo_inicial = NULLIF(REPLACE(@vl_inicial, ',', '.'), ''),
    vl_saldo_final = NULLIF(REPLACE(@vl_final, ',', '.'), '');
    
LOAD DATA INFILE 'D:\Giovanni\\Banco de Dados\\1T2023.csv'
INTO TABLE demonstracoes_contabeis
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(data, reg_ans, cd_conta_contabil, descricao, @vl_inicial, @vl_final)
SET
    vl_saldo_inicial = NULLIF(REPLACE(@vl_inicial, ',', '.'), ''),
    vl_saldo_final = NULLIF(REPLACE(@vl_final, ',', '.'), '');
    
Select * from demonstracoes_contabeis;

SELECT 
    reg_ans AS operadora,
    SUM(vl_saldo_final) AS total_despesas
FROM 
    demonstracoes_contabeis
WHERE 
    (cd_conta_contabil LIKE '33%' OR  -- Grupo de despesas com saúde
     cd_conta_contabil LIKE '34%')    -- Grupo de sinistros
    AND data >= DATE_SUB((SELECT MAX(data) FROM demonstracoes_contabeis), INTERVAL 3 MONTH)
GROUP BY 
    reg_ans
ORDER BY 
    total_despesas DESC
LIMIT 10;

SELECT 
    reg_ans AS operadora,
    SUM(vl_saldo_final) AS total_despesas
FROM 
    demonstracoes_contabeis
WHERE 
    (cd_conta_contabil LIKE '33%' OR  -- Grupo de despesas com saúde
     cd_conta_contabil LIKE '34%')    -- Grupo de sinistros
    AND data >= DATE_SUB((SELECT MAX(data) FROM demonstracoes_contabeis), INTERVAL 1 YEAR)
GROUP BY 
    reg_ans
ORDER BY 
    total_despesas DESC
LIMIT 10;
    
