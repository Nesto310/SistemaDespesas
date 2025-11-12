package src.br.com.sistemadespesas.model;

/**
 * POJO (Plain Old Java Object) que representa o Usuário.
 * v0.0.2
 */
public class Usuario {

    private int id;
    private String nomeUsuario;
    private String hashSenha; // Armazenaremos apenas o "hash" simulado

    public Usuario(int id, String nomeUsuario, String hashSenha) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.hashSenha = hashSenha;
    }

    public int getId() {
        return id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    // A senha real nunca deve ter um "get"
    public boolean verificarSenha(String hashSenha) {
        return this.hashSenha.equals(hashSenha);
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nomeUsuario=" + nomeUsuario + "]";
    }
}