package src.br.com.sistemadespesas.util;

import src.br.com.sistemadespesas.model.Despesa;

/**
 * Classe utilitária para simular a persistência (gravação/leitura)
 * de dados em arquivos de texto.
 * v0.0.2
 */
public class SimuladorPersistencia {

    private static final String NOME_ARQUIVO = "despesas_db.txt";

    /**
     * Simula a gravação de uma despesa em um arquivo .txt
     * @param despesa A despesa a ser "salva".
     */
    public static void salvarEmTxt(Despesa despesa) {
        String linha = despesa.toString(); // Usamos o toString polimórfico
        
        System.out.println("\n[Simulação de Persistência TXT]");
        System.out.println("-> Gravando no arquivo '" + NOME_ARQUIVO + "':");
        System.out.println("-> DADO: " + linha);
        System.out.println("[Simulação Concluída]");
    }

    /**
     * Simula o carregamento de dados do arquivo.
     */
    public static void carregarDoTxt() {
        System.out.println("\n[Simulação de Persistência TXT]");
        System.out.println("-> Lendo do arquivo '" + NOME_ARQUIVO + "'...");
        System.out.println("-> [Dados Carregados (Simulação)]");
    }
}