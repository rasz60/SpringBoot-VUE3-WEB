package com.example.rmfr.board.service;

import com.example.rmfr.board.dto.BoardItemsDto;
import com.example.rmfr.board.dto.ItemCommentsDto;
import com.example.rmfr.board.entity.item.ItemComments;
import com.example.rmfr.board.entity.item.ItemHeaders;
import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.entity.Members;
import com.example.rmfr.result.RestResults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BoardItemsService {
    public RestResults getBoardList(int itemStatus, int page, int limit);

    public List<ItemHeaders> getItemHeaders(int auth);

    public RestResults regItem(BoardItemsDto boardItemsDto, Members member);

    public RestResults getBoardItem(int seq, Members memberDto);

    public RestResults updateItem(BoardItemsDto boardItemsDto, Members member);

    public RestResults delItem(BoardItemsDto boardItemsDto, Members member);
}
