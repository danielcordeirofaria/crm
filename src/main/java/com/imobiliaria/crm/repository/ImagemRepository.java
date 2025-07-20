package com.imobiliaria.crm.repository;

import com.imobiliaria.crm.model.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Long> {

    List<Imagem> findByImovelId(Long imovelId);

    Optional<Imagem> findByIdAndImovelId(Long imagemId, Long imovelId);
}