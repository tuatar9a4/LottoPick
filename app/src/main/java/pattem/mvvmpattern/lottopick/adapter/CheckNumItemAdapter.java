package pattem.mvvmpattern.lottopick.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pattem.mvvmpattern.lottopick.R;
import pattem.mvvmpattern.lottopick.Retrofit.LottoResult;
import pattem.mvvmpattern.lottopick.adapter_item.LottoCheckResult;

public class CheckNumItemAdapter extends RecyclerView.Adapter<CheckNumItemAdapter.CheckNumItemVH> {

    private Context context;
    private ArrayList<LottoCheckResult> lottoResults=new ArrayList<>();


    public CheckNumItemAdapter(Context context){
        this.context=context;

    }

    @NonNull
    @Override
    public CheckNumItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= layoutInflater.inflate(R.layout.item_checkpage_expectnum,parent,false);
        return new CheckNumItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckNumItemVH holder, int position) {
        if (lottoResults.get(position).getResult().equals("0")){
            holder.no1.setText(lottoResults.get(position).getNo1().split("&")[1]);
            holder.no2.setText(lottoResults.get(position).getNo2().split("&")[1]);
            holder.no3.setText(lottoResults.get(position).getNo3().split("&")[1]);
            holder.no4.setText(lottoResults.get(position).getNo4().split("&")[1]);
            holder.no5.setText(lottoResults.get(position).getNo5().split("&")[1]);
            holder.no6.setText(lottoResults.get(position).getNo6().split("&")[1]);
        }else{
            if (lottoResults.get(position).getNo1().split("&")[0].equals("0")){
                holder.no1.setText(lottoResults.get(position).getNo1().split("&")[1]);
                holder.no1.setPaintFlags(holder.no1.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                holder.no1.setTextColor(Color.parseColor("#93949a"));
                holder.no1.setBackground(null);
            }else{
                holder.no1.setText(lottoResults.get(position).getNo1().split("&")[1]);
                holder.no1.setPaintFlags(0);
                holder.no1.setTextColor(context.getColor(R.color.checkCorrectColor));
                holder.no1.setBackground(context.getDrawable(R.drawable.correct_num_round));
            }

            if (lottoResults.get(position).getNo2().split("&")[0].equals("0")){
                holder.no2.setText(lottoResults.get(position).getNo2().split("&")[1]);
                holder.no2.setPaintFlags(holder.no2.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                holder.no2.setTextColor(Color.parseColor("#93949a"));
                holder.no2.setBackground(null);
            }else{
                holder.no2.setText(lottoResults.get(position).getNo2().split("&")[1]);
                holder.no2.setPaintFlags(0);
                holder.no2.setTextColor(context.getColor(R.color.checkCorrectColor));
                holder.no2.setBackground(context.getDrawable(R.drawable.correct_num_round));
            }

            if (lottoResults.get(position).getNo3().split("&")[0].equals("0")){
                holder.no3.setText(lottoResults.get(position).getNo3().split("&")[1]);
                holder.no3.setPaintFlags(holder.no3.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                holder.no3.setTextColor(Color.parseColor("#93949a"));
                holder.no3.setBackground(null);
            }else{
                holder.no3.setText(lottoResults.get(position).getNo3().split("&")[1]);
                holder.no3.setPaintFlags(0);
                holder.no3.setTextColor(context.getColor(R.color.checkCorrectColor));
                holder.no3.setBackground(context.getDrawable(R.drawable.correct_num_round));
            }

            if (lottoResults.get(position).getNo4().split("&")[0].equals("0")){
                holder.no4.setText(lottoResults.get(position).getNo4().split("&")[1]);
                holder.no4.setPaintFlags(holder.no4.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                holder.no4.setTextColor(Color.parseColor("#93949a"));
                holder.no4.setBackground(null);
            }else{
                holder.no4.setText(lottoResults.get(position).getNo4().split("&")[1]);
                holder.no4.setPaintFlags(0);
                holder.no4.setTextColor(context.getColor(R.color.checkCorrectColor));
                holder.no4.setBackground(context.getDrawable(R.drawable.correct_num_round));
            }

            if (lottoResults.get(position).getNo5().split("&")[0].equals("0")){
                holder.no5.setText(lottoResults.get(position).getNo5().split("&")[1]);
                holder.no5.setPaintFlags(holder.no5.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                holder.no5.setTextColor(Color.parseColor("#93949a"));
                holder.no5.setBackground(null);
            }else{
                holder.no5.setText(lottoResults.get(position).getNo5().split("&")[1]);
                holder.no5.setPaintFlags(0);
                holder.no5.setTextColor(context.getColor(R.color.checkCorrectColor));
                holder.no5.setBackground(context.getDrawable(R.drawable.correct_num_round));
            }

            if (lottoResults.get(position).getNo6().split("&")[0].equals("0")){
                holder.no6.setText(lottoResults.get(position).getNo6().split("&")[1]);
                holder.no6.setPaintFlags(holder.no6.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                holder.no6.setTextColor(Color.parseColor("#93949a"));
                holder.no6.setBackground(null);
            }else{
                holder.no6.setText(lottoResults.get(position).getNo6().split("&")[1]);
                holder.no6.setPaintFlags(0);
                holder.no6.setTextColor(context.getColor(R.color.checkCorrectColor));
                holder.no6.setBackground(context.getDrawable(R.drawable.correct_num_round));
            }
        }

        holder.tvCheckResult.setText(resultToString(lottoResults.get(position).getResult()));
    }

    @Override
    public int getItemCount() {
        return lottoResults.size();
    }

    public void setLottoResults(ArrayList<LottoCheckResult> lottoResults){
        this.lottoResults=deepClone(lottoResults);
        notifyDataSetChanged();
    }

    public ArrayList<LottoCheckResult> deepClone(ArrayList<LottoCheckResult> original){
        LottoCheckResult temp=new LottoCheckResult();
        ArrayList<LottoCheckResult> cloneList=new ArrayList<>();
        for (int a=0;a<original.size();a++){
            temp=original.get(a);
            cloneList.add(temp);
        }
        return cloneList;
    }

    public String resultToString(String result){
//        Log.d("도원","?? : "+result);
        switch (result){
            case "0":
                return "미추첨";
            case "1":
                return "낙첨";
            case "2":
                return "낙첨";
            case "3":
                return "낙첨";
            case "4":
                return "5등";
            case "5":
                return "4등";
            case "6":
                return "3등";
            case "7":
                return "1등";
            case "8":
                return "2등";
            default:
                return "미추첨";

        }

    }

    public class CheckNumItemVH extends RecyclerView.ViewHolder{
        TextView no1,no2,no3,no4,no5,no6;
        TextView tvCheckResult;
        public CheckNumItemVH(@NonNull View itemView) {
            super(itemView);
            no1=(TextView) itemView.findViewById(R.id.no1);
            no2=(TextView) itemView.findViewById(R.id.no2);
            no3=(TextView) itemView.findViewById(R.id.no3);
            no4=(TextView) itemView.findViewById(R.id.no4);
            no5=(TextView) itemView.findViewById(R.id.no5);
            no6=(TextView) itemView.findViewById(R.id.no6);
            tvCheckResult=(TextView) itemView.findViewById(R.id.tvCheckResult);

        }
    }
}
