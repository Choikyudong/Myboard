package Hellospring.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import Hellospring.constant.Method;
import Hellospring.domain.BoardDTO;
import Hellospring.domain.FileDTO;
import Hellospring.service.BoardService;
import Hellospring.util.UiUtils;

@Controller
//사용자의 요청, 응답을 처리하는 UI를 담당하는 컨트롤 클래스를 의미
public class BoardController extends UiUtils {
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private HttpServletRequest requset;
	
	/*
	 * 예전에는 @RequestMapping을 이용해서 value값을 주고 
	 * method 속성에 get, post 등등을 지정하는 방식이였다.
	 * 현재는 타입별로 매핑을 처리할 수 있는 애너테이션이 존재.
	 */
	
	//게시판 글쓰기 페이지
	@GetMapping(value = "/board/write.do")
	public String openBoardWrite(@ModelAttribute("params") BoardDTO params,
			@RequestParam(value = "idx", 	required = false) Long idx, Model model) {
		/*
		 * 화면에서 전달받은 파라미터를 처리하기 위해서 requestparam을 이용
		 * idx의 required를 false로 한 이유는 새로운 게시글을 등록하는 경우는
		 * 게시글 번호(idx)가 필요없기에 false로 지정 (required 기본값은 true)
		 * 만약 true로 할 경우에는 idx값이 파라미터로 전송이 되지 않아 오류발생
		 * 
		 * ModelAndView를 주로 사용하나
		 * String으로 가져오는 방법도 사용한다고 한다.
		 * 메서드의 파라미터인 Model은 데이터를 뷰로 전달하는 데 사용
		 */
		
		/*
		 * addAttribute라는 메서드로 BoardDTO객체를 
		 * board라는 이름으로 뷰(화면)에 전달한다.
		 */
		
		HttpSession session = requset.getSession(false);
		if(session == null) {
			return showMessageWithRedirect("글쓰기 기능은 로그인이 필요합니다.", 
					"/member/login.do", Method.GET, null, model);
		} 
		
		if (idx==null) { //게시글 번호(idx)가 전송되지 않으면 비어있는 객체를 전달
			model.addAttribute("board", new BoardDTO());
		} else {
			//게시글 번호가 전송된 경우에는
			BoardDTO board = boardService.getBoardDetail(idx); 
			//getboarddetail로 게시글 정보를 포함한 객체를 전달
				if(board==null || "Y".equals(board.getDeleteYn())) {
					return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다", 
							"/board/list.do", Method.GET, null, model);
				}	//근데 detail의 실행결과가 null이면 리스트 페이지로 리다이렉트
			
			model.addAttribute("board", board);
			
			List<FileDTO> fileList = boardService.getFileList(idx);
			//boardService의 getFileList로 업로드된 파일들을 볼 수 있따.
			model.addAttribute("fileList", fileList);
		}
		
		return "board/write"; //경로의 끝네 접미사(sufiix)로 확장자 .html이 자동으로 연결된다.
	}
	
	//게시판 등록
	@PostMapping(value = "/board/register.do")
	public String registerBoard(final BoardDTO params, 	final MultipartFile[] files, Model model) {
		//files를 BoardService의 registerBoard로 메서드를 전달하기 위해서 사용
		
		Map<String, Object> pagingParams = getPagingParams(params);
		
		try {
			boolean isRegistered = boardService.registerBoard(params);
			
			if (isRegistered == false) {
				return showMessageWithRedirect("게시글 등록 실패!", 
						"/board/list.do", Method.GET, pagingParams, model);
				//게시글 등록에 실패하였다는 메시지를 전달
			}
			
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 문제 발생!",
					"/board/list.do", Method.GET, pagingParams, model);
			//데이터베이스 처리 과정에 문제가 발생하였다는 메시지를 전달
		} catch (Exception e) {
			return showMessageWithRedirect("시스템 문제 발생!", 
					"/board/list.do", Method.GET, pagingParams, model);
			//시스템에 문제가 발생하였다는 메시지를 전달
		}

		return showMessageWithRedirect("게시글 등록 완료!", 
				"/board/list.do", Method.GET, pagingParams, model);
	}
	
	//게시판 목록 보기
	@GetMapping(value = "/board/list.do")
	public String openBoardList(@ModelAttribute("params") BoardDTO params, Model model) {
		List<BoardDTO> boardList = boardService.getBoardList(params);
		model.addAttribute("boardList", boardList);

		/*
		 * HttpSession s = requset.getSession(false); if(s != null) {
		 * System.out.println("세션있땅"); } else { System.out.println("없당"); }
		 */
		
		return "board/list";
		/*
		 * @ModelAttribute를 이용하면 파라미터로 받은 객체를 자동으로
		 * 뷰까지 전달을 해주는데 @RequestParam과 차이점은
		 * param은 model에게서 받고 addAttribute(key, value) 메서드로
		 * 하나하나 화면으로 전달하지만, @Model..은 별다른 거 없이 화면으로 전송
		 * @Model..("paging")은 화면에서 사용할 별칭 ${paging.currentPageNo}
		 * BoardService에서 호출한 
		 * getBoardList 메서드 실행결과를 
		 * boardList에 담아 뷰로 전달
		 */		
	}
	
	//게시글 보기
	@GetMapping(value = "/board/view.do")
	public String openBoardDetail(@ModelAttribute("params") BoardDTO params, 
			@RequestParam(value = "idx", required = false) Long idx, Model model) {
		/*
		 * required 속성은 파라미터가 필수 값인지에 대한 여부인데 
		 * 여기서는 당연히 idx값으로 해당 게시글을 불러올꺼기 때문에 당연히
		 * true로 해도 되지 않을까라는 생각이 들만하지만 
		 * false로 파라미터가 넘어오지 않을 경우를 
		 * 직접 처리할 예정이라 false로 지정한다
		 */
		
		if(idx==null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", 
					"/board/list.do", Method.GET, null, model);
			/*
			 * idx값이 없으면 당연히 해당 게시글이 존재하지 않으므로 
			 * 다시 리스트로 보낸다.
			 */
		}
		
		BoardDTO board = boardService.getBoardDetail(idx);
		/*
		 * 위의 if문이 통과되면 정상적인 게시글이고 
		 * 해당 메서드로 idx값을 전달해 정보를 조회
		 */
		if(board==null||"Y".equals(board.getDeleteYn())) {
			/*
			 * 이 프로젝트에서는 게시글의 데이터를 지우기 보다는 숨기는 형식이다 
			 * 그래스 Y라는 값을 해당메서드 값과 비교하여 일치하면 
			 * 다시 목록으로 이동하고
			 */
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", 
					"/board/list.do", Method.GET, null, model);
		}
		model.addAttribute("board", board);
		//모든 로직이 통과되면 정보를 페이지로 넣은 다음
		
		List<FileDTO> fileList = boardService.getFileList(idx);
		model.addAttribute("fileList", fileList);
		//해당 게시글에 맞는 파일들도 페이지로 넣고
		
		return "board/view";
		//화면으로 전달한다.
	}
	
	//게시판 삭제
	@PostMapping(value = "/board/delete.do")
	public String deleteBoard(@ModelAttribute("params")BoardDTO params,
			@RequestParam(value = "idx", required = false)Long idx, Model model) {
		//@ModelAttribute는  openBoardDetail 메서드와 마찬가지로 이전 페이지 정보가 담겨있는
		//Criteria 클래스를 상속받는 BoardDto 객체를 화면으로 전달한다.
		if(idx==null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다!",
					"/board/list.do", Method.GET, null, model);
		}
		
		Map<String, Object> pagingParams = getPagingParams(params);
		//UiUtils에 추가한 getPagingParams 메서드를 호출
		//메서드에 담긴 이전 페이지 정보를 shoMessageRedirect 메서드 인자로 전달
		
		try {
			boolean Deleted = boardService.deleteBoard(idx);
			if(Deleted==false) {
				return showMessageWithRedirect("게시글 삭제에 실패했습니다..",
						"/board/list.do", Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("DB에 문제 발생함;",
					"/board/list.do", Method.GET, pagingParams, model);
		} catch (Exception e) {
			
			return showMessageWithRedirect("시스템 문제 발생!",
					"/board/list.do", Method.GET, pagingParams, model);
		}
		
		return showMessageWithRedirect("게시글 삭제 완료!",
				"/board/list.do", Method.GET, pagingParams, model);
	}
	
	@GetMapping("/board/download.do")
	public void downloadFile(@RequestParam(value = "idx", required = false)final Long idx, Model model,
			HttpServletResponse response) {
		
		if(idx==null) throw new RuntimeException("올바르지 않는 접근입니다.");
		
		FileDTO fileInfo = boardService.getFileDetail(idx);
		if(fileInfo==null || "Y".equals(fileInfo.getDeleteYn())) {
			throw new RuntimeException("파일 정보를 찾을 수 없습니다.");
		}
		
		String uploadDate = fileInfo.getInsertTime().format(DateTimeFormatter.ofPattern("yyMMdd"));
		String uploadPath = Paths.get("D:","testforspring", "upload", uploadDate).toString();
		
		String filename = fileInfo.getOriginalName();
		File file = new File(uploadPath, fileInfo.getSaveName());
		
		try {
			byte[] data = FileUtils.readFileToByteArray(file);
			response.setContentType("application/octet-stream");
			response.setContentLength(data.length);
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(filename, "UTF-8") + "\";");

			response.getOutputStream().write(data);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			
		} catch (IOException e) {
			throw new RuntimeException("파일 다운로드에 실패하였습니다.");

		} catch (Exception e) {
			throw new RuntimeException("시스템에 문제가 발생하였습니다.");
		}
	}

}
