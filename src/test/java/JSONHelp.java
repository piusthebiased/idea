import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import java.net.UnknownHostException;

public class JSONHelp {
    public static void main(String[] args) throws UnknownHostException {
        JSONObject object = new JSONObject();
        object.put("test", null);
        System.out.println(JSONObject.toJSONString(object, SerializerFeature.WriteMapNullValue));
        System.out.println(object.toString());
    }
}
