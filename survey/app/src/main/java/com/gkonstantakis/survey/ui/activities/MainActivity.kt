package com.gkonstantakis.survey.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.gkonstantakis.survey.R
import com.gkonstantakis.survey.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.startSurveyBtn.setOnClickListener {
            questionActivityIntent()
        }

    }

    override fun onResume() {
        super.onResume()
    }

    fun questionActivityIntent() {
        val intent = Intent(this, QuestionsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)

        try {
            ContextCompat.startActivity(this, intent, Bundle())
        } catch (e: Exception) {
            Log.e("QuestionActIntent","Exception: "+e.toString())
        }
    }
}