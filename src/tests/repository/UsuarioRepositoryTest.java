package tests.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import app.customexpections.*;
import app.model.Usuario;
import app.repository.UsuarioRepository;
import java.sql.SQLException;
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
            usuarioRepository = new UsuarioRepository();
            usuario = new Usuario("vinicius", "12345678901");
            usuarioRepository.adicionarUsuario(usuario);
        } catch (SQLException | AdicionaUsuarioException | ClassNotFoundException e) {
            fail(e.getMessage());
        }
       
    }

    @Test
    public void buscaUsuarioTest(){
        String cpfinvalido = "123";
        
        try {
            // testa busca com cpf invalido
            assertTrue(usuarioRepository.buscaUsuario(cpfinvalido).isEmpty());

            // testa busca com cpf valido e verifica se os dados estao corretos
            Optional<Usuario> usuarioEncontrado = usuarioRepository.buscaUsuario(usuario.getCpf());
            assertEquals(usuario.getCpf(), usuarioEncontrado.get().getCpf());
            assertEquals(usuario.getNome(), usuarioEncontrado.get().getNome());

        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void adicionarUsuarioTest() {
        // adicionando usuario com cpf menor que 12
        Usuario usuarioInvalido = new Usuario("teste", "123");
        
        assertThrows(AdicionaUsuarioException.class, ()-> usuarioRepository.adicionarUsuario(usuarioInvalido));

        // adicionando usuario com cpf do tamanho correto porem invalido
        usuarioInvalido.setCpf("23456vinici");
        assertThrows(AdicionaUsuarioException.class, ()-> usuarioRepository.adicionarUsuario(usuarioInvalido));

        // adicionando usuario com cpf já existente
        usuarioInvalido.setCpf("12345678901");
        assertThrows(AdicionaUsuarioException.class, ()-> usuarioRepository.adicionarUsuario(usuarioInvalido));
    }

    @After
    public void fecharConexao() {
        try {
            usuarioRepository.removerUsuario(usuario.getCpf());
            usuarioRepository.closeConnection();
        } catch (SQLException | RemoveUsuarioException e) {
            fail(e.getMessage());
        }
      
    }
}
