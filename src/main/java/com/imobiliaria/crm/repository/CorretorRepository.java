package com.imobiliaria.crm.repository;

import com.imobiliaria.crm.model.Corretor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CorretorRepository extends JpaRepository<Corretor, Long> {
    Optional<Corretor> findByCpf(String cpf);
    Optional<Corretor> findByEmail(String email);
    Optional<Corretor> findByCreci(String creci);
    List<Corretor> findByAtivo(boolean ativo);
}