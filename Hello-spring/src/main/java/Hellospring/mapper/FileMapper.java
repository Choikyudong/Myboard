package Hellospring.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import Hellospring.domain.FileDTO;

@Mapper
public interface FileMapper {

	public int insertFile(List<FileDTO> FileList);
	//파일 정보를 저장하는 INSERT 쿼리 호출

	public FileDTO selectFileDetail(Long idx);
	//파라미터로 전달받은 파일 번호에 해당하는 파일 상세 전보 조회
	//다운로드 처리시에 필요
	
	public int deleteFile(Long boardIdx);
	//파일을 삭제
	//하지만 이 프로젝트에서는 실제 데이터를 삭제하지 않는다.

	public List<FileDTO> selectFileList(Long boardIdx);
	//특정 게시글에 포함된 파일 목록을 조회하는 SELECT 쿼리를 호출

	public int selectFileTotalCount(Long boardIdx);
	//특정 게시글에 포함된 파일 개수를 조회하는 SELECT 쿼리를 호출
	
	public int undeleteFile(List<Long> idxs);
	//게시글 삭제 취소처리 메서드
	
}
