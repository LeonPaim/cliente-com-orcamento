package com.finan.orcamento.controller;

import com.finan.orcamento.model.ClienteModel;
import com.finan.orcamento.model.OrcamentoModel;
import com.finan.orcamento.model.UsuarioModel;
import com.finan.orcamento.service.ClienteService;
import com.finan.orcamento.service.OrcamentoService;
import com.finan.orcamento.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orcamentos")
public class OrcamentoController {

    @Autowired
    private OrcamentoService orcamentoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String getOrcamentoPage(Model model) {
        try {
            System.out.println("📦 Carregando página de orçamentos...");

            // Inicializar um novo orçamento
            OrcamentoModel novoOrcamento = new OrcamentoModel();
            model.addAttribute("novoOrcamento", novoOrcamento);

            // Buscar todos os orçamentos
            List<OrcamentoModel> orcamentos = orcamentoService.buscarCadastro();
            System.out.println("✅ Encontrados " + orcamentos.size() + " orçamentos");

            model.addAttribute("orcamentos", orcamentos);

            return "orcamentoPage";

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar página de orçamentos: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Erro ao carregar orçamentos: " + e.getMessage());
            return "orcamentoPage";
        }
    }

    @GetMapping("/usuarios/search")
    @ResponseBody
    public List<UsuarioModel> searchUsuario(@RequestParam("searchTerm") String searchTerm) {
        System.out.println("🔍 Pesquisando usuário: " + searchTerm);
        return usuarioService.buscarPorNome(searchTerm);
    }

    @GetMapping("/clientes/search")
    @ResponseBody
    public List<ClienteModel> searchCliente(@RequestParam("searchTerm") String searchTerm) {
        System.out.println("🔍 Pesquisando cliente por nome: " + searchTerm);
        return clienteService.buscarPorNome(searchTerm);
    }

    @GetMapping("/clientes/searchCpf")
    @ResponseBody
    public List<ClienteModel> searchClientePorCpf(@RequestParam("searchTerm") String searchTerm) {
        System.out.println("🔍 Pesquisando cliente por CPF: " + searchTerm);
        return clienteService.buscarPorCpf(searchTerm);
    }

    @PostMapping
    public String cadastraOrcamento(@ModelAttribute OrcamentoModel orcamentoModel) {
        try {
            System.out.println("💾 Salvando orçamento...");
            orcamentoService.cadastrarOrcamento(orcamentoModel);
            System.out.println("✅ Orçamento salvo com sucesso!");
            return "redirect:/orcamentos";
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar orçamento: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/orcamentos?error=" + e.getMessage();
        }
    }


    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<OrcamentoModel>> buscaTodosOrcamentos() {
        return ResponseEntity.ok(orcamentoService.buscarCadastro());
    }

    @GetMapping("/api/pesquisaid/{id}")
    @ResponseBody
    public ResponseEntity<OrcamentoModel> buscaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(orcamentoService.buscaId(id));
    }

    @PostMapping("/api/put/{id}")
    @ResponseBody
    public ResponseEntity<OrcamentoModel> atualizaOrcamento(@RequestBody OrcamentoModel orcamentoModel, @PathVariable Long id) {
        return ResponseEntity.ok(orcamentoService.atualizaCadastro(orcamentoModel, id));
    }

    @DeleteMapping("/api/delete/{id}")
    @ResponseBody
    public void deleteOrcamento(@PathVariable Long id) {
        orcamentoService.deletaOrcamento(id);
    }

    // Endpoint de debug
    @GetMapping("/debug")
    @ResponseBody
    public String debug() {
        return "OrcamentoController está funcionando!";
    }
}