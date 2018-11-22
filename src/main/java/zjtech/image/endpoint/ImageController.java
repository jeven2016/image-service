package zjtech.image.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image-service/")
public class ImageController {


    @GetMapping("/status")
    public String getStatus() {
        return "ok";
    }

}
