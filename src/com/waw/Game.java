package com.waw;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.waw.accessories.weapon.magic.MagicWand;
import com.waw.accessories.weapon.melee.Axe;
import com.waw.accessories.weapon.range.Bow;
import com.waw.activities.military.Archer;
import com.waw.activities.military.Military;
import com.waw.activities.military.Swordman;
import com.waw.activities.military.Wizard;
import com.waw.activities.military.exceptions.NoCurrentWeaponException;
import com.waw.persons.Elf;
import com.waw.persons.Human;
import com.waw.persons.Orc;
import com.waw.persons.Person;
import com.waw.persons.exceptions.AccessoryException;
import com.waw.persons.exceptions.ActivityException;
import com.waw.persons.exceptions.InvalidAccessoryException;

public class Game {

	public static void main(String[] args) {
		
//		scenario1();
//		scenario2();
//		scenario3();
		scenario4();
	}
	
	public static void scenario1() {

		// Civilian 
		
		try {

			Person gor = new Orc("Gor", 120, new Swordman(), null);
//			gor.setActivity(new Builder());
			
			System.out.println(gor);
			
//			gor.setActivity(new Engineer());
//			System.out.println(gor);
			
			gor.addAccessory(new MagicWand());
			gor.setCurrent_accessory(gor.getAccessories().get(0));
			
		} catch (ActivityException | AccessoryException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Fin du sc�nario");
		}
	}

	public static void scenario2() {
		
		try {
			
			Person illidan = new Elf("Illidan", 130, new Archer(), null);
			illidan.addAccessory(new Bow());
			
			Person thrall = new Orc("Thrall", 100, new Swordman(), null);
			thrall.addAccessory(new Axe());
			
			Person medivh = new Human("Medivh", 80, new Wizard(), null);
			medivh.addAccessory(new MagicWand());
			
			List<Person> fighters = new ArrayList<Person>(Arrays.asList(
					illidan, thrall, medivh
					));
			Collections.shuffle(fighters);
			
			do {
				Collections.shuffle(fighters);
				fight(fighters);
			} while (areSeveralFightersAlive(fighters));

		} catch (ActivityException | NoCurrentWeaponException| AccessoryException  e) {
			
			e.printStackTrace();
			System.err.println();
			System.err.println("Scenario cannot go further...");
			
		} finally {
			System.out.println("Fin du sc�nario");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void fight(List<Person> fighters) throws NoCurrentWeaponException {

		for (Iterator iterator = fighters.iterator(); iterator.hasNext();) {
			
			Person current = (Person) iterator.next();
			
			if (!iterator.hasNext()) break;
			
			Person enemy = (Person) iterator.next();
			Military activity = (Military)current.getActivity();
			
			try {
				activity.attack(current, enemy);
			} catch (NoCurrentWeaponException e) {
				throw new NoCurrentWeaponException(current);
			}
			
			if (!enemy.isAlive()) {
				System.out.println(enemy.getName() + " fought well ! But died...");
				iterator.remove();
				System.out.println("Remaining fighters :");
				fighters.forEach((p) -> System.out.println(p.getName() + " "));
				System.out.println();
			}
		}
	}
	
	public static boolean areSeveralFightersAlive(List<Person> fighters) {
		return fighters.size() > 1;
	}
	
	public static void scenario3() {
		
		try (
				ObjectOutputStream oos = new ObjectOutputStream(
						new BufferedOutputStream(
								new FileOutputStream("resources/heroes.txt")));
		) {
			
			Person thrall = new Orc("Thrall", 120, new Swordman(), null);
			thrall.addAccessory(new Axe());
			
			Person medhiv = new Human("Medhiv", 80, new Wizard(), null);
			medhiv.addAccessory(new MagicWand());
			
			Person illidan = new Elf("Illidan", 150, new Archer(), null);
			illidan.addAccessory(new Bow());
			
			List<Person> heroes = new ArrayList<Person>();
			heroes.add(thrall);
			heroes.add(medhiv);
			heroes.add(illidan);
			
			for (Person person : heroes) {
				oos.writeObject(person);
			}
			oos.close();
			
		} catch (InvalidAccessoryException | ActivityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void scenario4() {

		List<Person> heroes = new ArrayList<Person>();

		try (ObjectInputStream ois = new ObjectInputStream(
										new BufferedInputStream(
											new FileInputStream("resources/heroes.txt")));
				) 
		
		{

			while (true)
				heroes.add((Person)ois.readObject());
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.io.EOFException e) {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Person person : heroes) {
			System.out.println(person);
		}
		
	}
}
