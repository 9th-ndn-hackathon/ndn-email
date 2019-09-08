package co.sdslabs.mdg.server;

import net.named_data.jndn.Face;
import net.named_data.jndn.Name;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.security.identity.IdentityManager;
import net.named_data.jndn.security.identity.MemoryIdentityStorage;
import net.named_data.jndn.security.identity.MemoryPrivateKeyStorage;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class Global {

    public static Face face;
    public static KeyChain keyChain;

    public static void setup() {
        if (face != null) return;
        face = new Face();

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

        Thread networkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        face.processEvents();
                        sleep(1000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        networkThread.start();
    }

    /**
     * Setup an in-memory KeyChain with a default identity.
     *
     * @return
     * @throws net.named_data.jndn.security.SecurityException
     */
    static KeyChain buildTestKeyChain() throws net.named_data.jndn.security.SecurityException {
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

}
