package org.example.Controller;

import io.javalin.http.Context;
import org.example.Model.Aluno;
import org.example.Model.Mensagem;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiController {

    private final List<Aluno> bancoDeDadosAlunos = new ArrayList<>();

    //1.1
    public void getHello(Context ctx) {
        ctx.result("Hello, Javalin!");
    }

    //1.2
    public void getStatus(Context ctx) {
        ctx.json(Map.of(
                "status", "ok",
                "timestamp", Instant.now().toString()
        ));
    }

    //1.3
    public void postEcho(Context ctx) {
        Mensagem msg = ctx.bodyAsClass(Mensagem.class);
        ctx.json(msg);
    }

    //1.4
    public void getSaudacao(Context ctx) {
        String nome = ctx.pathParam("nome");
        ctx.json(Map.of("mensagem", "Olá, " + nome + "!"));
    }

    //1.5
    public void createAluno(Context ctx) {
        Aluno novoAluno = ctx.bodyAsClass(Aluno.class);
        bancoDeDadosAlunos.add(novoAluno);
        ctx.status(201);
        ctx.json(novoAluno);
    }

    //1.6
    public void getAllAlunos(Context ctx) {
        ctx.json(bancoDeDadosAlunos);
    }

    public void getAlunoByEmail(Context ctx) {
        String emailBusca = ctx.pathParam("email");

        Aluno alunoEncontrado = bancoDeDadosAlunos.stream()
                .filter(aluno -> aluno.getEmail().equalsIgnoreCase(emailBusca))
                .findFirst()
                .orElse(null);

        if (alunoEncontrado != null) {
            ctx.json(alunoEncontrado);
        } else {
            ctx.status(404);
            ctx.json(Map.of("erro", "Aluno não encontrado para o email: " + emailBusca));
        }
    }
}