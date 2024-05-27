package co.iin.susiddhi.sushishaa_dhansaver.setting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import co.iin.susiddhi.sushishaa_dhansaver.R
import co.iin.susiddhi.sushishaa_dhansaver.database.DataBaseHandler
import co.iin.susiddhi.sushishaa_dhansaver.database.FILTER_MONTHWISE
import co.iin.susiddhi.sushishaa_dhansaver.database.SUSISHAA_CATEGORY_INCOME
import co.iin.susiddhi.sushishaa_dhansaver.database.SUSISHAA_CATEGORY_INVESTMENT
import co.iin.susiddhi.sushishaa_dhansaver.ui.ExpenseChartFragment
import java.time.Year

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SummaryReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SummaryReportFragment : Fragment() {
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
        var view:View =  inflater.inflate(R.layout.fragment_summary_report, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Summary Report"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var spinnerMonth:Spinner = requireView()?.findViewById(R.id.spinnerMonth)
        var spinnerYear:Spinner = requireView()?.findViewById(R.id.spinnerYear)
        var checkBoxMonth:CheckBox = requireView()?.findViewById(R.id.checkBoxMonth)
        var checkBoxYear:CheckBox = requireView()?.findViewById(R.id.checkBoxYear)
        var textviewReport:TextView = requireView()?.findViewById(R.id.textViewReportView)
        var buttonViewReport: Button = requireView()?.findViewById(R.id.buttonViewReport)

        val monthNameList = listOf<String>("January", "February", "March", "April", "May", "June", "July", "August","September", "October", "November", "December")

        val adapterMonth = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, monthNameList) }
        spinnerMonth.adapter = adapterMonth

        var yearList:ArrayList<String> = ArrayList()
        var year = Year.now()
        for(loop in 0..9)
        {
            yearList.add((year?.minusYears(loop.toLong())).toString())
        }
        val adapterYear = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, yearList) }
        spinnerYear.adapter = adapterYear

        checkBoxMonth.setOnClickListener {
            if(checkBoxMonth.isChecked)
            {
                checkBoxYear.isEnabled = false
            }
            else
            {
                checkBoxMonth.isEnabled = true
                checkBoxYear.isEnabled = true
            }
        }
        checkBoxYear.setOnClickListener {
            if(checkBoxYear.isChecked)
            {
                checkBoxMonth.isEnabled = false
            }else
            {
                checkBoxYear.isEnabled = true
                checkBoxMonth.isEnabled = true
            }
        }
        var monthSelected = ""
        var yearSelected = Year.now().toString()
        spinnerMonth.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                monthSelected = monthNameList[position]
                Log.e("MONTH", "Position:  $position, id: $id->> monthSelected:$monthSelected")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.e("MONTH", "No Selection")
            }
        }
        spinnerYear.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                yearSelected = yearList[position]
                Log.e("Year", "Position:  $position, id: $id  ->>yearSelected:$yearSelected")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                Log.e("Year", "No Selection")
            }
        }

        var monthMaxExpenseSpent = 0
        var monthMaxExpenseDay = ""
        var monthMaxCategoryName = ""
        var monthMaxExpenseCategory = 0
        var monthTotalDays = 0
        var monthTotalDaysMoneySpent = 0
        var monthTotalDaysMoneyNoExpense = 0
        var monthAvgExpense = 0
        var monthTotalIncome = 0
        var monthTotalExpnse = 0
        var monthTotalSave = 0
        var monthTotalInvestment = 0

        val yearMaxExpenseSpent = 0
        val yearMaxExpenseMonth = 0
        val yearMaxExpenseCategory = 0
        val yearMaxCategoryName = 0
        val yearAvgExpense = 0
        val yearTotalIncome = 0
        val yearTotalExpnse = 0
        val yearTotalSaving = 0
        val yearTotalExpnsePercent = 0
        val yearTotalInvestmentPercent = 0
        val yearTotalSavingPercent = 0
        val yearTotalInvestment = 0



        buttonViewReport.setOnClickListener {
            if(checkBoxMonth.isChecked)
            {
                var mon_num = monthNameList.indexOf(monthSelected)
                var db = context?.let { it1 -> DataBaseHandler(it1) }
                var dbData = db?.readExpenseData(FILTER_MONTHWISE, mon_num+1, yearSelected.toInt())
                if (dbData != null) {
                    var hasMapCategory:HashMap<String, Int> = HashMap()
                    for(data in dbData) {
                        if(data.rupee > monthMaxExpenseSpent && data.category != SUSISHAA_CATEGORY_INCOME){
                            monthMaxExpenseSpent = data.rupee
                            monthMaxExpenseDay = data.date.split("/")[0].toString()
                        }
                        if(hasMapCategory.containsKey(data.category))
                        {
                            hasMapCategory[data.category] = hasMapCategory[data.category]?.plus(data.rupee)!!
                        }
                        else
                        {
                            hasMapCategory[data.category] = data.rupee
                        }
                        if(data.rupee != 0)
                        {
                            monthTotalDaysMoneySpent++
                        }
                    }
                    for(map in hasMapCategory)
                    {
                        if(monthMaxExpenseCategory < map.value && map.key != SUSISHAA_CATEGORY_INCOME)
                        {
                            monthMaxExpenseCategory = map.value
                            monthMaxCategoryName = map.key

                        }
                        monthAvgExpense += map.value
                        if(map.key == SUSISHAA_CATEGORY_INCOME)
                        {
                            monthTotalIncome += map.value
                        }
                        else{
                            monthTotalExpnse += map.value
                            if(map.key == SUSISHAA_CATEGORY_INVESTMENT)
                            {
                                monthTotalInvestment += map.value
                            }
                        }
                    }
                    monthTotalDays = getMonthDays(mon_num+1, yearSelected.toInt())
                    monthAvgExpense /= monthTotalDays
                    monthTotalSave = monthTotalIncome - monthTotalExpnse
                }
                var monthlyReport = "Showing Report for $monthSelected, $yearSelected:\n"+
                        "\nMaximum Rs.$monthMaxExpenseSpent spent on $monthMaxExpenseDay day of this Month."+
                        "\nThis Month Total Rs.$monthMaxExpenseCategory spent on $monthMaxCategoryName Category." +
                        "\nAverage you spent Rs.$monthAvgExpense per day this month."+
                        "\n-----------------------------"+
                        "\nTotal Income: $monthTotalIncome\nTotal Expense:$monthTotalExpnse\nTotal Save: $monthTotalSave\nTotal Investment: $monthTotalInvestment"

                checkBoxMonth.error = null
                checkBoxYear.error = null
                textviewReport.text = monthlyReport
            }
            else if(checkBoxYear.isChecked)
            {
                var yearlyReport = "Showing Report for $yearSelected Year\n"+
                        "\nMaximum Rs.$yearMaxExpenseSpent on $yearMaxExpenseMonth Month"+
                        "\nThis Year total Rs.$yearMaxExpenseCategory spent on $yearMaxCategoryName" +
                        "\nAverage you spent Rs.$yearAvgExpense per month this year"+
                        "\nTotal Income: $yearTotalIncome Total Expense:$yearTotalExpnse Investment:$yearTotalInvestment Savings:$yearTotalSaving" +
                        "\nExpense:$yearTotalExpnsePercent Investment:$yearTotalInvestmentPercent Savings:$yearTotalSavingPercent"
2
                checkBoxMonth.error = null
                checkBoxYear.error = null
                textviewReport.text = yearlyReport
            }
            else
            {
                checkBoxMonth.error = "Select"
                checkBoxYear.error = "Select"
            }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
            activity?.run {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_section_mainactivity, ExpenseChartFragment())
                    //.addToBackStack(SettingsFragment.toString())
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
         * @return A new instance of fragment SummaryReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SummaryReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}