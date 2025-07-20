package com.imobiliaria.crm.service.corretor;

import com.imobiliaria.crm.dto.CorretorDTO;
import com.imobiliaria.crm.model.Corretor;
import com.imobiliaria.crm.repository.CorretorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

@Service
public class CorretorServiceImpl implements ICorretorService {

    private static final Pattern CPF_PATTERN = Pattern.compile("^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}$");

    @Autowired
    private CorretorRepository corretorRepository;

    @Transactional
    public CorretorDTO criarCorretor(CorretorDTO corretorDTO) {

        validarDadosCorretor(corretorDTO);

        Corretor corretor = corretorDTO.toEntity();
        corretor.setAtivo(true);
        corretor = corretorRepository.save(corretor);
        return CorretorDTO.fromEntity(corretor);
    }

    private void validarDadosCorretor(CorretorDTO dto) {

        // Validação de campos obrigatórios e formato
        if (!StringUtils.hasText(dto.getNome())) {
            throw new IllegalArgumentException("O nome do corretor é obrigatório.");
        }
        if (!StringUtils.hasText(dto.getCpf())) {
            throw new IllegalArgumentException("O CPF do corretor é obrigatório.");
        }
        if (!CPF_PATTERN.matcher(dto.getCpf()).matches()) {
            throw new IllegalArgumentException("Formato de CPF inválido. Use o formato XXX.XXX.XXX-XX.");
        }
        if (!StringUtils.hasText(dto.getEmail())) {
            throw new IllegalArgumentException("O e-mail do corretor é obrigatório.");
        }
        if (!isValidEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }

        // Validação de unicidade (Regras de Negócio)
        if (corretorRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        if (corretorRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        if (StringUtils.hasText(dto.getCreci()) && corretorRepository.findByCreci(dto.getCreci()).isPresent()) {
            throw new IllegalArgumentException("CRECI já cadastrado.");
        }
    }

    @Transactional
    public CorretorDTO atualizarCorretor(Long id, CorretorDTO corretorDTO) {
        Corretor corretor = corretorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Corretor não encontrado com ID: " + id));

        // Atualiza apenas os campos fornecidos
        if (corretorDTO.getNome() != null && !corretorDTO.getNome().trim().isEmpty()) {
            corretor.setNome(corretorDTO.getNome());
        }
        if (corretorDTO.getEmail() != null && !corretorDTO.getEmail().trim().isEmpty()) {
            if (!corretorDTO.getEmail().equals(corretor.getEmail()) &&
                    corretorRepository.findByEmail(corretorDTO.getEmail()).isPresent()) {
                throw new IllegalArgumentException("E-mail já cadastrado.");
            }
            corretor.setEmail(corretorDTO.getEmail());
        }
        if (corretorDTO.getCpf() != null && !corretorDTO.getCpf().equals(corretor.getCpf()) &&
                corretorRepository.findByCpf(corretorDTO.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        if (corretorDTO.getCreci() != null && !corretorDTO.getCreci().equals(corretor.getCreci()) &&
                corretorRepository.findByCreci(corretorDTO.getCreci()).isPresent()) {
            throw new IllegalArgumentException("CRECI já cadastrado.");
        }

        // Atualiza campos
        if (corretorDTO.getCpf() != null) {
            corretor.setCpf(corretorDTO.getCpf());
        }
        if (corretorDTO.getTelefone() != null) {
            corretor.setTelefone(corretorDTO.getTelefone());
        }
        if (corretorDTO.getCreci() != null) {
            corretor.setCreci(corretorDTO.getCreci());
        }
        if (corretorDTO.getAtivo() != null) {
            corretor.setAtivo(corretorDTO.getAtivo());
        }

        corretor = corretorRepository.save(corretor);
        return CorretorDTO.fromEntity(corretor);
    }

    @Transactional(readOnly = true)
    public Optional<CorretorDTO> buscarPorId(Long id) {
        return corretorRepository.findById(id)
                .map(CorretorDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<CorretorDTO> listarCorretores(boolean apenasAtivos) {
        List<Corretor> corretores = apenasAtivos ? corretorRepository.findByAtivo(true) : corretorRepository.findAll();
        return corretores.stream()
                .map(CorretorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void toggleAtivo(Long id) {
        Corretor corretor = corretorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Corretor não encontrado com ID: " + id));
        corretor.setAtivo(!corretor.isAtivo());
        corretorRepository.save(corretor);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}