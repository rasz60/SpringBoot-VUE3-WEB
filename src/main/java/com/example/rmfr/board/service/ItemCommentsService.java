package com.example.rmfr.board.service;

import com.example.rmfr.board.dto.ItemCommentsDto;
import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.entity.Members;
import com.example.rmfr.result.RestResults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemCommentsService {

    public List<ItemCommentsDto> getReplies(String itemUuid, String commentParentUuid, Members member);
    public RestResults addReply(ItemCommentsDto itemCommentsDto, Members member);
    public RestResults delComment(String commentUuid, Members member);
}
