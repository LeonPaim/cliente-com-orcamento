package com.finan.orcamento.service;

import com.finan.orcamento.model.ClienteModel;
import com.finan.orcamento.model.OrcamentoModel;
import com.finan.orcamento.model.UsuarioModel;
import com.finan.orcamento.repositories.OrcamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrcamentoService {

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteService clienteService;

    public List<OrcamentoModel> buscarCadastro() {
        return orcamentoRepository.findAll();
    }

    public OrcamentoModel buscaId(Long id) {
        Optional<OrcamentoModel> obj = orcamentoRepository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));
    }

    @Transactional
    public OrcamentoModel cadastrarOrcamento(OrcamentoModel orcamentoModel) {
        try {
            System.out.println("💾 Iniciando cadastro de orçamento...");

            // ✅ CORREÇÃO: Validar e carregar usuário CORRETAMENTE
            if (orcamentoModel.getUsuario() != null && orcamentoModel.getUsuario().getId() != null) {
                System.out.println("🔍 Buscando usuário com ID: " + orcamentoModel.getUsuario().getId());
                UsuarioModel usuario = usuarioService.buscaId(orcamentoModel.getUsuario().getId());
                orcamentoModel.setUsuario(usuario);
                System.out.println("✅ Usuário carregado: " + usuario.getNomeUsuario());
            } else {
                System.out.println("ℹ️ Nenhum usuário associado ao orçamento");
                orcamentoModel.setUsuario(null); // Garantir que seja null se não tiver ID
            }

            // ✅ CORREÇÃO: Validar e carregar cliente CORRETAMENTE
            if (orcamentoModel.getCliente() != null && orcamentoModel.getCliente().getId() != null) {
                System.out.println("🔍 Buscando cliente com ID: " + orcamentoModel.getCliente().getId());
                ClienteModel cliente = clienteService.buscaId(orcamentoModel.getCliente().getId());
                orcamentoModel.setCliente(cliente);
                System.out.println("✅ Cliente carregado: " + cliente.getNome());
            } else {
                System.out.println("ℹ️ Nenhum cliente associado ao orçamento");
                orcamentoModel.setCliente(null); // Garantir que seja null se não tiver ID
            }

            // ✅ Calcular ICMS
            orcamentoModel.calcularIcms();
            System.out.println("🧮 ICMS calculado: " + orcamentoModel.getValorICMS());

            // ✅ Salvar orçamento
            OrcamentoModel saved = orcamentoRepository.save(orcamentoModel);
            System.out.println("✅ Orçamento salvo com ID: " + saved.getId());

            return saved;

        } catch (Exception e) {
            System.err.println("❌ Erro ao cadastrar orçamento: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao cadastrar orçamento: " + e.getMessage());
        }
    }

    @Transactional
    public OrcamentoModel atualizaCadastro(OrcamentoModel orcamentoModel, Long id) {
        try {
            System.out.println("🔄 Atualizando orçamento ID: " + id);

            OrcamentoModel existing = buscaId(id);
            existing.setValorOrcamento(orcamentoModel.getValorOrcamento());
            existing.setIcmsEstados(orcamentoModel.getIcmsEstados());
            existing.setDescricao(orcamentoModel.getDescricao());

            // ✅ CORREÇÃO: Carregar usuário existente
            if (orcamentoModel.getUsuario() != null && orcamentoModel.getUsuario().getId() != null) {
                UsuarioModel usuario = usuarioService.buscaId(orcamentoModel.getUsuario().getId());
                existing.setUsuario(usuario);
                System.out.println("✅ Usuário atualizado: " + usuario.getNomeUsuario());
            } else {
                existing.setUsuario(null);
                System.out.println("ℹ️ Usuário removido do orçamento");
            }

            // ✅ CORREÇÃO: Carregar cliente existente
            if (orcamentoModel.getCliente() != null && orcamentoModel.getCliente().getId() != null) {
                ClienteModel cliente = clienteService.buscaId(orcamentoModel.getCliente().getId());
                existing.setCliente(cliente);
                System.out.println("✅ Cliente atualizado: " + cliente.getNome());
            } else {
                existing.setCliente(null);
                System.out.println("ℹ️ Cliente removido do orçamento");
            }

            existing.calcularIcms();
            OrcamentoModel updated = orcamentoRepository.save(existing);
            System.out.println("✅ Orçamento atualizado com sucesso");

            return updated;

        } catch (Exception e) {
            System.err.println("❌ Erro ao atualizar orçamento: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar orçamento: " + e.getMessage());
        }
    }

    public void deletaOrcamento(Long id) {
        try {
            System.out.println("🗑️ Deletando orçamento ID: " + id);
            orcamentoRepository.deleteById(id);
            System.out.println("✅ Orçamento deletado com sucesso");
        } catch (Exception e) {
            System.err.println("❌ Erro ao deletar orçamento: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar orçamento: " + e.getMessage());
        }
    }
}