package com.gdsc.canigraduate.dto;

import com.gdsc.canigraduate.domain.user.lecture.UserLecture;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLectureDto {

    private String semester;
    private Integer majorCredit;
    private Integer cultureCredit;
    private Integer normalCredit;

    public UserLectureDto(UserLectureDto userLectureDto) {
        semester = userLectureDto.semester;
        majorCredit = userLectureDto.majorCredit;
        cultureCredit = userLectureDto.cultureCredit;
        normalCredit = userLectureDto.normalCredit;
    }

    @Builder
    public UserLectureDto(String semester, Integer majorCredit, Integer cultureCredit, Integer normalCredit) {
        this.semester = semester;
        this.majorCredit = majorCredit;
        this.cultureCredit = cultureCredit;
        this.normalCredit = normalCredit;
    }

    public UserLecture toEntity() {
        UserLecture userLecture = UserLecture.builder()
                .semester(semester)
                .majorCredit(majorCredit)
                .cultureCredit(cultureCredit)
                .normalCredit(normalCredit).build();
        return userLecture;
    }
}
