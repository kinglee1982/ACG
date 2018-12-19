package com.kcx.acg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jb on 2018/10/11.
 */
public class InternationalAreaInfoListBean implements Serializable {


    /**
     * errorCode : 200
     * errorMsg : 请求成功
     * returnData : [{"areaCode":"86","areaName":"中国","enName":"China"},{"areaCode":"852","areaName":"香港","enName":"Hongkong"},{"areaCode":"853","areaName":"澳门","enName":"Macao"},{"areaCode":"81","areaName":"日本","enName":"Japan"},{"areaCode":"82","areaName":"韩国","enName":"Korea"},{"areaCode":"84","areaName":"越南","enName":"Vietnam"},{"areaCode":"856","areaName":"老挝","enName":"Laos"},{"areaCode":"855","areaName":"柬埔寨","enName":"Kampuchea (Cambodia )"},{"areaCode":"66","areaName":"泰国","enName":"Thailand"},{"areaCode":"95","areaName":"缅甸","enName":"Burma"},{"areaCode":"60","areaName":"马来西亚","enName":"Malaysia"},{"areaCode":"65","areaName":"新加坡","enName":"Singapore"},{"areaCode":"62","areaName":"印度尼西亚","enName":"Indonesia"},{"areaCode":"673","areaName":"文莱","enName":"Brunei"},{"areaCode":"63","areaName":"菲律宾","enName":"Philippines"},{"areaCode":"670","areaName":"东帝汶","enName":"Democratic Republic of Timor-Leste"},{"areaCode":"261","areaName":"马达加斯加","enName":"Madagascar"},{"areaCode":"265","areaName":"马拉维","enName":"Malawi"},{"areaCode":"228","areaName":"多哥","enName":"Togo"},{"areaCode":"676","areaName":"汤加","enName":"Tonga"},{"areaCode":"1809","areaName":"特立尼达和多巴哥","enName":"Trinidad and Tobago"},{"areaCode":"216","areaName":"突尼斯","enName":"Tunisia"},{"areaCode":"90","areaName":"土耳其","enName":"Turkey"},{"areaCode":"993","areaName":"土库曼斯坦","enName":"Turkmenistan "},{"areaCode":"256","areaName":"乌干达","enName":"Uganda"},{"areaCode":"380","areaName":"乌克兰","enName":"Ukraine"},{"areaCode":"971","areaName":"阿拉伯联合酋长国","enName":"United Arab Emirates"},{"areaCode":"44","areaName":"英国","enName":"United Kiongdom"},{"areaCode":"1","areaName":"美国","enName":"United States of America"},{"areaCode":"598","areaName":"乌拉圭","enName":"Uruguay"},{"areaCode":"233","areaName":"乌兹别克斯坦","enName":"Uzbekistan"},{"areaCode":"58","areaName":"委内瑞拉","enName":"Venezuela"},{"areaCode":"327","areaName":"哈萨克斯坦","enName":"Kazakstan"},{"areaCode":"254","areaName":"肯尼亚","enName":"Kenya"},{"areaCode":"965","areaName":"科威特","enName":"Kuwait"},{"areaCode":"331","areaName":"吉尔吉斯坦","enName":"Kyrgyzstan "},{"areaCode":"967","areaName":"也门","enName":"Yemen"},{"areaCode":"381","areaName":"南斯拉夫","enName":"Yugoslavia"},{"areaCode":"263","areaName":"津巴布韦","enName":"Zimbabwe"},{"areaCode":"243","areaName":"扎伊尔","enName":"Zaire"},{"areaCode":"260","areaName":"赞比亚","enName":"Zambia"},{"areaCode":"48","areaName":"波兰","enName":"Poland"},{"areaCode":"689","areaName":"法属玻利尼西亚","enName":"French Polynesia"},{"areaCode":"351","areaName":"葡萄牙","enName":"Portugal"},{"areaCode":"1787","areaName":"波多黎各","enName":"Puerto Rico"},{"areaCode":"974","areaName":"卡塔尔","enName":"Qatar"},{"areaCode":"262","areaName":"留尼旺","enName":"Reunion"},{"areaCode":"40","areaName":"罗马尼亚","enName":"Romania"},{"areaCode":"7","areaName":"俄罗斯","enName":"Russia"},{"areaCode":"1758","areaName":"圣卢西亚","enName":"Saint Lueia"},{"areaCode":"1784","areaName":"圣文森特岛","enName":"Saint Vincent"},{"areaCode":"684","areaName":"东萨摩亚(美)","enName":"Samoa Eastern"},{"areaCode":"685","areaName":"西萨摩亚","enName":"Samoa Western"},{"areaCode":"378","areaName":"圣马力诺","enName":"San Marino"},{"areaCode":"239","areaName":"圣多美和普林西比","enName":"Sao Tome and Principe"},{"areaCode":"966","areaName":"沙特阿拉伯","enName":"Saudi Arabia"},{"areaCode":"221","areaName":"塞内加尔","enName":"Senegal"},{"areaCode":"248","areaName":"塞舌尔","enName":"Seychelles"},{"areaCode":"232","areaName":"塞拉利昂","enName":"Sierra Leone"},{"areaCode":"421","areaName":"斯洛伐克","enName":"Slovakia"},{"areaCode":"386","areaName":"斯洛文尼亚","enName":"Slovenia"},{"areaCode":"677","areaName":"所罗门群岛","enName":"Solomon Is"},{"areaCode":"252","areaName":"索马里","enName":"Somali"},{"areaCode":"27","areaName":"南非","enName":"South Africa"},{"areaCode":"34","areaName":"西班牙","enName":"Spain"},{"areaCode":"94","areaName":"斯里兰卡","enName":"Sri Lanka"},{"areaCode":"1758","areaName":"圣卢西亚","enName":"St.Lucia"},{"areaCode":"1784","areaName":"圣文森特","enName":"St.Vincent"},{"areaCode":"249","areaName":"苏丹","enName":"Sudan"},{"areaCode":"597","areaName":"苏里南","enName":"Suriname"},{"areaCode":"268","areaName":"斯威士兰","enName":"Swaziland"},{"areaCode":"46","areaName":"瑞典","enName":"Sweden"},{"areaCode":"41","areaName":"瑞士","enName":"Switzerland"},{"areaCode":"963","areaName":"叙利亚","enName":"Syria"},{"areaCode":"886","areaName":"台湾省","enName":"Taiwan"},{"areaCode":"992","areaName":"塔吉克斯坦","enName":"Tajikstan"},{"areaCode":"255","areaName":"坦桑尼亚","enName":"Tanzania"},{"areaCode":"359","areaName":"保加利亚","enName":"Bulgaria"},{"areaCode":"226","areaName":"布基纳法索","enName":"Burkina-faso"},{"areaCode":"962","areaName":"约旦","enName":"Jordan"},{"areaCode":"36","areaName":"匈牙利","enName":"Hungary"},{"areaCode":"354","areaName":"冰岛","enName":"Iceland"},{"areaCode":"91","areaName":"印度","enName":"India"},{"areaCode":"371","areaName":"拉脱维亚","enName":"Latvia "},{"areaCode":"961","areaName":"黎巴嫩","enName":"Lebanon"},{"areaCode":"266","areaName":"莱索托","enName":"Lesotho"},{"areaCode":"231","areaName":"利比里亚","enName":"Liberia"},{"areaCode":"218","areaName":"利比亚","enName":"Libya"},{"areaCode":"423","areaName":"列支敦士登","enName":"Liechtenstein"},{"areaCode":"370","areaName":"立陶宛","enName":"Lithuania"},{"areaCode":"352","areaName":"卢森堡","enName":"Luxembourg"},{"areaCode":"960","areaName":"马尔代夫","enName":"Maldives"},{"areaCode":"223","areaName":"马里","enName":"Mali"},{"areaCode":"356","areaName":"马耳他","enName":"Malta"},{"areaCode":"1670","areaName":"马里亚那群岛","enName":"Mariana Is"},{"areaCode":"596","areaName":"马提尼克","enName":"Martinique"},{"areaCode":"230","areaName":"毛里求斯","enName":"Mauritius"},{"areaCode":"52","areaName":"墨西哥","enName":"Mexico"},{"areaCode":"373","areaName":"摩尔多瓦","enName":"Moldova, Republic of "},{"areaCode":"377","areaName":"摩纳哥","enName":"Monaco"},{"areaCode":"976","areaName":"蒙古","enName":"Mongolia "},{"areaCode":"1664","areaName":"蒙特塞拉特岛","enName":"Montserrat Is"},{"areaCode":"212","areaName":"摩洛哥","enName":"Morocco"},{"areaCode":"258","areaName":"莫桑比克","enName":"Mozambique"},{"areaCode":"264","areaName":"纳米比亚","enName":"Namibia "},{"areaCode":"674","areaName":"瑙鲁","enName":"Nauru"},{"areaCode":"977","areaName":"尼泊尔","enName":"Nepal"},{"areaCode":"599","areaName":"荷属安的列斯","enName":"Netheriands Antilles"},{"areaCode":"31","areaName":"荷兰","enName":"Netherlands"},{"areaCode":"64","areaName":"新西兰","enName":"New Zealand"},{"areaCode":"505","areaName":"尼加拉瓜","enName":"Nicaragua"},{"areaCode":"227","areaName":"尼日尔","enName":"Niger"},{"areaCode":"234","areaName":"尼日利亚","enName":"Nigeria"},{"areaCode":"850","areaName":"朝鲜","enName":"North Korea"},{"areaCode":"47","areaName":"挪威","enName":"Norway"},{"areaCode":"968","areaName":"阿曼","enName":"Oman"},{"areaCode":"92","areaName":"巴基斯坦","enName":"Pakistan"},{"areaCode":"507","areaName":"巴拿马","enName":"Panama"},{"areaCode":"675","areaName":"巴布亚新几内亚","enName":"Papua New Cuinea"},{"areaCode":"595","areaName":"巴拉圭","enName":"Paraguay"},{"areaCode":"51","areaName":"秘鲁","enName":"Peru"},{"areaCode":"98","areaName":"伊朗","enName":"Iran"},{"areaCode":"964","areaName":"伊拉克","enName":"Iraq"},{"areaCode":"353","areaName":"爱尔兰","enName":"Ireland"},{"areaCode":"972","areaName":"以色列","enName":"Israel"},{"areaCode":"39","areaName":"意大利","enName":"Italy"},{"areaCode":"225","areaName":"科特迪瓦","enName":"Ivory Coast"},{"areaCode":"1876","areaName":"牙买加","enName":"Jamaica"},{"areaCode":"257","areaName":"布隆迪","enName":"Burundi"},{"areaCode":"237","areaName":"喀麦隆","enName":"Cameroon"},{"areaCode":"1","areaName":"加拿大","enName":"Canada"},{"areaCode":"1345","areaName":"开曼群岛","enName":"Cayman Is."},{"areaCode":"236","areaName":"中非共和国","enName":"Central African Republic"},{"areaCode":"235","areaName":"乍得","enName":"Chad"},{"areaCode":"56","areaName":"智利","enName":"Chile"},{"areaCode":"244","areaName":"安哥拉","enName":"Angola"},{"areaCode":"93","areaName":"阿富汗","enName":"Afghanistan"},{"areaCode":"355","areaName":"阿尔巴尼亚","enName":"Albania"},{"areaCode":"213","areaName":"阿尔及利亚","enName":"Algeria"},{"areaCode":"376","areaName":"安道尔共和国","enName":"Andorra"},{"areaCode":"1264","areaName":"安圭拉岛","enName":"Anguilla"},{"areaCode":"1268","areaName":"安提瓜和巴布达","enName":"Antigua and Barbuda"},{"areaCode":"54","areaName":"阿根廷","enName":"Argentina"},{"areaCode":"374","areaName":"亚美尼亚","enName":"Armenia"},{"areaCode":"247","areaName":"阿森松","enName":"Ascension"},{"areaCode":"61","areaName":"澳大利亚","enName":"Australia"},{"areaCode":"43","areaName":"奥地利","enName":"Austria"},{"areaCode":"994","areaName":"阿塞拜疆","enName":"Azerbaijan"},{"areaCode":"1242","areaName":"巴哈马","enName":"Bahamas"},{"areaCode":"973","areaName":"巴林","enName":"Bahrain"},{"areaCode":"880","areaName":"孟加拉国","enName":"Bangladesh"},{"areaCode":"1246","areaName":"巴巴多斯","enName":"Barbados"},{"areaCode":"375","areaName":"白俄罗斯","enName":"Belarus"},{"areaCode":"32","areaName":"比利时","enName":"Belgium"},{"areaCode":"501","areaName":"伯利兹","enName":"Belize"},{"areaCode":"229","areaName":"贝宁","enName":"Benin"},{"areaCode":"1441","areaName":"百慕大群岛","enName":"Bermuda Is."},{"areaCode":"591","areaName":"玻利维亚","enName":"Bolivia"},{"areaCode":"267","areaName":"博茨瓦纳","enName":"Botswana"},{"areaCode":"55","areaName":"巴西","enName":"Brazil"},{"areaCode":"57","areaName":"哥伦比亚","enName":"Colombia"},{"areaCode":"242","areaName":"刚果","enName":"Congo"},{"areaCode":"682","areaName":"库克群岛","enName":"Cook Is."},{"areaCode":"506","areaName":"哥斯达黎加","enName":"Costa Rica"},{"areaCode":"53","areaName":"古巴","enName":"Cuba"},{"areaCode":"357","areaName":"塞浦路斯","enName":"Cyprus"},{"areaCode":"420","areaName":"捷克","enName":"Czech Republic "},{"areaCode":"45","areaName":"丹麦","enName":"Denmark"},{"areaCode":"253","areaName":"吉布提","enName":"Djibouti"},{"areaCode":"1890","areaName":"多米尼加共和国","enName":"Dominica Rep."},{"areaCode":"593","areaName":"厄瓜多尔","enName":"Ecuador"},{"areaCode":"20","areaName":"埃及","enName":"Egypt"},{"areaCode":"503","areaName":"萨尔瓦多","enName":"EI Salvador"},{"areaCode":"372","areaName":"爱沙尼亚","enName":"Estonia"},{"areaCode":"251","areaName":"埃塞俄比亚","enName":"Ethiopia"},{"areaCode":"679","areaName":"斐济","enName":"Fiji"},{"areaCode":"358","areaName":"芬兰","enName":"Finland"},{"areaCode":"33","areaName":"法国","enName":"France"},{"areaCode":"594","areaName":"法属圭亚那","enName":"French Guiana"},{"areaCode":"241","areaName":"加蓬","enName":"Gabon"},{"areaCode":"220","areaName":"冈比亚","enName":"Gambia"},{"areaCode":"995","areaName":"格鲁吉亚","enName":"Georgia "},{"areaCode":"49","areaName":"德国","enName":"Germany "},{"areaCode":"233","areaName":"加纳","enName":"Ghana"},{"areaCode":"350","areaName":"直布罗陀","enName":"Gibraltar"},{"areaCode":"30","areaName":"希腊","enName":"Greece"},{"areaCode":"1809","areaName":"格林纳达","enName":"Grenada"},{"areaCode":"1671","areaName":"关岛","enName":"Guam"},{"areaCode":"502","areaName":"危地马拉","enName":"Guatemala"},{"areaCode":"224","areaName":"几内亚","enName":"Guinea"},{"areaCode":"592","areaName":"圭亚那","enName":"Guyana"},{"areaCode":"509","areaName":"海地","enName":"Haiti"},{"areaCode":"504","areaName":"洪都拉斯","enName":"Honduras"}]
     */

    private int errorCode;
    private String errorMsg;
    private List<ReturnDataBean> returnData;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<ReturnDataBean> getReturnData() {
        return returnData;
    }

    public void setReturnData(List<ReturnDataBean> returnData) {
        this.returnData = returnData;
    }

    public static class ReturnDataBean {
        /**
         * areaCode : 86
         * areaName : 中国
         * enName : China
         */

        private String areaCode;
        private String areaName;
        private String enName;

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getEnName() {
            return enName;
        }

        public void setEnName(String enName) {
            this.enName = enName;
        }
    }
}
