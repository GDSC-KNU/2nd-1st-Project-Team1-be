package com.gdsc.canigraduate.domain.user;

import com.gdsc.canigraduate.domain.BaseEntity;
import com.gdsc.canigraduate.domain.BooleanToYNConverter;
import com.gdsc.canigraduate.domain.Department;
import com.gdsc.canigraduate.domain.user.lecture.UserLecture;
import com.gdsc.canigraduate.domain.user.lecture.UserLectureType;
import com.gdsc.canigraduate.dto.user.UserCreditResponse;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by im2sh
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "users")
public class User extends BaseEntity {

    private String name;

    private String classId; //학번

    private String userPw;

    @Enumerated(EnumType.STRING)
    private Department department;

    private Integer admissionYear;

    private Integer graduationCredit;

    private Integer presentCredit;

    private Integer semester;

    private String token;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean isUpload;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean isCreditGraduation; // 졸업학점을 넘기는 지

    @Convert(converter = BooleanToYNConverter.class)
    private boolean isConditionGraduation; // 졸업 요건을 만족하는 지

    private Integer majorCredit;
    private Integer cultureCredit;
    private Integer normalCredit;
    private Integer basicMajorCredit;
    private Integer techMajorCredit;

    private Integer restMajor;
    private Integer restCulture;
    private Integer restNormal;
    private Integer restBasicMajor;
    private Integer restTechMajor;
    private Integer restPresent;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLecture> userLectureList = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "GRADUATION_ID")
    private UserGraduationInfo userGraduationInfo;
    @Builder
    public User(String classId, String userPw, String name, Department department, Integer admissionYear) {
        this.classId = classId;
        this.userPw = userPw;
        this.name = name;
        this.token = UUID.randomUUID().toString();
        this.department = department;
        this.admissionYear = admissionYear;
        this.semester = 0;
        this.majorCredit = 0;
        this.cultureCredit = 0;
        this.normalCredit = 0;
        this.basicMajorCredit = 0;
        this.techMajorCredit = 0;
        this.presentCredit = 0;
        this.restMajor = 0;
        this.restCulture = 0;
        this.restNormal = 0;
        this.restBasicMajor = 0;
        this.restTechMajor = 0;
        this.restPresent = 0;
    }

    public void pwUpdate(String pw) {
        this.userPw = pw;
    }

    public void setProfile(Integer credit, Integer semester) {
        this.presentCredit = credit;
        this.semester = semester;
    }

    public void addLecture(UserLecture userLecture){
        this.userLectureList.add(userLecture);
    }


    public void setDepartment(Department department) {
        if (department.equals("심화컴퓨터공학전공"))
            this.department = department;
        else if (department.equals("글로벌SW융합전공"))
            this.department = department;
    }

    public void userUpload(boolean yn){
        if(yn == true)
            this.isUpload = true;
        else
            this.isUpload = false;
    }

    public void subCredit(Integer credit){
        this.presentCredit -= credit;
    }
    public void addCredit(Integer credit){
        this.presentCredit += credit;
    }

    public void addSemester(Integer semester){
        this.semester += semester;
    }

    public void subSemester(Integer semester){
        this.semester -= semester;
    }

    public void addDetailCredit(Integer credit, UserLectureType type){
        if(UserLectureType.전공.equals(type))
            this.majorCredit += credit;
        else if(UserLectureType.전공기반.equals(type))
            this.basicMajorCredit += credit;
        else if(UserLectureType.공학전공.equals(type))
            this.techMajorCredit += credit;
        else if(UserLectureType.교양.equals(type))
            this.cultureCredit += credit;
        else if(UserLectureType.일반선택.equals(type))
            this.normalCredit += credit;
    }

    public void subDetailCredit(Integer credit, UserLectureType type){
        if(UserLectureType.전공.equals(type))
            this.majorCredit -= credit;
        else if(UserLectureType.전공기반.equals(type))
            this.basicMajorCredit -= credit;
        else if(UserLectureType.공학전공.equals(type))
            this.techMajorCredit -= credit;
        else if(UserLectureType.교양.equals(type))
            this.cultureCredit -= credit;
        else if(UserLectureType.일반선택.equals(type))
            this.normalCredit -= credit;
    }

    public void userGraduation(boolean yn){
        if(yn == true)
            this.isCreditGraduation = true;
        else
            this.isCreditGraduation = false;
    }
    public UserLectureType globalSoft(UserLectureType userLectureType){
        if(userLectureType.equals("전공"))
            return UserLectureType.전공;
        else if(userLectureType.equals("교양"))
            return UserLectureType.교양;
        else if(userLectureType.equals("일반선택"))
            return UserLectureType.일반선택;
        return null;
    }

    public UserLectureType deepComputer(UserLectureType userLectureType){
        if(userLectureType.equals("전공기반"))
            return UserLectureType.전공기반;
        else if(userLectureType.equals("공학전공"))
            return UserLectureType.공학전공;
        else if(userLectureType.equals("교양"))
            return UserLectureType.교양;
        return null;
    }
    public void InitCredit() {
        this.majorCredit = 0;
        this.cultureCredit = 0;
        this.normalCredit = 0;
        this.basicMajorCredit = 0;
        this.techMajorCredit = 0;

        this.restMajor = 0;
        this.restCulture = 0;
        this.restNormal = 0;
        this.restBasicMajor = 0;
        this.restTechMajor = 0;
        this.restPresent = 0;
    }

    public void calRestCredit(){
        if(getDepartment().equals(Department.글로벌SW융합전공)){
            this.restMajor = 51 - getMajorCredit();
            this.restPresent = 130 - getPresentCredit();
            if(getAdmissionYear() == 2017 || getAdmissionYear() >= 2023)
                this.restCulture = 30 - getCultureCredit();
            else
                this.restCulture = 24 - getCultureCredit();
        }
        else if(getDepartment().equals(Department.심화컴퓨터공학)) {
            if (getAdmissionYear() < 2021) { // basic 21 tech 75 culture 15 졸업학점 150
                this.restCulture = 15 - getCultureCredit();
                this.restBasicMajor = 21 - getBasicMajorCredit();
                this.restTechMajor = 75 - getTechMajorCredit();
                this.restPresent = 150 - getPresentCredit();

            } else if (getAdmissionYear() >= 2021) { // basic 18 tech 60 culture 15 졸업학점 140
                this.restCulture = 15 - getCultureCredit();
                this.restBasicMajor = 18 - getBasicMajorCredit();
                this.restTechMajor = 60 - getTechMajorCredit();
                this.restPresent = 140 - getPresentCredit();
            }
        }

        checkRestCredit();
    }
    public UserCreditResponse toCreditResponse(){
        Integer majorCredit = getMajorCredit();
        Integer cultureCredit = getCultureCredit();
        Integer normalCredit = getNormalCredit();
        Integer basicMajorCredit = getBasicMajorCredit();
        Integer techMajorCredit = getTechMajorCredit();
        Integer presentCredit = getPresentCredit();

        return new UserCreditResponse(majorCredit,cultureCredit,normalCredit,basicMajorCredit,techMajorCredit, presentCredit, getRestMajor(),getRestCulture(),getRestNormal(),getRestBasicMajor(),getRestTechMajor(),getRestPresent());
    }

    public void checkRestCredit(){
        if(this.restMajor < 0)
            this.restMajor = 0;
        if(this.restCulture < 0)
            this.restCulture = 0;
        if(this.restNormal < 0)
            this.restNormal = 0;
        if(this.restBasicMajor < 0)
            this.restBasicMajor = 0;
        if(this.restTechMajor < 0)
            this.restTechMajor = 0;
        if(this.presentCredit < 0)
            this.presentCredit = 0;
    }

    public void setUserGraduationInfo(UserGraduationInfo info){
        this.userGraduationInfo = info;
    }

    public void setIsConditionGradation(boolean yn){
        if(yn == true)
            this.isConditionGraduation = true;
        else
            this.isConditionGraduation = false;
    }
}