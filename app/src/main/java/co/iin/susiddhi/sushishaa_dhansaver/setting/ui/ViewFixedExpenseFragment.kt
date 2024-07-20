package co.iin.susiddhi.sushishaa_dhansaver.setting.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.iin.susiddhi.susishaa_dhansaver.R
import co.iin.susiddhi.susishaa_dhansaver.adapter.ExpenseCardViewListAdapter
import co.iin.susiddhi.susishaa_dhansaver.adapter.FixedExpenseCardViewListAdapter
import co.iin.susiddhi.susishaa_dhansaver.database.DataBaseHandler
import co.iin.susiddhi.susishaa_dhansaver.database.FixedExpenseClassModel
import co.iin.susiddhi.susishaa_dhansaver.database.FILTER_MONTHWISE
import co.iin.susiddhi.susishaa_dhansaver.setting.SettingsFragment
import co.iin.susiddhi.susishaa_dhansaver.ui.EditExpenseFragment
import co.iin.susiddhi.susishaa_dhansaver.ui.ExpenseChartFragment
import co.iin.susiddhi.susishaa_dhansaver.ui.ExpenseSubCategoryChart
import co.iin.susiddhi.susishaa_dhansaver.ui.MonthWiseViewFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewFixedExpenseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewFixedExpenseFragment : Fragment() {
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
        var view:View =  inflater.inflate(R.layout.fragment_view_fixed_expense, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "View Fixed Expense"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var courseModelArrayList: ArrayList<FixedExpenseClassModel> = ArrayList()
        val db = context?.let { DataBaseHandler(it) }

        //val courseRV = requireView()?.findViewById(R.id.recyclerViewPlaceholder)
        val recycleView = view.findViewById<RecyclerView>(R.id.recyclerViewPlaceholder)

        // Here, we have created new array list and added data to it


        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // in below two lines we are setting layout manager and adapter to our recycler view.
        recycleView.layoutManager = linearLayoutManager

        var courseModelArrayListNew: ArrayList<FixedExpenseClassModel> = ArrayList()

        var fixedList = db?.readFixedExpenseData()
        if (fixedList == null) {
            courseModelArrayListNew.add(FixedExpenseClassModel(1, 0, "","","", "","","NO RECORD FOUND"))
        }
        else{
            courseModelArrayListNew = fixedList as ArrayList<FixedExpenseClassModel>
        }
        var courseAdapter = context?.let {
            FixedExpenseCardViewListAdapter(it, courseModelArrayListNew){ item->
                onListItemClick(item)
            }
        }
        recycleView.adapter = courseAdapter
    }

    fun onListItemClick(item: FixedExpenseClassModel){
        //Toast.makeText(context, "Clicked $item", Toast.LENGTH_SHORT).show()
        if(item.enddate.length == 0 && item.category.isEmpty())
        {
            return
        }
        val bundle = Bundle()
        bundle.putString("FixedExpenseEditDeleteRequest", "${item.id}~${item.expense}~${item.occurrence}~${item.sub_occurrence}~${item.category}~${item.sub_category}~${item.enddate}~${item.comments}") // Put anything what you want
        var fragment = UpdateFixedExpenseFragment()
        fragment.arguments = bundle
        activity?.run {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragmentContainerViewSetting,
                fragment
            ).addToBackStack("UpdateFixedExpenseFragment").commit()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
            activity?.run {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewSetting, SettingsFragment())
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
         * @return A new instance of fragment ViewFixedExpenseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewFixedExpenseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}