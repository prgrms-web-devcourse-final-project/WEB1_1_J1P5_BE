package org.j1p5.domain.comment.repository;

import org.j1p5.domain.comment.repository.querydsl.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentRepository,Long> , CommentRepositoryCustom {
}
