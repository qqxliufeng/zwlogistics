package com.android.ql.lf.zwlogistics.data;

public class PostSelectOrderBean {

    private String srcAddress = "0";
    private String desAddress = "0";
    private String lengthParams = "0";
    private String carTypeParams = "0";


    public String getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(String srcAddress) {
        this.srcAddress = srcAddress;
    }

    public String getDesAddress() {
        return desAddress;
    }

    public void setDesAddress(String desAddress) {
        this.desAddress = desAddress;
    }

    public String getLengthParams() {
        return lengthParams;
    }

    public void setLengthParams(String lengthParams) {
        this.lengthParams = lengthParams;
    }

    public String getCarTypeParams() {
        return carTypeParams;
    }

    public void setCarTypeParams(String carTypeParams) {
        this.carTypeParams = carTypeParams;
    }
}
