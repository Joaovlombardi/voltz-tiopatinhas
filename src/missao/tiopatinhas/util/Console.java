package missao.tiopatinhas.util;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

/**
 * Centraliza a leitura do teclado e a formatacao do que e exibido no console.
 * Todos os metodos de leitura repetem a pergunta ate receber um valor valido.
 */
public class Console {

    /** Maior valor aceito pelas colunas NUMBER(15,2) do banco. */
    public static final double LIMITE_VALOR = 9_999_999_999_999.99;

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Locale BRASIL = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Console() {
    }

    // ================= LEITURA =================

    public static String lerLinha(String rotulo) {
        System.out.print(rotulo);
        return SCANNER.nextLine().trim();
    }

    /** Le um numero inteiro entre minimo e maximo (usado nas opcoes de menu). */
    public static int lerOpcao(String rotulo, int minimo, int maximo) {

        while (true) {
            String entrada = lerLinha(rotulo);

            try {
                int opcao = Integer.parseInt(entrada);

                if (opcao >= minimo && opcao <= maximo) {
                    return opcao;
                }
            } catch (NumberFormatException e) {
                // cai na mensagem de erro abaixo
            }

            erro("Opcao invalida. Digite um numero entre " + minimo + " e " + maximo + ".");
        }
    }

    /** Le um ID (inteiro maior que zero). */
    public static int lerId(String rotulo) {

        while (true) {
            Integer id = converterInteiro(lerLinha(rotulo + ": "));

            if (id != null && id > 0) {
                return id;
            }

            erro("ID invalido. Digite um numero inteiro maior que zero.");
        }
    }

    /**
     * Le um ID permitindo 0 (cancelar). Se "atual" for informado,
     * ENTER devolve o valor atual.
     */
    public static int lerIdOuCancelar(String rotulo, Integer atual) {

        String sufixo = atual != null
                ? " [" + atual + "] (ENTER mantem, 0 cancela): "
                : " (0 cancela): ";

        while (true) {
            String entrada = lerLinha(rotulo + sufixo);

            if (entrada.isEmpty() && atual != null) {
                return atual;
            }

            Integer id = converterInteiro(entrada);

            if (id != null && id >= 0) {
                return id;
            }

            erro("ID invalido. Digite um numero inteiro.");
        }
    }

    /** Texto obrigatorio. Se "atual" for informado, ENTER mantem o valor atual. */
    public static String lerTexto(String rotulo, String atual, int tamanhoMaximo) {

        while (true) {
            String entrada = lerLinha(rotulo + sufixoAtual(atual) + ": ");

            if (entrada.isEmpty() && atual != null) {
                return atual;
            }

            if (entrada.isEmpty()) {
                erro("Campo obrigatorio.");
                continue;
            }

            if (excedeTamanho(entrada, tamanhoMaximo)) {
                erro("Texto muito longo. Maximo de " + tamanhoMaximo + " caracteres.");
                continue;
            }

            return entrada;
        }
    }

    /** Texto opcional. ENTER devolve o valor atual (ou null se nao houver). */
    public static String lerTextoOpcional(String rotulo, String atual, int tamanhoMaximo) {

        while (true) {
            String entrada = lerLinha(rotulo + " (opcional)" + sufixoAtual(atual) + ": ");

            if (entrada.isEmpty()) {
                return atual;
            }

            if (excedeTamanho(entrada, tamanhoMaximo)) {
                erro("Texto muito longo. Maximo de " + tamanhoMaximo + " caracteres.");
                continue;
            }

            return entrada;
        }
    }

    /** Valor em reais: maior que zero, com ate 2 casas decimais. */
    public static double lerValor(String rotulo, Double atual) {
        return lerDecimal(rotulo, atual, 0, false, 2);
    }

    /** Quantidade de criptomoeda: maior que zero, com ate 8 casas decimais. */
    public static double lerQuantidade(String rotulo, Double atual) {
        return lerDecimal(rotulo, atual, 0, false, 8);
    }

    /** Variacao percentual: pode ser negativa, mas nunca menor que -100%. */
    public static double lerVariacao(String rotulo, Double atual) {
        return lerDecimal(rotulo, atual, -100, true, 2);
    }

    public static boolean confirmar(String pergunta) {

        while (true) {
            String entrada = lerLinha(pergunta + " (S/N): ").toUpperCase();

            if (entrada.equals("S")) {
                return true;
            }

            if (entrada.equals("N")) {
                return false;
            }

            erro("Responda com S ou N.");
        }
    }

    private static double lerDecimal(
            String rotulo,
            Double atual,
            double minimo,
            boolean aceitaMinimo,
            int casasDecimais
    ) {

        String sufixo = atual != null
                ? " [" + decimal(atual) + "]"
                : "";

        while (true) {
            String entrada = lerLinha(rotulo + sufixo + ": ");

            if (entrada.isEmpty() && atual != null) {
                return atual;
            }

            // Aceita tanto 1234.56 quanto 1.234,56
            String normalizada = entrada.contains(",")
                    ? entrada.replace(".", "").replace(",", ".")
                    : entrada;

            if (!normalizada.matches("-?\\d+(\\.\\d+)?")) {
                erro("Numero invalido. Exemplo de formato aceito: 1500,75");
                continue;
            }

            int ponto = normalizada.indexOf('.');

            if (ponto >= 0 && normalizada.length() - ponto - 1 > casasDecimais) {
                erro("Use no maximo " + casasDecimais + " casas decimais.");
                continue;
            }

            double valor = Double.parseDouble(normalizada);

            if (valor < minimo || (!aceitaMinimo && valor == minimo)) {
                erro("O valor deve ser " + (aceitaMinimo ? "maior ou igual a " : "maior que ")
                        + decimal(minimo) + ".");
                continue;
            }

            if (valor > LIMITE_VALOR) {
                erro("Valor acima do limite aceito pelo sistema.");
                continue;
            }

            return valor;
        }
    }

    private static Integer converterInteiro(String entrada) {

        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // As colunas VARCHAR2 do Oracle sao medidas em bytes, e letras acentuadas ocupam mais de um.
    private static boolean excedeTamanho(String texto, int tamanhoMaximo) {
        return texto.getBytes(StandardCharsets.UTF_8).length > tamanhoMaximo;
    }

    private static String sufixoAtual(String atual) {
        return atual != null ? " [" + atual + "]" : "";
    }

    // ================= EXIBICAO =================

    public static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto.toUpperCase() + " ===");
    }

    public static void sucesso(String mensagem) {
        System.out.println("[OK] " + mensagem);
    }

    public static void erro(String mensagem) {
        System.out.println("[ERRO] " + mensagem);
    }

    public static void aviso(String mensagem) {
        System.out.println("[AVISO] " + mensagem);
    }

    public static String moeda(double valor) {
        return String.format(BRASIL, "R$ %,.2f", valor);
    }

    public static String percentual(double valor) {
        return String.format(BRASIL, "%+.2f%%", valor);
    }

    /** Numero sem zeros sobrando a direita (0.50000000 vira 0,5). */
    public static String decimal(double valor) {
        return BigDecimal.valueOf(valor)
                .stripTrailingZeros()
                .toPlainString()
                .replace('.', ',');
    }

    public static String data(LocalDateTime data) {
        return data != null ? data.format(FORMATO_DATA) : "-";
    }

    public static String texto(String valor) {
        return valor != null ? valor : "-";
    }
}
