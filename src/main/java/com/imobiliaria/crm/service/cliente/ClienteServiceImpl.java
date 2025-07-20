package com.imobiliaria.crm.service.cliente;

import com.imobiliaria.crm.dto.ClienteDTO;
import com.imobiliaria.crm.model.Cliente;
import com.imobiliaria.crm.model.Corretor;
import com.imobiliaria.crm.repository.ClienteRepository;
import com.imobiliaria.crm.repository.CorretorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements IClienteService {

    private final ClienteRepository clienteRepository;
    private final CorretorRepository corretorRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository, CorretorRepository corretorRepository) {
        this.clienteRepository = clienteRepository;
        this.corretorRepository = corretorRepository;
    }

    @Override
    @Transactional
    public ClienteDTO criarCliente(ClienteDTO clienteDTO) {
        // Valida se já existe um cliente com o mesmo CPF ou E-mail
        clienteRepository.findByCpf(clienteDTO.getCpf()).ifPresent(c -> {
            throw new IllegalArgumentException("Já existe um cliente com o CPF informado.");
        });
        if (clienteDTO.getEmail() != null && !clienteDTO.getEmail().isEmpty()) {
            clienteRepository.findByEmailIgnoreCase(clienteDTO.getEmail()).ifPresent(c -> {
                throw new IllegalArgumentException("Já existe um cliente com o e-mail informado.");
            });
        }

        Cliente cliente = clienteDTO.toEntity();

        // Associa o corretor, se um ID for fornecido
        if (clienteDTO.getCorretorId() != null) {
            Corretor corretor = corretorRepository.findById(clienteDTO.getCorretorId())
                    .orElseThrow(() -> new IllegalArgumentException("Corretor com ID " + clienteDTO.getCorretorId() + " não encontrado."));
            cliente.setCorretor(corretor);
        }

        Cliente clienteSalvo = clienteRepository.save(cliente);
        return ClienteDTO.fromEntity(clienteSalvo);
    }

    @Override
    @Transactional
    public ClienteDTO atualizarCliente(Long id, ClienteDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente com ID " + id + " não encontrado."));

        // Valida se o novo CPF ou E-mail já não pertence a OUTRO cliente
        clienteRepository.findByCpf(clienteDTO.getCpf()).ifPresent(c -> {
            if (!c.getId().equals(id)) {
                throw new IllegalArgumentException("Já existe outro cliente com o CPF informado.");
            }
        });
        if (clienteDTO.getEmail() != null && !clienteDTO.getEmail().isEmpty()) {
            clienteRepository.findByEmailIgnoreCase(clienteDTO.getEmail()).ifPresent(c -> {
                if (!c.getId().equals(id)) {
                    throw new IllegalArgumentException("Já existe outro cliente com o e-mail informado.");
                }
            });
        }

        // Atualiza os dados do cliente
        clienteExistente.setNome(clienteDTO.getNome());
        clienteExistente.setEmail(clienteDTO.getEmail());
        clienteExistente.setTelefone(clienteDTO.getTelefone());
        clienteExistente.setCpf(clienteDTO.getCpf());
        clienteExistente.setObservacoes(clienteDTO.getObservacoes());

        // Atualiza a associação com o corretor
        if (clienteDTO.getCorretorId() != null) {
            if (clienteExistente.getCorretor() == null || !clienteDTO.getCorretorId().equals(clienteExistente.getCorretor().getId())) {
                Corretor novoCorretor = corretorRepository.findById(clienteDTO.getCorretorId())
                        .orElseThrow(() -> new IllegalArgumentException("Corretor com ID " + clienteDTO.getCorretorId() + " não encontrado."));
                clienteExistente.setCorretor(novoCorretor);
            }
        } else {
            // Se o ID do corretor não for enviado, a associação é removida
            clienteExistente.setCorretor(null);
        }

        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        return ClienteDTO.fromEntity(clienteAtualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> buscarPorId(Long id) {
        return clienteRepository.findById(id).map(ClienteDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(ClienteDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente com ID " + id + " não encontrado.");
        }
        clienteRepository.deleteById(id);
    }
}