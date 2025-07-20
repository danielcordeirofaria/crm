package com.imobiliaria.crm.repository;

import com.imobiliaria.crm.model.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {
    // Método para evitar nomes duplicados
    Optional<Caracteristica> findByNomeIgnoreCase(String nome);
}