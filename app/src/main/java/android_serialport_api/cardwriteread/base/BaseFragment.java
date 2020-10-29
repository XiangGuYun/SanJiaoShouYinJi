package android_serialport_api.cardwriteread.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment {
    public View view;
    public Context context;
    public T mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (this.context == null){
            context = container.getContext();
        }
        view = inflater.inflate(getLayoutId(),null);
        bindView();
        init();
        return view;
    }

    /**
     * 绑定view
     */
    private void bindView(){
        initPresenter();
        if (mPresenter != null){
            mPresenter.attachView((BaseView) this);
        }
    }

    protected abstract void initPresenter();

    protected abstract void init();

    protected abstract int getLayoutId();

    public View getView(){
        return view;
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

}
