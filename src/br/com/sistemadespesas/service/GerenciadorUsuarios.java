package src.br.com.sistemadespesas.service;


import src.br.com.sistemadespesas.model.Usuario;
import src.br.com.sistemadespesas.util.Criptografia;

import java.util.HashMap;
import java.util.Map;

/**
 * Service (Serviço) responsável pelas regras de negócio
 * relacionadas a Usuários (autenticação, cadastro).
 * v0.0.2
 */
public class GerenciadorUsuarios {

    // Dados em memória para a v0.0.2
    private Map<String, Usuario> usuariosCadastrados;
    private int proximoIdUsuario = 1;

    public GerenciadorUsuarios() {
        this.usuariosCadastrados = new HashMap<>();
        // Adiciona um usuário padrão para teste
        cadastrarUsuario("admin", "admin123");
    }

    public Usuario cadastrarUsuario(String nomeUsuario, String senha) {
        if (usuariosCadastrados.containsKey(nomeUsuario)) {
            System.out.println("ERRO: Nome de usuário '" + nomeUsuario + "' já existe.");
            return null;
        }

        // Delega a simulação do hash para o util
        String hash = Criptografia.simularHash(senha);
        
        Usuario novoUsuario = new Usuario(proximoIdUsuario++, nomeUsuario, hash);
        usuariosCadastrados.put(nomeUsuario, novoUsuario);

        System.out.println("SUCESSO: Usuário '" + nomeUsuario + "' cadastrado.");
        return novoUsuario;
    }

    public Usuario login(String nomeUsuario, String senha) {
        Usuario usuario = usuariosCadastrados.get(nomeUsuario);
        
        if (usuario == null) {
            System.out.println("ERRO: Usuário '" + nomeUsuario + "' não encontrado.");
            return null;
        }

        // Simula a verificação do hash
        String hashTentativa = Criptografia.simularHash(senha);
        
        if (usuario.verificarSenha(hashTentativa)) {
            System.out.println("SUCESSO: Login de '" + nomeUsuario + "' realizado.");
            return usuario;
        } else {
            System.out.println("ERRO: Senha incorreta para '" + nomeUsuario + "'.");
            return null;
        }
    }
}