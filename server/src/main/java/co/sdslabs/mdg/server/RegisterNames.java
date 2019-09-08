package co.sdslabs.mdg.server;

import net.named_data.jndn.Name;
import net.named_data.jndn.OnInterestCallback;

import static co.sdslabs.mdg.server.Login.loginRequest;

public class RegisterNames {

    public static final OnInterestCallback callback = (prefix, interest, face, interestFilterId, filter) -> {

        face.setInterestFilter(prefix.append("_PROBE"), loginRequest);
        System.out.println("Called callback with Interest: " + interest.getName().toUri());
        System.out.println("Called callback with Interest: " + interest.getApplicationParameters().size());
        System.out.println("Called callback with Interest: " + prefix);
        System.out.println("Called callback with Interest: " + interestFilterId);
        System.out.println("Called callback with Interest: " + filter);


    };

    public static void register() {
        System.out.println("Register!!");
        try {
            Global.face.registerPrefix(new Name("/ndn/CA/"), callback,
                    prefix -> System.out.println("Registration Failure"),
                    (prefix, registeredPrefixId) -> System.out.println("Registration Success for prefix: " + prefix.toUri() + ", id: " + registeredPrefixId)
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }

    }
}
