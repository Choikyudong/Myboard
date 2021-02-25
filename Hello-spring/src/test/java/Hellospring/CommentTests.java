package Hellospring;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import Hellospring.domain.CommentDTO;
import Hellospring.service.CommentService;

@SpringBootTest
public class CommentTests {

	@Autowired
	private CommentService commentService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void registerComments() {
		int number = 10;

		for (int i = 1; i <= number; i++) {
			CommentDTO params = new CommentDTO();
			params.setBoardIdx((long) 1); // 댓글을 추가할 게시글 번호
			params.setContent(i + "번 댓글을 추가합니다!");
			params.setWriter(i + "번 회원");
			commentService.registerComment(params);
		}

		logger.debug("댓글 " + number + "개가 등록되었습니다.");
	}

	@Test
	public void deleteComment() {
		commentService.deleteComment((long) 10); // 삭제할 댓글 번호

		getCommentList();
	}

	@Test
	public void getCommentList() {
		CommentDTO params = new CommentDTO();
		params.setBoardIdx((long) 1000); // 댓글을 추가할 게시글 번호

		commentService.getCommentList(params);
	}

}
	