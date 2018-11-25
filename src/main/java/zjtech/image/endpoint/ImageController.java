package zjtech.image.endpoint;

import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import zjtech.image.common.ErrorCode;
import zjtech.image.common.ImageServiceException;

@RestController
@RequestMapping("/image-service/")
@Slf4j
public class ImageController {

  private final ImageServiceProxy serviceProxy;

  @Autowired
  public ImageController(ImageServiceProxy serviceProxy) {
    this.serviceProxy = serviceProxy;
  }

  @GetMapping(value = "/show")
  public Mono<ResponseEntity<byte[]>> showPicture(@RequestParam("tu") @NotNull String imageUrl) {
    if (imageUrl.isEmpty()) {
      throw new ImageServiceException(ErrorCode.IMAGE_URL_REQUIRED);
    }

    byte[] data = serviceProxy.retrieveImage(imageUrl);
    if (data.length == 0) {
      log.warn("No data found for image {}", imageUrl);

    }
    Resource resource = new ByteArrayResource(data);

    return Mono.just(ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(data));
  }

}
