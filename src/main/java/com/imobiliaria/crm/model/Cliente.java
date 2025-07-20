package com.imobiliaria.crm.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false, length = 14)
    private String cpf;

    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // Relacionamento opcional com Corretor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corretor_id")
    private Corretor corretor;

    /**
     * Método de callback do JPA.
     * É executado automaticamente antes de a entidade ser persistida pela primeira vez.
     */
    @PrePersist
    protected void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }
}