package Hellospring.paging;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Criteria {

	private int currentPageNo;
	//현재 페이지 번호로 화면 처리시 페이징 정보를 계산하는 용도
	
	private int recordsPerPage;
	//페이지마다 출력할 데이터의 개수
	
	private int pageSize;
	//화면 하단에 출력할 페이지의 크기
	
	private String searchKeyword;
	private String searchType;
	//검색을 위한 용도
	
	public Criteria() {
		this.currentPageNo = 1;
		this.recordsPerPage = 10;
		this.pageSize = 10;
	}
	
	public String makeQueryString(int pageNo) {

		UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.queryParam("currentPageNo", pageNo)
				.queryParam("recordsPerPage", recordsPerPage)
				.queryParam("pageSize", pageSize)
				.queryParam("searchType", searchType)
				.queryParam("searchKeyword", searchKeyword)
				.build()
				.encode();

		return uriComponents.toUriString();
		/*
		 * 해당 메서드는 스프링의 UriComponents 클래스로
		 * URI를 처리하게 해준다.
		 */
	}
	
}
