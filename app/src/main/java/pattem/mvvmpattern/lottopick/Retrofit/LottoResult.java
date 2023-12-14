package pattem.mvvmpattern.lottopick.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LottoResult {

    /**
     * totSellamnnt : 총 상금액  ex) 2555541
     * returnValue  : 요청 결과  ex) succes
     * drwNoDate    : 날짜       ex) 2004-10-30
     * firstWinamnt : 1등 상금액 ex) 221214
     * drwtNo1      : 1번 번호
     * drwtNo2      : 2번 번호
     * drwtNo3      : 3번 번호
     * drwtNo4      : 4번 번호
     * drwtNo5      : 5번 번호
     * drwtNo6      : 6번 번호
     * bnusNo       : 보너스 번호
     * drwNo        : 로또 회차차     */

    @SerializedName("totSellamnt")
    @Expose
    private Long totSellamnt;
    @SerializedName("returnValue")
    @Expose
    private String returnValue;
    @SerializedName("drwNoDate")
    @Expose
    private String drwNoDate;
    @SerializedName("firstWinamnt")
    @Expose
    private Long firstWinamnt;
    @SerializedName("firstPrzwnerCo")
    @Expose
    private Integer firstPrzwnerCo;
    @SerializedName("drwtNo1")
    @Expose
    private Integer drwtNo1;
    @SerializedName("drwtNo2")
    @Expose
    private Integer drwtNo2;
    @SerializedName("drwtNo3")
    @Expose
    private Integer drwtNo3;
    @SerializedName("drwtNo4")
    @Expose
    private Integer drwtNo4;
    @SerializedName("drwtNo5")
    @Expose
    private Integer drwtNo5;
    @SerializedName("drwtNo6")
    @Expose
    private Integer drwtNo6;
    @SerializedName("bnusNo")
    @Expose
    private Integer bnusNo;
    @SerializedName("firstAccumamnt")
    @Expose
    private Long firstAccumamnt;
    @SerializedName("drwNo")
    @Expose
    private Integer drwNo;




    public Long getTotSellamnt() {
        return totSellamnt;
    }

    public void setTotSellamnt(Long totSellamnt) {
        this.totSellamnt = totSellamnt;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getDrwNoDate() {
        return drwNoDate;
    }

    public void setDrwNoDate(String drwNoDate) {
        this.drwNoDate = drwNoDate;
    }

    public Long getFirstWinamnt() {
        return firstWinamnt;
    }

    public void setFirstWinamnt(Long firstWinamnt) {
        this.firstWinamnt = firstWinamnt;
    }

    public Integer getDrwtNo6() {
        return drwtNo6;
    }

    public void setDrwtNo6(Integer drwtNo6) {
        this.drwtNo6 = drwtNo6;
    }

    public Integer getDrwtNo4() {
        return drwtNo4;
    }

    public void setDrwtNo4(Integer drwtNo4) {
        this.drwtNo4 = drwtNo4;
    }

    public Integer getFirstPrzwnerCo() {
        return firstPrzwnerCo;
    }

    public void setFirstPrzwnerCo(Integer firstPrzwnerCo) {
        this.firstPrzwnerCo = firstPrzwnerCo;
    }

    public Integer getDrwtNo5() {
        return drwtNo5;
    }

    public void setDrwtNo5(Integer drwtNo5) {
        this.drwtNo5 = drwtNo5;
    }

    public Integer getBnusNo() {
        return bnusNo;
    }

    public void setBnusNo(Integer bnusNo) {
        this.bnusNo = bnusNo;
    }

    public Long getFirstAccumamnt() {
        return firstAccumamnt;
    }

    public void setFirstAccumamnt(Long firstAccumamnt) {
        this.firstAccumamnt = firstAccumamnt;
    }

    public Integer getDrwNo() {
        return drwNo;
    }

    public void setDrwNo(Integer drwNo) {
        this.drwNo = drwNo;
    }

    public Integer getDrwtNo2() {
        return drwtNo2;
    }

    public void setDrwtNo2(Integer drwtNo2) {
        this.drwtNo2 = drwtNo2;
    }

    public Integer getDrwtNo3() {
        return drwtNo3;
    }

    public void setDrwtNo3(Integer drwtNo3) {
        this.drwtNo3 = drwtNo3;
    }

    public Integer getDrwtNo1() {
        return drwtNo1;
    }

    public void setDrwtNo1(Integer drwtNo1) {
        this.drwtNo1 = drwtNo1;
    }

}
