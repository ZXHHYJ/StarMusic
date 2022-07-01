package studio.mandysa.music.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import studio.mandysa.music.ui.common.State;

public abstract class BaseViewModel extends ViewModel {
    protected final MutableLiveData<State> stateLiveData = new MutableLiveData<>(State.LOADING);

    @NonNull
    public final LiveData<State> getState() {
        return stateLiveData;
    }

    public BaseViewModel() {
        loading();
    }

    abstract public void loading();

    abstract public void refresh();

}
