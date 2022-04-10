package studio.mandysa.music.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mandysax.anna2.exception.AnnaException
import studio.mandysa.jiuwo.utils.addModels
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.*
import studio.mandysa.music.logic.decoration.HorizontalAlbumItemDecoration
import studio.mandysa.music.logic.ktx.load
import studio.mandysa.music.logic.ktx.viewBinding
import studio.mandysa.music.logic.model.*
import studio.mandysa.music.logic.model.design.SubTitleModel
import studio.mandysa.music.logic.model.design.TitleModel
import studio.mandysa.music.logic.network.ServiceCreator
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.ktx.allArtist
import studio.mandysa.music.ui.event.EventViewModel


class HomeFragment : Fragment() {

    private val mBinding: FragmentHomeBinding by viewBinding()

    private val mEvent: EventViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.root) { _, insets ->
            val navigationBarSize =
                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            mBinding.mainRecycle.setPadding(
                0,
                0,
                0,
                resources.getDimensionPixelSize(R.dimen.controller_height) + resources.getDimensionPixelSize(
                    R.dimen.nav_height
                ) + navigationBarSize
            )
            insets
        }
        mBinding.mainRecycle.linear().setup {
            addType<TitleModel>(R.layout.item_title) {
                ItemTitleBinding.bind(itemView).titleTv.text =
                    getModel<TitleModel>().title
            }
            addType<SubTitleModel>(R.layout.item_sub_title) {
                ItemSubTitleBinding.bind(itemView).title.text =
                    getModel<SubTitleModel>().title
            }
            addType<RecommendSongs.RecommendSong>(R.layout.item_song) {
                val model = getModel<RecommendSongs.RecommendSong>()
                ItemSongBinding.bind(itemView).apply {
                    songIndex.text = (modelPosition + 1).toString()
                    songName.text = model.title
                    songSingerName.text = model.artist.allArtist()
                    itemView.setOnClickListener {
                        PlayManager.loadPlaylist(
                            models,
                            modelPosition
                        )
                    }
                    more.setOnClickListener {

                    }
                }
            }
            val pagerSnapHelper = PagerSnapHelper()
            addType<BannerModels>(R.layout.layout_rv) {
                LayoutRvBinding.bind(itemView).recycler.apply {
                    pagerSnapHelper.attachToRecyclerView(this)
                    linear(orientation = RecyclerView.HORIZONTAL).setup {
                        addType<BannerModels.BannerModel>(R.layout.item_banner) {
                            val model = getModel<BannerModels.BannerModel>()
                            ItemBannerBinding.bind(itemView).let { it ->
                                it.title.text = model.typeTitle
                                it.planIv.load(
                                    model.pic
                                )
                                it.planIv.setOnClickListener {
                                    when (model.targetType) {
                                        3000 -> {
                                            val uri: Uri = Uri.parse(model.url)
                                            val intent = Intent(Intent.ACTION_VIEW, uri)
                                            startActivity(intent)
                                        }
                                        1000 -> {
                                            it.findNavController().navigate(
                                                HomeFragmentDirections.actionHomeFragmentToPlaylistFragment(
                                                    model.encodeId
                                                )
                                            )
                                        }
                                        1 -> {

                                        }
                                        else -> {

                                        }
                                    }
                                }
                            }
                        }
                    }
                    recyclerAdapter.models = getModel<BannerModels>().list
                }
            }
            val linearSnapHelper1 = LinearSnapHelper()
            addType<PlaylistSquareModel>(R.layout.layout_rv) {
                LayoutRvBinding.bind(itemView).recycler.apply {
                    linearSnapHelper1.attachToRecyclerView(this)
                    if (itemDecorationCount == 0)
                        addItemDecoration(HorizontalAlbumItemDecoration())
                    linear(orientation = RecyclerView.HORIZONTAL).setup {
                        addType<PlaylistModel>(R.layout.item_playlist) {
                            val model = getModel<PlaylistModel>()
                            ItemPlaylistBinding.bind(itemView).apply {
                                playlistCover.load(
                                    model.picUrl
                                )
                                playlistTitle.text = model.name
                                itemView.setOnClickListener {
                                    it.findNavController().navigate(
                                        HomeFragmentDirections.actionHomeFragmentToPlaylistFragment(
                                            model.id
                                        )
                                    )
                                }
                            }
                        }
                    }
                    recyclerAdapter.models = getModel<PlaylistSquareModel>().playlist
                }
            }
            val linearSnapHelper = LinearSnapHelper()
            addType<RecommendPlaylist>(R.layout.layout_rv) {
                LayoutRvBinding.bind(itemView).recycler.apply {
                    linearSnapHelper.attachToRecyclerView(this)
                    if (itemDecorationCount == 0)
                        addItemDecoration(HorizontalAlbumItemDecoration())
                    linear(orientation = RecyclerView.HORIZONTAL).setup {
                        addType<PlaylistModel>(R.layout.item_playlist) {
                            val model = getModel<PlaylistModel>()
                            ItemPlaylistBinding.bind(itemView).apply {
                                playlistTitle.text = model.name
                                playlistCover.load(
                                    model.picUrl
                                )
                                playlistCover.setOnClickListener {
                                    it.findNavController().navigate(
                                        HomeFragmentDirections.actionHomeFragmentToPlaylistFragment(
                                            model.id
                                        )
                                    )
                                }
                            }
                        }
                    }
                    recyclerAdapter.models = getModel<RecommendPlaylist>().playlist!!
                }
            }
        }
        mBinding.mainStateLayout.showLoading {
            mEvent.getCookieLiveData().observe(viewLifecycleOwner) { cookie ->
                if (cookie == null) return@observe
                viewLifecycleOwner.lifecycle.coroutineScope.launch(Dispatchers.IO) {
                    try {
                        ServiceCreator.create(NeteaseCloudMusicApi::class.java).let {
                            val bannerModel = it.getBannerList()
                            val recommendPlaylist =
                                it.getRecommendPlaylist(cookie)
                            val playlistSquare = it.getPlaylistSquare()
                            val recommendedSong = it.getRecommendedSong(cookie)
                            withContext(Dispatchers.Main) {
                                mBinding.mainRecycle.recyclerAdapter.headers = listOf(
                                    TitleModel(context.getString(R.string.browse)),
                                    bannerModel,
                                    SubTitleModel(getString(R.string.recommend_playlist)),
                                    recommendPlaylist,
                                    SubTitleModel(getString(R.string.playlist_square)),
                                    playlistSquare,
                                    SubTitleModel(getString(R.string.recommend_song))
                                )
                                mBinding.mainRecycle.addModels(recommendedSong.list!!)
                                mBinding.mainStateLayout.showContentState()
                            }
                        }
                    } catch (e: AnnaException) {
                        mBinding.mainStateLayout.showErrorState()
                    }
                }
            }
        }
        mBinding.mainStateLayout.showError {
            mBinding.mainRecycle.recyclerAdapter.clearModels()
        }
        mBinding.mainStateLayout.showLoadingState()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }
}