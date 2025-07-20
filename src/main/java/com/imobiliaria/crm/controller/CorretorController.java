package com.imobiliaria.crm.controller;

import com.imobiliaria.crm.dto.CorretorDTO;
import com.imobiliaria.crm.service.corretor.CorretorServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/corretores")
public class CorretorController {

    @Autowired
    private CorretorServiceImpl corretorService;

    @Operation(summary = "Cria um novo corretor", description = "Cria um corretor com base nos dados fornecidos no DTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Corretor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<CorretorDTO> criar(@Valid @RequestBody CorretorDTO corretorDTO) {
        CorretorDTO savedCorretor = corretorService.criarCorretor(corretorDTO);
        return ResponseEntity.ok(savedCorretor);
    }

    @Operation(summary = "Atualiza um corretor existente", description = "Atualiza os dados de um corretor com base no ID e DTO fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Corretor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Corretor não encontrado")
    })

    @PutMapping("/{id}")
    public ResponseEntity<CorretorDTO> atualizar(@PathVariable @Positive(message = "ID deve ser positivo") Long id, @Valid @RequestBody CorretorDTO corretorDTO) {
        CorretorDTO updatedCorretor = corretorService.atualizarCorretor(id, corretorDTO);
        return ResponseEntity.ok(updatedCorretor);
    }

    @Operation(summary = "Busca um corretor por ID", description = "Retorna os dados de um corretor com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Corretor encontrado"),
            @ApiResponse(responseCode = "404", description = "Corretor não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CorretorDTO> buscarPorId(@PathVariable @Positive(message = "ID deve ser positivo") Long id) {
        Optional<CorretorDTO> corretor = corretorService.buscarPorId(id);
        return corretor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lista todos os corretores", description = "Retorna uma lista de corretores, com opção de filtrar apenas os ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de corretores retornada com sucesso")
    })

    @GetMapping
    public List<CorretorDTO> listar(@RequestParam(defaultValue = "true") boolean apenasAtivos) {
        return corretorService.listarCorretores(apenasAtivos);
    }

    @Operation(summary = "Ativa ou desativa um corretor", description = "Alterna o status 'ativo' de um corretor com base no ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Corretor não encontrado")
    })

    @PatchMapping("/{id}/ativo")
    public ResponseEntity<Void> toggleAtivo(@PathVariable @Positive(message = "ID deve ser positivo") Long id) {
        corretorService.toggleAtivo(id);
        return ResponseEntity.ok().build();
    }

}