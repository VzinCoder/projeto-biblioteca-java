package app.facade;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import app.customexpections.AdicionaUsuarioException;
import app.customexpections.AdicionarLivroException;
import app.customexpections.AtualizarLivroException;
import app.customexpections.AtualizarUsuarioException;
import app.customexpections.BuscarEmprestimoException;
import app.customexpections.DevolverLivroException;
import app.customexpections.EmprestarLivroException;
import app.customexpections.RemoveEmprestimoException;
import app.customexpections.RemoveLivroException;
import app.customexpections.RemoveUsuarioException;
import app.model.Emprestimo;
import app.model.Livro;
import app.model.Usuario;
import app.service.EmprestimoService;
import app.service.LivroService;
import app.service.UsuarioService;

public class Biblioteca {

    private EmprestimoService emprestimoService;
    private UsuarioService usuarioService;
    private LivroService livroService;

    public Biblioteca() throws ClassNotFoundException, SQLException {
        emprestimoService = new EmprestimoService();
        usuarioService = new UsuarioService();
        livroService = new LivroService();
    }


    public int adicionarUsuario(Usuario usuario) throws SQLException, AdicionaUsuarioException {
        return usuarioService.adicionarUsuario(usuario);
    }

    public void removerUsuario(String cpf) throws SQLException, RemoveUsuarioException {
        usuarioService.removerUsuario(cpf);
    }

    public void removerUsuarioPorId(int id) throws SQLException, RemoveUsuarioException {
        usuarioService.removerUsuarioPorId(id);
    }

    public Optional<Usuario> buscaUsuario(String cpf) throws SQLException {
        return usuarioService.buscaUsuario(cpf);
    }

    public List<Usuario> buscarUsuariosPorNome(String nome) throws SQLException{
        return usuarioService.buscarUsuariosPorNome(nome);
    }

    public void atualizarUsuario(String cpf, Usuario usuarioAtualizado) throws AtualizarUsuarioException, SQLException{
        usuarioService.atualizarUsuario(cpf, usuarioAtualizado);
    }


    //livro

     public int adicionarLivro(Livro livro) throws SQLException, AdicionarLivroException {
       return livroService.adicionarLivro(livro);
    }

    public void removerLivro(int id) throws SQLException, RemoveLivroException{
        livroService.removerLivro(id);
    }

    public void atualizarLivro(Livro livro) throws SQLException, AtualizarLivroException{
        livroService.atualizarLivro(livro);
    }

    public List<Livro> buscarLivrosTitulo(String titulo) throws SQLException{
       return livroService.buscarLivrosTitulo(titulo);
    }

    public List<Livro> buscarLivrosAutor(String titulo) throws SQLException{
        return livroService.buscarLivrosAutor(titulo);
     }

     public List<Livro> buscarLivrosTituloAutor(String titulo,String autor) throws SQLException{
        return livroService.buscarLivrosTituloAutor(titulo, autor);
     }

     public Optional<Livro> buscarLivroId(int id) throws SQLException{
        return livroService.buscarLivroId(id);
     }

     //emprestimo

     public void emprestarLivro(Usuario usuario, Livro livro, LocalDate dataPrevistaDevolucao)
            throws SQLException, EmprestarLivroException, BuscarEmprestimoException {
        emprestimoService.emprestarLivro(usuario, livro, dataPrevistaDevolucao);
    }

    public void devolverLivro(Usuario usuario, Livro livro)
            throws SQLException, DevolverLivroException, BuscarEmprestimoException {
        emprestimoService.devolverLivro(usuario, livro);
    }

    public Optional<Emprestimo> buscarEmprestimoPendente(Usuario usuario, Livro livro)
            throws SQLException, BuscarEmprestimoException {
        return emprestimoService.buscarEmprestimoPendente(usuario, livro);
    }

    public List<Emprestimo> buscarEmprestimoUsuario(Usuario usuario) throws SQLException, BuscarEmprestimoException {
        return emprestimoService.buscarEmprestimoUsuario(usuario);
    }

    public List<Emprestimo> buscarEmprestimoLivro(Livro livro) throws SQLException, BuscarEmprestimoException {
        return emprestimoService.buscarEmprestimoLivro(livro);
    }

    public void removerEmprestimo(int id) throws SQLException, RemoveEmprestimoException {
        emprestimoService.removerEmprestimo(id);
    }

    public void closeConnection() throws SQLException{
        usuarioService.closeConnection();
        livroService.closeConnection();
        emprestimoService.closeConnection();
    }

    

}
