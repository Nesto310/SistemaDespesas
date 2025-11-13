package services;

import models.TipoDespesa;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TipoDespesaService {

    private static final String ARQUIVO_TIPOS = "tipos_despesa.txt";
    private List<TipoDespesa> tipos;

    public TipoDespesaService() {
        this.tipos = new ArrayList<>();
        carregarTipos();
    }

    public boolean criarTipo(String nome) {
        if (buscarPorNome(nome).isPresent()) {
            return false; // Nome já existe
        }
        this.tipos.add(new TipoDespesa(nome));
        salvarTipos();
        return true;
    }

    public List<TipoDespesa> listarTipos() {
        return this.tipos;
    }

    public boolean editarTipo(int id, String novoNome) {
        Optional<TipoDespesa> tipoOpt = buscarPorId(id);
        if (tipoOpt.isPresent()) {
            // Verifica se o novo nome já não está em uso por outro ID
            Optional<TipoDespesa> conflitoNome = buscarPorNome(novoNome);
            if (conflitoNome.isPresent() && conflitoNome.get().getId() != id) {
                return false; // Conflito de nome
            }
            tipoOpt.get().setNome(novoNome);
            salvarTipos();
            return true;
        }
        return false; // ID não encontrado
    }

    public boolean excluirTipo(int id) {
        // (Em um sistema real, verificar se este tipo não está em uso por nenhuma despesa)
        Optional<TipoDespesa> tipoOpt = buscarPorId(id);
        if (tipoOpt.isPresent()) {
            this.tipos.remove(tipoOpt.get());
            salvarTipos();
            return true;
        }
        return false;
    }

    public Optional<TipoDespesa> buscarPorId(int id) {
        return tipos.stream().filter(t -> t.getId() == id).findFirst();
    }

    public Optional<TipoDespesa> buscarPorNome(String nome) {
        return tipos.stream().filter(t -> t.getNome().equalsIgnoreCase(nome)).findFirst();
    }

    // --- Persistência em Arquivo ---

    private void carregarTipos() {
        File arquivo = new File(ARQUIVO_TIPOS);
        if (!arquivo.exists()) {
            // Cria categorias padrão
            criarTipo("Alimentação");
            criarTipo("Transporte");
            criarTipo("Moradia");
            criarTipo("Lazer");
            criarTipo("Eventual");
            criarTipo("Supérfluo");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_TIPOS))) {
            String linha;
            int maxId = 0;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 2) {
                    int id = Integer.parseInt(dados[0]);
                    TipoDespesa t = new TipoDespesa(id, dados[1]);
                    this.tipos.add(t);
                    if (id > maxId) maxId = id;
                }
            }
            TipoDespesa.setContadorGlobal(maxId); // Sincroniza o contador estático
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar tipos de despesa: " + e.getMessage());
        }
    }

    private void salvarTipos() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_TIPOS))) {
            for (TipoDespesa t : tipos) {
                bw.write(t.paraFormatoArquivo());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar tipos de despesa: " + e.getMessage());
        }
    }
}