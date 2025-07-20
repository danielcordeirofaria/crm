package com.imobiliaria.crm.service.imovel;

import com.imobiliaria.crm.dto.ImovelDTO;
import com.imobiliaria.crm.model.Caracteristica;
import com.imobiliaria.crm.model.Corretor;
import com.imobiliaria.crm.model.Imovel;
import com.imobiliaria.crm.repository.CaracteristicaRepository;
import com.imobiliaria.crm.repository.CorretorRepository;
import com.imobiliaria.crm.repository.ImovelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ImovelServiceImpl implements IImovelService {

    private final ImovelRepository imovelRepository;
    private final CorretorRepository corretorRepository;
    private final CaracteristicaRepository caracteristicaRepository; // Nova dependência

    public ImovelServiceImpl(ImovelRepository imovelRepository,
                             CorretorRepository corretorRepository,
                             CaracteristicaRepository caracteristicaRepository) {
        this.imovelRepository = imovelRepository;
        this.corretorRepository = corretorRepository;
        this.caracteristicaRepository = caracteristicaRepository;
    }

    @Override
    @Transactional
    public ImovelDTO criarImovel(ImovelDTO imovelDTO) {
        // 1. Converte a parte principal do DTO para a entidade
        Imovel imovel = imovelDTO.toEntity();

        // 2. Associa o corretor, se um ID for fornecido
        if (imovelDTO.getCorretorId() != null) {
            Corretor corretor = corretorRepository.findById(imovelDTO.getCorretorId())
                    .orElseThrow(() -> new IllegalArgumentException("Corretor com ID " + imovelDTO.getCorretorId() + " não encontrado."));
            imovel.setCorretor(corretor);
        }

        // 3. Associa as características, se IDs forem fornecidos
        if (imovelDTO.getCaracteristicaIds() != null && !imovelDTO.getCaracteristicaIds().isEmpty()) {
            Set<Caracteristica> caracteristicas = new HashSet<>(caracteristicaRepository.findAllById(imovelDTO.getCaracteristicaIds()));
            if (caracteristicas.size() != imovelDTO.getCaracteristicaIds().size()) {
                throw new IllegalArgumentException("Uma ou mais IDs de características são inválidas.");
            }
            imovel.setCaracteristicas(caracteristicas);
        }

        // 4. Define valores padrão
        if (imovel.getStatus() == null || imovel.getStatus().isEmpty()) {
            imovel.setStatus("DISPONIVEL");
        }

        Imovel imovelSalvo = imovelRepository.save(imovel);
        return ImovelDTO.fromEntity(imovelSalvo);
    }

    @Override
    @Transactional
    public ImovelDTO atualizarImovel(Long id, ImovelDTO imovelDTO) {
        // 1. Busca o imóvel existente no banco
        Imovel imovelExistente = imovelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado com ID: " + id));

        // 2. Atualiza os campos simples e o endereço embutido
        updateImovelData(imovelExistente, imovelDTO);

        // 3. Atualiza a associação com o Corretor
        updateCorretorAssociation(imovelExistente, imovelDTO);

        // 4. Atualiza a associação com as Características
        updateCaracteristicasAssociation(imovelExistente, imovelDTO);

        Imovel imovelAtualizado = imovelRepository.save(imovelExistente);
        return ImovelDTO.fromEntity(imovelAtualizado);
    }

    private void updateImovelData(Imovel imovel, ImovelDTO dto) {
        imovel.setCodigo(dto.getCodigo());
        imovel.setTipo(dto.getTipo());
        imovel.setFinalidade(dto.getFinalidade());
        imovel.setPreco(dto.getPreco());
        imovel.setValorCondominio(dto.getValorCondominio());
        imovel.setValorIptu(dto.getValorIptu());
        imovel.setAreaTotal(dto.getAreaTotal());
        imovel.setAreaUtil(dto.getAreaUtil());
        imovel.setQuartos(dto.getQuartos());
        imovel.setSuites(dto.getSuites());
        imovel.setBanheiros(dto.getBanheiros());
        imovel.setVagasGaragem(dto.getVagasGaragem());
        imovel.setAnoConstrucao(dto.getAnoConstrucao());
        imovel.setDescricao(dto.getDescricao());
        imovel.setStatus(dto.getStatus());
        imovel.setPublicado(dto.isPublicado());

        if (dto.getEndereco() != null) {
            imovel.setEndereco(dto.getEndereco().toEntity());
        } else {
            imovel.setEndereco(null);
        }
    }

    private void updateCorretorAssociation(Imovel imovel, ImovelDTO dto) {
        if (dto.getCorretorId() != null) {
            // Se o ID do corretor no DTO for diferente do ID do corretor existente (ou se não havia corretor)
            if (imovel.getCorretor() == null || !dto.getCorretorId().equals(imovel.getCorretor().getId())) {
                Corretor novoCorretor = corretorRepository.findById(dto.getCorretorId())
                        .orElseThrow(() -> new IllegalArgumentException("Corretor com ID " + dto.getCorretorId() + " não encontrado."));
                imovel.setCorretor(novoCorretor);
            }
        } else {
            // Se o DTO veio sem corretorId, removemos a associação
            imovel.setCorretor(null);
        }
    }

    private void updateCaracteristicasAssociation(Imovel imovel, ImovelDTO dto) {
        if (dto.getCaracteristicaIds() != null) {
            if (dto.getCaracteristicaIds().isEmpty()) {
                imovel.getCaracteristicas().clear();
            } else {
                Set<Caracteristica> novasCaracteristicas = new HashSet<>(caracteristicaRepository.findAllById(dto.getCaracteristicaIds()));
                if (novasCaracteristicas.size() != dto.getCaracteristicaIds().size()) {
                    throw new IllegalArgumentException("Uma ou mais IDs de características são inválidas.");
                }
                imovel.setCaracteristicas(novasCaracteristicas);
            }
        }
        // Se caracteristicaIds for nulo no DTO, não fazemos nenhuma alteração nas características existentes.
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImovelDTO> buscarPorId(Long id) {
        return imovelRepository.findById(id)
                .map(ImovelDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImovelDTO> listarTodos() {
        return imovelRepository.findAll().stream()
                .map(ImovelDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletarImovel(Long id) {
        if (!imovelRepository.existsById(id)) {
            throw new IllegalArgumentException("Imóvel não encontrado com ID: " + id);
        }
        imovelRepository.deleteById(id);
    }
}