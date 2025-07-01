package br.com.gerenciamento.repository;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    public void save() {
        Aluno aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.INFORMATICA);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");

        Aluno savedAluno = this.alunoRepository.save(aluno);
        Assert.assertNotNull(savedAluno.getId());
    }

    @Test
    public void findByStatusAtivo() {
        Aluno aluno = new Aluno();
        aluno.setNome("Maria Ativa");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("789123");
        this.alunoRepository.save(aluno);

        List<Aluno> alunosAtivos = this.alunoRepository.findByStatusAtivo();
        Assert.assertTrue(alunosAtivos.size() > 0);
    }

    @Test
    public void findByStatusInativo() {
        Aluno aluno = new Aluno();
        aluno.setNome("Pedro Inativo");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.CONTABILIDADE);
        aluno.setStatus(Status.INATIVO);
        aluno.setMatricula("456789");
        this.alunoRepository.save(aluno);

        List<Aluno> alunosInativos = this.alunoRepository.findByStatusInativo();
        Assert.assertTrue(alunosInativos.size() > 0);
    }

    @Test
    public void findByNomeContainingIgnoreCase() {
        Aluno aluno = new Aluno();
        aluno.setNome("Ana Carolina");
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.ENFERMAGEM);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("654321");
        this.alunoRepository.save(aluno);

        List<Aluno> alunosEncontrados = this.alunoRepository.findByNomeContainingIgnoreCase("ana");
        Assert.assertTrue(alunosEncontrados.size() > 0);
        Assert.assertEquals("Ana Carolina", alunosEncontrados.get(0).getNome());
    }
}
