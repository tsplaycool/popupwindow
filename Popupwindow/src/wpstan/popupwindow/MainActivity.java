package wpstan.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.PopupWindow;

public class MainActivity extends Activity {

	private Context mContext;
	private EditText mEdittext;
	private PopupWindow mPopWindow;
	private Runnable mRunnable;
	private Runnable mRemoveRunnable;

	private final String mKey = "wpstan";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setEditValue();// 刚进来，显示输入的草稿
		pop(1000);// 显示弹框
	}

	private void initPopupWindow() {
		View view = new View(mContext);
		view.setLayoutParams(new LayoutParams(200, 200));
		view.setBackgroundColor(Color.parseColor("#ff0000"));
		mPopWindow = new PopupWindow(view, 200, 200);
	}

	private void initRunnable() {
		mRunnable = new Runnable() {

			@Override
			public void run() {
				mPopWindow.showAsDropDown(mEdittext);
			}
		};

		mRemoveRunnable = new Runnable() {

			@Override
			public void run() {
				mPopWindow.dismiss();
			}
		};
	}

	private void initViews() {
		mContext = this;
		initRunnable();
		initPopupWindow();
		mEdittext = (EditText) findViewById(R.id.edittext);
		mEdittext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.equals("0", s)) {
					pop(0);
				} else {
					removeRightNow();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	private void pop(int delayMill) {
		//等于0才弹框
		if (!TextUtils.equals("0", mEdittext.getText())) {
			return;
		}
		mEdittext.removeCallbacks(mRunnable);
		mEdittext.removeCallbacks(mRemoveRunnable);
		mEdittext.postDelayed(mRunnable, delayMill);
		mEdittext.postDelayed(mRemoveRunnable, delayMill + 6000);
	}

	private void removeRightNow() {
		mEdittext.removeCallbacks(mRunnable);
		mEdittext.removeCallbacks(mRemoveRunnable);
		mPopWindow.dismiss();
	}

	private void setEditValue() {
		Toast.makeText(mContext,
				getPreferences(MODE_PRIVATE).getString(mKey, ""),
				Toast.LENGTH_LONG).show();
		mEdittext.setText(getPreferences(MODE_PRIVATE).getString(mKey, ""));
	}

	private void saveEditValue() {
		Editor e = getPreferences(MODE_PRIVATE).edit();
		e.putString(mKey, mEdittext.getText().toString());
		Toast.makeText(mContext, mEdittext.getText().toString(),
				Toast.LENGTH_LONG).show();
		e.commit();
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveEditValue();
	}

}
