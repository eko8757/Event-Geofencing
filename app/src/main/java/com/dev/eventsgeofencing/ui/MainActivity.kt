@file:Suppress("DEPRECATION")

package com.dev.eventsgeofencing.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.eventsgeofencing.R
import com.dev.eventsgeofencing.adapter.EventsAdapter
import com.dev.eventsgeofencing.model.response.ResponseEvents
import com.dev.eventsgeofencing.presenter.EventsPresenter
import com.dev.eventsgeofencing.services.BaseApi
import com.dev.eventsgeofencing.utils.StaticString
import com.dev.eventsgeofencing.view.EventsView
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), EventsView.EventsList {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var presenter: EventsPresenter
    private lateinit var adapter: EventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val factory: BaseApi = BaseApi.create()
        presenter = EventsPresenter(this, factory)
        presenter.getData()
    }

    override fun showProgress() {
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait..")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }

    override fun hideProgress() {
        progressDialog.dismiss()
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showData(data: ArrayList<ResponseEvents.Event>) {
        recycler_main.layoutManager = LinearLayoutManager(this)
        adapter = EventsAdapter(data) {
            val i = Intent(this, DetailActivity::class.java)
            i.putExtra("idEvent", it)
            startActivity(i)
        }
        recycler_main.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                Prefs.remove(StaticString().TOKEN)
                val i = Intent(this, LandingPage::class.java)
                startActivity(i)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
