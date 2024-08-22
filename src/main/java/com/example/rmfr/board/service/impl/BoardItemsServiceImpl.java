package com.example.rmfr.board.service.impl;

import com.example.rmfr.board.dto.BoardItemsDto;
import com.example.rmfr.board.entity.BoardItems;
import com.example.rmfr.board.entity.item.ItemHeaders;
import com.example.rmfr.board.entity.item.ItemHits;
import com.example.rmfr.board.repository.*;
import com.example.rmfr.board.service.BoardItemsService;
import com.example.rmfr.member.entity.Members;
import com.example.rmfr.result.RestResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardItemsServiceImpl implements BoardItemsService {

    private final BoardItemsRepository boardItemsRepository;
    private final ItemHeadersRepository itemHeadersRepository;
    private final ItemHitsRepository itemHitsRepository;
    private final ItemLikesRepository itemLikesRepository;

    @Override
    public RestResults getBoardList(int itemStatus, int page, int limit) {
        RestResults rst = new RestResults();
        Page<BoardItemsDto> boardItemsDtos = null;
        try {
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("itemRegDate").descending());
            Page<BoardItems> tmp = boardItemsRepository.findByItemStatus(itemStatus, pageRequest);
            boardItemsDtos = tmp.map(BoardItemsDto::new);
            rst.setResultCode(200);
            rst.setResult("boardList", boardItemsDtos);
        } catch (Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }
        return rst;
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
    public RestResults regItem(BoardItemsDto boardItemsDto, Members member) {
        RestResults rst = new RestResults();

        try {

            boardItemsDto.setItemRegUuid(member);
            boardItemsDto.setItemUpdaterUuid(member);

            if (!"".equals(boardItemsDto.getItemHeaderId())) {
                Optional<ItemHeaders> optionalItemHeaders = itemHeadersRepository.findById(boardItemsDto.getItemHeaderId());
                optionalItemHeaders.ifPresent(boardItemsDto::setItemHeader);
            }
            long seq = boardItemsRepository.count();

            boardItemsDto.setItemSeq((int) seq + 1);
            boardItemsRepository.save(BoardItems.of(boardItemsDto));

            rst.setResultCode(200);
        } catch(Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }

        return rst;
    }

    @Override
    public RestResults getBoardItem(int seq, Members member) {
        RestResults rst = new RestResults();
        try {
            Optional<BoardItems> optItem = boardItemsRepository.findByItemSeq(seq);
        
            if ( optItem.isPresent() ) {
                BoardItems item = optItem.get();
                String itemUuid = item.getItemUuid();
                if (member != null) {
                    // 조회수 체크
                    int hitChk = itemHitsRepository.countByItemUuidAndItemHitMemUuid(item, member).intValue();

                    // 처음 조회 시 조회수 up
                    if (hitChk == 0) {
                        ItemHits hit = new ItemHits();
                        hit.setItemUuid(item);
                        hit.setItemHitMemUuid(member);
                        itemHitsRepository.save(hit);
                        item = boardItemsRepository.findByItemUuid(itemUuid);
                    }
                }

                BoardItemsDto dto = new BoardItemsDto(item);
                boolean chk = false;

                if (member != null) {
                    // 등록자 본인이거나, 관리자 일 때
                    chk = member.getMemUuid().equals(dto.getItemRegUuid().getMemUuid()) || member.getMemLevel() > 1;
                    dto.setCAuth(true); // 댓글 권한, 회원이면 가능

                    // 좋아요 여부
                    Long likes = itemLikesRepository.countByItemUuidAndItemLikerUuid(item.getItemUuid(), member);
                    dto.setLikeItem(likes > 0);
                }

                dto.setEAuth(chk); // 수정 권한
                dto.setDAuth(chk); // 삭제 권한

                dto.setItemLikesCnt(itemLikesRepository.countByItemUuid(item.getItemUuid()).intValue());

                rst.setResultCode(200);
                rst.setResult("item", dto);
            } else {
               rst.setResultCode(400);
               rst.setResultMessage("읽어들일 게시물", 0);
            }
        } catch(Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }

        return rst;
    }

    @Override
    public RestResults updateItem(BoardItemsDto boardItemsDto, Members member) {
        RestResults rst = new RestResults();

        try {
            Optional<BoardItems> asItem = boardItemsRepository.findById(boardItemsDto.getItemUuid());

            if ( asItem.isPresent() ) {
                BoardItems item = asItem.get();

                // itemHeader setting
                if (boardItemsDto.getItemHeaderId() != null && !boardItemsDto.getItemHeaderId().isEmpty()) {
                    Optional<ItemHeaders> optionalItemHeaders = itemHeadersRepository.findById(boardItemsDto.getItemHeaderId());
                    optionalItemHeaders.ifPresent(boardItemsDto::setItemHeader);
                }

                // updater setting
                boardItemsDto.setItemUpdaterUuid(member);
                boardItemsDto.setItemUpdateDate(LocalDateTime.now());

                item.updateItems(boardItemsDto);
                boardItemsRepository.save(item);

                rst.setResultCode(200);
            } else {
                rst.setResultCode(400);
                rst.setResultMessage("수정할 게시물", 0);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }

        return rst;
    }

    public RestResults delItem(BoardItemsDto boardItemsDto, Members member) {
        RestResults rst = new RestResults();
        try {
            BoardItems item = boardItemsRepository.findById(boardItemsDto.getItemUuid()).orElse(null);

            if ( item != null ) {
                item.setItemStatus(2);
                item.setItemUpdaterUuid(member);
                item.setItemUpdateDate(LocalDateTime.now());
                boardItemsRepository.save(item);
                rst.setResultCode(200);
            } else {
                rst.setResultCode(400);
                rst.setResultMessage("삭제할 게시물", 0);
            }
        } catch(Exception e) {
            log.error(e.getMessage());
            rst.setResultCode(500);
        }
        return rst;
    }
}
