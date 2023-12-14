package pattem.mvvmpattern.lottopick;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import pattem.mvvmpattern.lottopick.fragments.CheckMyLottoFragment;
import pattem.mvvmpattern.lottopick.fragments.MainFragment;
import pattem.mvvmpattern.lottopick.utill.QrReaderActivity;
import pattem.mvvmpattern.lottopick.utill.SharedPre;

public class MainActivity extends AppCompatActivity {

    private FrameLayout FLMain;
    private MainFragment mainFragment=new MainFragment();;
    private CheckMyLottoFragment checkMyLottoFragment=new CheckMyLottoFragment();
    private Button btnQr,btnMain,btnCheck;
    private FrameLayout flLoading;
    private ConstraintLayout clBottomNav;
    private final int MAINPAGE=0;
    private final int QRPAGE=1;
    private final int MYCHECKPAGE=2;
    private int currentPage=MAINPAGE;

    private TextView tvRound;

    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        SharedPre.init(getApplicationContext());
        mContext=MainActivity.this;
        clBottomNav.setVisibility(View.GONE);
        flLoading.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.FLMain,mainFragment).commit();
        currentPage=MAINPAGE;
        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setCaptureActivity(QrReaderActivity.class);
                intentIntegrator.initiateScan();
            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage==MAINPAGE) return;
                getSupportFragmentManager().beginTransaction().replace(R.id.FLMain,mainFragment).commit();
                currentPage=MAINPAGE;
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage==MYCHECKPAGE) return;
                getSupportFragmentManager().beginTransaction().replace(R.id.FLMain,checkMyLottoFragment).commit();
                currentPage=MYCHECKPAGE;
            }
        });


    }


    public void initView(){
        FLMain=(FrameLayout) findViewById(R.id.FLMain);
        btnQr=(Button) findViewById(R.id.btnQr);
        btnMain=(Button) findViewById(R.id.btnMain);
        btnCheck=(Button) findViewById(R.id.btnCheck);
        flLoading=(FrameLayout) findViewById(R.id.flLoading);
        clBottomNav=(ConstraintLayout) findViewById(R.id.clBottomNav);

        tvRound=(TextView) findViewById(R.id.tvRound);

    }



    public void hideLoading(){
        clBottomNav.setVisibility(View.VISIBLE);
        flLoading.setVisibility(View.GONE);
    }

    public void setTextCurrentRound(String round){
        tvRound.setText(round + "회차 받아오는 중");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if (result!=null){
                if (result.getContents() ==null){

                }else{
                    try {
                        if (result.getContents().equals("cancel"))return;
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("로또 추첨 확인");
                        builder.setMessage(result.getContents().toString());
                        builder.setPositiveButton("이동", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents().toString()));
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();
                        Log.d("도원",result.getContents()+"");
                    }catch (Exception e){

                    }
                }
            }
        }
    }
}