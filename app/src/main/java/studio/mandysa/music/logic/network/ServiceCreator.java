package studio.mandysa.music.logic.network;

import mandysax.anna2.Anna2;

/**
 * @author liuxiaoliu66
 */
public class ServiceCreator {
    private static final Anna2 ANNA = Anna2.build().baseUrl("http://cloud-music.pl-fe.cn/");

    public static <T> T create(Class<T> clazz) {
        return ANNA.create(clazz);
    }
}
