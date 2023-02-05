package com.studyclub.modules.study;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.studyclub.modules.account.QAccount;
import com.studyclub.modules.tag.QTag;
import com.studyclub.modules.zone.QZone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;


public class StudyRepositoryExtensionImpl extends QuerydslRepositorySupport implements StudyRepositoryExtension {

    public StudyRepositoryExtensionImpl() {
        super(Study.class);
    }

    //TODO 쿼리튜닝
    // 1. distinct빼고 result transform제공하는 방법
    // 2. 왜 데이터가 중복이 되는지
    // 3. 어떻게 N+1 문제를 Fetch join으로 해결했는
    @Override
    public Page<Study> findByKeyword(String keyword,Pageable pageable) {
        QStudy study = QStudy.study;
        JPQLQuery<Study> query = from(study)
                .where(
                        study.published.isTrue()
                                .and(study.title.containsIgnoreCase(keyword))
                                .or(study.tags.any().title.containsIgnoreCase(keyword))
                                .or(study.zones.any().localNameOfCity.containsIgnoreCase(keyword)))
                .leftJoin(study.tags, QTag.tag).fetchJoin()
                .leftJoin(study.zones, QZone.zone).fetchJoin()
                .leftJoin(study.members, QAccount.account).fetchJoin()
                .distinct();
        JPQLQuery<Study> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Study> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }
}
