package pattem.mvvmpattern.lottopick.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import pattem.mvvmpattern.lottopick.MainActivity;
import pattem.mvvmpattern.lottopick.R;
import pattem.mvvmpattern.lottopick.adapter.ExpectLottoAdapter;
import pattem.mvvmpattern.lottopick.viewmodel.MainFragmentViewModel;

public class MainFragment extends Fragment {
    private LinearLayout llBoxContainer;
    private RecyclerView rcExpectNum;
    private TextView tvExpectRound;
    private ImageView ivToolbarIcon;
    private LinearLayout loadingFram;

    private ExpectLottoAdapter expectLottoAdapter=new ExpectLottoAdapter();

    private Button btnPlusLotto,btnDeleteLotto;

    MainFragmentViewModel mainFragmentViewModel;

    private RewardedInterstitialAd mRewardedAd;
    public MainFragment() {
        // Required empty public constructor

    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainFragmentViewModel=new ViewModelProvider(this).get(MainFragmentViewModel.class);
        mainFragmentViewModel.setLifeCycle(this);
        bindData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main, container, false);
        llBoxContainer=(LinearLayout) view.findViewById(R.id.llBoxContainer);
        loadingFram =(LinearLayout) view.findViewById(R.id.loadingFram);
        btnPlusLotto=(Button)view.findViewById(R.id.btnPlusLotto);
        btnDeleteLotto=(Button)view.findViewById(R.id.btnDeleteLotto);
        tvExpectRound=(TextView)view.findViewById(R.id.tvExpectRound);
        rcExpectNum=(RecyclerView)view.findViewById(R.id.rcExpectNum);
        ivToolbarIcon=(ImageView)view.findViewById(R.id.ivToolbarIcon);

        rcExpectNum.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rcExpectNum.setAdapter(expectLottoAdapter);
        mainFragmentViewModel.openDB(view.getContext());

        mainFragmentViewModel.getLottoList();

        MobileAds.initialize(view.getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.d("도원ad","onInitializationComplete : ");
            }
        });
        loadNewAd(view);

        ivToolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        btnPlusLotto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("도원ad","mRewardedAd : "+mRewardedAd);
                if (expectLottoAdapter.getItemCount()<5){
                    mainFragmentViewModel.pickUpNum();
                }else if (expectLottoAdapter.getItemCount()<=14){
                    if (mRewardedAd!=null){
                        mRewardedAd.show((Activity) view.getContext(), new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                int rewardAmount=rewardItem.getAmount();
                                String rewardType=rewardItem.getType();
                                if (rewardAmount>0){
                                    mainFragmentViewModel.pickUpNum();
                                }
                                Log.d("도원ad","rewardAmount : "+rewardAmount +"  || rewardType : "+rewardType);
                            }
                        });
                    }else {
                        mainFragmentViewModel.pickUpNum();
                    }
                }else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setTitle("로또 픽 제한");
                    builder.setMessage("로또 픽 예상 번호는 최대 15개로 제한 됩니다.\n 번호 삭제 후 재발급 부탁 드리겠습니다.");
                    builder.setPositiveButton("확인",null);
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }
//                mainFragmentViewModel.pickUpNum();
            }
        });
        btnDeleteLotto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragmentViewModel.deleteExpectItem();
            }
        });


        return view;
    }

    public void loadNewAd(View view){
        AdRequest adRequest=new AdRequest.Builder().build();
        //ca-app-pub-3940256099942544/5224354917 테스트 번호
        //ca-app-pub-5579046201163636/9221190456 광고 번호
        RewardedInterstitialAd.load(getContext(), "ca-app-pub-5579046201163636/9221190456", adRequest, new RewardedInterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                super.onAdLoaded(rewardedInterstitialAd);
                Log.d("도원ad","onAdLoaded :");
                mRewardedAd=rewardedInterstitialAd;
                admobSetting(view);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mRewardedAd=null;
            }
        });

    }

    public void admobSetting(View view){
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                Log.d("도원ad","onAdFailedToShowFullScreenContent :"+adError.getMessage());
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                Log.d("도원ad","onAdShowedFullScreenContent :");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                loadNewAd(view);
                Log.d("도원ad","onAdDismissedFullScreenContent :");
            }
        });
    }

    public void bindData(){
        mainFragmentViewModel.getLivePickString().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                if (strings.size()>0){
                    tvExpectRound.setText(strings.get(0).split("&")[0]+" 회차 예상 번호");
                    expectLottoAdapter.setExpectItem(strings);
                }else{
                    expectLottoAdapter.setExpectItem(strings);
                }
            }
        });

        mainFragmentViewModel.getPickMinNumLiveData().observe(this, new Observer<HashSet<Integer>>() {
            @Override
            public void onChanged(HashSet<Integer> integers) {
                LayoutInflater layoutInflater=getLayoutInflater();
                View itemView=layoutInflater.inflate(R.layout.picknum_layout,null);

                if (integers.size()>0){
                    LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params1.topMargin=5;
                    params1.rightMargin=10;
                    params1.leftMargin=10;
                    Iterator iterator=integers.iterator();
                    ArrayList<Integer> lottoList=new ArrayList<>();
                    ArrayList<Integer> tempNumber=new ArrayList<>();
                    ArrayList<Integer> pickNumber=new ArrayList<>();
                    while (iterator.hasNext()){
                        lottoList.add((int)iterator.next());
                    }
                    while(true) {
                        int a = (int) (Math.random() * lottoList.size()) + 1;
                        boolean checkDuplicate = true;
                        while (checkDuplicate) {
                            checkDuplicate = false;
                            for (int b = 0; b < tempNumber.size(); b++) {
                                if (a == tempNumber.get(b)) {
                                    a = (int) (Math.random() * lottoList.size()) + 1;
                                    checkDuplicate = true;
                                }
                            }
                        }
                        tempNumber.add(a);
                        pickNumber.add(lottoList.get(a-1));
                        if (pickNumber.size()==6 ){
                            break;
                        }else if (pickNumber.size()==lottoList.size()){
                            break;
                        }
                    }
                    for (int b=0;b<pickNumber.size();b++){
                        View itemView2=layoutInflater.inflate(R.layout.item_lottoball,null);
                        ImageView ivBall=(ImageView)itemView2.findViewById(R.id.ivBall);
                        LinearLayout llPickItem=(LinearLayout)itemView.findViewById(R.id.llPickItem);
                        TextView tvItemTitle=(TextView) itemView.findViewById(R.id.tvItemTitle);
                        tvItemTitle.setText("최근 많이 나오지 않은 번호");
                        int tempResource = getContext().getResources().getIdentifier("ball"+pickNumber.get(b),"drawable",getContext().getPackageName());
                        Bitmap ballBitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(),tempResource),
                                30,30,false);
                        ivBall.setImageBitmap(ballBitmap);
                        llPickItem.addView(itemView2,params1);
                    }

                    LinearLayout.LayoutParams params2=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params2.topMargin=40;
                    params2.rightMargin=40;
                    params2.leftMargin=40;
                    llBoxContainer.addView(itemView,params2);
                }

            }
        });

        mainFragmentViewModel.getPickMaxNumLiveData().observe(this, new Observer<HashSet<Integer>>() {
            @Override
            public void onChanged(HashSet<Integer> integers) {
                LayoutInflater layoutInflater=getLayoutInflater();
                View itemView=layoutInflater.inflate(R.layout.picknum_layout,null);

                if (integers.size()>0){
                    LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params1.topMargin=5;
                    params1.rightMargin=10;
                    params1.leftMargin=10;
                    params1.bottomMargin=20;
                    Iterator iterator=integers.iterator();
                    ArrayList<Integer> lottoList=new ArrayList<>();
                    ArrayList<Integer> tempNumber=new ArrayList<>();
                    ArrayList<Integer> pickNumber=new ArrayList<>();
                    while (iterator.hasNext()){
                        lottoList.add((int)iterator.next());
                    }
                    while(true) {
                        int a = (int) (Math.random() * lottoList.size()) + 1;
                        boolean checkDuplicate = true;
                        while (checkDuplicate) {
                            checkDuplicate = false;
                            for (int b = 0; b < tempNumber.size(); b++) {
                                if (a == tempNumber.get(b)) {
                                    a = (int) (Math.random() * lottoList.size()) + 1;
                                    checkDuplicate = true;
                                }
                            }
                        }
                        tempNumber.add(a);
                        pickNumber.add(lottoList.get(a-1));
                        if (pickNumber.size()==6 ){
                            break;
                        }else if (pickNumber.size()==lottoList.size()){
                            break;
                        }
                    }
                    for (int b=0;b<pickNumber.size();b++){
                        View itemView2=layoutInflater.inflate(R.layout.item_lottoball,null);
                        ImageView ivBall=(ImageView)itemView2.findViewById(R.id.ivBall);
                        LinearLayout llPickItem=(LinearLayout)itemView.findViewById(R.id.llPickItem);
                        TextView tvItemTitle=(TextView) itemView.findViewById(R.id.tvItemTitle);
                        tvItemTitle.setText("최근 많이 나온 번호");
                        int tempResource = getContext().getResources().getIdentifier("ball"+pickNumber.get(b),"drawable",getContext().getPackageName());
                        Bitmap ballBitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(),tempResource),
                                30,30,false);
                        ivBall.setImageBitmap(ballBitmap);
                        llPickItem.addView(itemView2,params1);
                    }

                    LinearLayout.LayoutParams params2=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params2.topMargin=40;
                    params2.rightMargin=40;
                    params2.leftMargin=40;
                    llBoxContainer.addView(itemView,params2);
                }
                ((MainActivity)MainActivity.mContext).hideLoading();
            }
        });

        mainFragmentViewModel.getDownRound().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ((MainActivity)MainActivity.mContext).setTextCurrentRound(s);
            }
        });

    }

    public void showDialog(){
        Dialog dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.image_link_text);
        TextView image1=dialog.findViewById(R.id.image1);
        TextView image2=dialog.findViewById(R.id.image2);
        TextView image3=dialog.findViewById(R.id.image3);
        TextView exit=dialog.findViewById(R.id.exit);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/authors/freepik"));
                startActivity(intent);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/authors/freepik"));
                startActivity(intent);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/authors/freepik"));
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }




}