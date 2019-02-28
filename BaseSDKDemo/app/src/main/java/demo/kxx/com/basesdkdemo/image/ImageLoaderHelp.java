package demo.kxx.com.basesdkdemo.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * @author : kongxx
 * @Company : 爱问医联
 * @Created Date : 2019/2/28 11:06
 * @Description : BaseSDKDemo
 */
public class ImageLoaderHelp {
    public static final String TAG = "ImageLoaderHelp";
    /**
     * Context
     */
    public static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }


    public static void displayImage(Context context, Object uri, ImageView imageView, int defaultId, final ImageLoadingListener imageLoadingListener) {

        if (null != imageLoadingListener) {
            if (imageView.getVisibility() != View.VISIBLE) {
                throw new IllegalArgumentException("ImageView 应该设置可见！图片下载移步 @link downloadOnlyImageToBitmap()");
            }

            Glide.with(context).load(uri).placeholder(defaultId).fitCenter().listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    imageLoadingListener.onLoadingFailed("", null);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                    // TODO: 2018/9/13 如果ImageView 设置View.GONE，不会回调监听
                    BitmapDrawable bd = (BitmapDrawable) resource;
                    Bitmap bitmap = bd.getBitmap();
                    imageLoadingListener.onLoadingComplete("", null, bitmap);

                    return false;
                }
            }).into(imageView);


        } else {
            Glide.with(context).load(uri).placeholder(defaultId).fitCenter().into(imageView);
        }
    }
}
