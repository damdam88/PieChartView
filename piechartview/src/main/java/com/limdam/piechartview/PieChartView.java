package com.limdam.piechartview;

/**
 * Created by limdam on 2018-01-31.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PieChartView extends RelativeLayout {

  ImageView img;
  TextView txt;
  float strokeDimen;
  float paddingDimen;

  int[] countVal = {1,1,1};

  public PieChartView(@NonNull Context context) {
    super(context);
    initView();
  }

  public PieChartView(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    initView();
    getAttrs(attrs);
  }

  public PieChartView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
    getAttrs(attrs, defStyleAttr);
  }

  private void initView() {
    String infService = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
    View v = null;
    if (li != null) {
      v = li.inflate(R.layout.piechart, this, false);
    }
    addView(v);

    img = findViewById(R.id.center_image);
    txt = findViewById(R.id.image_desc);
  }

  private void getAttrs(AttributeSet attrs) {
    TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PieChartView);
    setTypeArray(typedArray);
  }

  private void getAttrs(AttributeSet attrs, int defStyle) {
    TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PieChartView, defStyle, 0);
    setTypeArray(typedArray);
  }

  private void setTypeArray(TypedArray typedArray) {
    int img_resID = typedArray.getResourceId(R.styleable.PieChartView_image, R.mipmap.ic_launcher);
    img.setBackgroundResource(img_resID);
    String text_string = typedArray.getString(R.styleable.PieChartView_text);
    txt.setText(text_string);
    strokeDimen = typedArray.getDimension(R.styleable.PieChartView_stroke, 0);
    paddingDimen = typedArray.getDimension(R.styleable.PieChartView_padding, 0);
    typedArray.recycle();
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);

    int startAngle = -90;
    int[] angles = getAngles(countVal);
    String[] colorArr = {"#ff7c59", "#ffb225", "#3db3ff"};

    RectF rect = new RectF();
    rect.set(0 + paddingDimen, 0 + paddingDimen, canvas.getWidth() - paddingDimen, canvas.getHeight() - paddingDimen);

    Paint pnt = new Paint();
    pnt.setStrokeWidth(strokeDimen);
    pnt.setStyle(Paint.Style.STROKE);
    pnt.setAntiAlias(true);

    for(int i=0;i<3;i++) {
      pnt.setColor(Color.parseColor(colorArr[i]));
      canvas.drawArc(rect, startAngle, angles[i]+1, false, pnt);
      startAngle += angles[i];
    }
  }

  public void setCenterImage (int bg_resID) {
    img.setBackgroundResource(bg_resID);
  }

  public void setText(String text_string) {
    txt.setText(text_string);
  }

  public void setText(int text_resID) {
    txt.setText(text_resID);
  }

  public void setStrokeDimen(float f) {
    strokeDimen = f;
  }

  public  void setStrokeDimen(int dp) {
    strokeDimen = d2f(dp);
  }

  public void setPaddingDimen(float f) {
    paddingDimen = f;
  }

  public void setPaddingDimen(int dp) {
    paddingDimen = d2f(dp);
  }

  public void setValues(int[] values) {
    countVal = values;
  }

  private int[] getAngles(int[] a) {
    int totalVal = 0;
    for(int i : a) {
      totalVal += i;
    }

    int[] returnVal = new int[3];
    for(int i=0;i<3;i++) {
      returnVal[i] = 360 * a[i]/totalVal;
    }

    int maxIndex = 0;
    int maxVal = 0;
    totalVal = 0;
    for(int j=0;j<3;j++) {
      totalVal += returnVal[j];
      if(returnVal[j] > maxVal) {
        maxIndex = j;
        maxVal = returnVal[j];
      }
    }

    if(360 - totalVal != 0) {
      returnVal[maxIndex] += (360 - totalVal);
    }

    return returnVal;
  }

  private float d2f(int dp) {
    return TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp,
        getContext().getResources().getDisplayMetrics() );
  }
}
