package com.finan.orcamento.controller;

import com.finan.orcamento.model.ClienteModel;
import com.finan.orcamento.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String getClientePage(Model model) {
        model.addAttribute("clienteModel", new ClienteModel());
        model.addAttribute("clientes", clienteService.buscarClientes());
        return "clientePage";
    }

    @PostMapping("/save")
    public String salvarOuAtualizar(@ModelAttribute ClienteModel clienteModel,
                                    RedirectAttributes redirectAttributes) {
        try {
            if (clienteModel.getId() != null) {
                clienteService.atualizaCliente(clienteModel, clienteModel.getId());
                redirectAttributes.addFlashAttribute("success", "Cliente atualizado com sucesso!");
            } else {
                clienteService.cadastrarCliente(clienteModel);
                redirectAttributes.addFlashAttribute("success", "Cliente cadastrado com sucesso!");
            }
            return "redirect:/clientes";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes";
        }
    }

    @GetMapping("/search")
    @ResponseBody
    public List<ClienteModel> searchClientes(@RequestParam("searchTerm") String searchTerm) {
        return clienteService.buscarPorNome(searchTerm);
    }

    @GetMapping("/searchCpf")
    @ResponseBody
    public List<ClienteModel> searchClientesPorCpf(@RequestParam("searchTerm") String searchTerm) {
        return clienteService.buscarPorCpf(searchTerm);
    }

    @GetMapping("/pesquisar")
    public String pesquisarPorNome(@RequestParam("nome") String nome, Model model) {
        model.addAttribute("clientes", clienteService.buscarPorNome(nome));
        model.addAttribute("clienteModel", new ClienteModel());
        return "clientePage";
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        ClienteModel cliente = clienteService.buscaId(id);
        model.addAttribute("clienteModel", cliente);
        model.addAttribute("clientes", clienteService.buscarClientes());
        return "clientePage";
    }

    @GetMapping("/delete/{id}")
    public String deletarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.deletarCliente(id);
            redirectAttributes.addFlashAttribute("success", "Cliente deletado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao deletar cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    // Endpoints REST para API
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<ClienteModel>> buscaTodosClientes() {
        return ResponseEntity.ok(clienteService.buscarClientes());
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<ClienteModel> buscaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscaId(id));
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<ClienteModel> cadastrarCliente(@RequestBody ClienteModel clienteModel) {
        return ResponseEntity.ok(clienteService.cadastrarCliente(clienteModel));
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<ClienteModel> atualizarCliente(@RequestBody ClienteModel clienteModel, @PathVariable Long id) {
        return ResponseEntity.ok(clienteService.atualizaCliente(clienteModel, id));
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public void deletarClienteApi(@PathVariable Long id) {
        clienteService.deletarCliente(id);
    }
}