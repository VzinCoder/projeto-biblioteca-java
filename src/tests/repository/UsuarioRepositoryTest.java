package tests.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import app.customexpections.*;
import app.model.Usuario;
import app.repository.UsuarioRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UsuarioRepositoryTest {

    private UsuarioRepository usuarioRepository;
    private Usuario usuario;

    @Before
    public void iniciarConexao() {
        try {
            String nome = "vinicius";
            String cpf = "12345678901";
            usuarioRepository = new UsuarioRepository();
            usuario = new Usuario(nome, cpf);
            int id = usuarioRepository.adicionarUsuario(usuario);
            usuario.setId(id);
        } catch (SQLException | AdicionaUsuarioException | ClassNotFoundException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void buscaUsuarioTest() {
        String cpfinvalido = "123";

        try {
            // testa busca com cpf invalido
            assertTrue(usuarioRepository.buscaUsuario(cpfinvalido).isEmpty());

            // testa busca com cpf valido e verifica se os dados estao corretos
            Optional<Usuario> usuarioEncontrado = usuarioRepository.buscaUsuario(usuario.getCpf());
            assertEquals(usuario.getCpf(), usuarioEncontrado.get().getCpf());
            assertEquals(usuario.getNome(), usuarioEncontrado.get().getNome());

        } catch (SQLException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void adicionarUsuarioTest() {
        // adicionando usuario com cpf menor que 12
        Usuario usuarioInvalido = new Usuario("teste", "123");

        assertThrows(AdicionaUsuarioException.class, () -> usuarioRepository.adicionarUsuario(usuarioInvalido));

        // adicionando usuario com cpf do tamanho correto porem invalido
        usuarioInvalido.setCpf("23456vinici");
        assertThrows(AdicionaUsuarioException.class, () -> usuarioRepository.adicionarUsuario(usuarioInvalido));

        // adicionando usuario com cpf já existente
        usuarioInvalido.setCpf("12345678901");
        assertThrows(AdicionaUsuarioException.class, () -> usuarioRepository.adicionarUsuario(usuarioInvalido));
    }

    @Test
    public void removerUsuarioTest() {

        // remover usuario com formato de cpf invalido
        assertThrows(RemoveUsuarioException.class, () -> usuarioRepository.removerUsuario("123z"));

        // remover usuario que nao existe
        assertThrows(RemoveUsuarioException.class, () -> usuarioRepository.removerUsuario("22222222222"));
    }

    @Test
    public void atualizarUsuarioTest() {
        Usuario usuarioTest = new Usuario("test", "invalid cpf");
        String cpfFormatValid = "22222222222";
        String cpfInvalid = "143vhga";

        // buscando com cpf invalido
        assertThrows(AtualizarUsuarioException.class,
                () -> usuarioRepository.atualizarUsuario(cpfInvalid, usuarioTest));

        // atualizando usuario que nao existe
        assertThrows(AtualizarUsuarioException.class,
                () -> usuarioRepository.atualizarUsuario(cpfFormatValid, usuarioTest));

        // atualizando usuario com novo cpf invalido
        assertThrows(AtualizarUsuarioException.class,
                () -> usuarioRepository.atualizarUsuario(usuario.getCpf(), usuarioTest));

        // atualizando cpf do usuario para um já existente
        try {
            Usuario usuarioExistente = new Usuario("usuario test", cpfFormatValid);
            usuarioRepository.adicionarUsuario(usuarioExistente);
            assertThrows(AtualizarUsuarioException.class,
                    () -> usuarioRepository.atualizarUsuario(usuario.getCpf(), usuarioExistente));
            usuarioRepository.removerUsuario(cpfFormatValid);
        } catch (SQLException | RemoveUsuarioException | AdicionaUsuarioException e) {
            fail(e.getMessage());
        }

        // atualizando nome de um usuario existente
        try {
            // atualizando dados do usuario test
            String nomeAntigo = usuario.getNome();
            usuario.setNome("novo nome test");

            usuarioRepository.atualizarUsuario(usuario.getCpf(), usuario);
            Optional<Usuario> usuarioEncontrado = usuarioRepository.buscaUsuario(usuario.getCpf());
            assertNotEquals(nomeAntigo, usuarioEncontrado.get().getNome());
            assertEquals(usuario.getNome(), usuarioEncontrado.get().getNome());
            assertEquals(usuario.getCpf(), usuarioEncontrado.get().getCpf());

        } catch (SQLException | AtualizarUsuarioException e) {
            fail(e.getMessage());
        }

        // atualizando nome e cpf de um usuario existente
        try {
            String nomeAntigo = usuario.getNome();
            usuarioTest.setNome("novo nome test2");
            usuarioTest.setCpf(cpfFormatValid);

            usuarioRepository.atualizarUsuario(usuario.getCpf(), usuarioTest);
            Optional<Usuario> usuarioEncontrado = usuarioRepository.buscaUsuario(usuarioTest.getCpf());
            assertNotEquals(nomeAntigo, usuarioEncontrado.get().getNome());
            assertEquals(usuarioTest.getNome(), usuarioEncontrado.get().getNome());
            assertEquals(usuarioTest.getCpf(), usuarioEncontrado.get().getCpf());

        } catch (SQLException | AtualizarUsuarioException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void buscarUsuariosPorNomeTest() {
        try {
            // buscando usuarios existente
            List<Usuario> usuarios = usuarioRepository.buscarUsuariosPorNome("vinicius");
            assertFalse(usuarios.isEmpty());

            // buscando usuarios existente usando apenas uma parte do nome
            usuarios = usuarioRepository.buscarUsuariosPorNome("nici");
            assertFalse(usuarios.isEmpty());

            // buscando usuarios que nao existem
            usuarios = usuarioRepository.buscarUsuariosPorNome("ahfdaui");
            assertTrue(usuarios.isEmpty());
        } catch (SQLException e) {
            fail(e.getMessage());
        }

    }

    @After
    public void fecharConexao() {
        try {
            usuarioRepository.removerUsuarioPorId(usuario.getId());

            usuarioRepository.closeConnection();
        } catch (SQLException | RemoveUsuarioException e) {
            fail(e.getMessage());
        }

    }
}
