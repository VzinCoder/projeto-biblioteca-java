package app.model;

import java.time.LocalDate;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private LocalDate data_publicacao;



    public Livro(String titulo, String autor, LocalDate data_publicacao, int id) {
        this.titulo = titulo;
        this.autor = autor;
        this.data_publicacao = data_publicacao;
        this.id = id;
    }

    public Livro(String titulo, String autor, LocalDate data_publicacao) {
        this.titulo = titulo;
        this.autor = autor;
        this.data_publicacao = data_publicacao;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public LocalDate getData_publicacao() {
        return data_publicacao;
    }

    public void setData_publicacao(LocalDate data_publicacao) {
        this.data_publicacao = data_publicacao;
    }

    @Override
    public String toString() {
        return "Livro [id=" + id + ", titulo=" + titulo + ", autor=" + autor + ", data_publicacao=" + data_publicacao
                + "]";
    }

    public boolean validaData() {
        LocalDate dataAtual = LocalDate.now();
        if (data_publicacao.isAfter(dataAtual)) {
            return false;
        }
        return true;
    }

}
