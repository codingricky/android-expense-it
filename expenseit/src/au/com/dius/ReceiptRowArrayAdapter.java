package au.com.dius;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import au.com.dius.model.Receipt;

public class ReceiptRowArrayAdapter extends ArrayAdapter<Receipt> {

	private final List<Receipt> receipts;
	private final int resource;
	private final LayoutInflater inflater;
	private final Context context;

	public ReceiptRowArrayAdapter(Context context, int resource, List<Receipt> receipts) {
		super(context, resource, receipts);
		this.context = context;
		this.resource = resource;
		this.receipts = receipts;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Receipt receipt = receipts.get(position);
		convertView = inflater.inflate(resource, null);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.imageReceiptRow);
		imageView.setImageResource(R.drawable.receipt);
		
		TextView client = (TextView) convertView.findViewById(R.id.clientReceiptRow);
		TextView date = (TextView) convertView.findViewById(R.id.dateReceiptRow);		
		TextView amount  = (TextView) convertView.findViewById(R.id.amountReceiptRow);
		
		client.setText(receipt.getClient());
		float amountInDollars = receipt.getAmountInCents()/100f;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		amount.setText("$" + decimalFormat.format(amountInDollars));
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
		date.setText(dateFormat.format(receipt.getDate()));

		convertView.setTag(receipt);
		convertView.setClickable(true);
		convertView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				viewReceipt(v);
			}
		});
		return convertView;
	}
	
	private void viewReceipt(View view) {
		Intent intent = new Intent(context, ReceiptDetailActivity.class);
		intent.putExtra("au.com.dius.Receipt", (Serializable) view.getTag());
		context.startActivity(intent);
	}
	
	

}