package io.pocketbox.engine.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.spine.SkeletonData;
import io.pocketbox.engine.GameContext;

public class AssetsSource {

    private static final String TAG = AssetsSource.class.getName();
    private AssetManager manager;

    public AssetsSource(GameContext gameContext) {
        this.manager = new AssetManager();
        this.manager.setLoader(SkeletonData.class, new AnimationLoader(gameContext,
                new InternalFileHandleResolver()));
    }

    public <T> T getAsset(String name, Class<T> tClass) {
        return manager.get(name, tClass);
    }

    public TextureRegion getTextureRegion(String name) {
        return new TextureRegion(getAsset(name, Texture.class));
    }

    public float continueLoading() {
        float progress;
        if (manager.update()) {
            progress = 1.0f;
        } else {
            progress = manager.getProgress();
        }
        Log.debug(TAG, "Loading=" + progress);
        return progress;
    }

    public <T> void loadAssets(String subDirName, Class<T> assetType, String... extensions) {
        FileHandle dir = Gdx.files.internal(subDirName);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Failed requirement.");
        }
        traverseNestedFiles(dir, assetType, false, null, extensions);
    }

    public <T> void loadAssets(String subDirName, Class<T> assetType, boolean ignoreExtension,
                               String... extensions) {
        FileHandle dir = Gdx.files.internal(subDirName);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Failed requirement.");
        }
        traverseNestedFiles(dir, assetType, ignoreExtension, null, extensions);
    }

    public <T> void loadAssets(String subDirName, Class<T> assetType,
                               AssetLoaderParameters<T> parameters, String... extensions) {
        FileHandle dir = Gdx.files.internal(subDirName);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Failed requirement.");
        }
        traverseNestedFiles(dir, assetType, false, parameters, extensions);
    }

    public <T> void loadAssets(String subDirName, Class<T> assetType, boolean ignoreExtension,
                               AssetLoaderParameters<T> parameters, String... extensions) {
        FileHandle dir = Gdx.files.internal(subDirName);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Failed requirement.");
        }
        traverseNestedFiles(dir, assetType, ignoreExtension, parameters, extensions);
    }

    private <T> void traverseNestedFiles(FileHandle dir, Class<T> type, boolean ignoreExtension,
                                         AssetLoaderParameters<T> parameters, String... extensions) {
        for (FileHandle fh : dir.list()) {
            if (fh.isDirectory()) {
                traverseNestedFiles(fh, type, ignoreExtension, parameters, extensions);
            }
            boolean isMatched = false;
            int fileCounter = extensions.length;
            while (!isMatched && fileCounter > 0) {
                String extension = extensions[--fileCounter];
                if (fh.extension().equals(extension)) {
                    Log.debug(TAG, "load file:" + fh.name() + " as " + type.getName());
                    String path = ignoreExtension ? fh.pathWithoutExtension() : fh.path();
                    if (parameters != null) {
                        manager.load(path, type, parameters);
                    } else {
                        manager.load(path, type);
                    }
                    isMatched = true;
                }
            }
        }
    }
}
