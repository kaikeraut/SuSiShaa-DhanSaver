package co.iin.susiddhi.sushishaa_dhansaver

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import co.iin.susiddhi.sushishaa_dhansaver.database.DataBaseHandler
import co.iin.susiddhi.sushishaa_dhansaver.ui.AddExpenseFragment
import co.iin.susiddhi.sushishaa_dhansaver.ui.ExpenseChartFragment
import co.iin.susiddhi.sushishaa_dhansaver.ui.MonthWiseViewFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sp: SharedPreferences = getSharedPreferences("SUSISHAA_DHANSAVER", MODE_PRIVATE)
        if(sp.getString("DATABASE_POPULATED", null) == "yes")
        {
            //ignore
        }
        else
        {
            var db = DataBaseHandler(this)
            db.populateCategoryTable()
            val Ed = sp.edit()
            Ed.putString("DATABASE_POPULATED", "yes")
            Ed.commit()
        }

        var bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHomeView -> {
                    replaceFragment(ExpenseChartFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.menuAddExpense ->{
                    replaceFragment(AddExpenseFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.menuCalenderView ->{
                    replaceFragment(MonthWiseViewFragment())
                    return@setOnItemSelectedListener true
                }
                else -> {false}
            }
        }
        bottomNav.setSelectedItemId(R.id.menuHomeView)
    }//OnCreate

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment_section_mainactivity, fragment)
        transaction.commit()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.menu_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }

}