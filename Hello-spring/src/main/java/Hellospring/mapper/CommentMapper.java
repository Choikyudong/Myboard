package Hellospring.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import Hellospring.domain.CommentDTO;

@Mapper
//해당 인터페이스가 DB와 통신하는 인터페이스임을 의미
public interface CommentMapper {

	public int insertComment(CommentDTO params);
	//댓글을 삽입하는 INSERT 쿼리 호출
	
	public CommentDTO selectCommentDetail(Long idx);
	//댓글 삭제용 메서드
	
	public int updateComment(CommentDTO params);
	//댓글을 수정하는 UPDATE 쿼리
	
	public int deleteComment(Long idx);
	//댓글 삭제용 쿼리지만 delete_yn 컬럼 상태 Y로 댓글을 삭제보다는 숨긴다에 가깝다.
	
	public List<CommentDTO> selectCommentList(CommentDTO params);
	//특정 게시글에 포함된 댓글 목록 조회하는 SELECT
	
	public int selectCommentTotalCount(CommentDTO params);
	//특정 게시글에 포함된 댓글 개수를 조회하는 SELECT
	
}