package com.example.rmfr.board.controller;

import com.example.rmfr.board.dto.BoardItemsDto;
import com.example.rmfr.board.entity.item.ItemHeaders;
import com.example.rmfr.board.service.BoardItemsService;
import com.example.rmfr.member.dto.MemberDto;
import com.example.rmfr.member.service.MemberService;
import com.example.rmfr.result.RestResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    public RestResults addLike(@RequestBody BoardItemsDto boardItemsDto, Principal principal) {
        RestResults rst = new RestResults();
        MemberDto member = null;
        try {
            if ( principal != null ) {
                member = memberService.getUserInfo(principal.getName());
            }
            rst = boardItemsService.addLike(boardItemsDto, member);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rst;
    }

    @DeleteMapping("/rest/item/delLike")
    @ResponseBody
    public RestResults delLike(@RequestBody BoardItemsDto boardItemsDto, Principal principal) {
        RestResults rst = new RestResults();
        MemberDto member = null;
        try {
            if ( principal != null ) {
                member = memberService.getUserInfo(principal.getName());
            }
            rst = boardItemsService.delLike(boardItemsDto, member);
        } catch (Exception e) {
            log.error(e.getMessage());
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
            }
            rst = boardItemsService.delItem(boardItemsDto, member);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rst;
    }
}
