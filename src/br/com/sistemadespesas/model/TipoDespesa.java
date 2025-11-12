package src.br.com.sistemadespesas.model;

/**
 * POJO que representa um Tipo de Despesa (Categoria).
 * Ex: Moradia, Transporte, Alimentação.
 * Esta classe é gerenciável pelo usuário.
 * v0.0.3
 */
public class TipoDespesa {

    private int id;
    private String nome;

    public TipoDespesa(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // --- Getters ---
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    
    @Override
    public String toString() {
        return "Tipo [ID=" + id + ", Nome=" + nome + "]";
    }
    
    // --- Lógica de Persistência (CSV) ---
    
    /**
     * Converte o objeto para uma linha CSV.
     * @return String formatada como CSV.
     */
    public String toCsvLine() {
        return id + ";" + nome;
    }

    /**
     * Cria um objeto TipoDespesa a partir de uma linha CSV.
     * @param csvLine Linha do arquivo.
     * @return Objeto TipoDespesa.
     */
    public static TipoDespesa fromCsvLine(String csvLine) {
        try {
            String[] parts = csvLine.split(";");
            if (parts.length != 2) {
                System.err.println("AVISO: Linha de tipo mal formatada: " + csvLine);
                return null;
            }
            int id = Integer.parseInt(parts[0]);
            String nome = parts[1];
            return new TipoDespesa(id, nome);
        } catch (Exception e) {
            System.err.println("ERRO ao parsear linha de tipo: " + csvLine + " - " + e.getMessage());
            return null;
        }
    }
}