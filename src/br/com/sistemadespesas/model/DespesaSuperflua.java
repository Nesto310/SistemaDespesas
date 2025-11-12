package src.br.com.sistemadespesas.model;

/**
 * Subclasse concreta (Herança) para despesas Supérfluas.
 * v0.0.2
 */
public class DespesaSuperflua extends Despesa {

    // Usa o construtor sobrecarregado (1) da classe mãe
    public DespesaSuperflua(String descricao, double valor) {
        super(descricao, valor);
    }

    /**
     * Implementação (Sobrescrita) do método abstrato.
     */
    @Override
    public String getCategoria() {
        return "Supérflua";
    }
}