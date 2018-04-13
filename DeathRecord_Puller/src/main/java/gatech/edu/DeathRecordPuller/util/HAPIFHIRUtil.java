package gatech.edu.DeathRecordPuller.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.AgeDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.RangeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;

public class HAPIFHIRUtil {
	public static Date getDate(IDatatype data) {
		if(data instanceof DateTimeDt) {
			return getDate((DateTimeDt)data);
		}
		if(data instanceof AgeDt) {
			return getDate((AgeDt)data);
		}
		if(data instanceof PeriodDt) {
			return getDate((PeriodDt)data);
		}
		if(data instanceof RangeDt) {
			return getDate((RangeDt)data);
		}
		if(data instanceof StringDt) {
			return getDate((StringDt)data);
		}
		return null;
	}
	
	public static Date getDate(DateTimeDt dateTime) {
		return dateTime.getValue();
	}
	public static Date getDate(AgeDt age) {
		return null;
	}
	public static Date getDate(PeriodDt period) {
		return period.getStart();
	}
	public static Date getDate(RangeDt range) {
		return null;
	}
	public static Date getDate(StringDt string) {
		try {
			return DateFormat.getDateInstance().parse(string.toString());
		} catch (ParseException e) {
			return null;
		}
	}
}
