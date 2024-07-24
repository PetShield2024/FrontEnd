import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petshield.databinding.FragmentFoodBinding
import com.example.petshield.FilterFragment
import com.example.petshield.DataPassListener
import com.example.petshield.Food
import com.example.petshield.FoodRVAdapter
import com.example.petshield.MyfoodFragment

class FoodFragment : Fragment(), FoodRVAdapter.MyItemClickListener, DataPassListener {

    private lateinit var binding: FragmentFoodBinding
    private lateinit var foodRVAdapter: FoodRVAdapter
    private var foodDatas = ArrayList<Food>()

    private lateinit var filterFragment: FilterFragment
    private lateinit var myfoodFragment: MyfoodFragment


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodBinding.inflate(inflater, container, false)

        inputDummyFood()

        foodRVAdapter = FoodRVAdapter(foodDatas)
        foodRVAdapter.setMyItemClickListener(this)
        binding.foodRv.adapter = foodRVAdapter

        binding.foodMyfoodIb.setOnClickListener {
            startMyFoodSearch()
        }
        binding.foodFilterIb.setOnClickListener {
            openFilterFragment()
        }

        return binding.root
    }

    private fun startMyFoodSearch() {
        myfoodFragment = MyfoodFragment()
        // FilterFragment에서 선택된 값들을 받기 위해 리스너 설정

        myfoodFragment.show(parentFragmentManager, "MyfoodFragment")
    }

    private fun inputDummyFood() {
        foodDatas.apply {
            add(Food(1, "게더 독 엔드레스 벨리 비건 레시피", 28000, "포메라니안", "전체", "9-13세", "전체", "게더","캐나다", "https://bit.ly/3V0Zy6f", "", "https://share.pet-friends.co.kr/product/share?product_id=176749&next=https%3A%2F%2Fm.pet-friends.co.kr"))
            add(Food(2, "닥터독 다이어트 사료", 32500, "리트리버", "전체", "1-4세", "전체", "닥터","국내", "https://bit.ly/3V0Zy6f", "", "https://share.pet-friends.co.kr/product/share?product_id=176749&next=https%3A%2F%2Fm.pet-friends.co.kr"))
            add(Food(3, "닥터독 위장사료", 32500, "말티즈", "전체", "1-4세", "전체", "닥터", "국내","https://bit.ly/3V0Zy6f", "", "https://share.pet-friends.co.kr/product/share?product_id=176749&next=https%3A%2F%2Fm.pet-friends.co.kr"))
            add(Food(4, "더텐 독 오븐베이크 알러지케어&체중조절 올인원", 25000, "포메라니안", "대형견", "1-4세", "저체중", "더텐", "국내","https://bit.ly/3V0Zy6f", "", "https://share.pet-friends.co.kr/product/share?product_id=176749&next=https%3A%2F%2Fm.pet-friends.co.kr"))
            add(Food(5, "로얄캐닌 독 라이트웨이트 케어 파우치  다이어트", 2100, "포메라니안", "중형견", "9-13세", "전체", "로얄캐닌", "프랑스","https://bit.ly/3V0Zy6f", "", "https://share.pet-friends.co.kr/product/share?product_id=176749&next=https%3A%2F%2Fm.pet-friends.co.kr"))
            add(Food(6, "로얄캐닌 독 미니 라이트 웨이트 케어 체중조절", 15220, "포메라니안", "중형견", "9-13세", "전체", "로얄캐닌", "프랑스","https://bit.ly/3V0Zy6f", "", "https://share.pet-friends.co.kr/product/share?product_id=176749&next=https%3A%2F%2Fm.pet-friends.co.kr"))
        }
    }

    private fun openFilterFragment() {
        filterFragment = FilterFragment()

        // FilterFragment에서 선택된 값들을 받기 위해 리스너 설정
        filterFragment.setDataPassListener(this)

        filterFragment.show(parentFragmentManager, "FilterFragment")
    }

    override fun onDataPass(
        age: String,
        size: String,
        variety: String,
        weight: String,
        brand: String,
        origin: String
    ) {
        // FilterFragment에서 전달된 값을 받아서 처리하는 코드
        // 여기서 선택된 필터에 따라 데이터를 다시 로드하거나 화면을 업데이트하는 등의 작업 수행 가능
        binding.foodAgeTv.text = age
        binding.foodSizeTv.text = size
        binding.foodVarietyTv.text = variety
        binding.foodWeightTv.text = weight
        binding.foodWeightTv.text = brand
        binding.foodWeightTv.text = origin


        // 예시로 필터링된 데이터를 처리하는 코드를 작성
        val filteredList = foodDatas.filter { food ->
            // 값이 "전체"인 파라메터는 food에서 해당속성 값이 모두 표시되도록 수정
            // 즉, age="전체"면, age가 1-4세, 5-8세, 9-13세 등 모든 값이 표시되어야함
                    (age == "전체" || food.age == age || food.age == "전체") &&
                    (size == "전체" || food.size == size || food.size == "전체") &&
                    (variety == "전체" || food.variety == variety || food.variety == "전체") &&
                    (weight == "전체" || food.weight == weight || food.weight == "전체") &&
                            (brand == "전체" || food.brand == brand || food.brand == "전체")&&
                            (origin == "전체" || food.origin == origin || food.origin == "전체")

        }

        // 필터링된 데이터로 RecyclerView 갱신
        foodRVAdapter.updateList(filteredList)
    }

    override fun onSiteClick(food: Food) {
        val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(food.url))
        startActivity(urlIntent)
    }
}

