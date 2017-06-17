package com.besafx.app.entity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "courseSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "COURSE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "courseSequenceGenerator")
    @JsonView(Views.Summery.class)
    private Long id;

    @JsonView(Views.Summery.class)
    private Integer code;

    @JsonView(Views.Summery.class)
    private String instructor;

    @JsonView(Views.Summery.class)
    private String companyName;

    @Column(columnDefinition = "int default 1", nullable = false)
    @JsonView(Views.Summery.class)
    private Integer maxStudentCount;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(Views.Summery.class)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(Views.Summery.class)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "master")
    @JsonView(Views.Summery.class)
    @JsonIgnoreProperties(value = {"courses", "lastPerson", "lastUpdate"}, allowSetters = true)
    private Master master;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(Views.Summery.class)
    private Date lastUpdate;

    @ManyToOne
    @JoinColumn(name = "last_person")
    @JsonIgnoreProperties(value = {"branch"}, allowSetters = true)
    @JsonView(Views.Summery.class)
    private Person lastPerson;

    @OneToMany(mappedBy = "course")
    @JsonIgnoreProperties(value = {"course", "student", "lastPerson", "lastUpdate", "registerDate"}, allowSetters = true)
    private List<Account> accounts = new ArrayList<>();

    @JsonCreator
    public static Course Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Course course = mapper.readValue(jsonString, Course.class);
        return course;
    }

}
