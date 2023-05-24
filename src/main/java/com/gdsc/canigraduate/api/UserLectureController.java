package com.gdsc.canigraduate.api;

import com.gdsc.canigraduate.domain.user.User;
import com.gdsc.canigraduate.domain.user.lecture.UserLecture;
import com.gdsc.canigraduate.domain.user.lecture.UserLectureDetail;
import com.gdsc.canigraduate.dto.response.ResponseDto;
import com.gdsc.canigraduate.dto.excel.ExcelData;
import com.gdsc.canigraduate.dto.response.UserLectureDetailResponse;
import com.gdsc.canigraduate.dto.userLecture.UserLectureDetailDTO;
import com.gdsc.canigraduate.dto.response.UserLectureResponse;
import com.gdsc.canigraduate.service.user.UserService;
import com.gdsc.canigraduate.service.user.lecture.FileService;
import com.gdsc.canigraduate.service.user.lecture.UserLectureDetailService;
import com.gdsc.canigraduate.service.user.lecture.UserLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by im2sh
 */

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserLectureController {
    private final FileService fileService;
    private final UserService userService;
    private final UserLectureDetailService userLectureDetailService;
    private final UserLectureService userLectureService;

    @PostMapping("excel/read/{token}")
    public ResponseEntity<ResponseDto> readExcel(@PathVariable("token") String token, @RequestParam("file")MultipartFile file) throws IOException {
        User user = userService.findByToken(token);
        List<UserLectureDetailDTO> details = new ArrayList<>();

        if(user.isUpload() == true)
            return ResponseEntity.badRequest().body(new ResponseDto("이미 성적 정보가 업로드 되어 있습니다."));

        List<ExcelData> dataList = fileService.readExcel(file);
        for (ExcelData excelData : dataList) {
            UserLectureDetailDTO detailDTO = excelData.toDetailDTO(excelData);
            details.add(detailDTO);
        }
        userLectureService.dividedLecture(token, details);

        return ResponseEntity.ok().body(new ResponseDto("엑셀파일이 업로드 되었습니다"));
    }


    @GetMapping("/userLecture/{token}")
    public ResponseEntity<List<UserLectureResponse>> getUserLecture(@PathVariable("token")String token){
        Optional<User> one = Optional.ofNullable(userService.findByToken(token));
        User user = one.get();
        if(user == null){
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }
        List<UserLecture> userLectureList = userLectureService.findAllByUserId(user.getId());
        List<UserLectureResponse> userLectureResponseList = new ArrayList<>();
        for (UserLecture lecture : userLectureList) {
            UserLectureResponse response = lecture.toResponse();
            userLectureResponseList.add(response);
        }
         return ResponseEntity.ok().body(userLectureResponseList);
    }

    @GetMapping("/userLecture/{token}/{LectureID}")
    public ResponseEntity<List<UserLectureDetailResponse>> getDetail(@PathVariable("token")String token, @PathVariable("LectureID")Long lectureId){
        Optional<User> one = Optional.ofNullable(userService.findByToken(token));
        User user = one.get();
        if(user == null){
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }

        List<UserLectureDetail> detailList = userLectureDetailService.findAllByLectureID(lectureId);
        List<UserLectureDetailResponse> userLectureDetailResponseList = new ArrayList<>();
        for (UserLectureDetail detail : detailList) {
            UserLectureDetailResponse response = detail.toResponse();
            userLectureDetailResponseList.add(response);
        }
        return ResponseEntity.ok().body(userLectureDetailResponseList);
    }

    @PostMapping("/userLecture/delete/all/{token}")
    public ResponseEntity<ResponseDto> delete_userLecture(@PathVariable("token")String token){
        User user = userService.findByToken(token);
        if(!user.isUpload())
            return ResponseEntity.badRequest().body(new ResponseDto("성적 정보가 존재하지 않습니다."));
        userLectureService.lecture_delete(user);
        return ResponseEntity.ok().body(new ResponseDto("성적 정보가 삭제되었습니다."));
    }

    @PostMapping("/userLecture/delete/{id}")
    public ResponseEntity<ResponseDto> delete_one_userLecture(@PathVariable("id")Long id){
        if(!userLectureDetailService.delete_one(id))
            return ResponseEntity.badRequest().body(new ResponseDto("삭제 실패"));
        return ResponseEntity.ok().body(new ResponseDto("삭제 성공"));
    }

    @PostMapping("/userLecture/add/{token}")
    public ResponseEntity<ResponseDto> add_one_userLecture(@PathVariable("token")String token, @RequestBody UserLectureDetailDTO dto){
        Optional<User> one = Optional.ofNullable(userService.findByToken(token));
        User user = one.get();
        if(user == null){
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }
        if(!userLectureDetailService.findSameDetail(user,dto)) {
            return ResponseEntity.badRequest().body(new ResponseDto("이미 등록된 수업입니다."));
        }
        userLectureDetailService.add_one(dto,user);
        return ResponseEntity.ok().body(new ResponseDto("수업 추가"));
    }
}


