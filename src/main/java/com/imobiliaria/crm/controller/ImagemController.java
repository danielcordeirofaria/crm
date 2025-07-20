// Em src/main/java/com/imobiliaria/crm/controller/ImagemController.java
package com.imobiliaria.crm.controller;

import com.imobiliaria.crm.dto.ImagemDTO;
import com.imobiliaria.crm.service.IImagemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imoveis/{imovelId}/imagens")
public class ImagemController {

    private final IImagemService imagemService;

    @Autowired
    public ImagemController(IImagemService imagemService) {
        this.imagemService = imagemService;
    }

    @PostMapping
    public ResponseEntity<ImagemDTO> adicionarImagem(@PathVariable Long imovelId, @Valid @RequestBody ImagemDTO imagemDTO) {
        ImagemDTO novaImagem = imagemService.adicionarImagem(imovelId, imagemDTO);
        return new ResponseEntity<>(novaImagem, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ImagemDTO>> listarImagensDoImovel(@PathVariable Long imovelId) {
        List<ImagemDTO> imagens = imagemService.buscarImagensPorImovel(imovelId);
        return ResponseEntity.ok(imagens);
    }

    @PutMapping("/{imagemId}")
    public ResponseEntity<ImagemDTO> atualizarImagem(@PathVariable Long imovelId, @PathVariable Long imagemId, @Valid @RequestBody ImagemDTO imagemDTO) {
        ImagemDTO imagemAtualizada = imagemService.atualizarImagem(imovelId, imagemId, imagemDTO);
        return ResponseEntity.ok(imagemAtualizada);
    }

    @DeleteMapping("/{imagemId}")
    public ResponseEntity<Void> deletarImagem(@PathVariable Long imovelId, @PathVariable Long imagemId) {
        imagemService.deletarImagem(imovelId, imagemId);
        return ResponseEntity.noContent().build(); // 204 No Content é o status ideal para delete
    }
}