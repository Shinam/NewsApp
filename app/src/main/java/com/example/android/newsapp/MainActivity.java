package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener {
    private static final String USGS_REQUEST_URL =
            "https://content.guardianapis.com/search?api-key=test";
    private ListView newsListView;
    private TextView mEmptyStateTextView;
    public static List<News> mListNews;
    private static final int NEWS_LOADER_ID = 1;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsListView = (ListView) findViewById(R.id.list);


        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        mListNews = new ArrayList<News>();
        mAdapter = new NewsAdapter(this, 0, mListNews);

        final LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        final NewsAsyncTask task = new NewsAsyncTask();
        task.execute(USGS_REQUEST_URL);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void updateUi(List<News> book) {
        newsListView = (ListView) findViewById(R.id.list);

        NewsAdapter adapter = new NewsAdapter(MainActivity.this, 0, book);

        newsListView.setAdapter(adapter);
    }

    private class NewsAsyncTask extends AsyncTask<String, Void, List<News>> {
        @Override
        protected List<News> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<News> result = Utils.fetchNewsData(urls[0]);

            return result;
        }

        @Override
        protected void onPostExecute(List<News> result) {
            if (result == null) {
                mEmptyStateTextView.setText(R.string.noNew);
                return;
            }
            updateUi(result);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        mEmptyStateTextView.setText(R.string.loading);
        mAdapter.clear();

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
        else
        {
            mEmptyStateTextView.setText(R.string.noNew);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    @Override
    public void onRefresh() {
        MainActivity.this.getSupportLoaderManager().restartLoader(0, null, this);
    }

}