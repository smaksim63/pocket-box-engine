package io.pocketbox.engine.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

import static io.pocketbox.engine.Config.PIXEL_TO_METER;

public class AnimationLoader extends AsynchronousAssetLoader<SkeletonData, AnimationLoader.AnimationParameter> {

    private static final String TAG = AnimationLoader.class.getName();
    private SkeletonData skeletonData;

    public AnimationLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, AnimationParameter parameter) {
        if (parameter != null) {
            skeletonData = loadSkeletonData(file, parameter.scale, manager);
        } else {
            skeletonData = loadSkeletonData(file, 1.0f, manager);
        }
    }

    @Override
    public SkeletonData loadSync(AssetManager manager, String fileName, FileHandle file, AnimationParameter parameter) {
        SkeletonData skeletonData = this.skeletonData;
        this.skeletonData = null;
        return skeletonData;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, AnimationParameter parameter) {
        return null;
    }

    static public class AnimationParameter extends AssetLoaderParameters<SkeletonData> {
        float scale;

        public AnimationParameter(float scale) {
            this.scale = scale;
        }
    }

    public SkeletonData loadSkeletonData(FileHandle file, float scaleFactor, AssetManager manager) {
        String atlasName = file.pathWithoutExtension() + ".atlas";
        while (!manager.isLoaded(atlasName)) {
            //no-ops
        }
        TextureAtlas atlas = manager.get(atlasName, TextureAtlas.class);
        SkeletonData skeletonData;

        try {
            String extension = file.extension();
            if (extension.equalsIgnoreCase("json")) {
                SkeletonJson json = new SkeletonJson(atlas);
                json.setScale(scaleFactor * PIXEL_TO_METER);
                skeletonData = json.readSkeletonData(file);
            } else {
                SkeletonBinary binary = new SkeletonBinary(atlas);
                binary.setScale(scaleFactor * PIXEL_TO_METER);
                skeletonData = binary.readSkeletonData(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Gdx.app.log(TAG, "Error loading skeleton: " + file.name());
            throw new RuntimeException(ex);
        }

        return skeletonData;
    }
}
