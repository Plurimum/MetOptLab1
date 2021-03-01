package mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mygdx.methods.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    TextureRegionDrawable textureRegionDrawable;
    BitmapFont bitmapFont;
    ShapeRenderer graphicRenderer;
    Stage stage;
    TextField textOne;
    java.util.List<Point> renderPoints;
    Map<Button, Method> methods;
    Function<Double, Double> target = x -> x - Math.log(x);

    static class Point {
        Point(double x, double y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

        double x;
        double y;
        Color color;

    }
    int count = 0;
    class RedPointFunc implements Function<Double, Double> {

        final Function<Double, Double> func;

        RedPointFunc(final Function<Double, Double> func) {
            this.func = func;
        }

        @Override
        public Double apply(Double x) {
            count++;
            Double fx = func.apply(x);
            renderPoints.add(new Point(x, fx, new Color((float) count / 10, 0,0,1)));
            return fx;
        }
    }

    final int SCALE = 200;
    final int ZERO_X = 100;
    final int ZERO_Y = 100;
    final float MAX_VALUE = 4.5f;
    private void drawPoint(ShapeRenderer renderer, float x, float y, float radius) {
        if (x > MAX_VALUE || y > MAX_VALUE) {
            return;
        }
        renderer.circle(x * SCALE + ZERO_X, y * SCALE + ZERO_Y, radius);
    }

    @Override
    public void create() {
        renderPoints = new ArrayList<>();
        methods = new HashMap<>();
        batch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        graphicRenderer = new ShapeRenderer();
        stage = new Stage();
        textureRegionDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sources/memkek.jpg"))));
        Function<Double, Double> func = new RedPointFunc(target);
        int index= 0 ;
        for (Method m : Algorithms.methodList(func)) {
            Button b = new ImageButton(textureRegionDrawable);
            b.setBounds(1200f, (float) (900 - index * 150), 100f, 100f);
            b.addListener( new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    renderPoints.clear();
                    count = 0;
                    methods.get(b).findMin(0.5, 4, 1e-3);
                };
            });
            index++;
            methods.put(b, m);
        }
        stage = new Stage(new ScreenViewport());
        for (Button b : methods.keySet())
            stage.addActor(b);
        Gdx.input.setInputProcessor(stage);
        textOne = new TextField("1", new TextField.TextFieldStyle(bitmapFont, Color.BLACK, null, null, null));
        textOne.setX(ZERO_X + SCALE - 3);
        textOne.setY(ZERO_Y - 30);
        stage.addActor(textOne);

    }

    private void drawAxes(ShapeRenderer renderer) {
        renderer.setColor(Color.BLACK);
        renderer.rectLine(ZERO_X - 50, ZERO_Y, ZERO_X + MAX_VALUE * SCALE, ZERO_Y, 2);
        renderer.rectLine(ZERO_X, ZERO_Y - 50, ZERO_X, ZERO_Y + MAX_VALUE * SCALE, 2);
        for (float i = 0.5f; i <= MAX_VALUE; i += 0.5) {
            renderer.rectLine(ZERO_X + i * SCALE, ZERO_Y - 10, ZERO_X + i * SCALE, ZERO_Y + 10, 2);
            renderer.rectLine(ZERO_X - 10, ZERO_Y + i * SCALE, ZERO_X + 10, ZERO_Y + i * SCALE, 2);
        }
    }

    private void drawGraphic(ShapeRenderer renderer, Function<Double, Double> func) {
        renderer.setColor(Color.BLACK);
        float step = 0.0001f;
        for (float i = 0.003f; i < MAX_VALUE; i += step) {
            if (i >= 0.5f && i <= 4.0) {
                renderer.setColor(Color.PURPLE);
            } else {
                renderer.setColor(Color.BLACK);
            }
            drawPoint(renderer, i, func.apply((double) i).floatValue(), 1.5f);
        }
        for (Point p : renderPoints) {
            renderer.setColor(p.color);
            drawPoint(renderer, (float) p.x, (float) p.y, 6f);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        graphicRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawAxes(graphicRenderer);
        drawGraphic(graphicRenderer, target);
        graphicRenderer.end();
        batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        bitmapFont.dispose();
        batch.dispose();
    }
}
