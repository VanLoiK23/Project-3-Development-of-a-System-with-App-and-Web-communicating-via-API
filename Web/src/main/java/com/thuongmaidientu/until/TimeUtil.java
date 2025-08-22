package com.thuongmaidientu.until;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TimeUtil {
	public static Date getExpiryDate(int minutes) {
		LocalDateTime expiry = LocalDateTime.now().plusMinutes(minutes);
		return Date.from(expiry.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
	}
}
