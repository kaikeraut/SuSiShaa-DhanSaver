package co.iin.susiddhi.sushishaa_dhansaver.ui

import android.graphics.Color
import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import co.iin.susiddhi.sushishaa_dhansaver.R
import co.iin.susiddhi.sushishaa_dhansaver.database.DataBaseHandler
import co.iin.susiddhi.sushishaa_dhansaver.database.ExpenseChartCalulatedData
import co.iin.susiddhi.sushishaa_dhansaver.database.FILTER_MONTHWISE
import java.time.LocalDate
import java.time.Year
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MonthWiseViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MonthWiseViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view:View =  inflater.inflate(R.layout.fragment_month_wise_view, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Monthly View"
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var textViewCalList: ArrayList<TextView> = ArrayList()

        var textViewRId: ArrayList<Int> = ArrayList()
        textViewRId.add(R.id.textViewMonth1)
        textViewRId.add(R.id.textViewMonth2)
        textViewRId.add(R.id.textViewMonth3)
        textViewRId.add(R.id.textViewMonth4)
        textViewRId.add(R.id.textViewMonth5)
        textViewRId.add(R.id.textViewMonth6)
        textViewRId.add(R.id.textViewMonth7)
        textViewRId.add(R.id.textViewMonth8)
        textViewRId.add(R.id.textViewMonth9)
        textViewRId.add(R.id.textViewMonth10)
        textViewRId.add(R.id.textViewMonth11)
        textViewRId.add(R.id.textViewMonth12)
        textViewRId.add(R.id.textViewMonth13)
        textViewRId.add(R.id.textViewMonth14)
        textViewRId.add(R.id.textViewMonth15)
        textViewRId.add(R.id.textViewMonth16)
        textViewRId.add(R.id.textViewMonth17)
        textViewRId.add(R.id.textViewMonth18)
        textViewRId.add(R.id.textViewMonth19)
        textViewRId.add(R.id.textViewMonth20)
        textViewRId.add(R.id.textViewMonth21)
        textViewRId.add(R.id.textViewMonth22)
        textViewRId.add(R.id.textViewMonth23)
        textViewRId.add(R.id.textViewMonth24)
        textViewRId.add(R.id.textViewMonth25)
        textViewRId.add(R.id.textViewMonth26)
        textViewRId.add(R.id.textViewMonth27)
        textViewRId.add(R.id.textViewMonth28)
        textViewRId.add(R.id.textViewMonth29)
        textViewRId.add(R.id.textViewMonth30)
        textViewRId.add(R.id.textViewMonth31)
        textViewRId.add(R.id.textViewMonth32)
        textViewRId.add(R.id.textViewMonth33)
        textViewRId.add(R.id.textViewMonth34)
        textViewRId.add(R.id.textViewMonth35)
        textViewRId.add(R.id.textViewMonth36)
        textViewRId.add(R.id.textViewMonth37)
        textViewRId.add(R.id.textViewMonth38)
        textViewRId.add(R.id.textViewMonth39)
        textViewRId.add(R.id.textViewMonth40)
        textViewRId.add(R.id.textViewMonth41)
        textViewRId.add(R.id.textViewMonth42)

        val monthNameList = listOf<String>("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug","Sep", "Oct", "Nov", "Dec")
        val calender = Calendar.getInstance()
        val month_name_format = SimpleDateFormat("MMM")
        var month_number_format = SimpleDateFormat("MM")
        var month_name: String = month_name_format.format(calender.time)
        var month_number = month_number_format.format(calender.time).toString().toInt()-1
        var year = Year.now().toString().toInt()

        for(day in 1..42)
        {
            var textView: TextView = requireView()?.findViewById(textViewRId[day-1])
            context?.getColor(R.color.monthTextViewColor)?.let { textView?.setBackgroundColor(it) }
            textViewCalList.add(textView)
            textView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("YearFragmentMonthYear", "${textView.text},${month_number+1},$year") // Put anything what you want
                var fragment = ExpenseCardViewListFragment()
                fragment.arguments = bundle
                activity?.run {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.main_fragment_section_mainactivity,
                        fragment
                    )
                        .addToBackStack(ExpenseCardViewListFragment.toString())
                        .commit()
                }
            }

            Log.i("TAG", "textView$day ->> $textView")
        }

        var btnPrev: Button = requireView()?.findViewById(R.id.buttonPrev)
        var btnNext: Button = requireView()?.findViewById(R.id.buttonNext)
        var btnDate: Button = requireView()?.findViewById(R.id.buttonDate)

        //Center Button Text
        btnDate.text = "$month_name, $year"

        btnDate.setOnClickListener{
            month_number = month_number_format.format(calender.time).toString().toInt()-1
            year = Year.now().toString().toInt()
            btnDate.text = "${monthNameList[month_number]}, $year"
            populateDatesInCalenderView(textViewCalList, month_number+1, year)
        }

        btnPrev.setOnClickListener {
            month_number -= 1
            if(month_number == -1)
            {
                month_number = 11
                year -= 1
            }
            btnDate.text = "${monthNameList[month_number]}, $year"
            populateDatesInCalenderView(textViewCalList, month_number+1, year)
        }
        btnNext.setOnClickListener {
            month_number += 1
            if(month_number == 12)
            {
                month_number = 0
                year += 1
            }
            btnDate.text = "${monthNameList[month_number]}, $year"
            populateDatesInCalenderView(textViewCalList, month_number+1, year)
        }
        var buttonMonthYear: Button = requireView()?.findViewById(R.id.buttonMonthYear)
        buttonMonthYear.setOnClickListener{
            activity?.run {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragment_section_mainactivity, YearWiseViewFragment())
                    .addToBackStack(MonthWiseViewFragment.toString())
                    .commit()
            }
        }
        populateDatesInCalenderView(textViewCalList, month_number+1, year)
    }//onViewCreated

    private fun populateDatesInCalenderView(textViewCalList: ArrayList<TextView>, monthNumber: Int, year: Int) {
        var daysInMonth = getMonthDays(monthNumber, year)
        var daysInPrevMonth = getMonthDays(monthNumber-1, year)
        var weekDay = getMonthStartOfWeek(monthNumber, year)
        val weeksList = listOf<String>("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY")
        val startIndex = weeksList.indexOf(weekDay)
        Log.e("DAYS", "Mon:$monthNumber, Year:$year ->daysInMonth:$daysInMonth, weekDay:$weekDay startIndex:$startIndex")
        var noOfDaysInWeek = 7

        val db = context?.let { DataBaseHandler(it) }
        val calcData: ExpenseChartCalulatedData? = db?.calculateDateForPieChart(FILTER_MONTHWISE, monthNumber, year)

        var date = 1
        daysInPrevMonth = daysInPrevMonth - startIndex
        var loopCounter = 0
        var actualDayCounter = 0
        var endLeftDays = 0
        for(day in textViewCalList)
        {
            var dateGreyOut = 0
            if(loopCounter < startIndex)
            {
                daysInPrevMonth += 1;
                date = daysInPrevMonth;
                dateGreyOut = 1
            }
            else if(actualDayCounter < daysInMonth)
            {
                actualDayCounter += 1
                date = actualDayCounter
            }
            else
            {
                endLeftDays += 1
                date = endLeftDays
                dateGreyOut = 1
            }
            if(day == null)
            {
                Log.i("TAG", "$date ->> $day is null")
            }
            else
            {
                if(dateGreyOut == 1)
                {
                    day?.text = (date).toString()
                    day?.setTextColor(Color.DKGRAY)
                    day?.setBackgroundColor(Color.LTGRAY)
                }else
                {
                    var rupee = 0
                    if(calcData != null) {
                        if (calcData.dailyExpenseDataMap.containsKey(date)) {
                            rupee = calcData.dailyExpenseDataMap[date]!!
                        }
                    }
                    val html = ("<font color=" + Color.BLACK
                            + ">${date} </font><br/><br/><font color="
                            + Color.BLACK + ">${rupee}</font>")
                    day?.text = Html.fromHtml(html)

                    //day?.text = (date).toString() + "\n\n\n${rupee}"
                    //day?.setTextColor(Color.BLACK)
                    day?.setBackgroundColor(Color.parseColor("#FFA1CCF3"))
                    day?.setTypeface(day?.getTypeface(), Typeface.BOLD)
                }

                day?.setGravity(Gravity.CENTER_HORIZONTAL)
            }
            date += 1
            loopCounter++
        }
    }

    fun getMonthDays(month: Int, year: Int): Int {
        val daysInMonth: Int = if (month == 4 || month == 6 || month == 9 || month == 11) {
            30
        } else {
            if (month == 2) {
                if (year % 4 == 0) 29 else 28
            } else {
                31
            }
        }
        return daysInMonth
    }

    fun getMonthStartOfWeek(month: Int, year: Int): String {
        val calender = Calendar.getInstance()
        var dateMon = ""
        if (month.toInt() < 10) {
            dateMon = "0$month"
        }
        else{
            dateMon = "$month"
        }
        val dateStr = "$year-$dateMon-01"
        Log.i("WEEK START DAY", "Date:$dateStr")
        val dt = LocalDate.parse(dateStr)
        return dt.dayOfWeek.toString()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
            activity?.run {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragment_section_mainactivity, ExpenseChartFragment())
                    //.addToBackStack(ExpenseChartFragment().toString())
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MonthWiseViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MonthWiseViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}