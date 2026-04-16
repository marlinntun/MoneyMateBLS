package com.example.moneymate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnAddTransaction: Button
    private lateinit var btnViewTransactions: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = getString(R.string.app_name)

        btnAddTransaction = findViewById(R.id.btnAddTransaction)
        btnViewTransactions = findViewById(R.id.btnViewTransactions)

        btnAddTransaction.setOnClickListener {
            startActivity(Intent(this, InputActivity::class.java))
        }

        btnViewTransactions.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }
    }
}