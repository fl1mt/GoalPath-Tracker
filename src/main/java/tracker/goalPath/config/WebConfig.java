package tracker.goalPath.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/goals/selected/*/tasks/details/*/subtask/*").setViewName("forward:/subtask_details.html");
        registry.addViewController("/goals/selected/*/tasks/details/*").setViewName("forward:/task_details.html");
        registry.addViewController("/goals/selected/**").setViewName("forward:/goal_details.html");

        registry.addViewController("/goals").setViewName("forward:/goals.html");
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/login").setViewName("forward:/login.html");
        registry.addViewController("/register").setViewName("forward:/register.html");
    }
}