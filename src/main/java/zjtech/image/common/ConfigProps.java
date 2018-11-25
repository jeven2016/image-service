package zjtech.image.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "image.download")
public class ConfigProps {

    private Map<String, String> requestHeaders;

    private String requestMethod;

    private int bufferSize;

    private int retries;

    private int timeout;
}
