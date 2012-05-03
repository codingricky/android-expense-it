package au.com.dius;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import au.com.dius.model.Expense;
import au.com.dius.model.Receipt;
import au.com.dius.sql.ReceiptDataSource;

public class ReceiptListActivity extends Activity {

	private static final String TAG = "ReceiptListActivity";
	private ReceiptDataSource receiptDataSource;
	private ArrayAdapter<Receipt> adapter;
	private ProgressDialog dialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		receiptDataSource = new ReceiptDataSource(this);
		receiptDataSource.open();

		setContentView(R.layout.receipt);

		adapter = new ReceiptRowArrayAdapter(this, R.layout.receiptlistrow, receiptDataSource.getAllReceipts());
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
		listView.setClickable(true);

		Button addReceiptButton = (Button) findViewById(R.id.addReceiptButton);
		addReceiptButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				addReceipt();
			}
		});
		
		Button exportReceiptsButton = (Button) findViewById(R.id.exportReceiptsButton);
		exportReceiptsButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				export();
			}
		});
	}

	private void addReceipt() {
		startActivity(new Intent(this, ReceiptDetailActivity.class));		
	}

	private void export() {
	    dialog = ProgressDialog.show(ReceiptListActivity.this, "", "Exporting Please wait...", true);

	    String name = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("nameChoice", "");
		Expense expense = new Expense(name, receiptDataSource.getAllReceipts());
		new PostExpenseTask().execute(expense);
	}

	@Override
	protected void onResume() {
		receiptDataSource.open();
		adapter.clear();

		for (Receipt receipt : receiptDataSource.getAllReceipts()) {
			adapter.add(receipt);
		}

		adapter.notifyDataSetChanged();

		super.onResume();
	}

	@Override
	protected void onPause() {
		receiptDataSource.close();
		super.onPause();
	}
	
	 private class PostExpenseTask extends AsyncTask<Expense, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Expense... params) {

			try {
				Expense expense = params[0];
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost("http://expenseitserver.heroku.com/expense");
				post.setHeader("Accept", "application/json");
				post.setHeader("Content-type", "application/json");
				
				post.setEntity(new StringEntity(expense.serialize()));
				HttpResponse response = client.execute(post);
				String id = EntityUtils.toString(response.getEntity());
				Log.d(TAG, "response received=" + id);
				
//				HttpGet excelGet = new HttpGet("http://expenseitserver.heroku.com/expense/" + id + "/email/ricky@dius.com.au");
//				client.execute(excelGet);
			} catch (Exception e) {
				Log.e(TAG, "export failed", e);
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (dialog != null) {	
				dialog.dismiss();
			}
			
			if (result) {
				Context context = getApplicationContext();
				CharSequence text = "Export finished";
				int duration = Toast.LENGTH_SHORT;
				
				Toast toast = Toast.makeText(context, text, duration);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();				
			}
		}
		
		
	 }
}
