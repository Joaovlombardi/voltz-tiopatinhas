package missao.tiopatinhas.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Regras de validacao dos dados digitados pelo usuario.
 */
public class Validador {

    public static final int TAMANHO_MINIMO_SENHA = 6;

    private Validador() {
    }

    public static String somenteDigitos(String texto) {
        return texto == null ? "" : texto.replaceAll("\\D", "");
    }

    /**
     * Valida um CPF ja sem pontuacao: 11 digitos, nao repetidos,
     * e com os dois digitos verificadores corretos.
     */
    public static boolean cpfValido(String cpf) {

        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }

        // 000.000.000-00, 111.111.111-11 etc. passam na conta, mas nao existem
        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        return digitoVerificador(cpf, 9) == cpf.charAt(9) - '0'
                && digitoVerificador(cpf, 10) == cpf.charAt(10) - '0';
    }

    private static int digitoVerificador(String cpf, int quantidadeDigitos) {

        int soma = 0;

        for (int i = 0; i < quantidadeDigitos; i++) {
            soma += (cpf.charAt(i) - '0') * (quantidadeDigitos + 1 - i);
        }

        int resto = soma % 11;

        return resto < 2 ? 0 : 11 - resto;
    }

    public static boolean emailValido(String email) {
        return email != null
                && email.matches("[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+");
    }

    /** Telefone com DDD: 10 digitos (fixo) ou 11 digitos (celular). */
    public static boolean telefoneValido(String telefone) {

        int digitos = somenteDigitos(telefone).length();

        return digitos == 10 || digitos == 11;
    }

    /** Simbolo de criptomoeda: de 2 a 10 letras ou numeros (ex.: BTC, ETH). */
    public static boolean simboloValido(String simbolo) {
        return simbolo != null
                && simbolo.matches("[A-Za-z0-9]{2,10}");
    }

    public static boolean senhaValida(String senha) {
        return senha != null
                && senha.length() >= TAMANHO_MINIMO_SENHA;
    }

    /** Gera o hash SHA-256 da senha, para que ela nunca seja gravada em texto puro. */
    public static String gerarHash(String senha) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));

            StringBuilder hash = new StringBuilder();

            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }

            return hash.toString();

        } catch (NoSuchAlgorithmException e) {
            // SHA-256 faz parte de toda instalacao Java
            throw new IllegalStateException(e);
        }
    }
}
