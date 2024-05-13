package app.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import app.customexpections.AdicionarLivroException;
import app.customexpections.AtualizarLivroException;
import app.customexpections.RemoveLivroException;
import app.model.Livro;
import app.repository.LivroRepository;

public class LivroService {
    private LivroRepository livroRepository;

    public LivroService() throws ClassNotFoundException, SQLException {
        livroRepository = new LivroRepository();
    }

    public int adicionarLivro(Livro livro) throws SQLException, AdicionarLivroException {
       return livroRepository.adicionarLivro(livro);
    }

    public void removerLivro(int id) throws SQLException, RemoveLivroException{
        livroRepository.removerLivro(id);
    }

    public void atualizarLivro(Livro livro) throws SQLException, AtualizarLivroException{
        livroRepository.atualizarLivro(livro);
    }

    public List<Livro> buscarLivrosTitulo(String titulo) throws SQLException{
       return livroRepository.buscarLivrosTitulo(titulo);
    }

    public List<Livro> buscarLivrosAutor(String titulo) throws SQLException{
        return livroRepository.buscarLivrosAutor(titulo);
     }

     public List<Livro> buscarLivrosTituloAutor(String titulo,String autor) throws SQLException{
        return livroRepository.buscarLivrosTituloAutor(titulo, autor);
     }

     public Optional<Livro> buscarLivroId(int id) throws SQLException{
        return livroRepository.buscarLivroId(id);
     }

     public void closeConnection() throws SQLException{
        livroRepository.closeConnection();
     }
}
