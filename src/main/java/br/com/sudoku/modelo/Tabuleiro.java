package br.com.sudoku.modelo;

public class Tabuleiro {

    private int[][] tabuleiro;
    private boolean[][] celulasIniciais; // Para saber quais números são do jogo original

    public Tabuleiro() {
        this.tabuleiro = new int[9][9];
        this.celulasIniciais = new boolean[9][9];
    }

    // Método para gerar um novo jogo (aqui usamos um exemplo fixo)
    public void gerarNovoJogo() {
        // Um tabuleiro de Sudoku resolvido como base
        int[][] base = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };

        // Copia a base para o nosso tabuleiro
        for (int i = 0; i < 9; i++) {
            System.arraycopy(base[i], 0, this.tabuleiro[i], 0, 9);
        }

        // Remove alguns números para criar o quebra-cabeça (nível fácil)
        int numerosARemover = 40;
        for (int i = 0; i < numerosARemover; i++) {
            int lin = (int) (Math.random() * 9);
            int col = (int) (Math.random() * 9);
            // Garante que não removemos o mesmo número duas vezes
            if (this.tabuleiro[lin][col] != 0) {
                this.tabuleiro[lin][col] = 0;
            } else {
                i--; // Tenta novamente
            }
        }

        // Marca quais células são as iniciais (pistas)
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.celulasIniciais[i][j] = (this.tabuleiro[i][j] != 0);
            }
        }
    }

    // --- MÉTODOS DE VERIFICAÇÃO ---

    public boolean isJogadaValida(int linha, int coluna, int numero) {
        // Se o número for 0 (célula vazia), a jogada é considerada "neutra", não inválida
        if (numero == 0) return true;

        return !isNumeroNaLinha(linha, numero) &&
                !isNumeroNaColuna(coluna, numero) &&
                !isNumeroNaCaixa(linha, coluna, numero);
    }

    private boolean isNumeroNaLinha(int linha, int numero) {
        for (int col = 0; col < 9; col++) {
            if (tabuleiro[linha][col] == numero) {
                return true;
            }
        }
        return false;
    }

    private boolean isNumeroNaColuna(int coluna, int numero) {
        for (int lin = 0; lin < 9; lin++) {
            if (tabuleiro[lin][coluna] == numero) {
                return true;
            }
        }
        return false;
    }

    private boolean isNumeroNaCaixa(int linha, int coluna, int numero) {
        int linhaInicioCaixa = linha - linha % 3;
        int colunaInicioCaixa = coluna - coluna % 3;
        for (int lin = 0; lin < 3; lin++) {
            for (int col = 0; col < 3; col++) {
                if (tabuleiro[lin + linhaInicioCaixa][col + colunaInicioCaixa] == numero) {
                    return true;
                }
            }
        }
        return false;
    }

    // --- MÉTODOS GETTERS E SETTERS ---

    public int getNumero(int linha, int coluna) {
        return this.tabuleiro[linha][coluna];
    }

    public void setNumero(int linha, int coluna, int numero) {
        this.tabuleiro[linha][coluna] = numero;
    }

    public boolean isCelulaInicial(int linha, int coluna) {
        return this.celulasIniciais[linha][coluna];
    }
}