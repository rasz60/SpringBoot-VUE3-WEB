package com.example.rmfr.board.controller;

import com.example.rmfr.board.dto.BoardItemsDto;
import com.example.rmfr.board.dto.ItemCommentsDto;
import com.example.rmfr.board.entity.item.ItemComments;
import com.example.rmfr.board.entity.item.ItemHeaders;
import com.example.rmfr.board.service.BoardItemsService;
import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.service.MemberService;
import com.example.rmfr.result.RestResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardRestController {

    private final BoardItemsService boardItemsService;
    private final MemberService memberService;
    @GetMapping("/rest/itemHeaders")
    public List<ItemHeaders> itemHeaders(Principal principal) {
        int auth = 1;
        if ( principal != null )
            auth = memberService.getUserInfo(principal.getName()).getMemLevel();

        return boardItemsService.getItemHeaders(auth);
    }

    @PostMapping("/rest/board/save")
    @ResponseBody
    public Map<String, String> saveItem(@RequestBody BoardItemsDto boardItemsDto, Principal principal) {
        Map<String, String> rst = new HashMap<>();
        try {
            if ( principal != null ) {
                MemberDto member = memberService.getUserInfo(principal.getName());

                rst = "".equals(boardItemsDto.getItemUuid()) ?
                        boardItemsService.regItem(boardItemsDto, member) :
                        boardItemsService.updateItem(boardItemsDto, member);
            }
            else {
                rst.put("resultCode", "400");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rst;
    }

    @GetMapping("/rest/boardList/{itemStatus}/{page}/{limit}")
    public Page<BoardItemsDto> getBoardList(@PathVariable(value = "itemStatus") int itemStatus,
                                            @PathVariable(value = "page") int page,
                                            @PathVariable("limit") int limit) {
        Page<BoardItemsDto> dtos = null;

        try {
            dtos = boardItemsService.getBoardList(itemStatus, page, limit);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return dtos;
    }

    @GetMapping("/rest/item/{seq}")
    public BoardItemsDto getBoardItem(@PathVariable(value = "seq") int seq, Principal principal) {
        BoardItemsDto dto = new BoardItemsDto();
        MemberDto member = null;
        try {
            if ( principal != null ) {
                member = memberService.getUserInfo(principal.getName());
            }
            dto = boardItemsService.getBoardItem(seq, member);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return dto;
    }

    @PostMapping("/rest/item/addLike")
    @ResponseBody
    public RestResults addLike(@RequestBody String itemUuid, Principal principal) {
        RestResults rst = new RestResults();
        MemberDto member = null;
        try {
            if ( principal != null ) {
                member = memberService.getUserInfo(principal.getName());
                rst = boardItemsService.addLike(itemUuid, member);
            } else {
                rst.setResultCode("400");
            }
        } catch (Exception e) {
            rst.setResultCode("500");
            log.error(e.getMessage());
        }
        return rst;
    }

    @DeleteMapping("/rest/item/delLike")
    @ResponseBody
    public RestResults delLike(@RequestBody String itemUuid, Principal principal) {
        RestResults rst = new RestResults();
        MemberDto member = null;
        try {
            if ( principal != null ) {
                member = memberService.getUserInfo(principal.getName());
                rst = boardItemsService.delLike(itemUuid, member);
            } else {
                rst.setResultCode("400");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            rst.setResultCode("500");
        }
        return rst;
    }

    @PutMapping("/rest/delItem")
    @ResponseBody
    public RestResults delItem(@RequestBody BoardItemsDto boardItemsDto, Principal principal) {
        RestResults rst = new RestResults();
        MemberDto member = null;
        try {
            if ( principal != null ) {
                member = memberService.getUserInfo(principal.getName());
                rst = boardItemsService.delItem(boardItemsDto, member);
            } else {
                rst.setResultCode("400");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rst;
    }

    @PostMapping("/rest/addReply")
    @ResponseBody
    public RestResults addReply(@RequestBody ItemCommentsDto itemCommentsDto,Principal principal) {
        RestResults rst = new RestResults();
        MemberDto member = null;
        try {
            if ( principal != null ) {
                member = memberService.getUserInfo(principal.getName());
                rst = boardItemsService.addReply(itemCommentsDto, member);
            } else {
                rst.setResultCode("400");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rst;
    }

    @GetMapping("/rest/getReplies/{itemUuid}/{commentParentUuid}/{depth}")
    public RestResults getReplies(@PathVariable("itemUuid") String itemUuid,
                                  @PathVariable(value = "commentParentUuid", required = false) String commentParentUuid,
                                  Principal principal) {
        RestResults rst = new RestResults();
        Map<String, Object> msg = new HashMap<>();
        MemberDto member = null;
        try {
            if ( principal != null ) {
                member = memberService.getUserInfo(principal.getName());
            }
            msg.put("result", boardItemsService.getReplies(itemUuid, commentParentUuid, member));
            rst.setResultCode("200");
            rst.getResultMessage().add(msg);
        } catch (Exception e) {
            log.error(e.getMessage());
            msg.put("error", e.getMessage());

            rst.setResultCode("500");
            rst.getResultMessage().add(msg);
        }
        return rst;
    }

    @PutMapping("/rest/delComment")
    @ResponseBody
    public RestResults delComment(@RequestBody String commentUuid, Principal principal) {
        RestResults rst = new RestResults();
        Map<String, Object> msg = new HashMap<>();
        MemberDto member = null;
        try {
            if ( principal != null ) {
                member = memberService.getUserInfo(principal.getName());
                rst = boardItemsService.delComment(commentUuid, member);
            } else {
                rst.setResultCode("400");
                msg.put("message", "NOT FOUND LOGIN USER.");
            }
        } catch(Exception e) {
            log.error(e.getMessage());
            msg.put("error", e.getMessage());

            rst.setResultCode("500");
            rst.getResultMessage().add(msg);
        }

        return rst;
    }
}
