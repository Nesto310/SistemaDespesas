package services;

import models.Usuario;
import utils.CriptografiaUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private static final String ARQUIVO_USUARIOS = "usuarios.txt";
    private List<Usuario> usuarios;

    public UsuarioService() {
        this.usuarios = new ArrayList<>();
        carregarUsuarios();
    }

    /**
     * Tenta autenticar um usuário.
     * @return O objeto Usuario se o login for bem-sucedido, null caso contrário.
     */
    public Usuario tentarLogin(String login, String senha) {
        Optional<Usuario> usuario = buscarPorLogin(login);
        if (usuario.isPresent()) {
            if (CriptografiaUtil.verificarSenha(senha, usuario.get().getSenhaCriptografada())) {
                return usuario.get();
            }
        }
        return null; // Login ou senha incorretos
    }
    
    /**
     * Cadastra um novo usuário, se o login não existir.
     * @return true se o cadastro foi bem-sucedido, false se o login já existe.
     */
    public boolean cadastrarUsuario(String login, String senha) {
        if (buscarPorLogin(login).isPresent()) {
            return false; // Usuário já existe
        }
        String senhaHash = CriptografiaUtil.criptografar(senha);
        Usuario novoUsuario = new Usuario(login, senhaHash);
        this.usuarios.add(novoUsuario);
        salvarUsuarios();
        return true;
    }
    

    public boolean editarUsuario(String login, String novaSenha) {
         Optional<Usuario> usuarioOpt = buscarPorLogin(login);
         if (usuarioOpt.isPresent()) {
             String novoHash = CriptografiaUtil.criptografar(novaSenha);
             usuarioOpt.get().setSenhaCriptografada(novoHash);
             salvarUsuarios();
             return true;
         }
         return false; // Usuário não encontrado
    }

    public List<Usuario> listarUsuarios() {
        return this.usuarios;
    }

    public Optional<Usuario> buscarPorLogin(String login) {
        return usuarios.stream()
                .filter(u -> u.getLogin().equalsIgnoreCase(login))
                .findFirst();
    }

    // --- Persistência em Arquivo ---

    private void carregarUsuarios() {
        File arquivo = new File(ARQUIVO_USUARIOS);
        if (!arquivo.exists()) {
            // Se for o primeiro acesso, cria um usuário admin padrão
            cadastrarUsuario("admin", "admin");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 2) {
                    Usuario u = new Usuario(dados[0], dados[1]);
                    this.usuarios.add(u);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar usuários: " + e.getMessage());
        }
    }

    private void salvarUsuarios() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_USUARIOS))) {
            for (Usuario u : usuarios) {
                bw.write(u.paraFormatoArquivo());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }
}