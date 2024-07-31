package com.example.rmfr;

import com.example.rmfr.member.controller.MemberRestController;
import com.example.rmfr.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebMvcTest(MemberRestController.class)
class RmfrApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MemberService memberService;

	@Test
	void testCode() throws Exception {
		Map<String, Object> test = new HashMap<>();

		System.out.println(test);
	}


}
