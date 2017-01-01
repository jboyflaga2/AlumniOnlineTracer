package example.com.sampleproject.core;

import android.app.Activity;

import javax.inject.Inject;

import mortar.MortarScope;
import mortar.Presenter;
import mortar.bundler.BundleService;


public class WindowOwnerPresenter extends Presenter<WindowOwnerPresenter.View> {

    public interface View {
        MortarScope getMortarScope();

        Activity getActivity();

    }

    @Inject
    public WindowOwnerPresenter() {
    }

//    @Override
//    public WindowOwnerPresenter.View getView() {
//
//    }

//    @Override
//    protected MortarScope extractScope(View View) {
//        return View.getMortarScope();
//    }

    @Override
    protected BundleService extractBundleService(View view) {
        if (view == null) return null;
        return BundleService.getBundleService(view.getMortarScope());
    }

    public Activity getActivity() {
        if (getView() == null) return null;

        return getView().getActivity();
    }
}