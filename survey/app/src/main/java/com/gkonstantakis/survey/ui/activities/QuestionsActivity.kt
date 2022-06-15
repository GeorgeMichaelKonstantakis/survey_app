package com.gkonstantakis.survey.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkonstantakis.survey.R
import com.gkonstantakis.survey.SurveyApplication
import com.gkonstantakis.survey.data.models.Answer
import com.gkonstantakis.survey.data.models.Question
import com.gkonstantakis.survey.data.utils.DataState
import com.gkonstantakis.survey.databinding.ActivityQuestionsBinding
import com.gkonstantakis.survey.ui.adapters.QuestionsAdapter
import com.gkonstantakis.survey.ui.mapping.QuestionItemMapper
import com.gkonstantakis.survey.ui.models.QuestionListItem
import com.gkonstantakis.survey.ui.utils.SurveySnapHelper
import com.gkonstantakis.survey.ui.view_models.MainStateEvent
import com.gkonstantakis.survey.ui.view_models.MainViewModel


class QuestionsActivity : AppCompatActivity() {

    private lateinit var surveyApplication: SurveyApplication
    private lateinit var viewModel: MainViewModel

    lateinit var questionsListRecyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var pageCounterText: TextView
    lateinit var previousButton: Button
    lateinit var nextButton: Button

    lateinit var surveyData: MutableList<QuestionListItem>
    private lateinit var activityViewBinding: ActivityQuestionsBinding

    private var questionsAdapter: QuestionsAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var snapHelper: SurveySnapHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewBinding = ActivityQuestionsBinding.inflate(layoutInflater)
        val view = activityViewBinding.root
        setContentView(view)

        surveyApplication = (application as SurveyApplication)

        viewModel = MainViewModel(surveyApplication.mainRepository)

        subscribeObservers()

        progressBar = activityViewBinding.progressBar
        questionsListRecyclerView = activityViewBinding.questionsList
        pageCounterText = activityViewBinding.pageCounterText
        previousButton = activityViewBinding.previousBtn
        nextButton = activityViewBinding.nextBtn

        surveyData = ArrayList<QuestionListItem>()

        viewModel.setDataStateEvent(MainStateEvent.GetQuestionEvent, null)
    }

    fun subscribeObservers() {
        viewModel.questionDataState.observe(this, Observer { datastate ->
            when (datastate) {
                is DataState.SuccessNetworkQuestions<List<Question>> -> {
                    for (data in datastate.data) {
                        surveyData.add(QuestionItemMapper().mapFromDomainQuestion(data))
                    }
                    viewModel.setDataStateEvent(MainStateEvent.GetAnswersEvent, null)
                    displayProgressBar(false)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
                is DataState.Error -> {
                    Toast.makeText(
                        this.applicationContext,
                        this.resources?.getString(R.string.error_network_questions_message),
                        Toast.LENGTH_LONG
                    ).show()
                    displayProgressBar(false)
                }
            }

        })

        viewModel.answerDataState.observe(this, Observer { datastate ->
            when (datastate) {
                is DataState.SuccessDatabaseAnswers<List<Answer>> -> {
                    if (!datastate.data.isNullOrEmpty()) {
                        for (data in datastate.data) {
                            surveyData.find {
                                it.id == data.id
                            }?.answer = data.answer
                        }
                    }
                    displayQuestionsList(surveyData)
                    viewModel.setPageCount(1)
                }
                is DataState.Error -> {
                    displayQuestionsList(surveyData)
                    viewModel.setPageCount(1)
                }
            }
        })

        viewModel.pageCount.observe(this, Observer {
            setupPageCounterText(it)
            setupButtons(it)
        })
    }

    fun displayQuestionsList(data: List<QuestionListItem>){
        questionsAdapter = QuestionsAdapter(
            data,
            surveyApplication.mainRepository,
            viewModel,
            this
        )
        questionsListRecyclerView.adapter = questionsAdapter
        snapHelper = SurveySnapHelper()
        snapHelper?.attachToRecyclerView(questionsListRecyclerView)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        questionsListRecyclerView.layoutManager = linearLayoutManager
        displayProgressBar(false)
    }

    fun setupPageCounterText(count: Int) {
        pageCounterText.visibility = View.VISIBLE
        pageCounterText.text = "${count}/${questionsListRecyclerView.adapter?.itemCount}"
    }

    fun setupButtons(count: Int) {
        Log.e("setupButtons",""+count.toString())
        if (count <= 1) {
            previousButton.visibility = View.GONE
            nextButton.visibility = View.VISIBLE
        } else if (count >= questionsAdapter?.itemCount!!) {
            previousButton.visibility = View.VISIBLE
            nextButton.visibility = View.GONE
        } else {
            previousButton.visibility = View.VISIBLE
            nextButton.visibility = View.VISIBLE
        }
        if(count>0){
            questionsListRecyclerView.recycledViewPool.setMaxRecycledViews(
                questionsAdapter!!.getItemViewType(
                    count
                ), 0
            )
        }
        previousButtonClick(count)
        nextButtonClick(count)
    }

    fun previousButtonClick(count: Int) {
        previousButton.setOnClickListener {
            questionsListRecyclerView.smoothScrollToPosition(count-1)
            viewModel.setPageCount(count - 1)
        }
    }

    fun nextButtonClick(count: Int) {
        nextButton.setOnClickListener {
            questionsListRecyclerView.smoothScrollToPosition(count+1)
            viewModel.setPageCount(count + 1)
        }
    }

    fun displayProgressBar(display: Boolean) {
        if (display) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    fun mainActivityIntent() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)

        try {
            ContextCompat.startActivity(this, intent, Bundle())
        } catch (e: Exception) {
        }
    }
}