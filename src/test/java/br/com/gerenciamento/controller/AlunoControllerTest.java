package br.com.gerenciamento.controller;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.repository.AlunoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;

    @Before
    public void setUp() {
        alunoRepository.deleteAll();

        for (int i = 0; i < 3; i++) {
            Aluno ativo = new Aluno();
            ativo.setNome("AlunoAtivo" + i);
            ativo.setMatricula(String.format("A%05d", i));
            ativo.setCurso(Curso.INFORMATICA);
            ativo.setStatus(Status.ATIVO);
            ativo.setTurno(Turno.MATUTINO);
            alunoRepository.save(ativo);
        }

        for (int i = 0; i < 2; i++) {
            Aluno inativo = new Aluno();
            inativo.setNome("AlunoInativo" + i);
            inativo.setMatricula(String.format("I%05d", i));
            inativo.setCurso(Curso.ADMINISTRACAO);
            inativo.setStatus(Status.INATIVO);
            inativo.setTurno(Turno.NOTURNO);
            alunoRepository.save(inativo);
        }
    }

    @Test
    public void testGetAlunosAtivos() throws Exception {
        mockMvc.perform(get("/alunos-ativos"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("alunosAtivos"))
                .andExpect(model().attribute("alunosAtivos", hasSize(3)))
                .andExpect(model().attribute("alunosAtivos", everyItem(
                        hasProperty("status", is(Status.ATIVO)))));
    }

    @Test
    public void testGetAlunosInativos() throws Exception {
        mockMvc.perform(get("/alunos-inativos"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("alunosInativos"))
                .andExpect(model().attribute("alunosInativos", hasSize(2)))
                .andExpect(model().attribute("alunosInativos", everyItem(
                        hasProperty("status", is(Status.INATIVO)))));
    }

    @Test
    public void testInserirAluno() throws Exception {
        mockMvc.perform(post("/InsertAlunos")
                .param("nome", "Novo Aluno")
                .param("matricula", "M123456")
                .param("curso", "INFORMATICA")
                .param("status", "ATIVO")
                .param("turno", "MATUTINO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alunos-adicionados"));

        List<Aluno> encontrados = alunoRepository.findByNomeContainingIgnoreCase("Novo Aluno");
        assertEquals(1, encontrados.size());
        assertEquals("M123456", encontrados.get(0).getMatricula());
    }

    @Test
    public void testInserirAlunoComErroValidacao() throws Exception {
        mockMvc.perform(post("/InsertAlunos")
                .param("nome", "Aluno Sem Matricula")
                .param("curso", "INFORMATICA")
                .param("status", "ATIVO")
                .param("turno", "MATUTINO"))
                .andExpect(status().isOk());

        List<Aluno> encontrados = alunoRepository.findByNomeContainingIgnoreCase("Aluno Sem Matricula");
        assertEquals(0, encontrados.size());
    }

    @Test
    public void testRemoverAluno() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setNome("Aluno Remover");
        aluno.setMatricula("R123456");
        aluno.setCurso(Curso.INFORMATICA);
        aluno.setStatus(Status.ATIVO);
        aluno.setTurno(Turno.MATUTINO);
        aluno = alunoRepository.save(aluno);

        Long id = aluno.getId();

        mockMvc.perform(get("/remover/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alunos-adicionados"));

        assertFalse(alunoRepository.findById(id).isPresent());
    }
}