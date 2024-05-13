package tests;

import static org.junit.Assert.fail;

import java.time.LocalDate;
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


    @Before
    public void iniciarConexao() {
        try {
            usuarioRepository = new UsuarioRepository();
            livroRepository = new LivroRepository();
            emprestimoRepository = new EmprestimoRepository();

            usuario = new Usuario("vinicius", "12345678901");
            int idUsuario = usuarioRepository.adicionarUsuario(usuario);
            usuario.setId(idUsuario);

            livro = new Livro("java", "paul deitel",LocalDate.now());
            int idLivro = livroRepository.adicionarLivro(livro);
            livro.setId(idLivro);

            emprestimoRepository.emprestarLivro(usuario, livro, LocalDate.now().plusYears(2));
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
