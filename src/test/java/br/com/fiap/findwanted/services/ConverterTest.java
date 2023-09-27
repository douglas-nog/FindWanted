package br.com.fiap.findwanted.services;

import br.com.fiap.findwanted.entities.fbi.FBIWantedPersonFile;
import br.com.fiap.findwanted.entities.interpol.InterpolNotice;
import org.junit.jupiter.api.Test;

public class ConverterTest {


    @Test
    public void testGetDataFbi() {
        DataConverter converter = new DataConverter() {
            @Override
            public <T> T getData(String json, Class<T> tClass) {
                FBIWantedPersonFile fbiWantedPersonFile = new FBIWantedPersonFile();
                fbiWantedPersonFile.setName("BENJAMIN ATKINS");
                fbiWantedPersonFile.setUrl("https://www.fbi.gov/wanted/topten/benjamin-atkins");
                return (T) fbiWantedPersonFile;
            }
        };
        FBIWantedPersonFile fbiWantedPersonFile = converter.getData("{\"name\":\"BENJAMIN ATKINS\",\"url\":\"https://www.fbi.gov/wanted/topten/benjamin-atkins\"}", FBIWantedPersonFile.class);
        assert fbiWantedPersonFile.getName().equals("BENJAMIN ATKINS");
        assert fbiWantedPersonFile.getUrl().equals("https://www.fbi.gov/wanted/topten/benjamin-atkins");
    }

    @Test
    public void testGetDataInterpol() {
        DataConverter converter = new DataConverter() {
            @Override
            public <T> T getData(String json, Class<T> tClass) {
                InterpolNotice interpolNotice = new InterpolNotice();
                interpolNotice.setName("AARON");
                interpolNotice.setForename("BENJAMIN");
                interpolNotice.setDate_of_birth("1979-02-02");
                interpolNotice.setEntity_id("1");
                return (T) interpolNotice;
            }
        };
        InterpolNotice interpolNotice = converter.getData("{\"name\":\"AARON\",\"forename\":\"BENJAMIN\",\"date_of_birth\":\"1979-02-02\",\"entity_id\":\"1\"}", InterpolNotice.class);
        assert interpolNotice.getName().equals("AARON");
        assert interpolNotice.getForename().equals("BENJAMIN");
        assert interpolNotice.getDate_of_birth().equals("1979-02-02");
        assert interpolNotice.getEntity_id().equals("1");
    }


}
