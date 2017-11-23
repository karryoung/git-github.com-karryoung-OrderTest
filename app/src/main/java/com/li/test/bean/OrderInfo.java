package com.li.test.bean;

import java.io.Serializable;

/**
 * 订单实体类
 */
public class OrderInfo implements Serializable {
    public int Id;
    public String UserName;
    public String UserMobile;
    public String OrderNo;
    public int Status;
    public String StatusName;
    public String CreateDate;
}
