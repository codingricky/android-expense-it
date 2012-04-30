package au.com.dius.model;

import java.util.Date;

public class Receipt {
	
	private long id;
	private String client;
	private String category;
	private Date date;
	private int amountInCents;

	public Receipt(String client, String category, Date date, int amountInCents) {
		this.client = client;
		this.category = category;
		this.date = date;
		this.amountInCents = amountInCents;
	}

	public Receipt(long id, String client, String category, Date date, int amountInCents) {
		this.id = id;
		this.client = client;
		this.category = category;
		this.date = date;
		this.amountInCents = amountInCents;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getClient() {
		return client;
	}

	public String getCategory() {
		return category;
	}

	public Date getDate() {
		return date;
	}

	public int getAmountInCents() {
		return amountInCents;
	}

	public String toString() {
		return client + " " + category + " " + amountInCents + " " + date;
	}
}
