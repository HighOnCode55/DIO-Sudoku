package br.com.sudoku.ui;

import br.com.sudoku.modelo.Tabuleiro;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Jogo extends Application {

    private Tabuleiro tabuleiro = new Tabuleiro();
    private Button[][] celulasVisuais = new Button[9][9];
    private Button botaoSelecionado = null; // Rastreia o botão atualmente selecionado
    private int linhaSelecionada = -1; // Rastreia a linha do botão selecionado
    private int colunaSelecionada = -1; // Rastreia a coluna do botão selecionado

    @Override
    public void start(Stage primaryStage) {
        tabuleiro.gerarNovoJogo(); // Gera o quebra-cabeça

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        // Cria os 81 botões
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                Button celula = new Button();
                celula.getStyleClass().add("text-field"); // Mantém o estilo básico
                celulasVisuais[linha][coluna] = celula;
                gridPane.add(celula, coluna, linha);

                // Configura o evento de clique
                final int l = linha;
                final int c = coluna;
                celula.setOnAction(event -> {
                    handleButtonClick(celula, l, c);
                });
            }
        }

        atualizarInterface();

        Scene scene = new Scene(gridPane);

        // Carrega nosso arquivo CSS
        scene.getStylesheets().add(getClass().getResource("/br/com/sudoku/styles.css").toExternalForm());
        
        // Adiciona um handler para eventos de teclado
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            handleKeyPress(event);
        });

        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleButtonClick(Button celula, int linha, int coluna) {
        // Ignora cliques em células iniciais
        if (tabuleiro.isCelulaInicial(linha, coluna)) {
            return;
        }

        // Se já existe um botão selecionado, remove a classe de seleção
        if (botaoSelecionado != null) {
            botaoSelecionado.getStyleClass().remove("celula-selecionada");
        }

        // Seleciona o novo botão
        botaoSelecionado = celula;
        linhaSelecionada = linha;
        colunaSelecionada = coluna;
        
        // Adiciona a classe de seleção ao botão
        celula.getStyleClass().add("celula-selecionada");
    }
    
    private void handleKeyPress(KeyEvent event) {
        // Verifica se há um botão selecionado
        if (botaoSelecionado == null) {
            return;
        }
        
        // Verifica se a tecla pressionada é um número de 1 a 9
        KeyCode code = event.getCode();
        int numero = -1;
        
        if (code.isDigitKey()) {
            String digitText = code.getName();
            try {
                numero = Integer.parseInt(digitText);
                if (numero < 1 || numero > 9) {
                    return; // Ignora números fora do intervalo 1-9
                }
            } catch (NumberFormatException e) {
                return; // Ignora se não for um número válido
            }
        } else {
            return; // Ignora teclas que não são dígitos
        }
        
        // Remove estilos antigos
        botaoSelecionado.getStyleClass().removeAll("celula-valida", "celula-invalida");
        
        // Atualiza o texto do botão
        botaoSelecionado.setText(String.valueOf(numero));
        
        // Verifica se a jogada é válida e aplica o estilo
        if (tabuleiro.isJogadaValida(linhaSelecionada, colunaSelecionada, numero)) {
            botaoSelecionado.getStyleClass().add("celula-valida");
        } else {
            botaoSelecionado.getStyleClass().add("celula-invalida");
        }
        
        // Atualiza o tabuleiro lógico, mesmo que a jogada seja inválida
        tabuleiro.setNumero(linhaSelecionada, colunaSelecionada, numero);
        
        // Consome o evento para evitar que seja processado por outros handlers
        event.consume();
    }

    private void atualizarInterface() {
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                int numero = tabuleiro.getNumero(linha, coluna);
                Button celula = celulasVisuais[linha][coluna];

                if (tabuleiro.isCelulaInicial(linha, coluna)) {
                    celula.setText(String.valueOf(numero));
                    celula.getStyleClass().add("celula-inicial");
                    // Desabilita o botão para células iniciais
                    celula.setDisable(true);
                } else {
                    celula.setText(""); // Células do usuário começam vazias
                    // Habilita o botão para células do usuário
                    celula.setDisable(false);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}