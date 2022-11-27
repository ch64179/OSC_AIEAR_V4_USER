package com.aiear;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Properties;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.springframework.jmx.JmxException;

import com.aiear.ShutdownMain.HookThread;
import com.aiear.util.SpringApplicationAdminClient;

public class ShutdownMain {
	static class HookThread extends Thread {
		@Override
		public void run() {
			System.out.println("Hook Run");
		}
	}
	
    /**
     * Spring Boot 종료
     * @param args
     */
	public static void main(String[] args) throws Exception {
		String host;
		Integer port;
		
		if(args.length >= 2) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		}else {
			Properties props = new Properties();
	        InputStream stream = ShutdownMain.class.getResourceAsStream("/application.properties");
	        if(stream == null) {
	        	stream = new FileInputStream("./application.properties");
	        }
	        props.load(stream);
	        
	        host = props.getProperty("jmx.rmi.host");
	        port = Integer.parseInt(props.getProperty("jmx.rmi.port"));
		}
		
		new ShutdownMain().shutdown(host, port);
	}

	
	public void shutdown(String jmxHost, Integer jmxPort) throws Exception {
		System.out.println("@@@ ShutdownMain Starting...");
		
		String url = "service:jmx:rmi:///jndi/rmi://" + jmxHost + ":" + Integer.toString(jmxPort) + "/jmxrmi";
		System.out.println("@@@ url = " + url);
		
		String jmxName = SpringApplicationAdminClient.DEFAULT_OBJECT_NAME;
		//String jmxName = "org.springframework.boot:name=shutdownEndpoint,type=Endpoint";
		
		JMXServiceURL serviceUrl = new JMXServiceURL(url);
		try {
			JMXConnector connector = JMXConnectorFactory.connect(serviceUrl, null);
			try {
				MBeanServerConnection connection = connector.getMBeanServerConnection();
				try {
					ObjectName objectName = new ObjectName(jmxName);
					
					//shutdown 호출(async 이므로 바로 리턴됨)
					connection.invoke(objectName, "shutdown", null, null);
				} catch (ReflectionException ex) {
					throw new JmxException("Shutdown failed", ex.getCause());
				} catch (MBeanException ex) {
					throw new JmxException("Could not invoke shutdown operation", ex);
				} catch (MalformedObjectNameException ex) {
					throw new IllegalArgumentException("Invalid jmx name '" + jmxName + "'");
				} catch (InstanceNotFoundException ex) {
					throw new IllegalStateException("Spring application lifecycle JMX bean not " +
							"found, could not stop application gracefully", ex);
				} catch (IOException ex) {
					throw new JmxException("IOException", ex);
				}
			}
			finally {
				try {
					connector.close();
				}catch(Exception e) {
					System.out.println("##################################");
					System.out.println(e.getMessage());
					System.out.println("##################################");
					System.out.println("### JMI Control Error!!!");
					System.out.println("Please retry!!!");
					return;
				}
			}
		}catch(Exception ex) {
			System.out.println("##################################");
			System.out.println(ex.getMessage());
			System.out.println("##################################");
			System.out.println("### JMI Connection Error! Maybe Agent already shutdown ###");
			return;
		}
		
		
		//port가 사용가능한지 확인(2분간)
		int cnt = 0;
		while(cnt <= 600) {
			Thread.sleep(200);
			try{
                ServerSocket s = new ServerSocket(jmxPort);
				s.close();
				break;
			}catch(IOException ee) {
				System.out.println("Port has been opened yet!!");
			}
			if(++cnt % 5 == 0) {
				System.out.println("waiting...");
			}
		}

		System.out.println("@@@ ShutdownMain Ended");
	}

}
