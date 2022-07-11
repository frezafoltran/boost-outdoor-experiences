package com.foltran.core_utils.date

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

val outFormat = SimpleDateFormat("dd/MM/yyyy")
val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
val dayMonthFormat = SimpleDateFormat("MMM d")
val dayMonthYearFormat = SimpleDateFormat("MMM d yyyy")
val monthFormat = SimpleDateFormat("MMM yyyy")
val yearFormat = SimpleDateFormat("yyyy")

fun String.formatUTCDateToClean(): String {
    return outFormat.format(utcFormat.parse(this))
}

fun String.formatUTCDateToDayMonth(): String {
    return dayMonthFormat.format(utcFormat.parse(this))
}

fun String.formatUTCDateToDayMonthYear(): String {
    return dayMonthYearFormat.format(utcFormat.parse(this))
}

fun String.formatUTCDateToMonth(): String {
    return monthFormat.format(utcFormat.parse(this))
}

fun String.formatUTCDateToYear(): String {
    return yearFormat.format(utcFormat.parse(this))
}

fun formatDateToDayMonth(date: Date): String {
    return dayMonthFormat.format(date)
}

fun String.utcToDate(): Date = utcFormat.parse(this)

fun String.utcToWeek(): String {
    val cal = Calendar.getInstance()
    cal.time = utcToDate()

    cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
    val startDay = formatDateToDayMonth(cal.time)

    cal.add(Calendar.WEEK_OF_YEAR, 1);
    val endDay = formatDateToDayMonth(cal.time)

    return "$startDay - $endDay"
}

fun String.utcToWeekNumber(): Int = Calendar.getInstance().let {
    it.time = utcToDate()
    it[Calendar.WEEK_OF_YEAR]
}

fun String.utcToMonthNumber(): Int = Calendar.getInstance().let {
    it.time = utcToDate()
    it[Calendar.MONTH]
}

fun String.utcToYearNumber(): Int = Calendar.getInstance().let {
    it.time = utcToDate()
    it[Calendar.YEAR]
}

fun Date.formatToUTC() = utcFormat.format(this)

fun getRandomDate(): Date {

    val gc = GregorianCalendar()

    val year = randIntBetween(2019, 2021)

    gc.set(GregorianCalendar.YEAR, year);

    val dayOfYear = randIntBetween(1, gc.getActualMaximum(GregorianCalendar.DAY_OF_YEAR));

    gc.set(GregorianCalendar.DAY_OF_YEAR, dayOfYear);

    val out = "${gc.get(GregorianCalendar.DAY_OF_MONTH)}/${gc.get(GregorianCalendar.MONTH) + 1}/${gc.get(
        GregorianCalendar.YEAR)}"

    return outFormat.parse(out)
}


fun randIntBetween(start: Int, end: Int): Int = start + (Math.random() * (end - start)).roundToInt()

fun randDoubleBetween(start: Int, end: Int): Double = start + (Math.random() * (end - start))
