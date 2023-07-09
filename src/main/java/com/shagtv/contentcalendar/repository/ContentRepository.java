package com.shagtv.contentcalendar.repository;

import com.shagtv.contentcalendar.model.Content;
import com.shagtv.contentcalendar.model.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends CrudRepository<Content, Integer> {
    List<Content> findAllByTitleContains(String title);
    List<Content> findAllByStatus(@Param("status") Status status);
}
