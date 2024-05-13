package tests.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import app.customexpections.AdicionarLivroException;
import app.customexpections.AtualizarLivroException;
import app.customexpections.RemoveLivroException;
import app.model.Livro;
import app.repository.LivroRepository;

public class LivroRepositoryTest {

    private LivroRepository livroRepository;
    private LocalDate dataInvalida;
    private Livro livro;

    @Before
    public void iniciarConexao() {
        try {
            livroRepository = new LivroRepository();
            livro = new Livro("livro de java para burros", "advgdguidgfi", LocalDate.now());
            int id = livroRepository.adicionarLivro(livro);
            livro.setId(id);
            dataInvalida = LocalDate.now().plusYears(2);
        } catch (SQLException | AdicionarLivroException | ClassNotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void adicionarLivroTest() {

        // adicionando livro com data invalida
        Livro novoLivro = new Livro("titulo", "nome", dataInvalida);
        assertThrows(AdicionarLivroException.class, () -> livroRepository.adicionarLivro(novoLivro));
    }

    @Test
    public void removerLivroTest() {

        // removendo livro com id invalido
        assertThrows(RemoveLivroException.class, () -> livroRepository.removerLivro(-1));

        // removendo livro que nao existe
        assertThrows(RemoveLivroException.class, () -> livroRepository.removerLivro(999999999));

        // removendo livro
        try {
            int idLivro = livroRepository.adicionarLivro(new Livro("test", "test",LocalDate.now()));
            Optional<Livro> livroEncontrado = livroRepository.buscarLivroId(idLivro);
            assertTrue(livroEncontrado.isPresent());
            livroRepository.removerLivro(livroEncontrado.get().getId());
            livroEncontrado = livroRepository.buscarLivroId(idLivro);
            assertTrue(livroEncontrado.isEmpty());
        } catch (SQLException | RemoveLivroException | AdicionarLivroException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void buscarLivroIdTest() {
        try {
            // busca por livro existente
            Optional<Livro> livroEncontrado = livroRepository.buscarLivroId(livro.getId());
            assertTrue(livroEncontrado.isPresent());

            // busca por id invaido
            livroEncontrado = livroRepository.buscarLivroId(99999999);
            assertTrue(livroEncontrado.isEmpty());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void buscarLivrosTituloTest() {
        try {
            // busca por livros
            int id = livroRepository.adicionarLivro(new Livro("JAVA", livro.getAutor(), LocalDate.now()));
            List<Livro> livroEncontrados = livroRepository.buscarLivrosAutor(livro.getAutor());
            assertFalse(livroEncontrados.isEmpty());
            assertTrue(livroEncontrados.size() > 1);
            livroRepository.removerLivro(id);

            // busca por livros que nao existem
            livroEncontrados = livroRepository.buscarLivrosTitulo("ajufbaigfbahaf");
            assertTrue(livroEncontrados.isEmpty());
        } catch (SQLException | RemoveLivroException | AdicionarLivroException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void buscarLivrosPorAutorTest() {
        try {
            // busca por livros
            int id = livroRepository.adicionarLivro(new Livro("JAVA", livro.getAutor(),LocalDate.now()));
            List<Livro> livroEncontrados = livroRepository.buscarLivrosAutor(livro.getAutor());
            assertFalse(livroEncontrados.isEmpty());
            assertTrue(livroEncontrados.size() > 1);
            livroRepository.removerLivro(id);

            // busca por livros que nao existem
            livroEncontrados = livroRepository.buscarLivrosAutor("bfvasdbvuji");
            assertTrue(livroEncontrados.isEmpty());
        } catch (SQLException | RemoveLivroException | AdicionarLivroException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void buscarLivrosTituloAutorTest() {
        try {
            // busca por livros
            int id = livroRepository.adicionarLivro(new Livro("JAVA", livro.getAutor(), LocalDate.now()));
            List<Livro> livroEncontrados = livroRepository.buscarLivrosTituloAutor("java", livro.getAutor());
            assertFalse(livroEncontrados.isEmpty());
            assertTrue(livroEncontrados.size() > 1);
            livroRepository.removerLivro(id);

            // busca por livros com titulo que nao existem
            livroEncontrados = livroRepository.buscarLivrosTituloAutor("fhaifgas", livro.getAutor());
            assertTrue(livroEncontrados.isEmpty());

            // busca por livros com autor que nao existe
            livroEncontrados = livroRepository.buscarLivrosTituloAutor(livro.getTitulo(), "fhaifgas");
            assertTrue(livroEncontrados.isEmpty());

            // busca por livros com titulo e autor que nao existem
            livroEncontrados = livroRepository.buscarLivrosTituloAutor("fhaifgas", "fhaifgas");
            assertTrue(livroEncontrados.isEmpty());

        } catch (SQLException | RemoveLivroException | AdicionarLivroException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void buscarLivrosTest() {
        try {
            // busca por livros
            int id = livroRepository.adicionarLivro(new Livro("JAVA", livro.getAutor(), LocalDate.now()));
            List<Livro> livroEncontrados = livroRepository.buscarLivros();
            assertFalse(livroEncontrados.isEmpty());
            assertTrue(livroEncontrados.size() > 1);
            livroRepository.removerLivro(id);
        } catch (SQLException | RemoveLivroException | AdicionarLivroException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void atualizarLivroTest() {
        Livro livroInvalido = new Livro("ahfjfa", "gfiusd", dataInvalida, -1);
        // livro id invalido
        assertThrows(AtualizarLivroException.class, () -> livroRepository.atualizarLivro(livroInvalido));

        // colocando id valido no livro
        livroInvalido.setId(livro.getId());

        // livro data invalida
        assertThrows(AtualizarLivroException.class, () -> livroRepository.atualizarLivro(livroInvalido));

        // atualizando livro

        try {
            String tituloAntigo = livro.getTitulo();
            livro.setTitulo("Novo titulo para teste");
            livroRepository.atualizarLivro(livro);
            Optional<Livro> livroEncontrado = livroRepository.buscarLivroId(livro.getId());
            assertTrue(livroEncontrado.isPresent());
            assertEquals(livroEncontrado.get().getTitulo(), livro.getTitulo());
            assertEquals(livroEncontrado.get().getAutor(), livro.getAutor());
            assertEquals(livroEncontrado.get().getId(), livro.getId());
            assertNotEquals(tituloAntigo, livroEncontrado.get().getTitulo());
        } catch (SQLException | AtualizarLivroException e) {
            fail(e.getMessage());
        }
    }

    @After
    public void fecharConexao() {
        try {
            livroRepository.removerLivro(livro.getId());
            livroRepository.closeConnection();
        } catch (SQLException | RemoveLivroException e) {
            fail(e.getMessage());
        }

    }
}
