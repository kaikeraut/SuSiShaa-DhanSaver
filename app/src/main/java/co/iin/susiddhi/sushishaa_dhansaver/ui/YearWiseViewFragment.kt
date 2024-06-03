package co.iin.susiddhi.sushishaa_dhansaver.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import co.iin.susiddhi.sushishaa_dhansaver.R
import co.iin.susiddhi.sushishaa_dhansaver.database.DataBaseHandler
import co.iin.susiddhi.sushishaa_dhansaver.database.ExpenseChartCalulatedData
import co.iin.susiddhi.sushishaa_dhansaver.database.FILTER_YEARWISE
import java.time.Year
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [YearWiseViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YearWiseViewFragment : Fragment() {
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
        var view:View = inflater.inflate(R.layout.fragment_year_wise_view, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Yearly View"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var year = Year.now().toString().toInt()

        val monthNameList = listOf<String>("January", "February", "March", "April", "May", "June", "July", "August","September", "October", "November", "December")
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

        for(day in 1..12)
        {
            var textView: TextView = requireView()?.findViewById(textViewRId[day-1])
            textViewCalList.add(textView)
            Log.i("TAG", "textView$day ->> $textView")
            textView.text = monthNameList[day-1]
            textView?.setTypeface(textView?.getTypeface(), Typeface.BOLD)
            textView?.setGravity(Gravity.CENTER_HORIZONTAL)
            textView.setTextSize(24f)
            textView.setTextColor(Color.BLACK)
            //textView?.setBackgroundColor(Color.parseColor("#FFA1CCF3"))
            context?.getColor(R.color.yearTextViewColor)?.let { textView?.setBackgroundColor(it) }

            textView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("YearFragmentMonthYear", "${day},$year") // Put anything what you want
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
        }

        var btnPrev: Button = requireView()?.findViewById(R.id.buttonPrev)
        var btnNext: Button = requireView()?.findViewById(R.id.buttonNext)
        var btnDate: Button = requireView()?.findViewById(R.id.buttonDate)

        //Center Button Text
        btnDate.text = "$year"

        btnPrev.setOnClickListener {
            year -= 1
            btnDate.text = "$year"
            populateYearlyData(year, textViewCalList, monthNameList)
        }
        btnNext.setOnClickListener {
            year += 1
            btnDate.text = "$year"
            populateYearlyData(year, textViewCalList, monthNameList)
        }
        btnDate.setOnClickListener {
            year = Year.now().toString().toInt()
            btnDate.text = "$year"
            populateYearlyData(year, textViewCalList, monthNameList)
        }

        populateYearlyData(year, textViewCalList, monthNameList)

    }//onViewCreated

    private fun populateYearlyData(
        year: Int,
        textViewCalList: ArrayList<TextView>,
        monthNameList: List<String>
    ) {
        val db = context?.let { DataBaseHandler(it) }
        val calcData: ExpenseChartCalulatedData? = db?.calculateDateForPieChart(FILTER_YEARWISE, year, year)
        var month = 0
        for(textView in textViewCalList)
        {
            var monthlyExpense = 0;
            if(calcData?.dailyExpenseDataMap?.containsKey(month+1) == true)
            {
                monthlyExpense = calcData?.dailyExpenseDataMap[month+1]!!
            }
            textView.text = "${monthNameList[month]}\n\n${monthlyExpense}"
            textView?.setTypeface(textView?.getTypeface(), Typeface.BOLD)
            textView?.setGravity(Gravity.CENTER_HORIZONTAL)
            textView.setTextSize(24f)
            textView.setTextColor(Color.BLACK)
            textView?.setBackgroundColor(Color.parseColor("#FFA1CCF3"))
            month += 1
        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment YearWiseViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            YearWiseViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
            activity?.run {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragment_section_mainactivity, MonthWiseViewFragment())
                    //.addToBackStack(ExpenseChartFragment().toString())
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}