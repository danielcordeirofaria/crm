package com.imobiliaria.crm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.imobiliaria.crm.model.Corretor;

import java.time.LocalDateTime;

@Data
public class CorretorDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}$", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail deve ser válido")
    @Size(max = 100, message = "E-mail deve ter no máximo 100 caracteres")
    private String email;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    private String telefone;

    @Size(max = 20, message = "CRECI deve ter no máximo 20 caracteres")
    private String creci;

    private LocalDateTime dataCadastro;

    private Boolean ativo;

    // Converte DTO para entidade Corretor
    public Corretor toEntity() {
        Corretor corretor = new Corretor();
        corretor.setId(this.id);
        corretor.setNome(this.nome);
        corretor.setCpf(this.cpf);
        corretor.setEmail(this.email);
        corretor.setTelefone(this.telefone);
        corretor.setCreci(this.creci);
        corretor.setDataCadastro(this.dataCadastro);
        corretor.setAtivo(this.ativo != null ? this.ativo : true);
        return corretor;
    }

    // Converte entidade Corretor para DTO
    public static CorretorDTO fromEntity(Corretor corretor) {
        CorretorDTO dto = new CorretorDTO();
        dto.setId(corretor.getId());
        dto.setNome(corretor.getNome());
        dto.setCpf(corretor.getCpf());
        dto.setEmail(corretor.getEmail());
        dto.setTelefone(corretor.getTelefone());
        dto.setCreci(corretor.getCreci());
        dto.setDataCadastro(corretor.getDataCadastro());
        dto.setAtivo(corretor.isAtivo());
        return dto;
    }
}