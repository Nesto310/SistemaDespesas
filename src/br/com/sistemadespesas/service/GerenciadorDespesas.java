package src.br.com.sistemadespesas.service;

import src.br.com.sistemadespesas.model.Despesa;
import src.br.com.sistemadespesas.util.SimuladorPersistencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Service (Serviço) responsável pelas regras de negócio
 * relacionadas a Despesas (CRUD, Pagamentos, Listagens).
 * v0.0.2
 */
public class GerenciadorDespesas {

    // Em v0.0.2, os dados vivem em memória (Listas)
    private List<Despesa> bancoDeDespesas;

    public GerenciadorDespesas() {
        this.bancoDeDespesas = new ArrayList<>();
        // Simula o carregamento de dados ao iniciar
        SimuladorPersistencia.carregarDoTxt();
    }

    /**
     * Cria e armazena uma nova despesa.
     * Note que o parâmetro de entrada é a classe Abstrata (Despesa),
     * permitindo que qualquer subclasse (Transporte, Eventual) seja passada.
     * (Isto é Polimorfismo)
     */
    public void criarNovaDespesa(Despesa novaDespesa) {
        this.bancoDeDespesas.add(novaDespesa);
        System.out.println("SUCESSO: Nova despesa cadastrada!");
        
        // Simula a gravação no TXT
        SimuladorPersistencia.salvarEmTxt(novaDespesa);
    }

    public void anotarPagamento(int idDespesa, double valorPago) {
        Despesa despesa = buscarDespesaPorId(idDespesa);

        if (despesa == null) {
            System.out.println("ERRO: Despesa com ID " + idDespesa + " não encontrada.");
            return;
        }

        // Delega a lógica do pagamento para o próprio objeto
        // (Princípio da Responsabilidade Única)
        despesa.realizarPagamento(valorPago);
    }

    /**
     * Lista todas as despesas (pagas e não pagas)
     * para o menu simplificado [3].
     */
    public void listarTodasDespesas() {
        System.out.println("\n--- Listando Todas as Despesas ---");
        if (bancoDeDespesas.isEmpty()) {
            System.out.println("Nenhuma despesa cadastrada ainda.");
            return;
        }
        
        for (Despesa d : bancoDeDespesas) {
            System.out.println(d.toString()); // Usa o toString() polimórfico
        }
        System.out.println("------------------------------------");
        // Acessa o método estático da classe Despesa
        System.out.println("Total de despesas criadas na sessão: " + Despesa.getTotalDespesasCriadas());
    }

    public Despesa buscarDespesaPorId(int id) {
        for (Despesa d : bancoDeDespesas) {
            if (d.getId() == id) {
                return d;
            }
        }
        return null;
    }
}