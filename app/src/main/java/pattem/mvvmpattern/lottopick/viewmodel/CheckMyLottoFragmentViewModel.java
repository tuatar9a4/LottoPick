package pattem.mvvmpattern.lottopick.viewmodel;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pattem.mvvmpattern.lottopick.R;
import pattem.mvvmpattern.lottopick.adapter.CheckNumItemAdapter;
import pattem.mvvmpattern.lottopick.adapter_item.LottoCheckResult;
import pattem.mvvmpattern.lottopick.database.ExpectDBHelper;
import pattem.mvvmpattern.lottopick.database.LottoDbHelper;

public class CheckMyLottoFragmentViewModel extends ViewModel {
    private LifecycleOwner lifecycleOwner;
    private ExpectDBHelper expectDBHelper ;
    private LottoDbHelper lottoDbHelper;
    private ArrayList<String> tblNameList=new ArrayList<>();
    private ArrayList<LottoCheckResult> expectList = new ArrayList<>();
    private ArrayList<ArrayList<LottoCheckResult>> expectItemList=new ArrayList<>();
    private ArrayList<String> countingList=new ArrayList<>();
    private MutableLiveData<ArrayList<String>> countingLiveData =new MutableLiveData<>();
    private MutableLiveData<View> expectView=new MutableLiveData<>();
    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void openDb(Context context){
        expectDBHelper=new ExpectDBHelper(context);
        expectDBHelper.open();
        lottoDbHelper=new LottoDbHelper(context);
        lottoDbHelper.open();
    }

    public void getExpectList(Context context){
        Cursor cursor=expectDBHelper.getAllTBLName();
        tblNameList.clear();
        while (cursor.moveToNext()){
            Log.d("도원",cursor.getString(0)+"//");
            if (cursor.getString(0).startsWith("round")){
                tblNameList.add(cursor.getString(0));
            }
        }
        expectList.clear();
        expectItemList.clear();
        countingList.clear();
        for (String str : tblNameList){
            ArrayList<String> roundNum=new ArrayList<>();
            expectList=new ArrayList<>();
            Cursor cursor2=lottoDbHelper.selectRoundNum((Integer.parseInt(str.replace("round","")))+"");
//            Cursor cursor2=lottoDbHelper.sortColumn(LottoDatabase.CreateDB.LOTTOROUND);
            boolean noResult=false;
            if (cursor2.getCount()>0) noResult=true;
            while(cursor2.moveToNext()){
                roundNum.clear();
                roundNum.add(String.valueOf(cursor2.getInt(2)));
                roundNum.add(String.valueOf(cursor2.getInt(3)));
                roundNum.add(String.valueOf(cursor2.getInt(4)));
                roundNum.add(String.valueOf(cursor2.getInt(5)));
                roundNum.add(String.valueOf(cursor2.getInt(6)));
                roundNum.add(String.valueOf(cursor2.getInt(7)));
                roundNum.add(String.valueOf(cursor2.getInt(8)));
            }
            Cursor cursor1=expectDBHelper.selectColumns(str);
            expectList.clear();
            while (cursor1.moveToNext()){
                LottoCheckResult lottoCheckResult=new LottoCheckResult();
                lottoCheckResult.setNo1(cursor1.getString(2));
                lottoCheckResult.setNo2(cursor1.getString(3));
                lottoCheckResult.setNo3(cursor1.getString(4));
                lottoCheckResult.setNo4(cursor1.getString(5));
                lottoCheckResult.setNo5(cursor1.getString(6));
                lottoCheckResult.setNo6(cursor1.getString(7));
                int counting=1;
                if (roundNum.size()==0) counting=0;
                for (int a=0; a<roundNum.size()-1;a++){
                    for (int b=0; b<roundNum.size(); b++){
                        String temp="" ;
                        switch (a){
                            case 0:
                                temp=lottoCheckResult.getNo1()+"";
                                break;
                            case 1:
                                temp=lottoCheckResult.getNo2()+"";
                                break;
                            case 2:
                                temp=lottoCheckResult.getNo3()+"";
                                break;
                            case 3:
                                temp=lottoCheckResult.getNo4()+"";
                                break;
                            case 4:
                                temp=lottoCheckResult.getNo5()+"";
                                break;
                            case 5:
                                temp=lottoCheckResult.getNo6()+"";
                                break;
                        }


                        //1등 번호
                        if (temp.equals(roundNum.get(b))){
                            switch (a){
                                case 0:
                                    lottoCheckResult.setNo1("1&"+lottoCheckResult.getNo1());
                                    break;
                                case 1:
                                    lottoCheckResult.setNo2("1&"+lottoCheckResult.getNo2());
                                    break;
                                case 2:
                                    lottoCheckResult.setNo3("1&"+lottoCheckResult.getNo3());
                                    break;
                                case 3:
                                    lottoCheckResult.setNo4("1&"+lottoCheckResult.getNo4());
                                    break;
                                case 4:
                                    lottoCheckResult.setNo5("1&"+lottoCheckResult.getNo5());
                                    break;
                                case 5:
                                    lottoCheckResult.setNo6("1&"+lottoCheckResult.getNo6());
                                    break;
                            }

                            counting++;
                        }
                        if (counting==6 && b==6){
                            if (temp.equals(roundNum.get(b))){
                                counting+=2;
                            }
                        }


                    }
                }
                lottoCheckResult.setResult(counting+"");
                countingList.add(counting+"");
//                Log.d("도원",cursor1.getString(0)+"round : "+cursor1.getString(1) + " / "
//                +cursor1.getString(2)+" / "
//                                +cursor1.getString(3)+" / "
//                                +cursor1.getString(4)+" / "
//                                +cursor1.getString(5)+" / "
//                                +cursor1.getString(6)+" / "
//                                +cursor1.getString(7)+" / "
//                );
                if (lottoCheckResult.getNo1().split("&").length==1)lottoCheckResult.setNo1("0&"+lottoCheckResult.getNo1());
                if (lottoCheckResult.getNo2().split("&").length==1)lottoCheckResult.setNo2("0&"+lottoCheckResult.getNo2());
                if (lottoCheckResult.getNo3().split("&").length==1)lottoCheckResult.setNo3("0&"+lottoCheckResult.getNo3());
                if (lottoCheckResult.getNo4().split("&").length==1)lottoCheckResult.setNo4("0&"+lottoCheckResult.getNo4());
                if (lottoCheckResult.getNo5().split("&").length==1)lottoCheckResult.setNo5("0&"+lottoCheckResult.getNo5());
                if (lottoCheckResult.getNo6().split("&").length==1)lottoCheckResult.setNo6("0&"+lottoCheckResult.getNo6());

                expectList.add(lottoCheckResult);
            }
            expectItemList.add(expectList);
//            createExpectView(context,expectList,str);
        }

        createExpectView(context,expectItemList,tblNameList);
        countingLiveData.setValue(countingList);

    }

    public void createExpectView(Context context,ArrayList<ArrayList<LottoCheckResult>> expectList,ArrayList<String> round){
       for (int a=0; a<expectList.size();a++){
           LayoutInflater layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           View view=layoutInflater.inflate(R.layout.expect_titlelist_item,null);
           TextView tvExpectTitle=view.findViewById(R.id.tvExpectTitle);
           RecyclerView rcExpectList=view.findViewById(R.id.rcExpectList);
           rcExpectList.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
           tvExpectTitle.setText(round.get(a));
           if (round.get(a).contains("991")){
               Log.d("도원","expectList.get(a) : "+expectList.get(a));
           }
           CheckNumItemAdapter checkNumItemAdapter=new CheckNumItemAdapter(context);
           checkNumItemAdapter.setLottoResults(expectList.get(a));
           rcExpectList.setAdapter(checkNumItemAdapter);
           tvExpectTitle.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   ValueAnimator va= rcExpectList.getLayoutParams().height<100? ValueAnimator.ofInt(0,700):ValueAnimator.ofInt(700,0);
                   va.setDuration(400);
                   va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                       @Override
                       public void onAnimationUpdate(ValueAnimator animation) {
                           int value=(int)animation.getAnimatedValue();
                           rcExpectList.getLayoutParams().height=value;
                           rcExpectList.requestLayout();

                       }
                   });
                   va.start();
               }
           });

           expectView.setValue(view);
       }
    }

    public MutableLiveData<View> getExpectView() {
        return expectView;
    }

    public MutableLiveData<ArrayList<String>> getCountingLiveData() {
        return countingLiveData;
    }
}
