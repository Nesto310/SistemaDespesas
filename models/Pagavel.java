package models;

import java.time.LocalDate;

/**
 * Interface que define o contrato para entidades que podem ser pagas.
 */
public interface Pagavel {
    
    /**
     * Marca a entidade como paga.
     * @param valorPago O valor efetivamente pago.
     * @param dataPagamento A data em que o pagamento foi realizado.
     */
    void pagar(double valorPago, LocalDate dataPagamento);

    /**
     * Verifica se a entidade já foi paga.
     * @return true se estiver paga, false caso contrário.
     */
    boolean estaPaga();
}