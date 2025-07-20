package com.imobiliaria.crm.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "imagens")
@Data
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "imovel_id", nullable = false)
    private Imovel imovel;

    @Column(nullable = false, length = 255)
    private String url;

    @Column(length = 255)
    private String legenda;

    @Column(name = "data_upload", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dataUpload;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer ordem;
}