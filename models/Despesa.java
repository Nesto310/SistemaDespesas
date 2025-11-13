package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Despesa implements Pagavel {

    // Atributo estático para contagem global
    private static int contadorGlobalId = 0;
    protected static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    protected int id;
    protected String descricao;
    protected double valor;
    protected LocalDate dataVencimento;
    protected TipoDespesa tipoDespesa; // Link para o tipo
    protected boolean paga;
    protected LocalDate dataPagamento;
    protected double valorPago;

    public Despesa(String descricao, double valor, LocalDate dataVencimento, TipoDespesa tipoDespesa) {
        this.id = ++contadorGlobalId; // Incrementa o contador global
        this.descricao = descricao;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.tipoDespesa = tipoDespesa;
        this.paga = false;
    }


    public Despesa(int id, String descricao, double valor, LocalDate dataVencimento, TipoDespesa tipoDespesa,
                   boolean paga, LocalDate dataPagamento, double valorPago) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.tipoDespesa = tipoDespesa;
        this.paga = paga;
        this.dataPagamento = dataPagamento;
        this.valorPago = valorPago;
        
        // Garante que o contador global esteja sempre à frente do maior ID carregado
        if (id > contadorGlobalId) {
            contadorGlobalId = id;
        }
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public TipoDespesa getTipoDespesa() { return tipoDespesa; }
    public static int getContadorGlobalId() { return contadorGlobalId; }
    
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setValor(double valor) { this.valor = valor; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public void setTipoDespesa(TipoDespesa tipoDespesa) { this.tipoDespesa = tipoDespesa; }

    // Métodos da interface Pagavel
    @Override
    public void pagar(double valorPago, LocalDate dataPagamento) {
        this.paga = true;
        this.valorPago = valorPago;
        this.dataPagamento = dataPagamento;
    }

    @Override
    public boolean estaPaga() {
        return paga;
    }


    @Override
    public String toString() {
        String status = paga ? "PAGA" : "ABERTA";
        String strPagamento = paga ? 
            String.format(" (Pago em: %s, Valor: R$%.2f)", dataPagamento.format(FORMATADOR_DATA), valorPago) : "";
            
        return String.format("ID: %d | Status: %s | Venc: %s | Valor: R$%.2f | Cat: %s | Desc: %s%s",
                id,
                status,
                dataVencimento.format(FORMATADOR_DATA),
                valor,
                tipoDespesa.getNome(),
                descricao,
                strPagamento);
    }
    

    public String paraFormatoArquivo() {
        return String.join(";",
                String.valueOf(id),
                this.getClass().getSimpleName(), // Polimorfismo!
                descricao,
                String.valueOf(valor),
                dataVencimento.format(FORMATADOR_DATA),
                String.valueOf(paga),
                String.valueOf(tipoDespesa.getId()), // Salva o ID da categoria
                dataPagamento != null ? dataPagamento.format(FORMATADOR_DATA) : "null",
                String.valueOf(valorPago)
        );
    }
}