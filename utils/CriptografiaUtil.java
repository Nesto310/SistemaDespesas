package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Utiliza o algoritmo SHA-256.
 */
public class CriptografiaUtil {

    /**
     * @param senha A senha a ser criptografada.
     * @return O hash SHA-256 da senha em formato hexadecimal.
     */
    public static String criptografar(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            return bytesParaHex(hash);
        } catch (NoSuchAlgorithmException e) {
            // Em um ambiente real, SHA-256 sempre existirá.
            throw new RuntimeException("Erro de criptografia: Algoritmo SHA-256 não encontrado.", e);
        }
    }

    /**
     * Verifica se uma senha em plain text corresponde a um hash armazenado.
     * @param senhaDigitada A senha em plain text.
     * @param hashArmazenado O hash SHA-256 armazenado.
     * @return true se a senha corresponder ao hash, false caso contrário.
     */
    public static boolean verificarSenha(String senhaDigitada, String hashArmazenado) {
        String hashDaSenhaDigitada = criptografar(senhaDigitada);
        return hashDaSenhaDigitada.equals(hashArmazenado);
    }

    /**
     * Converte um array de bytes em sua representação hexadecimal.
     */
    private static String bytesParaHex(byte[] bytes) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }
}