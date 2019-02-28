package sdk.kxx.com.appsdk.image;

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

import java.io.File;

import sdk.kxx.com.appsdk.R;

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

    /**
     * 加载过度图片id
     */
    public static final int DEFAULT_ID = R.drawable.ic_launcher;

    /**
     * 加载网络图片
     *
     * @param uri       url
     * @param imageView ImageView
     */
    public static void displayImage(String uri, ImageView imageView) {
        displayImage(mContext, uri, imageView, DEFAULT_ID, null);
    }

    /**
     * 加载网络图片
     *
     * @param uri        url
     * @param imageView  ImageView
     * @param imgDefault 默认过渡图片
     */
    public static void displayImage(String uri, ImageView imageView, int imgDefault) {
        displayImage(mContext, uri, imageView, imgDefault, null);
    }


    /**
     * 加载本地资源图片
     *
     * @param resourceId 资源id
     * @param imageView  ImageView
     */
    public static void displayImage(int resourceId, ImageView imageView) {
        displayImage(mContext, resourceId, imageView, null);
    }

    /**
     * 加载本地图片文件
     *
     * @param file      图片File
     * @param imageView ImageView
     */
    public static void displayImage(File file, ImageView imageView) {
        displayImage(mContext, file, imageView, null);
    }

    /**
     * 加载本地图片文件
     *
     * @param file                     图片File
     * @param imageView                ImageView
     * @param iAskImageLoadingListener listener
     */
    public static void displayImage(File file, ImageView imageView, ImageLoadingListener iAskImageLoadingListener) {
        displayImage(mContext, file, imageView, iAskImageLoadingListener);
    }


    /**
     * 加载本地图片文件
     *
     * @param context              Context
     * @param file                 文件
     * @param imageView            ImageView
     * @param imageLoadingListener 加载监听
     */
    public static void displayImage(Context context, File file, ImageView imageView, final ImageLoadingListener imageLoadingListener) {

        if (null != imageLoadingListener) {
            Glide.with(context).load(file).fitCenter().listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    imageLoadingListener.onLoadingFailed("", null);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {


                    BitmapDrawable bd = (BitmapDrawable) resource;
                    Bitmap bitmap = bd.getBitmap();
                    imageLoadingListener.onLoadingComplete("", null, bitmap);

                    return false;
                }
            }).into(imageView);
        } else {
            Glide.with(context).load(file).fitCenter().into(imageView);
        }
    }


    /**
     * 加载资源文件里面的图片
     *
     * @param context              Context
     * @param resourceId           资源id
     * @param imageView            ImageView
     * @param imageLoadingListener 加载监听
     */
    public static void displayImage(Context context, int resourceId, ImageView imageView, final ImageLoadingListener imageLoadingListener) {

        if (null != imageLoadingListener) {
            throw new UnsupportedOperationException("加载资源文件还需要监听?是加载了多大的图片！！！");
        } else {
            Glide.with(context).load(resourceId).fitCenter().into(imageView);
        }
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
