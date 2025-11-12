import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("=== Sistema de Controle de Despesas ===");
            System.out.println("[1] - Entrar Despesa");
            System.out.println("[2] - Anotar Pagamento");
            System.out.println("[3] - Mostrar Despesas");
            System.out.println("[0] - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); //limpa

            switch (opcao) {
                case 1:
                    System.out.print("Digite o nome da despesa: ");
                    String nomeDespesa = scanner.nextLine();
                    System.out.print("Digite o valor: R$ ");
                    double valor = scanner.nextDouble();
                    System.out.println("Despesa '" + nomeDespesa + "' adicionada com valor de R$" + valor);
                    break;

                case 2:
                    System.out.print("Digite o nome da despesa a pagar: ");
                    String despesaPaga = scanner.nextLine();
                    System.out.println("Pagamento da despesa '" + despesaPaga + "' registrado.");
                    break;

                case 3:
                    System.out.println("Listando todas as despesas (exemplo)...");
                    break;

                case 0:
                    System.out.println("Saindo do sistema...");
                    break;

                default:
                    System.out.println("Opção inválida, tente novamente!");
            }

            System.out.println(); // linha em branco

        } while (opcao != 0);

        scanner.close();
    }
}
