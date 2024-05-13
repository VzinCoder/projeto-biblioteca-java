package app.model;

import java.util.Date;

public class Emprestimo {
    private int id;
    private int idUsuario;
    private int idLivro;
    private Date dataEmprestimo;
    private Date dataDevolucaoPrevista;
    private Date dataDevolucao;
    private StatusEmprestimo status;

    public Emprestimo(){
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public void setDataDevolucaoPrevista(Date dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public void setStatus(StatusEmprestimo status) {
        this.status = status;
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
