package co.sdslabs.mdg.ndnmail;

import androidx.lifecycle.ViewModel;

public class DatabaseViewModel extends ViewModel {
    public final RealmRepository repository;

    public DatabaseViewModel() {
        repository = RealmRepository.getInstance();
    }

    public void createInstance() {
        repository.createInstance();
    }

}