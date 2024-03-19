package com.example.foodster.fragment.bottomsheet

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodster.R
import com.example.foodster.activities.MainActivity
import com.example.foodster.activities.MealActivity
import com.example.foodster.databinding.FragmentMealBottomSheetBinding
import com.example.foodster.fragment.HomeFragment
import com.example.foodster.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"


class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        binding = FragmentMealBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealId?.let {
            viewModel.getMealById(it)
        }

        observeBottomSheetMeal()

        onBottomSheetLayoutClick()
    }

    private fun onBottomSheetLayoutClick() {
        binding.bottomSheet.setOnClickListener {
            if( mealName != null && mealThumb != null) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID, mealId)
                    putExtra(HomeFragment.MEAL_NAME, mealName)
                    putExtra(HomeFragment.MEAL_THUMB, mealThumb)
                }
                startActivity(intent)
            }
        }
    }

    private var mealName: String? = null
    private var mealThumb: String? =null
    private fun observeBottomSheetMeal() {
        viewModel.observeBottomMealSheetLiveData().observe(viewLifecycleOwner, Observer {meal ->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgBottomSheet)
            binding.tvBottomSheetCategory.text = meal.strCategory
            binding.tvBottomSheetMeal.text = meal.strMeal
            binding.tvBottomSheetLocation.text = meal.strArea

            mealName = meal.strMeal
            mealThumb = meal.strMealThumb
        })
    }

    companion object {

        @JvmStatic fun newInstance(param1: String) =
                MealBottomSheetFragment().apply {
                    arguments = Bundle().apply {
                        putString(MEAL_ID, param1)
                    }
                }
    }
}