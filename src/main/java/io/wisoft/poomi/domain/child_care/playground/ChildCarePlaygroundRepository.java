package io.wisoft.poomi.domain.child_care.playground;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChildCarePlaygroundRepository extends JpaRepository<ChildCarePlayground, Long> {

    @Query(value = "select * from Child_Care_Playground " +
            "where Child_Care_Playground.id in " +
            "(select playground_id from playground_search where address_tag_id = :searchTag)",
            nativeQuery = true)
    List<ChildCarePlayground> findBySearchTag(@Param("searchTag") final Long searchTagId);

}
