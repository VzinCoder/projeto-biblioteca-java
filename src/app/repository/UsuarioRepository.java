package app.repository;

import app.db.ConnectionPostgres;
import app.customexpections.*;
import app.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {
    private Connection conn;

    public UsuarioRepository() throws ClassNotFoundException, SQLException {
        this.conn = ConnectionPostgres.createConnection();
    }

    public int adicionarUsuario(Usuario usuario) throws SQLException, AdicionaUsuarioException {
        Optional<Usuario> usuarioEncontrado = buscaUsuario(usuario.getCpf());

        if (!usuario.validaCpf()) {
            throw new AdicionaUsuarioException("Cpf Invalido!");
        }

        if (usuarioEncontrado.isPresent()) {
            throw new AdicionaUsuarioException("Já existe um usuario com esse cpf!");
        }

        String sql = "Insert into usuarios(nome,cpf) values (?,?);";

        PreparedStatement pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pst.setString(1, usuario.getNome());
        pst.setString(2, usuario.getCpf());
        pst.executeUpdate();
        ResultSet resultSet = pst.getGeneratedKeys();
        resultSet.next();
        return resultSet.getInt("id");
    }

    public void removerUsuario(String cpf) throws SQLException, RemoveUsuarioException {
        boolean cpfValido = Usuario.validarCpfUsuario(cpf);

        if (!cpfValido) {
            throw new RemoveUsuarioException("Formato de Cpf Invalido!");
        }

        String sql = "Delete from  usuarios where cpf=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, cpf);
        int result = pst.executeUpdate();

        if (result == 0) {
            throw new RemoveUsuarioException("Usuario nao encontrado!");
        }
    }

    public void removerUsuarioPorId(int id) throws SQLException, RemoveUsuarioException {

        if (id < 1) {
            throw new RemoveUsuarioException("id Invalido!");
        }

        String sql = "Delete from  usuarios where id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        int result = pst.executeUpdate();

        if (result == 0) {
            throw new RemoveUsuarioException("Usuario nao encontrado!");
        }
    }

    public Optional<Usuario> buscaUsuario(String cpf) throws SQLException {
        String sql = "Select * from usuarios where cpf=?;";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, cpf);
        ResultSet result = pst.executeQuery();

        Optional<Usuario> usuario = Optional.empty();
        if (result.next()) {
            String nomeUsuario = result.getString("nome");
            int idUsuario = result.getInt("id");
            usuario = Optional.of(new Usuario(idUsuario, nomeUsuario, cpf));
        }

        return usuario;
    }

    public List<Usuario> buscarUsuariosPorNome(String nome) throws SQLException {
        List<Usuario> usuariosEncontrados = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE nome LIKE ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, "%" + nome + "%");

        ResultSet resultSet = pst.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String nomeUsuario = resultSet.getString("nome");
            String cpf = resultSet.getString("cpf");
            Usuario usuario = new Usuario(id, nomeUsuario, cpf);
            usuariosEncontrados.add(usuario);

        }

        return usuariosEncontrados;
    }

    public void atualizarUsuario(String cpf, Usuario usuarioAtualizado) throws AtualizarUsuarioException, SQLException {
        Optional<Usuario> usuarioEncontrado = buscaUsuario(cpf);
        boolean cpfValido = Usuario.validarCpfUsuario(cpf);
        Optional<Usuario> usuarioCpfUso = buscaUsuario(usuarioAtualizado.getCpf());

        if (!cpfValido) {
            throw new AtualizarUsuarioException("Formato de Cpf Invalido!");
        }

        if (!usuarioAtualizado.validaCpf()) {
            throw new AtualizarUsuarioException("Novo Cpf com formato Invalido!");
        }

        if (usuarioEncontrado.isEmpty()) {
            throw new AtualizarUsuarioException("Usuario nao encontrado!");
        }

        if (usuarioCpfUso.isPresent() && verificaCpfUso(usuarioEncontrado.get(), usuarioCpfUso.get())) {
            throw new AtualizarUsuarioException("Cpf em Uso!");
        }

        String sql = "Update usuarios set nome=?, cpf=? where cpf=?;";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, usuarioAtualizado.getNome());
        pst.setString(2, usuarioAtualizado.getCpf());
        pst.setString(3, cpf);
        pst.executeUpdate();

    }

    private boolean verificaCpfUso(Usuario atual, Usuario encontrado) {
        if (atual.getId() != encontrado.getId()) {
            return true;
        }
        return false;
    }

    public void closeConnection() throws SQLException {
        ConnectionPostgres.closeConnection(conn);
    }

}
