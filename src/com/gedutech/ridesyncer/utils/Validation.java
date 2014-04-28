package com.gedutech.ridesyncer.utils;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.gedutech.ridesyncer.R;

public class Validation {

	private Context context;
	private View focusView;
	private boolean hasErrors;

	public Validation(Context context) {
		this.context = context;
	}

	public void required(EditText input) {
		if (input.getText().length() == 0) {
			invalidate(input, R.string.error_field_required);
		}
	}

	public void invalidate(EditText input, int errorMsgId) {
		focusView = input;
		input.setError(context.getString(errorMsgId));
		hasErrors = true;
	}

	public View getFocusView() {
		return focusView;
	}

	public boolean hasErrors() {
		return hasErrors;
	}

}
