package me.goodt.vkpht.module.notification.api.dto.data;

public enum Month {
	JANUARY,
	FEBRUARY,
	MARCH,
	APRIL,
	MAY,
	JUNE,
	JULY,
	AUGUST,
	SEPTEMBER,
	OCTOBER,
	NOVEMBER,
	DECEMBER;

	private static final String JANUARY_RUS = "январь";
	private static final String FEBRUARY_RUS = "февраль";
	private static final String MARCH_RUS = "март";
	private static final String APRIL_RUS = "апрель";
	private static final String MAY_RUS = "май";
	private static final String JUNE_RUS = "июнь";
	private static final String JULY_RUS = "июль";
	private static final String AUGUST_RUS = "август";
	private static final String SEPTEMBER_RUS = "сентябрь";
	private static final String OCTOBER_RUS = "октябрь";
	private static final String NOVEMBER_RUS = "ноябрь";
	private static final String DECEMBER_RUS = "декабрь";

	public static Month getMonthByNumber(Integer number) {
		switch (number) {
			case 1:
				return JANUARY;
			case 2:
				return FEBRUARY;
			case 3:
				return MARCH;
			case 5:
				return MAY;
			case 6:
				return JUNE;
			case 7:
				return JULY;
			case 8:
				return AUGUST;
			case 9:
				return SEPTEMBER;
			case 10:
				return OCTOBER;
			case 11:
				return NOVEMBER;
			case 12:
				return DECEMBER;
			default:
				return null;
		}
	}

	public static String inRussian(Integer number) {
		if (number != null) {
			Month month = getMonthByNumber(number);
			if (month != null) {
				switch (month) {
					case JANUARY:
						return JANUARY_RUS;
					case FEBRUARY:
						return FEBRUARY_RUS;
					case MARCH:
						return MARCH_RUS;
					case APRIL:
						return APRIL_RUS;
					case MAY:
						return MAY_RUS;
					case JUNE:
						return JUNE_RUS;
					case JULY:
						return JULY_RUS;
					case AUGUST:
						return AUGUST_RUS;
					case SEPTEMBER:
						return SEPTEMBER_RUS;
					case OCTOBER:
						return OCTOBER_RUS;
					case NOVEMBER:
						return NOVEMBER_RUS;
					case DECEMBER:
						return DECEMBER_RUS;
					default:
						return null;
				}
			}
		}
		return null;
	}
}
