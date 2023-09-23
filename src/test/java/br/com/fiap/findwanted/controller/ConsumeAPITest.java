package br.com.fiap.findwanted.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConsumeAPITest {

    @Test
    public void testGetDataFbi() {
        ConsumeAPI consumeAPI = new ConsumeAPI();
        String result = consumeAPI.getData("https://api.fbi.gov/wanted/v1/list?title=alberto");
        assert(result != null);
    }

    @Test
    public void testGetDataInterpol() {
        ConsumeAPI consumeAPI = new ConsumeAPI();
        String result = consumeAPI.getData("https://ws-public.interpol.int/notices/v1/red?name=alberto");
        assert(result != null);
    }
    @Test
    public void testGetData_InvalidURL() {
        ConsumeAPI api = new ConsumeAPI();
        String websitePath = "invalid-url"; // URL inválida
        assertThrows(RuntimeException.class, () -> {
            api.getData(websitePath);
        });
    }
}
