package com.imobiliaria.crm.service;

import com.imobiliaria.crm.dto.ImovelDTO;
import java.util.List;
import java.util.Optional;

public interface IImovelService {
    ImovelDTO criarImovel(ImovelDTO imovelDTO);
    Optional<ImovelDTO> buscarPorId(Long id);
    List<ImovelDTO> listarTodos();
    ImovelDTO atualizarImovel(Long id, ImovelDTO imovelDTO);
    void deletarImovel(Long id);
}