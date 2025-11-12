package src.br.com.sistemadespesas.model;

/**
 * Subclasse concreta (Herança) para despesas de Transporte.
 * v0.0.2
 */
public class DespesaTransporte extends Despesa {

    private String meioTransporte; // Ex: "App", "Ônibus", "Gasolina"

    public DespesaTransporte(String descricao, double valor, String meioTransporte) {
        super(descricao, valor); // Chama construtor (1) da classe mãe
        this.meioTransporte = meioTransporte;
    }

    /**
     * Implementação (Sobrescrita) do método abstrato.
     */
    @Override
    public String getCategoria() {
        return "Transporte";
    }

    // Sobrescrita de toString (Polimorfismo) para adicionar detalhes
    @Override
    public String toString() {
        // Reutiliza o toString() da classe mãe e adiciona informação
        return super.toString() + " | Meio: " + this.meioTransporte;
    }
}