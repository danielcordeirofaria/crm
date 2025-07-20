package com.imobiliaria.crm.dto;

import com.imobiliaria.crm.model.Imagem;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImagemDTO {

    private Long id;
    private String url;
    private String legenda;
    private Integer ordem;
    private LocalDateTime dataUpload;

    // Converte a entidade Imagem para ImagemDTO
    public static ImagemDTO fromEntity(Imagem imagem) {
        ImagemDTO dto = new ImagemDTO();
        dto.setId(imagem.getId());
        dto.setUrl(imagem.getUrl());
        dto.setLegenda(imagem.getLegenda());
        dto.setOrdem(imagem.getOrdem());
        dto.setDataUpload(imagem.getDataUpload());
        return dto;
    }

    // Converte o DTO para a entidade Imagem
    // Note que não setamos o Imovel aqui. Isso será feito na camada de serviço.
    public Imagem toEntity() {
        Imagem imagem = new Imagem();
        imagem.setId(this.id);
        imagem.setUrl(this.url);
        imagem.setLegenda(this.legenda);
        imagem.setOrdem(this.ordem);
        imagem.setDataUpload(this.dataUpload);
        return imagem;
    }
}