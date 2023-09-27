package br.com.fiap.findwanted.controller;

import br.com.fiap.findwanted.entities.WantedPeopleEntity;
import br.com.fiap.findwanted.entities.fbi.FBIWantedList;
import br.com.fiap.findwanted.entities.fbi.FBIWantedPerson;
import br.com.fiap.findwanted.entities.fbi.FBIWantedPersonImage;
import br.com.fiap.findwanted.entities.interpol.*;
import br.com.fiap.findwanted.repository.WantedRepository;
import br.com.fiap.findwanted.services.WantedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControllerTest {

    @InjectMocks
    private Controller controller;

    @Mock
    private WantedRepository repository;

    @Mock
    private WantedService wantedService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetWantedDataFoundInDatabase() {
        String name = "John Doe";

        WantedPeopleEntity wantedPeopleEntity = new WantedPeopleEntity();
        wantedPeopleEntity.setGovernmetnalOrganization("FBI");
        wantedPeopleEntity.setDateOfBirth("01-01-1980");
        wantedPeopleEntity.setThumbnail("thumbnail_url");
        Optional<WantedPeopleEntity> wantedPeopleEntityOptional = Optional.of(wantedPeopleEntity);

        when(repository.findByName(name.toLowerCase())).thenReturn(wantedPeopleEntityOptional);

        ResponseEntity<String> responseEntity = controller.getWantedData(name);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Found in the wanted database! Wanted by FBI\nDate of birth: 01-01-1980\nTo see the photo visit: thumbnail_url", responseEntity.getBody());
    }

    @Test
    public void testGetWantedDataFoundInFbi() {
        String name = "John Doe";
        String encodedName = "john%20doe";

        InterpolWantedList apiResponseInterpol = new InterpolWantedList();
        apiResponseInterpol.setTotal(0);

        FBIWantedList apiResponseFbi = new FBIWantedList();
        FBIWantedPerson fbiItem = new FBIWantedPerson();
        fbiItem.setTitle("John Doe");
        FBIWantedPersonImage fbiImage = new FBIWantedPersonImage();
        fbiImage.setOriginal("thumbnail_url");
        fbiItem.setImages(Collections.singletonList(fbiImage));
        apiResponseFbi.setItems(Collections.singletonList(fbiItem));

        Optional<WantedPeopleEntity> wantedPeopleEntityOptional = Optional.empty();

        when(repository.findByName(name.toLowerCase())).thenReturn(wantedPeopleEntityOptional);
        when(wantedService.fetchInterpolData(encodedName)).thenReturn(apiResponseInterpol);
        when(wantedService.fetchFbiData(encodedName)).thenReturn(apiResponseFbi);
        when(wantedService.isInFbiList(apiResponseFbi, name.toLowerCase())).thenReturn(true);

        ResponseEntity<String> responseEntity = controller.getWantedData(name);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Found in the FBI database John Doe. To see the photo visit: thumbnail_url", responseEntity.getBody());

        verify(wantedService).fetchInterpolData(encodedName);
        verify(wantedService).fetchFbiData(encodedName);
        verify(wantedService).isInFbiList(apiResponseFbi, name.toLowerCase());
        verify(wantedService).createWantedEntityFromFbi(apiResponseFbi);
        verify(wantedService, never()).createWantedEntityFromInterpol(any());
    }

    @Test
    public void testGetWantedDataNotFound() {
        String name = "John Doe";
        String encodedName = "john%20doe";

        InterpolWantedList apiResponseInterpol = new InterpolWantedList();
        apiResponseInterpol.setTotal(0);

        FBIWantedList apiResponseFbi = new FBIWantedList();
        FBIWantedPerson fbiItem = new FBIWantedPerson();
        fbiItem.setTitle("Jane Smith");
        apiResponseFbi.setItems(Collections.singletonList(fbiItem));

        Optional<WantedPeopleEntity> wantedPeopleEntityOptional = Optional.empty();

        when(repository.findByName(name.toLowerCase())).thenReturn(wantedPeopleEntityOptional);
        when(wantedService.fetchInterpolData(encodedName)).thenReturn(apiResponseInterpol);
        when(wantedService.fetchFbiData(encodedName)).thenReturn(apiResponseFbi);
        when(wantedService.isInFbiList(apiResponseFbi, name.toLowerCase())).thenReturn(false);

        ResponseEntity<String> responseEntity = controller.getWantedData(name);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testGetAll_InvalidURL() {
        Controller controller = new Controller();
        String websitePath = "invalid-url"; // URL inválida
        assertThrows(RuntimeException.class, () -> {
            controller.getWantedData(websitePath);
        });
    }
}
