package app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import app.customexpections.BuscarEmprestimoException;
import app.customexpections.DevolverLivroException;
import app.customexpections.EmprestarLivroException;
import app.customexpections.RemoveEmprestimoException;
import app.db.ConnectionPostgres;
import app.model.Emprestimo;
import app.model.Livro;
import app.model.StatusEmprestimo;
import app.model.Usuario;

public class EmprestimoRepository {

    private Connection conn;
    private LivroRepository livroRepository;
    private UsuarioRepository usuarioRepository;

    public EmprestimoRepository() throws ClassNotFoundException, SQLException {
        conn = ConnectionPostgres.createConnection();
        livroRepository = new LivroRepository();
        usuarioRepository = new UsuarioRepository();
    }

    public int emprestarLivro(Usuario usuario, Livro livro, Date dataPrevistaDevolucao)
            throws SQLException, EmprestarLivroException, BuscarEmprestimoException {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.buscaUsuario(usuario.getCpf());
        Optional<Livro> livroEncontrado = livroRepository.buscarLivroId(livro.getId());
        Date dataAtual = new Date();

        Optional<Emprestimo> emprestimoEncontrado = buscarEmprestimoPendente(usuario, livro);

        if (emprestimoEncontrado.isPresent()) {
            throw new EmprestarLivroException("Usuario já pegou o livro solicitado!");
        }

        if (dataPrevistaDevolucao.getTime() < dataAtual.getTime()) {
            throw new EmprestarLivroException("Data prevista Invalida!");
        }

        if (usuarioEncontrado.isEmpty()) {
            throw new EmprestarLivroException("Usuario invalido!");
        }

        if (livroEncontrado.isEmpty()) {
            throw new EmprestarLivroException("Livro invalido!");
        }

        String sql = "Insert into emprestimos(id_usuario,id_livro,data_emprestimo,data_devolucao_prevista) values(?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pst.setInt(1, usuario.getId());
        pst.setInt(2, livro.getId());
        pst.setDate(3, new java.sql.Date(dataAtual.getTime())); // data atual
        pst.setDate(4, new java.sql.Date(dataPrevistaDevolucao.getTime())); // data prevista
        pst.executeUpdate();
        ResultSet resultSet = pst.getGeneratedKeys();
        resultSet.next();
        return resultSet.getInt("id");
    }

    public void devolverLivro(Usuario usuario, Livro livro)
            throws SQLException, DevolverLivroException, BuscarEmprestimoException {
        Optional<Emprestimo> emprestimoEncontrado = buscarEmprestimoPendente(usuario, livro);
        Date dataAtual = new Date();

        if (emprestimoEncontrado.isEmpty()) {
            throw new DevolverLivroException("Emprestimo nao encontrado!");
        }

        String sql = "Update emprestimos set data_devolucao=?,status_emprestimo=? where id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setDate(1, new java.sql.Date(dataAtual.getTime()));
        pst.setString(2, StatusEmprestimo.Concluído.name());
        pst.setInt(3, emprestimoEncontrado.get().getId());
        pst.executeUpdate();

    }

    public Optional<Emprestimo> buscarEmprestimoPendente(Usuario usuario, Livro livro)
            throws SQLException, BuscarEmprestimoException {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.buscaUsuario(usuario.getCpf());
        Optional<Livro> livroEncontrado = livroRepository.buscarLivroId(livro.getId());

        if (usuarioEncontrado.isEmpty()) {
            throw new BuscarEmprestimoException("Usuario invalido!");
        }

        if (livroEncontrado.isEmpty()) {
            throw new BuscarEmprestimoException("Livro invalido!");
        }

        String sql = "SELECT * FROM emprestimos WHERE id_usuario=? AND id_livro=? AND status_emprestimo=?";
        Optional<Emprestimo> emprestimoEncontrado = Optional.empty();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, usuario.getId());
        pst.setInt(2, livro.getId());
        pst.setString(3,  "Pendente");
        ResultSet resultSet = pst.executeQuery();

        if (resultSet.next()) {
            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setId(resultSet.getInt("id"));
            emprestimo.setIdLivro(resultSet.getInt("id_livro"));
            emprestimo.setIdUsuario(resultSet.getInt("id_usuario"));
            emprestimo.setDataEmprestimo(resultSet.getDate("data_emprestimo"));
            emprestimo.setDataDevolucaoPrevista(resultSet.getDate("data_devolucao_prevista"));
            emprestimo.setDataDevolucao(resultSet.getDate("data_devolucao"));
            emprestimo.setStatus(StatusEmprestimo.valueOf(resultSet.getString("status_emprestimo")));
            emprestimoEncontrado = Optional.of(emprestimo);
        }

        return emprestimoEncontrado;
    }

    public List<Emprestimo> buscarEmprestimoUsuario(Usuario usuario)
            throws SQLException, BuscarEmprestimoException {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.buscaUsuario(usuario.getCpf());

        if (usuarioEncontrado.isEmpty()) {
            throw new BuscarEmprestimoException("Usuario invalido!");
        }

        String sql = "SELECT * FROM emprestimos WHERE id_usuario=?";
        List<Emprestimo> emprestimoEncontrados = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, usuario.getId());
        ResultSet resultSet = pst.executeQuery();

        while (resultSet.next()) {
            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setId(resultSet.getInt("id"));
            emprestimo.setIdLivro(resultSet.getInt("id_livro"));
            emprestimo.setIdUsuario(resultSet.getInt("id_usuario"));
            emprestimo.setDataEmprestimo(resultSet.getDate("data_emprestimo"));
            emprestimo.setDataDevolucaoPrevista(resultSet.getDate("data_devolucao_prevista"));
            emprestimo.setDataDevolucao(resultSet.getDate("data_devolucao"));
            emprestimo.setStatus(StatusEmprestimo.valueOf(resultSet.getString("status_emprestimo")) );
            emprestimoEncontrados.add(emprestimo);
        }

        return emprestimoEncontrados;
    }

    public List<Emprestimo> buscarEmprestimoLivro(Livro livro)
            throws SQLException, BuscarEmprestimoException {
        Optional<Livro> livroEncontrado = livroRepository.buscarLivroId(livro.getId());

        if (livroEncontrado.isEmpty()) {
            throw new BuscarEmprestimoException("Livro invalido!");
        }

        String sql = "SELECT * FROM emprestimos WHERE id_livro=?";
        List<Emprestimo> emprestimoEncontrados = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, livro.getId());
        ResultSet resultSet = pst.executeQuery();

        while (resultSet.next()) {
            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setId(resultSet.getInt("id"));
            emprestimo.setIdLivro(resultSet.getInt("id_livro"));
            emprestimo.setIdUsuario(resultSet.getInt("id_usuario"));
            emprestimo.setDataEmprestimo(resultSet.getDate("data_emprestimo"));
            emprestimo.setDataDevolucaoPrevista(resultSet.getDate("data_devolucao_prevista"));
            emprestimo.setDataDevolucao(resultSet.getDate("data_devolucao"));
            emprestimo.setStatus(StatusEmprestimo.valueOf(resultSet.getString("status_emprestimo")) );
            emprestimoEncontrados.add(emprestimo);
        }

        return emprestimoEncontrados;
    }

    public void removerEmprestimo(int id) throws SQLException, RemoveEmprestimoException {

        if (id < 1) {
            throw new RemoveEmprestimoException("id Invalido!");
        }

        String sql = "Delete from emprestimos WHERE id =?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        int result = pst.executeUpdate();
        if (result == 0) {
            throw new RemoveEmprestimoException("Emprestimo nao encontrado!");
        }
    }

    public void closeConnection() throws SQLException {
        ConnectionPostgres.closeConnection(conn);
    }

}
