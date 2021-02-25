package Hellospring.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import Hellospring.domain.BoardDTO;
import Hellospring.domain.FileDTO;
import Hellospring.mapper.BoardMapper;
import Hellospring.mapper.FileMapper;
import Hellospring.paging.PaginationInfo;
import Hellospring.util.FileUtils;

@Service
//해당 클래스가 비즈니스 로직을 담당하는 서비스 클래스임을 표시하기 위한 애터네이션
public class BoardServiceImpl implements BoardService {

	@Autowired
	//오또와이드로 BoardMapper 인터페이스 bean을 주입한다.
	private BoardMapper boardMapper;
	
	@Autowired
	//주입안하고 왜 안되냐고 뭐라하는 건 좀;
	//컴포넌트를 만들었으면 꼭 주입해주자
	private FileMapper fileMapper;
	
	@Autowired
	private FileUtils fileUtils;
	
	@Override
	public boolean registerBoard(BoardDTO params) {
		/*
		 * 생성(INSERT)와 수정(UPDATE)를 따로 분리하지 않은 이유는
		 * 결국 두 행위 모두 데이터를 등록하는 용도이기 때문에 한번에 처리하도록
		 * 구현을 한다.
		 * 게시글번호(idx(PK))의 유무를 기준으로 생성, 수정을 실행한다.
		 */
		int queryResult = 0;
		/*
		 * 해당 변수는 메서드의 실행 결과를 저장하는 용도로
		 * 메서드에서 호출한 쿼리가 정상 작동을 할 경우
		 * 쿼리를 실행한 횟수, 1이 저장이 된다.
		 */		
		if(params.getIdx()==null) {
			/*
			 * 만약 idx값이 null일 경우 
			 * MySQL(현재 프로젝트는 mysql을 사용중)의 
			 * AUTO_INCREMENT 속성에 의해 PK(idx)가
			 * 자동 증가되어 게시글을 생성하고
			 */ 
			queryResult = boardMapper.insertBoard(params);
		} else {
			/*
			 * 만약 null이 아니라 
			 * idx에 값이 포함되어 있다면 
			 * 게시글을 수정하는 쿼리가 된다.
			 */
			queryResult = boardMapper.updateBoard(params);
			
			//파일이 추가, 삭제 등 변경이 될 경우
			if ("Y".equals(params.getChangeYn())) {
				//게시글이 수정 또는 삭제, 변경이되면 기존의 파일을 모두 삭제 처리한다.
				fileMapper.deleteFile(params.getIdx());

				// fileIdxs에 포함된 idx를 가지는 파일의 삭제여부를 'N'으로 업데이트
				if (CollectionUtils.isEmpty(params.getFileIdxs()) == false) {
				//게시글에 포함되있는 파일이 유지되는 경우
					fileMapper.undeleteFile(params.getFileIdxs());
				}
			}
		}
		
		return (queryResult > 0);
	}
	
	@Override
	public boolean registerBoard(BoardDTO params, MultipartFile[] files) {
		//게시물 등록을 처리하는 기존의 registerBoard 메서드를 호출하고
		
		int queryResult = 1;

		if (registerBoard(params) == false) {
			return false;
		} //게시물이 등록되었따면

		List<FileDTO> fileList = fileUtils.uploadFiles(files, params.getIdx());
		//fileList에 자료를 업로드한다.
		if (CollectionUtils.isEmpty(fileList) == false) {
			queryResult = fileMapper.insertFile(fileList);
			if (queryResult < 1) {
				queryResult = 0;
			}
		}

		return (queryResult > 0);
	}
	
	@Override
	public BoardDTO getBoardDetail(long idx) {
		return boardMapper.selectBoardDetail(idx);
	}	//단순한 하나의 게시글을 조회하는 메서드
	
	@Override
	public boolean deleteBoard(long idx) {
		int queryResult = 0;
		
		BoardDTO board = boardMapper.selectBoardDetail(idx);
		
		if(board!=null&&"N".equals(board.getDeleteYn())) {
			queryResult = boardMapper.deleteBoard(idx);
		}
		/*
		 * 컬럼에 delete_yn이라는 값이 Y가 되면 
		 * 더 이상 리시트에 나타나지 않도록 설정이 되어있다. 
		 * 그래서 N값일 경우에만 게시글을 삭제하도록 만듬.
		 */
		return (queryResult==1)?true:false;
	}
	
	@Override
	public List<BoardDTO> getBoardList(BoardDTO params) {
		//deleteBoard에서 Y값이 아닌 모든 게시글을 조회한다.
		List<BoardDTO> boardList = Collections.emptyList();
		//emptyList 메서드를 이용해서 비어있는 리스트를 선언한다.

		int boardTotalCount = boardMapper.selectBoardTotalCount(params);

		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(boardTotalCount);

		params.setPaginationInfo(paginationInfo);

		if (boardTotalCount > 0) {
			boardList = boardMapper.selectBoardList(params);
		}

		return boardList;
	}
	
	@Override
	public List<FileDTO> getFileList(Long boardIdx) {
		
		int fileTotalCount = fileMapper.selectFileTotalCount(boardIdx);
		//전체 파일 갯수를 카운트하고

		if(fileTotalCount<1) {
			return Collections.emptyList();
		} //파일이 없으면 빈값 있으면
		
		return fileMapper.selectFileList(boardIdx);
		//해당하는 게시글에 포함된 파일 리스트를 리턴
	}
	
	@Override
	public FileDTO getFileDetail(Long idx) {
		return fileMapper.selectFileDetail(idx);
	}
	
}
