package vip.modal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
@Table(name = "project_detail")
@Entity
public class ProjectDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String fullName;
    private String userId;
    private String email;
    private String phoneNumber;
    private String Address;
    private Date date;
    private String projectName;
    private String projectType;
    private String projectSize;
    private String city;
    private String state;
    private String country;
}
