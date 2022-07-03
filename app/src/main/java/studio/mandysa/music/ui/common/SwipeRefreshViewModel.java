package studio.mandysa.music.ui.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public abstract class SwipeRefreshViewModel extends ViewModel {
    protected final MutableLiveData<Boolean> mIsRefreshingLiveData = new MutableLiveData<>(false);

    public final LiveData<Boolean> isRefreshing = mIsRefreshingLiveData;

    abstract public void init();

    abstract public void refresh();

}
