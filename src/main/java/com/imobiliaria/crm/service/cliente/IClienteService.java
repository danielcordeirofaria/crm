package com.imobiliaria.crm.service.cliente;

import com.imobiliaria.crm.dto.ClienteDTO;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    ClienteDTO criarCliente(ClienteDTO clienteDTO);
    ClienteDTO atualizarCliente(Long id, ClienteDTO clienteDTO);
    Optional<ClienteDTO> buscarPorId(Long id);
    List<ClienteDTO> listarTodos();
    void deletarCliente(Long id);
}