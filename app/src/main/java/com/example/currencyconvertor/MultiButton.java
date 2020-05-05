package com.example.currencyconvertor;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.StyleableRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MultiButton extends ConstraintLayout
{
    public static final int MAX_NUMBER_OF_BUTTONS = 5;

    private int numOfButtons;
    private int activeButtonIndex;
    private TextView[] allTextViews;
    private View.OnClickListener[] additionalOnClickListeners;

    public MultiButton(Context context, AttributeSet attrs )
    {
        super(context,attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MultiButton);
        initNumOfButtons( typedArray.getInt(R.styleable.MultiButton_numOfButtons,1));

        initAllButtons(context,
                        typedArray.getColorStateList(R.styleable.MultiButton_textColorSelector),
                        typedArray.getDrawable(R.styleable.MultiButton_backgroundSelector));

        initButtonsConstraints(typedArray.getBoolean(R.styleable.MultiButton_reversedOrder,false));

        initActiveButton(typedArray.getInteger(R.styleable.MultiButton_activeButtonIndex,0));
    }

    private void initNumOfButtons(int numOfButtons)
    {
        if (numOfButtons < 0)
        {
            this.numOfButtons = 1;
        }
        else if (numOfButtons > MAX_NUMBER_OF_BUTTONS)
        {
            this.numOfButtons = MAX_NUMBER_OF_BUTTONS;
        }
        else
        {
            this.numOfButtons = numOfButtons;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        float[] corners = new float[8];
        float[] regularCorners = new float[] {0,0,0,0,0,0,0,0};
        float[] startFloat = new float[] {h,h,0,0,0,0,h,h};
        float[] endFloat = new float[] {0,0,h,h,h,h,0,0};
        for (int i=0; i< numOfButtons; i++)
        {


            if (i == 0)
            {
                corners = startFloat;
            }

            else if (i == numOfButtons -1)
            {
                corners = endFloat;
            }

            else
            {
                corners =regularCorners;
            }

                StateListDrawable stateListDrawable = (StateListDrawable) allTextViews[i].getBackground();

                int count = 0;
                try {
                    Method getStateCount = StateListDrawable.class.getMethod("getStateCount");
                    count = (int) getStateCount.invoke(stateListDrawable);

                    for (int j = 0; j < count; j++) {

                        Method getStateDrawable = StateListDrawable.class.getMethod("getStateDrawable", int.class);
                        GradientDrawable gd = (GradientDrawable) getStateDrawable.invoke(stateListDrawable, j);
                        gd.setCornerRadii(corners);

                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }


    }

    private void initAllButtons(Context context, ColorStateList colorStateList, Drawable drawable)
    {
        allTextViews = new TextView[numOfButtons];
        additionalOnClickListeners = new View.OnClickListener[numOfButtons];

        for (int i=0; i< numOfButtons;  i++)
        {

            allTextViews[i] = new TextView(context);
            allTextViews[i].setId(generateViewId());
            allTextViews[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_CONSTRAINT,LayoutParams.MATCH_PARENT));
            allTextViews[i].setOnClickListener(new DefaultButtonListener(i));
            if (colorStateList !=null)
            {
                allTextViews[i].setTextColor(colorStateList);
            }
            if (drawable != null)
            {

                /*
                for (int j=0; j<((StateListDrawable)drawable).getStateCount(); j++) {
                    GradientDrawable gradientDrawable = (GradientDrawable)((StateListDrawable)drawable).getStateDrawable(j);
                    gradientDrawable.setCornerRadius(8);
                }

                 */

                if (i==0)
                {
                    allTextViews[i].setBackground(drawable);
                }
                else
                {
                    allTextViews[i].setBackground(drawable.getConstantState().newDrawable().mutate());
                }
            }
            addView(allTextViews[i]);

        }

/*
        for (int i=0; i< 1;  i++) {
            GradientDrawable gd = new GradientDrawable();

            int[] state = new int[] {android.R.attr.state_selected};
            int[] state1 = new int[] {-android.R.attr.state_selected};
            gd.setState(state);
            // Specify the shape of drawable
            gd.setShape(GradientDrawable.RECTANGLE);

            // Set the fill color of drawable
            gd.setColor(Color.RED); // make the background transparent

            // Create a 2 pixels width red colored border for drawable
            gd.setStroke(2, Color.RED); // border width and color

            // Make the border rounded
            gd.setCornerRadius(90.0f); // border corner radius

            // Finally, apply the GradientDrawable as TextView background
            allTextViews[i].setBackground(gd);

            GradientDrawable gd1 = new GradientDrawable();
            gd1.setShape(GradientDrawable.RECTANGLE);
            gd1.setState(state1);
            gd1.setStroke(2, Color.RED); // border width and color
            allTextViews[i].setBackground(gd1);


        }
        */





    }

    private void initButtonsConstraints(boolean reversedOrder)
    {
        if (numOfButtons == 1) //chain must contain more than one button
        {
            return;
        }
        ConstraintSet set = new ConstraintSet();
        set.clone(this);
        int[] chainIds = new int[numOfButtons];
        float[] chainWeights = new float[numOfButtons];

        for (int i=0; i<numOfButtons; i++)
        {
            int currentIndex = reversedOrder ?  (numOfButtons-i-1) : i;
            chainIds[i] = allTextViews[currentIndex].getId();
            chainWeights[currentIndex] = 1;
        }
        set.createHorizontalChain(ConstraintSet.PARENT_ID,ConstraintSet.LEFT, ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,chainIds,chainWeights,ConstraintSet.CHAIN_PACKED);
        set.applyTo(this);
    }

    private void initActiveButton(int activeButtonIndex)
    {
        if (activeButtonIndex < 0 || activeButtonIndex > MAX_NUMBER_OF_BUTTONS -1)
        {
            this.activeButtonIndex = 0;
        }
        else
        {
            this.activeButtonIndex = activeButtonIndex;
        }
        allTextViews[this.activeButtonIndex].setSelected(true);
    }

    public int getNumOfButtons()
    {
        return numOfButtons;
    }

    public int getActiveButtonIndex()
    {
        return activeButtonIndex;
    }

    public void setSingleButtonText(String title, int index)
    {
        allTextViews[index].setText(title);
        allTextViews[index].setGravity(Gravity.CENTER);
    }

    public void addSingleButtonListener(View.OnClickListener listener,int index)
    {
        additionalOnClickListeners[index] = listener;
    }

    private class DefaultButtonListener implements View.OnClickListener
    {

        private int additionalListenerPosition;


        public DefaultButtonListener (int additionalListenerPosition)
        {
            this.additionalListenerPosition = additionalListenerPosition;
        }
        @Override
        public void onClick(View v)
        {
            allTextViews[activeButtonIndex].setSelected(false);
            activeButtonIndex = additionalListenerPosition;
            allTextViews[activeButtonIndex].setSelected(true);

            if (additionalOnClickListeners[activeButtonIndex]!=null) {
                additionalOnClickListeners[activeButtonIndex].onClick(v);
            }
        }
    }



}


 /*
            GradientDrawable gd = new GradientDrawable();


            gd.setState(SELECTED_STATE_SET);

            // Specify the shape of drawable
            gd.setShape(GradientDrawable.RECTANGLE);

            // Set the fill color of drawable
            gd.setColor(Color.RED); // make the background transparent

            // Create a 2 pixels width red colored border for drawable
            gd.setStroke(2, Color.RED); // border width and color

            // Make the border rounded
            gd.setCornerRadius(30.0f); // border corner radius

            // Finally, apply the GradientDrawable as TextView background
            allTextViews[i].setBackground(gd);

*/


 /*
        for  (int i=1; i< numOfButtons ; i++)
        {
            if (reversedOrder)
            {
                set.connect(allTextViews[i - 1].getId(), ConstraintSet.LEFT, allTextViews[i].getId(), ConstraintSet.RIGHT);
            }
            else
            {
                set.connect(allTextViews[i-1].getId(), ConstraintSet.RIGHT, allTextViews[i].getId(),ConstraintSet.LEFT);
            }
        }

        if (reversedOrder)
        {
            set.connect(allTextViews[0].getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT);
            set.connect(allTextViews[numOfButtons-1].getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT);
        }

        else
        {
            set.connect(allTextViews[0].getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT);
            set.connect(allTextViews[numOfButtons-1].getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT);
        }
*/