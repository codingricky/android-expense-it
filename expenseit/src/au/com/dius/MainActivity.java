package au.com.dius;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, ReceiptListActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("Receipts").setIndicator("Receipts").setContent(intent);
		spec.setIndicator("Receipts", getResources().getDrawable(R.drawable.icon_receipt_tab));

		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, SettingsActivity.class);
		spec = tabHost.newTabSpec("settings").setIndicator("Settings").setContent(intent);
		spec.setIndicator("Settings", getResources().getDrawable(R.drawable.icon_settings_tab));

		tabHost.addTab(spec);

		tabHost.setCurrentTab(2);
	}
}