package com.finan.orcamento.service;

import com.finan.orcamento.model.ClienteModel;
import com.finan.orcamento.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteModel cadastrarCliente(ClienteModel cliente) {
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("CPF já cadastrado no sistema");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    public List<ClienteModel> buscarClientes() {
        return clienteRepository.findAll();
    }

    public ClienteModel buscaId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public ClienteModel atualizaCliente(ClienteModel cliente, Long id) {
        ClienteModel clienteAtual = buscaId(id);
        clienteAtual.setNome(cliente.getNome());
        clienteAtual.setCpf(cliente.getCpf());
        return clienteRepository.save(clienteAtual);
    }

    public List<ClienteModel> buscarPorNome(String nome) {
        System.out.println("🔍 Buscando clientes por nome: " + nome);
        List<ClienteModel> resultados = clienteRepository.findByNomeContainingIgnoreCase(nome);
        System.out.println("✅ Encontrados " + resultados.size() + " clientes com nome: " + nome);
        resultados.forEach(cliente ->
                System.out.println("   - " + cliente.getNome() + " | CPF: " + cliente.getCpf())
        );
        return resultados;
    }

    public List<ClienteModel> buscarPorCpf(String cpf) {
        System.out.println("🔍 Buscando clientes por CPF: " + cpf);
        List<ClienteModel> resultados = clienteRepository.findByCpfContaining(cpf);
        System.out.println("✅ Encontrados " + resultados.size() + " clientes com CPF: " + cpf);
        return resultados;
    }

    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}