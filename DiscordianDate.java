import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A Class to work with discordian dates
 * 
 * @author 0xflotus
 * @version 0.9.7 06/06/2017
 * @since 06/02/2017
 *
 */
public class DiscordianDate {
	private int _year, _season, _yearDay, _seasonDay, _weekDay;
	private static boolean _isLeap;
	private static String[] _seasonNames = { "Chaos", "Discord", "Confusion", "Bureaucracy", "The Aftermath" };
	private static String[] _dayNames = { "Sweetmorn", "Boomtime", "Pungenday", "Prickle-Prickle", "Setting Orange" };
	private static String[] _holidays = { "Mungday", "Chaoflux", "St. Tib's Day", "Mojoday", "Discoflux", "Syaday",
			"Confuflux", "Zaraday", "Bureflux", "Maladay", "Afflux" };
	private static LocalDate _localDate;
	private static Comparator<DiscordianDate> COMPARATOR_BY_YEAR = (one, two) -> one.getYear() - two.getYear();
	private static Comparator<DiscordianDate> COMPARATOR_BY_DAY = (one, two) -> one.getYearDay() - two.getYearDay();

	/**
	 * maximum amount of days per season
	 */
	public static final int MAX_DAY_OF_SEASON = 73;

	/**
	 * difference of discordian year and gregorian year
	 */
	public static final int YEAR_DIFFERENCE = 1166;

	/**
	 * amount of seasons
	 */
	public static final int COUNT_SEASONS = _seasonNames.length;

	/**
	 * amount of days
	 */
	public static final int COUNT_DAYS = _dayNames.length;

	/**
	 * the maximum value of a discordian date
	 */
	public static final DiscordianDate MAX_VALUE = new DiscordianDate(LocalDate.MAX);

	/**
	 * a List of the Holiday names
	 */
	public static final List<String> HOLIDAY_NAMES = Arrays.asList(_holidays);

	/**
	 * a List of possible Day names
	 */
	public static final List<String> POSSIBLE_DAY_NAMES = Arrays.asList(_dayNames);

	/**
	 * a List of possible Season names
	 */
	public static final List<String> POSSIBLE_SEASON_NAMES = Arrays.asList(_seasonNames);

	/**
	 * a Comparator
	 */
	public static Comparator<DiscordianDate> COMPARATOR = COMPARATOR_BY_YEAR.thenComparing(COMPARATOR_BY_DAY);

	private DiscordianDate(LocalDate ld) {
		_year = ld.getYear() + YEAR_DIFFERENCE;
		_yearDay = ld.getDayOfYear();
		_isLeap = ld.isLeapYear();
		int yd = _yearDay - 1;
		if (_isLeap && yd > 59)
			yd--;
		_season = (yd / MAX_DAY_OF_SEASON) + 1;
		_weekDay = (yd % COUNT_DAYS) + 1;
		_seasonDay = (yd % MAX_DAY_OF_SEASON) + 1;
	}

	private static class DiscordianHoliday {
		int day;
		boolean leap;
		Map<Integer, String> map;
		private static final String NO_HOLIDAY = "No Holiday";

		DiscordianHoliday(int day, boolean leap) {
			this.day = day;
			this.leap = leap;
			populate();
		}

		public String getHoliday() {
			return map.getOrDefault(this.day, NO_HOLIDAY);
		}

		private void populate() {
			map = new HashMap<>();
			map.put(5, _holidays[0]);
			map.put(50, _holidays[1]);
			map.put(60, leap ? _holidays[2] : NO_HOLIDAY);
			map.put(leap ? 79 : 78, _holidays[3]);
			map.put(leap ? 124 : 123, _holidays[4]);
			map.put(leap ? 152 : 151, _holidays[5]);
			map.put(leap ? 197 : 196, _holidays[6]);
			map.put(leap ? 225 : 224, _holidays[7]);
			map.put(leap ? 270 : 269, _holidays[8]);
			map.put(leap ? 298 : 297, _holidays[9]);
			map.put(leap ? 343 : 342, _holidays[10]);
		}
	}

	/**
	 * @param ddate
	 *            the Discordian date
	 * @return an Optional of String of the Holiday
	 */
	public static Optional<String> whichHoliday(DiscordianDate ddate) {
		return getHolyday(ddate).equals(DiscordianHoliday.NO_HOLIDAY) ? Optional.empty()
				: Optional.of(getHolyday(ddate));
	}

	/**
	 * @param ddate
	 *            the discordian date
	 * @return the Holiday or "No Holiday"
	 */
	public static String getHolyday(DiscordianDate ddate) {
		return new DiscordianHoliday(ddate._yearDay, DiscordianDate._isLeap).getHoliday();
	}

	/**
	 * @param ddate
	 *            the discordian date
	 * @return true, if the date is a holyday
	 */
	public static boolean isHoliday(DiscordianDate ddate) {
		return whichHoliday(ddate).isPresent();
	}

	/**
	 * Object method to check for holyday
	 * 
	 * @return true, if the object is a holyday
	 */
	public boolean isHoliday() {
		return isHoliday(this);
	}

	/**
	 * @param days
	 *            to add
	 * @return a new DiscordianDate with added days
	 */
	public DiscordianDate addDaysFromNow(int days) {
		_localDate = LocalDate.now().plusDays(days);
		return new DiscordianDate(_localDate);
	}

	/**
	 * @param weeks
	 *            to add
	 * @return a new DiscordianDate with added weeks
	 */
	public DiscordianDate addWeeksFromNow(int weeks) {
		_localDate = LocalDate.now().plusWeeks(weeks);
		return new DiscordianDate(_localDate);
	}

	/**
	 * @param months
	 *            to add
	 * @return a new DiscordianDate with added months
	 */
	public DiscordianDate addMonthsFromNow(int months) {
		_localDate = LocalDate.now().plusMonths(months);
		return new DiscordianDate(_localDate);
	}

	/**
	 * @param years
	 *            to add
	 * @return a new DiscordianDate with added years
	 */
	public DiscordianDate addYearsFromNow(int years) {
		_localDate = LocalDate.now().plusYears(years);
		return new DiscordianDate(_localDate);
	}

	/**
	 * @return a new DiscordianDate with current value
	 */
	public static DiscordianDate now() {
		_localDate = LocalDate.now();
		return new DiscordianDate(_localDate);
	}

	/**
	 * @param localdate
	 * @return a new DiscordianDate with specified LocalDate
	 */
	public static DiscordianDate of(LocalDate ld) {
		_localDate = ld;
		return new DiscordianDate(_localDate);
	}

	/**
	 * @param dayOfMonth
	 * @return a new DiscordianDate with current year, month and specified
	 *         dayOfMonth
	 */
	public static DiscordianDate of(int dayOfMonth) {
		_localDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), dayOfMonth);
		return new DiscordianDate(_localDate);
	}

	/**
	 * @param month
	 * @param dayOfMonth
	 * @return a new DiscordianDate with current year and specified month and
	 *         dayOfMonth
	 */
	public static DiscordianDate of(int month, int dayOfMonth) {
		_localDate = LocalDate.of(LocalDate.now().getYear(), month, dayOfMonth);
		return new DiscordianDate(_localDate);
	}

	/**
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @return a new DiscordianDate with specified year, month and dayOfMonth
	 */
	public static DiscordianDate of(int year, int month, int dayOfMonth) {
		_localDate = LocalDate.of(year, month, dayOfMonth);
		return new DiscordianDate(_localDate);
	}

	/**
	 * @return the discordian year
	 */
	public int getYear() {
		return _year;
	}

	/**
	 * @return the number of season
	 */
	public int getSeason() {
		return _season;
	}

	/**
	 * @return the number of the day of the year
	 */
	public int getYearDay() {
		return _yearDay;
	}

	/**
	 * @return the day of the season
	 */
	public int getSeasonDay() {
		return _seasonDay;
	}

	/**
	 * @return the name of this date's season
	 */
	public String getSeasonName() {
		return _seasonNames[_season - 1];
	}

	/**
	 * @return the name of the date's day
	 */
	public String getDayName() {
		return _dayNames[_weekDay - 1];
	}

	/**
	 * @return the LocalDate-Representation of the discordian date
	 */
	public LocalDate getTime() {
		return _localDate;
	}

	/**
	 * A String representation of the discordian date
	 */
	@Override
	public String toString() {
		return _isLeap && _yearDay == 59 ? _holidays[2] + ", " + Integer.toString(_year)
				: _dayNames[_weekDay - 1] + ", " + _seasonNames[_season - 1] + " " + Integer.toString(_seasonDay) + ", "
						+ Integer.toString(_year);
	}
}
