package com.imobiliaria.crm.dto;

import com.imobiliaria.crm.model.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClienteDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 255)
    private String nome;

    @Email(message = "Formato de e-mail inválido.")
    @Size(max = 100)
    private String email; // Opcional, pois não é NOT NULL no DTO

    @Size(max = 20)
    private String telefone;

    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "Formato de CPF inválido. Utilize o formato XXX.XXX.XXX-XX.")
    private String cpf;

    private String observacoes;

    private LocalDateTime dataCadastro;

    // Para receber o ID do corretor ao criar/atualizar
    private Long corretorId;

    // Para exibir os dados completos do corretor ao buscar
    private CorretorDTO corretor;

    // --- MÉTODOS DE CONVERSÃO ---

    public static ClienteDTO fromEntity(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setCpf(cliente.getCpf());
        dto.setObservacoes(cliente.getObservacoes());
        dto.setDataCadastro(cliente.getDataCadastro());

        if (cliente.getCorretor() != null) {
            dto.setCorretorId(cliente.getCorretor().getId());
            dto.setCorretor(CorretorDTO.fromEntity(cliente.getCorretor()));
        }

        return dto;
    }

    public Cliente toEntity() {
        Cliente cliente = new Cliente();
        cliente.setId(this.id);
        cliente.setNome(this.nome);
        cliente.setEmail(this.email);
        cliente.setTelefone(this.telefone);
        cliente.setCpf(this.cpf);
        cliente.setObservacoes(this.observacoes);

        // A associação com o Corretor deve ser feita na camada de serviço,
        // buscando a entidade Corretor pelo corretorId deste DTO.
        return cliente;
    }
}