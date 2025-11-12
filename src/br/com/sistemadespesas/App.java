package src.br.com.sistemadespesas;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import src.br.com.sistemadespesas.model.Despesa;
import src.br.com.sistemadespesas.model.TipoDespesa;
import src.br.com.sistemadespesas.model.Usuario;
import src.br.com.sistemadespesas.service.GerenciadorDespesas;
import src.br.com.sistemadespesas.service.GerenciadorTiposDespesa;
import src.br.com.sistemadespesas.service.GerenciadorUsuarios;

/**
 * Classe principal do Sistema de Controle de Despesas.
 * Versão 0.0.3 (MVP)
 * Esta classe é a camada de Apresentação (View/Controller) do console.
 * Ela delega todas as regras de negócio para os Serviços.
 */
public class App {

    // Serviços (Regras de Negócio)
    private static GerenciadorDespesas gerenciadorDespesas;
    private static GerenciadorUsuarios gerenciadorUsuarios;
    private static GerenciadorTiposDespesa gerenciadorTipos;
    private static CriptografiaService criptoService;

    // Utilitários de UI
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Estado da Aplicação
    private static Usuario usuarioLogado = null;

    public static void main(String[] args) {
        System.out.println("--- Sistema de Controle de Despesas v0.0.3 (MVP) ---");
        
        // 1. Inicializar Serviços (A ordem importa!)
        // A persistência é carregada DENTRO dos construtores dos serviços.
        try {
            criptoService = new CriptografiaService();
            gerenciadorUsuarios = new GerenciadorUsuarios(criptoService);
            gerenciadorTipos = new GerenciadorTiposDespesa();
            gerenciadorDespesas = new GerenciadorDespesas(gerenciadorUsuarios, gerenciadorTipos);
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO AO INICIALIZAR SERVIÇOS: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 2. Loop de Login
        while (usuarioLogado == null) {
            System.out.println("\n--- LOGIN ---");
            System.out.println("[1] - Entrar");
            System.out.println("[2] - Sair do Sistema");
            System.out.print("Escolha: ");
            String op = scanner.nextLine();
            
            if (op.equals("1")) {
                System.out.print("Usuário: ");
                String user = scanner.nextLine();
                System.out.print("Senha: ");
                String pass = scanner.nextLine(); // Em app real, usaria Console.readPassword()
                usuarioLogado = gerenciadorUsuarios.login(user, pass);
            } else if (op.equals("2")) {
                System.out.println("Encerrando...");
                return;
            } else {
                System.out.println("Opção inválida.");
            }
        }

        // 3. Loop Principal
        boolean executando = true;
        while (executando) {
            exibirMenuPrincipal();
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    processarEntrarDespesa();
                    break;
                case "2":
                    processarAnotarPagamento();
                    break;
                case "3":
                    processarListarDespesas(false); // Em Aberto
                    break;
                case "4":
                    processarListarDespesas(true);  // Pagas
                    break;
                case "5":
                    processarGerenciarTiposDespesa();
                    break;
                case "6":
                    processarGerenciarUsuarios();
                    break;
                case "7":
                    System.out.println("\n--- Status do Sistema ---");
                    gerenciadorDespesas.exibirTotalDespesasCriadas();
                    System.out.println("Total de usuários cadastrados: " + gerenciadorUsuarios.listarTodos().size());
                    System.out.println("Total de tipos de despesa: " + gerenciadorTipos.listarTodos().size());
                    break;
                case "8":
                    System.out.println("\nDeslogando e saindo... Até logo, " + usuarioLogado.getNomeUsuario() + "!");
                    executando = false;
                    break;
                default:
                    System.out.println("\nOpção inválida. Por favor, escolha um número entre 1 e 8.");
                    break;
            }

            if (executando) {
                pressioneEnterParaContinuar();
            }
        }

        scanner.close();
        System.out.println("Sistema encerrado.");
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n========================================");
        System.out.println("        SISTEMA DE DESPESAS (MVP)");
        System.out.println("Usuário: " + usuarioLogado.getNomeUsuario());
        System.out.println("========================================");
        System.out.println("[1] - Entrar Despesa");
        System.out.println("[2] - Anotar Pagamento");
        System.out.println("[3] - Listar Despesas em Aberto no período");
        System.out.println("[4] - Listar Despesas Pagas no período");
        System.out.println("[5] - Gerenciar Tipos de Despesa");
        System.out.println("[6] - Gerenciar Usuários (Admin)");
        System.out.println("[7] - Ver Status do Sistema");
        System.out.println("[8] - Sair");
        System.out.println("========================================");
        System.out.print("Escolha uma opção: ");
    }
    
    // --- Processadores de Opção ---

    private static void processarEntrarDespesa() {
        System.out.println("\n--- Nova Despesa ---");
        try {
            System.out.print("Descrição: ");
            String desc = scanner.nextLine();
            
            System.out.print("Valor (ex: 120,50): R$ ");
            double val = Double.parseDouble(scanner.nextLine().replace(",", "."));
            
            LocalDate data = lerData("Data de Vencimento (dd/MM/yyyy) (Enter para hoje):");

            System.out.println("Tipos de Despesa Disponíveis:");
            gerenciadorTipos.listarTodos().forEach(System.out::println);
            System.out.print("Escolha o ID do Tipo: ");
            int idTipo = Integer.parseInt(scanner.nextLine());
            
            TipoDespesa tipo = gerenciadorTipos.buscarPorId(idTipo);
            if (tipo == null) {
                System.out.println("ERRO: ID de Tipo inválido. Operação cancelada.");
                return;
            }
            
            // Demonstração de construtor sobrecarregado
            Despesa novaDespesa;
            if (data.equals(LocalDate.now())) {
                novaDespesa = new Despesa(desc, val, usuarioLogado, tipo);
            } else {
                novaDespesa = new Despesa(desc, val, data, usuarioLogado, tipo);
            }
            
            gerenciadorDespesas.criarNovaDespesa(novaDespesa);

        } catch (NumberFormatException e) {
            System.out.println("ERRO: Valor ou ID inválido. Deve ser um número.");
        } catch (Exception e) {
            System.out.println("ERRO inesperado ao criar despesa: " + e.getMessage());
        }
    }

    private static void processarAnotarPagamento() {
        System.out.println("\n--- Anotar Pagamento ---");
        System.out.println("Despesas em aberto (Mês atual):");
        
        LocalDate inicioMes = YearMonth.now().atDay(1);
        LocalDate fimMes = YearMonth.now().atEndOfMonth();
        List<Despesa> abertas = gerenciadorDespesas.getDespesasEmAberto(inicioMes, fimMes);
        
        if (abertas.isEmpty()) {
            System.out.println("Nenhuma despesa em aberto encontrada para o mês atual.");
            return;
        }
        abertas.forEach(System.out::println);
        
        try {
            System.out.print("\nDigite o ID da despesa a pagar: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            Despesa d = gerenciadorDespesas.buscarDespesaPorId(id);
            if (d == null) {
                System.out.println("ERRO: ID não encontrado.");
                return;
            }
            
            if (d.estaPaga()) {
                System.out.println("AVISO: Esta despesa já está paga.");
                return;
            }
            
            System.out.print("Valor a pagar (Enter para valor total R$ " + d.getValor() + "): ");
            String valorInput = scanner.nextLine();
            double valorPago;
            
            if (valorInput.trim().isEmpty()) {
                valorPago = d.getValor();
            } else {
                valorPago = Double.parseDouble(valorInput.replace(",", "."));
            }
            
            gerenciadorDespesas.anotarPagamento(id, valorPago);

        } catch (NumberFormatException e) {
            System.out.println("ERRO: ID ou Valor inválido.");
        }
    }

    private static void processarListarDespesas(boolean pagas) {
        String titulo = pagas ? "Pagas" : "Em Aberto";
        System.out.println("\n--- Listar Despesas " + titulo + " ---");
        try {
            LocalDate inicio = lerData("Data Inicial (dd/MM/yyyy):");
            LocalDate fim = lerData("Data Final (dd/MM/yyyy):");
            
            if (fim.isBefore(inicio)) {
                System.out.println("ERRO: A data final não pode ser anterior à data inicial.");
                return;
            }
            
            List<Despesa> lista;
            if (pagas) {
                lista = gerenciadorDespesas.getDespesasPagas(inicio, fim);
            } else {
                lista = gerenciadorDespesas.getDespesasEmAberto(inicio, fim);
            }
            
            if (lista.isEmpty()) {
                System.out.println("Nenhuma despesa " + titulo.toLowerCase() + " encontrada no período de " + inicio.format(DATE_FORMATTER) + " a " + fim.format(DATE_FORMATTER));
            } else {
                System.out.println("--- Exibindo " + lista.size() + " Despesas " + titulo + " ---");
                double total = 0;
                for (Despesa d : lista) {
                    System.out.println(d);
                    total += d.getValor();
                }
                System.out.println("-------------------------------------------------");
                System.out.printf("Total no período: R$ %.2f\n", total);
            }
            
        } catch (Exception e) {
            System.out.println("ERRO ao listar despesas: " + e.getMessage());
        }
    }
    
    private static void processarGerenciarTiposDespesa() {
        System.out.println("\n--- Gerenciar Tipos de Despesa ---");
        System.out.println("Tipos atuais:");
        gerenciadorTipos.listarTodos().forEach(System.out::println);
        
        System.out.print("\nDeseja adicionar um novo tipo? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            System.out.print("Nome do novo tipo (ex: Saúde): ");
            String nome = scanner.nextLine();
            if (nome.trim().isEmpty() || nome.contains(";")) {
                System.out.println("ERRO: Nome inválido ou contém ';'.");
                return;
            }
            gerenciadorTipos.criarTipo(nome);
        }
    }
    
    private static void processarGerenciarUsuarios() {
        // Verificação de segurança simples
        if (!usuarioLogado.getNomeUsuario().equals("admin")) {
            System.out.println("ERRO: Apenas o usuário 'admin' pode gerenciar outros usuários.");
            return;
        }
        
        System.out.println("\n--- Gerenciar Usuários ---");
        System.out.println("Usuários atuais:");
        gerenciadorUsuarios.listarTodos().forEach(System.out::println);

        System.out.print("\nDeseja adicionar um novo usuário? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            System.out.print("Nome do novo usuário: ");
            String nome = scanner.nextLine();
            System.out.print("Senha provisória: ");
            String senha = scanner.nextLine();
            
            if (nome.trim().isEmpty() || senha.trim().isEmpty() || nome.contains(";")) {
                 System.out.println("ERRO: Nome ou senha inválidos.");
                 return;
            }
            
            gerenciadorUsuarios.cadastrarUsuario(nome, senha);
        }
    }
    
    // --- Métodos Utilitários de UI ---
    
    private static void pressioneEnterParaContinuar() {
        System.out.println("\n(Pressione Enter para voltar ao Menu...)");
        scanner.nextLine();
    }
    
    private static LocalDate lerData(String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            String input = scanner.nextLine();
            if (input.trim().isEmpty() && prompt.contains("(Enter para hoje)")) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Use dd/MM/yyyy (ex: 25/12/2025).");
            }
        }
    }
}