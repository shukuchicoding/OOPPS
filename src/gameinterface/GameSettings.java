package gameinterface;

public abstract class GameSettings {
	// Goku stats
	public float gokuSpeedX = 5.f;
	
	// MaBu stats
	public float mabuPosX = 650.f;
	public float mabuPosY = 20.f;
	public float mabuSpeedY = 0.2f;
	public int mabuDirectionY = 1;
	public int mabuHP = 3;
	
	// MaBu bullet stats
	public int mabuShootingPeriod = 5000; // milisecond
	public float mabuBulletSpeed = 7.f;
}
