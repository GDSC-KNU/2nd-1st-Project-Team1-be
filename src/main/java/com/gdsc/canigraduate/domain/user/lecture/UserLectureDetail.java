package com.gdsc.canigraduate.domain.user.lecture;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLectureDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DETAIL_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LECTURE_ID")
    private UserLecture userLecture;

    private String lectureName;

    private Integer credit;

    @Enumerated(EnumType.STRING)
    private UserLectureType userLectureType;
}
