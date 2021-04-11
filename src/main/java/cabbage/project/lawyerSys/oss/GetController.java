package cabbage.project.lawyerSys.oss;

import cabbage.project.lawyerSys.common.utils.R;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.Date;

@RestController
@RequestMapping("oss")
public class GetController {

  @Value("${alibaba.cloud.oss.endpoint}")
  private String endpoint;
  @Value("${alibaba.cloud.oss.bucket}")
  private String bucket;

  @Value("${alibaba.cloud.access-key}")
  private String accessId;

  @Value("${alibaba.cloud.secret-key}")
  private String secretKey;

  @RequestMapping("/get")
  public R getFile(@RequestParam("objectName") String objectName) {
    OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, secretKey);
    Date expiration = new Date(new Date().getTime() + 200 * 1000);
//    URL url = ossClient.generatePresignedUrl(bucket, objectName, expiration);
    GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, objectName, HttpMethod.GET);
    request.setExpiration(expiration);
// 通过HTTP GET请求生成签名URL。
    URL signedUrl = ossClient.generatePresignedUrl(request);

    return R.ok().put("url", signedUrl);
  }


}
