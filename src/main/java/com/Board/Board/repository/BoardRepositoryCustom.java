package com.Board.Board.repository;

import com.Board.Board.dto.SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepositoryCustom {

    Page<SearchForm> search(SearchForm searchForm, Pageable pageable);

}
