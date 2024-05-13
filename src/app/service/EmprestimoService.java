package app.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import app.customexpections.BuscarEmprestimoException;
import app.customexpections.DevolverLivroException;
import app.customexpections.EmprestarLivroException;
import app.customexpections.RemoveEmprestimoException;
import app.model.Emprestimo;
import app.model.Livro;
import app.model.Usuario;
import app.repository.EmprestimoRepository;

public class EmprestimoService {
    private EmprestimoRepository emprestimoRepository;

    public EmprestimoService() throws ClassNotFoundException, SQLException {
        emprestimoRepository = new EmprestimoRepository();
    }

    public void emprestarLivro(Usuario usuario, Livro livro, LocalDate dataPrevistaDevolucao)
            throws SQLException, EmprestarLivroException, BuscarEmprestimoException {
        emprestimoRepository.emprestarLivro(usuario, livro, dataPrevistaDevolucao);
    }

    public void devolverLivro(Usuario usuario, Livro livro)
            throws SQLException, DevolverLivroException, BuscarEmprestimoException {
        emprestimoRepository.devolverLivro(usuario, livro);
    }

    public Optional<Emprestimo> buscarEmprestimoPendente(Usuario usuario, Livro livro)
            throws SQLException, BuscarEmprestimoException {
        return emprestimoRepository.buscarEmprestimoPendente(usuario, livro);
    }

    public List<Emprestimo> buscarEmprestimoUsuario(Usuario usuario) throws SQLException, BuscarEmprestimoException {
        return emprestimoRepository.buscarEmprestimoUsuario(usuario);
    }

    public List<Emprestimo> buscarEmprestimoLivro(Livro livro) throws SQLException, BuscarEmprestimoException {
        return emprestimoRepository.buscarEmprestimoLivro(livro);
    }

    public void removerEmprestimo(int id) throws SQLException, RemoveEmprestimoException {
        emprestimoRepository.removerEmprestimo(id);
    }

    public void closeConnection() throws SQLException {
        emprestimoRepository.closeConnection();
    }
}
