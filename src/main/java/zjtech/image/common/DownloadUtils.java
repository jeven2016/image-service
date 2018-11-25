package zjtech.image.common;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashSet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class DownloadUtils {

  private final FastFileStorageClient storageClient;

  private final ConfigProps configProps;

  @Autowired
  public DownloadUtils(FastFileStorageClient storageClient, ConfigProps configProps) {
    this.storageClient = storageClient;
    this.configProps = configProps;
  }

  private StorePath downloadUsingStream(String urlStr, String imageName) {
    HttpURLConnection connection = null;
    try {
      if (!urlStr.startsWith("http://")) {
        urlStr = "http://" + urlStr;
      }

      URL url = new URL(urlStr);
      connection = (HttpURLConnection) url.openConnection();

      configProps.getRequestHeaders().forEach(connection::setRequestProperty);
      connection.setRequestMethod(configProps.getRequestMethod());
      connection.setConnectTimeout(configProps.getTimeout());

      try (BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
           FileOutputStream fis = new FileOutputStream("/tmp/" + imageName)) {
        byte[] buffer = new byte[2048];
        int count = 0;
        while ((count = bis.read(buffer, 0, 2048)) != -1) {
          fis.write(buffer, 0, count);
        }
      }

      File file = new File("/tmp/" + imageName);
      InputStream inputStream = new FileInputStream("/tmp/" + imageName);

//      String fileExtension = StringUtils.getFilenameExtension(urlStr);
      String fileExtension = null;
      if (urlStr.lastIndexOf(".jpg") > -1) {
        fileExtension = "jpg";
      }
      if (urlStr.lastIndexOf(".png") > -1) {
        fileExtension = "png";
      }
      if (urlStr.lastIndexOf(".gif") > -1) {
        fileExtension = "gif";
      }

      return storageClient
         .uploadFile(inputStream, file.length(), fileExtension, new HashSet<>());
    } catch (Exception e) {
      log.warn("Failed to download pic via url({})", urlStr, e);
      if (connection != null) {
        connection.disconnect();
      }
    } finally {
      try {
        Files.delete(Paths.get("tmp").resolve(imageName));
      } catch (IOException e) {
        log.warn("The local copy is failed to be deleted, please manually delte it latter, image={}", imageName);
      }
    }
    return null;
  }

  /**
   * Retrieve image bytes
   */
  public byte[] retrieveImageBytes(String group, String path) {
    return storageClient.downloadFile(group, path, new DownloadByteArray());
  }

  /**
   * Process this image
   */
  public StorePath process(String url) {
    String imageName = Base64.getEncoder().encodeToString(url.getBytes());
    log.info("will download image ({}) with url={} ", imageName, url);

    int count = 0;
    do {
      try {
        //download the pic
        return downloadUsingStream(url, imageName);
      } catch (Exception e) {
        if (count < configProps.getRetries()) {
          log.info("Retry downloading: image ({}) with url={} ", imageName, url);
          count++;
        } else {
          log.warn("failed to download: {}", url, e);
        }
      }
    } while (count < configProps.getRetries());
    return null;
  }

}