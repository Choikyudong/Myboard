/*
 * CRUD테스트
 */
package Hellospring;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Hellospring.domain.BoardDTO;
import Hellospring.mapper.BoardMapper;
//import lombok.Data;

@SpringBootTest
class MapperTests {

	@Autowired
	private BoardMapper boardMapper;
	//해당 애너테이션으로 BoardMapper 인터페이스 bean을 주입한다.

	@Test
	public void testOfInsert() {
	//게시글 생성을 테스트하는 메서드
		
		BoardDTO params = new BoardDTO();
		//테이블 구조화 클래스인 BoardDTO의 객체를 생성
		params.setTitle("1번 게시글 제목");
		params.setContent("1번 게시글 내용");
		params.setWriter("테스터");
		//set 메서드로 제목, 내용, 작성자를 만든다.

		int result = boardMapper.insertBoard(params);
		/*
		 * 그 다음 boardMapper의 insertBoard로 
		 * 게시글 정보가 저장된 params를 전달
		 */
		System.out.println("결과는 " + result + "입니다.");
		/*
		 * 여기서 int값은 쿼리 실행 횟수에 해당한다
		 * 즉 이 테스트에서는 1밖에 나올 수 없다.
		 */	
	}
	
	@Test
	public void testOfSelectDetail() {
		BoardDTO board = boardMapper.selectBoardDetail((long)1);
		/*
		 * 여기서 (long) 1은 1번 게시글 PK
		 * 즉 idx값에 해당하는 것을 불러오는것을 의미한다.
		 */		
		try {
			String boardJson = new ObjectMapper().writeValueAsString(board);
			
			System.out.println("======================");
			System.out.println(boardJson);
			System.out.println("======================");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOfUpdate() {
		BoardDTO params = new BoardDTO();
		params.setTitle("1번 게시글 제목");
		params.setContent("1번 게시글 내용");
		params.setWriter("테스터");
		params.setIdx((long)1);
		//INSERT와 다르게 수정 후 게시글 번호를 지정한다.
		
		int result = boardMapper.updateBoard(params);
		if (result==1) {
			BoardDTO board = boardMapper.selectBoardDetail((long)1);
			try {
				String boardJson = new ObjectMapper().writeValueAsString(board);

				System.out.println("=========================");
				System.out.println(boardJson);
				System.out.println("=========================");

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testOfDelete() {
		int result = boardMapper.deleteBoard((long)1);
		/*
		 * 해당 메서드 where 절에는 delete_yn = 'N'이 
		 * 적용되있기 때문에 boardJson을 null로 반환한다.
		 */	
		if(result==1) {
			BoardDTO board = boardMapper.selectBoardDetail((long)1);
			try {
				String boardJson = new ObjectMapper().writeValueAsString(board);

				System.out.println("=========================");
				System.out.println(boardJson);
				System.out.println("=========================");

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	//해당 파트는 테스트를 위해 여러개의 임시 게시판을 만드는 용도
	public void testMultipleInsert() {
		for (int i = 3; i <= 50; i++) {
			BoardDTO params = new BoardDTO();
			params.setTitle(i + "번 게시글 제목");
			params.setContent(i + "번 게시글 내용");
			params.setWriter(i + "번 게시글 작성자");
			boardMapper.insertBoard(params);
		}
	}
	
	@Test
	public void testSelectList() {
		int boardTotalCount = boardMapper.selectBoardTotalCount(null);
		
		/*
		 * 삭제되지 않은 전체 게시글 수를 저장하고 카운팅한다.
		 * 즉 delete_yn이 N인 게시글만 카운팅함
		 * 게시글이 한 개 이상일 경우, 제네릭 타입의 변수인 boardList에
		 * selectBoardList 메서드 실행 결과에
		 * 해당하는 게시글 목록을 저장한다.
		 */
		
		if (boardTotalCount > 0) {
			List<BoardDTO> boardList = boardMapper.selectBoardList(null);
			if (CollectionUtils.isEmpty(boardList) == false) {
				for (BoardDTO board : boardList) {
					System.out.println("=========================");
					System.out.println(board.getTitle());
					System.out.println(board.getContent());
					System.out.println(board.getWriter());
					System.out.println("=========================");
				}
			}
		}
		/*
		 * 스프링에서 지원하는 CollectionUtils.isEmpty로
		 * boardList가 비어있는지 확인하고
		 * foreach로 boardlist에 저장된 게시글들을 출력한다.
		 */
	}
}
