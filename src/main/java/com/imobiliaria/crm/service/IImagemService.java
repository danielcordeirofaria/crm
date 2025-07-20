package com.imobiliaria.crm.service;

import com.imobiliaria.crm.dto.ImagemDTO;
import java.util.List;

public interface IImagemService {


//    Adiciona uma nova imagem a um imóvel existente.
    ImagemDTO adicionarImagem(Long imovelId, ImagemDTO imagemDTO);

//    Deleta uma imagem específica de um imóvel.
    void deletarImagem(Long imovelId, Long imagemId);

//    Busca todas as imagens de um imóvel específico.
    List<ImagemDTO> buscarImagensPorImovel(Long imovelId);

//    Atualiza os metadados de uma imagem (ex: legenda, ordem).
    ImagemDTO atualizarImagem(Long imovelId, Long imagemId, ImagemDTO imagemDTO);
}