import net.named_data.jndn.*;
import java.io.IOException;
import net.named_data.jndn.util.Blob;
import net.named_data.jndn.security.*;
import net.named_data.jndn.security.pib.*;
import net.named_data.jndn.security.identity.*;
import net.named_data.jndn.security.v2.CertificateV2;
import java.util.Base64;
import org.json.*;
import java.nio.ByteBuffer;

public class Server {
    public static void main(String[] argv) throws Exception {
        Face face = new Face();

        KeyChain keyChain;

        try {
            keyChain = buildTestKeyChain();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Name defaultCertificateName;
        try {
            defaultCertificateName = keyChain.getDefaultCertificateName();
        } catch (Exception e) {
            e.printStackTrace();
            defaultCertificateName = new Name("/bogus/certificate/name");
        }
        face.setCommandSigningInfo(keyChain, defaultCertificateName);

        try{
            face.registerPrefix(new Name("/ndn/CA/_PROBE"), onCertInterest,
                new OnRegisterFailed() {
                    @Override
                    public void onRegisterFailed(Name prefix) {
                        System.out.println("Registration Failure");

                    }
                },
                new OnRegisterSuccess() {
                    @Override
                    public void onRegisterSuccess(Name prefix, long registeredPrefixId) {
                        System.out.println("Registration Success for prefix: " + prefix.toUri() + ", id: " + registeredPrefixId);

                    }
                }
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }


        // Loop calling processEvents until a callback sets enabled[0] = false.
        while (true) {
            try {
                face.processEvents();
                // System.out.println("beep");

                // We need to sleep for a few milliseconds so we don't use 100% of
                //   the CPU.
                Thread.sleep(500);
//                System.out.println("whiler");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

      /**
   * Setup an in-memory KeyChain with a default identity.
   *
   * @return
   * @throws net.named_data.jndn.security.SecurityException
   */
    public static KeyChain buildTestKeyChain() throws net.named_data.jndn.security.SecurityException {
        MemoryIdentityStorage identityStorage = new MemoryIdentityStorage();
        MemoryPrivateKeyStorage privateKeyStorage = new MemoryPrivateKeyStorage();
        IdentityManager identityManager = new IdentityManager(identityStorage, privateKeyStorage);
        KeyChain keyChain = new KeyChain(identityManager);
        try {
        keyChain.getDefaultCertificateName();
        } catch (net.named_data.jndn.security.SecurityException e) {
        keyChain.createIdentityAndCertificate(new Name("/test/identity"));
        keyChain.getIdentityManager().setDefaultIdentity(new Name("/test/identity"));
        }
        return keyChain;
  }

    public static final OnInterestCallback onCertInterest = new OnInterestCallback() {
        @Override
        public void onInterest(Name prefix, Interest interest, Face face, long interestFilterId, InterestFilter filter) {
            System.out.println("Called onCertInterest with Interest: "+ interest.getName().toUri());
            System.out.println("Called onCertInterest with Interest: "+ interest.getApplicationParameters().size());

            System.out.println("Got data");
            try{
                Blob blob = interest.getApplicationParameters();
                // byte[] bytes = k.getBytes( StandardCharsets.UTF_8 );
                byte[] arr = new byte[blob.buf().remaining()];
                blob.buf().get(arr);

                for (byte a :arr) {
                    System.out.print(a);
                }
                Base64.Decoder decoder = Base64.getDecoder();

                JSONObject jo = new JSONObject(new String(arr));
                System.out.println(blob.size());

                for (Object key : jo.keySet()) {
                    //based on you key types
                    String keyStr = (String)key;
                    Object keyvalue = jo.get(keyStr);
                    //Print key and value
                    System.out.println("key: "+ keyStr + " value: " + keyvalue);
                    //for nested objects iteration if required
                }
                byte[] certBytes = decoder.decode(jo.getString("name"));
                CertificateV2 certificateV2 = null;

                certificateV2 = new CertificateV2();
                certificateV2.wireDecode(ByteBuffer.wrap(certBytes));
            }catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    };
}