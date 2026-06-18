package com.ti2;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        // Carrega o .env
        Dotenv dotenv = Dotenv.configure().directory("../").load();

        // Azure keys
        String azureKey = dotenv.get("AZURE_KEY");
        String azureEndpoint = dotenv.get("AZURE_ENDPOINT");

        // Database connection string
        String dbHost = dotenv.get("DB_HOST");
        String dbPort = dotenv.get("DB_PORT");
        String dbName = dotenv.get("DB_NAME");
        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");
        String dbSslMode = dotenv.get("DB_SSLMODE");

        String dbUrl = String.format("jdbc:postgresql://%s:%s/%s?sslmode=%s", dbHost, dbPort, dbName, dbSslMode);

        TextAnalyticsClient client = new TextAnalyticsClientBuilder()
                .credential(new AzureKeyCredential(azureKey))
                .endpoint(azureEndpoint)
                .buildClient();

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Conexão com banco de dados estabelecida.");

            // Criacao da tabela
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS textos (id SERIAL PRIMARY KEY, conteudo TEXT)");
                System.out.println("Tabela 'textos' pronta para uso.");
            }

            Scanner scanner = new Scanner(System.in);
            int opcao = -1;

            while (opcao != 0) {
                System.out.println("\n===== MENU INTERATIVO =====");
                System.out.println("1 - Adicionar texto ao banco de dados");
                System.out.println("2 - Excluir texto do banco de dados");
                System.out.println("3 - Analisar sentimentos dos textos armazenados");
                System.out.println("4 - Listar textos armazenados (sem analisar)");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");
                
                try {
                    opcao = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida.");
                    continue;
                }

                switch (opcao) {
                    case 1:
                        System.out.print("Digite o texto a ser adicionado: ");
                        String texto = scanner.nextLine();
                        if (!texto.trim().isEmpty()) {
                            String sqlInsert = "INSERT INTO textos (conteudo) VALUES (?)";
                            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                                pstmt.setString(1, texto);
                                pstmt.executeUpdate();
                                System.out.println("Texto adicionado com sucesso!");
                            }
                        } else {
                            System.out.println("Texto não pode ser vazio.");
                        }
                        break;
                    case 2:
                        System.out.print("Digite o ID do texto a ser excluído: ");
                        try {
                            int id = Integer.parseInt(scanner.nextLine());
                            String sqlDelete = "DELETE FROM textos WHERE id = ?";
                            try (PreparedStatement pstmt = conn.prepareStatement(sqlDelete)) {
                                pstmt.setInt(1, id);
                                int rowsAffected = pstmt.executeUpdate();
                                if (rowsAffected > 0) {
                                    System.out.println("Texto ID " + id + " excluído com sucesso!");
                                } else {
                                    System.out.println("Nenhum texto encontrado com o ID " + id + ".");
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("ID inválido.");
                        }
                        break;
                    case 3:
                        System.out.println("\n--- Analisando Textos do Banco ---");
                        String sqlSelect = "SELECT id, conteudo FROM textos";
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(sqlSelect)) {
                             
                            boolean hasData = false;
                            while (rs.next()) {
                                hasData = true;
                                int id = rs.getInt("id");
                                String conteudo = rs.getString("conteudo");
                                
                                System.out.println("\n[ID: " + id + "] " + conteudo);
                                try {
                                    DocumentSentiment documentSentiment = client.analyzeSentiment(conteudo);
                                    System.out.printf("Sentimento: %s%n", documentSentiment.getSentiment());
                                } catch (Exception ex) {
                                    System.err.println("Erro ao processar sentimento: " + ex.getMessage());
                                }
                            }
                            if (!hasData) {
                                System.out.println("O banco de dados está vazio.");
                            }
                            System.out.println("----------------------------------");
                        }
                        break;
                    case 4:
                        System.out.println("\n--- Lista de Textos do Banco ---");
                        String sqlList = "SELECT id, conteudo FROM textos";
                        try (Statement stmtList = conn.createStatement();
                             ResultSet rsList = stmtList.executeQuery(sqlList)) {
                             
                            boolean hasDataList = false;
                            while (rsList.next()) {
                                hasDataList = true;
                                int id = rsList.getInt("id");
                                String conteudo = rsList.getString("conteudo");
                                System.out.println("[ID: " + id + "] " + conteudo);
                            }
                            if (!hasDataList) {
                                System.out.println("O banco de dados está vazio.");
                            }
                            System.out.println("--------------------------------");
                        }
                        break;
                    case 0:
                        System.out.println("Saindo do programa...");
                        break;
                    default:
                        System.out.println("Opção desconhecida.");
                        break;
                }
            }

        } catch (Exception e) {
            System.err.println("Erro na aplicação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
