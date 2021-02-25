//입력받은 데이터의 저장 및 전송을 하는 역활

package Hellospring.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/*
 * 해당 애너테이션은 롬복(Lombok)이라는 라이브러리에서 제공하는 기능으로
 * 더 이상 지겹게 만들 필요가 없다!
 */
public class BoardDTO extends CommonDTO {

	//번호 (PK)
	private Long idx;

	//제목
	private String title;

	//내용
	private String content;

	//작성자
	private String writer;

	//조회 수
	private int viewCnt;

	//공지 여부
	private String noticeYn;

	//비밀 여부
	private String secretYn;
	
	//파일 변경 여부
	private String changeYn;

	// 파일 인덱스 리스트
	private List<Long> fileIdxs;

}
