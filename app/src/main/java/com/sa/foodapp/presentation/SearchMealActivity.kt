package com.sa.foodapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sa.foodapp.R
import com.sa.foodapp.data.data_source.remote.Resource
import com.sa.foodapp.databinding.ActivitySearchMealBinding
import com.sa.foodapp.domain.model.Meal
import com.sa.foodapp.domain.util.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMealActivity : AppCompatActivity() {
    private val viewModel:SearchMealViewModel by viewModels()
    private lateinit var binding:ActivitySearchMealBinding
    private var mealListAdapter:MealListAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySearchMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFun()
        observeData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.foodListStateFlow.collectLatest {res->
                    when(res){
                        is Resource.Loading->{
                            showLoader()
                        }
                        is Resource.Error->{
                            showError(res.error)
                        }
                        is Resource.Success->{
                            if(binding.searchView.query.trim().isNotBlank()){
                                if (!res.data.isNullOrEmpty()){
                                    showData(res.data)
                                }else{
                                    showNoData()
                                }
                            }else{
                                showNoData()
                            }

                        }
                    }
                }
            }
        }

    }
    private fun showLoader(){
        binding.apply {
            recyclerMeal.isVisible=false
            errorView.content.isVisible=false
            loader.isVisible=true
            tvMessage.isVisible=false
        }
    }
    private fun showNoData(){
        binding.apply {
            recyclerMeal.isVisible=false
            errorView.content.isVisible=false
            loader.isVisible=false
            tvMessage.isVisible=true
            if (binding.searchView.query.trim().isEmpty()){
                tvMessage.text= getString(R.string.search_meals)
            }else{
                tvMessage.text= getString(R.string.no_meals_found)
            }

        }
    }
    private fun showError(error:String?){
        binding.apply {
            recyclerMeal.isVisible=false
            errorView.content.isVisible=true
            errorView.tverror.text=error
            loader.isVisible=false
            tvMessage.isVisible=false
        }
    }
    private fun showData(meals:List<Meal?>){
        binding.apply {
            recyclerMeal.isVisible=true
            errorView.content.isVisible=false
            loader.isVisible=false
            tvMessage.isVisible=false
            mealListAdapter?.submitList(meals)
        }
    }

    private fun initFun() {
        mealListAdapter=MealListAdapter()
        binding.recyclerMeal.itemAnimator=null
        binding.recyclerMeal.adapter=mealListAdapter
        binding.errorView.btnRetry.setOnClickListener {
            search(binding.searchView.query)
        }
        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return false
            }

        })
    }
    private fun search(query:CharSequence?){
        if (!query?.trim().isNullOrEmpty()){
            viewModel.searchMeal(query.toString())
        }else{
            binding.apply {
                recyclerMeal.isVisible=false
                errorView.content.isVisible=false
                loader.isVisible=false
                tvMessage.isVisible=true
                tvMessage.text= getString(R.string.search_meals)
            }
        }
    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit."
            )
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.getLeft() - scrcoords[0]
            val y = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) (this.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager).hideSoftInputFromWindow(
                this.window.decorView.applicationWindowToken, 0
            )
        }
        return super.dispatchTouchEvent(ev)
    }
}