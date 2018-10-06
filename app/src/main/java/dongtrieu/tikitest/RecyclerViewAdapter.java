package dongtrieu.tikitest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

/**
 * Created by Ho Dong Trieu on 10/06/2018
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<ViewModel> viewModelList;
    private HotKeywordOnclick listener;

    public RecyclerViewAdapter(Context mContext, List<ViewModel> viewModelList, HotKeywordOnclick listener) {
        this.mContext = mContext;
        this.viewModelList = viewModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recyclerview,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ViewModel viewModel = viewModelList.get(i);
        String[] s = viewModel.getName().split("\\s+");
        if(s.length == 1) {
            myViewHolder.keyword.setText(viewModel.getName());
        } else if(s.length == 2) {
            myViewHolder.keyword.setText(s[0] + "\n" + s[1]);
        } else {
            Random rand = new Random();
            int n = rand.nextInt(2);
            if(n == 1) {
                StringBuilder temp = new StringBuilder();
                for (int j = 0; j < s.length; j++) {
                    if ((j + 1) == s.length / 2) {
                        temp.append(s[j]).append("\n");
                    } else {
                        if (j < s.length - 1) {
                            temp.append(s[j]).append(" ");
                        } else {
                            temp.append(s[j]);
                        }
                    }
                }
                myViewHolder.keyword.setText(temp.toString());
            } else {
                StringBuilder temp = new StringBuilder();
                for (int j = 0; j < s.length; j++) {
                    if ((j) == s.length / 2) {
                        temp.append(s[j]).append("\n");
                    } else {
                        if (j < s.length - 1) {
                            temp.append(s[j]).append(" ");
                        } else {
                            temp.append(s[j]);
                        }
                    }
                }
                myViewHolder.keyword.setText(temp.toString());
            }

        }

        myViewHolder.cardView.setCardBackgroundColor(viewModel.getColor());
    }

    @Override
    public int getItemCount() {
        return viewModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView keyword;
        CardView cardView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            keyword = itemView.findViewById(R.id.keyword);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.ClickPosition(viewModelList.get(getAdapterPosition()));
        }
    }

    public interface HotKeywordOnclick {
        void ClickPosition(ViewModel viewModel);
    }
}
