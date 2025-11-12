package src.br.com.sistemadespesas.model;

/**
 * Subclasse concreta (Herança) para despesas Eventuais.
 * v0.0.2
 */
public class DespesaEventual extends Despesa {

    private String motivo;

    public DespesaEventual(String descricao, double valor, String motivo) {
        super(descricao, valor); // Chama construtor (1) da classe mãe
        this.motivo = motivo;
    }

    /**
     * Implementação (Sobrescrita) do método abstrato.
     */
    @Override
    public String getCategoria() {
        return "Eventual";
    }

    // Sobrescrita de toString (Polimorfismo) para adicionar detalhes
    @Override
    public String toString() {
        return super.toString() + " | Motivo: " + this.motivo;
    }
}