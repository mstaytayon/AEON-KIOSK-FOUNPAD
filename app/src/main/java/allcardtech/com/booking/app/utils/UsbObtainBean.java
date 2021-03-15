package allcardtech.com.booking.app.utils;


import java.util.List;

public class UsbObtainBean {
    private List<UsbObtainBean.UsbDataBean> escList;
    private List<UsbObtainBean.UsbDataBean> tscList;

    public UsbObtainBean() {
    }

    public List<UsbObtainBean.UsbDataBean> getEscList() {
        return this.escList;
    }

    public void setEscList(List<UsbObtainBean.UsbDataBean> escList) {
        this.escList = escList;
    }

    public List<UsbObtainBean.UsbDataBean> getTscList() {
        return this.tscList;
    }

    public void setTscList(List<UsbObtainBean.UsbDataBean> tscList) {
        this.tscList = tscList;
    }

    public static class UsbDataBean {
        private int vid;
        private int pid;

        public UsbDataBean() {
        }

        public UsbDataBean(int vid, int pid) {
            this.vid = vid;
            this.pid = pid;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                UsbObtainBean.UsbDataBean that = (UsbObtainBean.UsbDataBean)o;
                if (this.vid != that.vid) {
                    return false;
                } else {
                    return this.pid == that.pid;
                }
            } else {
                return false;
            }
        }

        public int hashCode() {
            int result = this.vid;
            result = 31 * result + this.pid;
            return result;
        }

        public int getVid() {
            return this.vid;
        }

        public void setVid(int vid) {
            this.vid = vid;
        }

        public int getPid() {
            return this.pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }
    }
}
