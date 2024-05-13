package app.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import app.customexpections.AdicionarLivroException;
import app.customexpections.AtualizarLivroException;
import app.customexpections.RemoveLivroException;
import app.db.ConnectionPostgres;
import app.model.Livro;

public class LivroRepository {

    private Connection conn;

    public LivroRepository() throws ClassNotFoundException, SQLException {
        this.conn = ConnectionPostgres.createConnection();
    }

    public int adicionarLivro(Livro livro) throws SQLException, AdicionarLivroException {

        if (!livro.validaData()) {
            throw new AdicionarLivroException("Data invalida!");
        }

        String sql = "Insert into livros(titulo,autor,data_publicacao) values(?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pst.setString(1, livro.getTitulo());
        pst.setString(2, livro.getAutor());
        pst.setDate(3, Date.valueOf(livro.getData_publicacao()));
        pst.executeUpdate();
        ResultSet resultSet = pst.getGeneratedKeys();
        resultSet.next();
        return resultSet.getInt("id");
    }

    public void removerLivro(int id) throws SQLException, RemoveLivroException {
        String sql = "Delete from livros where id=?";

        if (id < 1) {
            throw new RemoveLivroException("id Invalido!");
        }

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        int result = pst.executeUpdate();
        if (result == 0) {
            throw new RemoveLivroException("Livro nao encontrado!");
        }
    }

    public void atualizarLivro(Livro livro) throws SQLException, AtualizarLivroException {
        Optional<Livro> livroEncontrado = buscarLivroId(livro.getId());

        if (livroEncontrado.isEmpty()) {
            throw new AtualizarLivroException("Livro nao existe!");
        }

        if (!livro.validaData()) {
            throw new AtualizarLivroException("Data invalida!");
        }

        String sql = "Update livros set titulo=?,autor=?,data_publicacao=? where id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, livro.getTitulo());
        pst.setString(2, livro.getAutor());
        pst.setDate(3, Date.valueOf(livro.getData_publicacao()));
        pst.setInt(4, livro.getId());
        pst.executeUpdate();
    }

    public List<Livro> buscarLivrosTitulo(String titulo) throws SQLException {
        List<Livro> livrosEncontrados = new ArrayList<>();
        String sql = "SELECT * FROM livros WHERE LOWER(titulo) LIKE LOWER(?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, "%" + titulo + "%");
        ResultSet resultSet = pst.executeQuery();

        while (resultSet.next()) {
            int idLivro = resultSet.getInt("id");
            String tituloLivro = resultSet.getString("titulo");
            String autorLivro = resultSet.getString("autor");
            LocalDate dataPublicacaoLivro = resultSet.getDate("data_publicacao").toLocalDate();
            Livro livro = new Livro(tituloLivro, autorLivro, dataPublicacaoLivro, idLivro);
            livrosEncontrados.add(livro);
        }
        return livrosEncontrados;

    }

    public List<Livro> buscarLivrosAutor(String autor) throws SQLException {
        List<Livro> livrosEncontrados = new ArrayList<>();
        String sql = "SELECT * FROM livros WHERE LOWER(autor) LIKE LOWER(?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, "%" + autor + "%");
        ResultSet resultSet = pst.executeQuery();

        while (resultSet.next()) {
            int idLivro = resultSet.getInt("id");
            String tituloLivro = resultSet.getString("titulo");
            String autorLivro = resultSet.getString("autor");
            LocalDate dataPublicacaoLivro = resultSet.getDate("data_publicacao").toLocalDate();
            Livro livro = new Livro(tituloLivro, autorLivro, dataPublicacaoLivro, idLivro);
            livrosEncontrados.add(livro);
        }
        return livrosEncontrados;

    }

    public List<Livro> buscarLivros() throws SQLException {
        List<Livro> livrosEncontrados = new ArrayList<>();
        String sql = "SELECT * FROM livros LIMIT 100";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet resultSet = pst.executeQuery();

        while (resultSet.next()) {
            int idLivro = resultSet.getInt("id");
            String tituloLivro = resultSet.getString("titulo");
            String autorLivro = resultSet.getString("autor");
            LocalDate dataPublicacaoLivro = resultSet.getDate("data_publicacao").toLocalDate();
            Livro livro = new Livro(tituloLivro, autorLivro, dataPublicacaoLivro, idLivro);
            livrosEncontrados.add(livro);
        }
        return livrosEncontrados;

    }

    public List<Livro> buscarLivrosTituloAutor(String titulo, String autor) throws SQLException {
        List<Livro> livrosEncontrados = new ArrayList<>();
        String sql = "SELECT * FROM livros WHERE LOWER(titulo) LIKE LOWER(?) and LOWER(autor) LIKE LOWER(?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, "%" + titulo + "%");
        pst.setString(2, "%" + autor + "%");
        ResultSet resultSet = pst.executeQuery();

        while (resultSet.next()) {
            int idLivro = resultSet.getInt("id");
            String tituloLivro = resultSet.getString("titulo");
            String autorLivro = resultSet.getString("autor");
            LocalDate dataPublicacaoLivro = resultSet.getDate("data_publicacao").toLocalDate();
            Livro livro = new Livro(tituloLivro, autorLivro, dataPublicacaoLivro, idLivro);
            livrosEncontrados.add(livro);
        }
        return livrosEncontrados;
    }

    public Optional<Livro> buscarLivroId(int id) throws SQLException {
        String sql = "Select * from livros where id =?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet resultSet = pst.executeQuery();
        Optional<Livro> livroEncontrado = Optional.empty();
        if (resultSet.next()) {
            int idLivro = resultSet.getInt("id");
            String nomeLivro = resultSet.getString("titulo");
            String autorLivro = resultSet.getString("autor");
            LocalDate dataPublicacaoLivro = resultSet.getDate("data_publicacao").toLocalDate();
            livroEncontrado = Optional.of(new Livro(nomeLivro, autorLivro, dataPublicacaoLivro, idLivro));
        }
        return livroEncontrado;
    }

    public void closeConnection() throws SQLException {
        ConnectionPostgres.closeConnection(conn);
    }
}
