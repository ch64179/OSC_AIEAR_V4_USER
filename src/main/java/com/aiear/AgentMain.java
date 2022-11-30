package com.aiear;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.aiear.config.session.User;
import com.aiear.config.session.UserRepository;

import de.codecentric.boot.admin.config.EnableAdminServer;

@SpringBootApplication(scanBasePackages= {"com.aiear"}, exclude = {
//		DataSourceTransactionManagerAutoConfiguration.class,
		DataSourceAutoConfiguration.class
})
@EnableJpaRepositories
@EnableScheduling
@EnableAdminServer
@EnableAsync
public class AgentMain {
	
	public static ConfigurableApplicationContext context;
	public static String[] args;
	public static boolean isRestarting = false;
    
	@Autowired
	private UserRepository repository;

//	@Bean(name="threadPoolTaskExecutor")
//    public TaskScheduler taskScheduler() {
//    	ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
//    	taskScheduler.setPoolSize(10);
//    	return taskScheduler;t
//    }
	
    /**
     * Spring Boot 실행
     * @param args
     */
	public static void main(String[] args) {
		
		System.out.println("@@@ Starting...");
		
		AgentMain.args = args;
		
        SpringApplicationBuilder builder = new SpringApplicationBuilder(AgentMain.class);
        builder.headless(false);
        context = builder.run(args);
		 
		System.out.println("@@@ Started!!!");
	} 

	
}
