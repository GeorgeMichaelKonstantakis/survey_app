package com.gkonstantakis.survey.ui.adapters

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.gkonstantakis.survey.R
import com.gkonstantakis.survey.data.MainRepositoryImpl
import com.gkonstantakis.survey.data.models.Answer
import com.gkonstantakis.survey.data.utils.DataState
import com.gkonstantakis.survey.databinding.QuestionListItemBinding
import com.gkonstantakis.survey.ui.models.MainStateEventParameters
import com.gkonstantakis.survey.ui.models.QuestionListItem
import com.gkonstantakis.survey.ui.view_models.MainStateEvent
import com.gkonstantakis.survey.ui.view_models.MainViewModel

class QuestionsAdapter(
    var questionsList: List<QuestionListItem>,
    val mainRepository: MainRepositoryImpl,
    val viewModel: MainViewModel,
    val lifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<QuestionsAdapter.QuestionsListViewHolder>() {

    var adapterBinding: QuestionListItemBinding? = null

    inner class QuestionsListViewHolder(questionsListItemBinding: QuestionListItemBinding) :
        RecyclerView.ViewHolder(questionsListItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsListViewHolder {
        adapterBinding =
            QuestionListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionsListViewHolder(adapterBinding!!)
    }

    override fun onBindViewHolder(holder: QuestionsListViewHolder, position: Int) {
        holder.itemView.apply {
            val questionListItem = questionsList[position]

            Log.e("onBindViewHolder","ENTER: questionItem: "+questionListItem.toString())

            var answerToSubmit = ""

            val questionText = adapterBinding?.questionText
            val answerText = adapterBinding?.answerText
            val submitButton = adapterBinding?.submitButton
            val submitTextMessage = adapterBinding?.submitTextMessage

            questionText?.text = questionListItem.question
            if (questionListItem.answer != null) {
                answerText?.setText(questionListItem.answer, TextView.BufferType.EDITABLE)
            }

            answerText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    answerToSubmit = p0.toString()
                    Log.e("answerToSubmit",""+answerToSubmit)
                }

            })

            viewModel.postAnswerState.observe(lifecycleOwner, Observer { datastate ->
                when (datastate) {
                    is DataState.SuccessPostAnswer<List<Answer>> -> {
                        if(datastate.data[0].id == questionListItem.id) {
                            submitButton?.isEnabled = false
                            answerText?.isEnabled = false
                            submitButton?.text = resources.getString(R.string.post_answer_submit_button_text)
                            submitTextMessage?.visibility = View.VISIBLE
                            submitTextMessage?.text = resources.getString(R.string.post_answer_success_text_message)
                            submitTextMessage?.setTextColor(Color.GREEN)
                        }
                    }
                    is DataState.ErrorPostAnswer -> {
                        if(datastate.data[0].id == questionListItem.id) {
                            submitButton?.isEnabled = true
                            answerText?.isEnabled = true
                            submitButton?.text = resources.getString(R.string.post_answer_try_again_button_message)
                            submitTextMessage?.visibility = View.VISIBLE
                            submitTextMessage?.text = resources.getString(R.string.post_answer_try_again_text_message)
                            submitTextMessage?.setTextColor(Color.RED)
                        }
                    }
                }
            })

            submitButton?.setOnClickListener {
                viewModel.setDataStateEvent(
                    MainStateEvent.PostAnswerEvent,
                    MainStateEventParameters(
                        Answer(
                            id = questionListItem.id,
                            answer = answerToSubmit
                        )
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return questionsList.size
    }

}