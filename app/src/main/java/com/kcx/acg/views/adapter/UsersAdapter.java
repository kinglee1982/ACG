package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.api.AttentionMemberApi;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.AttentionMemberBean;
import com.kcx.acg.bean.GetSearchRelatedUsersBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.view.BottomDialog2;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.MyStateButton;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 */

public class UsersAdapter extends DelegateAdapter.Adapter {

    private Context context;
    private LayoutHelper layoutHelper;
    private List<GetSearchRelatedUsersBean.ReturnDataBean.ListBean> userList;
    private boolean isAttentionedAuthor;
    private int pos;


    public UsersAdapter(Context context, LayoutHelper layoutHelper, List<GetSearchRelatedUsersBean.ReturnDataBean.ListBean> userList) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.userList = userList;
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.users_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyHolder userHolder = (MyHolder) holder;
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE);
        mRequestOptions.placeholder(R.mipmap.placehold_head);
        mRequestOptions.error(R.mipmap.placehold_head);
        final GetSearchRelatedUsersBean.ReturnDataBean.ListBean item = userList.get(position);
        Glide.with(context).load(item.getPhoto()).apply(mRequestOptions).into(userHolder.headIv);
        userHolder.nameTv.setText(item.getUserName());
        if (item.isIsAttentioned()) {
            userHolder.followTv.setBackgroundResource(R.drawable.shape_gray_ccc_bg);
            userHolder.followTv.setText(context.getString(R.string.browse_followed_msg));
        } else {
            userHolder.followTv.setBackgroundResource(R.drawable.shape_pink_bg);
            userHolder.followTv.setText(context.getString(R.string.follow_string));
        }
        switch (item.getUserIdentify()) {
            case Constants.Author.ID_F:
                userHolder.vipIv.setVisibility(View.VISIBLE);
                break;
            case Constants.Author.UNKNOWN:
            default:
                userHolder.vipIv.setVisibility(View.GONE);
                break;
        }
        userHolder.followStateTv.setOnInnerClickeListener(new MyStateButton.ButtonClickListener() {
            @Override
            public void innerClick() {
                if (!item.isIsAttentioned()) {
                    userHolder.followStateTv.setOnInnerStart();
                    attentionMember(userHolder, item.getMemberID(), 1, position);
                } else {
                    BottomDialog2.Builder builder = new BottomDialog2.Builder(context);
                    builder.setLayoutId(R.layout.dialog_unfollow_layout).show();
                    builder.setOnConfirmListener(new BottomDialog2.Builder.OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            attentionMember(userHolder, item.getMemberID(), 0, position);
                        }
                    });
                }
            }
        });
        userHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFollowListener.onItemClick(view, position);
            }
        });
        if (position == userList.size() - 1) {
            userHolder.lineIv.setVisibility(View.GONE
            );
        }
    }

    public void attentionMember(final MyHolder userHolder, int memberID, final int type, final int position) {
        AttentionMemberApi attentionMemberApi = new AttentionMemberApi((RxAppCompatActivity) context);
        attentionMemberApi.setMemberID(memberID);
        attentionMemberApi.setType(type);
        attentionMemberApi.setListener(new HttpOnNextListener<AttentionMemberBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AttentionMemberBean attentionMemberBean) {
                if (attentionMemberBean.getErrorCode() == 200) {
                    EventBus.getDefault().post(new BusEvent(BusEvent.ATTENTION_MEMBER, type == 1 ? true : false));
                    if (type == 1) {
                        userHolder.followTv.setBackgroundResource(R.drawable.shape_gray_ccc_bg);
                        userHolder.followTv.setText(context.getString(R.string.browse_followed_msg));
                        userList.get(position).setIsAttentioned(true);
                    } else {
                        userHolder.followTv.setBackgroundResource(R.drawable.shape_pink_bg);
                        userHolder.followTv.setText(context.getString(R.string.follow_string));
                        userList.get(position).setIsAttentioned(false);
                    }
                }
                CustomToast.showToast(attentionMemberBean.getErrorMsg());
                userHolder.followStateTv.setOnInnerResult();

                return null;
            }
        });

        HttpManager.getInstance().doHttpDeal(context, attentionMemberApi);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private ImageView headIv;
        private ImageView vipIv;
        private ImageView lineIv;
        private TextView nameTv;
        private Button followTv;
        private MyStateButton followStateTv;
        private boolean isChecked;

        public MyHolder(View itemView) {
            super(itemView);
            lineIv = itemView.findViewById(R.id.search_result_user_line_iv);
            headIv = itemView.findViewById(R.id.search_result_user_head_iv);
            vipIv = itemView.findViewById(R.id.search_result_user_vip_iv);
            nameTv = itemView.findViewById(R.id.search_result_user_name_tv);
            followTv = itemView.findViewById(R.id.search_result_user_follow_tv);
            followStateTv = itemView.findViewById(R.id.search_result_user_follow_state_tv);

            followTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isChecked = !isChecked;
                    if (isChecked) {
                        followTv.setBackgroundResource(R.drawable.shape_gray_bg5);
                    } else {
                        followTv.setBackgroundResource(R.drawable.shape_pink_bg);
                    }
                }
            });
        }
    }

    private OnFollowListener onFollowListener;

    public void setOnFollowListener(OnFollowListener onFollowListener) {
        this.onFollowListener = onFollowListener;
    }

    public interface OnFollowListener {

        void onItemClick(View view, int position);
    }
}
