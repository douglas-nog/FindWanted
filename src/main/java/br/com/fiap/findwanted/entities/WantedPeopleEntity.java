package br.com.fiap.findwanted.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_wanted_people")
@Data
public class WantedPeopleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 160, unique = true)
    private String name;
    @Column(length = 160)
    private String forename;
    @Column(length = 30)
    private LocalDate dateOfBirth;
    private String thumbnail;
    @Column(length = 9)
    private String governmetnalOrganization;
    @Column(length = 36)
    private String idFromSource;

}
