package com.sinosafe.payment.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with base.
 * User: anguszhu
 * Date: Apr,06 2016
 * Time: 8:15 PM
 * description:
 */
public class CommonVo  implements Serializable{

    private String createdBy;

    private String updatedBy;

    private Date createdDate;

    private Date updatedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
