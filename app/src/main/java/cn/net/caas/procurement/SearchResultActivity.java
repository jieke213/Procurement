package cn.net.caas.procurement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import cn.net.caas.procurement.util.SvgUtil;

/**
 * Created by wjj on 2017/5/3.
 */
public class SearchResultActivity extends AppCompatActivity {
    private static final int SEARCH_RESULT_CODE = 002;
    private EditText et_searchresult;
    private Toolbar toolbar_searchresult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);

        initView();

        Intent intent = getIntent();
        String searchContent = intent.getStringExtra("searchContent");
        et_searchresult.setText(searchContent);

        /**
         *  将.svg文件转为bitmap
         */
//        ImageView image_searchresult= (ImageView) findViewById(R.id.image_searchresult);
//        Bitmap bitmapFromSvg = SvgUtil.getBitmapFromSvg(SearchResultActivity.this);
//        Log.i("123","bitmapFromSvg: "+bitmapFromSvg);
//        Log.i("123","bitmapFromSvg.getHeight(): "+bitmapFromSvg.getHeight());
//        Log.i("123","bitmapFromSvg.getWidth(): "+bitmapFromSvg.getWidth());
//        Log.i("123","bitmapFromSvg.getRowBytes(): "+bitmapFromSvg.getRowBytes());
//        Log.i("123","bitmapFromSvg.getByteCount(): "+bitmapFromSvg.getByteCount());
//        image_searchresult.setImageBitmap(bitmapFromSvg);

    }

    private void initView() {
        et_searchresult= (EditText) findViewById(R.id.et_searchresult);
        toolbar_searchresult= (Toolbar) findViewById(R.id.toolbar_searchresult);
        setSupportActionBar(toolbar_searchresult);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_searchresult.setNavigationIcon(R.drawable.toolbar_back2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
