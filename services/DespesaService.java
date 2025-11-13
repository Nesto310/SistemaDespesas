package services;

import models.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DespesaService {

    private static final String ARQUIVO_DESPESAS = "despesas.txt";
    private List<Despesa> despesas;
    private TipoDespesaService tipoDespesaService; // Dependência para validar tipos
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DespesaService(TipoDespesaService tipoDespesaService) {
        this.despesas = new ArrayList<>();
        this.tipoDespesaService = tipoDespesaService;
        carregarDespesas();
    }

    public void criarDespesa(String descricao, double valor, LocalDate dataVencimento, int idTipoDespesa, String tipoClasse) {
        Optional<TipoDespesa> tipoOpt = tipoDespesaService.buscarPorId(idTipoDespesa);
        if (tipoOpt.isEmpty()) {
            System.err.println("Tipo de despesa não encontrado.");
            return;
        }
        TipoDespesa tipo = tipoOpt.get();
        
        Despesa novaDespesa;

        // Polimorfismo na criação
        switch (tipoClasse) {
            case "Alimentação":
                novaDespesa = new DespesaAlimentacao(descricao, valor, dataVencimento, tipo);
                break;
            case "Transporte":
                novaDespesa = new DespesaTransporte(descricao, valor, dataVencimento, tipo);
                break;
            case "Eventual":
            default: // Categoria padrão
                novaDespesa = new DespesaEventual(descricao, valor, dataVencimento, tipo);
                break;
        }

        this.despesas.add(novaDespesa);
        salvarDespesas();
    }

    public boolean pagarDespesa(int id, double valorPago, LocalDate dataPagamento) {
        Optional<Despesa> despesaOpt = buscarPorId(id);
        if (despesaOpt.isPresent() && !despesaOpt.get().estaPaga()) {
            despesaOpt.get().pagar(valorPago, dataPagamento);
            salvarDespesas();
            return true;
        }
        return false; // Não encontrada ou já paga
    }

    public boolean editarDespesa(int id, String novaDesc, double novoValor, LocalDate novaDataVenc, int idNovoTipo) {
        Optional<Despesa> despesaOpt = buscarPorId(id);
        Optional<TipoDespesa> tipoOpt = tipoDespesaService.buscarPorId(idNovoTipo);

        if (despesaOpt.isPresent() && tipoOpt.isPresent()) {
            Despesa d = despesaOpt.get();
            d.setDescricao(novaDesc);
            d.setValor(novoValor);
            d.setDataVencimento(novaDataVenc);
            d.setTipoDespesa(tipoOpt.get());
            salvarDespesas();
            return true;
        }
        return false;
    }
    
    public boolean excluirDespesa(int id) {
        Optional<Despesa> despesaOpt = buscarPorId(id);
        if (despesaOpt.isPresent()) {
            this.despesas.remove(despesaOpt.get());
            salvarDespesas();
            return true;
        }
        return false;
    }

    public Optional<Despesa> buscarPorId(int id) {
        return despesas.stream().filter(d -> d.getId() == id).findFirst();
    }

    // --- Métodos de Listagem ---

    public List<Despesa> listarDespesasEmAberto(LocalDate inicio, LocalDate fim) {
        return despesas.stream()
                .filter(d -> !d.estaPaga())
                .filter(d -> !d.getDataVencimento().isBefore(inicio) && !d.getDataVencimento().isAfter(fim))
                .collect(Collectors.toList());
    }

    public List<Despesa> listarDespesasPagas(LocalDate inicio, LocalDate fim) {
        return despesas.stream()
                .filter(Despesa::estaPaga)
                .filter(d -> !d.getDataVencimento().isBefore(inicio) && !d.getDataVencimento().isAfter(fim))
                .collect(Collectors.toList());
    }
    
    // Sobrecarga (se quisermos listar todas, independente do período)
    public List<Despesa> listarDespesasEmAberto() {
        return despesas.stream().filter(d -> !d.estaPaga()).collect(Collectors.toList());
    }
    
    public List<Despesa> listarDespesasPagas() {
        return despesas.stream().filter(Despesa::estaPaga).collect(Collectors.toList());
    }


    // --- Persistência em Arquivo ---

    private void carregarDespesas() {
        File arquivo = new File(ARQUIVO_DESPESAS);
        if (!arquivo.exists()) {
            return; // Nenhum dado para carregar
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_DESPESAS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Formato: ID;CLASSE;DESCRICAO;VALOR;VENCIMENTO;PAGA;ID_TIPO_DESPESA;DATA_PAGAMENTO;VALOR_PAGO
                String[] dados = linha.split(";");
                if (dados.length != 9) continue;

                try {
                    int id = Integer.parseInt(dados[0]);
                    String classeNome = dados[1];
                    String desc = dados[2];
                    double valor = Double.parseDouble(dados[3]);
                    LocalDate venc = LocalDate.parse(dados[4], FORMATADOR_DATA);
                    boolean paga = Boolean.parseBoolean(dados[5]);
                    int idTipo = Integer.parseInt(dados[6]);
                    LocalDate dataPag = dados[7].equals("null") ? null : LocalDate.parse(dados[7], FORMATADOR_DATA);
                    double valorPago = Double.parseDouble(dados[8]);

                    Optional<TipoDespesa> tipoOpt = tipoDespesaService.buscarPorId(idTipo);
                    if (tipoOpt.isEmpty()) {
                        System.err.println("Aviso: Tipo de despesa ID " + idTipo + " não encontrado. Pulando despesa ID " + id);
                        continue;
                    }
                    TipoDespesa tipo = tipoOpt.get();
                    
                    Despesa d = null;
                    // Polimorfismo na leitura
                    switch (classeNome) {
                        case "DespesaAlimentacao":
                            d = new DespesaAlimentacao(id, desc, valor, venc, tipo, paga, dataPag, valorPago);
                            break;
                        case "DespesaTransporte":
                            d = new DespesaTransporte(id, desc, valor, venc, tipo, paga, dataPag, valorPago);
                            break;
                        case "DespesaEventual":
                            d = new DespesaEventual(id, desc, valor, venc, tipo, paga, dataPag, valorPago);
                            break;
                        default:
                             System.err.println("Aviso: Classe de despesa desconhecida: " + classeNome);
                             continue;
                    }
                    this.despesas.add(d);

                } catch (DateTimeParseException | NumberFormatException e) {
                    System.err.println("Erro ao parsear linha da despesa: " + linha + " | Erro: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar despesas: " + e.getMessage());
        }
    }

    private void salvarDespesas() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_DESPESAS))) {
            for (Despesa d : despesas) {
                bw.write(d.paraFormatoArquivo());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar despesas: " + e.getMessage());
        }
    }
}