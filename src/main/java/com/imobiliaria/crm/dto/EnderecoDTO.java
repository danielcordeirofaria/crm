package com.imobiliaria.crm.dto;

import com.imobiliaria.crm.model.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnderecoDTO {

    @NotBlank(message = "O logradouro é obrigatório.")
    @Size(max = 255)
    private String logradouro;

    @NotBlank(message = "O bairro é obrigatório.")
    @Size(max = 100)
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória.")
    @Size(max = 100)
    private String cidade;

    @NotBlank(message = "O estado é obrigatório.")
    @Size(min = 2, max = 2, message = "O estado deve ser a sigla com 2 caracteres.")
    private String estado;

    @Size(max = 9, message = "O CEP deve ter no máximo 9 caracteres.")
    private String cep;

    @Size(max = 50)
    private String complemento;

    // --- MÉTODOS DE CONVERSÃO ---

    public static EnderecoDTO fromEntity(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        EnderecoDTO dto = new EnderecoDTO();
        dto.setLogradouro(endereco.getLogradouro());
        dto.setBairro(endereco.getBairro());
        dto.setCidade(endereco.getCidade());
        dto.setEstado(endereco.getEstado());
        dto.setCep(endereco.getCep());
        dto.setComplemento(endereco.getComplemento());
        return dto;
    }

    public Endereco toEntity() {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(this.logradouro);
        endereco.setBairro(this.bairro);
        endereco.setCidade(this.cidade);
        endereco.setEstado(this.estado);
        endereco.setCep(this.cep);
        endereco.setComplemento(this.complemento);
        return endereco;
    }
}