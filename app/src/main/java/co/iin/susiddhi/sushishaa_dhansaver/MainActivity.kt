package co.iin.susiddhi.sushishaa_dhansaver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import co.iin.susiddhi.sushishaa_dhansaver.ui.AddExpenseFragment
import co.iin.susiddhi.sushishaa_dhansaver.ui.ExpenseChartFragment
import co.iin.susiddhi.sushishaa_dhansaver.ui.MonthWiseViewFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment_section_mainactivity, fragment)
        transaction.commit()
    }
}