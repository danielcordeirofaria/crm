package com.imobiliaria.crm.dto;

import com.imobiliaria.crm.model.Caracteristica;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CaracteristicaDTO {

    private Long id;

    @NotBlank(message = "O nome da característica é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    private String nome;

    /**
     * Converte uma entidade Caracteristica para CaracteristicaDTO.
     * @param caracteristica A entidade a ser convertida.
     * @return O DTO correspondente.
     */
    public static CaracteristicaDTO fromEntity(Caracteristica caracteristica) {
        if (caracteristica == null) {
            return null;
        }
        CaracteristicaDTO dto = new CaracteristicaDTO();
        dto.setId(caracteristica.getId());
        dto.setNome(caracteristica.getNome());
        return dto;
    }

    /**
     * Converte este DTO para uma entidade Caracteristica.
     * @return A entidade correspondente.
     */
    public Caracteristica toEntity() {
        Caracteristica caracteristica = new Caracteristica();
        caracteristica.setId(this.id);
        caracteristica.setNome(this.nome);
        return caracteristica;
    }
}