package zjtech.image.endpoint;

import com.github.tobato.fastdfs.domain.StorePath;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import zjtech.image.common.DownloadUtils;
import zjtech.image.document.ImageDocument;
import zjtech.image.repository.ImageMetaDataRepo;

@Service
@Slf4j
public class ImageServiceProxy {

  private final DownloadUtils downloadUtils;
  private final ImageMetaDataRepo repo;

  @Autowired
  public ImageServiceProxy(DownloadUtils downloadUtils, ImageMetaDataRepo repo) {
    this.downloadUtils = downloadUtils;
    this.repo = repo;
  }

  public byte[] retrieveImage(String imageUrl) {
    ImageDocument doc = repo.findByUrl(imageUrl.trim());

    if (doc == null) {
      doc = downloadImage(imageUrl);
    }

    if (doc == null) {
      log.warn("The image is not avaiable, url={}", imageUrl);
      return new byte[0];
    }

    byte[] data = downloadUtils
       .retrieveImageBytes(doc.getDfsGroup(), doc.getDfsPath());
    return data;
  }


  private ImageDocument downloadImage(String url) {
    StorePath storePath = downloadUtils.process(url);
    ImageDocument imageDocument = null;
    if (storePath != null) {
      imageDocument = new ImageDocument();
      imageDocument.setUrl(url);
      imageDocument.setDfsPath(storePath.getPath());
      imageDocument.setDfsFullPath(storePath.getFullPath());
      imageDocument.setDfsGroup(storePath.getGroup());

      imageDocument = repo.insert(imageDocument);
    }
    return imageDocument;
  }
}
