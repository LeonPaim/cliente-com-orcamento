package com.finan.orcamento.repositories;

import com.finan.orcamento.model.OrcamentoModel;
import com.finan.orcamento.model.dto.RelatorioClienteDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrcamentoRepository extends JpaRepository<OrcamentoModel, Long> {

    @Query("SELECT new com.finan.orcamento.model.dto.RelatorioClienteDTO(" +
            "c.id, c.nome, c.cpf, SUM(o.valorOrcamento), COUNT(o), MAX(o.dataCriacao)) " +
            "FROM OrcamentoModel o " +
            "JOIN o.cliente c " +
            "WHERE (:clienteId IS NULL OR c.id = :clienteId) " +
            "AND (:valorMin IS NULL OR o.valorOrcamento >= :valorMin) " +
            "AND (:valorMax IS NULL OR o.valorOrcamento <= :valorMax) " +
            "AND (:dataInicio IS NULL OR o.dataCriacao >= :dataInicio) " +
            "AND (:dataFim IS NULL OR o.dataCriacao <= :dataFim) " +
            "GROUP BY c.id, c.nome, c.cpf")
    List<RelatorioClienteDTO> findRelatorioClientes(
            @Param("clienteId") Long clienteId,
            @Param("valorMin") BigDecimal valorMin,
            @Param("valorMax") BigDecimal valorMax,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);

    List<OrcamentoModel> findByClienteId(Long clienteId);
}