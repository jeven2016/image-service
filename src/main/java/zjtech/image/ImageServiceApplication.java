package zjtech.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import zjtech.image.common.ConfigProps;

@SpringBootApplication
@EnableScheduling
public class ImageServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ImageServiceApplication.class, args);
  }
}
