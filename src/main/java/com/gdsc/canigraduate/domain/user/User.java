package com.gdsc.canigraduate.domain.user;

import com.gdsc.canigraduate.domain.BaseEntity;
import com.gdsc.canigraduate.domain.user.lecture.UserLecture;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "users")
public class User extends BaseEntity {

    private String name;

    private String classId; //학번

    private String userPw;

    @Enumerated(EnumType.STRING)
    private Department department;

    private LocalDate admissionYear;

    private Integer graduationCredit;

    private Integer presentCredit;

    private Integer semester;

    private String token;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLecture> userLectureList = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Builder
    public User(String classId, String pw, String name) {
        this.classId = classId;
        this.userPw = pw;
        this.name = name;
        this.token = UUID.randomUUID().toString();
    }

    public void pwUpdate(String pw) {
        this.userPw = pw;
    }
}
