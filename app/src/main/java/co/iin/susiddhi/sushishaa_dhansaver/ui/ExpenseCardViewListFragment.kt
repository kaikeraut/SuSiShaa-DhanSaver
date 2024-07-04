package co.iin.susiddhi.susishaa_dhansaver.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.iin.susiddhi.susishaa_dhansaver.R
import co.iin.susiddhi.susishaa_dhansaver.adapter.ExpenseCardViewListAdapter
import co.iin.susiddhi.susishaa_dhansaver.database.*
import java.util.Arrays.toString

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExpenseCardViewListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ExpenseCardViewListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var callerFunction = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

   //private lateinit var interfaceAdapter:InterfaceAdapterViewClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view:View =  inflater.inflate(R.layout.fragment_expense_card_view_list, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Monthly View"
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var courseModelArrayList: ArrayList<ExpenseClassModel> = ArrayList()
        val db = context?.let { DataBaseHandler(it) }
        val bundle = this.arguments
        var recvdDay  = 0
        var recvdMonth  = 0
        var recvdYear  = 0
        var dailyView = false
        if (bundle != null) {
            // handle your code here.
            var recvd:String = ""
            recvd = bundle.getString("YearFragmentMonthYear").toString()
            callerFunction = bundle.getString("piChartViewCallerFrag").toString()
            Log.e("YearFragmentMonthYear", "recvd $recvd")
            if(recvd != null) {
                var list = recvd.split(",")
                if(list.size == 2) {
                    recvdMonth = list[0].toString().toInt()
                    recvdYear = list[1].toString().toInt()
                    Log.w("YearFragmentMonthYear", "Month:$recvdMonth, Year: $recvdYear")
                    courseModelArrayList = db?.readExpenseData(FILTER_MONTHWISE, recvdMonth, recvdYear) as ArrayList<ExpenseClassModel>
                }
                else if(list.size == 3)
                {
                    var dayStr = list[0].toString().split("\n")
                    recvdDay = dayStr[0].toString().trim().toInt()
                    recvdMonth = list[1].toString().toInt()
                    recvdYear = list[2].toString().toInt()
                    Log.w("MonthFragmentMonthYear", "Day:$dayStr, Month:$recvdMonth, Year: $recvdYear")
                    dailyView = true
                    courseModelArrayList = db?.readExpenseData(FILTER_MONTHWISE, recvdMonth, recvdYear) as ArrayList<ExpenseClassModel>
                }
                else if(list.size == 4)
                {
                    //"${month_number},$year,$categoryRecvd, $categoryRecvdTotalExpense"
                    recvdMonth = list[0].toString().toInt()
                    recvdYear = list[1].toString().toInt()
                    var category = list[2].split("=")[0]
                    Log.w("CategoryView", "Month:$recvdMonth, Year: $recvdYear Category:$category")
                    dailyView = false
                    courseModelArrayList = db?.readMonthCategoryBasedData(FILTER_MONTHWISE, recvdMonth, recvdYear, category) as ArrayList<ExpenseClassModel>
                }
            }
        }

        //val courseRV = requireView()?.findViewById(R.id.recyclerViewPlaceholder)
        val recycleView = view.findViewById<RecyclerView>(R.id.recyclerViewPlaceholder)

        // Here, we have created new array list and added data to it


        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recycleView.layoutManager = linearLayoutManager

        var courseModelArrayListNew: ArrayList<ExpenseClassModel> = ArrayList()
        if(dailyView)
        {
            for(data in courseModelArrayList)
            {
                var dataDay = data.date.split("/")[0].toString().trim().toInt()
                if(dataDay == recvdDay)
                {
                    courseModelArrayListNew.add(data)
                }
            }
            if(courseModelArrayListNew.size == 0)
            {
                courseModelArrayListNew.add(ExpenseClassModel(0, "",0,"","","",
                    "NO EXPENSE AVAILABLE", " ", 0, 0, 0))
            }
            var courseAdapter = context?.let {
                ExpenseCardViewListAdapter(it, courseModelArrayListNew){ item->
                    onListItemClick(item)
                }
            }
            recycleView.adapter = courseAdapter
        }
        else {
            if(courseModelArrayList.size == 0)
            {
                courseModelArrayList.add(ExpenseClassModel(0, "",0,"","","",
                    "NO EXPENSE AVAILABLE", " ", 0, 0, 0))
            }
            // we are initializing our adapter class and passing our arraylist to it.
             var courseAdapter = context?.let {
                 ExpenseCardViewListAdapter(it, courseModelArrayList){ item->
                     onListItemClick(item)
                 }
             }
            recycleView.adapter = courseAdapter

        }
        if(callerFunction == ExpenseSubCategoryChart.toString())
        {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = "SubCategory View"
        }

    }

    fun onListItemClick(item:ExpenseClassModel){
        //Toast.makeText(context, "Clicked $item", Toast.LENGTH_SHORT).show()
        if(item.date.length == 0 && item.category.length == 0)
        {
            return
        }
        val bundle = Bundle()
        bundle.putString("AddExpenseEditDeleteRequest", "${item.id}~${item.date}~${item.rupee}~${item.mode}~${item.category}~${item.sub_category}~${item.purpose}~${item.user}~${item.month}~${item.year}~${item.essential}") // Put anything what you want
        var fragment = EditExpenseFragment()
        fragment.arguments = bundle
        activity?.run {
            supportFragmentManager.beginTransaction().replace(
                R.id.main_fragment_section_mainactivity,
                fragment
            ).addToBackStack("EditExpenseFragment").commit()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
            if(callerFunction == ExpenseSubCategoryChart.toString())
            {
                activity?.run {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_section_mainactivity, ExpenseChartFragment())
                        //.addToBackStack(ExpenseChartFragment().toString())
                        .commit()
                }
            }else {
                activity?.run {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_section_mainactivity, MonthWiseViewFragment())
                        //.addToBackStack(ExpenseChartFragment().toString())
                        .commit()
                }
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
         * @return A new instance of fragment ExpenseCardViewListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExpenseCardViewListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

        fun onAdapterItemClickListener(data: ExpenseClassModel) {
        Log.e("AdapterClick", "${data.category}, ${data.sub_category} ${data.rupee}")
    }
}