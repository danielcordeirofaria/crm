package com.imobiliaria.crm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Endereco {

    @Column(name = "endereco", nullable = false, length = 255)
    private String logradouro;

    @Column(length = 100)
    private String bairro;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, length = 2)
    private String estado;

    @Column(length = 9)
    private String cep;

    @Column(length = 50)
    private String complemento; // Campo útil que faltava
}