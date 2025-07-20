package com.imobiliaria.crm.service.caracteristica;

import com.imobiliaria.crm.dto.CaracteristicaDTO;
import com.imobiliaria.crm.model.Caracteristica;
import com.imobiliaria.crm.repository.CaracteristicaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CaracteristicaServiceImpl implements ICaracteristicaService {

    private final CaracteristicaRepository caracteristicaRepository;

    public CaracteristicaServiceImpl(CaracteristicaRepository caracteristicaRepository) {
        this.caracteristicaRepository = caracteristicaRepository;
    }

    @Override
    @Transactional
    public CaracteristicaDTO criar(CaracteristicaDTO dto) {
        // Valida se já existe uma característica com o mesmo nome (ignorando maiúsculas/minúsculas)
        caracteristicaRepository.findByNomeIgnoreCase(dto.getNome()).ifPresent(c -> {
            throw new IllegalArgumentException("Já existe uma característica com o nome '" + dto.getNome() + "'.");
        });

        Caracteristica caracteristica = dto.toEntity();
        Caracteristica caracteristicaSalva = caracteristicaRepository.save(caracteristica);
        return CaracteristicaDTO.fromEntity(caracteristicaSalva);
    }

    @Override
    @Transactional
    public CaracteristicaDTO atualizar(Long id, CaracteristicaDTO dto) {
        // Busca a característica existente
        Caracteristica caracteristicaExistente = caracteristicaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Característica com ID " + id + " não encontrada."));

        // Valida se o novo nome já não pertence a OUTRA característica
        caracteristicaRepository.findByNomeIgnoreCase(dto.getNome()).ifPresent(c -> {
            if (!c.getId().equals(id)) {
                throw new IllegalArgumentException("O nome '" + dto.getNome() + "' já está em uso por outra característica.");
            }
        });

        // Atualiza o nome e salva
        caracteristicaExistente.setNome(dto.getNome());
        Caracteristica caracteristicaAtualizada = caracteristicaRepository.save(caracteristicaExistente);
        return CaracteristicaDTO.fromEntity(caracteristicaAtualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CaracteristicaDTO> buscarPorId(Long id) {
        return caracteristicaRepository.findById(id)
                .map(CaracteristicaDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaracteristicaDTO> listarTodas() {
        return caracteristicaRepository.findAll().stream()
                .map(CaracteristicaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        // Antes de deletar, verifica se a característica existe para fornecer uma mensagem de erro clara.
        if (!caracteristicaRepository.existsById(id)) {
            throw new IllegalArgumentException("Característica com ID " + id + " não encontrada.");
        }

        // A relação ManyToMany com Imovel será removida automaticamente
        // pela constraint ON DELETE CASCADE na tabela imovel_caracteristicas.
        caracteristicaRepository.deleteById(id);
    }
}