package service;

import dao.ProdutoDAO;
import model.Produto;
import spark.Request;
import spark.Response;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDate;

public class ProdutoService {
    private ProdutoDAO produtoDAO;
    private Gson gson;

    public ProdutoService() {
        produtoDAO = new ProdutoDAO();
        produtoDAO.conectar();
        
        gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return LocalDate.parse(json.getAsString());
                }
            })
            .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.toString());
                }
            })
            .create();
    }

    public Object add(Request request, Response response) {
        response.type("application/json");
        try {
            Produto produto = gson.fromJson(request.body(), Produto.class);
            boolean inserted = produtoDAO.insert(produto);
            if (!inserted) {
                throw new RuntimeException("Falha ao inserir no DAO. Verifique a conexao e a query.");
            }
            response.status(201);
            return gson.toJson(produto);
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return "{\"message\": \"Erro ao inserir produto: " + e.getMessage() + "\"}";
        }
    }

    public Object get(Request request, Response response) {
        return "{}";
    }

    public Object update(Request request, Response response) {
        response.type("application/json");
        try {
            int id = Integer.parseInt(request.params(":id"));
            Produto produto = gson.fromJson(request.body(), Produto.class);
            produto.setId(id);
            
            boolean updated = produtoDAO.update(produto);
            if (!updated) {
                response.status(404);
                return "{\"message\": \"Produto nao encontrado\"}";
            }
            return gson.toJson(produto);
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return "{\"message\": \"Erro ao atualizar produto: " + e.getMessage() + "\"}";
        }
    }

    public Object remove(Request request, Response response) {
        response.type("application/json");
        try {
            int id = Integer.parseInt(request.params(":id"));
            boolean deleted = produtoDAO.delete(id);
            if (!deleted) {
                response.status(404);
                return "{\"message\": \"Produto nao encontrado\"}";
            }
            return "{\"message\": \"Produto excluido com sucesso\"}";
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return "{\"message\": \"Erro ao excluir produto: " + e.getMessage() + "\"}";
        }
    }

    public Object getAll(Request request, Response response) {
        response.type("application/json");
        return gson.toJson(produtoDAO.getAll());
    }
}
