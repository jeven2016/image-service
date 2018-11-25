package zjtech.image.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import zjtech.image.document.ImageDocument;

public interface ImageMetaDataRepo extends MongoRepository<ImageDocument, String> {

  ImageDocument findByUrl(String url);
}
