package co.iin.susiddhi.susishaa_dhansaver.ui

import android.graphics.Color
import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import co.iin.susiddhi.susishaa_dhansaver.R
import co.iin.susiddhi.susishaa_dhansaver.database.DataBaseHandler
import co.iin.susiddhi.susishaa_dhansaver.database.ExpenseClassModel
import co.iin.susiddhi.susishaa_dhansaver.database.FILTER_MONTHWISE
import co.iin.susiddhi.susishaa_dhansaver.database.PiChartClassModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import java.time.Year
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExpenseSubCategoryChart.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExpenseSubCategoryChart : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    var globalTotalCategoryDebit = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view:View =  inflater.inflate(R.layout.fragment_expense_sub_category_chart, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Sub Category Details"
        return view
    }
    var firstCallDone = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pieChart = requireView()?.findViewById(R.id.pieChart)
        var spinnerCategory: Spinner = requireView()?.findViewById(R.id.spinnerCategory)

        val bundle = this.arguments
        var categoryRecvd = ""
        var categoryRecvdDate = ""
        var categoryRecvdTotalExpense = ""
        if (bundle != null) {
            // handle your code here.
            categoryRecvd = bundle.getString("PiChartCategory").toString()
            categoryRecvdDate = bundle.getString("PiChartCategoryDate").toString()
            categoryRecvdTotalExpense = bundle.getString("PiChartCategoryTotalRupee").toString()
        }

        var db = context?.let { it1 -> DataBaseHandler(it1) }
        val categoryClassList = db?.readCategoryTable() as ArrayList
        val CategoryList: ArrayList<String> = ArrayList()
        val SubCategoryList: ArrayList<String> = ArrayList()
        val ModeList:List<String> = db?.getModeList()

        for (i in 0 until categoryClassList.size) {
            CategoryList.add(categoryClassList[i].category)
            SubCategoryList.add(categoryClassList[i].sub_category)
        }

        var finalCategorySelected = ""

        val CategoryAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, CategoryList) }
        spinnerCategory.adapter = CategoryAdapter

        spinnerCategory.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                finalCategorySelected = CategoryList[position]
                if(firstCallDone){
                    categoryRecvd = "$finalCategorySelected=0"
                    fillPiChartDetails(categoryRecvd, categoryRecvdDate, "")
                    categoryRecvd = "$finalCategorySelected=${globalTotalCategoryDebit}"
                    categoryRecvdTotalExpense = globalTotalCategoryDebit.toString()
                }
                Log.e("TAG", "finalCategorySelected GET : $finalCategorySelected")
                firstCallDone = true
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        Log.e("TAG", "FINAL GET : $categoryRecvd")
        fillPiChartDetails(categoryRecvd, categoryRecvdDate, categoryRecvdTotalExpense)

        pieChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onNothingSelected() {
                Log.e("Entry selected", "Nothing selected.")
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val calender = Calendar.getInstance()
                var month_number_format = SimpleDateFormat("MM")
                var month_number = month_number_format.format(calender.time).toString().toInt()
                var year = Year.now().toString().toInt()
                val bundle = Bundle()
                bundle.putString("YearFragmentMonthYear", "${month_number},$year,$categoryRecvd, $categoryRecvdTotalExpense") // Put anything what you want
                bundle.putString("piChartViewCallerFrag", ExpenseSubCategoryChart.toString())
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
        })

    }

    private fun fillPiChartDetails(
        categoryRecvd: String,
        categoryRecvdDate: String,
        categoryRecvdTotalExpense: String
    ) {
        if (pieChart != null) {
            pieChart.setUsePercentValues(true)
            pieChart.getDescription().setEnabled(false)
            pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

            // on below line we are setting drag for our pie chart
            pieChart.setDragDecelerationFrictionCoef(0.95f)

            // on below line we are setting hole
            // and hole color for pie chart
            pieChart.setDrawHoleEnabled(true)
            pieChart.setHoleColor(Color.WHITE)

            // on below line we are setting circle color and alpha
            pieChart.setTransparentCircleColor(Color.WHITE)
            pieChart.setTransparentCircleAlpha(110)

            // on below line we are setting hole radius
            pieChart.setHoleRadius(58f)
            pieChart.setTransparentCircleRadius(61f)

            var piData = getSubCategoryDetailsfromDb(categoryRecvd, categoryRecvdDate)
            // on below line we are setting center text
            pieChart.setDrawCenterText(true)
            if(piData.entriesData.size == 0)
            {
                pieChart.centerText = "NO SUBCATEGORY DATA"
            }
            else
            {
                if(categoryRecvdTotalExpense.isEmpty())
                {
                    pieChart.centerText = categoryRecvd.split("=")[0]+"\n("+globalTotalCategoryDebit+")"
                }
                else{
                    pieChart.centerText = categoryRecvd.split("=")[0]+"\n("+categoryRecvdTotalExpense+")"
                }
            }
            pieChart.setCenterTextSize(20f)

            // on below line we are setting
            // rotation for our pie chart
            pieChart.setRotationAngle(0f)

            // enable rotation of the pieChart by touch
            pieChart.setRotationEnabled(true)
            pieChart.setHighlightPerTapEnabled(true)

            // on below line we are setting animation for our pie chart
            pieChart.animateY(1400, Easing.EaseInOutQuad)

            // on below line we are disabling our legend for pie chart
            pieChart.legend.isEnabled = true
            pieChart.setEntryLabelColor(Color.WHITE)
            pieChart.setEntryLabelTextSize(12f)
            pieChart.legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
            pieChart.legend.setTextSize(14f)
            pieChart.legend.setForm(Legend.LegendForm.CIRCLE)
            pieChart.legend.setWordWrapEnabled(true)
            // on below line we are creating array list and
            // adding data to it to display in pie chart
            // on below line we are setting pie data set
            val dataSet = PieDataSet(piData.entriesData, "Expenses Details")
            pieChart.legend.setCustom(piData.entriesLegend)

            // on below line we are setting icons.
            dataSet.setDrawIcons(false)

            // on below line we are setting slice for pie
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f

            // on below line we are setting colors.
            dataSet.colors = piData.colors

            // on below line we are setting pie data set
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(15f)
            data.setValueTypeface(Typeface.DEFAULT_BOLD)
            data.setValueTextColor(Color.WHITE)
            pieChart.setData(data)

            // undo all highlights
            pieChart.highlightValues(null)

            // loading chart
            pieChart.invalidate()
        }
    }

    private fun getSubCategoryDetailsfromDb(categoryRecvd: String, categoryRecvdDate: String): PiChartClassModel {
        globalTotalCategoryDebit = 0
        val entriesData: ArrayList<PieEntry> = ArrayList()
        val colors: ArrayList<Int> = ArrayList()
        val entriesLegend: MutableList<LegendEntry> = ArrayList()

        var month = categoryRecvdDate.split(",")[0].toInt()
        var year = categoryRecvdDate.split(",")[1].toInt()
        val db = context?.let { DataBaseHandler(it) }
        val calcData: MutableList<ExpenseClassModel>? = db?.readExpenseData(FILTER_MONTHWISE, month+1, year)
        var hashMap:HashMap<String,Int> = HashMap()
        var categoryTofind = categoryRecvd.split("=")[0]
        var categoryTotalDebit = categoryRecvd.split("=")[1].toInt()
        Log.w("getSubCategoryDetailsfromDb", "getSubCategoryDetailsfromDb->categoryRecvd:${categoryRecvd},  categoryRecvdDate:${categoryRecvdDate}")
        if (calcData != null) {
            for (loop in calcData) {
                if(loop.category == categoryTofind)
                {
                    if(hashMap.containsKey(loop.sub_category))
                    {
                        hashMap[loop.sub_category] = hashMap[loop.sub_category]?.plus(loop.rupee)!!
                    }
                    else
                    {
                        hashMap[loop.sub_category] = loop.rupee
                    }
                    globalTotalCategoryDebit += loop.rupee
                }
            }
            Log.i("asdf",  "categoryTotalDebit:${categoryTotalDebit}, globalTotalCategoryDebit:${globalTotalCategoryDebit}")
            if(categoryTotalDebit == 0) {
                categoryTotalDebit = globalTotalCategoryDebit
            }

        }
        for(map in hashMap)
        {
            Log.i("TAG", "(${map.value}/${categoryTotalDebit})*100")
            var categoryFloat = (map.value.toFloat() / categoryTotalDebit.toFloat()) * 100
            Log.i("TAG", "${map.key}: $categoryFloat")
            entriesData.add(PieEntry(categoryFloat))
            val entry = LegendEntry()
            val rnd = Random()
            var color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            entry.formColor = color
            entry.label = map.key + "(${map.value}, ${categoryFloat.roundToInt()}%)"
            entriesLegend.add(entry)
            // add a lot of colors to list
            colors.add(color)
        }
        return PiChartClassModel(entriesData, entriesLegend, colors)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.e("CHART SUBCATEGORY", "ITEM BACK BTN")
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
         * @return A new instance of fragment ExpenseSubCategoryChart.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExpenseSubCategoryChart().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}