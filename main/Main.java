package main;
import services.DespesaService;
import services.TipoDespesaService;
import services.UsuarioService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import models.Despesa;
import models.Usuario;


public class Main {

    // Serviços
    private static final TipoDespesaService tipoDespesaService = new TipoDespesaService();
    private static final DespesaService despesaService = new DespesaService(tipoDespesaService);
    private static final UsuarioService usuarioService = new UsuarioService();

    // Utilitários de Console
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static Usuario usuarioLogado = null;

    public static void main(String[] args) {
        System.out.println("--- BEM-VINDO AO SISTEMA DE CONTROLE DE DESPESAS ---");
        
        while(usuarioLogado == null) {
            autenticarUsuario();
        }
        
        System.out.println("\nLogin bem-sucedido! Bem-vindo(a), " + usuarioLogado.getLogin());
        
        loopMenuPrincipal();
        
        System.out.println("Obrigado por usar o sistema. Até logo!");
        scanner.close();
    }

    private static void autenticarUsuario() {
        System.out.println("\n[ AUTENTICAÇÃO ]");
        System.out.println("1. Login");
        System.out.println("2. Cadastrar novo usuário");
        System.out.print("Escolha uma opção: ");
        String opcao = scanner.nextLine();
        
        if (opcao.equals("1")) {
            System.out.print("Login: ");
            String login = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine(); // Em um app real, usar Console.readPassword()
            
            usuarioLogado = usuarioService.tentarLogin(login, senha);
            if(usuarioLogado == null) {
                System.err.println("Login ou senha inválidos.");
            }
        } else if (opcao.equals("2")) {
            handleGerenciarUsuarios(true); // Chama o cadastro
        } else {
            System.err.println("Opção inválida.");
        }
    }

    private static void loopMenuPrincipal() {
        boolean executando = true;
        while (executando) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Entrar Despesa");
            System.out.println("2. Anotar Pagamento (Conciliar)");
            System.out.println("3. Listar Despesas em Aberto no período");
            System.out.println("4. Listar Despesas Pagas no período");
            System.out.println("5. Gerenciar Tipos de Despesa");
            System.out.println("6. Gerenciar Usuários");
            System.out.println("7. Sair");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1": handleEntrarDespesa(); break;
                case "2": handleAnotarPagamento(); break;
                case "3": handleListarDespesas(false); break; // false = Abertas
                case "4": handleListarDespesas(true); break;  // true = Pagas
                case "5": handleGerenciarTiposDespesa(); break;
                case "6": handleGerenciarUsuarios(false); break; // false = Menu completo
                case "7": executando = false; break;
                default: System.err.println("Opção inválida. Tente novamente.");
            }
        }
    }

    // 1. Entrar Despesa
    private static void handleEntrarDespesa() {
        System.out.println("\n[ Entrar Nova Despesa ]");
        try {
            System.out.print("Descrição: ");
            String desc = scanner.nextLine();
            System.out.print("Valor (ex: 150.75): R$");
            double valor = Double.parseDouble(scanner.nextLine());
            System.out.print("Data Vencimento (dd/MM/yyyy): ");
            LocalDate venc = LocalDate.parse(scanner.nextLine(), FORMATADOR_DATA);

            System.out.println("Tipos de Despesa disponíveis:");
            tipoDespesaService.listarTipos().forEach(System.out::println);
            System.out.print("ID do Tipo de Despesa: ");
            int idTipo = Integer.parseInt(scanner.nextLine());

            String tipoClasse = "Eventual";
            if (tipoDespesaService.buscarPorId(idTipo).get().getNome().equalsIgnoreCase("Alimentação")) {
                tipoClasse = "Alimentação";
            } else if (tipoDespesaService.buscarPorId(idTipo).get().getNome().equalsIgnoreCase("Transporte")) {
                tipoClasse = "Transporte";
            }
            
            despesaService.criarDespesa(desc, valor, venc, idTipo, tipoClasse);
            System.out.println("Despesa registrada com sucesso!");

        } catch (DateTimeParseException e) {
            System.err.println("Formato de data inválido. Use dd/MM/yyyy.");
        } catch (NumberFormatException e) {
            System.err.println("Formato de número (valor ou ID) inválido.");
        } catch (Exception e) {
            System.err.println("Erro ao registrar despesa: " + e.getMessage());
        }
    }

    // 2. Anotar Pagamento
    private static void handleAnotarPagamento() {
        System.out.println("\n[ Anotar Pagamento ]");
        System.out.println("Despesas em aberto:");
        List<Despesa> abertas = despesaService.listarDespesasEmAberto();
        if (abertas.isEmpty()) {
            System.out.println("Nenhuma despesa em aberto.");
            return;
        }
        abertas.forEach(System.out::println);
        
        try {
            System.out.print("ID da Despesa a pagar: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Valor Pago (R$): ");
            double valorPago = Double.parseDouble(scanner.nextLine());
            System.out.print("Data Pagamento (dd/MM/yyyy): ");
            LocalDate dataPag = LocalDate.parse(scanner.nextLine(), FORMATADOR_DATA);
            
            if (despesaService.pagarDespesa(id, valorPago, dataPag)) {
                System.out.println("Pagamento anotado com sucesso!");
            } else {
                System.err.println("Não foi possível anotar o pagamento (ID não encontrado ou já pago).");
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            System.err.println("Formato de data ou número inválido.");
        }
    }
    
    // 3 & 4. Listar Despesas Abertas/Pagas
    private static void handleListarDespesas(boolean pagas) {
        String titulo = pagas ? "[ Despesas Pagas ]" : "[ Despesas em Aberto ]";
        System.out.println("\n" + titulo);
        
        try {
            System.out.print("Mês de referência (MM/yyyy) ou (ENTER para todos): ");
            String mesAnoStr = scanner.nextLine();
            
            LocalDate inicio;
            LocalDate fim;
            List<Despesa> despesas;

            if (mesAnoStr.isBlank()) {
                // Lista todos
                despesas = pagas ? despesaService.listarDespesasPagas() : despesaService.listarDespesasEmAberto();
            } else {
                 YearMonth ym = YearMonth.parse(mesAnoStr, DateTimeFormatter.ofPattern("MM/yyyy"));
                 inicio = ym.atDay(1);
                 fim = ym.atEndOfMonth();
                 despesas = pagas ? 
                    despesaService.listarDespesasPagas(inicio, fim) :
                    despesaService.listarDespesasEmAberto(inicio, fim);
            }
            
            if (despesas.isEmpty()) {
                System.out.println("Nenhuma despesa encontrada.");
                return;
            }
            
            despesas.forEach(System.out::println);
            // Chama o Submenu
            handleSubmenuDespesas();

        } catch (DateTimeParseException e) {
            System.err.println("Formato de data inválido. Use MM/yyyy.");
        }
    }

    // Submenu de Listagem
    private static void handleSubmenuDespesas() {
        System.out.println("\n--- Submenu Despesas ---");
        System.out.println("1. Editar Despesa");
        System.out.println("2. Excluir Despesa");
        System.out.println("3. Voltar ao Menu Principal");
        System.out.print("Opção: ");
        
        String opcao = scanner.nextLine();
        switch (opcao) {
            case "1": // Editar
                try {
                    System.out.print("ID da Despesa para EDITAR: ");
                    int idEdit = Integer.parseInt(scanner.nextLine());
                    
                    
                    System.out.print("Nova Descrição: ");
                    String nDesc = scanner.nextLine();
                    System.out.print("Novo Valor (R$): ");
                    double nVal = Double.parseDouble(scanner.nextLine());
                    System.out.print("Nova Data Venc (dd/MM/yyyy): ");
                    LocalDate nVenc = LocalDate.parse(scanner.nextLine(), FORMATADOR_DATA);
                    
                    System.out.println("Tipos disponíveis:");
                    tipoDespesaService.listarTipos().forEach(System.out::println);
                    System.out.print("ID do Novo Tipo: ");
                    int nIdTipo = Integer.parseInt(scanner.nextLine());
                    
                    if(despesaService.editarDespesa(idEdit, nDesc, nVal, nVenc, nIdTipo)) {
                        System.out.println("Despesa editada com sucesso.");
                    } else {
                        System.err.println("Erro ao editar (ID da despesa or ID do tipo não encontrado).");
                    }
                } catch (Exception e) {
                    System.err.println("Dados inválidos. Abortando edição.");
                }
                break;
            case "2": // Excluir
                 try {
                    System.out.print("ID da Despesa para EXCLUIR: ");
                    int idDel = Integer.parseInt(scanner.nextLine());
                    System.out.print("Tem certeza que deseja excluir a Despesa " + idDel + "? (S/N): ");
                    if(scanner.nextLine().equalsIgnoreCase("S")) {
                        if (despesaService.excluirDespesa(idDel)) {
                            System.out.println("Despesa excluída.");
                        } else {
                            System.err.println("ID não encontrado.");
                        }
                    } else {
                        System.out.println("Exclusão cancelada.");
                    }
                 } catch (NumberFormatException e) {
                     System.err.println("ID inválido.");
                 }
                break;
            case "3": // Voltar
            default:
                break;
        }
    }

    // 5. Gerenciar Tipos de Despesa
    private static void handleGerenciarTiposDespesa() {
        System.out.println("\n[ Gerenciar Tipos de Despesa ]");
        System.out.println("1. Listar Tipos");
        System.out.println("2. Criar Novo Tipo");
        System.out.println("3. Editar Tipo");
        System.out.println("4. Excluir Tipo");
        System.out.println("5. Voltar");
        System.out.print("Opção: ");
        
        switch (scanner.nextLine()) {
            case "1":
                System.out.println("Tipos cadastrados:");
                tipoDespesaService.listarTipos().forEach(System.out::println);
                break;
            case "2":
                System.out.print("Nome do novo tipo: ");
                String nome = scanner.nextLine();
                if(tipoDespesaService.criarTipo(nome)) {
                    System.out.println("Tipo criado!");
                } else {
                    System.err.println("Erro: Esse nome já existe.");
                }
                break;
            case "3":
                System.out.print("ID do tipo a editar: ");
                try {
                    int idEdit = Integer.parseInt(scanner.nextLine());
                    System.out.print("Novo nome: ");
                    String nNome = scanner.nextLine();
                    if(tipoDespesaService.editarTipo(idEdit, nNome)) {
                        System.out.println("Tipo atualizado.");
                    } else {
                        System.err.println("Erro: ID não encontrado ou nome já em uso.");
                    }
                } catch (NumberFormatException e) {
                     System.err.println("ID inválido.");
                }
                break;
            case "4":
                 System.out.print("ID do tipo a excluir: ");
                 try {
                    int idDel = Integer.parseInt(scanner.nextLine());
                    // (Aviso: Não estamos verificando se o tipo está em uso)
                    if(tipoDespesaService.excluirTipo(idDel)) {
                        System.out.println("Tipo excluído.");
                    } else {
                        System.err.println("ID não encontrado.");
                    }
                 } catch (NumberFormatException e) {
                     System.err.println("ID inválido.");
                 }
                break;
            case "5":
            default:
                break;
        }
    }
    
    // 6. Gerenciar Usuários
    private static void handleGerenciarUsuarios(boolean apenasCadastro) {
        if (apenasCadastro) {
            System.out.println("\n[ Cadastro de Novo Usuário ]");
            System.out.print("Login desejado: ");
            String login = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();
            if(usuarioService.cadastrarUsuario(login, senha)) {
                System.out.println("Usuário cadastrado com sucesso! Por favor, faça o login.");
            } else {
                System.err.println("Erro: Login '" + login + "' já existe.");
            }
            return;
        }

        System.out.println("\n[ Gerenciar Usuários ]");
        System.out.println("1. Listar Usuários");
        System.out.println("2. Cadastrar Novo Usuário");
        System.out.println("3. Editar Senha de Usuário");
        System.out.println("4. Voltar");
        System.out.print("Opção: ");
        
        switch (scanner.nextLine()) {
            case "1":
                System.out.println("Usuários cadastrados:");
                usuarioService.listarUsuarios().forEach(System.out::println);
                break;
            case "2":
                handleGerenciarUsuarios(true); // Reusa a lógica de cadastro
                break;
            case "3":
                System.out.print("Login do usuário a editar: ");
                String loginEdit = scanner.nextLine();
                System.out.print("Nova Senha: ");
                String nSenha = scanner.nextLine();
                if(usuarioService.editarUsuario(loginEdit, nSenha)) {
                    System.out.println("Senha atualizada.");
                } else {
                    System.err.println("Usuário não encontrado.");
                }
                break;
            case "4":
            default:
                break;
        }
    }
}