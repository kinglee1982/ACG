package com.kcx.acg.views.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.api.GetSearchTitleApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.GetSearchTitleBean;
import com.kcx.acg.bean.TabEntity;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.adapter.LinkAdapter;
import com.kcx.acg.views.adapter.TabFragmentPagerAdapter;
import com.kcx.acg.views.fragment.AllResultFragment;
import com.kcx.acg.views.fragment.HobbyFragment;
import com.kcx.acg.views.fragment.LazyFragment;
import com.kcx.acg.views.fragment.UsersFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.jessyan.autosize.internal.CancelAdapt;

import static com.kcx.acg.views.activity.SearchActivity.KEY_SEARCH_HISTORY_LIST;

/**
 */

public class SearchResultActivity extends BaseActivity implements CancelAdapt {

    private static final Integer[] STRING_TABS = {R.string.search_result_all_msg, R.string.search_result_hobby_msg, R.string.search_result_users_msg};

    private View rootView;
    private ArrayList<? extends LazyFragment<SearchResultActivity>> fragments;
    private CommonTabLayout tabLayout;
    public ViewPager viewPager;
    private FragmentManager fragmentManager;
    private ImageView deleteIv;
    private TextView cancelTv;
    private EditText searchEt;
    private LinearLayout linkLayout;
    private LinearLayout keywordLayout;
    private RecyclerView linkRv;
    private TextView keyWordTv;
    private DelegateAdapter delegateAdapter;
    private LinkAdapter linkAdapter;
    private List<String> linkList;
    public String searchString;
    private TabFragmentPagerAdapter adapter;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_search_result, null);
        linkLayout = rootView.findViewById(R.id.search_result_link_layout);
        linkRv = rootView.findViewById(R.id.search_result_link_rv);
        keyWordTv = rootView.findViewById(R.id.search_result_keyword_tv);
        keywordLayout = rootView.findViewById(R.id.search_result_keyword_layout);
        deleteIv = rootView.findViewById(R.id.search_result_delete_iv);
        cancelTv = rootView.findViewById(R.id.search_result_cancel_tv);
        searchEt = rootView.findViewById(R.id.search_result_edittext);
        tabLayout = rootView.findViewById(R.id.search_result_tablayout);
        viewPager = rootView.findViewById(R.id.search_result_viewpager);
        viewPager.setOffscreenPageLimit(3);

        linkRv.setLayoutManager(layoutManager);
        linkRv.setRecycledViewPool(viewPool);
        bindView(linkRv);
        return rootView;
    }

    @Override
    public void initData() {
        searchString = getIntent().getStringExtra(SearchActivity.SEARCH_STRING);
        searchEt.setText(searchString);
        searchEt.setSelection(searchString.length());


        ArrayList<CustomTabEntity> tabEntities = Lists.newArrayList();
        for (int tab :
                STRING_TABS) {
            tabEntities.add(new TabEntity(getString(tab), 0, 0));
        }


        addFragments(searchString);

        tabLayout.setTabData(tabEntities);

        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setDividerHeight(2);
        linearLayoutHelper.setBgColor(ContextCompat.getColor(this, R.color.gray_eee));
        linkList = Lists.newArrayList();
        linkAdapter = new LinkAdapter(this, linkList, linearLayoutHelper, searchEt.getText().toString());
        delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.addAdapter(linkAdapter);
        linkRv.setAdapter(delegateAdapter);
    }

    @Override
    public void setListener() {
        deleteIv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
        keywordLayout.setOnClickListener(this);
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                linkLayout.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence.toString())) {
                    linkLayout.setVisibility(View.GONE);
                } else {
                    getSearchTitle(charSequence.toString());
                    String searchString = getString(R.string.search_of_keyword_msg, charSequence);
                    SpannableString spannableString = new SpannableString(searchString);
                    ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(SearchResultActivity.this, R.color.pink_ff8));
                    spannableString.setSpan(span, searchString.indexOf(charSequence.toString()), searchString.indexOf(charSequence.toString()) + charSequence.toString().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    keyWordTv.setText(spannableString);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position, true);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        linkAdapter.setOnItemClickListener(new LinkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String string) {
                searchString = string;
                searchEt.setText(searchString);
                addFragments(searchString);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        searchEt.setText("");
                        linkLayout.setVisibility(View.GONE);

                    }
                }, 500);
            }
        });
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    addFragments(searchEt.getText().toString());
                    linkLayout.setVisibility(View.GONE);
                    Hidekeyboard(searchEt);
                    return true;
                }
                return false;
            }
        });
        searchEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linkLayout.setVisibility(View.VISIBLE);
                getSearchTitle(searchEt.getText().toString());
                String searchString = getString(R.string.search_of_keyword_msg, searchEt.getText().toString());
                SpannableString spannableString = new SpannableString(searchString);
                ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(SearchResultActivity.this, R.color.pink_ff8));
                spannableString.setSpan(span, searchString.indexOf(searchEt.getText().toString()), searchString.indexOf(searchEt.getText().toString()) + searchEt.getText().toString().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                keyWordTv.setText(spannableString);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_result_delete_iv:
                searchEt.setText("");
                finish();
                break;
            case R.id.search_result_cancel_tv:
                EventBus.getDefault().post(new BusEvent(BusEvent.GO_HOME_CODE, true));
                finish();
                break;
            case R.id.search_result_keyword_layout:
                addFragments(searchEt.getText().toString());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        searchEt.setText("");
                        linkLayout.setVisibility(View.GONE);

                    }
                }, 500);
                break;
        }
    }

    public void addFragments(String searchString) {
        Bundle bundle = new Bundle();
        bundle.putString(SearchActivity.SEARCH_STRING, searchString);
        AllResultFragment allResultFragment = new AllResultFragment();
        allResultFragment.setArguments(bundle);

        HobbyFragment hobbyFragment = new HobbyFragment();
        hobbyFragment.setArguments(bundle);

        UsersFragment usersFragment = new UsersFragment();
        usersFragment.setArguments(bundle);

        fragments = Lists.newArrayList(allResultFragment, hobbyFragment, usersFragment);
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        Set<String> historySet = (Set<String>) SPUtil.readObject(this, SearchActivity.KEY_SEARCH_HISTORY);
        historySet.add(searchString);
        SPUtil.saveObject(this, SearchActivity.KEY_SEARCH_HISTORY, historySet);
        List<String> historyList = (List<String>) SPUtil.readObject(this, KEY_SEARCH_HISTORY_LIST);
        historyList.add(searchString);
        SPUtil.saveObject(this, SearchActivity.KEY_SEARCH_HISTORY_LIST, historyList);
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
