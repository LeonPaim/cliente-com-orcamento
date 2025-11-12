package com.finan.orcamento.service;

import com.finan.orcamento.model.UsuarioModel;
import com.finan.orcamento.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioModel cadastrarUsuario(UsuarioModel usuario) {
        try {
            System.out.println("✅ Tentando salvar usuário: " + usuario.getNomeUsuario());

            // ✅ REMOVER validações manuais - deixar o JPA fazer
            UsuarioModel saved = usuarioRepository.save(usuario);
            System.out.println("✅ Usuário salvo com ID: " + saved.getId());

            return saved;

        } catch (DataIntegrityViolationException e) {
            System.err.println("❌ ERRO: CPF ou RG já cadastrado");
            throw new RuntimeException("CPF ou RG já cadastrado no sistema");
        } catch (Exception e) {
            System.err.println("❌ ERRO ao salvar usuário: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public List<UsuarioModel> buscarUsuario() {
        List<UsuarioModel> usuarios = usuarioRepository.findAll();
        System.out.println("✅ Encontrados " + usuarios.size() + " usuários");
        return usuarios;
    }

    public UsuarioModel buscaId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UsuarioModel atualizaUsuario(UsuarioModel usuario, Long id) {
        UsuarioModel usuarioAtual = buscaId(id);
        usuarioAtual.setNomeUsuario(usuario.getNomeUsuario());
        usuarioAtual.setRg(usuario.getRg());
        usuarioAtual.setCpf(usuario.getCpf());
        usuarioAtual.setNomeMae(usuario.getNomeMae());
        return usuarioRepository.save(usuarioAtual);
    }

    public List<UsuarioModel> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeUsuarioContainingIgnoreCase(nome);
    }
}