package xin.cosmos;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesResponse;
import org.apache.xmlbeans.impl.common.XmlStreamUtils;

public class MyTest {
    final static String APP_ID = "1307506702";
    final static String SECRET_ID = "AKIDPdaGfogHbfSyCArHPGq9BwRfcIhpgGZi";
    final static String SECRET_KEY = "tVquhDSRAcniyxf6G9l59wSheLksjJcf";
    public static void main(String[] args) {
        try {
            Credential cred = new Credential(SECRET_ID, SECRET_KEY);
            CvmClient client = new CvmClient(cred, "ap-shanghai");

            DescribeInstancesRequest req = new DescribeInstancesRequest();
            DescribeInstancesResponse resp = client.DescribeInstances(req);
            System.out.println(DescribeInstancesResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e);
        }
    }
}
