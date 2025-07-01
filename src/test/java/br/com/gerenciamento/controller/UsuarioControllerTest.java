package br.com.gerenciamento.controller;

import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.repository.UsuarioRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Before
    public void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    public void testGetCadastroPage() throws Exception {
        mockMvc.perform(get("/cadastro"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    public void testSalvarUsuario() throws Exception {
        mockMvc.perform(post("/salvarUsuario")
                .param("user", "usuarioTeste")
                .param("email", "usuario@email.com")
                .param("senha", "senha123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        List<Usuario> encontrados = usuarioRepository.findAll();
        assertEquals(1, encontrados.size());
        assertEquals("usuario@email.com", encontrados.get(0).getEmail());
    }

    @Test
    public void testLoginUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUser("usuarioLogin");
        usuario.setEmail("login@email.com");
        usuario.setSenha(br.com.gerenciamento.util.Util.md5("senha123"));
        usuarioRepository.save(usuario);

        mockMvc.perform(post("/login")
                .param("user", "usuarioLogin")
                .param("senha", "senha123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginUsuarioInvalido() throws Exception {
        mockMvc.perform(post("/login")
                .param("user", "usuarioInvalido")
                .param("senha", "senhaErrada"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetIndexPage() throws Exception {
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("aluno"));
    }
}