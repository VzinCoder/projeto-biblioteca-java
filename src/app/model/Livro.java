package app.model;

import java.util.Date;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private Date data_publicacao;

    public Livro(String titulo, String autor, Date data_publicacao,int id) {
        this.titulo = titulo;
        this.autor = autor;
        this.data_publicacao = data_publicacao;
        this.id = id;
    }

    public Livro(String titulo, String autor, Date data_publicacao) {
        this.titulo = titulo;
        this.autor = autor;
        this.data_publicacao = data_publicacao;
    }


    public int getId() { 
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public Date getData_publicacao() {
        return data_publicacao;
    }


    @Override
    public String toString() {
        return "Livro [id=" + id + ", titulo=" + titulo + ", autor=" + autor + ", data_publicacao=" + data_publicacao
                + "]";
    }

    
}
