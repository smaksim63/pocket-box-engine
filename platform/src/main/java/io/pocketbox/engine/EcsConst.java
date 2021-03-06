package io.pocketbox.engine;

public interface EcsConst {
    int GAME_LOGIC_SYSTEM_PRIORITY = 9930;

    int BODY_TRANSFORM_PRIORITY = 9935;
    int ANIMATION_TRANSFORM_PRIORITY = 9940;
    int PARTICLE_TRANSFORM_PRIORITY = 9950;

    int TEXTURE_RENDERING_PRIORITY = 9960;
    int ANIMATION_RENDERING_PRIORITY = 9970;
    int PARTICLE_RENDERING_PRIORITY = 9980;
    int DEBUG_RENDERING_PRIORITY = 9990;
}
