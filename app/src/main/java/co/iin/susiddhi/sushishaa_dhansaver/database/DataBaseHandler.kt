package co.iin.susiddhi.sushishaa_dhansaver.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

const val FILTER_NONE = 0
const val FILTER_MONTHWISE = 1
const val FILTER_YEARWISE = 2
const val EXPENSE_ESSENTIALS = 1
const val EXPENSE_NON_ESSENTIALS = 2
const val SUSISHAA_DATABASENAME = "SUSIDDHI_DATABASE"

const val SUSISHAA_TABLENAME_CATEGORY = "SUSISHAA_CATEGORY"
const val SUSISHAA_COL_NAME_CATEGORY = "CATEGORY"
const val SUSISHAA_COL_NAME_SUB_CATEGORY = "SUB_CATEGORY"

const val SUSISHAA_CATEGORY_MAINTENANCE = "MAINTENANCE"
const val SUSISHAA_CATEGORY_TRAVEL = "TRAVEL"
const val SUSISHAA_CATEGORY_FOOD = "FOOD"
const val SUSISHAA_CATEGORY_GROCERIES = "GROCERIES"
const val SUSISHAA_CATEGORY_INVESTMENT = "INVESTMENT"
const val SUSISHAA_CATEGORY_EMI = "EMI"
const val SUSISHAA_CATEGORY_EDUCATION = "EDUCATION"
const val SUSISHAA_CATEGORY_GENERAL = "GENERAL"
const val SUSISHAA_CATEGORY_SAVING = "SAVING"
const val SUSISHAA_CATEGORY_DEBT_LOAN = "DEBT_LOAN"
const val SUSISHAA_CATEGORY_INCOME = "INCOME"

const val COL_ID = "id"
const val SUSISHAA_TABLENAME_EXPENSE = "SUSISHAA_EXPENSE"
const val SUSISHAA_TABLENAME_EXPENSE_COL_NAME_DATE = "DATE"
const val SUSISHAA_TABLENAME_EXPENSE_COL_NAME_RUPEE = "RUPEE"
const val SUSISHAA_TABLENAME_EXPENSE_COL_NAME_MODE = "MODE"
const val SUSISHAA_TABLENAME_EXPENSE_COL_NAME_PURPOSE = "PURPOSE"
const val SUSISHAA_TABLENAME_EXPENSE_COL_NAME_CATEGORY = "CATEGORY"
const val SUSISHAA_TABLENAME_EXPENSE_COL_NAME_SUB_CATEGORY = "SUB_CATEGORY"
const val SUSISHAA_TABLENAME_EXPENSE_COL_NAME_USER = "USER"
const val SUSISHAA_TABLENAME_EXPENSE_COL_NAME_YEAR = "YEAR"
const val SUSISHAA_TABLENAME_EXPENSE_COL_NAME_MONTH = "MONTH"
const val SUSISHAA_TABLENAME_EXPENSE_COL_NAME_ESSENTIAL = "ESSENTIAL"

//Sub Category List

val sub_category_maintenance = listOf("HOME", "CAR", "2_WHEELER")
val sub_category_mode = listOf("UPI", "CREDIT_CARD", "DEBIT_CARD", "INTERNET_BANKING")
val sub_category_travel = listOf("AUTO", "2_WHEELER", "4_WHEELER", "AIR", "TAXI", "TRAIN")
val sub_category_food = listOf("ZOMATO", "SWIGGY", "MESH", "MILK/CURD", "BREAD", "BAKERY", "RESTAURANT")
val sub_category_groceries = listOf("GENERAL", "VEGETABLES", "FRUITS", "MIX", "ONLINE", "AMAZON", "FLIPKART")
val sub_category_investments = listOf("MUTUAL_FUND", "STOCKS", "NPS", "PPF", "SUKANAYA", "RD", "FD", "LIC", "OTHERS")
val sub_category_emi = listOf("HOME", "PERSONAL", "CAR", "AMAZON", "CARD")
val sub_category_education = listOf("SCHOOL_FEE", "BOOKS", "PEN/PAPER", "COPY", "OTHERS")
val sub_category_general = listOf("ELECTRICITY", "MOBILE_RECHARGE", "PETROL", "BADMINTON", "COSMETICS", "GIFTS",
    "HOSPITAL", "OTT", "BROADBAND", "BEVERAGES", "SMOKE", "GYM", "OTHERS")
val sub_category_saving = listOf("BANK", "SBI_BANK", "KOTAK_BANK", "HDFC_BANK", "IDFC_BANK")
val sub_category_debt_loan = listOf("BORROW", "LEND")
val sub_category_income = listOf("SALARY", "GIFT_CASH", "GIFT_ONLINE", "STOCKS_SELL", "ASSEST_SELL", "MF_SELL", "TRADING", "OTHERS")

val categoriesMap = mapOf("INCOME" to sub_category_income, "DEBT_LOAN" to sub_category_debt_loan,"SAVING" to sub_category_saving , "GENERAL" to sub_category_general,"EDUCATION" to sub_category_education,"EMI" to sub_category_emi, "INVESTMENTS" to sub_category_investments,"GROCERIES" to sub_category_groceries,"FOOD" to sub_category_food, "MAINTENANCE" to sub_category_maintenance, "MODE" to sub_category_mode, "TRAVEL" to sub_category_travel)

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, SUSISHAA_DATABASENAME, null,
    1) {
    override fun onCreate(db: SQLiteDatabase?) {
        //val createTable = "CREATE TABLE " + SUSISHAA_TABLENAME_CATEGORY + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SUSISHAA_CATEGORY_COL_NAME + " VARCHAR(256)," + COL_AGE + " INTEGER)"
        val createTableCategory = "CREATE TABLE $SUSISHAA_TABLENAME_CATEGORY ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,$SUSISHAA_COL_NAME_CATEGORY VARCHAR(256), $SUSISHAA_COL_NAME_SUB_CATEGORY VARCHAR(256))"
        db?.execSQL(createTableCategory)
        val createTableExpense = "CREATE TABLE $SUSISHAA_TABLENAME_EXPENSE ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$SUSISHAA_TABLENAME_EXPENSE_COL_NAME_DATE VARCHAR(256), $SUSISHAA_TABLENAME_EXPENSE_COL_NAME_RUPEE INTEGER, " +
                "$SUSISHAA_TABLENAME_EXPENSE_COL_NAME_MODE VARCHAR(256), $SUSISHAA_TABLENAME_EXPENSE_COL_NAME_PURPOSE VARCHAR(256), " +
                "$SUSISHAA_TABLENAME_EXPENSE_COL_NAME_CATEGORY VARCHAR(256), $SUSISHAA_TABLENAME_EXPENSE_COL_NAME_SUB_CATEGORY VARCHAR(256)," +
                "$SUSISHAA_TABLENAME_EXPENSE_COL_NAME_USER VARCHAR(256), $SUSISHAA_TABLENAME_EXPENSE_COL_NAME_MONTH VARCHAR(256)," +
                "$SUSISHAA_TABLENAME_EXPENSE_COL_NAME_YEAR VARCHAR(256), $SUSISHAA_TABLENAME_EXPENSE_COL_NAME_ESSENTIAL INTEGER)"
        db?.execSQL(createTableExpense)
        Log.e("TAG", "Expense and Category Table Created")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        //onCreate(db);
    }
    fun getModeList(): List<String> {
        return sub_category_mode
    }

    fun populateCategoryTable() {
        fillCategoryDatabase(sub_category_maintenance, SUSISHAA_CATEGORY_MAINTENANCE);
        //fillCategoryDatabase(sub_category_mode, "MODE");
        fillCategoryDatabase(sub_category_travel, SUSISHAA_CATEGORY_TRAVEL);
        fillCategoryDatabase(sub_category_food, SUSISHAA_CATEGORY_FOOD);
        fillCategoryDatabase(sub_category_groceries, SUSISHAA_CATEGORY_GROCERIES);
        fillCategoryDatabase(sub_category_investments, SUSISHAA_CATEGORY_INVESTMENT);
        fillCategoryDatabase(sub_category_emi, SUSISHAA_CATEGORY_EMI);
        fillCategoryDatabase(sub_category_education, SUSISHAA_CATEGORY_EDUCATION);
        fillCategoryDatabase(sub_category_general, SUSISHAA_CATEGORY_GENERAL);
        fillCategoryDatabase(sub_category_saving, SUSISHAA_CATEGORY_SAVING);
        fillCategoryDatabase(sub_category_debt_loan, SUSISHAA_CATEGORY_DEBT_LOAN);
        fillCategoryDatabase(sub_category_income, SUSISHAA_CATEGORY_INCOME);
        Log.e("TAG", "Category Table Populate")
    }

    private fun fillCategoryDatabase(subCategoryMaintenance: List<String>, category: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        var stringSubCate = ""
        for (i in 0 until subCategoryMaintenance.size) {
            if(i == 0)
            {
                stringSubCate += subCategoryMaintenance[i]
            }
            else{
                stringSubCate += ","+subCategoryMaintenance[i]
            }
        }
        contentValues.put(SUSISHAA_COL_NAME_CATEGORY, category)
        contentValues.put(SUSISHAA_COL_NAME_SUB_CATEGORY, stringSubCate)
        val result = database.insert(SUSISHAA_TABLENAME_CATEGORY, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Category Database filled", Toast.LENGTH_SHORT).show()
        }
        database.close()
    }

    fun insertNewCategory(newCategory: String): Long {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(SUSISHAA_COL_NAME_CATEGORY, newCategory)
        val result = database.insert(SUSISHAA_TABLENAME_CATEGORY, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "New Category $newCategory Added", Toast.LENGTH_SHORT).show()
        }
        database.close()
        //readCategoryTable()
        return result
    }

    fun updateNewSubCategory(newSubCategory: String, category: String):Int{
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(SUSISHAA_COL_NAME_SUB_CATEGORY, newSubCategory)
        //contentValues.put(SUSISHAA_COL_NAME_CATEGORY, category)
        // Updating Row
        Log.i("UPDATE", "Category:$category SubCategory:$newSubCategory")
        val success = database.update(SUSISHAA_TABLENAME_CATEGORY, contentValues,
            "$SUSISHAA_COL_NAME_CATEGORY='$category'",null)
        //2nd argument is String containing nullColumnHack
        if (success == 0) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            var sub = newSubCategory.split(",")
            Toast.makeText(context, "New Sub Category "+sub[sub.size-1]+" Added", Toast.LENGTH_SHORT).show()
        }
        database.close()
        //readCategoryTable()
        return success
    }

    fun readCategoryTable(): ArrayList<CategoryClassModel> {
        val db = this.readableDatabase
        val query = "Select * from $SUSISHAA_TABLENAME_CATEGORY"
        val result = db.rawQuery(query, null)
        val array: ArrayList<CategoryClassModel> = ArrayList()
        if (result.moveToFirst()) {
            do {
                var id = result.getInt(result.getColumnIndexOrThrow(COL_ID))
                val category = result.getString(result.getColumnIndexOrThrow(SUSISHAA_COL_NAME_CATEGORY))
                var subcategory = result.getString(result.getColumnIndexOrThrow(SUSISHAA_COL_NAME_SUB_CATEGORY))
                if(subcategory.isNullOrBlank())
                {
                    subcategory = ""
                }
                Log.i("CATEGORY", "Id:$id category:$category SubCategory:$subcategory")
                array.add(CategoryClassModel(id, category, subcategory))
            }
            while (result.moveToNext())
        }
        return array
    }


    fun insertExpenseData(expense: ExpenseClassModel): Long {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_DATE, expense.date)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_RUPEE, expense.rupee)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_MODE, expense.mode)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_PURPOSE, expense.purpose)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_CATEGORY, expense.category)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_SUB_CATEGORY, expense.sub_category)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_USER, expense.user)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_MONTH, expense.month)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_YEAR, expense.year)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_ESSENTIAL, expense.essential)


        val result = database.insert(SUSISHAA_TABLENAME_EXPENSE, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Expense Added", Toast.LENGTH_SHORT).show()
        }
        database.close()
        return result
    }
    fun readExpenseData(filterBy: Int, filterValue: Int, filterSubValue: Int): MutableList<ExpenseClassModel> {
        val expenseList: MutableList<ExpenseClassModel> = ArrayList()
        val db = this.readableDatabase
        var filterString = ""
        if(filterBy == FILTER_MONTHWISE)
        {
            filterString = "where $SUSISHAA_TABLENAME_EXPENSE_COL_NAME_MONTH = $filterValue and $SUSISHAA_TABLENAME_EXPENSE_COL_NAME_YEAR = $filterSubValue"
        }
        if(filterBy == FILTER_YEARWISE)
        {
            filterString = "where $SUSISHAA_TABLENAME_EXPENSE_COL_NAME_YEAR = $filterValue"
        }
        val query = "Select * from $SUSISHAA_TABLENAME_EXPENSE $filterString"
        Log.i("READ_DB", "READ Query:$query")
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var id = result.getInt(result.getColumnIndexOrThrow(COL_ID))
                val date = result.getString(result.getColumnIndexOrThrow(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_DATE))
                val rupee = result.getInt(result.getColumnIndexOrThrow(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_RUPEE))
                val mode = result.getString(result.getColumnIndexOrThrow(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_MODE))
                val purpose = result.getString(result.getColumnIndexOrThrow(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_PURPOSE))
                val category = result.getString(result.getColumnIndexOrThrow(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_CATEGORY))
                val subCategory = result.getString(result.getColumnIndexOrThrow(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_SUB_CATEGORY))
                val user = result.getString(result.getColumnIndexOrThrow(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_USER))
                val month = result.getInt(result.getColumnIndexOrThrow(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_MONTH))
                val year = result.getInt(result.getColumnIndexOrThrow(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_YEAR))
                val essential = result.getInt(result.getColumnIndexOrThrow(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_ESSENTIAL))
                val singleExpense = ExpenseClassModel(id, date, rupee, mode, category, subCategory,purpose, user, month, year, essential)
                expenseList.add(singleExpense)
                Log.i("DB_READ", "$id, $date, $rupee, $mode, $purpose, $category, $subCategory, $user, $month, $year $essential")
            }
            while (result.moveToNext())
        }
        return expenseList
    }//ReadExpenseData

    //method to update data
    fun updateExpenseData(expense: ExpenseClassModel):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID, expense.id)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_DATE, expense.date)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_RUPEE, expense.rupee)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_MODE, expense.mode)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_PURPOSE, expense.purpose)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_CATEGORY, expense.category)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_SUB_CATEGORY, expense.sub_category)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_USER, expense.user)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_MONTH, expense.month)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_YEAR, expense.year)
        contentValues.put(SUSISHAA_TABLENAME_EXPENSE_COL_NAME_ESSENTIAL, expense.essential)

        // Updating Row
        val success = db.update(SUSISHAA_TABLENAME_EXPENSE, contentValues,"id="+expense.id,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteExpenseData(expense: ExpenseClassModel):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID, expense.id) // expense.id
        // Deleting Row
        val success = db.delete(SUSISHAA_TABLENAME_EXPENSE,"id="+expense.id,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    fun calculateDateForPieChart(filterType:Int, filterValue:Int, filterSubValue: Int): ExpenseChartCalulatedData {
        val db = context?.let { DataBaseHandler(it) }
        // Here, we have created new array list and added data to it
        val courseModelArrayList: ArrayList<ExpenseClassModel> = db?.readExpenseData(filterType, filterValue, filterSubValue) as ArrayList<ExpenseClassModel>
        var totalCredit = 0
        var totalDebit = 0
        var totalLeft = 0
        Log.i("TAG", "filterType: $filterType value:$filterValue")
        val dailyWiseExpense = HashMap<Int, Int>()
        val categoryBasedMap = HashMap<String, Int>()

        for (i in 0 until courseModelArrayList.size) {
            var cate = courseModelArrayList[i].category
            if(cate == SUSISHAA_CATEGORY_INCOME)
            {
                totalCredit += courseModelArrayList[i].rupee
            }
            else
            {
                totalDebit += courseModelArrayList[i].rupee

                if(categoryBasedMap.containsKey(cate)) {
                    categoryBasedMap[cate] = categoryBasedMap[cate]?.plus(courseModelArrayList[i].rupee)!!
                }else
                {
                    categoryBasedMap[cate] = courseModelArrayList[i].rupee
                }
                Log.i("DAILY_EXPENSE", "categoryBasedMap[$cate]: ${categoryBasedMap[cate]}")

                if(filterType == FILTER_MONTHWISE)
                {
                    var day = courseModelArrayList[i].date.split(" ")[0].split("/")[0].toString().toInt()
                    if(dailyWiseExpense.containsKey(day)) {
                        dailyWiseExpense[day] =
                            dailyWiseExpense[day]?.plus(courseModelArrayList[i].rupee)!!
                    }else
                    {
                        dailyWiseExpense[day] = courseModelArrayList[i].rupee
                    }
                    Log.i("DAILY_EXPENSE", "dailyWise[$day]: ${dailyWiseExpense[day]}")
                }
                else if(filterType == FILTER_YEARWISE)
                {
                    var month = courseModelArrayList[i].month
                    if(dailyWiseExpense.containsKey(month)) {
                        dailyWiseExpense[month] =
                            dailyWiseExpense[month]?.plus(courseModelArrayList[i].rupee)!!
                    }else
                    {
                        dailyWiseExpense[month] = courseModelArrayList[i].rupee
                    }
                    Log.i("DAILY_EXPENSE", "dailyWise[$month]: ${dailyWiseExpense[month]}")
                }
            }
        }
        totalLeft = totalCredit-totalDebit

        return ExpenseChartCalulatedData(dailyWiseExpense, categoryBasedMap, totalCredit, totalDebit)
    }
}