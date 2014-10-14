/**
 * 
 */
package de.fhb.uebung1.helper;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Max Gregor
 * 
 */
public class ConvertHelper{
/**
 * 
 * @param time in Millisekunden
 * @return Zeit als String (hh:mm:ss)
 */
    
    public static String timerDisplay(long time) {
	StringBuilder timer = new StringBuilder();
	timer.append(((time / 1000) / 60) / 60)
		.append("h ")
		.append((time / 1000) / 60)
		.append("m ")
		.append(((time / 1000) % 60 < 10) ? ("0" + (time / 1000) % 60)
			: (time / 1000) % 60).append("s");
	return timer.toString();
    }

    /**
     * 
     * @return alter oder 0 wenn birthday null
     */
    public static int getAge(Calendar birthday) {
	GregorianCalendar now = new GregorianCalendar();
	GregorianCalendar birthdayGC = (GregorianCalendar) birthday;
	int age;
	if (birthday != null) {
	    age = now.get(GregorianCalendar.YEAR)
		    - birthdayGC.get(GregorianCalendar.YEAR);
	    if (now.get(GregorianCalendar.MONTH) < birthdayGC
		    .get(GregorianCalendar.MONTH)) {
		age -= 1;
	    } else if (now.get(GregorianCalendar.MONTH) == birthdayGC
		    .get(GregorianCalendar.MONTH)) {
		if (now.get(GregorianCalendar.DATE) < birthdayGC
			.get(GregorianCalendar.DATE)) {
		    age = age - 1;
		}
	    }
	    return age;
	} else {
	    return 0;
	}
    }

}
