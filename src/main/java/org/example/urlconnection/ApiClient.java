package org.example.urlconnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Model.Aluno;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiClient {
    //3.1
    public static void main(String[] args) {
        HttpURLConnection connection = null;
        try {
            URL url = URI.create("http://localhost:7000/alunos").toURL();

            Aluno novoAluno = new Aluno();
            novoAluno.setNome("Aluno Teste");
            novoAluno.setEmail("AlunoTeste@gmail.com");
            novoAluno.setIdade(20);
            String alunoJson = new ObjectMapper().writeValueAsString(novoAluno);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = alunoJson.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int responseCode = connection.getResponseCode();
            System.out.println("Status da Resposta: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("Aluno cadastrado com sucesso!");

                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Corpo da Resposta: " + response.toString());
                }
            } else {
                System.out.println("Ocorreu um erro ao cadastrar o aluno.");
            }
        } catch (Exception e) {
            System.err.println("Ocorreu uma falha na operação: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        //3.2
        HttpURLConnection getConnection = null;
        try {
            System.out.println("\nGET ALUNOS");
            URL url = URI.create("http://localhost:7000/alunos").toURL();

            getConnection = (HttpURLConnection) url.openConnection();
            getConnection.setRequestMethod("GET");
            getConnection.setRequestProperty("Accept", "application/json");

            int responseCode = getConnection.getResponseCode();
            System.out.println("Status da Resposta GET: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Lista de alunos recuperada:");

                try (BufferedReader br = new BufferedReader(new InputStreamReader(getConnection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    String jsonResponse = response.toString();
                    ObjectMapper mapper = new ObjectMapper();
                    Object jsonObject = mapper.readValue(jsonResponse, Object.class);
                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject));
                }
            } else {
                System.out.println("Ocorreu um erro ao listar os alunos.");
            }
        } catch (Exception e) {
            System.err.println("Ocorreu uma falha na operação: " + e.getMessage());
        } finally {
            if (getConnection != null) {
                getConnection.disconnect();
            }
        }
        //3.3
        HttpURLConnection getByEmailConnection = null;
        try {
            System.out.println("\nGET com PathParam");

            String emailParaBuscar = "AlunoTeste@gmail.com";

            URL url = URI.create("http://localhost:7000/alunos/" + emailParaBuscar).toURL();

            getByEmailConnection = (HttpURLConnection) url.openConnection();
            getByEmailConnection.setRequestMethod("GET");
            getByEmailConnection.setRequestProperty("Accept", "application/json");

            int responseCode = getByEmailConnection.getResponseCode();
            System.out.println("Status da Resposta GET (por email): " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Aluno encontrado:");

                try (BufferedReader br = new BufferedReader(new InputStreamReader(getByEmailConnection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    String jsonResponse = response.toString();
                    ObjectMapper mapper = new ObjectMapper();
                    Object jsonObject = mapper.readValue(jsonResponse, Object.class);
                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject));
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                System.out.println("Aluno com o email '" + emailParaBuscar + "' não foi encontrado.");
            } else {
                System.out.println("Ocorreu um erro ao buscar o aluno.");
            }

        } catch (Exception e) {
            System.err.println("Ocorreu uma falha na operação: " + e.getMessage());
        } finally {
            if (getByEmailConnection != null) {
                getByEmailConnection.disconnect();
            }
        }

        //3.4
        HttpURLConnection getStatusConnection = null;
        try {
            System.out.println("\nGET Status e Timestamp");

            URL url = URI.create("http://localhost:7000/status").toURL();

            getStatusConnection = (HttpURLConnection) url.openConnection();
            getStatusConnection.setRequestMethod("GET");
            getStatusConnection.setRequestProperty("Accept", "application/json");

            int responseCode = getStatusConnection.getResponseCode();
            System.out.println("Status da Resposta GET (/status): " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Resposta do /status:");

                try (BufferedReader br = new BufferedReader(new InputStreamReader(getStatusConnection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    String jsonResponse = response.toString();
                    ObjectMapper mapper = new ObjectMapper();
                    Object jsonObject = mapper.readValue(jsonResponse, Object.class);
                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject));
                }
            } else {
                System.out.println("Ocorreu um erro ao verificar o status da API.");
            }

        } catch (Exception e) {
            System.err.println("Ocorreu uma falha na operação: " + e.getMessage());
        } finally {
            if (getStatusConnection != null) {
                getStatusConnection.disconnect();
            }
        }
    }
}