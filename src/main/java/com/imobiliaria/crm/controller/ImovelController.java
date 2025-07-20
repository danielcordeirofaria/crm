package com.imobiliaria.crm.controller;

import com.imobiliaria.crm.dto.ImovelDTO;
import com.imobiliaria.crm.service.IImovelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    private final IImovelService imovelService;

    public ImovelController(IImovelService imovelService) {
        this.imovelService = imovelService;
    }

    // --- ATUALIZAÇÃO NA DOCUMENTAÇÃO ---
    @Operation(summary = "Cria um novo imóvel", description = "Cria um novo imóvel, podendo associá-lo a um corretor e a características existentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Imóvel criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos. Verifique os campos obrigatórios, ou se o corretor/características informados existem.")
    })
    @PostMapping
    public ResponseEntity<ImovelDTO> criarImovel(@Valid @RequestBody ImovelDTO imovelDTO) {
        ImovelDTO novoImovel = imovelService.criarImovel(imovelDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoImovel.getId())
                .toUri();
        return ResponseEntity.created(location).body(novoImovel);
    }

    // --- ATUALIZAÇÃO NA DOCUMENTAÇÃO ---
    @Operation(summary = "Busca um imóvel por ID", description = "Retorna os dados de um imóvel, incluindo seu endereço, corretor, imagens e características.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imóvel encontrado"),
            @ApiResponse(responseCode = "404", description = "Imóvel não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ImovelDTO> buscarPorId(@PathVariable Long id) {
        return imovelService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lista todos os imóveis", description = "Retorna uma lista de todos os imóveis cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de imóveis retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ImovelDTO>> listarTodos() {
        List<ImovelDTO> imoveis = imovelService.listarTodos();
        return ResponseEntity.ok(imoveis);
    }

    // --- ATUALIZAÇÃO NA DOCUMENTAÇÃO ---
    @Operation(summary = "Atualiza um imóvel existente", description = "Atualiza todos os dados de um imóvel com base no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imóvel atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos."),
            @ApiResponse(responseCode = "404", description = "Imóvel, corretor ou características associadas não encontrados.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ImovelDTO> atualizarImovel(@PathVariable Long id, @Valid @RequestBody ImovelDTO imovelDTO) {
        ImovelDTO imovelAtualizado = imovelService.atualizarImovel(id, imovelDTO);
        return ResponseEntity.ok(imovelAtualizado);
    }

    // --- ATUALIZAÇÃO NA DOCUMENTAÇÃO ---
    @Operation(summary = "Deleta um imóvel", description = "Deleta um imóvel e todas as suas associações (imagens, características) devido ao Cascade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imóvel deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Imóvel não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarImovel(@PathVariable Long id) {
        imovelService.deletarImovel(id);
        return ResponseEntity.noContent().build();
    }
}