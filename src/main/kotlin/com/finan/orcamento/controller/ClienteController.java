package com.finan.orcamento.controller;

import com.finan.orcamento.model.ClienteModel;
import com.finan.orcamento.model.dto.RelatorioClienteDTO;
import com.finan.orcamento.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Métodos existentes...

    @GetMapping("/relatorio")
    public String getRelatorioPage(Model model) {
        try {
            model.addAttribute("clientes", clienteService.buscarClientes());
            // Lista vazia temporariamente
            model.addAttribute("relatorioClientes", java.util.Collections.emptyList());
            return "relatorioClientePage";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erro: " + e.getMessage());
            return "relatorioClientePage";
        }
    }




    @GetMapping
    public String getClientePage(Model model) {
        model.addAttribute("clienteModel", new ClienteModel());
        model.addAttribute("clientes", clienteService.buscarClientes());
        return "clientePage";
    }









    // NOVO: Gerar relatório com filtros
    @PostMapping("/relatorio/gerar")
    public String gerarRelatorio(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @RequestParam(value = "valorMin", required = false) BigDecimal valorMin,
            @RequestParam(value = "valorMax", required = false) BigDecimal valorMax,
            @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Model model) {

        try {
            List<RelatorioClienteDTO> relatorio = clienteService.gerarRelatorioClientes(
                    clienteId, valorMin, valorMax, dataInicio, dataFim);

            model.addAttribute("relatorioClientes", relatorio);
            model.addAttribute("clientes", clienteService.buscarClientes());
            model.addAttribute("filtroClienteId", clienteId);
            model.addAttribute("filtroValorMin", valorMin);
            model.addAttribute("filtroValorMax", valorMax);
            model.addAttribute("filtroDataInicio", dataInicio);
            model.addAttribute("filtroDataFim", dataFim);

        } catch (Exception e) {
            model.addAttribute("error", "Erro ao gerar relatório: " + e.getMessage());
        }

        return "relatorioClientePage";
    }

    // NOVO: Endpoint API para relatório
    @GetMapping("/api/relatorio")
    @ResponseBody
    public ResponseEntity<List<RelatorioClienteDTO>> getRelatorioApi(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @RequestParam(value = "valorMin", required = false) BigDecimal valorMin,
            @RequestParam(value = "valorMax", required = false) BigDecimal valorMax,
            @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        List<RelatorioClienteDTO> relatorio = clienteService.gerarRelatorioClientes(
                clienteId, valorMin, valorMax, dataInicio, dataFim);

        return ResponseEntity.ok(relatorio);
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









}