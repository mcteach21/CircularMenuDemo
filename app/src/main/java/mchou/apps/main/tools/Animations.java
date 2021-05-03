package mchou.apps.main.tools;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;

import mchou.apps.main.R;

public class Animations {

	public static void AnimateActionBar(Context context, ActionBar actionbar, int animate_duration) {
		ColorDrawable begin = new ColorDrawable(context.getResources().getColor(R.color.black));
		ColorDrawable end =  new ColorDrawable(context.getResources().getColor(R.color.indianred));

		TransitionDrawable actionBarTransition = new TransitionDrawable(new Drawable[] {begin, end});

		actionbar.setBackgroundDrawable(actionBarTransition);
		actionBarTransition.startTransition(animate_duration);
	}
}
