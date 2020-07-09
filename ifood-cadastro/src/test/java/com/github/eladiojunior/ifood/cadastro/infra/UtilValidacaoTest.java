package com.github.eladiojunior.ifood.cadastro.infra;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class UtilValidacaoTest {

    @Test
    public void testIsCNPJValido() {
        String cnpjValido = "14572457000185";
        boolean result = true;
        Assert.assertEquals(UtilValidacao.of().isCnpjValido(cnpjValido), result);
    }

    @Test
    public void testIsCNPJValidoComMascara() {
        String cnpjValido = "14.572.457.0001/85";
        boolean result = true;
        Assert.assertEquals(UtilValidacao.of().isCnpjValido(cnpjValido), result);
    }

    @Test
    public void testIsCNPJInvalidoNumeroIguais() {
        String cnpjValido = "00000000000000";
        boolean result = false;
        Assert.assertEquals(UtilValidacao.of().isCnpjValido(cnpjValido), result);
    }

    @Test
    public void testIsCNPJInvalido() {
        String cnpjValido = "10572457000185";
        boolean result = false;
        Assert.assertEquals(UtilValidacao.of().isCnpjValido(cnpjValido), result);
    }

    @Test
    public void testIsCNPJInvalidoComMascara() {
        String cnpjValido = "10.572.457/0001-85";
        boolean result = false;
        Assert.assertEquals(UtilValidacao.of().isCnpjValido(cnpjValido), result);
    }

}
