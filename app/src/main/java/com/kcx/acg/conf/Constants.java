package com.kcx.acg.conf;


import android.content.Context;

import com.amazonaws.services.s3.model.Region;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.utils.SPUtil;

public class Constants {
    public static final String DEBUG_URL = "http://192.168.1.80:8899/";
    public static final String DEV3_URL = "http://192.168.1.164:8899/";
    public static final String DEV1_URL = "http://192.168.1.36:8899/";
    public static final String DEV2_URL = "http://192.168.1.140:8899/";
    public static final String RELEASE_URL = "https://api.nicoacg1.com/";
    public static final String YOUTUBE_URL = "https://www.youtube.com/";
    public static final String DYNAMIC_URL = "https://nicoacg.acgsitelist.com/WebApi/Domain/GetDomain";

    public static final String URL_UPLOAD = "http://192.168.3.247:8080/";
    public final static String AES_SECRET_KEY = "QgCsE,ReT1HIg.te";   //AES 加密key
    public final static String ACCESS_TOKEN = "accessToken";
    public final static String ACCOUNT = "account";  //账号
    public final static String COOKIE_DATA = "cookieData";
    public static final String PRIVACY_PW = "privacyPw";  //隐私密码
    public static final String HAVE_PRIVACY_PW = "havePrivacyPw";  //true:已经有隐私密码，false：还未开启隐私密码
    public static final String IS_REFRESH_USER_INFO = "isRefreshUserInfo";  //是否刷新个人中心数据
    public static final String IS_REGISTER = "isRegister";  //是否注册成功
    public static final String USER_INFO = "userInfo";  //用户数据
    public static final String SYS_DATE = "sysDate";  //当前系统日期
    public static final String AREA_CODE = "areaCode";  //手机区号
    public static final String DYNAMIC_HOST = "dynamicHost";  //动态
    public static final String HOT_TAG = "hotTag";  //热门标签
    public static final String LOCAL_MEDIA_VIDEO = "localMediaVideo";  //本地视频list
    public static final String LOCAL_MEDIA_PICTURE = "localMediaPicture";  //本地圖片list
    //    AWS
    public static final String ENDPOINT = "http://s3.ap-southeast-1.amazonaws.com";
    public static final String ACCESS_KEY = "AKIAJ7JS6CKZ4JP5AMHA";
    public static final String SECRET_KEY = "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";
    public static final String BUCKET_NAME = "nicoacg-sg";
    public static final String BUCKET_REGION = "ap-southeast-1";
    public static final String CONFIG_NAME = "亚太地区-新加坡";
    public static final String COVER_IMAGE_ID = "coverImageId";
//    public static final String BUCKET_REGION = Region.AP_Tokyo.toString();
    //    public static final String COGNITO_POOL_ID = "123";
    //    public static final String COGNITO_POOL_REGION = Region.CN_Beijing.toString();

    public static final String PICTURE_OBSERVERS = "pictureObservers";
    public static final String VIDEO_OBSERVERS = "videoObservers";

    public static final int KEY_ADVERTIS_PREVIEW = 1;
    public static final int KEY_ADVERTIS_WORK_DETILS = 2;
    public static final int KEY_ADVERTIS_FOLLOW = 3;
    public static final int KEY_ADVERTIS_FIND = 4;
    public static final int KEY_ADVERTIS_HOBBY = 5;
    public static final int KEY_ADVERTIS_LOTTERY = 6;
    public static final int KEY_ADVERTIS_VIDEO = 7;
    public static final int KEY_ADVERTIS_INCOME = 8;
    public static final int KEY_ADVERTIS_DIALOG_SHARE = 9;
    public static final int KEY_ADVERTIS_DIALOG_CALL_FOR_PAPERS = 10;

    public static final String KEY_SHOW_CALL_FOR_PAPERS_DIALOG = "key_show_call_for_papers_dialog";

    public static final String KEY_IS_FINISH_CUR_ACTIVITY = "key_is_finish_cur_activity";
    public static boolean isShowDialog = true;

    public static class Works {
        //(0：不能观看，1：免费观看，2：付费观看，3：vip特权观看（50次），4：原创作品未关注作者不能观看【关注提示页】)
        public static final int CANT_WATCH = 0;
        public static final int FREE_WATCH = 1;
        public static final int PAY_WATCH = 2;
        public static final int VIP_WATCH = 3;
        public static final int CANT_WATCH_OF_ORIGINAL = 4;
    }

    public static class Author{
        public static final int UNKNOWN = 0;
        public static final int ID_F = 1;
        public static final int ID_PORTER = 2;
    }

    public static class UserInfo {
        public static final int ORDINARY_MEMBER = 1;
        public static final int VIP_MEMBER = 2;
        public static final int VIP_EXPIRED = 3;

        public static final String USER_LOCELA_CODE = "locale_code";
        public static final String USER_IS_DEFAULT_LANGUAGE = "is_default_language";
        public static final String KEY_AMAZON_S3_CONFIG = "key_amazon_s3_config";

        public static final int STATUS_USER_AUTHENTICATE_UN_SUB = 0;
        public static final int STATUS_USER_AUTHENTICATE_REVIEW = 1;
        public static final int STATUS_USER_AUTHENTICATE_PASSED = 2;
        public static final int STATUS_USER_AUTHENTICATE_UNPASS = 3;

        /**
         * @SEE NORMAL 正常
         * @SUSPEND 封停
         * @ACOIN_FREEZE A币冻结
         * @DISABLE_SPEECH 账号禁言
         * @ACCOUNT_BANNED 账号封禁
         * @ACOIN_FREEZE_AND_DISABLE_SPEECH A币冻结+账号禁言
         * @ACCOUNT_BANNED_AND_ACOIN_FREEZE A币冻结+账号封禁
         * @ACCOUNT_BANNED_AND_DISABLE_SPEECH 账号禁言+账号封禁
         * @ACCOUNT_BANNED_AND_DISABLE_SPEECH_AND_ACOIN_FREEZE A币冻结+账号禁言+账号封禁
         */
        public static class AccountStatus {
            public static final int NORMAL = 1;
            public static final int SUSPEND = 2;
            public static final int ACOIN_FREEZE = 3;
            public static final int DISABLE_SPEECH = 4;
            public static final int ACCOUNT_BANNED = 5;
            public static final int ACOIN_FREEZE_AND_DISABLE_SPEECH = 6;
            public static final int ACCOUNT_BANNED_AND_ACOIN_FREEZE = 7;
            public static final int ACCOUNT_BANNED_AND_DISABLE_SPEECH = 8;
            public static final int ACCOUNT_BANNED_AND_DISABLE_SPEECH_AND_ACOIN_FREEZE = 9;
        }
    }

    public interface ImgUrlSuffix {
        //用户头像
        String small_9 = "!small9";
        //单品列表
        String dp_tall = "!dptall";
        //单品列表 正方形
        String dp_list = "!dplist";
        //详情大图
        String small = "!small";
        //图集小图
        String dp_small = "!dpsmall";
        //拼单列表
        String crowd_list = "!crowdlist";
        //feed列表:
        String feed_list = "!feedlist";
        //moblist:
        String mob_list = "!moblist";
        //bizlist
        String biz_list = "!bizlist";
        //tall
        String tall = "!tall";
    }

    //---------------------------            hybrid==========================================================
    public static class Hybrid{
        public final static String TO_LOGIN = "toLogin";  //token失效
        public final static String BACK_VC = "backVC";
        public final static String SHARE = "share"; //分享
        public final static String SAVE_QCODE_IMG = "saveQcodeImg"; //保存二维码
        public final static String IS_LOTTERY = "isLottery  "; //抽奖
        public final static String GO_WITHDRAW ="goWithdraw"; //去提现
        public final static String SAVE_QCODE ="saveQcode";
    }

    public static class URL {
        public static String getUrl(Context context) {
            return SPUtil.getString(context, DYNAMIC_HOST, RELEASE_URL);
        }

        public static void setUrl(Context context, String baseUrl) {
            SPUtil.putString(context, DYNAMIC_HOST, baseUrl);
        }
    }

    public static class UploadState{
        public static final String WAITING = "WAITING";
        public static final String IN_PROGRESS = "IN_PROGRESS";
        public static final String PAUSED = "PAUSED";
        public static final String FAILED = "FAILED";
        public static final String COMPLETED = "COMPLETED";
    }
}
