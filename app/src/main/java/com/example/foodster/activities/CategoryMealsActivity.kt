package com.example.foodster.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodster.R
import com.example.foodster.adapter.CategoryMealAdapter
import com.example.foodster.databinding.ActivityCategoryMealsBinding
import com.example.foodster.fragment.HomeFragment
import com.example.foodster.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel

    private lateinit var categoryMealAdapter: CategoryMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealsViewModel.observerMealsLiveData().observe(this, Observer { mealsList ->
            mealsList.forEach {
                Log.d("Check", it.strMeal)
            }
            binding.tvCategoryCount.text ="Total " + intent.getStringExtra(HomeFragment.CATEGORY_NAME) + " : " + mealsList.size
            categoryMealAdapter.setMealsList(mealsList)
        })
    }

    private fun prepareRecyclerView() {
        categoryMealAdapter = CategoryMealAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context,2, GridLayoutManager.VERTICAL,false)
            adapter = categoryMealAdapter
        }
    }
}