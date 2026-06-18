package app;

import static spark.Spark.*;
import service.ProdutoService;

public class Aplicacao {
    private static ProdutoService produtoService = new ProdutoService();

    public static void main(String[] args) {
        port(4567);

        // Configuracao de CORS para permitir que o Vite consiga acessar a API
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        // Mapeamento das rotas
        post("/produto", (request, response) -> produtoService.add(request, response));
        get("/produto/:id", (request, response) -> produtoService.get(request, response));
        put("/produto/:id", (request, response) -> produtoService.update(request, response));
        delete("/produto/:id", (request, response) -> produtoService.remove(request, response));
        get("/produto", (request, response) -> produtoService.getAll(request, response));
    }
}
