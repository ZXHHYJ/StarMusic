package studio.mandysa.music.ui.searchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import studio.mandysa.music.databinding.FragmentSearchListBinding
import studio.mandysa.music.logic.ktx.viewBinding

class SearchListFragment : Fragment() {

    private val mBinding: FragmentSearchListBinding by viewBinding()

    private val mSearchContentLiveData = MutableLiveData<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* requireActivity().onBackPressedDispatcher
             .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                 override fun handleOnBackPressed() {
                     findNavController().navigateUp()
                 }
             })
         mBinding.run {
             val args = SearchListFragmentArgs.fromBundle(requireArguments())
             searchListToolbar.title = args.searchContent
             mSearchContentLiveData.value = args.searchContent
             searchListFragmentPage.adapter = requireActivity().createFragmentStateAdapter(
                 SearchMusicFragment(mSearchContentLiveData),
                 SearchSingerFragment(mSearchContentLiveData)
             )
             TabLayoutMediator(
                 tabview, searchListFragmentPage
             ) { tab, position ->
                 run {
                     tab.text = resources.getStringArray(R.array.search_list_tabs)[position]
                 }
             }.attach()
         }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

}