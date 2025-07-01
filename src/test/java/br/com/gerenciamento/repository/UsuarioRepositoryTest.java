package br.com.gerenciamento.repository;

import br.com.gerenciamento.model.Usuario;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void save() {
        Usuario usuario = new Usuario();
        usuario.setUser("testuser");
        usuario.setEmail("test@email.com");
        usuario.setSenha("password123");

        Usuario savedUser = this.usuarioRepository.save(usuario);
        Assert.assertNotNull(savedUser.getId());
    }

    @Test
    public void findByEmail() {
        Usuario usuario = new Usuario();
        usuario.setUser("emailtest");
        usuario.setEmail("findby@email.com");
        usuario.setSenha("password123");
        this.usuarioRepository.save(usuario);

        Usuario foundUser = this.usuarioRepository.findByEmail("findby@email.com");
        Assert.assertNotNull(foundUser);
        Assert.assertEquals("emailtest", foundUser.getUser());
    }

    @Test
    public void buscarLogin() {
        Usuario usuario = new Usuario();
        usuario.setUser("loginuser");
        usuario.setEmail("login@email.com");
        usuario.setSenha("hashedpass");
        this.usuarioRepository.save(usuario);

        Usuario loginUser = this.usuarioRepository.buscarLogin("loginuser", "hashedpass");
        Assert.assertNotNull(loginUser);
        Assert.assertEquals("loginuser", loginUser.getUser());
    }

    @Test
    public void deleteById() {
        Usuario usuario = new Usuario();
        usuario.setUser("deleteuser");
        usuario.setEmail("delete@email.com");
        usuario.setSenha("password123");
        Usuario savedUser = this.usuarioRepository.save(usuario);

        this.usuarioRepository.deleteById(savedUser.getId());
        Assert.assertFalse(this.usuarioRepository.existsById(savedUser.getId()));
    }
}
