package com.gdsc.canigraduate.service.user.lecture;

import com.gdsc.canigraduate.domain.user.User;
import com.gdsc.canigraduate.domain.user.lecture.UserLecture;
import com.gdsc.canigraduate.domain.user.lecture.UserLectureDetail;
import com.gdsc.canigraduate.dto.userLecture.UserLectureDetailDTO;
import com.gdsc.canigraduate.repository.UserLectureDetailRepository;
import com.gdsc.canigraduate.repository.UserLectureRepository;
import com.gdsc.canigraduate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by im2sh
 */

@Service
@RequiredArgsConstructor
@Transactional
public class UserLectureDetailService {

    private final UserLectureDetailRepository userLectureDetailRepository;
    private final UserLectureService userLectureService;
    private final UserLectureRepository userLectureRepository;
    private final UserService userService;

    public Long save(UserLectureDetail detail){
        return userLectureDetailRepository.save(detail).getId();
    }

    public List<UserLectureDetail> findAllByLectureID(Long lectureId){
        return userLectureDetailRepository.findAllByUserLectureId(lectureId);
    }


    public void deleteByLectureDetail(UserLectureDetail userLectureDetail){
        userLectureDetailRepository.delete(userLectureDetail);
    }
    @Transactional
    public boolean delete_one(Long detailId){
        Optional<UserLectureDetail> detail = userLectureDetailRepository.findById(detailId);
        if(detail == null)
            return false;
        UserLectureDetail lectureDetail = detail.get();
        UserLecture userLecture = userLectureService.findByDetail(lectureDetail.getUserLecture().getId());
        userLecture.subCredit(lectureDetail.getCredit(), lectureDetail.getUserLectureType());
        Optional<User> one = userService.findOne(userLecture.getUser().getId());
        if(one == null)
            return false;
        User user = one.get();
        user.subCredit(lectureDetail.getCredit());
        user.subDetailCredit(lectureDetail.getCredit(), lectureDetail.getUserLectureType());
        if(userLecture.getMajorCredit() == 0 && userLecture.getCultureCredit() == 0 && userLecture.getNormalCredit() == 0) {
            userLectureRepository.delete(userLecture);
            user.subSemester(1);
        }
        deleteByLectureDetail(lectureDetail);
        user.calRestCredit();
        return true;
    }

    @Transactional
    public void add_one(UserLectureDetailDTO dto, User user){
        UserLectureDetail detail = dto.toEntity(dto);
        String s = detail.YearToString(detail.getLectureYear(), detail.getSemester());
        Integer credit = detail.getCredit();

        if(userLectureService.findLectureBySemesterAndUserId(s, user.getId()) == null){
            UserLecture userLecture = new UserLecture(user, s);
            userLecture.addUserLectureDetail(detail);
            detail.setUserLecture(userLecture);
            detail.setType(detail.getMajor());
            userLecture.addCredit(credit, detail.getUserLectureType());
            user.addSemester(1);
            save(detail);
        }
        else{
            UserLecture userLecture = userLectureService.findLectureBySemesterAndUserId(s, user.getId());
            userLecture.addUserLectureDetail(detail);
            detail.setUserLecture(userLecture);
            detail.setType(detail.getMajor());
            userLecture.addCredit(credit,detail.getUserLectureType());
            save(detail);
        }
        user.addCredit(credit);
        user.addDetailCredit(credit,detail.getUserLectureType());
        user.calRestCredit();
    }

    public boolean findSameDetail(User user, UserLectureDetailDTO dto){
        List<UserLecture> userLectureList = user.getUserLectureList();
        for (UserLecture userLecture : userLectureList) {
            List<UserLectureDetail> details = userLecture.getLectureDetails();
            for (UserLectureDetail detail : details) {
                if(detail.getLectureName().equals(dto.getLectureName())) {
                    return false;
                }
            }
        }
        return true;
    }
}
