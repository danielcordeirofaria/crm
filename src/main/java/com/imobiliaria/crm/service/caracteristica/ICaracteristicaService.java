package com.imobiliaria.crm.service.caracteristica;

import com.imobiliaria.crm.dto.CaracteristicaDTO;
import java.util.List;
import java.util.Optional;

public interface ICaracteristicaService {
    CaracteristicaDTO criar(CaracteristicaDTO dto);
    Optional<CaracteristicaDTO> buscarPorId(Long id);
    List<CaracteristicaDTO> listarTodas();
    CaracteristicaDTO atualizar(Long id, CaracteristicaDTO dto);
    void deletar(Long id);
}