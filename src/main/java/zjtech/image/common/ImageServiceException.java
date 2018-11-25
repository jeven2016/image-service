package zjtech.image.common;

import lombok.Getter;

public class ImageServiceException extends RuntimeException {

  @Getter
  private ErrorCode errorCode;

  public ImageServiceException(ErrorCode errorCode) {
    super(null, null, true, false);
    this.errorCode = errorCode;
  }

  public ImageServiceException(ErrorCode errorCode, String message) {
    super(message, null, true, false);
    this.errorCode = errorCode;
  }

}
