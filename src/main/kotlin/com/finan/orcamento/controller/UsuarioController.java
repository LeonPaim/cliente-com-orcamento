package com.finan.orcamento.controller;

import com.finan.orcamento.model.UsuarioModel;
import com.finan.orcamento.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String getUsuarioPage(Model model) {
        model.addAttribute("usuarioModel", new UsuarioModel());
        model.addAttribute("usuarios", usuarioService.buscarUsuario());
        return "usuarioPage";
    }

    @PostMapping("/save")
    public String salvarOuAtualizar(@ModelAttribute UsuarioModel usuarioModel,
                                    RedirectAttributes redirectAttributes) {
        try {
            if (usuarioModel.getId() != null) {
                usuarioService.atualizaUsuario(usuarioModel, usuarioModel.getId());
                redirectAttributes.addFlashAttribute("success", "Usuário atualizado com sucesso!");
            } else {
                usuarioService.cadastrarUsuario(usuarioModel);
                redirectAttributes.addFlashAttribute("success", "Usuário cadastrado com sucesso!");
            }
            return "redirect:/usuarios";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios";
        }
    }

    @GetMapping("/pesquisa")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.buscarUsuario());
        model.addAttribute("usuarioModel", new UsuarioModel());
        return "usuarioPage";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<UsuarioModel> searchUsuarios(@RequestParam("searchTerm") String searchTerm) {
        return usuarioService.buscarPorNome(searchTerm);
    }

    @GetMapping("/pesquisar")
    public String pesquisarPorNome(@RequestParam("nome") String nome, Model model) {
        model.addAttribute("usuarios", usuarioService.buscarPorNome(nome));
        model.addAttribute("usuarioModel", new UsuarioModel());
        return "usuarioPage";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        UsuarioModel usuario = usuarioService.buscaId(id);
        model.addAttribute("usuarioModel", usuario);
        model.addAttribute("usuarios", usuarioService.buscarUsuario());
        return "usuarioPage";
    }
}