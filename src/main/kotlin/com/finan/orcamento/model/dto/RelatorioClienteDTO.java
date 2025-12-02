package com.finan.orcamento.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RelatorioClienteDTO {
    private Long clienteId;
    private String clienteNome;
    private String clienteCpf;
    private BigDecimal totalGasto;
    private Long quantidadeOrcamentos;
    private LocalDateTime dataUltimoOrcamento;
    private BigDecimal valorMedio;

    public RelatorioClienteDTO() {}

    public RelatorioClienteDTO(Long clienteId, String clienteNome, String clienteCpf,
                               BigDecimal totalGasto, Long quantidadeOrcamentos,
                               LocalDateTime dataUltimoOrcamento) {
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.clienteCpf = clienteCpf;
        this.totalGasto = totalGasto;
        this.quantidadeOrcamentos = quantidadeOrcamentos;
        this.dataUltimoOrcamento = dataUltimoOrcamento;
        this.valorMedio = quantidadeOrcamentos > 0 ?
                totalGasto.divide(BigDecimal.valueOf(quantidadeOrcamentos), 2, BigDecimal.ROUND_HALF_UP) :
                BigDecimal.ZERO;
    }

    // Getters e Setters
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }

    public String getClienteCpf() { return clienteCpf; }
    public void setClienteCpf(String clienteCpf) { this.clienteCpf = clienteCpf; }

    public BigDecimal getTotalGasto() { return totalGasto; }
    public void setTotalGasto(BigDecimal totalGasto) { this.totalGasto = totalGasto; }

    public Long getQuantidadeOrcamentos() { return quantidadeOrcamentos; }
    public void setQuantidadeOrcamentos(Long quantidadeOrcamentos) { this.quantidadeOrcamentos = quantidadeOrcamentos; }

    public LocalDateTime getDataUltimoOrcamento() { return dataUltimoOrcamento; }
    public void setDataUltimoOrcamento(LocalDateTime dataUltimoOrcamento) { this.dataUltimoOrcamento = dataUltimoOrcamento; }

    public BigDecimal getValorMedio() { return valorMedio; }
    public void setValorMedio(BigDecimal valorMedio) { this.valorMedio = valorMedio; }
}