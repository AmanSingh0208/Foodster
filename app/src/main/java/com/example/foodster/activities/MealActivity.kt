package com.example.foodster.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodster.R
import com.example.foodster.databinding.ActivityMealBinding
import com.example.foodster.db.MealDatabase
import com.example.foodster.fragment.HomeFragment
import com.example.foodster.pojo.Meal
import com.example.foodster.viewModel.MealViewModel
import com.example.foodster.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private var mealYoutubeLink: String? = ""

    private lateinit var mealMVVM: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMVVM = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()

        setInformationIntoView()

        loadingCase()

        mealMVVM.getMealDetails(mealId)
        observeMealDetailsLiveData()

        onYoutubeImageClick()

        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnAddToFav.setOnClickListener {
            mealToSave?.let {
                mealMVVM.insertMeal(it)
                Toast.makeText(this, "Meal added to favorites", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mealYoutubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave : Meal? = null
    private fun observeMealDetailsLiveData() {
        mealMVVM.observeMealDetailLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(value: Meal) {
                onResponseCase()
                val meal = value
                mealToSave = meal

                binding.tvCategory.text = "Category: ${meal.strCategory}"
                binding.tvArea.text = "Area: ${meal.strArea}"
                binding.tvInstructionsStep.text = meal.strInstructions
            }

        })
    }

    private fun setInformationIntoView() {
        Glide.with(applicationContext).load(mealThumb).into(binding.imgMealDetails)

        binding.collapsingToolbar.title = mealName
    }

    private fun getMealInformationFromIntent() {
        val intent = intent

        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
        mealYoutubeLink = intent.getStringExtra(HomeFragment.MEAL_YOUTUBE)


    }

    private fun loadingCase() {
        binding.linearProgressbar.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.GONE
        binding.tvArea.visibility = View.GONE
        binding.tvInstructions.visibility = View.GONE
        binding.tvCategory.visibility = View.GONE
        binding.imgYoutube.visibility = View.GONE
    }

    private fun onResponseCase() {
        binding.linearProgressbar.visibility = View.GONE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}