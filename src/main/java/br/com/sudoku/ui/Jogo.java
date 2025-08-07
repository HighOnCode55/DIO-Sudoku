package br.com.sudoku.ui;

import br.com.sudoku.modelo.Tabuleiro;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Jogo extends Application {

    private Tabuleiro tabuleiro = new Tabuleiro();
    private TextField[][] celulasVisuais = new TextField[9][9];

    @Override
    public void start(Stage primaryStage) {
        tabuleiro.gerarNovoJogo(); // Gera o quebra-cabeça

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        // Cria os 81 campos de texto
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                TextField celula = new TextField();
                celulasVisuais[linha][coluna] = celula;
                gridPane.add(celula, coluna, linha);

                // Listener para validar a jogada em tempo real
                final int l = linha;
                final int c = coluna;
                celula.textProperty().addListener((obs, oldText, newText) -> {
                    handleInput(celula, newText, l, c);
                });
            }
        }

        atualizarInterface();

        Scene scene = new Scene(gridPane);

        // Carrega nosso arquivo CSS
        scene.getStylesheets().add(getClass().getResource("/br/com/sudoku/styles.css").toExternalForm());


        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleInput(TextField celula, String newText, int linha, int coluna) {
        // Remove estilos antigos
        celula.getStyleClass().removeAll("celula-valida", "celula-invalida");

        if (!newText.matches("[1-9]?")) { // Permite apenas um dígito de 1-9 ou vazio
            celula.setText("");
            tabuleiro.setNumero(linha, coluna, 0);
            return;
        }

        if (newText.isEmpty()) {
            tabuleiro.setNumero(linha, coluna, 0);
            return;
        }

        int numero = Integer.parseInt(newText);

        // Verifica se a jogada é válida e aplica o estilo
        if (tabuleiro.isJogadaValida(linha, coluna, numero)) {
            celula.getStyleClass().add("celula-valida");
        } else {
            celula.getStyleClass().add("celula-invalida");
        }

        // Atualiza o tabuleiro lógico, mesmo que a jogada seja inválida
        tabuleiro.setNumero(linha, coluna, numero);
    }

    private void atualizarInterface() {
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                int numero = tabuleiro.getNumero(linha, coluna);
                TextField celula = celulasVisuais[linha][coluna];

                if (tabuleiro.isCelulaInicial(linha, coluna)) {
                    celula.setText(String.valueOf(numero));
                    celula.setEditable(false);
                    celula.getStyleClass().add("celula-inicial");
                } else {
                    celula.setText(""); // Células do usuário começam vazias
                    celula.setEditable(true);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}