//해당 클래스는 하단에 보이는 페이지 개수를 계산하는 클래스

package Hellospring.paging;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationInfo {

	private Criteria criteria;
	//페이징 계산에 필요한 파라미터가 담긴 클래스
	
	private int totalRecordCount;
	//전체 데이터 개수
	
	private int totalPageCount;
	//전체 페이지 개수
	
	private int firstPage;
	//pageSize가 10, currentPageNo가 3일 때 1페이지를 의미
	
	private int lastPage;
	//pageSize가 10, currentPageNo가 3일 때 10페이지를 의미
	
	private int firstRecordIndex;
	//LIMIT 구문의 첫 번째 값에 사용되는 변수
	
	private int lastRecordIndex;
	//아직 사용하지 않음
	
	private boolean hasPreviousPage;
	//이전 페이지의 존재여부를 구분하는 용도
	
	private boolean hasNextPage;
	//다음 페이지의 존재여부를 구분하는 용도
	
	public PaginationInfo(Criteria criteria) {
		//잘못된 값이 들어왔을 때 구분하는 용도
		if(criteria.getCurrentPageNo()<1) {
			criteria.setCurrentPageNo(1);
		}
		if(criteria.getRecordsPerPage()<1||criteria.getRecordsPerPage()>100) {
			criteria.setRecordsPerPage(10);
		}
		if(criteria.getPageSize()<5||criteria.getPageSize()>20) {
			criteria.setPageSize(10);
		}
		
		this.criteria = criteria;
		//확인 후 파라미터 정보를 가진 criteria을 pageInfo의 criteria에 저장한다.
		//이 데이터를 받아야 이 데이터를 기반으로 페이지 번호를 계산가능
	}
	
	public void setTotalRecordCount(int totalRecordCount) {
		//이제 받은 데이터를 기반으로 이 클래스에 저장을 하는데
		this.totalRecordCount = totalRecordCount;
		
		if(totalRecordCount>0) {
			//여기서 데이터가 1개 이상이면 페이지 번호를 계산하는
			//calculation이 실행(밑에 있음)
			calculation();
		}
	}
	
	private void calculation() {
		
		totalPageCount = ((totalRecordCount-1)/criteria.getRecordsPerPage()) + 1;
		//(전체 데이터 개수 - 1) / 페이지당 출력할 데이터 개수) + 1을 하면 전체 페이지 개수가 나옴
		//100개의 데이터가 있고 한 페이지당 데이터의 개수를 20개로 한다면 전체 페이지 개수는 5개
		if(criteria.getCurrentPageNo()>totalPageCount) {
			criteria.setCurrentPageNo(totalPageCount);
			/* 현재 페이지 번호가 전체 페이지 개수의 값보다 크면
			현재 페이지에 전체 페이지 개수를 저장
			전체 페이지가 5인데 현재 페이지가 6이면 좀 이상하다; */
		}
		
		firstPage = ((criteria.getCurrentPageNo()-1)/criteria.getPageSize()) * criteria.getPageSize() + 1;
		//((현재 페이지 번호 - 1) / 화면 하단의 페이지 개수) * 화면 하단의 페이지 개수 + 1로
		//좌측의 페이지 번호를 구할 수 있다.
		lastPage = firstPage + criteria.getPageSize() - 1;
		/*
		 * (첫 페이지 번호 + 화면 하단의 페이지 개수) - 1 
		 * 현재 페이지가 13 화면 하단에 출력할 페이지가 10이라면 
		 * (11 + 10) - 1 = 20이 된다.
		 */
		if(lastPage > totalPageCount) {
			lastPage = totalPageCount;
			//마지막 페이지 번호가 전체 페이지보 보다 크다면
			//lastPage에 totalPageCount값을 저장한다.
		}
		
		firstRecordIndex = (criteria.getCurrentPageNo()-1) * criteria.getRecordsPerPage();
		//지금 주석처리된 criteria에서 getStartPage 메서드를 대신해서
		//LIMIT 구문의 첫 번째 데이터
		
		lastRecordIndex = criteria.getCurrentPageNo() * criteria.getRecordsPerPage();
		//사용하지 않음
		
		hasPreviousPage = firstPage != 1;
		//이전 페이지 존재 여부를 확인하는 방법
		
		hasNextPage = (lastPage*criteria.getRecordsPerPage()) < totalPageCount;
		/* 
		 * (마지막 페이지 번호 * 페이지당 출력할 데이터 개수)
		 * 전체 데이터 개수보다 크거나 같으면 false, 작으면 true
		 * 페이지당 출력 데이터 10개, 마지막 번호 10일 때 두 값을 곱하면 100
		 * 그런데 전체 데이터 개수가 105면 hasNextPage는 true가 된다.
		 */
	}
}
