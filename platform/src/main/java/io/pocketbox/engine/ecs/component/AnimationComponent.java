package io.pocketbox.engine.ecs.component;

import com.badlogic.ashley.core.Component;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;

public class AnimationComponent implements Component {

    public AnimationState state;
    public AnimationStateData stateData;
    public Skeleton skeleton;

    public AnimationComponent(SkeletonData skeletonData, String initAnimation) {
        skeleton = new Skeleton(skeletonData);
        skeleton.updateWorldTransform();

        stateData = new AnimationStateData(skeletonData);
        state = new AnimationState(stateData);

        skeleton.setSkin(skeletonData.getSkins().first());
        state.setAnimation(0, initAnimation, true);
    }

    public void setAnimation(String animationName) {
        state.clearTracks();
        skeleton.setToSetupPose();
        state.setAnimation(0, animationName, true);
    }
}
