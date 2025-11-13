package models;

import java.time.LocalDate;

public class DespesaTransporte extends Despesa {
    // (Construtores idênticos ao DespesaAlimentacao, chamando 'super')
    
    public DespesaTransporte(String descricao, double valor, LocalDate dataVencimento, TipoDespesa tipoDespesa) {
        super(descricao, valor, dataVencimento, tipoDespesa);
    }

    public DespesaTransporte(int id, String descricao, double valor, LocalDate dataVencimento, TipoDespesa tipoDespesa,
                              boolean paga, LocalDate dataPagamento, double valorPago) {
        super(id, descricao, valor, dataVencimento, tipoDespesa, paga, dataPagamento, valorPago);
    }

    @Override
    public String toString() {
        return "[TRANSPORTE] " + super.toString();
    }
}