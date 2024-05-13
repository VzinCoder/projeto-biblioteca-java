package app.model;

import java.time.LocalDate;

public class Emprestimo {
    private int id;
    private int idUsuario;
    private int idLivro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucao;
    private StatusEmprestimo status;

    public Emprestimo(){
    }

    public Emprestimo(int id, int idUsuario, int idLivro, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista,
            LocalDate dataDevolucao, StatusEmprestimo status) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idLivro = idLivro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucao = dataDevolucao;
        this.status = status;
    }

    public Emprestimo(int idUsuario, int idLivro, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista,
            LocalDate dataDevolucao, StatusEmprestimo status) {
        this.idUsuario = idUsuario;
        this.idLivro = idLivro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucao = dataDevolucao;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public void setStatus(StatusEmprestimo status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Emprestimo [id=" + id + ", idUsuario=" + idUsuario + ", idLivro=" + idLivro + ", dataEmprestimo="
                + dataEmprestimo + ", dataDevolucaoPrevista=" + dataDevolucaoPrevista + ", dataDevolucao="
                + dataDevolucao + ", status=" + status + "]";
    }

    

}
