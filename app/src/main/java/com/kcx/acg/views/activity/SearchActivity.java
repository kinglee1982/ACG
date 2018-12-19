package com.kcx.acg.views.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.donkingliang.labels.LabelsView;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kcx.acg.R;
import com.kcx.acg.api.GetSearchHotTagsApi;
import com.kcx.acg.api.GetSearchTitleApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.GetSearchHotTagsBean;
import com.kcx.acg.bean.GetSearchTitleBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.adapter.LinkAdapter;
import com.kcx.acg.views.view.BottomDialog2;
import com.library.flowlayout.FlowAdapter;
import com.library.flowlayout.LineFlowLayout;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import me.jessyan.autosize.internal.CancelAdapt;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 */

public class SearchActivity extends BaseActivity implements CancelAdapt  {

    public static final String SEARCH_STRING = "search_string";
    public static final String KEY_SEARCH_HISTORY = "key_search_history";
    public static final String KEY_SEARCH_HISTORY_LIST = "key_search_history_list";
    private View rootView;
    private TextView cancelTv;
    private TextView clearTv;
    private LabelsView hotKeyLV;
    private LineFlowLayout historyFlow;
    private RecyclerView linkRv;
    private TextView keyWordTv;
    private EditText searchEt;
    private LinearLayoutHelper linearLayoutHelper;
    private LinearLayout linkLayout;
    private LinearLayout keyWordLayout;
    private LinearLayout searchLayout;
    private List<String> linkList;
    private DelegateAdapter delegateAdapter;
    private LinkAdapter linkAdapter;
    private ImageView deleteIv;
    private List<String> historyList;
    private Set<String> historySet;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_search, null);
        cancelTv = rootView.findViewById(R.id.search_cancel_tv);
        clearTv = rootView.findViewById(R.id.search_clear_tv);
        searchEt = rootView.findViewById(R.id.search_edittext);
        deleteIv = rootView.findViewById(R.id.search_delete_iv);
        linkLayout = rootView.findViewById(R.id.search_link_layout);
        keyWordLayout = rootView.findViewById(R.id.search_keyword_layout);
        searchLayout = rootView.findViewById(R.id.search_layout);
        hotKeyLV = rootView.findViewById(R.id.search_hot_key_labelview);
        historyFlow = rootView.findViewById(R.id.search_history_flow);

        linkRv = rootView.findViewById(R.id.search_link_rv);
        keyWordTv = rootView.findViewById(R.id.search_keyword_tv);
        linkRv.setRecycledViewPool(viewPool);
        linkRv.setLayoutManager(layoutManager);
        bindView(linkRv);
        return rootView;
    }

    @Override
    public void initData() {
        getSearchHotTags();

        linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setDividerHeight(2);
        linearLayoutHelper.setBgColor(ContextCompat.getColor(this, R.color.gray_eee));

        linkList = Lists.newArrayList();
        linkAdapter = new LinkAdapter(SearchActivity.this, linkList, linearLayoutHelper, searchEt.getText().toString());
        delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.addAdapter(linkAdapter);
        linkRv.setAdapter(delegateAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        historyList = (List<String>) SPUtil.readObject(this, KEY_SEARCH_HISTORY_LIST);
        if(historyList!=null)
        historyList = removeDuplicate(historyList);

        if (historyList != null && historyList.size() > 0) {
            List<String> list = Lists.newArrayList(historyList);
            Collections.reverse(list);

            historyFlow.setAdapter(new FlowAdapter<String>(this, list) {
                @Override
                protected int generateLayout(int i) {
                    return R.layout.flow_item;
                }

                @Override
                protected void getView(final String s, View view) {
                    TextView text = view.findViewById(R.id.flow_text);
                    text.setText(s);
//                    text.setBackground(getBack());
                    text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityWithOptions(s);
                        }
                    });
                }

            });
        } else {
            clearTv.setVisibility(View.GONE);

        }

        historySet = (Set<String>) SPUtil.readObject(this, KEY_SEARCH_HISTORY);

//        if (historySet != null) {
//            for (String hisLabel :
//                    historySet) {
//                historyList.add(hisLabel);
//            }
//            historyLV.setLabels((List<String>) historyList);
//        } else {
//            clearTv.setVisibility(View.GONE);
//        }
    }

    @Override
    public void setListener() {
        cancelTv.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
        clearTv.setOnClickListener(this);
        keyWordLayout.setOnClickListener(this);
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(searchEt.getText())) {
                        startActivityWithOptions(searchEt.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    linkLayout.setVisibility(View.GONE);
                } else {
                    getSearchTitle(charSequence.toString().trim());
                    String searchString = getString(R.string.search_of_keyword_msg, charSequence);
                    SpannableString spannableString = new SpannableString(searchString);
                    ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(SearchActivity.this, R.color.pink_ff8));
                    spannableString.setSpan(span, searchString.indexOf(charSequence.toString().trim()), searchString.indexOf(charSequence.toString().trim()) + charSequence.toString().trim().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    keyWordTv.setText(spannableString);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        linkAdapter.setOnItemClickListener(new LinkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String searchString) {
                startActivityWithOptions(searchString);

            }
        });

        hotKeyLV.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                startActivityWithOptions(data.toString());
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_cancel_tv:
                finish();
                break;
            case R.id.search_delete_iv:
                searchEt.setText("");
                linkLayout.setVisibility(View.GONE);
//                delegateAdapter.notifyDataSetChanged();
                break;
            case R.id.search_clear_tv:
                BottomDialog2.Builder builder = new BottomDialog2.Builder(this);
                builder.setLayoutId(R.layout.dialog_clear_history).show();
                builder.setOnConfirmListener(new BottomDialog2.Builder.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        historyFlow.removeAllViews();
                        SPUtil.saveObject(SearchActivity.this, KEY_SEARCH_HISTORY, null);
                        historyList.clear();
                        SPUtil.saveObject(SearchActivity.this, KEY_SEARCH_HISTORY_LIST, historyList);
                        clearTv.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.search_keyword_layout:
                startActivityWithOptions(searchEt.getText().toString());
                break;
        }
    }

    private void startActivityWithOptions(String searchString) {
        if (TextUtils.isEmpty(searchString.trim())) {
            Hidekeyboard(searchEt);
            return;
        }

        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        intent.putExtra(SEARCH_STRING, searchString);
        if (android.os.Build.VERSION.SDK_INT > 20)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SearchActivity.this, searchLayout, "search").toBundle());
        else
            startActivity(intent);

        if (historySet == null)
            historySet = Sets.newHashSet();
        if (historySet.size() >= 20)
            historySet.remove(historyList.get(0));
        historySet.add(searchString);

        if (historyList == null)
            historyList = Lists.newArrayList();
        if (historyList.size() >= 20)
            historyList.remove(historyList.size() - 1);
        historyList.add(searchString);

        SPUtil.saveObject(this, KEY_SEARCH_HISTORY_LIST, historyList);
        SPUtil.saveObject(this, KEY_SEARCH_HISTORY, historySet);
        clearTv.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchEt.setText("");
                linkLayout.setVisibility(View.GONE);

            }
        }, 500);
    }

    public List removeDuplicate(List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    public void getSearchHotTags() {
        GetSearchHotTagsApi getSearchHotTagsApi = new GetSearchHotTagsApi(this);
        getSearchHotTagsApi.setListener(new HttpOnNextListener<GetSearchHotTagsBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetSearchHotTagsBean getSearchHotTagsBean) {
                List<String> labels = Lists.newArrayList();
                for (GetSearchHotTagsBean.ReturnDataBean.ListBean tag :
                        getSearchHotTagsBean.getReturnData().getList()) {
                    labels.add(tag.getName());
                }
                hotKeyLV.setLabels(labels);

                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, getSearchHotTagsApi);
    }

    public void getSearchTitle(final String searchKey) {
        GetSearchTitleApi getSearchTitleApi = new GetSearchTitleApi(this);
        getSearchTitleApi.setSearchKey(searchKey);
        getSearchTitleApi.setListener(new HttpOnNextListener<GetSearchTitleBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetSearchTitleBean getSearchTitleBean) {
                linkList.clear();
                for (GetSearchTitleBean.ReturnDataBean linkTitle :
                        getSearchTitleBean.getReturnData()) {
                    linkList.add(linkTitle.getTitle());
                }
                linkAdapter.setKeyWord(searchKey);
                linkLayout.setVisibility(View.VISIBLE);
                linkAdapter.notifyDataSetChanged();
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(this, getSearchTitleApi);
    }
}
