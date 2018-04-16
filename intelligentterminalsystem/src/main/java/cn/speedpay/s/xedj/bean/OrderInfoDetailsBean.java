package cn.speedpay.s.xedj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taomf on 2016/8/22.
 * 订单详情信息实体
 */
public class OrderInfoDetailsBean implements Serializable{


    /**
     * address : 北京市北京市海淀区农大南路5号
     * asigntime : 2016-01-04 10:40:58
     * bhtime : 2016-01-04 21:19:34
     * bujiaoprice : 0
     * confirmtime : 2016-01-04 10:40:59
     * couponpayprice : 0.0
     * fptitle :
     * fptype :
     * fptypename :
     * gethtime : 2016-01-04 10:40:58
     * goodsinfo : [{"goodscnt":1,"goodsid":"B15640","goodsname":"皇家骑士 14度加强烈性啤酒 500ml/听","zkgoodsprice":3.96},{"goodscnt":1,"goodsid":"B15603","goodsname":"波罗的海 雅士冰纯啤酒 500ml/听","zkgoodsprice":3.96},{"goodscnt":1,"goodsid":"B15638","goodsname":"莫吉托 鸡尾酒 275ml/瓶","zkgoodsprice":3.96}]
     * isappoint : 1
     * isfp : 否
     * name : 我是大哥
     * orderaccount : 15620978888
     * ordergoodsallprice : 7.92
     * orderid : 16010410394592010001
     * orderpaychannel : 货到付款
     * orderpayprice : 7.92
     * orderphone : 15620978888
     * orderstatus : 发货中
     * ordersupermarket : 超市发-上地超市
     * ordertime : 2016-04-14 15:00:00
     * ordertype :
     * paytime :
     * phone : 15620978888
     * printcnt : 8
     * ptallprice : 0.0
     * remark :
     * resultcode : 0
     * resultdesc : 订单小票打印成功
     * scopetype : 1
     * sendtime : 2016-01-11 17:32:54
     * updatetime : 2016-01-04 20:07:57
     * yfallprice : 8.0
     * yhprice : 0.0
     * yxjmprice : 0.79
     * zoneid : ZI141223135650820001
     * zonename : 上地社区
     */

    private String address;
    private String asigntime;
    private String bhtime;
    private int bujiaoprice;
    private String confirmtime;
    private double couponpayprice;
    private String fptitle;
    private String fptype;
    private String fptypename;
    private String gethtime;
    private String isappoint;
    private String isfp;
    private String name;
    private String orderaccount;
    private double ordergoodsallprice;
    private String orderid;
    private String orderpaychannel;
    private double orderpayprice;
    private String orderphone;
    private String orderstatus;
    private String ordersupermarket;
    private String ordertime;
    private String ordertype;
    private String paytime;
    private String phone;
    private int printcnt;
    private double ptallprice;
    private String remark;
    private String resultcode;
    private String resultdesc;
    private String scopetype;
    private String sendtime;
    private String updatetime;
    private double yfallprice;
    private double yhprice;
    private double yxjmprice;
    private String zoneid;
    private String zonename;
    /**
     * goodscnt : 1
     * goodsid : B15640
     * goodsname : 皇家骑士 14度加强烈性啤酒 500ml/听
     * zkgoodsprice : 3.96
     */

    private List<GoodsinfoBean> goodsinfo;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAsigntime() {
        return asigntime;
    }

    public void setAsigntime(String asigntime) {
        this.asigntime = asigntime;
    }

    public String getBhtime() {
        return bhtime;
    }

    public void setBhtime(String bhtime) {
        this.bhtime = bhtime;
    }

    public int getBujiaoprice() {
        return bujiaoprice;
    }

    public void setBujiaoprice(int bujiaoprice) {
        this.bujiaoprice = bujiaoprice;
    }

    public String getConfirmtime() {
        return confirmtime;
    }

    public void setConfirmtime(String confirmtime) {
        this.confirmtime = confirmtime;
    }

    public double getCouponpayprice() {
        return couponpayprice;
    }

    public void setCouponpayprice(double couponpayprice) {
        this.couponpayprice = couponpayprice;
    }

    public String getFptitle() {
        return fptitle;
    }

    public void setFptitle(String fptitle) {
        this.fptitle = fptitle;
    }

    public String getFptype() {
        return fptype;
    }

    public void setFptype(String fptype) {
        this.fptype = fptype;
    }

    public String getFptypename() {
        return fptypename;
    }

    public void setFptypename(String fptypename) {
        this.fptypename = fptypename;
    }

    public String getGethtime() {
        return gethtime;
    }

    public void setGethtime(String gethtime) {
        this.gethtime = gethtime;
    }

    public String getIsappoint() {
        return isappoint;
    }

    public void setIsappoint(String isappoint) {
        this.isappoint = isappoint;
    }

    public String getIsfp() {
        return isfp;
    }

    public void setIsfp(String isfp) {
        this.isfp = isfp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderaccount() {
        return orderaccount;
    }

    public void setOrderaccount(String orderaccount) {
        this.orderaccount = orderaccount;
    }

    public double getOrdergoodsallprice() {
        return ordergoodsallprice;
    }

    public void setOrdergoodsallprice(double ordergoodsallprice) {
        this.ordergoodsallprice = ordergoodsallprice;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderpaychannel() {
        return orderpaychannel;
    }

    public void setOrderpaychannel(String orderpaychannel) {
        this.orderpaychannel = orderpaychannel;
    }

    public double getOrderpayprice() {
        return orderpayprice;
    }

    public void setOrderpayprice(double orderpayprice) {
        this.orderpayprice = orderpayprice;
    }

    public String getOrderphone() {
        return orderphone;
    }

    public void setOrderphone(String orderphone) {
        this.orderphone = orderphone;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getOrdersupermarket() {
        return ordersupermarket;
    }

    public void setOrdersupermarket(String ordersupermarket) {
        this.ordersupermarket = ordersupermarket;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPrintcnt() {
        return printcnt;
    }

    public void setPrintcnt(int printcnt) {
        this.printcnt = printcnt;
    }

    public double getPtallprice() {
        return ptallprice;
    }

    public void setPtallprice(double ptallprice) {
        this.ptallprice = ptallprice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getResultdesc() {
        return resultdesc;
    }

    public void setResultdesc(String resultdesc) {
        this.resultdesc = resultdesc;
    }

    public String getScopetype() {
        return scopetype;
    }

    public void setScopetype(String scopetype) {
        this.scopetype = scopetype;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public double getYfallprice() {
        return yfallprice;
    }

    public void setYfallprice(double yfallprice) {
        this.yfallprice = yfallprice;
    }

    public double getYhprice() {
        return yhprice;
    }

    public void setYhprice(double yhprice) {
        this.yhprice = yhprice;
    }

    public double getYxjmprice() {
        return yxjmprice;
    }

    public void setYxjmprice(double yxjmprice) {
        this.yxjmprice = yxjmprice;
    }

    public String getZoneid() {
        return zoneid;
    }

    public void setZoneid(String zoneid) {
        this.zoneid = zoneid;
    }

    public String getZonename() {
        return zonename;
    }

    public void setZonename(String zonename) {
        this.zonename = zonename;
    }

    public List<GoodsinfoBean> getGoodsinfo() {
        return goodsinfo;
    }

    public void setGoodsinfo(List<GoodsinfoBean> goodsinfo) {
        this.goodsinfo = goodsinfo;
    }

    public static class GoodsinfoBean {
        private int goodscnt;
        private String goodsid;
        private String goodsname;
        private double zkgoodsprice;

        public int getGoodscnt() {
            return goodscnt;
        }

        public void setGoodscnt(int goodscnt) {
            this.goodscnt = goodscnt;
        }

        public String getGoodsid() {
            return goodsid;
        }

        public void setGoodsid(String goodsid) {
            this.goodsid = goodsid;
        }

        public String getGoodsname() {
            return goodsname;
        }

        public void setGoodsname(String goodsname) {
            this.goodsname = goodsname;
        }

        public double getZkgoodsprice() {
            return zkgoodsprice;
        }

        public void setZkgoodsprice(double zkgoodsprice) {
            this.zkgoodsprice = zkgoodsprice;
        }
    }
}
