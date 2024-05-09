package app.repository;

import app.db.ConnectionPostgres;
import app.customexpections.*;
import app.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsuarioRepository {
    private Connection conn;

    public UsuarioRepository() throws ClassNotFoundException, SQLException {
        this.conn = ConnectionPostgres.createConnection();
    }

    public void adicionarUsuario(Usuario usuario) throws SQLException, AdicionaUsuarioException {
        Optional<Usuario> usuarioEncontrado = buscaUsuario(usuario.getCpf());

        if(!usuario.validaCpf()){
            throw new AdicionaUsuarioException("Cpf Invalido!");
        }

        if(usuarioEncontrado.isPresent()){
            throw new AdicionaUsuarioException("Já existe um usuario com esse cpf!");
        }

        String sql = "Insert into usuarios(nome,cpf) values (?,?);";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, usuario.getNome());
        pst.setString(2, usuario.getCpf());
        pst.executeUpdate();
    }

    public void removerUsuario(String cpf) throws SQLException, RemoveUsuarioException {
        String sql = "Delete from  usuarios where cpf=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, cpf);
        int result = pst.executeUpdate();

        if (result == 0) {
            throw new RemoveUsuarioException("Usuario nao encontrado");
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
            usuario = Optional.of(new Usuario(nomeUsuario, cpf));
        }

        return usuario;
    }

    public void closeConnection() throws SQLException {
        ConnectionPostgres.closeConnection(conn);
    }

}
