package mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mygdx.methods.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    TextureRegionDrawable textureRegionDrawable;
    BitmapFont bitmapFont;
    ShapeRenderer graphicRenderer;
    Stage stage;
    Map<Button, Method> methods;
    Function<Double, Double> target = x -> x - Math.log(x);
    Graphic graphic;

    class RedPointFunc implements Function<Double, Double> {

        final Function<Double, Double> func;
        int count = 0;

        RedPointFunc(final Function<Double, Double> func) {
            this.func = func;
        }

        @Override
        public Double apply(Double x) {
            count++;
            Double fx = func.apply(x);
            graphic.renderPoints.add(new Point(x.floatValue(), fx.floatValue(), new Color((float) Math.log(count) / 3.5f, 0,0,1)));
            return fx;
        }
    }


    @Override
    public void create() {
        methods = new HashMap<>();
        batch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        graphicRenderer = new ShapeRenderer();
        stage = new Stage();
        textureRegionDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sources/memkek.jpg"))));
        RedPointFunc func = new RedPointFunc(target);

        graphic = new Graphic(graphicRenderer, x -> target.apply(x.doubleValue()).floatValue());
        graphic.setBounds(100, 100, 800, 800);
        stage.addActor(graphic);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(graphic);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        graphicRenderer.begin(ShapeRenderer.ShapeType.Filled);
        graphicRenderer.setColor(Color.BLACK);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        graphicRenderer.end();
        batch.end();
    }

    @Override
    public void dispose() {
        stage.dispose();
        bitmapFont.dispose();
        batch.dispose();
        graphicRenderer.dispose();
    }
}
