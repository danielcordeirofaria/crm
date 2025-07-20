package com.imobiliaria.crm.repository;

import com.imobiliaria.crm.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Métodos para verificar duplicidade de dados
    Optional<Cliente> findByCpf(String cpf);
    Optional<Cliente> findByEmailIgnoreCase(String email);
}
