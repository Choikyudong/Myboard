//데이터베이스와 '통신' 역할을 한다.

package Hellospring.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import Hellospring.domain.BoardDTO;

@Mapper
/*
 * 마이바티스는 인터페이스에 해당 애너테이션만 지정하면
 * xml mapper에서 메서드의 이름과 일치하는 sql문을 찾아 실행한다.
 * mapper영역은 데이터베이스와의 통신, 즉 sql쿼리를 호출하는 것이 전부이다
 */
public interface BoardMapper {

	/*
	 * 리턴 타입이 int로 지정되 있는 것들이 있는데
	 * 보통은 void로 리턴타입을 갖는 경우가 많다고 한다.
	 * 하지만, 서비스 영역에서 Mapper영역의 메서드를 호출,
	 * sql 실행에 대한 결과값을 확실하게 전달받기 위해서 int로 처리한다.
	 */
	
	public int insertBoard(BoardDTO params);
	/*
	 * 게시글을 생성할 때 INSERT쿼리를 호출하는 메서드
	 * BoardDTO 클래스가 params라는 이름으로 지정되고
	 * params에 게시글의 정보가 담긴다.
	 */
	
	public BoardDTO selectBoardDetail(Long idx);
	/*
	 * 게시글을 조회하는 SELECT쿼리를 호출하는 메서드
	 * SELECT 쿼리가 실행되면, 긱 칼럼에 해당하는 결과값이 리턴 타입으로 지정된
	 * BoardDTO 클래스이 멤버 변수에 매핑된다.
	 * 파라미터는 idx, PK값으로 지정한 녀석을 받고
	 * WHERE 조건으로 idx를 사용해서 게시글을 조회한다.
	 */
	
	public int updateBoard(BoardDTO params);
	/*
	 * 게시글을 수정하는 UPDATE쿼리를 호출하는 메서드
	 * BoardDTO의 params가 이름으로 지정되고
	 * insertBoard와 마찬가지로 params에 게시글의 정보가 담긴다
	 */
	
	public int deleteBoard(Long idx);
	/*
	 * 게시글을 삭제하는 DELETE쿼리를 호출하는 메서드
	 * board테이블에는 delete_yn이라는 컬럼이 존재하는데
	 * 컬럼의 상태 값을 'Y', 'N'으로 지정을 한다음
	 * 상태 값이 'N'으로 지정된 데이터만 노출을 한다.
	 * 파라미터는 조회와 마찬가지로 idx값을 통해서 한다.
	 */

	public List<BoardDTO> selectBoardList(BoardDTO params);
	/*
	 * 게시글 목록을 조회하는 SELECT 쿼리를 호출하는 메서드
	 * 리턴타입으로 지정된 List<BoardDTO>을 제네릭 타입으로 한다.
	 * 이렇게 하면 위에있는 selectBoardDetail 메서드 호출 결과를
	 * 여러 개 저장하는 것과 유사하다고 한다.
	 */

	public int selectBoardTotalCount(BoardDTO params);
	/*
	 * 위에서 언급된 삭제 여부(delete_yn)이 'N'이 될 경우 조회가 가능하다고
	 * 말을 했는데 그 'N'으로 지정된 게시글의 개수를 조회하는
	 * SELECT쿼리를 호출하는 메서드이다
	 */
}
