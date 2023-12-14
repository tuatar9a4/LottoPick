package pattem.mvvmpattern.lottopick.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pattem.mvvmpattern.lottopick.R;

public class ExpectLottoAdapter extends RecyclerView.Adapter<ExpectLottoAdapter.ExpectLottoVH> {

    private LayoutInflater inflater;
    private ArrayList<String> expectItem=new ArrayList<>();
    private Context context;
    @NonNull
    @Override
    public ExpectLottoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.item_expect_lotto,parent,false);
        return new ExpectLottoVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpectLottoVH holder, int position) {
        holder.tvCntExpectNum.setText((position+1)+"");
        String[] tempStr=expectItem.get(position).toString().split("&");
        int temp = context.getResources().getIdentifier("ball"+tempStr[1],"drawable",context.getPackageName());
        Bitmap ballBitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),temp),
                40,40,false);
        holder.ivExpectNum1.setImageBitmap(ballBitmap);
        temp = context.getResources().getIdentifier("ball"+tempStr[2],"drawable",context.getPackageName());
        ballBitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),temp),
                40,40,false);
        holder.ivExpectNum2.setImageBitmap(ballBitmap);
        temp = context.getResources().getIdentifier("ball"+tempStr[3],"drawable",context.getPackageName());
        ballBitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),temp),
                40,40,false);
        holder.ivExpectNum3.setImageBitmap(ballBitmap);
        temp = context.getResources().getIdentifier("ball"+tempStr[4],"drawable",context.getPackageName());
        ballBitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),temp),
                40,40,false);
        holder.ivExpectNum4.setImageBitmap(ballBitmap);
        temp = context.getResources().getIdentifier("ball"+tempStr[5],"drawable",context.getPackageName());
        ballBitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),temp),
                40,40,false);
        holder.ivExpectNum5.setImageBitmap(ballBitmap);
        temp = context.getResources().getIdentifier("ball"+tempStr[6],"drawable",context.getPackageName());
        ballBitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),temp),
                40,40,false);
        holder.ivExpectNum6.setImageBitmap(ballBitmap);


    }

    @Override
    public int getItemCount() {
        return expectItem.size();
    }

    public void setExpectItem(ArrayList<String> expectItem){
        this.expectItem=expectItem;
        notifyDataSetChanged();
    }

    public class ExpectLottoVH extends RecyclerView.ViewHolder{
        TextView tvCntExpectNum;
        ImageView ivExpectNum1,ivExpectNum2,ivExpectNum3,ivExpectNum4,ivExpectNum5,ivExpectNum6;

        public ExpectLottoVH(@NonNull View itemView) {
            super(itemView);
            tvCntExpectNum=(TextView) itemView.findViewById(R.id.tvCntExpectNum);
            ivExpectNum1=(ImageView) itemView.findViewById(R.id.ivExpectNum1);
            ivExpectNum2=(ImageView) itemView.findViewById(R.id.ivExpectNum2);
            ivExpectNum3=(ImageView) itemView.findViewById(R.id.ivExpectNum3);
            ivExpectNum4=(ImageView) itemView.findViewById(R.id.ivExpectNum4);
            ivExpectNum5=(ImageView) itemView.findViewById(R.id.ivExpectNum5);
            ivExpectNum6=(ImageView) itemView.findViewById(R.id.ivExpectNum6);

        }
    }

}
