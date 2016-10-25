package com.study.mli.dobe.utils.loader;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;

import com.study.mli.dobe.app.DBGlobal;
import com.study.mli.dobe.cls.Picture;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by crown on 2016/9/8.
 */
public class ParserUtils implements Runnable {

	Handler handler;
	private Activity mActivity;
	private ExecutorService executorService;
	private iGetDataListener mListener;
	private int page = 0;

	public ParserUtils() {
		handler = new Handler();
		executorService = Executors.newCachedThreadPool();
	}

	public void loadData(Activity activity, iGetDataListener listener) {
		this.mActivity = activity;
		this.mListener = listener;
		executorService.execute(this);
	}

	public void resetPage() {
		page = 0;
	}

	public void reset(Activity activity, iGetDataListener listener) {
		page = 0;
		this.loadData(activity, listener);
	}

	public void parserHtml() {
		parser(3);
	}

	public void parser(int time) {
		if(time == 0) {
			return;
		}
		final List<Picture> list = new ArrayList<>();
		if(TextUtils.isEmpty(DBGlobal.mInstance.URL)) {
			return;
		}
		try {
			int loadPage = page+1;
			Document doc = Jsoup.connect(DBGlobal.mInstance.URL + loadPage).timeout(5000).get();
			String html = doc.toString();
			Document content = Jsoup.parse(html);
			Element divs = content.select("div.picture-list").get(1);
			Document divDoc = Jsoup.parse(divs.toString());
			Elements elements = divDoc.getElementsByTag("a");
			for (Element e : elements) {
				String id = e.attr("data-picture-id");
				String title = e.attr("alt");
				String hotLevel = e.attr("title");
				String imgUrl = e.select("a").attr("href").trim();
				String thumbnail = e.select("img").attr("src").trim();
				list.add(new Picture(id, title, imgUrl, thumbnail, hotLevel));
			}
			handler.post(new Runnable() {
				@Override
				public void run() {
					mListener.onFinish(page, list, true);
					page++;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			parser(time - 1);
			if(time - 1 == 0) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						mListener.onFinish(page, null, false);
					}
				});
			}
		}
	}

	@Override
	public void run() {
		parserHtml();
	}
}
