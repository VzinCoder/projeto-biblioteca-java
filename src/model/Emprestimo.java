package model;

import java.util.Date;

public class Emprestimo {
    private int id;
    private int idUsuario;
    private int idLivro;
    private Date dataEmprestimo;
    private Date dataDevolucaoPrevista;
    private Date dataDevolucao;
    private StatusEmprestimo status;

    public Emprestimo(int id, int idUsuario, int idLivro, Date dataEmprestimo, Date dataDevolucaoPrevista,
            Date dataDevolucao, StatusEmprestimo status) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idLivro = idLivro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucao = dataDevolucao;
        this.status = status;
    }

    public Emprestimo(int idUsuario, int idLivro, Date dataEmprestimo, Date dataDevolucaoPrevista,
            Date dataDevolucao, StatusEmprestimo status) {
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public Date getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Emprestimo [id=" + id + ", idUsuario=" + idUsuario + ", idLivro=" + idLivro + ", dataEmprestimo="
                + dataEmprestimo + ", dataDevolucaoPrevista=" + dataDevolucaoPrevista + ", dataDevolucao="
                + dataDevolucao + ", status=" + status + "]";
    }

    

}
