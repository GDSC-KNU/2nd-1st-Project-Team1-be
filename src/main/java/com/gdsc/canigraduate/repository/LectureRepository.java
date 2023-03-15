package com.gdsc.canigraduate.repository;

import com.gdsc.canigraduate.domain.user.lecture.UserLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<UserLecture, Long> {
}
