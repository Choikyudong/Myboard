/*
 * 해당 DTO는 공통적으로 사용되는
 * 컬럼들을 위해서 만들어짐
 */
package Hellospring.domain;

import java.time.LocalDateTime;

import Hellospring.paging.PaginationInfo;
import Hellospring.paging.Criteria;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonDTO extends Criteria {

	private PaginationInfo paginationInfo;
	//페이징 정보
	
	private String deleteYn;
	//삭제 여부
	
	private LocalDateTime insertTime;
	//등록일
	
	private LocalDateTime updateTime;
	//수정일
	
	private LocalDateTime deleteTime;
	//삭제일
	
}
