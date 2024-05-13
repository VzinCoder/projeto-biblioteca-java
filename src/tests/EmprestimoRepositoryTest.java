package tests;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import app.model.Emprestimo;
import app.model.Livro;
import app.model.Usuario;
import app.repository.EmprestimoRepository;
import app.repository.LivroRepository;
import app.repository.UsuarioRepository;

public class EmprestimoRepositoryTest {

    private EmprestimoRepository emprestimoRepository;
    private LivroRepository livroRepository;
    private UsuarioRepository usuarioRepository;
    private Usuario usuario;
    private Livro livro;
    private Emprestimo emprestimo;

    @SuppressWarnings("deprecation")
    @Before
    public void iniciarConexao() {
        try {
            usuarioRepository = new UsuarioRepository();
            livroRepository = new LivroRepository();
            emprestimoRepository = new EmprestimoRepository();

            usuario = new Usuario("vinicius", "12345678901");
            int idUsuario = usuarioRepository.adicionarUsuario(usuario);
            usuario.setId(idUsuario);

            livro = new Livro("java", "paul deitel", new Date());
            int idLivro = livroRepository.adicionarLivro(livro);
            livro.setId(idLivro);

            emprestimoRepository.emprestarLivro(usuario, livro, new Date(2025,1,1));
            Optional<Emprestimo> emprestimoEncontrado = emprestimoRepository.buscarEmprestimoPendente(usuario, livro);
            emprestimo = emprestimoEncontrado.get();

            System.out.println(emprestimo);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test(){

    }

    @After
    public void fecharConexao() {
        try {
            emprestimoRepository.removerEmprestimo(emprestimo.getId());
            usuarioRepository.removerUsuarioPorId(usuario.getId());
            livroRepository.removerLivro(livro.getId());

            emprestimoRepository.closeConnection();
            usuarioRepository.closeConnection();
            livroRepository.closeConnection();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
