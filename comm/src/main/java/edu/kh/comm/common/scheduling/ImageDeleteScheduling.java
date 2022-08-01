package edu.kh.comm.common.scheduling;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.kh.comm.board.model.service.BoardService;

@Component // bean등록
public class ImageDeleteScheduling {

	private Logger logger = LoggerFactory.getLogger(ImageDeleteScheduling.class);

	// BOARD_IMG테이블에서는 삭제되었으나
	// 서버 / resources / images / board 폴더에는 존재하는
	// 이미지 파일을 정시 마다 삭제

	// 코딩 순서
	// 1) BOARD_IMG에 존재하는 모든 이미지 목록 조회
	// 2) /resources/images/board 폴더에 존재하는 모든 이미지 파일 목록 조회
	// 3) 두 목록을 비교해서 일치하지 않는 이미지 파일을 삭제
	// (DB에는 없는데 서버 폴더에 있으면 삭제)

	@Autowired // 의존성 주입 (DI)
	private BoardService service;

	@Autowired
	private ServletContext application; // application scope 객체 -> 서버 폴더 경로 얻어오기에 사용

	// 스케줄링에 사용되는 메서드는 무조건 public void 메서드명() 모양으로 만들어야한다.
	 @Scheduled(cron= "0 0 * * * *") // 정시마다 (1시 2시)
	// @Scheduled(cron= "0 * * * * *") // 매분마다 (테스트용)
	//@Scheduled(fixedDelay =10000) // 10초마다
	public void serverImageDelete() {
		
		// 1) BOARD_IMG에 존재하는 모든 이미지 목록 조회
		List<String> dbList = service.selectDBList();
				
		// 2) /resources/images/board 폴더에 존재하는 모든 이미지 파일 목록 조회
		String folderPath = application.getRealPath("/resources/images/board");
		
		File path = new File(folderPath); //  /resources/images/board 폴더를 참조하는 객체 
		File[] arr = path.listFiles(); // path가 참조하는 폴더에 있는 모든 파일을 얻어와 File[]로 반환

		List<File> serverList = Arrays.asList(arr); // arr을 List로 변환

		
		// 3) 두 목록을 비교해서 일치하지 않는 이미지 파일을 삭제
		//    (DB에는 없는데 서버 폴더에 있으면 삭제)
		if( !serverList.isEmpty() ) { // 서버에 이미지 파일이 있을때만 비교/삭제 진행
			
			// server : \resources\images\board\sample1.jpg
			// DB : /resources/images/board/sample1.jpg
			
			// 서버이미지
			for( File serverImage : serverList) {
				
				String name ="/resources/images/board/"+ serverImage.getName(); // 파일명만 얻어오기 
				
				
				//	/resources/images/board/ + sample1.jpg     /resources/images/board/sample1.jpg    
 				//	     서버 파일명(앞에 붙여서 비교하겠다)			             DB
				
			
				//Lis.indexOf(value) : List에 value와 같은 값이 있으면 인덱스 반환 / 없으면 -1 반환
				if(dbList.indexOf(name) == -1) {
					
					//dbList에는 없는데 serverList에만 파일이 존재하는 경우
					logger.info(serverImage.getName()+"삭제");
					serverImage.delete(); // 파일 삭제 
					// serverImage == File타입
					
				}
				
			}
			
			logger.info("--------서버 이미지 삭제완료--------");
			// 글쓰기에서 이미지를 추가해서 글작성을 등록하고 수정하기에서 이미지를 모두 삭제하면 server이미지스폴드와 dbList에서 이미지가 모두 삭제된다
			
			
			
			
		}
	}

}
