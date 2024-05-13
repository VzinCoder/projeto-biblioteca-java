package tests.repository;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import app.customexpections.BuscarEmprestimoException;
import app.customexpections.DevolverLivroException;
import app.customexpections.EmprestarLivroException;
import app.customexpections.RemoveEmprestimoException;
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
    private Usuario usuario2;
    private Livro livro;
    private Livro livro2;
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

            livro = new Livro("java", "paul deitel", LocalDate.now());
            int idLivro = livroRepository.adicionarLivro(livro);
            livro.setId(idLivro);

            emprestimoRepository.emprestarLivro(usuario, livro, LocalDate.now().plusYears(2));
            Optional<Emprestimo> emprestimoEncontrado = emprestimoRepository.buscarEmprestimoPendente(usuario, livro);
            emprestimo = emprestimoEncontrado.get();

            usuario2 = new Usuario("vinicius", "22222222222");
            int idUsuario2 = usuarioRepository.adicionarUsuario(usuario2);
            usuario2.setId(idUsuario2);

            livro2 = new Livro("js", "vinicius", LocalDate.now());
            int idLivro2 = livroRepository.adicionarLivro(livro);
            livro2.setId(idLivro2);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void emprestarLivroTest() {

        // emprestando livro que já foi emprestado para esse usuario e ainda nao foi
        // devolvido

        assertThrows(EmprestarLivroException.class,
                () -> emprestimoRepository.emprestarLivro(usuario, livro, LocalDate.now().plusDays(1)));

        // emprestando livro com data prevista invalida
        assertThrows(EmprestarLivroException.class,
                () -> emprestimoRepository.emprestarLivro(usuario2, livro, LocalDate.now().minusDays(1)));

    }

    @Test
    public void devolverLivroTest() {

        // devolvendo livro de um emprestimo que nao foi cadastrado
        assertThrows(DevolverLivroException.class, () -> emprestimoRepository.devolverLivro(usuario2, livro));

    }

    @Test
    public void buscarEmprestimoPendente() {

        // buscando emprestimo com usuario invalido
        assertThrows(BuscarEmprestimoException.class,
                () -> emprestimoRepository.buscarEmprestimoPendente(new Usuario("fgabgafuhsa", "33333333333"), livro));

        // buscando emprestimo com livro invalido
        assertThrows(BuscarEmprestimoException.class, () -> emprestimoRepository.buscarEmprestimoPendente(usuario,
                new Livro("fjha", "fgjfg", LocalDate.now())));

        // buscando emprestimo que nao existe
        try {
            Optional<Emprestimo> emprestimoEncontrado = emprestimoRepository.buscarEmprestimoPendente(usuario2, livro);
            assertTrue(emprestimoEncontrado.isEmpty());
        } catch (SQLException | BuscarEmprestimoException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void buscarEmprestimoUsuario() {

        // buscando emprestimo com usuario nao cadastrado
        assertThrows(BuscarEmprestimoException.class,
                () -> emprestimoRepository.buscarEmprestimoUsuario(new Usuario("gsdg", "44444444444")));

        try {
            // buscando emprestimos com usuario que nao pegou livros emprestados
            assertTrue(emprestimoRepository.buscarEmprestimoUsuario(usuario2).isEmpty());

            // buscando emprestimos pelo usuario
            assertTrue(emprestimoRepository.buscarEmprestimoUsuario(usuario).size() > 0);

        } catch (SQLException | BuscarEmprestimoException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void buscarEmprestimoLivro() {
        // buscando emprestimo com livro nao cadastrado
        assertThrows(BuscarEmprestimoException.class,
                () -> emprestimoRepository.buscarEmprestimoLivro(new Livro("fvaiujg", "hvisdhgd", LocalDate.now())));

        try {
            // buscando emprestimos com livro que nao foi emprestado
            assertTrue(emprestimoRepository.buscarEmprestimoLivro(livro2).isEmpty());

            // buscando emprestimos pelo livro
            assertTrue(emprestimoRepository.buscarEmprestimoLivro(livro).size() > 0);

        } catch (SQLException | BuscarEmprestimoException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void removerEmprestimo(){

        // removendo emprestimo com id invalido
        assertThrows(RemoveEmprestimoException.class, ()-> emprestimoRepository.removerEmprestimo(0));

        // removendo emprestimo que nao existe
        assertThrows(RemoveEmprestimoException.class, ()-> emprestimoRepository.removerEmprestimo(12435));
    }

    @After
    public void fecharConexao() {
        try {
            usuarioRepository.removerUsuarioPorId(usuario2.getId());
            livroRepository.removerLivro(livro2.getId());

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
