package au.com.dius;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import au.com.dius.model.Receipt;
import au.com.dius.sql.ReceiptDataSource;

public class ReceiptDetailActivity extends Activity {

	private ReceiptDataSource receiptDataSource;

	private Spinner clientSpinner;
	private Spinner categorySpinner;
	private DatePicker datePicker;
	private TextView amountTextView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		receiptDataSource = new ReceiptDataSource(this);
		receiptDataSource.open();

		setContentView(R.layout.receiptdetail);

		clientSpinner = (Spinner) findViewById(R.id.clientSpinner);
		ArrayAdapter<CharSequence> clientAdapter = ArrayAdapter.createFromResource(this, R.array.client_array, android.R.layout.simple_spinner_item);
		clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		clientSpinner.setAdapter(clientAdapter);

		categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorySpinner.setAdapter(categoryAdapter);

		datePicker = (DatePicker) findViewById(R.id.datePicker);
		amountTextView = (TextView) findViewById(R.id.numberTextView);

		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				save();
			}
		});

		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				cancel();
			}
		});
	}

	private void save() {
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();

		final Calendar enteredDate = Calendar.getInstance();
		enteredDate.set(Calendar.DAY_OF_MONTH, day);
		enteredDate.set(Calendar.MONTH, month);
		enteredDate.set(Calendar.YEAR, year);

		String amount = amountTextView.getText().toString();
		int amountInCents = (int) (Float.parseFloat(amount) * 100);

		Receipt receipt = new Receipt(clientSpinner.getSelectedItem().toString(), categorySpinner.getSelectedItem().toString(), enteredDate.getTime(), amountInCents);
		receiptDataSource.createReceipt(receipt);
		receiptDataSource.close();
		finish();
	}
	
	private void cancel() {
		receiptDataSource.close();
		finish();
	}

	@Override
	protected void onResume() {
		receiptDataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		receiptDataSource.close();
		super.onPause();
	}
}
