package io.pocketbox.engine.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import io.pocketbox.engine.scripting.Script;

public class ScriptingComponent implements Component {
    public final Array<Script<?>> scripts;

    public ScriptingComponent(Script<?>... scripts) {
        this.scripts = new Array<>();
        this.scripts.addAll(scripts);
    }
}
