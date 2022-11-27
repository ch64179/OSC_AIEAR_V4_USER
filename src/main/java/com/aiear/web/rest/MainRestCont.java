package com.aiear.web.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aiear.AgentMain;
import com.aiear.dao.CommonDAO;

@RestController
@RequestMapping(value="/rest/*")
public class MainRestCont {
	
	protected final Logger logger = LogManager.getLogger(getClass());
		
	@Autowired
	private CommonDAO commonDAO;
	
	
	@RequestMapping("/isAlive")
	public String isAlive() {
		String chk = commonDAO.isAlive();
		
		chk = chk != null || !"".equals(chk) ? "success" : "fail";
		
		return chk;
	}
	
	/**
	 * agent를 서비스로 등록한 경우에는 중지를 시켜도 다시 재시작되므로 rest api 사용시 주의할 것
	 * @return
	 */
	@RequestMapping("/stop")
	public ResponseEntity<String> stop() {
		logger.info("@@@ /stop command received");
		
		//성공 응답 후 스프링 종료
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(200);
					//Main.stop();
					
					// Spring Boot 종료( Agent 종료)
					SpringApplication.exit(AgentMain.context);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		return ResponseEntity.ok("success");
	}
	
	/**
	 * 중지될때까지 대기하며 종료한다.
	 * agent를 서비스로 등록한 경우에는 중지를 시켜도 다시 재시작되므로 rest api 사용시 주의할 것
	 * @return
	 */
	@RequestMapping("/stopWait")
	public ResponseEntity<String> stopWait() {
		logger.info("@@@ /stopWait command received");
		
		// Spring Boot 종료( Agent 종료)
		SpringApplication.exit(AgentMain.context, new ExitCodeGenerator() {
			@Override
			public int getExitCode() {
				return 0;
			}
			
		});
					
		return ResponseEntity.ok("success");
	}
	
	
	@RequestMapping("/restart")
	public String restart() {
		logger.info("@@@ /restart command received");
		
		Thread restartThread = new Thread(() -> {
			try {
				Thread.sleep(200);
				
				//Main.restart();
				
				if(AgentMain.isRestarting) return; 
				AgentMain.isRestarting = true;
				
				logger.info("@@@ ReStarting... <==");
				
				AgentMain.context.close();
				AgentMain.context = SpringApplication.run(AgentMain.class, AgentMain.args);
				
				logger.info("@@@ ReStarted <==");
				AgentMain.isRestarting = false;
				
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		});
		
		restartThread.setDaemon(false);
		restartThread.start();
		
		return "success";
	}
	
	
}

