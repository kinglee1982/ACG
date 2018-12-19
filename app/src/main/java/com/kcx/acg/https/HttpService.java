package com.kcx.acg.https;


import com.kcx.acg.bean.AcceptActivityCouponBean;
import com.kcx.acg.bean.GetMyCouponListBean;
import com.kcx.acg.bean.AddBankBean;
import com.kcx.acg.bean.AddCommentBean;
import com.kcx.acg.bean.AmazonS3ConfigBean;
import com.kcx.acg.bean.AttentionMemberBean;
import com.kcx.acg.bean.BuyProductPopTipBean;
import com.kcx.acg.bean.BuyProductWithACoinBean;
import com.kcx.acg.bean.CommentLikeBean;
import com.kcx.acg.bean.DelMyBrowseHistoryBean;
import com.kcx.acg.bean.DelProductByIDBean;
import com.kcx.acg.bean.FavouriteProductBean;
import com.kcx.acg.bean.FavouriteTagListBean;
import com.kcx.acg.bean.GetAdvertisementInfoBean;
import com.kcx.acg.bean.BankListBean;
import com.kcx.acg.bean.CommonBean;
import com.kcx.acg.bean.DeleteBankBean;
import com.kcx.acg.bean.FavouriteProductListBean;
import com.kcx.acg.bean.GetCanViewTypeBean;
import com.kcx.acg.bean.GetCommentReplysBean;
import com.kcx.acg.bean.GetH5ShareLinkUrlBean;
import com.kcx.acg.bean.GetMyBrowseHistoryBean;
import com.kcx.acg.bean.GetPopularityInfoBean;
import com.kcx.acg.bean.GetProductByIDBean;
import com.kcx.acg.bean.GetProductDetailInfoBean;
import com.kcx.acg.bean.GetProductInfoBean;
import com.kcx.acg.bean.GetProductListBean;
import com.kcx.acg.bean.H5LinkUrlBean;
import com.kcx.acg.bean.InternationalAreaInfoListBean;
import com.kcx.acg.bean.IsAcceptActivityCouponBean;
import com.kcx.acg.bean.MemberEntityBean;
import com.kcx.acg.bean.GetRecommendBean;
import com.kcx.acg.bean.GetSearchHotTagsBean;
import com.kcx.acg.bean.GetSearchRelatedProductBean;
import com.kcx.acg.bean.GetSearchRelatedUsersBean;
import com.kcx.acg.bean.GetSearchTitleBean;
import com.kcx.acg.bean.HotTagBean;
import com.kcx.acg.bean.H5PayLinkBean;
import com.kcx.acg.bean.MySettingBean;
import com.kcx.acg.bean.ProductCommentsBean;
import com.kcx.acg.bean.AddReplyBean;
import com.kcx.acg.bean.GetUserTokenBean;
import com.kcx.acg.bean.HomeBean;
import com.kcx.acg.bean.PreviewBean;
import com.kcx.acg.bean.ModifyNickNameBean;
import com.kcx.acg.bean.ModifyPWBean;
import com.kcx.acg.bean.ProductToDayListBean;
import com.kcx.acg.bean.ReadNoticeBean;
import com.kcx.acg.bean.RechargeSettingListBean;
import com.kcx.acg.bean.RegisterUserBean;
import com.kcx.acg.bean.ReplyLikeBean;
import com.kcx.acg.bean.ResetPasswordBean;
import com.kcx.acg.bean.SaveMemberAuthenticateUrlBean;
import com.kcx.acg.bean.SaveProductBean;
import com.kcx.acg.bean.SaveProductVO;
import com.kcx.acg.bean.SelectMemberBankBean;
import com.kcx.acg.bean.SendIdentifyCodeBean;
import com.kcx.acg.bean.SetPayFailureReasonBean;
import com.kcx.acg.bean.ShareSuccessBean;
import com.kcx.acg.bean.ShareSuccessV1_2Bean;
import com.kcx.acg.bean.TagBean;
import com.kcx.acg.bean.UpUserPhotoBean;
import com.kcx.acg.bean.UpgradeInfoBean;
import com.kcx.acg.bean.UserAttentionListBean;
import com.kcx.acg.bean.UserIncomeDetailBean;
import com.kcx.acg.bean.UserIncomeInfoBean;
import com.kcx.acg.bean.UserIncomeSummaryBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.bean.UsersDynamicListBean;
import com.kcx.acg.bean.UsersFavouriteTagListBean;
import com.kcx.acg.bean.VIPSettingListBean;
import com.kcx.acg.bean.ValidateIdentifyCodeBean;
import com.kcx.acg.bean.WithdrawCashBean;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jb.
 */

public interface HttpService {

    //发送短信验证码
    @FormUrlEncoded
    @POST("WebApi/User/SendIdentifyCode")
    Observable<SendIdentifyCodeBean> sendIdentifyCode(@Field("SmsType") String smsType, @Field("Phone") String phone, @Field("areaCode") String areaCode);

    //验证手机号和短信验证码
    @FormUrlEncoded
    @POST("WebApi/User/ValidateIdentifyCode")
    Observable<ValidateIdentifyCodeBean> validateIdentifyCode(@Field("Code") String Code, @Field("Phone") String phone, @Field("areaCode") String areaCode);

    //注册
    @FormUrlEncoded
    @POST("WebApi/User/RegisterUser")
    Observable<RegisterUserBean> registerUser(@Field("InviteCode") String inviteCode, @Field("DeviceID") String deviceID, @Field("Guid") String guid, @Field("Phone") String phone, @Field("Pwd") String pwd, @Field("UserName") String userName, @Field("areaCode") String areaCode);

    //根据手机号和密码获取用户AccessToken
    @FormUrlEncoded
    @POST("WebApi/User/GetUserToken")
    Observable<GetUserTokenBean> getUserToken(@Field("Pwd") String pwd, @Field("Phone") String phone, @Field("areaCode") String areaCode);

    /**
     * 首页
     *
     * @param deviceId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GET("/WebApi/HomePage/GetRecommendProductList")
    Observable<HomeBean> getRecommendProductList(@Query("deviceId") String deviceId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("/WebApi/HomePage/GetCanViewType")
    Observable<GetCanViewTypeBean> getCanViewType(@Query("productID") int productID);

    /**
     * 获取预览页内容
     *
     * @param productID
     * @return
     */
    @GET("/WebApi/Product/GetPreviewProductInfo")
    Observable<PreviewBean> getPreviewProductInfo(@Query("productID") int productID);

    /**
     * 获取评论列表
     *
     * @param productID
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GET("/WebApi/Comment/GetProductComments")
    Observable<ProductCommentsBean> getProductComments(@Query("productID") int productID, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /**
     * 添加评论
     *
     * @param productID
     * @param content
     * @return
     */
    @FormUrlEncoded
    @POST("/WebApi/Comment/AddComment")
    Observable<AddCommentBean> addComment(@Field("productID") int productID, @Field("content") String content);

    /**
     * 添加回复
     *
     * @param commentID
     * @param content
     * @return
     */
    @FormUrlEncoded
    @POST("/WebApi/Comment/AddReply")
    Observable<AddReplyBean> addReply(@Field("commentID") int commentID, @Field("content") String content);

    /**
     * 获取回复的二级评论列表
     *
     * @param commentID
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GET("/WebApi/Comment/GetCommentReplys")
    Observable<GetCommentReplysBean> getCommentReplys(@Query("commentID") int commentID, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /**
     * 评论点赞
     *
     * @param commentID
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("/WebApi/Comment/CommentLike")
    Observable<CommentLikeBean> commentLike(@Field("commentID") int commentID, @Field("type") int type);

    /**
     * 二级回复点赞
     *
     * @param replyID
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("/WebApi/Comment/ReplyLike")
    Observable<ReplyLikeBean> replyLike(@Field("replyID") int replyID, @Field("type") int type);

    /**
     * 获取用户弹窗
     *
     * @param productID
     * @return
     */
    @GET("/WebApi/Product/GetBuyProductPopTip")
    Observable<BuyProductPopTipBean> buyProductPopTip(@Query("productID") int productID);

    @FormUrlEncoded
    @POST("/WebApi/Product/FavouriteProduct")
    Observable<FavouriteProductBean> favouriteProduct(@Field("productID") int productID, @Field("type") int type);

    @FormUrlEncoded
    @POST("/WebApi/Product/AttentionMember")
    Observable<AttentionMemberBean> attentionMember(@Field("memberID") int memberID, @Field("type") int type);

    @GET("/WebApi/Product/GetProductDetailInfo")
    Observable<GetProductDetailInfoBean> getProductDetailInfo(@Query("productID") int productID, @Query("type") int type);

    //广告接口
    @GET("/WebApi/Advertisement/GetAdvertisementInfo")
    Observable<GetAdvertisementInfoBean> getAdvertisementInfo(@Query("LocationID") int LocationID);

    @FormUrlEncoded
    @POST("/WebApi/Product/BuyProductWithACoin")
    Observable<BuyProductWithACoinBean> buyProductWithACoin(@Field("productID") int productID);

    /**
     * 获取作品详情
     *
     * @param productID
     * @return
     */
    @GET("/WebApi/Product/GetProductInfo")
    Observable<GetProductInfoBean> getProductInfo(@Query("productID") int productID);

    //根据AccessToken来获取用户基本信息(AccessToken通过请求头信息进行传递)
    @POST("WebApi/User/GetUserInfo")
    Observable<UserInfoBean> getUserInfo();

    //重置密码
    @FormUrlEncoded
    @POST("WebApi/User/ResetPassword")
    Observable<ResetPasswordBean> resetPassword(@Field("Phone") String phone, @Field("Pwd") String pwd, @Field("Guid") String guid, @Field("areaCode") String areaCode);

    //修改用户昵称
    @FormUrlEncoded
    @POST("WebApi/User/ModifyNickName")
    Observable<ModifyNickNameBean> modifyNickName(@Field("UserName") String userName);

    //修改密码
    @FormUrlEncoded
    @POST("WebApi/User/ModifyPassword")
    Observable<ModifyPWBean> modifyPW(@Field("Pwd") String pwd, @Field("SurePwd") String surePwd);


    //获取用户收益信息
    @GET("WebApi/Income/GetUserIncomeInfo")
    Observable<UserIncomeInfoBean> getUserIncomeInfo();

    //选择提现银行卡
    @POST("WebApi/PersonalCenter/SelectMemberBank")
    Observable<SelectMemberBankBean> selectMemberBank();

    //获取所有银行列表
    @GET("WebApi/PersonalCenter/GetBankList")
    Observable<BankListBean> getBankList();

    //添加银行卡
    @FormUrlEncoded
    @POST("WebApi/PersonalCenter/AddBank")
    Observable<AddBankBean> addBank(@Field("bankID") int bankID, @Field("accountNo") String accountNo, @Field("Surname") String surname, @Field("AccountName") String accountName);

    //删除银行卡
    @FormUrlEncoded
    @POST("WebApi/PersonalCenter/DeleteBank")
    Observable<DeleteBankBean> deleteBank(@Field("memberBankID") int memberBankID);

    //会员提现
    @FormUrlEncoded
    @POST("WebApi/PersonalCenter/WithdrawCash")
    Observable<WithdrawCashBean> withdrawCash(@Field("guid") String guid, @Field("memberBankID") int memberBankID, @Field("money") int money);

    //获取用户收藏列表
    @GET("WebApi/PersonalCenter/GetFavouriteProductList")
    Observable<FavouriteProductListBean> getFavouriteProductList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    //获取用户关注列表
    @GET("WebApi/PersonalCenter/GetUserAttentionList")
    Observable<UserAttentionListBean> getUserAttentionList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    //获取用户粉丝列表
    @GET("WebApi/PersonalCenter/GetUserFansList")
    Observable<UserAttentionListBean> getUserFansList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    //吐槽
    @FormUrlEncoded
    @POST("WebApi/PersonalCenter/AddSuggesttion")
    Observable<CommonBean> addSuggesttion(@Field("content") String content);


    //=================发现页面接口

    /**
     * 推荐
     *
     * @return
     */
    @GET("/WebApi/Finds/GetRecommend")
    Observable<GetRecommendBean> getRecommend();

    /**
     * 相关兴趣
     *
     * @param searchKey
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST("/WebApi/Finds/SearchHotTag")
    Observable<HotTagBean> searchHotTag(@Field("searchKey") String searchKey, @Field("pageIndex") int pageIndex, @Field("pageSize") int pageSize);

    /**
     * 热门搜索 - 关键词
     *
     * @return
     */
    @GET("/WebApi/Finds/GetSearchHotTags")
    Observable<GetSearchHotTagsBean> getSearchHotTags();

    /**
     * 搜索关联词
     *
     * @param SearchKey
     * @return
     */
    @GET("/WebApi/Finds/GetSearchTitle")
    Observable<GetSearchTitleBean> getSearchTitle(@Query("SearchKey") String SearchKey);

    /**
     * 搜索相关用户
     *
     * @param searchKey
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST("/WebApi/Finds/GetSearchRelatedUsers")
    Observable<GetSearchRelatedUsersBean> getSearchRelatedUsers(@Field("searchKey") String searchKey, @Field("pageIndex") int pageIndex, @Field("pageSize") int pageSize);

    /**
     * 搜索相关作品
     *
     * @param searchKey
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST("/WebApi/Finds/GetSearchRelatedProduct")
    Observable<GetSearchRelatedProductBean> getSearchRelatedProduct(@Field("searchKey") String searchKey, @Field("pageIndex") int pageIndex, @Field("pageSize") int pageSize);

    //用户兴趣标签Top列表
    @GET("WebApi/Tag/GetUsersTopFavouriteTagList")
    Observable<UsersFavouriteTagListBean> getUsersTopFavouriteTagList(@Query("topCount") int topCount);

    //分页获取动态列表(AccessToken通过请求头信息进行传递） Type:动态类型(0：发布作品，1：更新作品)
    @GET("WebApi/Tag/GetUsersDynamicList")
    Observable<UsersDynamicListBean> getUsersDynamicList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);


    //根据用户Token分页获取用户兴趣标签列表
    @GET("WebApi/Tag/GetUsersFavouriteTagList")
    Observable<FavouriteTagListBean> getUsersFavouriteTagList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    //查看标签的详情
    @GET("WebApi/Tag/GetTag")
    Observable<TagBean> getTag(@Query("tagID") int tagID);

    //兴趣专题列表
    @GET("WebApi/Product/GetProductToDayList")
    Observable<ProductToDayListBean> getProductToDayList(@Query("tagID") int tagID, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    //查看用户的所有作品
    @GET("WebApi/Product/GetProductToMenberList")
    Observable<ProductToDayListBean> getProductToMenberList(@Query("memberId") int memberId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    //获取用户中心的基本信息
    @GET("WebApi/User/GetMemberEntity")
    Observable<MemberEntityBean> getMemberEntity(@Query("MemberID") int MemberID);

    //获取vip配置信息
    @GET("WebApi/VIP/GetVIPSettingList")
    Observable<VIPSettingListBean> GetVIPSettingList();

    //余额购买vip
    @FormUrlEncoded
    @POST("WebApi/VIP/BalanceRechargeVIP")
    Observable<CommonBean> balanceRechargeVIP(@Field("packageID") int packageID);

    //获取A币配置信息
    @GET("WebApi/Recharge/GetRechargeSettingList")
    Observable<RechargeSettingListBean> GetRechargeSettingList();

    //余额购买A币
    @FormUrlEncoded
    @POST("WebApi/Recharge/BalanceRechargeACoin")
    Observable<CommonBean> balanceRechargeACoin(@Field("rechargeSettingID") int rechargeSettingID);

    //上传头像
    @Multipart
    @POST("WebApi/User/UpUserPhoto")
    Observable<UpUserPhotoBean> upUserPhoto(@Part MultipartBody.Part part);

    //签到
    @POST("WebApi/PersonalCenter/UserSignIn")
    Observable<CommonBean> userSignIn();


    //添加用户兴趣/关注标签
    @FormUrlEncoded
    @POST("WebApi/Tag/AddMemberTag")
    Observable<CommonBean> addMemberTag(@Field("tagID") int tagID, @Field("type") int type);

    //获取所有国际地区信息列表
    @GET("WebApi/PersonalCenter/GetInternationalAreaInfoList")
    Observable<InternationalAreaInfoListBean> getInternationalAreaInfoList();

    @GET("WebApi/PersonalCenter/GetAppUpgradeInfo")
    Observable<UpgradeInfoBean> getAppUpgradeInfo();

    @GET("WebApi/PersonalCenter/GetH5ShareLinkUrl")
    Observable<GetH5ShareLinkUrlBean> getH5ShareLinkUrl(@Query("type") int type, @Query("shareID") int shareID);

    //获取h5链接地址
    @GET("WebApi/PersonalCenter/GetH5LinkUrl")
    Observable<H5LinkUrlBean> getH5LinkUrl(@Query("type") int type);

    //分享成功回调
    @POST("WebApi/PersonalCenter/ShareSuccess")
    Observable<ShareSuccessBean> shareSuccess();

    //获取H5支付地址
    @GET("WebApi/Pay/GetH5PayLink")
    Observable<H5PayLinkBean> getH5PayLink(@Query("payType") int payType, @Query("goodsType") int goodsType, @Query("packageID") int packageID, @Query("buyAmount") int buyAmount, @Query("memberCouponID") int memberCouponID);

    //消息通知
    @GET("WebApi/Notice/GetReadNotice")
    Observable<ReadNoticeBean> getReadNotice(@Query("isRead") int isRead, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    //用户收益统计信息
    @GET("WebApi/Income/GetUserIncomeSummaryNew")
    Observable<UserIncomeSummaryBean> getUserIncomeSummaryNew(@Query("type") int type);

    //获取用户收益明细信息
    @GET("WebApi/Income/GetUserIncomeDetailNew")
    Observable<UserIncomeDetailBean> getUserIncomeDetailNew(@Query("type") int type, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("WebApi/Popularity/GetPopularityInfo")
    Observable<GetPopularityInfoBean> getPopularityInfo(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("WebApi/Setting/MySetting")
    Observable<MySettingBean> mySetting();

    @GET("WebApi/Config/GetAmazonS3Config")
    Observable<AmazonS3ConfigBean> getAmazonS3Config();

    @GET("WebApi/Browse/GetMyBrowseHistory")
    Observable<GetMyBrowseHistoryBean> getMyBrowseHistory(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @POST("WebApi/Browse/DelMyBrowseHistory")
    Observable<DelMyBrowseHistoryBean> delMyBrowseHistory();

    @GET("WebApi/CreateCenter/GetProductList")
    Observable<GetProductListBean> getProductList(@Query("auditStatus") int auditStatus, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @FormUrlEncoded
    @POST("WebApi/CreateCenter/DelProductByID")
    Observable<DelProductByIDBean> delProductByID(@Field("productID") int productID);

    @FormUrlEncoded
    @POST("WebApi/OriginalPlan/SaveMemberAuthenticateUrl")
    Observable<SaveMemberAuthenticateUrlBean> saveMemberAuthenticateUrl(@Field("authenticateVideoUrl") String authenticateVideoUrl);

    @GET("WebApi/Coupon/GetMyCouponList")
    Observable<GetMyCouponListBean> getMyCouponList(@Query("useType") int useType, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @POST("WebApi/Coupon/IsAcceptActivityCoupon")
    Observable<IsAcceptActivityCouponBean> isAcceptActivityCoupon();

    @POST("WebApi/Coupon/AcceptActivityCoupon")
    Observable<AcceptActivityCouponBean> acceptActivityCoupon();

    //提交作品
    @POST("WebApi/CreateCenter/SaveProduct")
    Observable<SaveProductBean> saveProduct(@Body SaveProductVO saveProductVO);

    @FormUrlEncoded
    @POST("WebApi/Pay/SetPayFailureReason")
    Observable<SetPayFailureReasonBean> setPayFailureReason(@Field("orderID") String orderID, @Field("failureReason") String failureReason);

    @FormUrlEncoded
    @POST("WebApi/PersonalCenter/ShareSuccessV1_2")
    Observable<ShareSuccessV1_2Bean> shareSuccessV1_2(@Field("type") int type, @Field("shareID") int shareID);

    //根据作品ID获取作品信息
    @GET("WebApi/CreateCenter/GetProductByID")
    Observable<GetProductByIDBean> getProductByID(@Query("productID") int productID);
}
