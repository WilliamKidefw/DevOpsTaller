package com.mitocode.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.mitocode.calculator.SimpleCalculatorController;


@RunWith(SpringRunner.class)
@SpringBootTest(  classes = SimpleCalculatorController.class)
@AutoConfigureMockMvc
public class SimpleCalculatorControllerTest {
	
	@Autowired(required = true)
    private MockMvc mvc;
	
	@Test
	public void givenWhoami() throws Exception {
	 
	    mvc.perform(get("/api/whoami")
	      .contentType(MediaType.TEXT_PLAIN))
	      .andExpect(status().isOk())
	      .andExpect(content().string("Hi Unknown"));
	}
	
	@Test
	public void calculatorAdd() throws Exception {
		
	    mvc.perform(get("/api/add/{num1}/{num2}", new Integer[] {2,3})
	      .contentType(MediaType.TEXT_PLAIN))
	      .andDo(print())
	      .andExpect(status().isOk())
	      .andExpect(content().string(containsString(2+" + "+3+" = "+(2+3))));
	}
	
	@Test
	public void calculatorSub() throws Exception {
		
	    mvc.perform(get("/api/sub/{num1}/{num2}", new Integer[] {2,3})
	      .contentType(MediaType.TEXT_PLAIN))
	      .andDo(print())
	      .andExpect(status().isOk())
	      .andExpect(content().string(containsString(2+" - "+3+" = "+(2-3))));
	}
	
	@Test
	public void calculatorMul() throws Exception {
		
	    mvc.perform(get("/api/mul/{num1}/{num2}", new Integer[] {2,3})
	      .contentType(MediaType.TEXT_PLAIN))
	      .andDo(print())
	      .andExpect(status().isOk())
	      .andExpect(content().string(containsString(2+" x "+3+" = "+(2*3))));
	}
	
	@Test
	public void calculatorDiv() throws Exception {
		
	    mvc.perform(get("/api/div/{num1}/{num2}", new Integer[] {2,3})
	      .contentType(MediaType.TEXT_PLAIN))
	      .andDo(print())
	      .andExpect(status().isOk())
	      .andExpect(content().string(containsString(2+" / "+3+" = "+(2/3))));
	}
}
