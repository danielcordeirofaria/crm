// Em src/main/java/com/imobiliaria/crm/service/ImagemServiceImpl.java
package com.imobiliaria.crm.service;

import com.imobiliaria.crm.dto.ImagemDTO;
import com.imobiliaria.crm.model.Imagem;
import com.imobiliaria.crm.model.Imovel;
import com.imobiliaria.crm.repository.ImagemRepository;
import com.imobiliaria.crm.repository.ImovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImagemServiceImpl implements IImagemService {

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private ImovelRepository imovelRepository; // Precisamos dele para associar a imagem

    @Override
    @Transactional
    public ImagemDTO adicionarImagem(Long imovelId, ImagemDTO imagemDTO) {
        // 1. Busca o imóvel que será o "pai" da imagem
        Imovel imovel = imovelRepository.findById(imovelId)
                .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado com ID: " + imovelId));

        // 2. Converte o DTO para entidade e associa ao imóvel
        Imagem imagem = imagemDTO.toEntity();
        imagem.setImovel(imovel);

        // 3. Salva a nova imagem
        Imagem imagemSalva = imagemRepository.save(imagem);

        return ImagemDTO.fromEntity(imagemSalva);
    }

    @Override
    @Transactional
    public void deletarImagem(Long imovelId, Long imagemId) {
        // Valida se a imagem pertence ao imóvel antes de deletar
        Imagem imagem = imagemRepository.findByIdAndImovelId(imagemId, imovelId)
                .orElseThrow(() -> new IllegalArgumentException("Imagem com ID " + imagemId + " não encontrada para o imóvel com ID " + imovelId));

        imagemRepository.delete(imagem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagemDTO> buscarImagensPorImovel(Long imovelId) {
        if (!imovelRepository.existsById(imovelId)) {
            throw new IllegalArgumentException("Imóvel não encontrado com ID: " + imovelId);
        }
        List<Imagem> imagens = imagemRepository.findByImovelId(imovelId);
        return imagens.stream()
                .map(ImagemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ImagemDTO atualizarImagem(Long imovelId, Long imagemId, ImagemDTO imagemDTO) {
        Imagem imagem = imagemRepository.findByIdAndImovelId(imagemId, imovelId)
                .orElseThrow(() -> new IllegalArgumentException("Imagem com ID " + imagemId + " não encontrada para o imóvel com ID " + imovelId));

        // Atualiza apenas os campos permitidos
        if (imagemDTO.getLegenda() != null) {
            imagem.setLegenda(imagemDTO.getLegenda());
        }
        if (imagemDTO.getOrdem() != null) {
            imagem.setOrdem(imagemDTO.getOrdem());
        }

        Imagem imagemAtualizada = imagemRepository.save(imagem);
        return ImagemDTO.fromEntity(imagemAtualizada);
    }
}