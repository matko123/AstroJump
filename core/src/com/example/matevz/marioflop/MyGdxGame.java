package com.example.matevz.marioflop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;

import java.util.Random;



public class MyGdxGame extends ApplicationAdapter {




	SpriteBatch batch;
	Texture background;
	Texture gameover;
	Texture birds;
	Texture komet;
	Texture srca;

	private Music music;
	private Music jump;

	ShapeRenderer shapeRenderer;





	float AstronavtPozicija = 0;
	float velocity = 0;

	int score = 0;

	BitmapFont font;

	int StanjeIgre = 0;
	float gravity = 1f;
	int stevec_trkov = 0;

	float delay = 2.5f;
	float delay1 = 2f;
	float time = 0f;
	int sekunde;


	int stevecObjekt = 6;


	float[] tubeOffset = new float[stevecObjekt];



	Circle[] kometModel;
	Rectangle AstronavtModel;


	Random randomGenerator;



	float[] y1 = new float[stevecObjekt];
	float[] x1 = new float[stevecObjekt];


	int flapState = 0;
	Texture astroPolje[];

	float AstronavtPozicija1 = 0;
	int prenehaj = 0;

	@Override
	public void create ()
	{



		music = Gdx.audio.newMusic(Gdx.files.internal("PixelSpace.mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();

		jump = Gdx.audio.newMusic(Gdx.files.internal("jump5.ogg"));
		jump.setVolume(0.05f);



		batch = new SpriteBatch();
		background = new Texture("ozadje5.png");
		gameover = new Texture("Gameover12.png");
		birds = new Texture("astronavt13.png");
		srca = new Texture("4heart.png");
		komet = new Texture("Komet2.png");

		shapeRenderer = new ShapeRenderer();
		AstronavtModel = new Rectangle();
		kometModel = new Circle[stevecObjekt];



		randomGenerator = new Random();
		astroPolje = new Texture[3];
		astroPolje[0] = new Texture("astronavt13.png");
		astroPolje[1] = new Texture("praznina.png");
		astroPolje[2] = new Texture("astronavt10.png");

		startGame();





	}




	public void startGame() {



		AstronavtPozicija1 = Gdx.graphics.getHeight() / 2 - astroPolje[0].getHeight() / 2;

		AstronavtPozicija = Gdx.graphics.getHeight() / 2 - birds.getHeight() / 2; //POSTAVI ASTRONAVTA NA SREDINO

		for (int i = 0; i < stevecObjekt; i++) {
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getWidth());

			//NOVO*****************************************************************
			float visina = (randomGenerator.nextFloat()* (5f - 1f) + 1f);
			float sirina = (randomGenerator.nextFloat() * (1f - 0.5f) + 0.5f);


			x1[i] = Gdx.graphics.getWidth() / visina -   i * Gdx.graphics.getWidth() * 1/2;;
			y1[i] = Gdx.graphics.getHeight() * sirina + tubeOffset[i];

			//NOVO******************************************************************





			kometModel[i] = new Circle();

			srca = new Texture("4heart.png");
			font = new BitmapFont();
			font.setColor(Color.WHITE);
			font.getData().setScale(9);

		}

	}



	@Override
	public void render ()
	{




		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(srca, srca.getWidth() * 0.05f, Gdx.graphics.getHeight() - srca.getHeight() * 1.5f, 250, 60);

		float nakljucnaY = randomGenerator.nextFloat() * (1.2f * Gdx.graphics.getHeight() - 1f * Gdx.graphics.getHeight()) + 1f * Gdx.graphics.getHeight();

		if (StanjeIgre == 1) {

			time += Gdx.graphics.getDeltaTime();

			sekunde = Math.round(time);




			if (Gdx.input.isTouched())
			{
				birds = astroPolje[2];
				//birds = new Texture("astronavt10.png");
				jump.play();
				velocity = -10;
			}
			else
			{
				birds = astroPolje[0];
				//birds = new Texture("astronavt13.png");
			}


			for (int i = 0; i < stevecObjekt; i++) {

				if (x1[i] < - komet.getWidth())
				{
					x1[i] = x1[i] + nakljucnaY;
				}
				if (y1[i] < - komet.getHeight())
				{
					y1[i] = y1[i] + nakljucnaY;
				}
				else
				{
					x1[i] = x1[i] - 4f;

					y1[i] = y1[i] - 0.5f;
				}

				batch.draw(komet, x1[i], y1[i] );

				//batch.draw(komet, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - komet.getHeight() + tubeOffset[i] + padecY );

				kometModel[i] = new Circle(x1[i] + komet.getWidth() / 2, y1[i] + komet.getHeight() / 2, komet.getWidth() / 1.95f);

			}



			if ((AstronavtPozicija > -100) && (AstronavtPozicija < Gdx.graphics.getHeight()-100 ))
			{

				velocity = velocity + gravity;
				AstronavtPozicija -= velocity;

			} else
			{

				StanjeIgre = 2;

			}

		} else if (StanjeIgre == 0)
		{

			birds = astroPolje[0];
			//birds = new Texture("astronavt13.png");
			batch.draw(birds, Gdx.graphics.getWidth() / 2 - birds.getWidth() / 2, AstronavtPozicija);

			if (Gdx.input.justTouched())
			{
				StanjeIgre = 1;
			}

		} else if (StanjeIgre == 2) {

			batch.draw(gameover, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			//	birds = new Texture("praznina.png");
			birds = astroPolje[1];


			if (Gdx.input.justTouched() ) {
				Timer.schedule(new Timer.Task() {
					@Override
					public void run()
					{
						stevec_trkov = 0;
						score = 0;
						velocity = 0;
						sekunde = 0;
						time = 0;
						StanjeIgre = 0;
						startGame();

					}
				}, delay);
			}





		}


		/* if( prenehaj == 1 ) {
			if (flapState == 0) {
				birds = astroPolje[0];
				flapState = 1;

			} else {
				birds = astroPolje[1];
				flapState = 0;
			}
		}*/

















		//batch.draw(astroPolje[flapState], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		batch.draw(birds, Gdx.graphics.getWidth() / 2 - birds.getWidth() / 2, AstronavtPozicija);

		font.draw(batch, String.valueOf(sekunde), 55, 230);

		AstronavtModel.set(Gdx.graphics.getWidth() / 2 - birds.getWidth() / 2+33, AstronavtPozicija + 100, birds.getWidth()-40, birds.getHeight() - 105);



		//	shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//	shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - birds.getWidth() / 2+33, birdY + 100, birds.getWidth()-40, birds.getHeight() - 105);





		for (int i = 0; i < stevecObjekt; i++) {


			//shapeRenderer.circle(x1[i] + komet.getWidth() / 2, y1[i] + komet.getHeight() / 2, komet.getWidth() / 1.9f);

			if (Intersector.overlaps(kometModel[i], AstronavtModel) && stevec_trkov == 0)
			{

				prenehaj = 1;
				srca = new Texture("35heart.png");
				Timer.schedule(new Timer.Task() {
					@Override
					public void run()
					{
						stevec_trkov = 1;
						prenehaj = 0;


					}
				}, delay1);
				break;
			}
			else if (Intersector.overlaps(kometModel[i], AstronavtModel) && stevec_trkov == 1)
			{
				prenehaj = 1;
				srca = new Texture("3heart.png");
				Timer.schedule(new Timer.Task() {
					@Override
					public void run()
					{
						stevec_trkov = 2;
						prenehaj = 0;
					}
				}, delay1);
				break;
			}
			else if (Intersector.overlaps(kometModel[i], AstronavtModel) && stevec_trkov == 2)
			{
				prenehaj = 1;
				srca = new Texture("251heart.png");
				Timer.schedule(new Timer.Task() {
					@Override
					public void run()
					{
						stevec_trkov = 3;
						prenehaj = 0;
					}
				}, delay1);
				break;
			}
			else if (Intersector.overlaps(kometModel[i], AstronavtModel) && stevec_trkov == 3)
			{
				prenehaj = 1;
				srca = new Texture("21heart.png");
				Timer.schedule(new Timer.Task() {
					@Override
					public void run()
					{
						stevec_trkov = 4;
						prenehaj = 0;
					}
				}, delay1);
				break;
			}
			else if (Intersector.overlaps(kometModel[i], AstronavtModel) && stevec_trkov == 4)
			{
				prenehaj = 1;
				srca = new Texture("151heart.png");
				Timer.schedule(new Timer.Task() {
					@Override
					public void run()
					{
						stevec_trkov = 5;
						prenehaj = 0;
					}
				}, delay1);
				break;
			}
			else if (Intersector.overlaps(kometModel[i], AstronavtModel) && stevec_trkov == 5)
			{
				prenehaj = 1;
				srca = new Texture("10heart.png");
				Timer.schedule(new Timer.Task() {
					@Override
					public void run()
					{
						stevec_trkov = 6;
						prenehaj = 0;
					}
				}, delay1);
				break;
			}
			else if (Intersector.overlaps(kometModel[i], AstronavtModel) && stevec_trkov == 6)
			{
				prenehaj = 1;
				srca = new Texture("05heart.png");
				Timer.schedule(new Timer.Task() {
					@Override
					public void run()
					{
						stevec_trkov = 7;
						prenehaj = 0;
					}
				}, delay1);
				break;
			}
			else  if (Intersector.overlaps(kometModel[i], AstronavtModel) && stevec_trkov == 7)
			{

				StanjeIgre = 2;
				break;
			}



		}

		batch.end();

		//	shapeRenderer.end();



	}


}