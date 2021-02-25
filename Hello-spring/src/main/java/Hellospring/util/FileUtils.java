package Hellospring.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import Hellospring.domain.FileDTO;
import Hellospring.exception.SpringFileException;

@Component
public class FileUtils {

	private final String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
	//오늘 날짜
	
//	private final String uploadPath = Paths.get("D:","testforspring", "upload", today).toString();
	//업로드 경로
	private final String uploadPath = Paths.get("C:", "develop", "upload", today).toString();

	private final String getRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	} //서버에 생성할 파일명을 처리할 랜덤 문자열 반환
	
	public List<FileDTO> uploadFiles(MultipartFile[] files, Long boardIdx) {
		
		List<FileDTO> FileList = new ArrayList<>(); //tb_file 테이블에 업로드 파일 정보를 담을 리스트
		
		File dir = new File(uploadPath);
		if (dir.exists() == false) {
			dir.mkdirs();
		} //uploadPath에 해당 디렉토리가 없으면, 부모 디렉토리 포함 모든 디렉토리 생성
		
		for(MultipartFile file : files) { //파일 개수만큼 foreach 실행
			if (file.getSize() < 1) {
				continue;
			}
			try {
				final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
				//파일 확장자명을 extension에 저장
				
				final String saveName = getRandomString() + "." + extension;
				//서버에 저장할 파일명(랜덤 문자열 + 확장자)
				
				File target = new File(uploadPath, saveName);
				//target이라는 이름으로, 업로드 경로와 파일명이 담긴 파일 객체 생성
				file.transferTo(target);
				//transferTo 메서드로 업로드 경로에 saveName과 동일한 이름을 가진 파일 생성
				
				FileDTO SpringFile = new FileDTO();
				SpringFile.setBoardIdx(boardIdx);
				SpringFile.setOriginalName(file.getOriginalFilename());
				SpringFile.setSaveName(saveName);
				SpringFile.setSize(file.getSize());
				//tb_file 테이블에 파일 정보 저장을 위해서 FileDTO 객체에 파일정보를 담고
				
				FileList.add(SpringFile);
				//FileList에 파일 정보를 추가한다.
				
			} catch (IOException e) {
				throw new SpringFileException("[" + file.getOriginalFilename() + "] 파일 저장 실패 ");
			} catch (Exception e) {
				throw new SpringFileException("[" + file.getOriginalFilename() + "] 파일 저장 실패.. ");
			}
		} //for end
		
		return FileList;
	} //function end
	
}
