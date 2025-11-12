package src.br.com.sistemadespesas.util;

/**
 * Classe utilitária para simular operações de criptografia.
 * Na v0.0.2, apenas simula um hash.
 */
public class Criptografia {

    /**
     * Simula a geração de um hash para uma senha.
     * Em um sistema real, usaria BCrypt ou Argon2.
     * @param senha A senha em texto plano.
     * @return Um "hash" simulado.
     */
    public static String simularHash(String senha) {
        // Apenas reverte a string e adiciona um prefixo "hash_"
        String hashSimulado = "hash_" + new StringBuilder(senha).reverse().toString();
        System.out.println("[Simulação Cripto] Senha '" + senha + "' gerou o hash: " + hashSimulado);
        return hashSimulado;
    }
}