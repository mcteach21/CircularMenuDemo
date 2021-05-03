package mchou.apps.main.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;

import java.util.ArrayList;
import java.util.List;

import mchou.apps.main.R;

public class CircularMenuWidget extends View {
	private Context context;

	private boolean circular=true;
	private boolean withText=true;

	private int NbMenuItems = 6;
	private int angleEcart=360/NbMenuItems;
	private int[] colors;
	public boolean isLoading=true;
	public boolean isOpened=false;

	public float menuRadius=0.0f;
	public float menuMaxRadius=180.0f;
	public float menuMinRadius=120.0f;
	public float menuItemsRadius=0.0f;
	public float menuMaxItemsRadius=160.0f;
	
	public int xCenter=0;
	public int yCenter=0;

	private int currentMenuImage = R.drawable.ic_menu_suite;
	private String currentMenuText="Open";
	private float textSize = 24 * getResources().getDisplayMetrics().density;

	public int startAngle = 0;
	public int swepAngle = 0;

	private DrawAnimation menuAnimation, menuItemsAnimation ;
	private List<CircleMenuItem> menuItems= new ArrayList<CircleMenuItem>();

	OnItemMenuPressed callback;
	CircularMenuWidget thisWidget;
	public CircularMenuWidget(Context context, boolean circular, boolean withText, OnItemMenuPressed callback) {
		super(context);
		thisWidget=this;
		
		this.callback=callback;
		this.context=context;
		this.circular=circular;
		this.withText=withText;

		InitMenu();
	}
	private void InitMenu() {
		colors=new int[] {
				context.getResources().getColor(R.color.whitesmoke)
				, context.getResources().getColor(R.color.bisque)
				, context.getResources().getColor(R.color.maroon)
				, context.getResources().getColor(R.color.darkturquoise)
				, context.getResources().getColor(R.color.sandybrown)
				, context.getResources().getColor(R.color.indianred)
				, context.getResources().getColor(R.color.cadetblue)
		};

		menuAnimation = new DrawAnimation(this,true);
		menuAnimation.setDuration(1000);
		this.startAnimation(menuAnimation);

		menuItemsAnimation = new DrawAnimation(this,false);		
		menuItemsAnimation.setDuration(500);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawItems(canvas);
		drawMenu(canvas);
		drawBitmaps(canvas);
	}
	private void drawMenu(Canvas canvas) {
		int top, left,right,bottom;
		Paint menuPaint = getPaint(0);

		if(circular) {
			canvas.drawCircle(xCenter, yCenter , menuRadius , menuPaint);
		}else {
			
			if(!isOpened) {
				left = (getWidth()/2) - (int) menuRadius;
				right = (getWidth()/2) + (int) menuRadius;			
				top = getHeight()/2 - (int) menuRadius;
				bottom = getHeight()/2 + (int) menuRadius;
				
				Rect rect = new Rect(left-(int) menuRadius, top-(int) menuRadius, right+(int) menuRadius, bottom+(int) menuRadius);
				RectF menuBounds = new RectF(rect);
							
				startAngle=180;
				swepAngle=180;
				canvas.drawArc(menuBounds, startAngle, swepAngle, true, menuPaint);			
			}
		}

		if(withText) {
			canvas.drawText(currentMenuText,  xCenter - menuRadius/2 , yCenter , getTextPaint());
		}else {
			Bitmap imgBitmap = BitmapFactory.decodeResource(getResources(), currentMenuImage);
			canvas.drawBitmap(imgBitmap, xCenter - menuMaxRadius/2 , yCenter - menuMaxRadius/2  , new Paint(Paint.ANTI_ALIAS_FLAG));
		}
	}
	private void drawItems(Canvas canvas) {
		int top , left, right, bottom;	

		if(!isLoading) {

			left = (getWidth()/2) - (int) menuItemsRadius;
			right = (getWidth()/2) + (int) menuItemsRadius;
			top = getHeight()/2 - (int) menuItemsRadius;
			bottom = getHeight()/2 + (int) menuItemsRadius;

			Rect rect = new Rect(left-(int) menuItemsRadius, top-(int) menuItemsRadius, right+(int) menuItemsRadius, bottom+(int) menuItemsRadius);
			RectF bounds = new RectF(rect);

			int angle;

			if(menuItems.size()>0)
				menuItems.clear();

			for (int i = 1; i <= NbMenuItems; i++) {
				angle = startAngle+angleEcart*(i-1);
				canvas.drawArc(bounds, angle, swepAngle, true, getPaint(i));
				menuItems.add(new CircleMenuItem(i,angle,(angle+swepAngle)));
			}
		}
	}
	private void drawBitmaps(Canvas canvas) {
		if(isOpened) {
			Bitmap bitmap;
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStyle(Paint.Style.FILL);

			int x,y;
			float radius = 250.0f;
			double angleRadian;
			for (int i = 1; i <= NbMenuItems; i++) {
				bitmap = getBitmap(i-1);	

				angleRadian = Math.toRadians(((startAngle+angleEcart*(i-1))+swepAngle/2));

				x = (int)(getWidth()/2 + radius * Math.cos(angleRadian));
				y = (int)(getHeight()/2 + radius * Math.sin(angleRadian));

				canvas.drawBitmap(bitmap, (x-radius/3) + 50 , y - 50 , paint);
			}
		}
	}

	class DrawAnimation extends Animation {
		CircularMenuWidget drawing;
		boolean mainAnimation;

		public DrawAnimation(CircularMenuWidget drawing, boolean mainAnimation) {
			this.drawing=drawing;
			this.mainAnimation=mainAnimation;
		}
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			if(mainAnimation) {				
				float x = this.drawing.xCenter + ((getWidth()/2  - this.drawing.xCenter ) * interpolatedTime);
				float y = this.drawing.yCenter + ((getHeight()/2 - this.drawing.yCenter ) * interpolatedTime);
				this.drawing.UpdatePosition(x,y);

				float mradius = this.drawing.menuRadius + ((menuMaxRadius - this.drawing.menuRadius) * interpolatedTime);

				this.drawing.UpdateRadius(mradius);
			}else {
				float newRadius=this.drawing.isOpened?menuMaxItemsRadius:0;
				float oldRadius=this.drawing.isOpened?0:menuMaxItemsRadius;

				int oldSwepAngle = 10;
				int newSwepAngle = angleEcart;

				float radius = oldRadius + ((newRadius - oldRadius) * interpolatedTime);
				int swepAngle = (int) (oldSwepAngle + ((newSwepAngle - oldSwepAngle) * interpolatedTime));

				this.drawing.UpdateDrawing(radius,swepAngle);
			}
			this.drawing.requestLayout();
		}
	}

	class CircleMenuItem{
		private int id;	
		private int startAngle;
		private int endAngle;

		public CircleMenuItem(int id, int startAngle, int endAngle) {
			this.id=id;
			this.startAngle=startAngle;
			this.endAngle=endAngle;
		}

		public int getId() {
			return id;
		}
	}

	private Context getApplicationContext() {
		return this.context;
	}
	public void UpdateRadius(float mradius) {
		menuRadius=mradius;
		invalidate();
	}

	public void UpdatePosition(float x, float y) {
		xCenter=(int) x; 
		yCenter=(int) y;
		invalidate();
	}

	public void UpdateDrawing(float radius, int swepAngle) {
		this.menuItemsRadius=radius;
		this.swepAngle=swepAngle;
		invalidate();
	}

	private Paint getPaint(int n) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);

		paint.setColor(colors[n]);
		paint.setStrokeWidth((float) 2.5* getResources().getDisplayMetrics().density);
		return paint;
	}
	private Paint getTextPaint() {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(context.getResources().getColor(R.color.whitesmoke));
		paint.setStrokeWidth((float) 2.5 * getResources().getDisplayMetrics().density);
		paint.setAntiAlias(true);
		paint.setTextSize(textSize);
		return paint;
	}

	private Bitmap getBitmap(int i) {
		int[] bitmaps=new int[] {
				R.drawable.ic_action_camera,
				R.drawable.ic_action_user,
				R.drawable.ic_about,
				R.drawable.ic_contact,
				R.drawable.ic_action_call,
				R.drawable.ic_action_locate
		};
		Bitmap bm = BitmapFactory.decodeResource(getResources(),bitmaps[i]);
		return bm;
	}

	private boolean pointMenuTouch(float x, float y){
		float touchRadius = (float) Math.sqrt(Math.pow(x - xCenter, 2) + Math.pow(y - yCenter, 2));
		return (touchRadius < menuRadius);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()== MotionEvent.ACTION_UP){

			float x=event.getX();
			float y=event.getY();

			if(pointMenuTouch(x,y)) {
				isOpened =!isOpened;
				
				currentMenuImage=isOpened?R.drawable.ic_menu_list:R.drawable.ic_menu_suite;
				currentMenuText=isOpened?"Close":"Open";
				
				menuMaxRadius=isOpened?menuMinRadius:180.0f;

				AnimationSet animationSet = new AnimationSet(true);
				animationSet.addAnimation(menuAnimation);
				animationSet.addAnimation(menuItemsAnimation);

				thisWidget.startAnimation(animationSet);

				isLoading=false;
			}else {	
				int angleTouch=getTouchAngle(x,y);
				float touchRadius = getTouchradius(x,y);	

				for (CircleMenuItem menuItem : menuItems) {
					if((angleTouch>=menuItem.startAngle) && (angleTouch<=menuItem.endAngle) && (touchRadius <= menuItemsRadius*2)) {						
						callback.OnItemPressed(menuItem.getId());
						break;
					}
				}
			}
		}
		return true;
	}

	private int getTouchradius(float x,float y) {			
		float xpol=(x-xCenter);
		float ypol=(-1)*(y-yCenter);
		//Pythagore!!			
		return (int) Math.sqrt(xpol*xpol+ypol*ypol);
	}
	private int getTouchAngle(float x,float y) {			
		float xpol=(x-xCenter);
		float ypol=(-1)*(y-yCenter);

		double rpol= Math.sqrt(xpol*xpol+ypol*ypol); //Pythagore!!
		double radianPol;
		if(xpol>0) {
			radianPol = Math.atan(ypol/xpol)+((ypol>=0)?0:2* Math.PI);
		}else if(xpol<0) {
			radianPol = Math.atan(ypol/xpol)+ Math.PI;
		}else {
			radianPol = ypol>0? Math.PI/2:3* Math.PI;
		}
		//Log.i("Drawing","Polaires : "+xpol+" x "+ypol+" ==> "+rpol+" | "+radianPol+" ["+(radianPol*180/Math.PI)+"]");
		return (int)(radianPol*180/ Math.PI);
	}

}
