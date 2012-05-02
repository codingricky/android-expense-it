package au.com.dius.model;

import java.util.Arrays;
import java.util.Calendar;

import junit.framework.TestCase;

public class ExpenseTest extends TestCase {

	public void testJSONSerialization() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 2);
		cal.set(Calendar.YEAR, 2012);
		
		Receipt receipt1 = new Receipt(0, "Jemena", "Travel", cal.getTime(), 1050, "description", "path");
		Receipt receipt2 = new Receipt(2, "ResMed", "Travel", cal.getTime(), 2050, "description", "");
		Expense e = new Expense("John Smith", Arrays.asList(receipt1 ,receipt2));
		String expectedJSON = "{\"name\":\"John Smith\",\"receipts\":[{\"id\":0,\"client\":\"Jemena\",\"category\":\"Travel\",\"date\":\"01/03/2012\",\"amountInCents\":1050},{\"id\":2,\"client\":\"ResMed\",\"category\":\"Travel\",\"date\":\"01/03/2012\",\"amountInCents\":2050}]}";
		assertEquals(expectedJSON, e.serialize());
	}
}
