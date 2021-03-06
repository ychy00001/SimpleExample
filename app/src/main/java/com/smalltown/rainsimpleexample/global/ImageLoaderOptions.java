package com.smalltown.rainsimpleexample.global;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.smalltown.rainsimpleexample.R;

public class ImageLoaderOptions {
	//在显示小图的选项
	public static DisplayImageOptions list_options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.mipmap.load_img_def)// 加载过程中显示什么图片
			.showImageForEmptyUri(R.mipmap.load_img_def)// url为空的时候显示什么图片
			.showImageOnFail(R.mipmap.load_img_def)// 加载失败显示什么图片
			.cacheInMemory(true)// 在内存缓存
			.cacheOnDisk(true)// 在硬盘缓存
			.considerExifParams(true)// 会识别图片的方向信息
			.displayer(new FadeInBitmapDisplayer(500)).build();// 渐渐显示的动画
	// .displayer(new RoundedBitmapDisplayer(100)).build();//显示圆角或圆形图片

	//在显示大图的时候的选项，
	public static DisplayImageOptions pager_options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.mipmap.load_img_def)
			.showImageOnFail(R.mipmap.load_img_def)
			.resetViewBeforeLoading(true)//在ImageView显示图片之前先清空已有的图片内容
			.cacheOnDisk(true)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//会进一步将按照ImageView的宽高来缩放
			.bitmapConfig(Bitmap.Config.RGB_565)//设置颜色的渲染模式,是比较节省内存的模式
			.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(300)).build();
}
