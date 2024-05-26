package co.iin.susiddhi.sushishaa_dhansaver.database

import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieEntry


//Creating a Data Model class ExpenseClass
class ExpenseClassModel(var id:Int, var date: String, var rupee: Int, var mode: String,
                        var category: String, var sub_category: String, var purpose: String,
                        var user: String, var month: Int, var year: Int, var essential: Int)
{

}


class CategoryClassModel(var id:Int, var category: String, var sub_category: String)
{

}

class ExpenseChartCalulatedData(var dailyExpenseDataMap:HashMap<Int, Int>,
                                var categoryExpenseDataMap:HashMap<String, Int>, var totalCredit:Int, var totalDebit:Int){}

class PiChartClassModel(
    var entriesData: ArrayList<PieEntry>,
    var entriesLegend: MutableList<LegendEntry>,
    var colors: ArrayList<Int>
)
{}