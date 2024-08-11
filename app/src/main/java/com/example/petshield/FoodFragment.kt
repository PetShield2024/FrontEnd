import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petshield.ApiResponse
import com.example.petshield.databinding.FragmentFoodBinding
import com.example.petshield.FilterFragment
import com.example.petshield.DataPassListener
import com.example.petshield.Food
import com.example.petshield.FoodListResponse
import com.example.petshield.FoodRVAdapter
import com.example.petshield.MyfoodFragment
import com.example.petshield.RetrofitClientApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodFragment : Fragment(), FoodRVAdapter.MyItemClickListener, DataPassListener {

    private lateinit var binding: FragmentFoodBinding
    private lateinit var foodRVAdapter: FoodRVAdapter
    private var foodDatas = ArrayList<Food>()
    private var currentPage = 0
    private var isLoading = false
    private var isLastPage = false

    private lateinit var filterFragment: FilterFragment
    private lateinit var myfoodFragment: MyfoodFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodBinding.inflate(inflater, container, false)

        setupRecyclerView()
        loadFoodData(currentPage)

        binding.foodMyfoodIb.setOnClickListener {
            startMyFoodSearch()
        }
        binding.foodFilterIb.setOnClickListener {
            openFilterFragment()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        foodRVAdapter = FoodRVAdapter(foodDatas)
        foodRVAdapter.setMyItemClickListener(this)
        binding.foodRv.adapter = foodRVAdapter

        val layoutManager = binding.foodRv.layoutManager as LinearLayoutManager

        binding.foodRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadFoodData(currentPage + 1)
                    }
                }
            }
        })
    }

    private fun loadFoodData(page: Int) {
        isLoading = true

        RetrofitClientApi.retrofitInterface.getFoodList(page)
            .enqueue(object : Callback<ApiResponse<FoodListResponse>> {
                override fun onResponse(
                    call: Call<ApiResponse<FoodListResponse>>,
                    response: Response<ApiResponse<FoodListResponse>>
                ) {
                    if (response.isSuccessful) {
                        val foodListResponse = response.body()?.result
                        foodListResponse?.let {
                            val newFoods = it.foodList.map { foodItem ->
                                Food(
                                    foodName = foodItem.foodName,
                                    price = foodItem.price,
                                    size = foodItem.size,
                                    age = foodItem.age,
                                    weight = foodItem.obesity,
                                    brand = foodItem.brand,
                                    origin = foodItem.origin,
                                    image = foodItem.image,
                                    extra = foodItem.extra,
                                    site = foodItem.site
                                )
                            }
                            foodDatas.addAll(newFoods)
                            foodRVAdapter.notifyDataSetChanged()
                            currentPage = page
                            isLastPage = it.isLast
                        }
                    } else {
                        Log.e("FoodFragment", "Error: ${response.errorBody()?.string()}")
                    }
                    isLoading = false
                }

                override fun onFailure(call: Call<ApiResponse<FoodListResponse>>, t: Throwable) {
                    Log.e("FoodFragment", "Failure: ${t.message}")
                    isLoading = false
                }
            })
    }

    private fun startMyFoodSearch() {
        myfoodFragment = MyfoodFragment()
        myfoodFragment.show(parentFragmentManager, "MyfoodFragment")
    }

    private fun openFilterFragment() {
        filterFragment = FilterFragment()
        filterFragment.setDataPassListener(this)
        filterFragment.show(parentFragmentManager, "FilterFragment")
    }

    override fun onDataPass(
        age: String,
        size: String,
        weight: String,
        brand: String,
        origin: String
    ) {
        binding.foodAgeTv.text = age
        binding.foodSizeTv.text = size
        binding.foodWeightTv.text = weight
        binding.foodBrandTv.text = brand
        binding.foodOriginTv.text = origin

        val ageResult = when (age) {
            "1세 이하" -> "UNDER_1_YEARS"
            "1-7세" -> "FROM_1_TO_7_YEARS"
            "7세 이상" -> "OVER_7_YEARS"
            else -> "ALL"
        }

        val filteredList = foodDatas.filter { food ->
            (age == "전체" || food.age == ageResult || food.age == "ALL") &&
                    (size == "전체" || food.size == size || food.size == "전체") &&
                    (weight == "전체" || food.weight == weight || food.weight == "전체") &&
                    (brand == "전체" || food.brand == brand || food.brand == "전체") &&
                    (origin == "전체" || food.origin == origin || food.origin == "전체")
        }

        foodRVAdapter.updateList(filteredList)
    }

    override fun onSiteClick(food: Food) {
        val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(food.site))
        startActivity(urlIntent)
    }
}
