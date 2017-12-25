package inc.droidflick.firebasetutorial.Recycle_view;

import android.view.View;

/**
 * Created by enchor25 on 2/24/2017.
 */

public interface RecyclerViewItemClickListener {
    public void onClick(View view, int position);

    public void onLongClick(View view, int position);
}