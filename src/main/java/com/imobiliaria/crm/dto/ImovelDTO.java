package com.imobiliaria.crm.dto;

import com.imobiliaria.crm.model.Imovel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ImovelDTO {

    private Long id;

    // --- Identificação e Tipo ---
    @NotBlank(message = "O código do imóvel é obrigatório.")
    @Size(max = 50)
    private String codigo;

    @NotBlank(message = "O tipo do imóvel é obrigatório.")
    @Size(max = 50)
    private String tipo;

    @NotBlank(message = "A finalidade é obrigatória.")
    @Size(max = 20)
    private String finalidade;

    // --- Endereço Embutido ---
    @NotNull(message = "O endereço é obrigatório.")
    @Valid // Garante que as validações dentro de EnderecoDTO sejam acionadas
    private EnderecoDTO endereco;

    // --- Valores ---
    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser um valor positivo.")
    private BigDecimal preco;

    @PositiveOrZero(message = "O valor do condomínio não pode ser negativo.")
    private BigDecimal valorCondominio;

    @PositiveOrZero(message = "O valor do IPTU não pode ser negativo.")
    private BigDecimal valorIptu;

    // --- Estrutura ---
    @Positive(message = "A área total deve ser um valor positivo.")
    private BigDecimal areaTotal;

    @Positive(message = "A área útil deve ser um valor positivo.")
    private BigDecimal areaUtil;

    @PositiveOrZero(message = "O número de quartos não pode ser negativo.")
    private Integer quartos;

    @PositiveOrZero(message = "O número de suítes não pode ser negativo.")
    private Integer suites;

    @PositiveOrZero(message = "O número de banheiros não pode ser negativo.")
    private Integer banheiros;

    @PositiveOrZero(message = "O número de vagas na garagem não pode ser negativo.")
    private Integer vagasGaragem;

    private Integer anoConstrucao;

    // --- Descrição e Controle ---
    private String descricao;
    private String status;
    private boolean publicado;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    // --- Relacionamentos ---
    private Long corretorId;
    private CorretorDTO corretor; // Para exibir os dados completos do corretor

    private List<ImagemDTO> imagens; // Para exibir a lista de imagens

    // Para receber os IDs das características ao criar/atualizar um imóvel
    private Set<Long> caracteristicaIds;

    // Para exibir os dados completos das características
    private Set<CaracteristicaDTO> caracteristicas;

    // --- MÉTODOS DE CONVERSÃO ---

    public static ImovelDTO fromEntity(Imovel imovel) {
        ImovelDTO dto = new ImovelDTO();
        dto.setId(imovel.getId());
        dto.setCodigo(imovel.getCodigo());
        dto.setTipo(imovel.getTipo());
        dto.setFinalidade(imovel.getFinalidade());
        dto.setPreco(imovel.getPreco());
        dto.setValorCondominio(imovel.getValorCondominio());
        dto.setValorIptu(imovel.getValorIptu());
        dto.setAreaTotal(imovel.getAreaTotal());
        dto.setAreaUtil(imovel.getAreaUtil());
        dto.setQuartos(imovel.getQuartos());
        dto.setSuites(imovel.getSuites());
        dto.setBanheiros(imovel.getBanheiros());
        dto.setVagasGaragem(imovel.getVagasGaragem());
        dto.setAnoConstrucao(imovel.getAnoConstrucao());
        dto.setDescricao(imovel.getDescricao());
        dto.setStatus(imovel.getStatus());
        dto.setPublicado(imovel.isPublicado());
        dto.setDataCadastro(imovel.getDataCadastro());
        dto.setDataAtualizacao(imovel.getDataAtualizacao());

        // Converte o objeto Endereco para EnderecoDTO
        if (imovel.getEndereco() != null) {
            dto.setEndereco(EnderecoDTO.fromEntity(imovel.getEndereco()));
        }

        // Converte o Corretor associado para CorretorDTO
        if (imovel.getCorretor() != null) {
            dto.setCorretorId(imovel.getCorretor().getId());
            dto.setCorretor(CorretorDTO.fromEntity(imovel.getCorretor()));
        }

        // Converte a lista de Imagem para uma lista de ImagemDTO
        if (imovel.getImagens() != null) {
            dto.setImagens(imovel.getImagens().stream()
                    .map(ImagemDTO::fromEntity)
                    .collect(Collectors.toList()));
        } else {
            dto.setImagens(Collections.emptyList());
        }

        // Converte o Set de Caracteristica para um Set de CaracteristicaDTO
        if (imovel.getCaracteristicas() != null) {
            dto.setCaracteristicas(imovel.getCaracteristicas().stream()
                    .map(CaracteristicaDTO::fromEntity) // Supondo que CaracteristicaDTO.fromEntity exista
                    .collect(Collectors.toSet()));
        } else {
            dto.setCaracteristicas(Collections.emptySet());
        }

        return dto;
    }

    public Imovel toEntity() {
        Imovel imovel = new Imovel();
        imovel.setId(this.id);
        imovel.setCodigo(this.codigo);
        imovel.setTipo(this.tipo);
        imovel.setFinalidade(this.finalidade);
        imovel.setPreco(this.preco);
        imovel.setValorCondominio(this.valorCondominio);
        imovel.setValorIptu(this.valorIptu);
        imovel.setAreaTotal(this.areaTotal);
        imovel.setAreaUtil(this.areaUtil);
        imovel.setQuartos(this.quartos);
        imovel.setSuites(this.suites);
        imovel.setBanheiros(this.banheiros);
        imovel.setVagasGaragem(this.vagasGaragem);
        imovel.setAnoConstrucao(this.anoConstrucao);
        imovel.setDescricao(this.descricao);
        imovel.setStatus(this.status);
        imovel.setPublicado(this.publicado);

        // Converte o EnderecoDTO para o objeto Endereco
        if (this.endereco != null) {
            imovel.setEndereco(this.endereco.toEntity());
        }

        // A associação com Corretor, Imagens e Caracteristicas deve ser feita na camada de serviço.
        return imovel;
    }
}