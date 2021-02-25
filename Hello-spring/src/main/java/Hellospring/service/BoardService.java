package Hellospring.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import Hellospring.domain.BoardDTO;
import Hellospring.domain.FileDTO;

public interface BoardService {

	public boolean registerBoard(BoardDTO params);
	
	public boolean registerBoard(BoardDTO params, MultipartFile[] files);
	//오버로딩은 보는 것 처럼 같은 이름을 가진 메서드이지만 
	//파라미터의 타입 또는 개수를 다르게 해서 메서드를 선언하는 것
	
	public BoardDTO getBoardDetail(long idx);
	
	public boolean deleteBoard(long idx);
	
	public List<BoardDTO> getBoardList(BoardDTO params);
	
	public List<FileDTO> getFileList(Long boardIdx);
	
	public FileDTO getFileDetail(Long idx);
	
}
