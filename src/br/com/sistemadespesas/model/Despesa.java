package src.br.com.sistemadespesas.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe abstrata que representa uma Despesa genérica.
 * Implementa a interface Pagavel.
 * v0.0.2
 */
public abstract class Despesa implements Pagavel {

    // Contador estático para simular IDs únicos (auto-incremento)
    private static int contadorDespesas = 0;

    protected int id;
    protected String descricao;
    protected double valor;
    protected LocalDate dataVencimento;
    protected boolean paga;
    protected Usuario responsavel; // Demonstração de associação de classes

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Construtor sobrecarregado (1): Despesa simples.
     * Assume data de vencimento como hoje e sem responsável.
     */
    public Despesa(String descricao, double valor) {
        this(descricao, valor, LocalDate.now(), null); // Chama o construtor completo
    }

    /**
     * Construtor sobrecarregado (2): Despesa completa.
     */
    public Despesa(String descricao, double valor, LocalDate dataVencimento, Usuario responsavel) {
        this.id = ++contadorDespesas; // Incrementa o contador estático
        this.descricao = descricao;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.paga = false;
        this.responsavel = responsavel;
    }

    // --- Métodos da Interface Pagavel ---

    @Override
    public void realizarPagamento(double valorPago) {
        if (this.paga) {
            System.out.println("  AVISO: Despesa ID " + this.id + " ('" + this.descricao + "') já estava paga.");
            return;
        }
        
        if (valorPago >= this.valor) {
            this.paga = true;
            System.out.println("  SUCESSO: Pagamento da despesa '" + this.descricao + "' realizado.");
        } else {
            System.out.println("  FALHA: Valor " + valorPago + " é insuficiente para pagar a despesa '" + this.descricao + "' (Valor: " + this.valor + ").");
        }
    }

    @Override
    public boolean estaPaga() {
        return this.paga;
    }

    @Override
    public double getValor() {
        return this.valor;
    }

    // --- Método Abstrato (deve ser implementado pelas subclasses) ---

    /**
     * Retorna a categoria específica da despesa.
     * @return String representando a Categoria.
     */
    public abstract String getCategoria();

    // --- Getters ---

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }
    
    /**
     * Método estático para acessar o contador.
     * @return Total de despesas criadas nesta sessão.
     */
    public static int getTotalDespesasCriadas() {
        return contadorDespesas;
    }

    // --- Sobrescrita do método toString (Polimorfismo) ---

    @Override
    public String toString() {
        // [ID: 1] Almoço (Alimentação) | R$ 25.50 | Venc: 12/11/2025 | Paga: Não
        return String.format("[ID: %d] %s (%s) | R$ %.2f | Venc: %s | Paga: %s",
                this.id,
                this.descricao,
                this.getCategoria(), // Chama o método abstrato (que será o da subclasse)
                this.valor,
                this.dataVencimento.format(DATE_FORMATTER),
                (this.paga ? "Sim" : "Não")
        );
    }
}