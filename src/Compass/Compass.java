package Compass;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.widget.ImageView;

public class Compass extends ImageView{
	Paint p;
	int direction;
	public Compass(Context context, Bitmap bmp){
		super(context);
		
		p = new Paint();
		p.setColor(Color.WHITE);
		p.setStrokeWidth(2);
		p.setStyle(Style.STROKE);
		
		this.setImageBitmap(bmp);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.rotate(direction, this.getWidth()/2, this.getHeight()/2);
		super.onDraw(canvas);
	}
	
	
	public void setDirection (int direction){
		this.direction = direction;
		this.invalidate();
	}
	
	

}
