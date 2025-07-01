package br.com.gerenciamento.service;

import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.repository.UsuarioRepository;
import br.com.gerenciamento.util.Util;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    private ServiceUsuario serviceUsuario;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Before
    public void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    public void salvarUsuario_Success() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUser("testuser");
        usuario.setEmail("test@email.com");
        usuario.setSenha("password123");
        serviceUsuario.salvarUsuario(usuario);
        Usuario saved = usuarioRepository.findByEmail("test@email.com");
        assertNotNull(saved);
        assertNotEquals("password123", saved.getSenha());
    }

    @Test(expected = EmailExistsException.class)
    public void salvarUsuario_EmailDuplicado() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setUser("user1");
        usuario1.setEmail("duplicate@email.com");
        usuario1.setSenha("password123");
        serviceUsuario.salvarUsuario(usuario1);
        Usuario usuario2 = new Usuario();
        usuario2.setUser("user2");
        usuario2.setEmail("duplicate@email.com");
        usuario2.setSenha("password456");
        serviceUsuario.salvarUsuario(usuario2);
    }

    @Test
    public void loginUsuario_Success() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUser("logintest");
        usuario.setEmail("login@email.com");
        usuario.setSenha("password123");
        serviceUsuario.salvarUsuario(usuario);
        Usuario loginResult = serviceUsuario.loginUser("logintest", Util.md5("password123"));
        assertNotNull(loginResult);
        assertEquals("logintest", loginResult.getUser());
    }

    @Test
    public void loginUsuarioInvalido() {
        Usuario result = serviceUsuario.loginUser("nonexistent", "wrongpassword");
        assertNull(result);
    }
}
