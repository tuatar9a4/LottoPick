package pattem.mvvmpattern.lottopick.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import pattem.mvvmpattern.lottopick.R;
import pattem.mvvmpattern.lottopick.database.ExpectDBHelper;
import pattem.mvvmpattern.lottopick.database.LottoDbHelper;
import pattem.mvvmpattern.lottopick.viewmodel.CheckMyLottoFragmentViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckMyLottoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckMyLottoFragment extends Fragment {

    private CheckMyLottoFragmentViewModel checkMyLottoFragmentViewModel;
    private ScrollView scrollList;
    private LinearLayout listContainer;
    private TextView tvOneWinner,tvTwoWinner,tvThreeWinner,tvForthWinner,tvFifthWinner;
    private int one=0,two=0,three=0,four=0,five=0;

    public CheckMyLottoFragment() {
        // Required empty public constructor
    }

    public static CheckMyLottoFragment newInstance() {
        CheckMyLottoFragment fragment = new CheckMyLottoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkMyLottoFragmentViewModel=new ViewModelProvider(this).get(CheckMyLottoFragmentViewModel.class);
        checkMyLottoFragmentViewModel.setLifecycleOwner(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_check_my_lotto,container,false);
        initView(view);
        checkMyLottoFragmentViewModel.openDb(view.getContext());
        bindData(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkMyLottoFragmentViewModel.getExpectList(view.getContext());
            }
        },100);
        return view;
    }
    public void initView(View view){
        tvOneWinner=view.findViewById(R.id.tvOneWinner);
        tvTwoWinner=view.findViewById(R.id.tvTwoWinner);
        tvThreeWinner=view.findViewById(R.id.tvThreeWinner);
        tvForthWinner=view.findViewById(R.id.tvForthWinner);
        tvFifthWinner=view.findViewById(R.id.tvFifthWinner);
        scrollList=view.findViewById(R.id.scrollList);
        listContainer=view.findViewById(R.id.listContainer);
    }



    public void bindData(LifecycleOwner lifecycleOwner){
        checkMyLottoFragmentViewModel.getCountingLiveData().observe(lifecycleOwner, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                for (int a= 0; a<strings.size(); a++){
                    switch (strings.get(a)){
                        case "4":
                            five++;
                            break;
                        case "5":
                            four++;
                            break;
                        case "6":
                            three++;
                            break;
                        case "7":
                            two++;
                            break;
                        case "8":
                            one++;
                            break;

                    }
                }
                tvFifthWinner.setText(five+" 번");
                tvForthWinner.setText(four+" 번");
                tvThreeWinner.setText(three+" 번");
                tvThreeWinner.setText(two+" 번");
                tvOneWinner.setText(one+" 번");

            }
        });

        checkMyLottoFragmentViewModel.getExpectView().observe(lifecycleOwner, new Observer<View>() {
            @Override
            public void onChanged(View view) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.bottomMargin=40;
                view.setLayoutParams(params);
                listContainer.addView(view);
            }
        });

    }
}