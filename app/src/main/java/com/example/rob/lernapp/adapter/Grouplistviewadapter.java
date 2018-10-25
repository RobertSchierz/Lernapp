import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rob.lernapp.R;
import com.example.rob.lernapp.restdataGet.Member;

import java.util.ArrayList;

public class Grouplistviewadapter extends BaseAdapter {


    public Grouplistviewadapter(ArrayList<Member> listContent, Context context) {
        this.listContent = listContent;
        this.layoutInflater = layoutInflater.from(context);
    }

    private ArrayList<Member> listContent;
    private LayoutInflater layoutInflater;


    @Override
    public int getCount() {
        return this.listContent.size();
    }

    @Override
    public Object getItem(int i) {
        return this.listContent.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.group_memberlist_item, null);

            holder = new ViewHolder();

            holder.groupmembername = (TextView) view.findViewById(R.id.groupmember_name);
            holder.groupmembername = (TextView) view.findViewById(R.id.groupmember_role);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        holder.groupmembername.setText(this.listContent.get(i).getMember().getName());
        holder.groupmemberrole.setText(this.listContent.get(i).getRole());

        return view;

    }

    static class ViewHolder {
        TextView groupmembername;
        TextView groupmemberrole;

    }
}
