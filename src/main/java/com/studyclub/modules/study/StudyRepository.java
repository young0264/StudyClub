package com.studyclub.modules.study;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
public interface StudyRepository extends JpaRepository<Study, Long>, StudyRepositoryExtension {
    boolean existsByPath(String path);

    @EntityGraph(attributePaths = {"tags", "managers", "zones", "members"})
    Study findByPath(String path);
    @EntityGraph(attributePaths = {"tags","managers"})
    Study findStudyWithTagsByPath(String path);

    @EntityGraph(attributePaths = {"zones","managers"})
    Study findStudyWithZonesByPath(String path);

    @EntityGraph(attributePaths = {"managers"})
    Study findStudyWithManagersByPath(String path);

    @EntityGraph(attributePaths = {"members"}) // FETCH가 기본
    Study findStudyWithMembersByPath(String path);

    Study findStudyOnlyByPath(String path);

    @EntityGraph(attributePaths = {"zones", "tags"})
    Study findStudyWithTagsAndZonesById(Long id);


    @EntityGraph(attributePaths = {"members","managers"})
    Study findStudyWithManagersAndMembersById(Long id);

    List<Study> findFirst9ByPublishedAndClosedOrderByPublishedDateTimeDesc(boolean published, boolean closed);

}
