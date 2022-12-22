package com.codewarts.noriter.article.repository;

import com.codewarts.noriter.article.domain.Study;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyRepository extends JpaRepository<Study, Long> {

    @Query("select s from Study s where s.completed = :completed")
    List<Study> findStudyByCompleted(@Param("completed") boolean completed);

    @Query("select s from Study s where s.id =:id")
    Optional<Study> findByStudyId(@Param("id") Long id);

    @Query("select s from Study s where s.id =:id and s.writer.id =:writerId")
    Optional<Study> findByStudyIdAndWriterId(@Param("id") Long id, @Param("writerId") Long writerId);
}