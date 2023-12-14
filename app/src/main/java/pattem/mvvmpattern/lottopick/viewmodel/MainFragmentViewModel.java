package pattem.mvvmpattern.lottopick.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import pattem.mvvmpattern.lottopick.MainActivity;
import pattem.mvvmpattern.lottopick.Retrofit.LottoResult;
import pattem.mvvmpattern.lottopick.Retrofit.LottoRetrofit;
import pattem.mvvmpattern.lottopick.database.ExpectDBHelper;
import pattem.mvvmpattern.lottopick.database.LottoDatabase;
import pattem.mvvmpattern.lottopick.database.LottoDbHelper;

public class MainFragmentViewModel  extends ViewModel {

    private LifecycleOwner mLifeCycle;
    private LottoRetrofit lottoRetrofit=new LottoRetrofit();

    private int startNum=1;

    //최신 회차
    private MutableLiveData<Integer> newestRound=new MutableLiveData<>();
    private ArrayList<LottoResult> lottoItemsArray=new ArrayList<>();
    private HashMap<Integer,ArrayList<Integer>> lastPickNumber =new HashMap<>();
    private ArrayList<String> expectNumList =new ArrayList<>();
    private ArrayList<Integer> pickString=new ArrayList<>();
    private MutableLiveData<ArrayList<String>> livePickString =new MutableLiveData<>();

    private MutableLiveData<String> downRound=new MutableLiveData<>();


    private LottoDbHelper lottoDbHelper;
    private ExpectDBHelper expectDBHelper;

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private String selectTblName="";
    private String selectRound="";
    private long winnerPrize=0;

    public void setLifeCycle(LifecycleOwner mLifeCycle){
        this.mLifeCycle=mLifeCycle;
        bindData();
    }
    public void openDB(Context context){
        //LOTTO 크롤링 DB
        lottoDbHelper=new LottoDbHelper(context);
        lottoDbHelper.open();
//        lottoDbHelper.dropTable();
        lottoDbHelper.create();

        //예상 번호 DB
        expectDBHelper=new ExpectDBHelper(context);
        expectDBHelper.open();
        //create 는 테이블 데이터를 받아온 후 정한다.
    }

    //처음 시작 부분 DB에 데이터가 있는지 없는지 확인 후 진행
    public void getLottoList(){
        if (lottoDbHelper.selectColumns().getCount()>0){
            Log.d("도원","count : "+lottoDbHelper.selectColumns().getCount());
            Cursor cursor=lottoDbHelper.sortColumn(LottoDatabase.CreateDB.LOTTOROUND);
//            Log.d("도원","item : "+cursor.getInt(1));
            if (cursor.moveToNext()){
                Log.d("도원","item1 : "+cursor.getInt(1));
                lottoRetrofit.getLottoNum(cursor.getInt(1)+1);

            }
            cursor.close();
        }else{
            lottoRetrofit.getLottoNum(startNum);
        }
    }


    public void bindData(){
        lottoRetrofit.getLottoItemData().observe(mLifeCycle, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                String[] temp=str.split("&");
                Log.d("도원","temp[0] : "+temp[0] + "|| temp[1] : "+temp[1]);
                if(temp[0].equals("success")){
                    startNum+=1;
                    lottoRetrofit.getLottoNum(Integer.parseInt(temp[1])+1);
                    downRound.setValue(Integer.parseInt(temp[1])+"");

                }else{
                    //예상번호 DB 생성
                    selectTblName="round"+temp[1];
                    selectRound=temp[1];
                    expectDBHelper.create(selectTblName);
                    newestRound.setValue(Integer.parseInt(temp[1])-1);
                    setLottoListDatabase(lottoRetrofit.getLottoResultList());
                }
            }
        });

    }

    public void setLottoListDatabase(ArrayList<LottoResult> receiveList){
        for (int a = 0 ; a < receiveList.size(); a++) {
            Log.d("도원","recevieList: "+receiveList.get(a).getDrwNo()+ "// "+receiveList.get(a).getFirstWinamnt());
            lottoDbHelper.insertColumn(receiveList.get(a).getDrwNo().toString(),receiveList.get(a).getDrwtNo1().toString(),receiveList.get(a).getDrwtNo2().toString(),
                    receiveList.get(a).getDrwtNo3().toString(),receiveList.get(a).getDrwtNo4().toString(),receiveList.get(a).getDrwtNo5().toString(),
                    receiveList.get(a).getDrwtNo6().toString(),receiveList.get(a).getBnusNo().toString(),receiveList.get(a).getFirstAccumamnt().toString());
        }
        pickNum();
    }

    public void setExpectDBCreateAndInsert(){
        expectDBHelper.create(selectTblName);
        Cursor expectCursor=expectDBHelper.sortColumn(selectTblName,"lottoRound");
//        Log.d("도원","expectCursor : "+expectCursor.getCount());
        if (expectCursor.getCount()==0){
//            pickUpNum();
        }else{
            ArrayList<String> expectDbData=new ArrayList<>();
            while (expectCursor.moveToNext()){
                String temp =expectCursor.getInt(1)+"&"+expectCursor.getInt(2)+"&"+expectCursor.getInt(3)+"&"+expectCursor.getInt(4)+"&"+
                        expectCursor.getInt(5)+"&"+expectCursor.getInt(6)+"&"+expectCursor.getInt(7);
                expectDbData.add(temp);
            }
            livePickString.setValue(expectDbData);
        }
        expectCursor.close();
    }

    /**
     * totalNumToNum : 전체가 들어 있는 HashMap <위치,HashMap<숫자경우,Hashmap<숫자,나온횟수>>>
     * firstOneToNum : 첫번째 1일때 다음수 num *45
     * secondOneToNum : 두번째 1일때 다음수 num *45
     * thirdOneToNum : 세번째 1일때 다음수 num *45
     * fourOneToNum : 네번째 1일때 다음수 num *45
     * fifthOneToNum : 다섯번째 1일때 다음수 num *45
     * sixthOneToNum : 여섯번째 1일때 다음수 num *45
     *
     */
    private HashMap<Integer,HashMap<Integer,HashMap<Integer,Integer>>> totalNumToNum=new HashMap<>();
    private HashMap<Integer,HashMap<Integer,Integer>> firstNumToNum = new HashMap<>();
    private HashMap<Integer,HashMap<Integer,Integer>> secondNumToNum = new HashMap<>();
    private HashMap<Integer,HashMap<Integer,Integer>> thirdNumToNum = new HashMap<>();
    private HashMap<Integer,HashMap<Integer,Integer>> fourNumToNum = new HashMap<>();
    private HashMap<Integer,HashMap<Integer,Integer>> fifthNumToNum = new HashMap<>();
    private HashMap<Integer,HashMap<Integer,Integer>> sixthNumToNum = new HashMap<>();


    public void pickNum () {
        Cursor cursor=lottoDbHelper.sortColumn(LottoDatabase.CreateDB.LOTTOROUND);
        lottoItemsArray.clear();
        //베이직 버전은 최대 100회차만
        int startCount=0;
        for (int b=startCount;b<cursor.getCount();b++){
            LottoResult tempResult=new LottoResult();
            if (cursor.moveToNext()){
                tempResult.setDrwNo(cursor.getInt(1));
                tempResult.setDrwtNo1(cursor.getInt(2));
                tempResult.setDrwtNo2(cursor.getInt(3));
                tempResult.setDrwtNo3(cursor.getInt(4));
                tempResult.setDrwtNo4(cursor.getInt(5));
                tempResult.setDrwtNo5(cursor.getInt(6));
                tempResult.setDrwtNo6(cursor.getInt(7));
                tempResult.setBnusNo(cursor.getInt(8));
                tempResult.setFirstWinamnt(cursor.getLong(9));
            }
            lottoItemsArray.add(tempResult);
        }
        cursor.close();

        for(int a=0 ; a<lottoItemsArray.size()-1; a++){
            for(int c = 1; c<46; c++) {
                for (int b=1; b<46; b++) {
                    if (lottoItemsArray.get(a).getDrwtNo1() == c) {
                        if (lottoItemsArray.get(a+1).getDrwtNo1() == b) {
                            firstNumToNum.put(c,plusPickNum(firstNumToNum.get(c),b));
                        }
                    }
                    if(lottoItemsArray.get(a).getDrwtNo2()==c){
                        if(lottoItemsArray.get(a+1).getDrwtNo2() ==b){
                            secondNumToNum.put(c,plusPickNum(secondNumToNum.get(c),b));
                        }
                    }
                    if(lottoItemsArray.get(a).getDrwtNo3()==c){
                        if(lottoItemsArray.get(a+1).getDrwtNo3() ==b){
                            thirdNumToNum.put(c,plusPickNum(thirdNumToNum.get(c),b));
                        }
                    }
                    if(lottoItemsArray.get(a).getDrwtNo4()==c){
                        if(lottoItemsArray.get(a+1).getDrwtNo4() ==b){
                            fourNumToNum.put(c,plusPickNum(fourNumToNum.get(c),b));
                        }
                    }
                    if(lottoItemsArray.get(a).getDrwtNo5()==c){
                        if(lottoItemsArray.get(a+1).getDrwtNo5() ==b){
                            fifthNumToNum.put(c,plusPickNum(fifthNumToNum.get(c),b));
                        }
                    }
                    if(lottoItemsArray.get(a).getDrwtNo6()==c){
                        if(lottoItemsArray.get(a+1).getDrwtNo6() ==b){
                            sixthNumToNum.put(c,plusPickNum(sixthNumToNum.get(c),b));
                        }
                    }
                }
            }
        }
        totalNumToNum.put(1,firstNumToNum);
        totalNumToNum.put(2,secondNumToNum);
        totalNumToNum.put(3,thirdNumToNum);
        totalNumToNum.put(4,fourNumToNum);
        totalNumToNum.put(5,fifthNumToNum);
        totalNumToNum.put(6,sixthNumToNum);
        totalPickLottoNum();

        setExpectDBCreateAndInsert();

    }

    private HashMap<Integer,Integer> plusPickNum(HashMap<Integer,Integer> hashNum ,int b){
        HashMap<Integer,Integer> hasNum =hashNum;
        if(hasNum ==null){
            hasNum=new HashMap<Integer,Integer>();
        }
        Integer selectNum=hasNum.get(b);
        if(selectNum !=null){
            selectNum +=1;
        }else{
            selectNum = 1;
        }
        hasNum.put(b,selectNum);

        return hasNum;
    }


    //번호 뽑기
    public void pickUpNum(){

        LottoResult newestNum = lottoItemsArray.get(0);
        Log.d("도원","round : "+newestNum.getDrwNo()+" / 1 : "+newestNum.getDrwtNo1()+" / 2 : "+newestNum.getDrwtNo2()+"/ 3 : "+newestNum.getDrwtNo3()
        +"/ 4 : "+newestNum.getDrwtNo4()+"/ 5 : "+newestNum.getDrwtNo5()+"/ 6 : "+newestNum.getDrwtNo6());
        winnerPrize=newestNum.getFirstWinamnt();
        itemListOfPosition(newestNum.getDrwtNo1(),1);
        itemListOfPosition(newestNum.getDrwtNo2(),2);
        itemListOfPosition(newestNum.getDrwtNo3(),3);
        itemListOfPosition(newestNum.getDrwtNo4(),4);
        itemListOfPosition(newestNum.getDrwtNo5(),5);
        itemListOfPosition(newestNum.getDrwtNo6(),6);
        expectNumList.clear();
        for(int a=1;a<6;a++){
            pickItems(lastPickNumber,a);
        }

        Cursor pickCursor=expectDBHelper.sortColumn(selectTblName,"lottoRound");
        while (pickCursor.moveToNext()){
            String temp=pickCursor.getInt(1)+"&"+pickCursor.getInt(2)+"&"+pickCursor.getInt(3)+"&"+pickCursor.getInt(4)+"&"+
                    pickCursor.getInt(5)+"&"+pickCursor.getInt(6)+"&"+pickCursor.getInt(7);
            expectNumList.add(temp);
        }
        livePickString.setValue(expectNumList);
    }

    private void itemListOfPosition(Integer drtNo, Integer position){
        int totalsize=0;
        ArrayList<Integer> pickNumList = new ArrayList<>();
        HashMap<Integer, HashMap<Integer, Integer>> tempHash =totalNumToNum.get(position);
        if(tempHash.containsKey(drtNo)){
            for (Integer a : tempHash.get(drtNo).keySet()){
                totalsize+=tempHash.get(drtNo).get(a);
            }
            for (Integer a : tempHash.get(drtNo).keySet()){
                if(tempHash.get(drtNo).get(a)/(float)totalsize>0.05){
                    pickNumList.add(a);
                }
            }
        }else{
            for (int a=1 ; a< 9;a++){
                pickNumList.add((position*position)+a);
            }
        }
        lastPickNumber.put(position,pickNumList);
    }

    private void pickItems(HashMap<Integer,ArrayList<Integer>> itemMap,Integer position){
        String pickStr="";
        pickString.clear();
        int tempNum1=0;
        ArrayList<Integer> tempNumbs= new ArrayList<>();
        boolean checkDuplicate =true;
        for(int a=1;a<7;a++){
            if(a % 2 == 0){
                if(itemMap.get(a).size()>0){
                    checkDuplicate=true;
                    Collections.shuffle(itemMap.get(a));
                    tempNum1=itemMap.get(a).get(0);
                    while(checkDuplicate){
                        checkDuplicate=false;
                        for(int tempN : tempNumbs){
                            if(tempN==tempNum1){
                                Collections.shuffle(itemMap.get(a));
                                tempNum1=itemMap.get(a).get(0);
                                checkDuplicate=true;
                            }
                        }
                    }

                    tempNumbs.add(tempNum1);
                    pickString.add(tempNum1);
                }
            }else{
                if(itemMap.get(a).size()>0) {
                    checkDuplicate=true;
                    Collections.shuffle(itemMap.get(a));
                    tempNum1=itemMap.get(a).get(0);
                    while(checkDuplicate){
                        checkDuplicate=false;
                        for(int tempN : tempNumbs){
                            if(tempN ==tempNum1){
                                Collections.shuffle(itemMap.get(a));
                                tempNum1=itemMap.get(a).get(0);
                                checkDuplicate=true;
                            }
                        }
                    }
                    tempNumbs.add(tempNum1);
                    pickString.add(tempNum1);
                }
            }
        }

        pickString.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        for(Integer element : pickString){
            pickStr+=element+"&";
        }
        String[] temp=pickStr.split("&");
        expectDBHelper.create(selectTblName);
        expectDBHelper.insertColumn(selectTblName,selectRound,temp[0],temp[1],temp[2],temp[3],temp[4],temp[5],String.valueOf(winnerPrize));


    }

    public void deleteExpectItem(){
        expectDBHelper.dropTable(selectTblName);
        expectNumList.clear();
        livePickString.setValue(expectNumList);

    }

    //셋팅값에 따른 픽당 선택 번호
    /**
     * 다음 회차 번호 예측하는 메소드
     */

    private HashMap<Integer,Integer> pickCountNum=new HashMap<>();
    public void totalPickLottoNum(){
        for(HashMap<Integer,HashMap<Integer,Integer>> numToNum : totalNumToNum.values()){
            for(HashMap<Integer,Integer> numCount : numToNum.values()){
                for(Integer numKey : numCount.keySet()){
                    Integer temp =pickCountNum.get(numKey);
                    if(temp==null){
                        temp=numCount.get(numKey);
                    }else{
                        temp+=numCount.get(numKey);
                    }
                    pickCountNum.put(numKey,temp);
                }
            }
        }

        minPickLottoNum(13);
        maxPickLottoNum(13);
    }

    //한동안 안나온 번호 픽 (limit 보다 적게나온
    private HashSet<Integer> pickMinNum =new HashSet<Integer>();
    private MutableLiveData<HashSet<Integer>> pickMinNumLiveData = new MutableLiveData<>();
    private void minPickLottoNum(int limit){
        pickMinNum.clear();
        for (int a=1;a<46;a++){
            pickMinNum.add(a);
        }

        int max = pickCountNum.values().stream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).orElse(-1);
        int min = pickCountNum.values().stream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        }).orElse(-1);
        for(Integer keys : pickCountNum.keySet()){
//            Log.d("도원","keys : "+keys + "|| pickCountNum.get(keys) : "+pickCountNum.get(keys) + "|| max : "+max
//            +"|| min : "+min+ " || (max)*30/45 : "+(max+min)/2);
            if(pickCountNum.get(keys) > (max+min)/2 ){
                pickMinNum.remove(keys);
            }
        }
        pickMinNumLiveData.setValue(pickMinNum);
    }

    //한동안 많이 나온 픽 (limit 보다 많은픽 ?)
    private HashSet<Integer> pickMaxNum = new HashSet<>();
    private MutableLiveData<HashSet<Integer>> pickMaxNumLiveData = new MutableLiveData<>();
    private void maxPickLottoNum(int limit){
        int max = pickCountNum.values().stream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).orElse(-1);
        int min = pickCountNum.values().stream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        }).orElse(-1);
        pickMaxNum.clear();
        for(Integer keys : pickCountNum.keySet()){
            if (max!=-1){
                if(pickCountNum.get(keys) > (max)*6/7){
                    pickMaxNum.add(keys);
                }
            }else{

            }
        }
        pickMaxNumLiveData.setValue(pickMaxNum);
    }


    //getLiveData
    public MutableLiveData<ArrayList<String>> getLivePickString() {
        return livePickString;
    }

    public MutableLiveData<HashSet<Integer>> getPickMaxNumLiveData() {
        return pickMaxNumLiveData;
    }

    public MutableLiveData<HashSet<Integer>> getPickMinNumLiveData() {
        return pickMinNumLiveData;
    }

    public MutableLiveData<String> getDownRound() {
        return downRound;
    }
}
