package com.study.mli.dobe.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.study.mli.dobe.R;
import com.study.mli.dobe.activity.tools.HomeAdapter;
import com.study.mli.dobe.app.DBGlobal;
import com.study.mli.dobe.cls.Picture;
import com.study.mli.dobe.customview.dialog.DBLoadingDialog;
import com.study.mli.dobe.customview.loader.PullToRefreshLayout;
import com.study.mli.dobe.customview.loader.PullableGridView;
import com.study.mli.dobe.tools.DBLog;
import com.study.mli.dobe.utils.loader.iGetDataListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private PullableGridView mGridView;
    private PullToRefreshLayout mMainView;
    private HomeAdapter mAdapter;
    private List<Picture> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mAdapter = new HomeAdapter(this, mList);
        mGridView.setAdapter(mAdapter);
        parser();
    }

    private void initView() {
        mGridView = (PullableGridView) findViewById(R.id.gv);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setOnItemClickListener(view, position, id);
            }
        });
        DBLog.i("GridView x:" + mGridView.getX() + " Y:" + mGridView.getY());
        mMainView = (PullToRefreshLayout) findViewById(R.id.main_layout);
        mMainView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                DBGlobal.parser.resetPage();
                loadData();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                loadData();
            }
        });
    }

    private void parser() {
        final DBLoadingDialog dialog = new DBLoadingDialog(this);
        dialog.show();
        DBGlobal.parser.reset(this, new iGetDataListener() {
            @Override
            public void onFinish(int page, List<Picture> pictures, boolean success) {
                if (success) {
                    mList.addAll(pictures);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "获取数据失败，请刷新重试", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    private void loadData() {
        DBGlobal.parser.loadData(this, new iGetDataListener() {
            @Override
            public void onFinish(int page, List<Picture> pictures, boolean success) {
                if (success) {
                    if (isRefresh(page)) {
                        mList.clear();
                        mMainView.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } else {
                        mMainView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    mList.addAll(pictures);
                    mAdapter.notifyDataSetChanged();
                } else {
                    if (isRefresh(page)) {
                        mMainView.refreshFinish(PullToRefreshLayout.FAIL);
                    } else {
                        mMainView.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }
                    Toast.makeText(getContext(), "获取数据失败，请刷新重试", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean isRefresh(int page) {
        return page == 0;
    }

    private Context getContext() {
        return MainActivity.this;
    }











    //test code
    private void writeHtml2File(byte[] bytes) {
        String fileName = "html.txt";
        File file = new File(getExternalFilesDir(null), fileName);
        try {
            FileOutputStream os = new FileOutputStream(file, false);
            os.write(bytes);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<String> parseHtml() {
        final List<String> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(DBGlobal.mInstance.URL).timeout(5000).get();
            String html = doc.toString();
            writeHtml2File(html.getBytes());
            Document content = Jsoup.parse(html);
            Element divs = content.select("div.picture-list").get(1);
            Document divDoc = Jsoup.parse(divs.toString());
            Elements elements = divDoc.getElementsByTag("a");
            for (Element e : elements) {
                String title = e.select("a").attr("href").trim();
                list.add(title);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
