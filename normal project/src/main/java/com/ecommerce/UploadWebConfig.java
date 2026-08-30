package com.ecommerce;
import org.springframework.beans.factory.annotation.Value; import org.springframework.context.annotation.Configuration; import org.springframework.web.servlet.config.annotation.*; import java.nio.file.Paths;
@Configuration
public class UploadWebConfig implements WebMvcConfigurer {@Value("${app.upload-dir}")private String dir;@Override public void addResourceHandlers(ResourceHandlerRegistry r){r.addResourceHandler("/uploads/**").addResourceLocations(Paths.get(dir).toAbsolutePath().normalize().toUri().toString());}}
