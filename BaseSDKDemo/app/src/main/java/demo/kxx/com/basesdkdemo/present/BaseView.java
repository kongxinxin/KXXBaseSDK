package demo.kxx.com.basesdkdemo.present;

import demo.kxx.com.basesdkdemo.model.ErrorItem;

/**
 * @author : kongxx
 * @Created Date : 2019/2/27 11:29
 * @Description : BaseSDKDemo
 */
public interface BaseView<T> {
    void setPresenter(T presenter);

    void getError(ErrorItem errorItem);
}
