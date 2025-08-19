package com.example.edupay.font;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.edupay.R;


public class RobotoTextView extends AppCompatTextView {


	public RobotoTextView(Context context) {
		super(context);
		if (isInEditMode()) return;
		parseAttributes(null);
	}

	public RobotoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) return;
		parseAttributes(attrs);
	}

	public RobotoTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode()) return;
		parseAttributes(attrs);
	}

	public static Typeface getRoboto(Context context, int typeface) {
		switch (typeface) {
			default:
			case Roboto.manrope_FALLBACK:
				if (Roboto.manropeFallback == null) {
					Roboto.manropeFallback = Typeface.createFromAsset(context.getAssets(), "fonts/manrope_regular.ttf");
					//  Roboto.manropeFallback = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Italic.ttf");
				}
				return Roboto.manropeFallback;
			case Roboto.manrope_SEMI_BOLD:
				if (Roboto.manropeSemibold == null) {
					Roboto.manropeSemibold = Typeface.createFromAsset(context.getAssets(), "fonts/manrope_semibold.ttf");
					//  Roboto.manropeFallback = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Italic.ttf");
				}
				return Roboto.manropeSemibold;

			case Roboto.manrope_BOLD:
				if (Roboto.manropebold == null) {
					Roboto.manropebold = Typeface.createFromAsset(context.getAssets(), "fonts/manrope_bold.ttf");
					//  Roboto.manropeFallback = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Italic.ttf");
				}
				return Roboto.manropebold;
		}
	}

	private void parseAttributes(AttributeSet attrs) {
		int typeface;
		if (attrs == null) { //Not created from xml
			typeface = Roboto.manrope_FALLBACK;
		} else {
			TypedArray values = getContext().obtainStyledAttributes(attrs, R.styleable.RobotoTextView);
			typeface = values.getInt(R.styleable.RobotoTextView_typeface, Roboto.manrope_FALLBACK);
			values.recycle();
		}
		setTypeface(getRoboto(typeface));
	}

	public void setRobotoTypeface(int typeface) {
		setTypeface(getRoboto(typeface));
	}

	private Typeface getRoboto(int typeface) {
		return getRoboto(getContext(), typeface);
	}

	public static class Roboto {

		public static final int manrope_FALLBACK = 18;
		public static final int manrope_SEMI_BOLD = 19;
		public static final int manrope_BOLD = 20;


		//   private static Typeface sMontserratMmedium;
		// private static Typeface sMontserratRegular;
		private static Typeface manropeFallback;
		private static Typeface manropebold;
		private static Typeface manropeSemibold;
	}
}
