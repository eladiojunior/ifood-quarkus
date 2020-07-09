package com.github.eladiojunior.ifood.cadastro.infra;

public class UtilValidacao {

    private UtilValidacao() {
    }

    public static UtilValidacao of() {
        return new UtilValidacao();
    }

    /**
     * Verifica se o CNPJ informado é válido, segundo as regras de calculo do dígito verificador.
     *
     * @param cnpj - Número do CNPJ para verificação.
     * @return
     */
    public boolean isCnpjValido(String cnpj) {

        if (cnpj == null || cnpj.isEmpty())
            return false;

        //Remover tudo que não for número. Retirar máscara.
        cnpj = cnpj.trim().replaceAll("[^0-9]", "");

        //Considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") ||
                cnpj.equals("22222222222222") || cnpj.equals("33333333333333") ||
                cnpj.equals("44444444444444") || cnpj.equals("55555555555555") ||
                cnpj.equals("66666666666666") || cnpj.equals("77777777777777") ||
                cnpj.equals("88888888888888") || cnpj.equals("99999999999999") ||
                (cnpj.length() != 14)) {
            return false;
        }

        char dig13, dig14;
        int sm, i, r, num, peso;
        // Calculo do 1o. Digito Verificador
        sm = 0;
        peso = 2;
        for (i = 11; i >= 0; i--) {
            //Converte o i-ésimo caractere do CNPJ em um número:
            //Por exemplo, transforma o caractere '0' no inteiro 0 (48 eh a posição de '0' na tabela ASCII)
            num = (int) (cnpj.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso + 1;
            if (peso == 10)
                peso = 2;
        }

        r = sm % 11;
        if ((r == 0) || (r == 1))
            dig13 = '0';
        else dig13 = (char) ((11 - r) + 48);

        // Calculo do 2o. Digito Verificador
        sm = 0;
        peso = 2;
        for (i = 12; i >= 0; i--) {
            num = (int) (cnpj.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso + 1;
            if (peso == 10)
                peso = 2;
        }

        r = sm % 11;
        if ((r == 0) || (r == 1))
            dig14 = '0';
        else dig14 = (char) ((11 - r) + 48);

        // Verifica se os dígitos calculados conferem com os dígitos informados.
        return (dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13));

    }

}
