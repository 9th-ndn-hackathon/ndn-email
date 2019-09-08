package co.sdslabs.mdg.server;

import net.named_data.jndn.OnInterestCallback;
import net.named_data.jndn.security.v2.CertificateV2;
import net.named_data.jndn.util.Blob;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.Base64;

public class Login {
    static OnInterestCallback loginRequest = (name, interest, face, l, interestFilter) -> {
        System.out.println("Called login with Interest: " + interest.getName().toUri());
        System.out.println("Called login with Interest: " + interest.getApplicationParameters().size());
        System.out.println("Called login with Interest: " + name);
        System.out.println("Called login with Interest: " + l);

        System.out.println("Got data");
        try {
            Blob blob = interest.getApplicationParameters();
            // byte[] bytes = k.getBytes( StandardCharsets.UTF_8 );
            byte[] arr = new byte[blob.buf().remaining()];
            blob.buf().get(arr);

            for (byte a : arr) {
                System.out.print(a);
            }
            Base64.Decoder decoder = Base64.getDecoder();

            JSONObject jo = new JSONObject(new String(arr));

            System.out.println(blob.size());

            for (Object key : jo.keySet()) {
                //based on you key types
                String keyStr = (String) key;
                Object keyvalue = jo.get(keyStr);
                //Print key and value
                System.out.println("key: " + keyStr + " value: " + keyvalue);
                //for nested objects iteration if required
            }
            byte[] certBytes = decoder.decode(jo.getString("name"));
            CertificateV2 certificateV2 = null;

            certificateV2 = new CertificateV2();
            certificateV2.wireDecode(ByteBuffer.wrap(certBytes));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    };

}
