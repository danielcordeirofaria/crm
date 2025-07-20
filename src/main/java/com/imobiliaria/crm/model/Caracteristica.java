package com.imobiliaria.crm.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "caracteristicas")
@Data
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome; // Ex: "Piscina", "Churrasqueira", "Academia", "Aceita Pet"
}