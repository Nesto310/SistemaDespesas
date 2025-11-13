package models;

public class TipoDespesa {
    private static int contadorGlobalId = 0;
    
    private int id;
    private String nome;

    // Construtor para novo
    public TipoDespesa(String nome) {
        this.id = ++contadorGlobalId;
        this.nome = nome;
    }

    // Construtor para carregar
    public TipoDespesa(int id, String nome) {
        this.id = id;
        this.nome = nome;
        if (id > contadorGlobalId) {
            contadorGlobalId = id;
        }
    }
    
    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public static void setContadorGlobal(int id) {
        contadorGlobalId = id;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Nome: %s", id, nome);
    }
    
    public String paraFormatoArquivo() {
        // Formato: ID;NOME
        return id + ";" + nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TipoDespesa that = (TipoDespesa) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}