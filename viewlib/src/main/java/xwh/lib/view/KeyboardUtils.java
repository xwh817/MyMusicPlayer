package xwh.lib.view;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by xwh on 2019/10/10.
 */
public class KeyboardUtils {
	public static void hideKeyboard(Context context, EditText editText) {
		InputMethodManager manager = (InputMethodManager) (context.getSystemService(Context.INPUT_METHOD_SERVICE));
		manager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
