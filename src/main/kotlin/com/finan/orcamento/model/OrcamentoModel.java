package com.finan.orcamento.model;

import com.finan.orcamento.model.enums.IcmsEstados;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "orcamento")
public class OrcamentoModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "icms_estados")
    private IcmsEstados icmsEstados;

    @Column(name = "valor_orcamento", precision = 10, scale = 2)
    private BigDecimal valorOrcamento;

    @Column(name = "valor_icms", precision = 10, scale = 2)
    private BigDecimal valorICMS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private ClienteModel cliente;

    @Column(name = "descricao", length = 255)
    private String descricao;

    public OrcamentoModel() {}

    public OrcamentoModel(BigDecimal valorOrcamento, IcmsEstados icmsEstados, UsuarioModel usuario, ClienteModel cliente, String descricao) {
        this.valorOrcamento = valorOrcamento;
        this.icmsEstados = icmsEstados;
        this.usuario = usuario;
        this.cliente = cliente;
        this.descricao = descricao;
        calcularIcms();
    }

    public void calcularIcms() {
        if (this.icmsEstados != null && this.valorOrcamento != null) {
            this.valorICMS = this.icmsEstados.getStrategy().calcular(this.valorOrcamento);
        }
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public IcmsEstados getIcmsEstados() { return icmsEstados; }
    public void setIcmsEstados(IcmsEstados icmsEstados) {
        this.icmsEstados = icmsEstados;
        calcularIcms();
    }

    public BigDecimal getValorOrcamento() { return valorOrcamento; }
    public void setValorOrcamento(BigDecimal valorOrcamento) {
        this.valorOrcamento = valorOrcamento;
        calcularIcms();
    }

    public BigDecimal getValorICMS() { return valorICMS; }
    public void setValorICMS(BigDecimal valorICMS) { this.valorICMS = valorICMS; }

    public UsuarioModel getUsuario() { return usuario; }
    public void setUsuario(UsuarioModel usuario) { this.usuario = usuario; }

    public ClienteModel getCliente() { return cliente; }
    public void setCliente(ClienteModel cliente) { this.cliente = cliente; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrcamentoModel that = (OrcamentoModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrcamentoModel{" +
                "id=" + id +
                ", valorOrcamento=" + valorOrcamento +
                ", valorICMS=" + valorICMS +
                ", icmsEstados=" + icmsEstados +
                ", descricao='" + descricao + '\'' +
                ", usuario=" + (usuario != null ? usuario.getNomeUsuario() : "null") +
                ", cliente=" + (cliente != null ? cliente.getNome() : "null") +
                '}';
    }
}