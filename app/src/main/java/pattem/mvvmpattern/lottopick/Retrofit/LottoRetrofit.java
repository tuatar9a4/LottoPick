package pattem.mvvmpattern.lottopick.Retrofit;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import pattem.mvvmpattern.lottopick.database.LottoDbHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class LottoRetrofit {


    Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("https://www.dhlottery.co.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    LottoService lottoService= retrofit.create(LottoService.class);

    private ArrayList<LottoResult> lottoResultList=new ArrayList<>();
    private MutableLiveData<String> lottoItemData=new MutableLiveData<>();

    public MutableLiveData<String> getLottoItemData() {
        return lottoItemData;
    }
    public ArrayList<LottoResult> getLottoResultList() {
        return lottoResultList;
    }
    public void getLottoNum(int round){
        Call<LottoResult> lottoItem=lottoService.callresult(String.valueOf(round));
        lottoItem.enqueue(new Callback<LottoResult>() {
            @Override
            public void onResponse(Call<LottoResult> call, Response<LottoResult> response) {
                if (response.isSuccessful()){
                    if (response.body().getReturnValue().equals("fail")){
                        Log.d("도원","fail > round : "+round);
                        lottoItemData.setValue("fail&"+round);
                    }else{
                        Log.d("도원","success > round : "+round);
                        lottoResultList.add(response.body());
                        lottoItemData.setValue("success&"+round);
                    }
                    Log.d("도원 ","성공 ");
//                    lottoDbHelper.insertColumn("2","2","3","4","5","6","7","8");
                }else{
                    Log.d("도원 ","실패 ");
                }

            }

            @Override
            public void onFailure(Call<LottoResult> call, Throwable t) {
                Log.d("도원 ","실패 ");
            }
        });
    }

    public interface LottoService {
        @POST("/common.do?method=getLottoNumber")
        Call<LottoResult> callresult(
                @Query("drwNo") String num

        );
    }
}
