package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.Article;
import com.khumu.alimi.data.entity.SimpleKhumuUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhumuUserRepository extends JpaRepository<SimpleKhumuUser, String> {
}
