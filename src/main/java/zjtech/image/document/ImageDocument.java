package zjtech.image.document;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;

@Getter
@Setter
@TypeAlias("image")
public class ImageDocument {

  @Id
  private String id;

  private String url;

  private String dfsGroup;

  private String dfsPath;

  private String dfsFullPath;

  @Version
  private Long version;

  @CreatedDate
  private LocalDateTime createdDate;

}
