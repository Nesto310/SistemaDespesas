package src.br.com.sistemadespesas.model;

/**
 * Interface que define a capacidade de um objeto ser "Pagável".
 * v0.0.2
 */
public interface Pagavel {

    /**
     * Tenta realizar o pagamento.
     * @param valorPago O valor que está sendo pago.
     */
    void realizarPagamento(double valorPago);

    /**
     * Verifica o estado do pagamento.
     * @return true se estiver pago, false caso contrário.
     */
    boolean estaPaga();

    /**
     * Retorna o valor total da despesa.
     * @return O valor.
     */
    double getValor();
}