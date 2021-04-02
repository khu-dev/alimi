package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.Article;
import com.khumu.alimi.data.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
