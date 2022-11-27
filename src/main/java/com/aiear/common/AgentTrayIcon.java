package com.aiear.common;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import com.aiear.AgentMain;

@Component
public class AgentTrayIcon {
	//refer : https://blog.silentsoft.org/archives/6
	
	
//	@Value("${spring.application.name}")
//	private String agentName;
//	
//	private static final String IMAGE_PATH = "/static/img/icon.png";	
//	
//	private PopupMenu popup;
//	private TrayIcon tray;
//	private boolean isSupported=false;
//	private SystemTray systemTry;
//	
//	@PostConstruct
//	private void init() {
//		//오류나면 미지원되는 OS로 판단
//		try {
//			if(!SystemTray.isSupported()) return;
//			systemTry = SystemTray.getSystemTray();
//			tray = new TrayIcon(createImage(IMAGE_PATH,agentName),agentName);
//		}catch(Exception ex) {
//			return;
//		}
//		
//		isSupported = true;
//		
//		try {
//			tray.setImageAutoSize(true);
//			
//			popup = new PopupMenu();
//	
//			// Create a pop-up menu components
//			MenuItem exitItem = new MenuItem("Exit");
//			popup.add(exitItem);
//			exitItem.addActionListener(new ActionListener(){
//				public void actionPerformed(ActionEvent e) {
//					final int exitCode = 0;
//					ExitCodeGenerator exitCodeGenerator = new ExitCodeGenerator() {
//						@Override
//						public int getExitCode() {
//						  return exitCode;
//						}
//					};
//					
//					try {
//						systemTry.remove(tray);
//					}catch(Exception ex) {}
//					
//					try {
//						System.exit(SpringApplication.exit(AgentMain.context, exitCodeGenerator));
//					}catch(Exception ex) {
//						System.exit(0);
//					}
//				}
//			});
//			
//			
//			// popup.addSeparator();
//			tray.setPopupMenu(popup);
//			
//			systemTry.add(tray);
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//	
//	@PreDestroy
//	public void destroy() {
//		if(!isSupported) return;
//		if(tray == null) return;
//		systemTry.remove(tray);
//	}
//
//	protected Image createImage(String path, String description){
//		URL imageURL = AgentTrayIcon.class.getResource(path);
//		if(imageURL == null){
//		    System.err.println("Failed Creating Image. Resource not found: "+path);
//		    return null;
//		}else {
//			return Toolkit.getDefaultToolkit().getImage(imageURL);
//		}
//	}
}
