package co.sdslabs.mdg.ndnmail;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmRepository {
    private static RealmRepository instance;
    public Realm realm;

    private RealmRepository() {
        realm = Realm.getDefaultInstance();
    }

    public static RealmRepository getInstance() {
        if (instance == null) {
            instance = new RealmRepository();

        }
        return instance;
    }

    public static RealmRepository getInstanceForNonUI() {
        return new RealmRepository();
    }

    public void createInstance() {
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
    }

    public ArrayList<Mail> getAllMails() {
        realm.beginTransaction();
        RealmResults<MailRealm> mailRealms = realm.where(MailRealm.class).findAll();
        ArrayList<Mail> mails = new ArrayList<>();
        for (MailRealm mail : mailRealms) {
            mails.add(mailRealmToMail(mail));
        }
        realm.commitTransaction();
        return mails;
    }

    private Mail mailRealmToMail(MailRealm mailRealm) {
        Mail mail = new Mail();
        if (mailRealm == null) return mail;
        mail.setContent(mailRealm.getContent());
        mail.setReceiver(mailRealm.getReceiver());
        mail.setDate(mailRealm.getDate());
        mail.setSender(mailRealm.getSender());
        mail.setSubject(mailRealm.getSubject());
        mail.setId(mailRealm.getId());
        return mail;
    }
}
