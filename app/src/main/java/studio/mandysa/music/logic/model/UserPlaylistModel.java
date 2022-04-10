package studio.mandysa.music.logic.model;

import java.util.List;

import mandysax.anna2.annotation.Path;
import mandysax.anna2.annotation.Value;

/**
 * @author Huang hao
 */
public class UserPlaylistModel {
    @Value("playlist")
    public List<UserPlaylist> list;

    public static class UserPlaylist {
        @Value("id")
        public String id;

        @Value("name")
        public String name;

        @Value("nickname")
        @Path("creator")
        public String nickname;

        @Value("coverImgUrl")
        public String coverImgUrl;

        @Value("signature")
        public String signature;
    }

}
