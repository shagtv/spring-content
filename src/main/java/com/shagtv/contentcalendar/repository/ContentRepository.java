package com.shagtv.contentcalendar.repository;

import com.shagtv.contentcalendar.model.Content;
import com.shagtv.contentcalendar.model.Status;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentRepository extends CrudRepository<Content, Integer> {
    List<Content> findAllByTitleContains(String title);
    @Query("""
        SELECT * FROM Content
        WHERE status = :status
    """)
    List<Content> listByStatus(@Param("status") Status status);
}
