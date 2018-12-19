package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.collect.Maps;
import com.kcx.acg.R;
import com.kcx.acg.api.AttentionMemberApi;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.AttentionMemberBean;
import com.kcx.acg.bean.GetRecommendBean;
import com.kcx.acg.bean.HotTagBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.activity.LoginActivity;
import com.kcx.acg.views.activity.MainActivity;
import com.kcx.acg.views.view.BottomDialog2;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.MyStateButton;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 */

public class RecommendAdapter extends DelegateAdapter.Adapter {
    private List<String> list;
    // 用于存放数据列表

    private Context context;
    private LayoutHelper layoutHelper;
    private int layoutId;
    private List<HotTagBean.ReturnDataBean.ListBean> hotTagList;
    private List<GetRecommendBean.ReturnDataBean> reCommendList;
    private Map<Integer, Boolean> checkedMap = Maps.newHashMap();

    public RecommendAdapter(Context context, LayoutHelper layoutHelper, List<String> list) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.list = list;
    }

    public RecommendAdapter(Context context, LayoutHelper layoutHelper, int layoutId, List<GetRecommendBean.ReturnDataBean> reCommendList) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.reCommendList = reCommendList;
    }

    public RecommendAdapter(Context context, LayoutHelper layoutHelper, int layoutId, List<HotTagBean.ReturnDataBean.ListBean> hotTagList, int type) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutId = layoutId;
        this.hotTagList = hotTagList;
    }

    public void setCheckedMap(List<GetRecommendBean.ReturnDataBean> reCommendList) {
        for (int i = 0; i < reCommendList.size(); i++) {
            checkedMap.put(i, false);
        }
    }

    public void setCheckedByPosition(int position, boolean isAttentionedAuthor) {
        checkedMap.put(position, isAttentionedAuthor);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (layoutId) {
            case R.layout.browse_recommend_layout:
                return new RecommendHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
            case R.layout.browse_hot_item_layout:
                return new HotHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (layoutId) {
            case R.layout.browse_recommend_layout:
                final RecommendHolder recommendHolder = (RecommendHolder) holder;
                RequestOptions mRequestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE);
                mRequestOptions.placeholder(R.mipmap.placehold_head);
                mRequestOptions.error(R.mipmap.placehold_head);
                final GetRecommendBean.ReturnDataBean item = reCommendList.get(position);
                Glide.with(context).load(item.getMemberPhoto()).apply(mRequestOptions).into(recommendHolder.headCiv);
                recommendHolder.nameTv.setText(item.getMemberUserName());
                switch (item.getUserIdentify()){
                    case Constants.Author.ID_F:
                        recommendHolder.vipIv.setVisibility(View.VISIBLE);
                        break;
                    case Constants.Author.UNKNOWN:
                    default:
                        recommendHolder.vipIv.setVisibility(View.GONE);
                        break;
                }
                RequestOptions reCommendOptions = new RequestOptions().placeholder(R.drawable.img_holder).error(R.drawable.img_holder);
                int mWidth = (SysApplication.mWidthPixels / 2 - 86) / 3;
                reCommendOptions.override(mWidth, mWidth);
                reCommendOptions.centerCrop();
                recommendHolder.imgsLayout.removeAllViews();

                for (int i = 0; i < item.getRecommendProductList().size(); i++) {
                    ImageView imageView = new ImageView(context);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    if (i == 0)
                        lp.setMargins(0, 0, 0, 0);
                    else
                        lp.setMargins(10, 0, 0, 0);
                    lp.weight = (SysApplication.mWidthPixels / 2 - 86) / 3;
                    lp.height = (SysApplication.mWidthPixels / 2 - 86) / 3;
                    imageView.setLayoutParams(lp);
//                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Glide.with(context).load(item.getRecommendProductList().get(i).getCoverPicUrl()).apply(reCommendOptions).into(imageView);
                    recommendHolder.imgsLayout.addView(imageView);
                }
                if (checkedMap != null && checkedMap.size() > 0) {
                    if (checkedMap.get(position)) {
                        recommendHolder.followTv.setBackgroundResource(R.drawable.shape_gray_bg5);
                        recommendHolder.followTv.setText(context.getString(R.string.browse_followed_msg));
                    } else {
                        recommendHolder.followTv.setBackgroundResource(R.drawable.shape_pink_bg2);
                        recommendHolder.followTv.setText(context.getString(R.string.follow_string));
                    }
                }
                recommendHolder.followStateTv.setOnInnerClickeListener(new MyStateButton.ButtonClickListener() {
                    @Override
                    public void innerClick() {
                        if (!checkedMap.get(position)) {
                            recommendHolder.followStateTv.setOnInnerStart();
                            attentionMember(recommendHolder, item.getMemberID(), 1, position);
                        } else {
                            BottomDialog2.Builder builder = new BottomDialog2.Builder(context);
                            builder.setLayoutId(R.layout.dialog_unfollow_layout).show();
                            builder.setOnConfirmListener(new BottomDialog2.Builder.OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    attentionMember(recommendHolder, item.getMemberID(), 0, position);
                                }
                            });
                        }
                    }
                });
                recommendHolder.followTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
                recommendHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onFollowListener.onItemClick(view, item.getMemberID(), position);
                    }
                });
                break;
            case R.layout.browse_hot_item_layout:
                HotHolder hotHolder = (HotHolder) holder;
                HotTagBean.ReturnDataBean.ListBean hotItem = hotTagList.get(position);
                MultiTransformation multi = new MultiTransformation(
                        new CenterCrop(),
                        new RoundedCornersTransformation(15, 0, RoundedCornersTransformation.CornerType.ALL));
                RequestOptions advOptions = new RequestOptions();
                advOptions.placeholder(R.drawable.img_holder_5dp);
                advOptions.error(R.drawable.img_holder_5dp);
                advOptions.skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL);
                advOptions.transform(multi);
                Glide.with(context).load(hotItem.getTagPhoto()).apply(advOptions).into(hotHolder.imgIv);
                hotHolder.labelTv.setText(hotItem.getTagName());
                hotHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onHobbyListener.onHobby(view, position);
                    }
                });
                break;
        }
    }

    public void attentionMember(final RecommendHolder recommendHolder, int memberID, final int type, final int position) {
        AttentionMemberApi attentionMemberApi = new AttentionMemberApi((RxAppCompatActivity) context);
        attentionMemberApi.setMemberID(memberID);
        attentionMemberApi.setType(type);
        attentionMemberApi.setListener(new HttpOnNextListener<AttentionMemberBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AttentionMemberBean attentionMemberBean) {
                if (attentionMemberBean.getErrorCode() == 200) {
                    EventBus.getDefault().post(new BusEvent(BusEvent.ATTENTION_MEMBER, type == 1 ? true : false));
                    if (type == 1) {
                        recommendHolder.followTv.setBackgroundResource(R.drawable.shape_gray_ccc_5dp_bg);
                        recommendHolder.followTv.setText(context.getString(R.string.browse_followed_msg));
                    } else {
                        recommendHolder.followTv.setBackgroundResource(R.drawable.shape_pink_bg2);
                        recommendHolder.followTv.setText(context.getString(R.string.follow_string));
                    }
                    setCheckedByPosition(position, type == 1 ? true : false);
//                    browseAdapter.notifyDataSetChanged();
                } else {
                    if (attentionMemberBean.getErrorCode() == 402) {
//                        ((MainActivity) context).startDDMActivity(LoginActivity.class, true);
                    }
                    if (type == 0) {
                        recommendHolder.followTv.setBackgroundResource(R.drawable.shape_gray_ccc_5dp_bg);
                        recommendHolder.followTv.setText(context.getString(R.string.browse_followed_msg));
                    } else {
                        recommendHolder.followTv.setBackgroundResource(R.drawable.shape_pink_bg2);
                        recommendHolder.followTv.setText(context.getString(R.string.follow_string));
                    }
                }
                CustomToast.showToast( attentionMemberBean.getErrorMsg());
                recommendHolder.followStateTv.setOnInnerResult();
                return null;
            }
        });

        HttpManager.getInstance().doHttpDeal(context, attentionMemberApi);
    }

    @Override
    public int getItemCount() {
        switch (layoutId) {
            case R.layout.browse_recommend_layout:
                return reCommendList != null ? reCommendList.size() : 0;
            case R.layout.browse_hot_item_layout:
                return hotTagList != null ? hotTagList.size() : 0;
            default:
                return 0;
        }
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }
//
//    public void setIsAttentionedAuthor(boolean isAttentionedAuthor) {
//        this.isAttentionedAuthor = isAttentionedAuthor;
//        if (isAttentionedAuthor) {
//            recommendHolder.followTv.setBackgroundResource(R.drawable.shape_gray_bg5);
//            recommendHolder.followTv.setText(context.getString(R.string.browse_followed_msg));
//        } else {
//            recommendHolder.followTv.setBackgroundResource(R.drawable.shape_pink_bg2);
//            recommendHolder.followTv.setText(context.getString(R.string.follow_string));
//        }
//    }

    class RecommendHolder extends RecyclerView.ViewHolder {
        private ImageView headCiv;
        private ImageView vipIv;
        private TextView nameTv;
        private Button followTv;
        private MyStateButton followStateTv;
        private LinearLayout imgsLayout;

        public RecommendHolder(View itemView) {
            super(itemView);
            headCiv = itemView.findViewById(R.id.browse_recomment_head_civ);
            vipIv = itemView.findViewById(R.id.browse_recomment_vip_iv);
            nameTv = itemView.findViewById(R.id.browse_recomment_name_tv);
            followTv = itemView.findViewById(R.id.browse_recomment_follow_tv);
            followStateTv = itemView.findViewById(R.id.browse_recomment_follow_state_tv);
            imgsLayout = itemView.findViewById(R.id.browse_recomment_imgs_layout);

        }

    }

    class HotHolder extends RecyclerView.ViewHolder {
        private ImageView imgIv;
        private TextView labelTv;

        public HotHolder(View itemView) {
            super(itemView);
            imgIv = itemView.findViewById(R.id.hot_item_img_iv);
            labelTv = itemView.findViewById(R.id.hot_item_lab_tv);
        }
    }

    private OnFollowListener onFollowListener;
    private OnHobbyListener onHobbyListener;

    public void setOnHobbyListener(OnHobbyListener onHobbyListener) {
        this.onHobbyListener = onHobbyListener;
    }

    public void setOnFollowListener(OnFollowListener onFollowListener) {
        this.onFollowListener = onFollowListener;
    }

    public interface OnFollowListener {
        void onItemClick(View view, int memberID, int position);
    }

    public interface OnHobbyListener {
        void onHobby(View view, int position);
    }
}
