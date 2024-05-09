package app.model;

public class Usuario {
    private int id;
    private String nome;
    private String cpf;

    public Usuario(int id, String nome, String cpf) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
    }

    public Usuario(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nome=" + nome + ", cpf=" + cpf + "]";
    }

    public boolean validaCpf() {
        if (this.cpf.length() != 11) {
            return false;
        }
    
        for (int i = 0; i < this.cpf.length(); i++) {
            if (!Character.isDigit(this.cpf.charAt(i))) {
                return false;
            }
        }

        return true;
    }
    
    
}
