package com.finan.orcamento.service;

import com.finan.orcamento.model.ClienteModel;
import com.finan.orcamento.model.dto.RelatorioClienteDTO;
import com.finan.orcamento.repositories.ClienteRepository;
import com.finan.orcamento.repositories.OrcamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    // Métodos existentes...
    public List<ClienteModel> buscarClientes() {
        return clienteRepository.findAll();
    }

    public ClienteModel cadastrarCliente(ClienteModel clienteModel) {
        return clienteRepository.save(clienteModel);
    }

    public ClienteModel atualizaCliente(ClienteModel clienteModel, Long id) {
        clienteModel.setId(id);
        return clienteRepository.save(clienteModel);
    }

    public ClienteModel buscaId(Long id) {
        Optional<ClienteModel> cliente = clienteRepository.findById(id);
        return cliente.orElse(null);
    }

    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public List<ClienteModel> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<ClienteModel> buscarPorCpf(String cpf) {
        return clienteRepository.findByCpfContaining(cpf);
    }

    // NOVOS MÉTODOS PARA RELATÓRIO
    public List<RelatorioClienteDTO> gerarRelatorioClientes(Long clienteId, BigDecimal valorMin,
                                                            BigDecimal valorMax, LocalDate dataInicio,
                                                            LocalDate dataFim) {

        LocalDateTime dataInicioDateTime = null;
        LocalDateTime dataFimDateTime = null;

        if (dataInicio != null) {
            dataInicioDateTime = dataInicio.atStartOfDay();
        }

        if (dataFim != null) {
            dataFimDateTime = dataFim.atTime(LocalTime.MAX);
        }

        return orcamentoRepository.findRelatorioClientes(
                clienteId, valorMin, valorMax, dataInicioDateTime, dataFimDateTime);
    }
}