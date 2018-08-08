package io.pocketbox.prototype.scripts;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import io.pocketbox.engine.event.EventBus;
import io.pocketbox.engine.scripting.Script;

import static io.pocketbox.prototype.scripts.event.GameEvent.PLAYER_KILLED;


public class PlayerHealthScript extends Script {

    private int health = 100;

    private Label healthLabel;
    private Image hurtVeil;

    public PlayerHealthScript(Label healthLabel, Image hurtVeil) {
        this.healthLabel = healthLabel;
        this.hurtVeil = hurtVeil;
        this.updateLabel(health);
    }

    private void updateLabel(int amount) {
        this.healthLabel.setText(String.valueOf(amount));
    }

    public boolean isDead() {
        return health == 0;
    }

    public void hurt() {
        hurtVeil.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(0.05f),
                Actions.fadeOut(0.05f), Actions.visible(false)));
        health -= 15;
        if (health < 0) {
            EventBus.instance().post(PLAYER_KILLED);
            health = 0;
            gameObject.destroy();
        }
        this.updateLabel(health);
    }
}
