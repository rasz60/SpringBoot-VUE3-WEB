package com.example.rmfr.board.service.impl;

import com.example.rmfr.board.dto.BoardItemsDto;
import com.example.rmfr.board.entity.BoardItems;
import com.example.rmfr.board.entity.item.ItemHeaders;
import com.example.rmfr.board.repository.BoardItemsRepository;
import com.example.rmfr.board.repository.ItemHeadersRepository;
import com.example.rmfr.board.service.BoardItemsService;
import com.example.rmfr.board.spec.BoardItemsSpecifications;
import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.entity.Members;
import com.example.rmfr.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardItemsServiceImpl implements BoardItemsService {

    private final BoardItemsRepository boardItemsRepository;
    private final ItemHeadersRepository itemHeadersRepository;
    @Override
    public Page<BoardItemsDto> getBoardList(int itemStatus, int page, int limit) {
        Page<BoardItemsDto> boardItemsDtos = null;
        try {
            Page<BoardItems> tmp = boardItemsRepository.findAll(BoardItemsSpecifications.withItemStatus(itemStatus), PageRequest.of(page, limit, Sort.by("itemRegDate").descending()));
            boardItemsDtos = tmp.map(BoardItemsDto::new);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return boardItemsDtos;
    }
    @Override
    public List<ItemHeaders> getItemHeaders(int auth) {
        List<ItemHeaders> headers = new ArrayList<>();
        try {
            headers = itemHeadersRepository.findByItemHeaderAuth(auth);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return headers;
    }

    @Override
    public Map<String, String> regItem(BoardItemsDto boardItemsDto, MemberDto memberDto) {
        Map<String, String> rst = new HashMap<>();

        try {
            Members member = new Members(memberDto);
            boardItemsDto.setItemRegUuid(member);
            boardItemsDto.setItemUpdaterUuid(member);
            boardItemsDto.setItemRegDate(LocalDateTime.now());
            boardItemsDto.setItemUpdateDate(LocalDateTime.now());
            if (!"".equals(boardItemsDto.getItemHeaderId())) {
                Optional<ItemHeaders> optionalItemHeaders = itemHeadersRepository.findById(boardItemsDto.getItemHeaderId());
                optionalItemHeaders.ifPresent(boardItemsDto::setItemHeader);
            }
            Long seq = boardItemsRepository.count();

            boardItemsDto.setItemSeq(seq.intValue() + 1);
            boardItemsRepository.save(new BoardItems(boardItemsDto));

            rst.put("resultCode", "200");
        } catch(Exception e) {
            log.error(e.getMessage());
            rst.put("resultCode", "500");
        }

        return rst;
    }

    @Override
    public BoardItemsDto getBoardItem(int seq) {
        BoardItemsDto dto = new BoardItemsDto();

        try {
            BoardItems item = boardItemsRepository.findByItemSeq(seq);
            dto = new BoardItemsDto(item);
        } catch(Exception e) {
            log.error(e.getMessage());
        }

        return dto;
    }
}
