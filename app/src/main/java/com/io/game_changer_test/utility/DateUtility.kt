package com.io.game_changer_test.utility

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DateUtility {

    companion object commonFunction{

        fun dateDifference(dateInPref: String): Long {

            val dateAccessToken = dateFormatfromDifference(dateInPref)
            val date = Date()

            val diff = dateAccessToken.time - date.time
            val seconds = diff / 1000

            val minutes = seconds / 60
            val hours = minutes / 60

            val days = hours / 24
            return days
        }

        fun dateFormatfromDifference(date: String): Date {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val returnDate = formatter.parse(date)
            return returnDate

        }

        fun getDateWithTime(date: String): String? {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

            val formatterDisplay = SimpleDateFormat("dd-MM-yyyy hh:mm a")
            val dateInString = formatter.parse(date)
            val displayDate = formatterDisplay.format(dateInString)
            return displayDate

        }

        fun dateFormatToShow(date: String): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val dateInString = formatter.parse(date)

            var finalString = ""


            val calendar = Calendar.getInstance()


            calendar.time = dateInString


            val MONTHS =
                arrayOf(
                    "Jan",
                    "Feb",
                    "Mar",
                    "Apr",
                    "May",
                    "Jun",
                    "Jul",
                    "Aug",
                    "Sep",
                    "Oct",
                    "Nov",
                    "Dec"
                )


            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)


            val monthName = MONTHS[currentMonth]


            finalString =
                currentDay.toString() + " " + monthName + "'" + (currentYear).toString().substring(2)
            return finalString

        }

        fun dateForWebservice(date: String): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            val formatterDisplay = SimpleDateFormat("dd-MM-yyyy hh:mm aa")
            val dateInString = formatterDisplay.parse(date)
            val displayDate = formatter.format(dateInString)
            return displayDate

        }

        fun getSelectedMonth(currentMonth : String?): String? {

            val strMonth: String? = null
            val calendar = Calendar.getInstance()


            val currentYear = calendar.get(Calendar.YEAR)
            val currentSelectedMonth = calendar.get(Calendar.MONTH)



            val MONTHS =
                arrayOf(
                    "January",
                    "February",
                    "March",
                    "April",
                    "May",
                    "Jun",
                    "July",
                    "August",
                    "September",
                    "October",
                    "November",
                    "December"
                )





            val monthName = MONTHS[(currentMonth?.toInt()!! - 1)!!]

            var finalString:String? = null




            val monthNameCurrent = MONTHS[currentSelectedMonth]



            if(monthNameCurrent != null && !monthNameCurrent.equals("")){
                if(monthNameCurrent.equals("April")){
                    finalString = monthName + "'" + (currentYear).toString().substring(2)
                }else{
                    if(monthName.equals("January") || monthName.equals("February") || monthName.equals("March")){
                        finalString =
                            monthName + "'" + (currentYear).toString().substring(2)

                    }else{
                        finalString =
                            monthName + "'" + (currentYear - 1).toString().substring(2)
                    }
                }

            }else{
                finalString = monthName + "'" + (currentYear).toString().substring(2)
            }




            return finalString
        }

        fun timeStampaTimeago(time1: String): String? {
            var time: String = time1
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            format.timeZone = TimeZone.getTimeZone("UTC")
            val past = format.parse(time)
            val now = Date()
            val seconds1 = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
            val minutes1 = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
            val hours1 = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
            val days1 = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
            var seconds = Math.abs(seconds1)
            var minutes = Math.abs(minutes1)
            var hours = Math.abs(hours1)
            var days = Math.abs(days1)
            if (seconds < 60) {
                println("$seconds seconds ago")
                time = "$seconds seconds ago"
            } else if (minutes < 60) {
                println("$minutes minutes ago")
                time = "$minutes minutes ago"
            } else if (hours < 24) {
                println("$hours hours ago")
                time = "$hours hours ago"
            } else if (days < 30) {

                if ("$days".equals("1")) {
                    time = "$days day ago"
                } else {
                    time = "$days days ago"
                }
            } else if (days < 365) {
                var month = days / 30
                time = "$month month ago"
            } else if (days >= 365) {
                time = (days / 365).toString() + " year ago"
            } else {
                println("$days days ago")
                time = "$days days ago"
            }
            return time
        }

        fun datefrom(date: String): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'")
            val formatterDisplay = SimpleDateFormat("dd-MM-yyyy")
            val dateInString = formatterDisplay.parse(date)
            val displayDate = formatter.format(dateInString)
            return displayDate

        }

        fun dateTo(date: String): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'23:59:59.000'Z'")
            val formatterDisplay = SimpleDateFormat("dd-MM-yyyy")
            val dateInString = formatterDisplay.parse(date)
            val displayDate = formatter.format(dateInString)
            return displayDate

        }
    }
}