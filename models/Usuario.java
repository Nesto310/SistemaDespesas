package models;

public class Usuario {
    private String login;
    private String senhaCriptografada;

    public Usuario(String login, String senhaCriptografada) {
        this.login = login;
        this.senhaCriptografada = senhaCriptografada;
    }

    public String getLogin() { return login; }
    public String getSenhaCriptografada() { return senhaCriptografada; }
    
    public void setSenhaCriptografada(String senhaCriptografada) {
        this.senhaCriptografada = senhaCriptografada;
    }
    
    public String paraFormatoArquivo() {
        // Formato: LOGIN;SENHA_HASH
        return login + ";" + senhaCriptografada;
    }
    
    @Override
    public String toString() {
        return "Login: " + login;
    }
}