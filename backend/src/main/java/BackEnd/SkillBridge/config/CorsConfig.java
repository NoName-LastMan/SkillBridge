package BackEnd.SkillBridge.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Mengizinkan kredensial (seperti token/cookies)
        config.setAllowCredentials(true);

        // Mengizinkan akses dari origin React kamu (Vite biasanya pakai 5173 atau 5174)
        config.addAllowedOriginPattern("http://localhost:*");

        // Mengizinkan semua jenis header
        config.addAllowedHeader("*");

        // Mengizinkan semua jenis method (GET, POST, PUT, DELETE, OPTIONS)
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Terapkan ke semua endpoint API

        return new CorsFilter(source);
    }
}