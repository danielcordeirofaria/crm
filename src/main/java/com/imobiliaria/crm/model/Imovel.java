package com.imobiliaria.crm.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "imoveis")
@Data
public class Imovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Campos de Identificação e Tipo ---
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false, length = 50)
    private String tipo; // Ex: "Apartamento", "Casa", "Terreno"

    @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'VENDA'")
    private String finalidade;

    // --- Objeto de Endereço Embutido ---
    // Agrupa todos os campos de endereço em um objeto coeso,
    // mas os salva como colunas na tabela 'imoveis'.
    @Embedded
    private Endereco endereco;

    // --- Campos de Valores ---
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal preco;

    @Column(name = "valor_condominio", precision = 10, scale = 2)
    private BigDecimal valorCondominio;

    @Column(name = "valor_iptu", precision = 10, scale = 2)
    private BigDecimal valorIptu;

    // --- Campos de Estrutura ---
    @Column(name = "area_total", precision = 10, scale = 2)
    private BigDecimal areaTotal;

    @Column(name = "area_util", precision = 10, scale = 2)
    private BigDecimal areaUtil;

    private Integer quartos;

    private Integer suites;

    private Integer banheiros;

    @Column(name = "vagas_garagem")
    private Integer vagasGaragem;

    @Column(name = "ano_construcao")
    private Integer anoConstrucao;

    // --- Campos de Controle e Descrição ---
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(length = 20)
    private String status; // Ex: "DISPONIVEL", "VENDIDO", "INATIVO"

    @Column(nullable = false)
    private boolean publicado = false; // Controle para portais

    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    @UpdateTimestamp // Anotação do Hibernate que atualiza automaticamente na alteração
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // --- Relacionamentos ---
    @ManyToOne(fetch = FetchType.LAZY) // LAZY é melhor para performance
    @JoinColumn(name = "corretor_id")
    private Corretor corretor;

    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagem> imagens;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "imovel_caracteristicas",
            joinColumns = @JoinColumn(name = "imovel_id"),
            inverseJoinColumns = @JoinColumn(name = "caracteristica_id")
    )
    private Set<Caracteristica> caracteristicas; // Supondo que você crie a entidade Caracteristica

    @PrePersist
    protected void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }
}