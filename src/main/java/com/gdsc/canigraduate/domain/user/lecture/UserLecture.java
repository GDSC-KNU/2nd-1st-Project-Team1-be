package com.gdsc.canigraduate.domain.user.lecture;

import com.gdsc.canigraduate.domain.BaseEntity;
import com.gdsc.canigraduate.domain.user.User;
import com.gdsc.canigraduate.dto.response.UserLectureResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by im2sh
 */

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserLecture extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LECTURE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID")
    private User user;

    private String semester;
    private Integer majorCredit;
    private Integer cultureCredit;
    private Integer normalCredit;

    @OneToMany(mappedBy = "userLecture", cascade = CascadeType.ALL)
    private List<UserLectureDetail> lectureDetails = new ArrayList<>();


    public UserLecture(User user, String semester) {
        this.majorCredit = 0;
        this.cultureCredit = 0;
        this.normalCredit = 0;
        this.user = user;
        this.semester = semester;
    }

    public void YearToString(Integer year, String semester) {
        this.semester = year.toString() + semester;
    }

    public void addUserLectureDetail(UserLectureDetail userLectureDetail) {
        lectureDetails.add(userLectureDetail);
    }

    public void addCredit(Integer credit, UserLectureType type) {
        if (UserLectureType.전공.equals(type) || UserLectureType.공학전공.equals(type) || UserLectureType.전공기반.equals(type))
            this.majorCredit += credit;
        else if (UserLectureType.교양.equals(type))
            this.cultureCredit += credit;
        else if (UserLectureType.일반선택.equals(type))
            this.normalCredit += credit;
    }

    public void subCredit(Integer credit, UserLectureType type){
        if (UserLectureType.전공.equals(type) || UserLectureType.공학전공.equals(type) || UserLectureType.전공기반.equals(type))
            this.majorCredit -= credit;
        else if (UserLectureType.교양.equals(type))
            this.cultureCredit -= credit;
        else if (UserLectureType.일반선택.equals(type))
            this.normalCredit -= credit;
    }

    public UserLectureResponse toResponse(){
        String semester = this.semester;
        Integer major = this.majorCredit;
        Integer culture = this.cultureCredit;
        Integer normal = this.normalCredit;
        UserLectureResponse userLectureResponse = new UserLectureResponse(semester, major, culture, normal);
        return userLectureResponse;
    }

}
