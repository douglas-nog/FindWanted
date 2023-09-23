package br.com.fiap.findwanted.service;

import br.com.fiap.findwanted.controller.ConsumeAPI;
import br.com.fiap.findwanted.model.WantedPeopleEntity;
import br.com.fiap.findwanted.model.fbi.FBIWantedList;
import br.com.fiap.findwanted.model.fbi.FBIWantedPerson;
import br.com.fiap.findwanted.model.fbi.FBIWantedPersonImage;
import br.com.fiap.findwanted.model.interpol.*;
import br.com.fiap.findwanted.repository.WantedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WantedServiceTest {

    @InjectMocks
    private WantedService wantedService;

    @Mock
    private WantedRepository repository;

    @Mock
    private Converter converter;

    @Mock
    private ConsumeAPI consumeAPI;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateWantedEntityFromFbi() {
        FBIWantedList apiResponseFbi = new FBIWantedList();
        FBIWantedPerson fbiItem = new FBIWantedPerson();
        fbiItem.setTitle("John Doe");
        fbiItem.setAliases(Collections.singletonList("Doe John"));
        FBIWantedPersonImage fbiImage = new FBIWantedPersonImage();
        fbiImage.setOriginal("thumbnail_url");
        fbiItem.setImages(Collections.singletonList(fbiImage));
        fbiItem.setDates_of_birth_used(Collections.singletonList("01-01-1980"));
        fbiItem.setUid("12345");
        apiResponseFbi.setItems(Collections.singletonList(fbiItem));

        wantedService.createWantedEntityFromFbi(apiResponseFbi);

        verify(repository).save(argThat(wantedPeopleEntity -> {
            assertEquals("john doe", wantedPeopleEntity.getName());
            assertEquals("doe john", wantedPeopleEntity.getForename());
            assertEquals("thumbnail_url", wantedPeopleEntity.getThumbnail());
            assertEquals("01-01-1980", wantedPeopleEntity.getDateOfBirth());
            assertEquals("FBI", wantedPeopleEntity.getGovernmetnalOrganization());
            assertEquals("12345", wantedPeopleEntity.getIdFromSource());
            return true;
        }));
    }

    @Test
    public void testCreateWantedEntityFromInterpol() {
        InterpolWantedList apiResponseInterpol = new InterpolWantedList();
        InterpolEmbedded embedded = new InterpolEmbedded();
        InterpolNotice notice = new InterpolNotice();
        InterpolLinks links = new InterpolLinks();
        InterpolLink thumbnail = new InterpolLink();
        thumbnail.setHref("thumbnail_url");
        links.setThumbnail(thumbnail);
        notice.set_links(links);
        notice.setName("John Doe");
        notice.setForename("Doe John");
        notice.setDate_of_birth("01-01-1980");
        notice.setEntity_id("12345");
        embedded.setNotices(Collections.singletonList(notice));
        apiResponseInterpol.set_embedded(embedded);

        wantedService.createWantedEntityFromInterpol(apiResponseInterpol);

        verify(repository).save(argThat(wantedPeopleEntity -> {
            assertEquals("john doe", wantedPeopleEntity.getName());
            assertEquals("doe john", wantedPeopleEntity.getForename());
            assertEquals("thumbnail_url", wantedPeopleEntity.getThumbnail());
            assertEquals("01-01-1980", wantedPeopleEntity.getDateOfBirth());
            assertEquals("Interpol", wantedPeopleEntity.getGovernmetnalOrganization());
            assertEquals("12345", wantedPeopleEntity.getIdFromSource());
            return true;
        }));
    }

    @Test
    public void testFetchInterpolData() {
        String jsonInterpol = "{\"_embedded\":{\"notices\":[{\"name\":\"John Doe\",\"forename\":\"Doe John\",\"date_of_birth\":\"01-01-1980\",\"entity_id\":\"12345\",\"_links\":{\"thumbnail\":{\"href\":\"thumbnail_url\"}}}]}";
        InterpolWantedList apiResponseInterpol = new InterpolWantedList();
        InterpolEmbedded embedded = new InterpolEmbedded();
        InterpolNotice notice = new InterpolNotice();
        InterpolLinks links = new InterpolLinks();
        InterpolLink thumbnail = new InterpolLink();
        thumbnail.setHref("thumbnail_url");
        links.setThumbnail(thumbnail);
        notice.set_links(links);
        notice.setName("John Doe");
        notice.setForename("Doe John");
        notice.setDate_of_birth("01-01-1980");
        notice.setEntity_id("12345");
        embedded.setNotices(Collections.singletonList(notice));
        apiResponseInterpol.set_embedded(embedded);

        when(consumeAPI.getData(anyString())).thenReturn(jsonInterpol);
        when(converter.getData(jsonInterpol, InterpolWantedList.class)).thenReturn(apiResponseInterpol);

        InterpolWantedList result = wantedService.fetchInterpolData("encodedName");

        assertNotNull(result);
        assertEquals(1, result.get_embedded().getNotices().size());
        assertEquals("John Doe", result.get_embedded().getNotices().get(0).getName());
    }

    @Test
    public void testFetchFbiData() {
        String jsonFbi = "{\"items\":[{\"title\":\"John Doe\",\"aliases\":[\"Doe John\"],\"images\":[{\"original\":\"thumbnail_url\"}],\"dates_of_birth_used\":[\"01-01-1980\"],\"uid\":\"12345\"}]}";
        FBIWantedList apiResponseFbi = new FBIWantedList();
        FBIWantedPerson fbiItem = new FBIWantedPerson();
        fbiItem.setTitle("John Doe");
        fbiItem.setAliases(Collections.singletonList("Doe John"));
        FBIWantedPersonImage fbiImage = new FBIWantedPersonImage();
        fbiItem.setImages(Collections.singletonList(fbiImage));
        fbiItem.setDates_of_birth_used(Collections.singletonList("01-01-1980"));
        fbiItem.setUid("12345");
        apiResponseFbi.setItems(Collections.singletonList(fbiItem));

        when(consumeAPI.getData(anyString())).thenReturn(jsonFbi);
        when(converter.getData(jsonFbi, FBIWantedList.class)).thenReturn(apiResponseFbi);

        FBIWantedList result = wantedService.fetchFbiData("encodedName");

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("John Doe", result.getItems().get(0).getTitle());
    }

    @Test
    public void testIsInFbiListWhenFound() {
        FBIWantedList apiResponseFbi = new FBIWantedList();
        FBIWantedPerson fbiItem = new FBIWantedPerson();
        fbiItem.setTitle("John Doe");
        apiResponseFbi.setItems(Collections.singletonList(fbiItem));

        boolean isInFbiList = wantedService.isInFbiList(apiResponseFbi, "John Doe");

        assertTrue(isInFbiList);
    }

    @Test
    public void testGetStringResponseEntityInterpolWithoutThumbnail() {
        InterpolWantedList apiResponseInterpol = new InterpolWantedList();
        InterpolEmbedded embedded = new InterpolEmbedded();
        InterpolNotice notice = new InterpolNotice();
        InterpolLinks links = new InterpolLinks();
        links.setThumbnail(null);
        notice.set_links(links);
        embedded.setNotices(Collections.singletonList(notice));
        apiResponseInterpol.set_embedded(embedded);

        ResponseEntity<String> responseEntity = WantedService.getStringResponseEntityInterpol("John Doe", apiResponseInterpol, "Interpol");

        assertNotNull(responseEntity);
        assertEquals("Found in the Interpol database John Doe. Photo not available.", responseEntity.getBody());
    }

    @Test
    public void testGetStringResponseEntityFbiWithThumbnail() {
        FBIWantedList apiResponseFbi = new FBIWantedList();
        FBIWantedPerson fbiItem = new FBIWantedPerson();
        fbiItem.setTitle("John Doe");
        FBIWantedPersonImage fbiImage = new FBIWantedPersonImage();
        fbiImage.setOriginal("thumbnail_url");
        fbiItem.setImages(Collections.singletonList(fbiImage));
        fbiItem.setImages(Collections.singletonList(fbiImage));
        apiResponseFbi.setItems(Collections.singletonList(fbiItem));

        ResponseEntity<String> responseEntity = WantedService.getStringResponseEntityFbi("John Doe", apiResponseFbi, "FBI");

        assertNotNull(responseEntity);
        assertEquals("Found in the FBI database John Doe. To see the photo visit: thumbnail_url", responseEntity.getBody());
    }

    @Test
    public void testGetStringResponseEntityFbiWithoutThumbnail() {
        FBIWantedList apiResponseFbi = new FBIWantedList();
        FBIWantedPerson fbiItem = new FBIWantedPerson();
        fbiItem.setTitle("John Doe");
        FBIWantedPersonImage fbiImage = new FBIWantedPersonImage();
        fbiItem.setImages(Collections.singletonList(fbiImage));
        apiResponseFbi.setItems(Collections.singletonList(fbiItem));

        ResponseEntity<String> responseEntity = WantedService.getStringResponseEntityFbi("John Doe", apiResponseFbi, "FBI");

        assertNotNull(responseEntity);
        assertEquals("Found in the FBI database John Doe. Photo not available.", responseEntity.getBody());
    }

    @Test
    public void testGetStringResponseEntityDb() {
        WantedPeopleEntity wantedPeopleEntity = new WantedPeopleEntity();
        wantedPeopleEntity.setGovernmetnalOrganization("FBI");
        wantedPeopleEntity.setDateOfBirth("01-01-1980");
        wantedPeopleEntity.setThumbnail("thumbnail_url");

        ResponseEntity<String> responseEntity = WantedService.getStringResponseEntityDb(wantedPeopleEntity);

        assertNotNull(responseEntity);
        assertEquals("Found in the wanted database! Wanted by FBI\nDate of birth: 01-01-1980\nTo see the photo visit: thumbnail_url", responseEntity.getBody());
    }
}
