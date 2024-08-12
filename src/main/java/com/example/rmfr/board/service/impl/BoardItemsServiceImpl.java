package com.example.rmfr.board.service.impl;

import com.example.rmfr.board.dto.BoardItemsDto;
import com.example.rmfr.board.dto.ItemCommentsDto;
import com.example.rmfr.board.entity.BoardItems;
import com.example.rmfr.board.entity.item.ItemComments;
import com.example.rmfr.board.entity.item.ItemHeaders;
import com.example.rmfr.board.entity.item.ItemHits;
import com.example.rmfr.board.entity.item.ItemLikes;
import com.example.rmfr.board.repository.*;
import com.example.rmfr.board.service.BoardItemsService;
import com.example.rmfr.board.spec.BoardItemsSpecifications;
import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.entity.Members;
import com.example.rmfr.member.service.MemberService;
import com.example.rmfr.result.RestResults;
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
    private final ItemHitsRepository itemHitsRepository;
    private final ItemLikesRepository itemLikesRepository;
    private final ItemCommentsRepository itemCommentsRepository;
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
    public BoardItemsDto getBoardItem(int seq, MemberDto memberDto) {
        BoardItemsDto dto = new BoardItemsDto();

        try {
            Members member = memberDto == null ? null : new Members(memberDto);
            BoardItems item = boardItemsRepository.findByItemSeq(seq);

            if ( member != null ) {
                // 조회수 체크
                int hitChk = itemHitsRepository.countByItemUuidAndItemHitMemUuid(item, member).intValue();

                // 처음 조회 시 조회수 up
                if (hitChk == 0) {
                    ItemHits hit = new ItemHits();
                    hit.setItemUuid(item);
                    hit.setItemHitMemUuid(member);
                    itemHitsRepository.save(hit);
                    item = boardItemsRepository.findByItemSeq(seq);
                }
            }

            dto = new BoardItemsDto(item);
            boolean chk = false;

            if (member != null ) {
                // 등록자 본인이거나, 관리자 일 때
                chk = memberDto.getMemUuid().equals(dto.getItemRegUuid().getMemUuid()) || memberDto.getMemLevel() > 1;
                dto.setCAuth(true); // 댓글 권한, 회원이면 가능
            }

            dto.setEAuth(chk); // 수정 권한
            dto.setDAuth(chk); // 삭제 권한

            if ( chk ) {
                Long likes = itemLikesRepository.countByItemUuidAndItemLikerUuid(item.getItemUuid(), member);
                dto.setLikeItem(likes.intValue() > 0);
            }

            dto.setItemLikesCnt(itemLikesRepository.countByItemUuid(item.getItemUuid()).intValue());

        } catch(Exception e) {
            log.error(e.getMessage());
        }

        return dto;
    }

    @Override
    public Map<String, String> updateItem(BoardItemsDto boardItemsDto, MemberDto member) {
        Map<String, String> rst = new HashMap<>();

        try {
            Optional<BoardItems> asItem = boardItemsRepository.findById(boardItemsDto.getItemUuid());

            if ( asItem.isPresent() ) {
                BoardItems item = asItem.get();

                // itemHeader setting
                if (!"".equals(boardItemsDto.getItemHeaderId())) {
                    Optional<ItemHeaders> optionalItemHeaders = itemHeadersRepository.findById(boardItemsDto.getItemHeaderId());
                    optionalItemHeaders.ifPresent(boardItemsDto::setItemHeader);
                }

                // updater setting
                boardItemsDto.setItemUpdaterUuid(new Members(member));
                boardItemsDto.setItemUpdateDate(LocalDateTime.now());

                item.of(boardItemsDto);

                boardItemsRepository.save(item);

                rst.put("resultCode", "200");
            } else {
                rst.put("resultCode", "500");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            rst.put("resultCode", "500");
        }

        return rst;
    }

    @Override
    public RestResults addLike(String itemUuid, MemberDto memberDto) {
        RestResults rst = new RestResults();
        Map<String, Object> msg = new HashMap<>();

        try {
            Members member = new Members(memberDto);

            Long likes = itemLikesRepository.countByItemUuidAndItemLikerUuid(itemUuid, member);

            if ( likes == 0 ) {
                ItemLikes like = new ItemLikes();
                like.setItemUuid(itemUuid);
                like.setItemLikerUuid(member);
                itemLikesRepository.save(like);
                rst.setResultCode("200");
            }
            msg.put("itemLikes", itemLikesRepository.countByItemUuid(itemUuid));
            rst.getResultMessage().add(msg);

        } catch(Exception e) {
            log.error(e.getMessage());

            msg.put("error", e.getMessage());

            rst.setResultCode("500");
            rst.getResultMessage().add(msg);
        }
        return rst;
    }

    @Override
    public RestResults delLike(String itemUuid, MemberDto member) {
        RestResults rst = new RestResults();
        Map<String, Object> msg = new HashMap<>();

        try {
            itemLikesRepository.findByItemUuidAndItemLikerUuid(itemUuid, new Members(member)).ifPresent(itemLikesRepository::delete);

            msg.put("itemLikes", itemLikesRepository.countByItemUuid(itemUuid));

            rst.setResultCode("200");
            rst.getResultMessage().add(msg);

        } catch(Exception e) {
            log.error(e.getMessage());

            msg.put("error", e.getMessage());

            rst.setResultCode("500");
            rst.getResultMessage().add(msg);
        }
        return rst;
    }

    public RestResults delItem(BoardItemsDto boardItemsDto, MemberDto member) {
        RestResults rst = new RestResults();
        Map<String, Object> msg = new HashMap<>();

        try {
            BoardItems item = boardItemsRepository.findById(boardItemsDto.getItemUuid()).orElse(null);

            if ( item != null ) {
                item.setItemStatus(2);
                item.setItemUpdaterUuid(member);
                item.setItemUpdateDate(LocalDateTime.now());
                boardItemsRepository.save(item);
                rst.setResultCode("200");
            } else {
                rst.setResultCode("201");
            }
        } catch(Exception e) {
            log.error(e.getMessage());

            msg.put("error", e.getMessage());

            rst.setResultCode("500");
            rst.getResultMessage().add(msg);
        }
        return rst;
    }

    @Override
    public RestResults addReply(ItemCommentsDto itemCommentsDto, MemberDto member) {
        RestResults rst = new RestResults();
        Map<String, Object> msg = new HashMap<>();

        try {
            Members mem = new Members(member);

            itemCommentsDto.setCommentStatus(0);
            itemCommentsDto.setCommentRegUuid(mem);
            itemCommentsDto.setCommentUpdaterUuid(mem);

            ItemComments cmm = ItemComments.of(itemCommentsDto);

            // parentComment 가 있을 때, set 해주기
            if ( !"".equals(itemCommentsDto.getCommentParentUuid()) ) {
                String pUuid = itemCommentsDto.getCommentParentUuid();
                Optional<ItemComments> pCmm = itemCommentsRepository.findById(pUuid);
                pCmm.ifPresent(cmm::setCommentParentUuid);
            }

            itemCommentsRepository.save(cmm);

            rst.setResultCode("200");
        } catch(Exception e) {
            log.error(e.getMessage());

            msg.put("error", e.getMessage());

            rst.setResultCode("500");
            rst.getResultMessage().add(msg);
        }
        return rst;
    }

    @Override
    public List<ItemCommentsDto> getReplies(String itemUuid, String commentParentUuid, MemberDto member) {
        List<ItemCommentsDto> cmmDtos = new ArrayList<>();
        try {
            List<ItemComments> cmms = new ArrayList<>();

            if (! "0".equals(commentParentUuid) ) {
                Optional<ItemComments> pCmm = itemCommentsRepository.findById(commentParentUuid);
                if(pCmm.isPresent()) {
                    ItemComments parentComment = pCmm.get();
                    int depth = parentComment.getCommentDepth() + 1;
                    cmms = itemCommentsRepository.findByItemUuidAndCommentParentUuidAndCommentDepth(itemUuid, parentComment, depth);
                }
            }

            else cmms = itemCommentsRepository.findByItemUuid(itemUuid);


            for (ItemComments cmm : cmms) {
                ItemCommentsDto dto = ItemCommentsDto.of(cmm);
                if ( member != null ) {
                    // 로그인한 아이디로 좋아요를 누른 댓글 인지
                    Long likes = itemLikesRepository.countByItemUuidAndItemLikerUuid(cmm.getCommentUuid(), new Members(member));
                    boolean likeCmm = likes > 0;
                    dto.setLikeComment(likeCmm);

                    // 로그인한 아이디로 작성된 댓글 인지? 수정/삭제 권한
                    boolean regChk = authChk(member, dto.getCommentRegUuid().getMemUuid());
                    dto.setEAuth(regChk);
                    dto.setDAuth(regChk);
                }
                cmmDtos.add(dto);
            }


        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return cmmDtos;
    }

    @Override
    public RestResults delComment(String commentUuid, MemberDto member) {
        RestResults rst = new RestResults();
        Map<String, Object> msg = new HashMap<>();

        try {
            Optional<ItemComments> comm = itemCommentsRepository.findById(commentUuid);
            if ( comm.isPresent() ) {
                ItemComments comment = comm.get();
                if ( authChk(member, comment.getCommentRegUuid().getMemUuid()) ) {
                    comment.setCommentStatus(1);
                    comment.setCommentUpdaterUuid(new Members(member));
                    comment.setCommentUpdateDate(LocalDateTime.now());
                    itemCommentsRepository.save(comment);
                    rst.setResultCode("200");
                } else {
                    rst.setResultCode("402");
                    msg.put("message", "NOT AUTH.");
                }
            } else {
                rst.setResultCode("401");
                msg.put("message", "comment NOT FOUND.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            msg.put("error", e.getMessage());

            rst.setResultCode("500");
            rst.getResultMessage().add(msg);
        }
        return rst;
    }

    public boolean authChk(MemberDto memberDto, String memUuid) {
        return memberDto.getMemUuid().equals(memUuid) || memberDto.getMemLevel() > 1;
    }
}
