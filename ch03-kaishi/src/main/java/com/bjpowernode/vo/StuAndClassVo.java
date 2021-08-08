package com.bjpowernode.vo;


//多个的字段组合
public class StuAndClassVo {
    int sid;
    String sname;
    String semail;
    int sage;
    int cid;
    String cname ;

    public StuAndClassVo() {
    }

    @Override
    public String toString() {
        return "StuAndClassVo{" +
                "sid=" + sid +
                ", sname='" + sname + '\'' +
                ", semail='" + semail + '\'' +
                ", sage=" + sage +
                ", cid=" + cid +
                ", cname='" + cname + '\'' +
                '}';
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSemail() {
        return semail;
    }

    public void setSemail(String semail) {
        this.semail = semail;
    }

    public int getSage() {
        return sage;
    }

    public void setSage(int sage) {
        this.sage = sage;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public StuAndClassVo(int sid, String sname, String semail, int sage, int cid, String cname) {
        this.sid = sid;
        this.sname = sname;
        this.semail = semail;
        this.sage = sage;
        this.cid = cid;
        this.cname = cname;
    }
}
