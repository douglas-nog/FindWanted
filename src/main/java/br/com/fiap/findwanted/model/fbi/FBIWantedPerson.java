package br.com.fiap.findwanted.model.fbi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FBIWantedPerson {
    private String hair_raw;
    private String description;
    private int reward_max;
    private String person_classification;
    private String eyes;
    private String nationality;
    private List<String> possible_countries;
    private int weight_max;
    private String uid;
    private String race_raw;
    private String warning_message;
    private String sex;
    private List<FBIWantedPersonImage> images;
    private List<String> field_offices;
    private String weight;
    private String poster_classification;
    private List<Double> coordinates;
    private String age_range;
    private String scars_and_marks;
    private String details;
    private int height_max;
    private String path;
    private String caution;
    private String publication;
    private String complexion;
    private List<FBIWantedPersonFile> files;
    private String locations;
    private List<String> dates_of_birth_used;
    private String url;
    private String remarks;
    private int reward_min;
    private String modified;
    private String status;
    private List<String> aliases;
    private int height_min;
    private List<String> occupations;
    private String title;
    private List<String> legat_names;
    private int age_max;
    private int age_min;
    private String additional_information;
    private String race;
    private String reward_text;
    private String ncic;
    private List<String> languages;
    private List<String> subjects;
    private List<String> possible_states;
    private String place_of_birth;
    private String hair;
    private String suspects;
    private int weight_min;
    private String id;
}
