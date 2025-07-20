CREATE TABLE corretores (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL DEFAULT '000.000.000-00',
    email VARCHAR(100) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    creci VARCHAR(20) UNIQUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE,
    CONSTRAINT valid_cpf_corretores CHECK (cpf ~ '^[0-9]{3}\.[0-9]{3}\.[0-9]{3}-[0-9]{2}$')
);

CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    telefone VARCHAR(20),
    cpf VARCHAR(14) NOT NULL DEFAULT '000.000.000-00',
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    corretor_id INTEGER,
    observacoes TEXT,
    CONSTRAINT valid_cpf_clientes CHECK (cpf ~ '^[0-9]{3}\.[0-9]{3}\.[0-9]{3}-[0-9]{2}$'),
    FOREIGN KEY (corretor_id) REFERENCES corretores(id)
);

CREATE TABLE imoveis (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- Ponto de atenção: considere usar uma tabela separada ou ENUM
    finalidade VARCHAR(20) NOT NULL DEFAULT 'VENDA', -- Ex: 'VENDA', 'ALUGUEL'

    -- Endereço mais completo
    endereco VARCHAR(255) NOT NULL,
    bairro VARCHAR(100),
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(9),

    -- Valores
    preco DECIMAL(15, 2) NOT NULL,
    valor_condominio DECIMAL(10, 2),
    valor_iptu DECIMAL(10, 2),

    -- Detalhes da estrutura
    area_total DECIMAL(10, 2),
    area_util DECIMAL(10, 2),
    quartos INTEGER,
    suites INTEGER,
    banheiros INTEGER,
    vagas_garagem INTEGER,
    ano_construcao INTEGER,

    -- Controle e Metadados
    descricao TEXT,
    status VARCHAR(20) DEFAULT 'DISPONIVEL', -- Ex: 'DISPONIVEL', 'VENDIDO', 'ALUGADO', 'INATIVO'
    publicado BOOLEAN DEFAULT FALSE, -- Controle para publicação nos portais
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,

    -- Relacionamentos
    corretor_id BIGINT, -- Usar BIGINT para corresponder ao tipo SERIAL/IDENTITY
    FOREIGN KEY (corretor_id) REFERENCES corretores(id)
);

CREATE TABLE caracteristicas (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL -- Ex: 'Piscina', 'Churrasqueira', 'Academia', 'Aceita Pet'
);

CREATE TABLE imovel_caracteristicas (
    imovel_id BIGINT NOT NULL,
    caracteristica_id BIGINT NOT NULL,
    PRIMARY KEY (imovel_id, caracteristica_id), -- Chave primária composta garante que a relação é única
    FOREIGN KEY (imovel_id) REFERENCES imoveis(id) ON DELETE CASCADE, -- Se o imóvel for deletado, a relação some
    FOREIGN KEY (caracteristica_id) REFERENCES caracteristicas(id) ON DELETE CASCADE
);

CREATE TABLE imagens (
    id SERIAL PRIMARY KEY,
    imovel_id INTEGER NOT NULL,
    url VARCHAR(255) NOT NULL,
    legenda VARCHAR(255),
    data_upload TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ordem INTEGER DEFAULT 0, -- Para controlar a ordem das imagens
    FOREIGN KEY (imovel_id) REFERENCES imoveis(id)
);

CREATE TABLE interacoes (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER NOT NULL,
    corretor_id INTEGER NOT NULL,
    imovel_id INTEGER,
    tipo VARCHAR(50) NOT NULL,
    data_interacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observacoes TEXT,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (corretor_id) REFERENCES corretores(id),
    FOREIGN KEY (imovel_id) REFERENCES imoveis(id)
);

CREATE TABLE portais_integracao (
    id SERIAL PRIMARY KEY,
    imovel_id INTEGER NOT NULL,
    portal_nome VARCHAR(100) NOT NULL,
    codigo_portal VARCHAR(100),
    url_anuncio VARCHAR(255),
    data_publicacao TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ATIVO',
    FOREIGN KEY (imovel_id) REFERENCES imoveis(id)
);

CREATE TABLE vendas (
    id SERIAL PRIMARY KEY,
    imovel_id INTEGER NOT NULL,
    cliente_id INTEGER NOT NULL,
    corretor_id INTEGER NOT NULL,
    valor_venda DECIMAL(15, 2) NOT NULL,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observacoes TEXT,
    FOREIGN KEY (imovel_id) REFERENCES imoveis(id),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (corretor_id) REFERENCES corretores(id)
);