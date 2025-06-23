package org.example.Controller;

import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Model.Aluno;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

class ApiControllerTest {

    private final ApiController controller = new ApiController();
    //2.1
    @Test
    void test_GET_hello_endpoint() {

        JavalinTest.test((app, client) -> {

            app.get("/hello", controller::getHello);
            var response = client.get("/hello");
            assertEquals(200, response.code());
            assertEquals("Hello, Javalin!", response.body().string());
        });
    }
    //2.2
    @Test
    void test_POST_create_aluno_returns_201() {
        JavalinTest.test((app, client) -> {

            app.post("/alunos", controller::createAluno);

            Aluno novoAluno = new Aluno();
            novoAluno.setNome("Joao Teste");
            novoAluno.setEmail("joao.teste@email.com");
            novoAluno.setIdade(30);

            String alunoJson = new ObjectMapper().writeValueAsString(novoAluno);
            var response = client.post("/alunos", alunoJson);
            assertEquals(201, response.code());
        });
    }
    //2.3
    @Test
    void test_GET_aluno_by_email_retrieves_correct_data() {
        ApiController localController = new ApiController();

        JavalinTest.test((app, client) -> {

            app.post("/alunos", localController::createAluno);
            app.get("/alunos/{email}", localController::getAlunoByEmail);

            Aluno alunoParaCadastrar = new Aluno();
            alunoParaCadastrar.setNome("Aluno Teste");
            alunoParaCadastrar.setEmail("AlunoTeste@gmail.com");
            alunoParaCadastrar.setIdade(19);

            String alunoJson = new ObjectMapper().writeValueAsString(alunoParaCadastrar);

            client.post("/alunos", alunoJson);

            var getResponse = client.get("/alunos/" + alunoParaCadastrar.getEmail());

            assertEquals(200, getResponse.code());
            Aluno alunoRecuperado = new ObjectMapper().readValue(getResponse.body().string(), Aluno.class);
            assertEquals(alunoParaCadastrar.getNome(), alunoRecuperado.getNome());
            assertEquals(alunoParaCadastrar.getEmail(), alunoRecuperado.getEmail());
            assertEquals(alunoParaCadastrar.getIdade(), alunoRecuperado.getIdade());
        });
    }

    //2.4
    @Test
    void test_GET_all_alunos_returns_list_after_creation() {
        ApiController localController = new ApiController();

        JavalinTest.test((app, client) -> {

            app.post("/alunos", localController::createAluno);
            app.get("/alunos", localController::getAllAlunos);

            Aluno aluno1 = new Aluno();
            aluno1.setNome("Aluno Teste 1");
            aluno1.setEmail("AlunoTeste1@gmail.com");
            aluno1.setIdade(35);

            Aluno aluno2 = new Aluno();
            aluno2.setNome("Aluno Teste 2");
            aluno2.setEmail("AlunoTeste2@gmail.com");
            aluno2.setIdade(26);

            client.post("/alunos", new ObjectMapper().writeValueAsString(aluno1));

            client.post("/alunos", new ObjectMapper().writeValueAsString(aluno2));

            var getResponse = client.get("/alunos");

            assertEquals(200, getResponse.code());

            List<Aluno> listaRecuperada = new ObjectMapper().readValue(
                    getResponse.body().string(),
                    new TypeReference<List<Aluno>>() {}
            );

            assertEquals(2, listaRecuperada.size());
            assertTrue(listaRecuperada.stream().anyMatch(aluno ->
                    aluno.getEmail().equals("AlunoTeste1@gmail.com")
            ));
        });
    }
}