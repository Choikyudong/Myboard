package Hellospring.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Hellospring.adapter.GsonLocalDateTimeAdapter;
import Hellospring.domain.CommentDTO;
import Hellospring.service.CommentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
//해당 애너테이션이 선언된 컨트롤러의 모든 메서드는 화면이 아닌
//리턴 타입에 해당하는 '데이터 자체'를 리턴합니다.
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@RequestMapping(value = { "/comments", "/comments/{idx}" }, 
			method = { RequestMethod.POST, RequestMethod.PATCH })
	/*GetMapping이 아닌 RequestMapping으로 한 이유는 REST의 설계 규칙 때문에 URI가 구분되어야함
	 /commetns는 새로은 댓글 등록, /commetns/{idx}는 댓글의 수정 */
	public JsonObject registerComment(@PathVariable(value = "idx", required = false) Long idx, 
			@RequestBody final CommentDTO params) {

		JsonObject jsonObj = new JsonObject();
		//댓글을 저장한 JSON 객체 생성

		try {
			boolean isRegistered = commentService.registerComment(params);
			jsonObj.addProperty("result", isRegistered);

		} catch (DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");

		} catch (Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}

		return jsonObj;
	}
	
	@GetMapping(value = "/comments/{boardIdx}")
	public JsonObject getCommentList(@PathVariable("boardIdx")Long boardIdx,
			@ModelAttribute("params")CommentDTO params) {
		/*
		 * @PathVariable은 @RequestParam과 유사한 기능이며 REST 방식에서 리소스를 표현하는 데 사용된다. 
		 * 해당 애너테이션의값은 /comments/{boardIdx} URI의 {boardIdx}을 의미하며
		 * 
		 * @ModelAttribute는 파라미터로 받은 객체를 자동으로 화면으로 전달한다. 
		 * 해당 애너테이션은 게시글 번호(boardIdx)를 가진 params를 전달해 특정 게시글에 등록된 댓글을 조회하는 것
		 */
		
		
		JsonObject jsonObj = new JsonObject();
		//먼저 JSON 객체를 생성
		
		List<CommentDTO> commentList = commentService.getCommentList(params);
		//해당 서비스의 메서드로 댓글 목록을 저장
		
		if(CollectionUtils.isEmpty(commentList)==false) {
			//댓글이 1개 이상이면
			
			Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, 
					new GsonLocalDateTimeAdapter()).create();
			//adapter 패키ㅣ지의 GsonLocal..클래스로 gson을 통해 시간 조정이 가능해짐
			
			JsonArray jsonArr = gson.toJsonTree(commentList).getAsJsonArray();
			
			jsonObj.add("commentList", jsonArr);
			//그리고 JSON 객체에 담고 마지막으로 리턴
		}
		
		return jsonObj;
		//JsonArray 자체를 리턴해도 되지만 해당 방식으로 할 경우 JSON 객체에 데이터를 추가할 수 있어서 좋다고 한다.
	}
	
	@DeleteMapping(value = "/comments/{idx}")
	//HTTP 요청 메서드 중 DELETE를 사용
	public JsonObject deleteComment(@PathVariable("idx") final Long idx) {
	//@PathVariable은 URI에 파라미터로 전달받은 변수를 지정할 수 있습니다.
		
		JsonObject jsonObj = new JsonObject();
		//JSON 생성

		try {
			boolean isDeleted = commentService.deleteComment(idx);
			jsonObj.addProperty("result", isDeleted);
			//isDeleted 실행결과를 JSON에 저장
			//각 catch 문에 해당 예외 발생 시 메세지를 JSON 객체에 저장
			
		} catch (DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");

		} catch (Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}

		return jsonObj;
		//모든게 완료 후 JSON 객체를 리턴
	}
	
}
