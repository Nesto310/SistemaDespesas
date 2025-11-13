package models;

import java.time.LocalDate;

public class DespesaAlimentacao extends Despesa {

    // Construtor para nova despesa
    public DespesaAlimentacao(String descricao, double valor, LocalDate dataVencimento, TipoDespesa tipoDespesa) {
        super(descricao, valor, dataVencimento, tipoDespesa);
    }

    // Construtor para carregar do arquivo
    public DespesaAlimentacao(int id, String descricao, double valor, LocalDate dataVencimento, TipoDespesa tipoDespesa,
                              boolean paga, LocalDate dataPagamento, double valorPago) {
        super(id, descricao, valor, dataVencimento, tipoDespesa, paga, dataPagamento, valorPago);
    }

    @Override
    public String toString() {
        return "[ALIMENTAÇÃO] " + super.toString();
    }
}