package untangle.accounting.server;

import static org.springframework.security.config.Customizer.withDefaults;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@EnableWebSecurity

public class WebConfig  {
	Logger logger = LoggerFactory.getLogger(WebConfig.class);
	
	@Value("${untangle.accounting.csrf}")
	private boolean csrfEnabled;
	
	@Value("${untangle.accounting.auth}")
	private boolean authEnable;

	@Value("${untangle.accounting.user.name}")
	private String defaultUser;
	
	@Value("${untangle.accounting.user.password}")
	private String defaultPassword;
	
	
	@Autowired
	WebProperties props;
	
	@Bean
	@Profile({"dev","test"})
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**").allowedOrigins("*").allowedMethods("*");
			}
		};
	}
	
	@Bean
	WebMvcConfigurer spaConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/**")
				.addResourceLocations(props.getResources().getStaticLocations())
				.resourceChain(true)
				.addResolver(new SpaPageResourceResolver());
			}
		};
	}
	
    public static class SpaPageResourceResolver extends PathResourceResolver {
        @Override
        protected Resource getResource(String resourcePath, Resource location) throws IOException {
            Optional<Resource> resource = Optional.ofNullable(super.getResource(resourcePath, location));
            return resource.orElse(super.getResource("/index.html", location));
        }
    }
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
        http.csrf(csrf -> { 
        	if(csrfEnabled) {
        		logger.info("CSRF ENABLED");
        		csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
            	// Spring way to prevent to disable url encoded token + random 
            	// way to circumvent resolveCsrfTokenValue override in XorCsrfTokenRequestAttributeHandler
            	// by only using its handle method and fallback to the default implementation on the interface 
            	// CsrfTokenRequestHandler
            	
            	XorCsrfTokenRequestAttributeHandler  delegate = new XorCsrfTokenRequestAttributeHandler ();
            	CsrfTokenRequestHandler handle = delegate::handle;
            	csrf.csrfTokenRequestHandler(handle);
            	csrf.ignoringRequestMatchers("/login");
        	} else {
        		logger.info("CSRF DISABLED");
         		csrf.disable();
         	}
        });
    	
        if(authEnable) {
        	logger.info("AUTH ENABLED");
	    	http.authorizeHttpRequests()
	    		.anyRequest()
	    		.authenticated()
	    		.and()
	    		.formLogin(withDefaults());
	        } else {
        	logger.info("AUTH DISABLED");
        	http.authorizeHttpRequests().anyRequest().permitAll();
        }

    	return http.build();
    }
	
    @Bean
    InMemoryUserDetailsManager users() {
        return new InMemoryUserDetailsManager(
                User.withUsername(defaultUser)
                        .password(defaultPassword)
                        .authorities("read")
                        .build()
        );
    }
    
    
}
