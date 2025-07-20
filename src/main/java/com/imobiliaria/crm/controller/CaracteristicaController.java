package com.imobiliaria.crm.controller;

import com.imobiliaria.crm.dto.CaracteristicaDTO;
import com.imobiliaria.crm.service.ICaracteristicaService;
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
@RequestMapping("/caracteristicas")
public class CaracteristicaController {

    private final ICaracteristicaService caracteristicaService;

    public CaracteristicaController(ICaracteristicaService caracteristicaService) {
        this.caracteristicaService = caracteristicaService;
    }

    @Operation(summary = "Cria uma nova característica", description = "Cria uma nova característica que pode ser associada a imóveis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Característica criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome da característica já existe")
    })
    @PostMapping
    public ResponseEntity<CaracteristicaDTO> criar(@Valid @RequestBody CaracteristicaDTO dto) {
        CaracteristicaDTO novaCaracteristica = caracteristicaService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaCaracteristica.getId())
                .toUri();
        return ResponseEntity.created(location).body(novaCaracteristica);
    }

    @Operation(summary = "Lista todas as características", description = "Retorna uma lista de todas as características disponíveis no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de características retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<CaracteristicaDTO>> listar() {
        List<CaracteristicaDTO> caracteristicas = caracteristicaService.listarTodas();
        return ResponseEntity.ok(caracteristicas);
    }

    @Operation(summary = "Busca uma característica por ID", description = "Retorna os dados de uma característica específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Característica encontrada"),
            @ApiResponse(responseCode = "404", description = "Característica não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CaracteristicaDTO> buscarPorId(@PathVariable Long id) {
        return caracteristicaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualiza uma característica existente", description = "Atualiza o nome de uma característica com base no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Característica atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome já em uso"),
            @ApiResponse(responseCode = "404", description = "Característica não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CaracteristicaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CaracteristicaDTO dto) {
        CaracteristicaDTO caracteristicaAtualizada = caracteristicaService.atualizar(id, dto);
        return ResponseEntity.ok(caracteristicaAtualizada);
    }

    @Operation(summary = "Deleta uma característica", description = "Deleta uma característica do sistema. Cuidado: isso pode afetar imóveis que a utilizam.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Característica deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Característica não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        caracteristicaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}