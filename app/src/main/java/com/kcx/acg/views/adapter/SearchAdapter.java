package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.bean.GetSearchRelatedProductBean;
import com.kcx.acg.bean.GetSearchRelatedUsersBean;
import com.kcx.acg.bean.HotTagBean;
import com.kcx.acg.conf.Constants;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 */

public class SearchAdapter extends DelegateAdapter.Adapter {

    private Context context;
    private LayoutHelper layoutHelper;
    private Integer layoutId;
    private int type;
    private List<HotTagBean.ReturnDataBean.ListBean> hotTagList;
    private List<GetSearchRelatedUsersBean.ReturnDataBean.ListBean> userList;
    private List<GetSearchRelatedProductBean.ReturnDataBean.ListBean> worksList;
    private HobbyHolder hobbyHolder;
    private UsersHolder usersHolder;
    private WorksHolder worksHolder;
    private String keyWord;

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public SearchAdapter(Context context, LayoutHelper layoutHelper, Integer layoutId, List<HotTagBean.ReturnDataBean.ListBean> hotTagList) {
        this.context = context;
        this.hotTagList = hotTagList;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
    }

    public SearchAdapter(Context context, LayoutHelper layoutHelper, Integer layoutId, List<GetSearchRelatedUsersBean.ReturnDataBean.ListBean> userList, int type, int type2) {
        this.context = context;
        this.userList = userList;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
    }

    public SearchAdapter(Context context, LayoutHelper layoutHelper, Integer layoutId, List<GetSearchRelatedProductBean.ReturnDataBean.ListBean> worksList, int type) {
        this.context = context;
        this.worksList = worksList;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.type = type;
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (layoutId) {
            case R.layout.search_hobby_item_layout:
                return new HobbyHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
            case R.layout.search_users_item_layout:
                return new UsersHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
            case R.layout.search_works_item_layout:
                return new WorksHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
            default:
                return null;
        }

    }

    public void setHobbyVisibility(boolean visibility) {
        hobbyHolder.setVisibility(false);
    }

    public void setUsersVisibility(boolean visibility) {
        usersHolder.setVisibility(false);
    }

    public void setWorksVisibility(boolean visibility) {
        worksHolder.setVisibility(false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (layoutId) {
            case R.layout.search_hobby_item_layout:
                hobbyHolder = (HobbyHolder) holder;
                if (hotTagList != null && hotTagList.size() > 0) {
                    hobbyHolder.setVisibility(true);
                    MultiTransformation multi = new MultiTransformation(
                            new CenterCrop(),
                            new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL));
                    RequestOptions options = new RequestOptions().transform(multi).placeholder(R.drawable.img_holder_5dp).error(R.drawable.img_holder_5dp);
                    if (position == 4 && hotTagList.size() == 5) {
                        hobbyHolder.imgIv.setBackgroundResource(R.drawable.shape_gray_bg3);
                        hobbyHolder.imgIv.setImageResource(R.mipmap.arrow_right);
                        hobbyHolder.labelTv.setText(context.getString(R.string.more_msg));
                    } else {
                        if (position < hotTagList.size()) {
                            Glide.with(context).load(hotTagList.get(position).getTagPhoto()).apply(options).into(hobbyHolder.imgIv);
                            hobbyHolder.labelTv.setText(hotTagList.get(position).getTagName());
                        }
                    }
                    hobbyHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (position == 4) {
                                onHobbyListener.onMore(view, position);
                            } else {
                                onHobbyListener.onHobby(view, position);
                            }
                        }
                    });
                } else {
                    hobbyHolder.setVisibility(false);
                }
                break;
            case R.layout.search_users_item_layout:
                usersHolder = (UsersHolder) holder;
                if (userList != null && userList.size() > 0) {
                    usersHolder.setVisibility(true);
                    RequestOptions mRequestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.mipmap.placehold_head)
                            .error(R.mipmap.placehold_head);
                    if (position == 4 && userList.size() == 5) {
                        usersHolder.headIv.setBackgroundResource(R.drawable.shape_cricle_gray_bg);
                        usersHolder.headIv.setImageResource(R.mipmap.arrow_right);
                        usersHolder.nameTv.setText(context.getString(R.string.more_msg));
                    } else {
//                        if (position < userList.size()) {

                        GetSearchRelatedUsersBean.ReturnDataBean.ListBean item = userList.get(position);
                        Glide.with(context).load(item.getPhoto()).apply(mRequestOptions).into(usersHolder.headIv);
                        switch (item.getUserIdentify()){
                            case Constants.Author.ID_F:
                                usersHolder.vipIv.setVisibility(View.VISIBLE);
                                break;
                            case Constants.Author.UNKNOWN:
                            default:
                                usersHolder.vipIv.setVisibility(View.GONE);
                                break;
                        }
                        String result = item.getUserName();
                        SpannableString spannableString = new SpannableString(result);
                        ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.pink_ff8));
                        spannableString.setSpan(span, result.toLowerCase().indexOf(keyWord.toLowerCase()), result.toLowerCase().indexOf(keyWord.toLowerCase()) + keyWord.toLowerCase().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                        usersHolder.nameTv.setText(spannableString);
                        usersHolder.nameTv.setText(item.getUserName());
//                        }
                    }
                    usersHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (position == 4) {
                                onUserListener.onMore(view, position);
                            } else {
                                onUserListener.onUser(view, position);
                            }
                        }
                    });
                } else {
                    usersHolder.setVisibility(false);
                }
                break;
            case R.layout.search_works_item_layout:
                worksHolder = (WorksHolder) holder;
                if (worksList != null && worksList.size() > 0) {
                    worksHolder.setVisibility(true);
                    MultiTransformation multi = new MultiTransformation(
                            new CenterCrop(),
                            new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL));
                    RequestOptions options = new RequestOptions().transform(multi).placeholder(R.drawable.home_img_holder).error(R.drawable.home_img_holder);
                    GetSearchRelatedProductBean.ReturnDataBean.ListBean item = worksList.get(position);
                    Glide.with(context).load(item.getCoverPicUrl()).apply(options).into(worksHolder.imgIv);
                    worksHolder.txtTv.setText(item.getTitle());
                } else {
                    worksHolder.setVisibility(false);
                }
                worksHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onWorkListener.onWork(view, position);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (layoutId) {
            case R.layout.search_hobby_item_layout:
//                return hotTagList != null  ? hotTagList.size() : 1;
                return 5;
            case R.layout.search_users_item_layout:
                return userList != null ? userList.size() : 1;
//                return 5;
            case R.layout.search_works_item_layout:
                return worksList != null ? worksList.size() : 1;
            default:
                return 0;
        }
    }

    class HobbyHolder extends RecyclerView.ViewHolder {
        private ImageView imgIv;
        private TextView labelTv;

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = FrameLayout.LayoutParams.WRAP_CONTENT;
                param.width = FrameLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }

        public HobbyHolder(final View itemView) {
            super(itemView);
            imgIv = itemView.findViewById(R.id.search_hobby_item_img_iv);
            labelTv = itemView.findViewById(R.id.search_hobby_item_lab_tv);
        }
    }

    class UsersHolder extends RecyclerView.ViewHolder {
        private ImageView headIv;
        private ImageView vipIv;
        private TextView nameTv;

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = FrameLayout.LayoutParams.WRAP_CONTENT;
                param.width = FrameLayout.LayoutParams.MATCH_PARENT;
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }

        public UsersHolder(final View itemView) {
            super(itemView);
            headIv = itemView.findViewById(R.id.search_user_item_head_iv);
            vipIv = itemView.findViewById(R.id.search_user_item_vip_iv);
            nameTv = itemView.findViewById(R.id.search_user_item_name_iv);

        }
    }

    class WorksHolder extends RecyclerView.ViewHolder {
        private ImageView imgIv;
        private TextView txtTv;

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = FrameLayout.LayoutParams.WRAP_CONTENT;
                param.width = FrameLayout.LayoutParams.MATCH_PARENT;
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }

        public WorksHolder(final View itemView) {
            super(itemView);
            imgIv = itemView.findViewById(R.id.search_works_item_img_iv);
            txtTv = itemView.findViewById(R.id.search_works_item_txt_iv);

        }
    }

    private OnHobbyListener onHobbyListener;
    private OnUserListener onUserListener;
    private OnWorkListener onWorkListener;

    public void setOnHobbyListener(OnHobbyListener onHobbyListener) {
        this.onHobbyListener = onHobbyListener;
    }

    public void setOnUserListener(OnUserListener onUserListener) {
        this.onUserListener = onUserListener;
    }

    public void setOnWorkListener(OnWorkListener onWorkListener) {
        this.onWorkListener = onWorkListener;
    }

    public interface OnHobbyListener {
        void onHobby(View view, int position);

        void onMore(View view, int position);
    }

    public interface OnUserListener {
        void onUser(View view, int position);

        void onMore(View view, int position);
    }

    public interface OnWorkListener {
        void onWork(View view, int position);
    }

    public interface OnUserMoreListener {
    }
}
