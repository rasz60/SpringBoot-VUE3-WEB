package com.example.rmfr.board.service;

import com.example.rmfr.board.dto.BoardItemsDto;
import com.example.rmfr.board.entity.item.ItemHeaders;
import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BoardItemsService {
    public Page<BoardItemsDto> getBoardList(int itemStatus, int page, int limit);

    public List<ItemHeaders> getItemHeaders(int auth);

    public Map<String, String> regItem(BoardItemsDto boardItemsDto, MemberDto member);

    public BoardItemsDto getBoardItem(int seq);
}
