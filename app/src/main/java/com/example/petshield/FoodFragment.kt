import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.example.petshield.FoodRecommendationResponse
import com.example.petshield.MyfoodFragment
import com.example.petshield.R
import com.example.petshield.RetrofitClientApi
import com.example.petshield.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodFragment : Fragment(), FoodRVAdapter.MyItemClickListener, DataPassListener {

    private lateinit var binding: FragmentFoodBinding
    private lateinit var progressDialog: AlertDialog
    private lateinit var foodRVAdapter: FoodRVAdapter
    private var foodDatas = ArrayList<Food>()
    private var currentPage = 0
    private var isLoading = false
    private var isLastPage = false

    private lateinit var filterFragment: FilterFragment
    private lateinit var myfoodFragment: MyfoodFragment
    var dogId = 1L;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodBinding.inflate(inflater, container, false)

        setupRecyclerView()
        loadFoodData(currentPage)

        binding.foodMyfoodIb.setOnClickListener {

            startMyFoodSearch(dogId)
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

    private fun startMyFoodSearch(dogId: Long) {
        showLoadingDialog()  // 로딩 화면 표시

        RetrofitClientApi.retrofitInterface.getFoodRecommendations(dogId).enqueue(object : Callback<FoodRecommendationResponse> {
            override fun onResponse(
                call: Call<FoodRecommendationResponse>,
                response: Response<FoodRecommendationResponse>
            ) {
                hideLoadingDialog()  // 로딩 화면 숨기기

                if (response.isSuccessful) {
                    val recommendationResponse = response.body()
                    if (recommendationResponse != null) {
                        showAlertDialog(requireContext(), recommendationResponse.recommendation)
                    } else {
                        Log.e("FoodFragment", "Error: ${response.errorBody()?.string()}")
                        showAlertDialog(requireContext(), "No recommendation available")
                    }
                } else {
                    Log.e("FoodFragment", "fail: ${response.errorBody()?.string()}")
                    showAlertDialog(requireContext(), "HTTP error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<FoodRecommendationResponse>, t: Throwable) {
                hideLoadingDialog()  // 로딩 화면 숨기기
                showAlertDialog(requireContext(), "Network error: ${t.message}")
            }
        })
    }


    private fun showAlertDialog(context: Context, message: String) {
        val spannableMessage = SpannableString(message)
        val matcher = Patterns.WEB_URL.matcher(message)

        while (matcher.find()) {
            val url = matcher.group()
            val start = matcher.start()
            val end = matcher.end()

            spannableMessage.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }
            }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle("사료 추천")
            .setMessage(spannableMessage)
            .setPositiveButton("확인", null)
            .setNegativeButton("Retry") { _, _ -> startMyFoodSearch(dogId) }
            .create()

        dialog.show()

        // 링크를 활성화하려면 아래 설정을 추가해야 합니다.
        (dialog.findViewById<TextView>(android.R.id.message))?.movementMethod = LinkMovementMethod.getInstance()
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

    private fun showLoadingDialog() {
        progressDialog = AlertDialog.Builder(requireContext())
            .setView(R.layout.progress_dialog) // 커스텀 로딩 레이아웃 사용 가능
            .setCancelable(false)
            .create()
        progressDialog.show()
    }

    private fun hideLoadingDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}
