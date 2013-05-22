package com.vito.xmutems;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.vito.xmutems.course.CourseFragment;
import com.vito.xmutems.exam.ExamFragment;
import com.vito.xmutems.fragment.notification.NotificationFragment;
import com.vito.xmutems.fragment.score.ScoreFragment;
import com.vito.xmutems.settings.SettingFragment;
import com.vito.xmutems.utils.CacheProvider;
import com.vito.xmutems.utils.Constant;

public class MainActivity extends SlidingFragmentActivity {
	private Fragment mContent;
	private CanvasTransformer mTransformer;
	private boolean isexit = false;   
	private boolean hastask = false;  
	Timer texit = new Timer(); 
	//自定义menu动画
	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}		
	};
	
	public MainActivity() {
		// see the class CustomAnimation for how to attach 
		// the CanvasTransformer to the SlidingMenu
		mTransformer = new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.translate(0, canvas.getHeight()*(1-interp.getInterpolation(percentOpen)));
			}
		};
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		setTitle(getString(R.string.notification));
        //使actionbar上的logo可以点击
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        	getSupportActionBar().setHomeButtonEnabled(true);
        	getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        }
        
        // set the Above View
 		if (savedInstanceState != null)
 			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
 		if (mContent == null) {
 			mContent = new NotificationFragment();	
 		}
 		
 		// set the Above View
 		setContentView(R.layout.content_frame);
 		getSupportFragmentManager()
 		.beginTransaction()
 		.replace(R.id.content_frame, mContent)
 		.commit();
 		
 		// set the Behind View
 		setBehindContentView(R.layout.menu_frame);
 		getSupportFragmentManager()
 		.beginTransaction()
 		.replace(R.id.menu_frame, new MenuFragment())
 		.commit();
 		
 		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		//设置menu动画
//		setSlidingActionBarEnabled(true);
//		sm.setBehindScrollScale(0.0f);
//		sm.setBehindCanvasTransformer(mTransformer);
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
    
    @Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
    TimerTask task = new TimerTask() {  
        public void run() {  
        isexit = false;  
        hastask = true;  
        }  
    };  
  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && !getSlidingMenu().isMenuShowing()){  
        	
            if(isexit == false){  
                isexit = true;  
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();  
                if(!hastask) {  
                    texit.schedule(task, 2000);  
                }  
            }else{  
            	CacheProvider.remove(getApplicationContext(), Constant.USER_COOKIE);
                finish();  
                System.exit(0);  
            }  
            return false;  
            }  
          
        return super.onKeyDown(keyCode, event);  
    }  
    
	/**
	 * 点击切换Fragment 
	 **/
	public void menuItemClick(View view) {
		Fragment newContent = null;
		switch(view.getId()) {
			case R.id.notification:
				setTitle(getString(R.string.notification));
				newContent = new NotificationFragment();
				break;
			case R.id.curriculum:
				setTitle(getString(R.string.curriculum));
				newContent = new CourseFragment();
				break;
			case R.id.score:
				setTitle(getString(R.string.score));
				newContent = new ScoreFragment();
				break;
			case R.id.exam:
				setTitle(getString(R.string.exam));
				newContent = new ExamFragment();
				break;
			case R.id.settings:
				setTitle(getString(R.string.settings));
				newContent = new SettingFragment();
				break;
			case R.id.feedback:
				sendFeedbackEmail();
				break;
		}
		if (newContent != null)
			switchContent(newContent);
	}

	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
	}
	
	public void sendFeedbackEmail() {
		Intent intent = new Intent(Intent.ACTION_SENDTO); 
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "意见反馈");
		intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n--" + CacheProvider.get(MainActivity.this, "stuName"));
		intent.setData(Uri.parse("mailto:zhangwei.noair@gmail.com"));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		try{
			startActivity(intent);
		} catch (Exception e){
			Toast.makeText(MainActivity.this, "没有找到您的邮箱账户", Toast.LENGTH_LONG).show();
		}
	}
}
