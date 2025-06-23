package org.example;

import io.javalin.Javalin;
import org.example.Controller.ApiController;

public class Main {
    public static void main(String[] args) {

        ApiController controller = new ApiController();

        Javalin app = Javalin.create().start(7000);

        app.get("/hello", controller::getHello);
        app.get("/status", controller::getStatus);
        app.post("/echo", controller::postEcho);
        app.get("/saudacao/{nome}", controller::getSaudacao);
        app.post("/alunos", controller::createAluno);
    }
}