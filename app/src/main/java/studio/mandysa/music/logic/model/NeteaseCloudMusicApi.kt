package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.*
import mandysax.anna2.model.ResponseBody
import mandysax.anna2.observable.Observable
import studio.mandysa.music.logic.user.UserManager.cookie
import studio.mandysa.music.logic.user.UserManager.userId

/**
 * @author Huang hao
 */
interface NeteaseCloudMusicApi {
    //搜索音乐
    @Get("search")
    @Path("result/songs")
    fun searchMusic(
        @Query("keywords") name: String,
        @Query("offset") index: Int
    ): Observable<List<SearchMusicModel>>

    //搜索歌手
    @Get("search")
    @Path("result/artists")
    fun searchSinger(
        @Query("keywords") name: String,
        @Query("offset") index: Int,
        @Query("type") type: Int
    ): Observable<List<SearchSingerModel>>

    //获取音乐详细信息
    @Get("song/detail")
    @Path("songs")
    fun getMusicInfo(
        @Query("cookie") cookie: String = cookie(),
        @Query("ids") ids: List<Any>
    ): List<MusicModel>

    //获取推荐歌曲
    @Get("recommend/songs")
    @Path("data/dailySongs")
    fun getRecommendedSong(@Query("cookie") cookie: String = cookie()): List<RecommendSong>

    //获取推荐歌单
    @Get("recommend/resource")
    @Path("recommend")
    fun getRecommendPlaylist(@Query("cookie") cookie: String = cookie()): List<PlaylistModel>

    //歌单广场
    @Get("personalized")
    @Path("result")
    fun getPlaylistSquare(): List<PlaylistModel>

    //获取歌词
    @Get("lyric")
    @Path("lrc/lyric")
    fun getLyric(@Query("id") id: String): String

    //获取歌单详情
    @Get("playlist/detail")
    @Path("playlist")
    fun getSongListInfo(
        @Query("cookie") cookie: String = cookie(),
        @Query("id") id: String
    ): PlaylistInfoModel

    //登录
    @Post("login/cellphone")
    fun login(
        @FormData("phone") phone: String?,
        @FormData("password") password: String?,
        @Query("timestamp") timestamp: Long = System.currentTimeMillis()
    ): LoginModel

    @Get("user/playlist")
    @Path("playlist")
    fun getUserPlaylist(@Query("uid") uid: String = userId()): List<UserPlaylist>

    //获取账号信息
    @Post("user/account")
    @Path("profile")
    fun getUserInfo(
        @FormData("cookie") cookie: String = cookie(),
        @Query("timestamp") timestamp: Long
    ): UserModel

    //获取用户详情
    @Post("user/detail")
    fun getUserDetail(
        @Query("uid") uid: String
    ): UserDetailModel

    //获取所有榜单
    @Get("toplist")
    @Path("list")
    fun getToplist(): List<ListModel>

    @Get("like")
    fun likeMusic(
        @Query("cookie") cookie: String,
        @Query("id") id: String,
        @Query("like") like: Boolean
    ): ResponseBody

    @Get("subscribe")
    fun subscribe(@Query("t") t: Int, @Query("id") playlistId: String): ResponseBody

    //主页轮播图
    @Get("banner?type=1")
    @Path("banners")
    fun getBannerList(): List<BannerModel>

//    @Get("personal_fm")
//    @Path("data")
//    fun getPersonalFm(@Query("cookie") cookie: String): Observable<List<PersonalFmModel>>

    @Get("artist/detail")
    @Path("data")
    fun getSingerDetails(@Query("id") id: String): SingerDetailedModel

    @Get("likelist")
    @Path("ids")
    fun getLikeList(
        @Query("cookie") cookie: String = cookie(),
        @Query("uid") uid: String = userId()
    ): List<String>

    @Get("artist/top/song")
    fun getSingerHotSong(@Query("id") id: String): SingerHotSongModel

    @Get("album")
    fun getAlbumContent(
        @Query("id") id: String
    ): AlbumContentModel
}