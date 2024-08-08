package com.example.rmfr.board.repository;

import com.example.rmfr.board.entity.item.ItemComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCommentsRepository extends JpaRepository<ItemComments, Long> {
}
