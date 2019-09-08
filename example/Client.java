import net.named_data.jndn.*;
import net.named_data.jndn.util.SegmentFetcher;
import java.io.IOException;
import net.named_data.jndn.util.Blob;
import net.named_data.jndn.security.v2.ValidationPolicyAcceptAll;
import net.named_data.jndn.security.v2.Validator;
import net.named_data.jndn.NetworkNack;
import net.named_data.jndn.security.tpm.*;
import net.named_data.jndn.security.*;
import net.named_data.jndn.security.pib.*;
import net.named_data.jndn.security.identity.*;
import java.nio.ByteBuffer;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import org.json.JSONObject;
import net.named_data.jndn.security.v2.CertificateV2;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class Client {

    static Interest generateProbeInterest() {
        Name interestName = new Name("/ndn");
        interestName.append("CA").append("_PROBE");
        Interest interest = new Interest(interestName);
        interest.setMustBeFresh(true);
        interest.setCanBePrefix(false);

        JSONObject jo = new JSONObject();
        Base64.Encoder encoder = Base64.getEncoder();

        PibIdentity identity = null;
        PibKey m_key = null;
        KeyChain keyChain = null;
        // Pib pib = null;
        try{
            keyChain = new KeyChain("pib-memory:", "tpm-memory:");
        } catch (Exception e) {
            System.out.println("error");
        }
        try {
            identity = keyChain.createIdentityV2(new Name("/ndn"));
            m_key = identity.getDefaultKey();
        } catch (PibImpl.Error | Pib.Error | TpmBackEnd.Error | Tpm.Error | KeyChain.Error er) {
            er.printStackTrace();
        }

        System.out.println(m_key.getName().toUri());

        // generate certificate request
        Data data = new Data(new Name("/ndn/13408565461642704842/KEY/yq%1E%D23z%C2%5C/cert-request/%FD%00%00%01l%A1D%B4%5D"));
        // Data data = new Data(new Name("/ndn/KEY/%22%B9y%CA%B6%81Kl/cert-request"));

        data.setContent(m_key.getPublicKey());
        MetaInfo metaInfo = new MetaInfo();
        metaInfo.setType(ContentType.KEY);
        metaInfo.setFreshnessPeriod(TimeUnit.HOURS.toMillis(24));
        data.setMetaInfo(metaInfo);
        CertificateV2 certRequest = null;
        try {
            certRequest = new CertificateV2(data);
        } catch (CertificateV2.Error error) {
            System.out.println("error");
            error.printStackTrace();
        }

        System.out.println((new Name(m_key.getName()).append("cert-request")).toUri());

        byte[] arr = new byte[certRequest.wireEncode().buf().remaining()];
        certRequest.wireEncode().buf().get(arr);
        String certString = encoder.encodeToString(arr);

        jo.put("name", certString);

        Blob blob = new Blob(jo.toString().getBytes());
        for (byte a :jo.toString().getBytes()) {
            System.out.print(a);

        }
        System.out.println();
        System.out.println("Sending blob: " + jo.toString());
        System.out.println("Sending blob with size: " + blob.size());

        interest.setApplicationParameters(blob);

        interest.appendParametersDigestToName();
        return interest;
    }

    final static OnData onData = new OnData() {
        @Override
        public void onData(Interest interest, Data data) {
            System.out.println("Got data");

        }
    };

    final static OnTimeout onTimeout = new OnTimeout() {
        @Override
        public void onTimeout(Interest interest) {
            System.out.println("Timeout");
        }
    };

    final static OnNetworkNack onNack = new OnNetworkNack() {
        @Override
        public void onNetworkNack(Interest interest, NetworkNack networkNack) {
            System.out.println("Got Nack");
        }
    };

    public static void main(String[] argv) {
        Face face = new Face();

        Interest interest = generateProbeInterest();
        System.out.println("interest: " + interest.getName());
        try {
            face.expressInterest(interest, onData, onTimeout);
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }

        // Loop calling processEvents until a callback sets enabled[0] = false.
        while (true) {
            try {
                face.processEvents();
                // We need to sleep for a few milliseconds so we don't use 100% of
                //   the CPU.
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}