package app.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import app.customexpections.AdicionaUsuarioException;
import app.customexpections.AtualizarUsuarioException;
import app.customexpections.RemoveUsuarioException;
import app.model.Usuario;
import app.repository.UsuarioRepository;

public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioService() throws ClassNotFoundException, SQLException {
        usuarioRepository = new UsuarioRepository();
    }

    public int adicionarUsuario(Usuario usuario) throws SQLException, AdicionaUsuarioException {
        return usuarioRepository.adicionarUsuario(usuario);
    }

    public void removerUsuario(String cpf) throws SQLException, RemoveUsuarioException {
        usuarioRepository.removerUsuario(cpf);
    }

    public void removerUsuarioPorId(int id) throws SQLException, RemoveUsuarioException {
        usuarioRepository.removerUsuarioPorId(id);
    }

    public Optional<Usuario> buscaUsuario(String cpf) throws SQLException {
        return usuarioRepository.buscaUsuario(cpf);
    }

    public List<Usuario> buscarUsuariosPorNome(String nome) throws SQLException{
        return usuarioRepository.buscarUsuariosPorNome(nome);
    }

    public void atualizarUsuario(String cpf, Usuario usuarioAtualizado) throws AtualizarUsuarioException, SQLException{
        usuarioRepository.atualizarUsuario(cpf, usuarioAtualizado);
    }

    public void closeConnection() throws SQLException{
        usuarioRepository.closeConnection();
    }

}
